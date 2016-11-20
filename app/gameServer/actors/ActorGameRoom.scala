package actors

import akka.actor.{ActorRef, Actor, ActorLogging}
import scala.collection.mutable.Map

import gameEngine.GameEngine

import gameServer.Messages._

//This class manages the akka side of one game room
//and houses the GameEngine object
class ActorGameRoom extends Actor with ActorLogging {
  val maxPlayers = 2

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
    messageEveryone(s"$username has joined the game.")

    if (!gameEngine.gameStarted)
      checkGameReady()

    case PlayerDisconnect(user, username) =>

    case PeerConnect(user, username) =>
    peers += user -> username
    messageEveryone(s"$username has joined as a spectator.")

    case PeerDisconnect(user, username) =>

    case other =>
      log.error(s"[ActorGameState] Not handled: $other")
  }

  def sendGameStateToAll(): Unit = {
    //TODO: Update the game state to all game clients
    //playersData.foreach({ case (n, d) => messageEveryone("\n" +  players(n)._2 + ": " + d.ToString() + "\n")})
  }

  def messageEveryone(msg: String) = {
    messageUserMap(players, msg)
    messageUserMap(peers, msg)
  }

  def messageUserMap(m: Map[ActorRef, String], msg: String): Unit = {
    m.foreach({case (u, _) => u ! TextMessage(msg)})
  }
}