package ecs

//An entity serves the purpose of a singular game object.
//Only holds a unique ID. No data, no logic, could even be replaced
//by just an integer value!
//Can only be created by the Entity Manager class
class Entity  (val UID: Int) {
  override def toString(): String = { s"UID:$UID" }
}
