package org.maelstromprojects.broadcast

import org.scalatest.flatspec._
import org.scalatest.matchers._

class NodeSpec extends AnyFlatSpec with should.Matchers {

  "Broadcast" should "handle init and then topology from client c2" in {
    val node = new Node()
    val response: InitResponseMessage = node.initialize(
      InitMessage(
        "c2",
        "n3",
        InitMessageBody("init", "n3", List("n1", "n2", "n3"), 1)
      )
    )
    response should be(
      InitResponseMessage("n3", "c2", InitResponseMessageBody("init_ok", 1, 10))
    )
    val topologyResponse = node.topology(
      TopologyMessage(
        "c2",
        "n3",
        TopologyBody(
          "topology",
          Map("n3" -> List("n1", "n2"), "n1" -> List("n3"), "n2" -> List("n3")),
          2
        )
      )
    )
    topologyResponse should be(
      TopologyResponseMessage(
        "n3",
        "c2",
        TopologyResponseBody("topology_ok", 2, 11)
      )
    )
  }
}
