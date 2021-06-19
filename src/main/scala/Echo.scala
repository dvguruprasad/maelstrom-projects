import io.circe._
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.syntax._

import scala.io.StdIn.readLine

object Echo extends App {
  @JsonCodec case class InitMessage(src: String, dest: String, body: InitMessageBody)

  @JsonCodec case class InitMessageBody(`type`: String, node_id: String, msg_id: Int)

  @JsonCodec case class InitMessageResponse(src: String, dest: String, body: InitMessageResponseBody)

  @JsonCodec case class InitMessageResponseBody(`type`: String, in_reply_to: Int, msg_id: Int)

  @JsonCodec case class EchoMessage(src: String, dest: String, body: EchoMessageBody)

  @JsonCodec case class EchoMessageBody(`type`: String, echo: String, msg_id: Int)

  @JsonCodec case class EchoMessageResponse(src: String, dest: String, body: EchoMessageResponseBody)

  @JsonCodec case class EchoMessageResponseBody(`type`: String, in_reply_to: Int, msg_id: Int, echo: String)

  var str = readLine()
  var messageId = 10
  var nodeId = ""
  while (str != null) {
    System.err.println(s"Received $str")
    val message = parse(str).getOrElse(Json.Null)
    val cursor = message.hcursor
    val body = cursor.downField("body")
    body.downField("type").as[String].map {
      case "init" =>
        nodeId = asString(body, "node_id")
        respond(InitMessageResponse(nodeId, asString(cursor, "src"),
          InitMessageResponseBody("init_ok", body.downField("msg_id").as[Int].getOrElse(0), messageId))
          .asJson.noSpaces)
      case "echo" =>
        val echoMessage = body.downField("echo").as[String].getOrElse("")
        respond(EchoMessageResponse(nodeId, cursor.downField("src").as[String].getOrElse(""),
          EchoMessageResponseBody("echo_ok", body.downField("msg_id").as[Int].getOrElse(0), messageId, echoMessage))
          .asJson.noSpaces)

    }
    messageId += 1
    str = readLine()
  }

  private def asString(body: ACursor, node_id: String) = {
    body.downField(node_id).as[String].getOrElse("")
  }

  private def respond(outputMessage: String): Unit = {
    System.err.println(s"Responding with $outputMessage")
    System.out.println(outputMessage)
    System.out.flush()
  }
}
