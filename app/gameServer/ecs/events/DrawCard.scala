package ecs.events

import ecs.systems.{CardDrawSystem, CardDrawSystemNode}
import ecs.components.HasPlayerData
import ecs.{ Entity, ComponentManager, SystemManager, EventManager }

//Todo: this event
class DrawCard(owner: Entity, val target: Entity, val howMany: Int) extends AEvent(owner) {

  def onEnqueue(): Unit = {}

  def execute(em: EventManager, cm: ComponentManager, sm: SystemManager): Unit = {
    cm.getComponent[HasPlayerData](target).foreach(c => sm.getSystem[CardDrawSystem]().addNode(new CardDrawSystemNode(c, howMany)))
  }

  def onDequeue(): Unit = {}
}