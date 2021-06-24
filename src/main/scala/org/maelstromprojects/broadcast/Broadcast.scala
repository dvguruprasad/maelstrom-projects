package org.maelstromprojects.broadcast

import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

import scala.io.StdIn.readLine

object Broadcast {
  var inputString: String = _
  var messageId: Int = 9

  def nextMessageId: Int = {
    messageId += 1
    messageId
  }
  def processInit(message: InitMessage): InitResponseMessage =
    InitResponseMessage(
      message.dest,
      message.src,
      InitResponseMessageBody("init_ok", message.body.msg_id, nextMessageId)
    )

  def processTopology(message: TopologyMessage): TopologyResponseMessage = {
    TopologyResponseMessage(message.dest, message.src, TopologyResponseBody("topology_ok", message.body.msg_id, nextMessageId))
  }

  def processMessage(input: Json, inputString: String): Unit =
    input
      .hcursor
      .downField("body")
      .downField("type")
      .as[String] match {
      case Right(messageType) if messageType == "init" =>
        decode[InitMessage](inputString) match {
          case Right(message) =>
            val response = processInit(message).asJson.noSpaces
            System.err.println(s"Responding with $response")
            System.out.println(response)
          case Left(error) =>
            System.err.println(s"Could not process init request: $error")
        }
      case Right(messageType) if messageType == "topology" =>
        decode[TopologyMessage](inputString) match {
          case Right(message) =>
            val response = processTopology(message).asJson.noSpaces
            System.err.println(s"Responding with $response")
            System.out.println(response)
          case Left(error) =>
            System.err.println(s"Could not process topology request: $error")
        }
      case Right(messageType) =>
        System.err.println(s"Cannot handle message type $messageType yet!")
      case Left(error) =>
        System.err.println(s"Could not parse input message: $error")
    }

  def run(): Unit =
    while ({ inputString = readLine(); inputString != null }) {
      System.err.println(s"Received $inputString")
      parse(inputString) match {
        case Right(input) => processMessage(input, inputString)
        case Left(error) =>
          System.err.println(s"Could not parse input message: $error")
      }
    }
}
