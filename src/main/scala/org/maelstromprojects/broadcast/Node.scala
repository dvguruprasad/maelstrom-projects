package org.maelstromprojects.broadcast
import io.circe.syntax._


class Node() {
  var nodeId = ""
  var neighbors: Seq[String] = Seq()
  var messages: Seq[Int] = Seq()
  var messageId: Int = 9

  def initialize(message: InitMessage): InitResponseMessage = {
    this.nodeId = message.body.node_id
    InitResponseMessage(
      this.nodeId,
      message.src,
      InitResponseMessageBody("init_ok", message.body.msg_id, nextMessageId)
    )
  }

  def topology(message: TopologyMessage): TopologyResponseMessage = {
    this.neighbors = message.body.topology(nodeId)
    System.err.println(s"[$nodeId] My neighbors are: $neighbors")
    TopologyResponseMessage(
      this.nodeId,
      message.src,
      TopologyResponseBody("topology_ok", message.body.msg_id, nextMessageId)
    )
  }

  def broadcast(message: BroadcastMessage): BroadcastResponseMessage = {
    if (!messages.contains(message.body.message)) {
      messages = messages :+ message.body.message
      neighbors.foreach { (neighbor: String) =>
        System
          .out
          .println(
            BroadcastMessage(
              this.nodeId,
              neighbor,
              BroadcastBody(
                "broadcast",
                message.body.message,
                nextMessageId
              )
            ).asJson.noSpaces
          )
      }
    }
    BroadcastResponseMessage(
      this.nodeId,
      message.src,
      BroadcastResponseBody("broadcast_ok", message.body.msg_id, nextMessageId)
    )
  }

  def read(message: ReadMessage): ReadResponseMessage =
    ReadResponseMessage(
      this.nodeId,
      message.src,
      ReadResponseBody("read_ok", messages, message.body.msg_id, nextMessageId)
    )

  def nextMessageId: Int = {
    messageId += 1
    messageId
  }
}
