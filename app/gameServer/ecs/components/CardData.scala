package ecs.components

import play.api.libs.json._

class CardData(
  var cost: Int = 0,
  var intensity: String = "",
  val cardText: String = "Default Text",
  val flavorText: String = "Default Flavor Text"
) extends AComponent {

  def toJson(): JsObject = {
    Json.obj(
      "cost" ->  cost,
      "intensity" ->  intensity,
      "cardText" ->  cardText,
      "flavorText" ->  flavorText
    )
  }

  // implicit val writes = new Writes[CardData] {
  //   def writes(cd: CardData) = Json.obj(
  //     "cost" ->  cd.cost,
  //     "intensity" ->  cd.intensity,
  //     "cardText" ->  cd.cardText,
  //     "flavorText" ->  cd.flavorText
  //   )
  // }
}