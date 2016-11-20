package ecs.events

//import ecs.systems.
//import ecs.systems.
//import ecs.components.
import ecs.{ Entity, ComponentManager, SystemManager, EventManager }

//Todo: this event
class DrawCard(owner: Entity, val target: Entity, val howMany: Int) extends AEvent(owner) {

  def onEnqueue(): Unit = {}

  def execute(em: EventManager, cm: ComponentManager, sm: SystemManager): Unit = {
    //cm.getComponent[HasHP](target).foreach(c => sm.getSystem[HPSystem]().addNode(new HPSystemNode(c, howMuch)))
  }

  def onDequeue(): Unit = {}
}