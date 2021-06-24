package org.maelstromprojects.echo

import org.maelstromprojects.echo.messages._
import io.circe.parser.parse
import io.circe.syntax.EncoderOps
import io.circe.{ACursor, Json}

import scala.io.StdIn.readLine

object Echo {
  var str: String = _
  var messageId = 10
  var nodeId = ""

  def run(): Unit = {
    while ({
      str = readLine()
      str != null
    }){
      System.err.println(s"Received $str")
      processMessage(parse(str).getOrElse(Json.Null)) match {
        case Right(response) => respond(response.asJson.noSpaces)
        case Left(error) =>
          System.err.println(s"Could not process message: $error")
      }
      messageId += 1
    }
  }

  private def processMessage(message: Json) = {
    val body = message.hcursor.downField("body")
    for {
      typeField <- body.downField("type").as[String]
      sourceMessageId <- body
        .downField("msg_id")
        .as[Int]
      src <- message.hcursor.downField("src").as[String]
    } yield response(typeField, src, sourceMessageId, body)
  }

  def response(
      `type`: String,
      src: String,
      sourceMessageId: Int,
      body: ACursor
    ): EchoMessage =
    `type` match {
      case "init" =>
        nodeId = body.downField("node_id").as[String].getOrElse("")
        echoMessage(src, sourceMessageId, "init_ok", None)
      case "echo" =>
        echoMessage(
          src,
          sourceMessageId,
          "echo_ok",
          Some(body.downField("echo").as[String].getOrElse(""))
        )
    }

  private def echoMessage(
      src: String,
      sourceMessageId: Int,
      `type`: String,
      echoMessage: Option[String]
    ) =
    EchoMessage(
      nodeId,
      src,
      EchoMessageBody(
        `type`,
        Some(sourceMessageId),
        messageId,
        echoMessage
      )
    )

  private def respond(outputMessage: String): Unit = {
    System.err.println(s"Responding with $outputMessage")
    System.out.println(outputMessage)
    System.out.flush()
  }
}
