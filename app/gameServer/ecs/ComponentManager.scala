package ecs

import scala.collection.mutable.Map
import scala.reflect.ClassTag

import ecs.components.AComponent

import play.api.libs.json._

//Manages the components and their creation
//Use CreateComponent to initialize new components
//while adding them to the collection
class ComponentManager() {

  //The matrix of components
  //1st key is the Component Type
  //2nd key is the entity's components
  val components = Map[Class[_], Map[Entity, AComponent]]()

  //Creates and returns a component, and adds it to the collection
  def createComponent[C <: AComponent](entity: Entity, component: C): C = {
    addComponent(entity, component)
    component
  }

  //Removes the component
  def removeComponent(entity: Entity, component: AComponent): Unit = {
    //b.get(1).get.get("first").get == 111
    if (checkComponentDefined(component))
      components.get(component.getClass).get -= entity
  }

  //Gets a component
  def getComponent[C <: AComponent](entity: Entity)(implicit t: ClassTag[C]): Option[C] = {
    components.get(t.runtimeClass).get.get(entity).asInstanceOf[Option[C]]
   }

  //Adds the component to the map of maps...
  private def addComponent(entity: Entity, component: AComponent): Unit = {
    if (checkComponentDefined(component))
      components.get(component.getClass()).get += entity -> component
    else
      components += component.getClass() -> Map(entity -> component)
  }

  private def checkComponentDefined(component: AComponent): Boolean = {
    components.get(component.getClass()).isDefined
  }

//This function is super ugly, dont question how it works
//and don't bother changing it unless it's really broken
  def getComponentsJSON(): String = {
    var toReturn = "{"
    var firstLoop = false;
    components.foreach(componentType => {
      if (firstLoop == true) toReturn += "," 
      toReturn += "\"" + componentType._1.getSimpleName + "\": {" 
      var secondLoop = false 
      componentType._2.foreach(component => {
        if (secondLoop == true) toReturn += ","
        toReturn += "\"" + component._1.UID + "\":" + component._2.toJson
        secondLoop = true   
       })
      firstLoop = true
      toReturn += "}"
    })
    toReturn += "}"
    println(toReturn)
    toReturn
  }
}