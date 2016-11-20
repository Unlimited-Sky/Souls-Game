package ecs.components

class HasColor(n: Int) extends ABitMaskComponent(n: Int)

object ColorBitMaskHelper extends ABitMaskHelper {
  val white = addBitMask("White")
  val blue = addBitMask("Blue")
  val purple = addBitMask("Purple")
  val black = addBitMask("Black")
  val red = addBitMask("Red")
  val green = addBitMask("Green")
}
