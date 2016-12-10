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
  def drawCards(howMany: Int): CardStack = {
    val split = cards.splitAt(howMany)
    cards = split._2
    new CardStack(split._1)
  }

  //shuffles the deck
  def shuffle(): CardStack = {
    cards = util.Random.shuffle(cards)
    this
  }

  //returns how many cards are in the stack
  def length(): Int = cards.length

  //adds a card to the stack
  def addCards(newStack: CardStack): CardStack = {
    cards ++= newStack.cards
    this
  }

  def addCard(newCard: Entity): CardStack = {
    cards += newCard
    this
  }
}