package echo

import io.circe.generic.JsonCodec

package object messages {
  @JsonCodec case class InitMessage(
      src: String,
      dest: String,
      body: InitMessageBody
    )

  @JsonCodec case class InitMessageBody(
      `type`: String,
      node_id: String,
      msg_id: Int
    )

  @JsonCodec case class InitMessageResponse(
      src: String,
      dest: String,
      body: InitMessageResponseBody
    )

  @JsonCodec case class InitMessageResponseBody(
      `type`: String,
      in_reply_to: Int,
      msg_id: Int
    )

  @JsonCodec case class EchoMessage(
      src: String,
      dest: String,
      body: EchoMessageBody
    )

  @JsonCodec case class EchoMessageBody(
      `type`: String,
      echo: String,
      msg_id: Int
    )

  @JsonCodec case class EchoMessageResponse(
      src: String,
      dest: String,
      body: EchoMessageResponseBody
    )

  @JsonCodec case class EchoMessageResponseBody(
      `type`: String,
      in_reply_to: Int,
      msg_id: Int,
      echo: String
    )
}
