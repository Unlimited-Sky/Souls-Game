package gameObject

object GamePhase extends Enumeration {
  // type GamePhase = Value
  val NoPhase = Value("NoPhase")
  val AwatingPlayers = Value("AwatingPlayers")
  val GameOver = Value("GameOver")
  val TurnStart = Value("TurnStart")
  val MainPhase1 = Value("MainPhase1")
  val Clash1 = Value("Clash1")
  val Combat = Value("Combat")
  val MainPhase2 = Value("MainPhase2")
  val Clash2 = Value("Clash2")
  val TurnEnd = Value("TurnEnd")
}


// object GamePhase {

//   private class GamePhaseType(val value: Value)

//   case object NoPhase extends GamePhaseType(NoPhaseValue)
//   case object AwatingPlayers extends GamePhaseType(AwatingPlayersValue)
//   case object GameOver extends GamePhaseType(GameOverValue)
//   case object TurnStart extends GamePhaseType(TurnStartValue)
//   case object MainPhase1 extends GamePhaseType(MainPhase1Value)
//   case object Clash1 extends GamePhaseType(Clash1Value)
//   case object Combat extends GamePhaseType(CombatValue)
//   case object MainPhase2 extends GamePhaseType(MainPhase2Value)
//   case object Clash2 extends GamePhaseType(Clash2Value)
//   case object TurnEnd extends GamePhaseType(TurnEndValue)

//   // type GamePhase = Value
//   //val Zombie = 1 <<< 1
//   //val Spirit = 1 <<< 2

//   // val NoPhaseValue = Value("NoPhase")
//   // val AwatingPlayersValue = Value("AwatingPlayers")
//   // val GameOverValue = Value("GameOver")
//   // val TurnStartValue = Value("TurnStart")
//   // val MainPhase1Value = Value("MainPhase1")
//   // val Clash1Value = Value("Clash1")
//   // val CombatValue = Value("Combat")
//   // val MainPhase2Value = Value("MainPhase2")
//   // val Clash2Value = Value("Clash2")
//   // val TurnEndValue = Value("TurnEnd")
// }

// int color = 0

// 000001 red
// 000010 blue

// color = ColorEnum.Red || colorEnum.Blue



// object Cards {
//   case object Zombie extends Card("zombie") with Attackable with Creture
//   case object Zombie extends Card("zombie") with Attackable
//   case object Zombie extends Card("zombie") with Attackable with RedColor
//   case object Zombie extends Card("zombie") with Attackable with Creture
//   case object Zombie extends Card("zombie") with Attackable with Creture
//   case object Zombie extends Card("zombie") with Attackable with Creture
//   case object Zombie extends Card("zombie") with Attackable with Creture

//   case obect Eji extends Card("Eji-san", SubType.Human + SubType.Creature) with Creature


//   case object Teo extends Card("Teo-kun") with SubType.Human with DealDamageEvent(DealDamageEvent.PlayersOnly, 5)
//   case object Fireball extends Card("big fireball") with CardType.Sorcery with DealDamageEvent(dealDamageEvent.PlayersAndCreatures, 10)
// }

// class Card(val name: String)

// trait Creature {
//   val power: Int
//   val hp: Int
// }

// class CreatureCard(name: String, val power: Int, val hp: Int) extends Card(name) with Creature

// def makeACard(isCreature: Boolean): Card = {
//   if (isCreature)
//     new CreatureCard("Eji", 2, 2)
//   else
//     new Card("Teo")
// }
