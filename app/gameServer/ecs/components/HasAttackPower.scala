package ecs.components

import play.api.libs.json._

class HasAttackPower(
  var currAP: Int = 0
) extends AComponent {

  def toJson(): JsObject = {
    Json.obj(
      "currAP" ->  currAP
    )
  }
}
