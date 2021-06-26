package org.maelstromprojects.broadcast

import io.circe.parser._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}

import scala.io.StdIn.readLine

trait ProcessMessage[T, U] {
  def process(obj: T): U
}

object Broadcast {
  private val node = new Node()

  private var inputString: String = _

  def processMessage(input: Json, inputString: String): Unit =
    input
      .hcursor
      .downField("body")
      .downField("type")
      .as[String] match {
      case Right(messageType) if messageType == "init" =>
        processMessage[InitMessage, InitResponseMessage](inputString)
      case Right(messageType) if messageType == "topology" =>
        processMessage[TopologyMessage, TopologyResponseMessage](inputString)
      case Right(messageType) if messageType == "broadcast" =>
        processMessage[BroadcastMessage, BroadcastResponseMessage](inputString)
      case Right(messageType) if messageType == "read" =>
        processMessage[ReadMessage, ReadResponseMessage](inputString)
      case Right(messageType) =>
        System.err.println(s"Unknown message type $messageType")
      case Left(error) =>
        System.err.println(s"Could not parse input message: $error")
    }

  private def processMessage[T, U](
      inputString: String
    )(implicit
      decoder: Decoder[T],
      encoder: Encoder[U],
      monoid: ProcessMessage[T, U]
    ): Unit =
    decode[T](inputString) match {
      case Right(message) =>
        respond(monoid.process(message).asJson.noSpaces)
      case Left(error) =>
        System.err.println(s"Could not process request: $error")
    }

  implicit private val initMessageMonoid
      : ProcessMessage[InitMessage, InitResponseMessage] =
    (obj: InitMessage) => {
      node.initialize(obj)
    }

  implicit private val topologyMessageMonoid
      : ProcessMessage[TopologyMessage, TopologyResponseMessage] =
    (obj: TopologyMessage) => {
      node.topology(obj)
    }

  implicit private val broadcastMessageMonoid
      : ProcessMessage[BroadcastMessage, BroadcastResponseMessage] =
    (obj: BroadcastMessage) => {
      node.broadcast(obj)
    }

  implicit private val readMessageMonoid
      : ProcessMessage[ReadMessage, ReadResponseMessage] =
    (obj: ReadMessage) => {
      node.read(obj)
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
