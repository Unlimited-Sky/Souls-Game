package gameEngine

import gameObject.GamePhase
import scala.collection.mutable.{Map, ArrayBuffer}
import actors.ActorGameRoom
import ecs._
import ecs.systems._
import ecs.components._
import ecs.events._

import helpers.CardStack

//The object which handles all game related object
//Works closesly with its roomActor
class GameEngine(val roomActor: ActorGameRoom) {
  var currentGamePhase = GamePhase.AwatingPlayers
  var gameStarted: Boolean = false
  var players: ArrayBuffer[Entity] = ArrayBuffer[Entity]()
  var currentPlayer: Int = -1
  var activePlayer: Int = -1

  //Initialize ECS
  val entityManager = new EntityManager()
  val gameRoom = entityManager.generateEntity()
  val componentManager = new ComponentManager()
  val systemManager = new SystemManager()
  val eventManager = new EventManager(componentManager, systemManager)
  systemManager.init(eventManager)

	//Increment the state, and do any game-wide changes
  def enterNextPhase(): Unit = {
    currentGamePhase match {
    	case GamePhase.AwatingPlayers => {
    		//No draw on first turn!
    		currentGamePhase = GamePhase.MainPhase1
    	}
      case GamePhase.TurnStart => {
        //set the current player & active
        eventManager.enqueueNewEvent(new DrawCard(gameRoom, players(currentPlayer), 1))
        currentGamePhase = GamePhase.MainPhase1
      }
      case GamePhase.MainPhase1 => {
        currentGamePhase = GamePhase.Clash1
      }
      case GamePhase.Clash1 => {
        currentGamePhase = GamePhase.Combat
      }
      case GamePhase.Combat => {
        currentGamePhase = GamePhase.MainPhase2
      }
      case GamePhase.MainPhase2 => {
        currentGamePhase = GamePhase.Clash2
      }
      case GamePhase.Clash2 => {
        currentGamePhase = GamePhase.TurnEnd
      }
      case GamePhase.TurnEnd => {
        currentGamePhase = GamePhase.TurnStart
      }
    }
  }

  def initializeGame(): Unit = {
    println("Initializing Game...")
    players = util.Random.shuffle(players)
    players.foreach(p => initializePlayer(p))

    activePlayer = 0
    currentPlayer = 0

    players.foreach(p => eventManager.enqueueNewEvent(new DrawCard(gameRoom, p, 5)))
    players.foreach(p => eventManager.enqueueNewEvent(new DrawDrive(gameRoom, p, 1)))

    enterNextPhase()

    println("Game Initialized!")
   
    roomActor.sendGameStateToAll()
  }

  def initializePlayer(player: Entity): Unit = {
    componentManager.createComponent[HasHP](player, new HasHP(30))
    componentManager.createComponent[HasPlayerData](player, new HasPlayerData())
  }

  def onPlayerConnect(username: String): Unit = {
  	val connectedPlayerEntity = entityManager.generateEntity()
    players += connectedPlayerEntity
    componentManager.createComponent[HasName](connectedPlayerEntity, new HasName(username))
  }
}