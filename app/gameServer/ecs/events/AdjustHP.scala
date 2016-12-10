package ecs.events

import ecs.systems.{HPSystem, HPSystemNode}
import ecs.components.HasHP
import ecs.{ Entity, ComponentManager, SystemManager, EventManager }

//Adjust the HP of an entity. Positive values for healing, and
//negative values for damage. Works on any entity with a HasHP Component
class AdjustHP(owner: Entity, val target: Entity, val howMuch: Int) extends AEvent(owner) {

  def onEnqueue(): Unit = {}

  def execute(em: EventManager, cm: ComponentManager, sm: SystemManager): Unit = {
    // cm.getComponent[HasHP](target) match {
    //   case Some(c) => sm.getSystem[HPSystem]().addNode(new HPSystemNode(c, howMuch))
    //   case None =>
    // }
    cm.getComponent[HasHP](target).foreach(c => sm.getSystem[HPSystem]().addNode(new HPSystemNode(c, howMuch)))
  }

  def onDequeue(): Unit = {}
}
