package ecs.systems

import scala.collection.mutable.ArrayBuffer
import ecs.EventManager

//A system (sometimes called a processor) defines
//all of the logic for the components and entities.
//Systems need to know it's targets: a node which contains
//the data to modify.
//Example: PositionComponent + VelocityComponent = MovementSystem
abstract class ASystem[N <: ANode] private[ecs](
  private val nodes: ArrayBuffer[N]) {

  var em: EventManager = _

  private[ecs] def processAll(): Unit = {
    nodes.foreach(
      n => {
        process(n)
        if (n.processOnce)
          removeNode(n)})
  }

  protected def process(node: N): Unit

  def addNode(node: N*): Unit = node.foreach(v => nodes += v)

  def removeNode(node: N): Unit = nodes -= node
}
