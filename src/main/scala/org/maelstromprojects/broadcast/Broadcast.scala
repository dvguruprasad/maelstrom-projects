package org.maelstromprojects.broadcast

import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

import scala.io.StdIn.readLine

object Broadcast {
  var str: String = _
  var node: Node = new Node()

  private def processMessage(input: Json, inputLine: String): Unit =
    input
      .hcursor
      .downField("body")
      .downField("type")
      .as[String] match {
      case Right(messageType) if messageType == "init" =>
        val initResponse =
          node.initialize(decode[InitMessage](inputLine)).asJson
        System
          .err
          .println(
            s"Node initialized successfully. Responding with $initResponse"
          )
        respond(initResponse)
      case Right(messageType) if messageType == "topology" =>
        node.topology(input)
      case Right(messageType) =>
        System.err.println(s"Cannot handle message type $messageType yet!")
      case Left(error) =>
        System.err.println(s"Could not parse input message: $error")
    }

  private def respond(initResponse: Json) = {
    System.out.println(initResponse)
    System.out.flush()
  }

  def run(): Unit =
    while ({ str = readLine(); str != null }) {
      System.err.println(s"Received $str")
      parse(str) match {
        case Right(input) => processMessage(input, str)
        case Left(error) =>
          System.err.println(s"Could not parse input message: $error")
      }
    }
}
