package helpers

import ecs.Entity
import scala.util.Random
import scala.collection.mutable.ListBuffer

class CardStack {
  var cards: ListBuffer[Entity] = new ListBuffer()

  def this(cardList: ListBuffer[Entity]) = {
    this()
    cards = cardList
  }

  def this(cs: CardStack) = {
    this()
    cards = cs.cards
  }

  //draws "howMany" cards and returns that list
  def DrawCards(howMany: Int): CardStack = {
    val split = cards.splitAt(howMany)
    cards = split._2
    new CardStack(split._1)
  }

  //shuffles the deck
  def Shuffle(): CardStack = {
    cards = util.Random.shuffle(cards)
    this
  }

  //returns how many cards are in the stack
  def Length(): Int = cards.length

  //adds a card to the stack
  def AddCards(newStack: CardStack): CardStack = {
    cards ++= newStack.cards
    this
  }
}