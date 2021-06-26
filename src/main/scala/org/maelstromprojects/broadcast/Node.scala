package org.maelstromprojects.broadcast
import io.circe.syntax._

class Node() {
  var nodeIdOpt: Option[String] = None
  var neighbors: Seq[String] = Seq()
  var messages: Seq[Int] = Seq()
  var messageId: Int = 9

  def initialize(message: InitMessage): InitResponseMessage = {
    this.nodeIdOpt =
      Option[String](message.body.node_id).map(_.trim).filterNot(_.isEmpty)
    this.nodeIdOpt match {
      case Some(nodeId) =>
        InitResponseMessage(
          nodeId,
          message.src,
          InitResponseMessageBody("init_ok", message.body.msg_id, nextMessageId)
        )
      case _ => throw new IllegalArgumentException("Could not initialize node")
    }
  }

  def topology(message: TopologyMessage): TopologyResponseMessage =
    nodeIdOpt match {
      case Some(nodeId) =>
        this.neighbors = message.body.topology(nodeId)
        System.err.println(s"[$nodeIdOpt] my neighbors are: $neighbors")
        TopologyResponseMessage(
          nodeId,
          message.src,
          TopologyResponseBody(
            "topology_ok",
            message.body.msg_id,
            nextMessageId
          )
        )
      case _ => throw new Exception("Node not yet initialized")
    }

  def broadcast(message: BroadcastMessage): BroadcastResponseMessage =
    nodeIdOpt match {
      case Some(nodeId) if messages.contains(message.body.message) =>
        broadcastResponse(message, nodeId)
      case Some(nodeId) =>
        broadcastToNeighbors(nodeId, message)
        broadcastResponse(message, nodeId)
      case _ => throw new Exception("Not not yet initialized")
    }

  private def broadcastResponse(message: BroadcastMessage, nodeId: String) =
    BroadcastResponseMessage(
      nodeId,
      message.src,
      BroadcastResponseBody(
        "broadcast_ok",
        message.body.msg_id,
        nextMessageId
      )
    )

  private def broadcastToNeighbors(
      nodeId: String,
      message: BroadcastMessage
    ): Unit = {
    messages = messages :+ message.body.message
    neighbors.foreach { (neighbor: String) =>
      System
        .out
        .println(
          BroadcastMessage(
            nodeId,
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

  def read(message: ReadMessage): ReadResponseMessage =
    nodeIdOpt match {
      case Some(nodeId) =>
        ReadResponseMessage(
          nodeId,
          message.src,
          ReadResponseBody(
            "read_ok",
            messages,
            message.body.msg_id,
            nextMessageId
          )
        )
      case _ => throw new Exception("Not not yet initialized")
    }

  def nextMessageId: Int = {
    messageId += 1
    messageId
  }
}
