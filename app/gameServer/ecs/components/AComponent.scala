package ecs.components

import play.api.libs.json._

//The component class ONLY contains data.
//These typically hold the minimum data set required
//to perform some set of actions by a "System" object
//Example: PositionComponent has values xPosition and yPosition
abstract class AComponent() {
  def toJson(): JsObject
}