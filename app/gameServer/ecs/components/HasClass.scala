package ecs.components

class HasClass(n: Int) extends ABitMaskComponent(n: Int)

object ClassBitMaskHelper extends ABitMaskHelper {
  val angel = addBitMask("Angel")
  val demon = addBitMask("Demon")
  val elemental = addBitMask("Elemental")
  val raptor = addBitMask("Raptor")
  val scholar = addBitMask("Scholar")
  val soldier = addBitMask("Soldier")
  val spirit = addBitMask("Spirit")
  val terror = addBitMask("Terror")
  val zombie = addBitMask("Zombie")
}
