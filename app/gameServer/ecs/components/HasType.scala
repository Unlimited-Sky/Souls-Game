package ecs.components

class HasType(n: Int) extends ABitMaskComponent(n: Int)

object TypeBitMaskHelper extends ABitMaskHelper {
  val aura = addBitMask("Aura")
  val drive = addBitMask("Drive")
  val skill = addBitMask("Skill")
  val soul = addBitMask("Soul")
}
