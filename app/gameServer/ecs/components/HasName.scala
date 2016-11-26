package ecs.components

import play.api.libs.json._

class HasName(
  val name: String = ""
) extends AComponent {

  def toJson(): JsObject = {
    Json.obj(
      "name" ->  name
    )
  }
}

// object HasName {
//   implicit val writes = new Writes[HasName] {
//     def writes(hn: HasName) = Json.obj(
//       "name" ->  hn.name
//     )
//   }
// }
