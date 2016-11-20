package ecs

import scala.collection.mutable.ListBuffer

//Generates and manages Entity objects
//Uses a unique ID system UniqueIDGenerator to ensure uniqueness
class EntityManager() {

  //Could try ArrayBuffer for performance...
  //Holds our list of managed Entities
  private val entities = new ListBuffer[Entity]()

  //Generates a new entity
  def generateEntity(): Entity = {
    new Entity(UniqueIDGenerator.getUniqueID())
  }

  //This object generates unique ID's for entities. Use the getUniqueID function
  object UniqueIDGenerator {
    private var curID = -1

    //Returns a unique ID
    def getUniqueID(): Int = {
      curID += 1
      curID
    }
  }
}
