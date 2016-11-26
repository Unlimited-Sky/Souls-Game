package actors

import akka.actor.{ActorRef, Actor, ActorLogging}
import scala.collection.mutable.Map

import gameEngine.GameEngine

import gameServer.Messages._

//This class manages the akka side of one game room
//and houses the GameEngine object
class ActorGameRoom extends Actor with ActorLogging {
  val maxPlayers = 3

  val gameEngine = new GameEngine(this)

  var players: Map[ActorRef, String] = Map[ActorRef, String]()
  var peers: Map[ActorRef, String] = Map[ActorRef, String]()

  //Checks if the game is ready to start
  def checkGameReady(): Unit =  {
    println("Checking for enough players...")
    if (players.size == maxPlayers)
      gameEngine.initializeGame()
  }

  //Handle all the messages from other actors
  def receive = {
    case PlayerConnect(user, username) =>
    //Todo: load the players deck
    players += user -> username
    gameEngine.onPlayerConnect(username)
    messageEveryone(new TextMessage(s"$username has joined the game."))

    if (!gameEngine.gameStarted)
      checkGameReady()

    case PlayerDisconnect(user, username) =>

    case PeerConnect(user, username) =>
    peers += user -> username
    messageEveryone(new TextMessage(s"$username has joined as a spectator."))

    case PeerDisconnect(user, username) =>

    case other =>
      log.error(s"[ActorGameState] Not handled: $other")
  }

  def sendGameStateToAll(gameState: String): Unit = {
    messageEveryone(new GameStateMessage(gameState))
  }

  def messageEveryone[T](msg: T) = {
    messageUserMap(players, msg)
    messageUserMap(peers, msg)
  }

  def messageUserMap[T](m: Map[ActorRef, String], msg: T): Unit = {
    m.foreach({case (u, _) => u ! msg})
  }
}