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
  @JsonCodec case class TopologyMessage(
      src: String,
      dest: String,
      body: TopologyBody
    )
  @JsonCodec case class TopologyBody(
      `type`: String,
      topology: Map[String, List[String]],
      msg_id: Int
    )
  @JsonCodec case class TopologyResponseMessage(
      src: String,
      dest: String,
      body: TopologyResponseBody
    )
  @JsonCodec case class TopologyResponseBody(
      `type`: String,
      in_reply_to: Int,
      msg_id: Int
    )
  @JsonCodec case class BroadcastMessage(
      src: String,
      dest: String,
      body: BroadcastBody
    )
  @JsonCodec case class BroadcastBody(
      `type`: String,
      message: Int,
      msg_id: Int
    )
  @JsonCodec case class BroadcastResponseMessage(
      src: String,
      dest: String,
      body: BroadcastResponseBody
    )
  @JsonCodec case class BroadcastResponseBody(
      `type`: String,
      in_reply_to: Int,
      msg_id: Int
    )
  @JsonCodec case class ReadMessage(
      src: String,
      dest: String,
      body: ReadBody
    )
  @JsonCodec case class ReadBody(
      `type`: String,
      msg_id: Int
    )
  @JsonCodec case class ReadResponseMessage(
      src: String,
      dest: String,
      body: ReadResponseBody
    )
  @JsonCodec case class ReadResponseBody(
      `type`: String,
      messages: Seq[Int],
      in_reply_to: Int,
      msg_id: Int
    )
}
