package ecs.events

import ecs.systems.HPSystem
import ecs.systems.HPSystemNode
import ecs.components.{ HasAttackPower, HasHP }
import ecs.{ Entity, ComponentManager, SystemManager, EventManager }

//The base class for all game events, the class
//that will drive all of the logic for the game
abstract class AEvent(val owner: Entity) {

  //Called once the event appears on the queue
  def onEnqueue(): Unit

  //Called on the first event in the queue before it is dequeued.
  //Mainly for component related/data/gameplay things
  //Usually this involves:
  //1. Take the relevant components based on their entity IDs
  //2. Put those from step 1 into the correct nodes, do any modifications
  //3. Forward them to the system OR create another corresponding event
  def execute(em: EventManager, cm: ComponentManager, sm: SystemManager): Unit

  //Called when the event is removed from the queue
  //Best place to notify effects, sounds, etc
  def onDequeue(): Unit
}
