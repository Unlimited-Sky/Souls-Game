package gameServer

import akka.actor.ActorRef

object Messages {
  case class GameStateUpdate()
  case class MetaGameStateUpdate()

  case class StartGame()
  case class EndGame()

  case class TextMessage(msg: String)

  case class PlayerConnect(actor: ActorRef, username: String)
  case class PlayerDisconnect(actor: ActorRef, username: String)

  case class PeerConnect(actor: ActorRef, username: String)
  case class PeerDisconnect(actor: ActorRef, username: String)
}
