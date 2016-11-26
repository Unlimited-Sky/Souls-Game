package ecs.components

import play.api.libs.json._

abstract class ABitMaskComponent(n: Int) extends AComponent {
  val typ = new BitMaskObject(n)

  def toJson(): JsObject = {
    Json.obj(
      "type" -> typ.get
    )
  }
}
