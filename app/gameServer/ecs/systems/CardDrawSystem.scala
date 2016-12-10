package ecs.systems

import ecs.{ Entity, EventManager }
import ecs.components.AComponent
import scala.collection.mutable.ArrayBuffer

import ecs.components.HasPlayerData

class CardDrawSystem extends ASystem[CardDrawSystemNode](
  new ArrayBuffer[CardDrawSystemNode]()
  ) {

  def process(n: CardDrawSystemNode): Unit = {
    n.pData.hand.addCards(n.pData.deck.drawCards(n.howMany))
  }
}

class CardDrawSystemNode(
  val pData: HasPlayerData,
  val howMany: Int
) extends ANode(
  true,
  Array[Any](pData, howMany)
)
