package ecs.systems

import ecs.{ Entity, EventManager }
import ecs.components.AComponent
import scala.collection.mutable.ArrayBuffer

import ecs.components.HasHP

class HPSystem extends ASystem[HPSystemNode](
  new ArrayBuffer[HPSystemNode]()
  ) {

//TODO: See HasHP component for todo information
  def process(n: HPSystemNode): Unit = {
    n.hasHP.currHP += n.amount

    //TODO: (different from above)
    //Maybe add some logic for fading Status
    //like fire a new "change status" or "begin fading" event
    if (n.hasHP.currHP <= 0)
      println("I'm dead. :(")
  }
}

class HPSystemNode(
  val hasHP: HasHP,
  val amount: Int
) extends ANode(
  true,
  Array[Any](hasHP, amount)
)
