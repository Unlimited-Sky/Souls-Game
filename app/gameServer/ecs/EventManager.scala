package ecs

import scala.collection.mutable.Queue
import ecs.events.AEvent

//The event manager class.
//Handles almost all of the game logic,
//since every thing which could happen in-game
//is built as an event
class EventManager(val componentManager: ComponentManager, val systemManager: SystemManager) {
  private val eventQueue = new Queue[AEvent]()

  //Adds a new event to the queue, and runs its "onEnqueue" func
  def enqueueNewEvent(event: AEvent): Unit = {
    event.onEnqueue()
    eventQueue.enqueue(event)
  }

  //Dequeues the first event, and runs its "onDequeue" func
  def dequeueEvent(): Unit = eventQueue.dequeue().onDequeue()

  //Runs the "execute" code for the first event in the queue
  def execute(): Unit = eventQueue.front.execute(this, componentManager, systemManager)

  def hasEvent(): Boolean = { !eventQueue.isEmpty }
}
