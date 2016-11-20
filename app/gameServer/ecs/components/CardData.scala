package ecs.components

class CardData(
  var cost: Int = 0,
  var intensity: String = "",
  val cardText: String = "Default Text",
  val flavorText: String = "Default Flavor Text"
) extends AComponent
