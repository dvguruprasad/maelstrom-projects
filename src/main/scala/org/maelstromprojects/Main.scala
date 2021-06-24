package org.maelstromprojects

import org.maelstromprojects.broadcast.Broadcast
import org.maelstromprojects.echo.Echo

object Main extends App {
  if(args.isEmpty) {
    System.err.println("No command passed. Running echo server")
    Echo.run()
  }
  if(args.length > 0) {
    args(0) match {
      case "echo" => Echo.run()
      case "broadcast" => Broadcast.run()
      case command => System.err.println(s"Invalid command $command")
    }
  }
}
