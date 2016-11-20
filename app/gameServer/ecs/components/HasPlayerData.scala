package ecs.components

import helpers.CardStack

class HasPlayerData() extends AComponent {
  val deck: CardStack = new CardStack()
  val driveDeck: CardStack = new CardStack()
  val terminus: CardStack = new CardStack()
  val void: CardStack = new CardStack()
  val hand: CardStack = new CardStack()
  var power: Int = 0
  var intensity: String = ""
}
