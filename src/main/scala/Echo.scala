import io.circe._
import io.circe.generic.JsonCodec
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.semiauto._
import scala.io.StdIn.readLine

object Echo extends App {
  @JsonCodec case class InitMessage(src: String, dest: String, body: InitMessageBody)
  @JsonCodec case class InitMessageBody(`type`: String, node_id: String, msg_id: Int)
  @JsonCodec case class InitMessageResponse(src: String, dest: String, body: InitMessageResponseBody)
  @JsonCodec case class InitMessageResponseBody(`type`: String, in_reply_to: Int, msg_id: Int)

  implicit val decodeInitMessageBody: Decoder[InitMessageBody] = deriveDecoder[InitMessageBody]
  implicit val decodeInitMessage: Decoder[InitMessage] = deriveDecoder[InitMessage]

  implicit val decodeInitMessageResponseBody: Decoder[InitMessageResponseBody] = deriveDecoder[InitMessageResponseBody]
  implicit val decodeInitMessageResponse: Decoder[InitMessageResponse] = deriveDecoder[InitMessageResponse]

  var str = readLine()
  var messageId = 10
  var nodeId = ""
  """{"dest":"n1","body":{"type":"init","node_id":"n1","node_ids":["n1"],"msg_id":1},"src":"c0","id":0}"""
  while (str != null) {
    System.err.println(s"Received $str")
    val message = parse(str).getOrElse(Json.Null)
    val cursor = message.hcursor
    cursor.downField("body").downField("type").as[String].getOrElse("") match {
      case "init" => {
        val body = cursor.downField("body")
        nodeId = body.downField("node_id").as[String].getOrElse("")
        val outputMessage: String = InitMessageResponse(nodeId, cursor.downField("src").as[String].getOrElse(""),
          InitMessageResponseBody("init_ok", body.downField("msg_id").as[Int].getOrElse(0), messageId))
          .asJson.noSpaces
        System.err.println(s"Responding with $outputMessage")
        System.out.println(outputMessage)
        System.out.flush()
      }
    }
    str = readLine()
  }
}
