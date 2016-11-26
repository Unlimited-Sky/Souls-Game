package ecs.components

import play.api.libs.json._

import helpers.CardStack

class HasPlayerData() extends AComponent {
  val deck: CardStack = new CardStack()
  val driveDeck: CardStack = new CardStack()
  val terminus: CardStack = new CardStack()
  val void: CardStack = new CardStack()
  val hand: CardStack = new CardStack()
  var power: Int = 0
  var intensity: String = ""

  def toJson(): JsObject = {
    Json.obj(
      "deck" ->  deck.cards,
      "driveDeck" ->  driveDeck.cards,
      "terminus" ->  terminus.cards,
      "void" ->  void.cards,
      "hand" ->  hand.cards,
      "power" ->  power,
      "intensity" ->  intensity
    )
  }
}

// object HasPlayerData {
//   implicit val writes = new Writes[HasPlayerData] {
//     def writes(pd: HasPlayerData) = Json.obj(
//       "deck" ->  pd.deck,
//       "driveDeck" ->  pd.driveDeck,
//       "terminus" ->  pd.terminus,
//       "void" ->  pd.void,
//       "hand" ->  pd.hand,
//       "power" ->  pd.power,
//       "intensity" ->  pd.intensity
//     )
//   }
// }