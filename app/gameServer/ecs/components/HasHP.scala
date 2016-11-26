package ecs.components

import play.api.libs.json._

//todo: add maxHP, and maybe bool for bound/unbound
//max HP values. will have to also adjust HPSystem
//to clamp these values too
class HasHP(
  var currHP: Int = 0
) extends AComponent {

  def toJson(): JsObject = {
    Json.obj(
      "currHP" -> currHP
    )
  }
}

// object HasHP {
//   implicit val writes = new Writes[HasHP] {
//     def writes(hp: HasHP) = Json.obj(
//       "currHP" ->  hp.currHP
//     )
//   }
// }
