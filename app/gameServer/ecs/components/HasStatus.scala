package ecs.components

class HasStatus(n: Int) extends ABitMaskComponent(n: Int)

object StatusBitMaskHelper extends ABitMaskHelper {
  val depleted = addBitMask("Depleted")
  val fading = addBitMask("Fading")
  val forming = addBitMask("Forming")
}
