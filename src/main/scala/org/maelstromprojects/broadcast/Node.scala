package org.maelstromprojects.broadcast

class Node() {
  var nodeId = ""
  var neighbors: Seq[String] = Seq()
  var messageId: Int = 9

  def initialize(message: InitMessage): InitResponseMessage = {
    this.nodeId = message.body.node_id
    InitResponseMessage(
      message.dest,
      message.src,
      InitResponseMessageBody("init_ok", message.body.msg_id, nextMessageId)
    )
  }

  def topology(message: TopologyMessage): TopologyResponseMessage = {
    this.neighbors = message.body.topology(nodeId)
    System.err.println(s"[$nodeId] My neighbors are: $neighbors")
    TopologyResponseMessage(
      message.dest,
      message.src,
      TopologyResponseBody("topology_ok", message.body.msg_id, nextMessageId)
    )
  }

  def nextMessageId: Int = {
    messageId += 1
    messageId
  }
}
