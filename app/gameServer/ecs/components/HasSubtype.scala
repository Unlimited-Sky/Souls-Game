package ecs.components

class HasSubtype(n: Int) extends ABitMaskComponent(n: Int)

object SubtypeBitMaskHelper extends ABitMaskHelper {
  val basic = addBitMask("Basic")
  val clash = addBitMask("Clash")
}
