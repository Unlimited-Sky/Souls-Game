package ecs

import scala.collection.mutable.ArrayBuffer
import scala.reflect.ClassTag
import ecs.systems.ASystem

//Manages the systems, which are each responsible for
//performing the logic on their nodes
class SystemManager() {
  val systems = new ArrayBuffer[ASystem[_]]

  //Both SystemManager and EventManager need references to eachother...
  //So we have this ugly code
  private var em: EventManager = _
  private var emSet = false

  def init(eventManager: EventManager): Unit = {
    if (emSet)
      throw new Exception("System Manager: EventManager has already been set!")
    em = eventManager
    emSet = true
  }
  //End ugly code


  //todo add safety check for adding multiple systems
  //Creates and adds the system to the system manager
  def createSystem[S <: ASystem[_]]()(implicit t: ClassTag[S]): S = {
    val system = Class.forName(t.toString).newInstance.asInstanceOf[S]
    system.em = em
    addSystem[S](system)
    system
  }

  private def addSystem[S](system: ASystem[_]): Unit = systems += system

  //Gets the system
  def getSystem[S <: ASystem[_]]()(implicit t: ClassTag[S]): S = {
     systems.find(s => s.getClass == t.runtimeClass).get.asInstanceOf[S]
  }

  //Removes the system
  def removeSystem(system: ASystem[_]): Unit = systems -= system

  //Tell all systems within the manager to process their nodes
  def processAll(): Unit = systems.foreach(_.processAll())
}
