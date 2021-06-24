package org.maelstromprojects.broadcast

import io.circe
import io.circe.Json

class Node {
  var nodeId: String = null
  var msgId: Int = 0

  def initialize(
      initMessageE: Either[circe.Error, InitMessage]
    ): InitResponseMessage =
    initMessageE match {
      case Right(initMessage) => {
        this.nodeId = initMessage.body.node_id
        msgId += 1
        InitResponseMessage(
          nodeId,
          initMessage.src,
          InitResponseMessageBody("init_ok", initMessage.body.msg_id, msgId)
        )
      }
      case Left(error) =>
        System.err.println(s"Could not process init message: $error")
        throw error
    }

  def topology(input: Json) = ???

}
