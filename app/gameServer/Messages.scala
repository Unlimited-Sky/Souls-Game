package gameServer

import akka.actor.ActorRef
import scala.collection.mutable.{Queue, Map}
import ecs.Entity
import ecs.events.AEvent
import ecs.components.AComponent

object Messages {
  case class GameStateUpdate()
  case class MetaGameStateUpdate()

  case class StartGame()
  case class EndGame()

  case class TextMessage(msg: String)

  case class PlayerConnect(actor: ActorRef, username: String)
  case class PlayerDisconnect(actor: ActorRef, username: String)
  case class PlayerUIDMessage(entity: Entity)

  case class PeerConnect(actor: ActorRef, username: String)
  case class PeerDisconnect(actor: ActorRef, username: String)

  case class GameStateMessage(gameState: String)

  case class GameOnNextButtonClicked()
}
