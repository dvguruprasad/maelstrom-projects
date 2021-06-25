package org.maelstromprojects.broadcast

import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

import scala.io.StdIn.readLine

object Broadcast {
  private val node = new Node()

  var inputString: String = _

  def processMessage(input: Json, inputString: String): Unit =
    input
      .hcursor
      .downField("body")
      .downField("type")
      .as[String] match {
      case Right(messageType) if messageType == "init" =>
        decode[InitMessage](inputString) match {
          case Right(message) =>
            respond(node.initialize(message).asJson.noSpaces)
          case Left(error) =>
            System.err.println(s"Could not process init request: $error")
        }
      case Right(messageType) if messageType == "topology" =>
        decode[TopologyMessage](inputString) match {
          case Right(message) =>
            respond(node.topology(message).asJson.noSpaces)
          case Left(error) =>
            System.err.println(s"Could not process topology request: $error")
        }
      case Right(messageType) if messageType == "broadcast" =>
        decode[BroadcastMessage](inputString) match {
          case Right(message) =>
            respond(node.broadcast(message).asJson.noSpaces)
          case Left(error) =>
            System.err.println(s"Could not process broadcast request: $error")
        }
      case Right(messageType) if messageType == "read" =>
        decode[ReadMessage](inputString) match {
          case Right(message) =>
            respond(node.read(message).asJson.noSpaces)
          case Left(error) =>
            System.err.println(s"Could not process read request: $error")
        }
      case Right(messageType) =>
        System.err.println(s"Unknown message type $messageType")
      case Left(error) =>
        System.err.println(s"Could not parse input message: $error")
    }

  private def respond(response: String): Unit = {
    System.err.println(s"Responding with $response")
    System.out.println(response)
  }

  def run(): Unit =
    while ({ inputString = readLine(); inputString != null }) {
      System.err.println(s"Received $inputString")
      parse(inputString) match {
        case Right(input) => processMessage(input, inputString)
        case Left(error) =>
          System.err.println(s"Could not parse input message: $error")
      }
    }
}
