package org.maelstromprojects.echo

import io.circe.generic.JsonCodec

package object messages {
  @JsonCodec sealed case class EchoMessage(
      src: String,
      dest: String,
      body: EchoMessageBody
    )
  @JsonCodec sealed case class EchoMessageBody(
      `type`: String,
      in_reply_to: Option[Int],
      msg_id: Int,
      echo: Option[String]
    )
}
