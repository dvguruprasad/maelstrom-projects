package org.maelstromprojects

import io.circe.generic.JsonCodec

package object broadcast {
  @JsonCodec case class InitMessage(
      src: String,
      dest: String,
      body: InitMessageBody
    )
  @JsonCodec case class InitMessageBody(
      `type`: String,
      node_id: String,
      node_ids: List[String],
      msg_id: Int
    )
  @JsonCodec case class InitResponseMessage(
      src: String,
      dest: String,
      body: InitResponseMessageBody
    )
  @JsonCodec case class InitResponseMessageBody(
      `type`: String,
      in_reply_to: Int,
      msg_id: Int
    )
}
