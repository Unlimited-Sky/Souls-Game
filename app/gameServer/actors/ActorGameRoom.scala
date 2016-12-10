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

  val t = new java.util.Timer()
  val task = new java.util.TimerTask {
    def run() = gameEngine.tick()
  }

  //Checks if the game is ready to start
  def checkGameReady(): Unit =  {
    println("Checking for enough players...")
    if (players.size == maxPlayers) {
      gameEngine.initializeGame()
      t.schedule(task, 1000L, 1000L)
    } 
  }

  //Handle all the messages from other actors
  def receive = {
    case PlayerConnect(user, username) =>
    players += user -> username
    user ! new PlayerUIDMessage(gameEngine.onPlayerConnect(username))
    messageEveryone(new TextMessage(s"$username has joined the game."))

    if (!gameEngine.gameStarted)
      checkGameReady()

    case PlayerDisconnect(user, username) =>
    players -= user
    gameEngine.onPlayerDisconnect(username)
    messageEveryone(new TextMessage(s"$username has left the game."))
    task.cancel()

    case PeerConnect(user, username) =>
    peers += user -> username
    messageEveryone(new TextMessage(s"$username has joined as a spectator."))

    case PeerDisconnect(user, username) =>

    case GameOnNextButtonClicked() => 
    println("GameOnNextButtonClicked recv")
    gameEngine.enterNextPhase()

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