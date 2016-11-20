package ecs.components

import scala.collection.mutable.ArrayBuffer

trait BitChecking {
  protected def isValidBit(v: Int): Unit = {
    if (v.toBinaryString.count(v => v == '1') != 1)
      throw new Exception("Invalid Bitmask, not a single bit: " + v.toBinaryString)
    }
}

//Bit mask data structure,
class BitMask(val name: String, val value: Int) {

  def |(n: Int): Int = value | n
}

//Helper object which allows the implicit conversion
object BitMask {
  import scala.language.implicitConversions

  implicit def bitMaskToInt(b: BitMask): Int = b.value
}

//A bit mask object which holds the value, and has some helper functions
//for ease of use
class BitMaskObject(private var value: Int = 0) {
  def addValue(v: Int): Int = {
    value = value | v
    value
  }

  def addValue(b: BitMask): Int = addValue(b.value)

  def removeValue(v: Int): Int = {
    value = value ^ v
    value
  }

  def removeValue(b: BitMask): Int = removeValue(b.value)

  def hasValue(check: Int): Boolean = (value & check) != 0

  def get(): Int = value
}

abstract class ABitMaskHelper() {
  private val masks = ArrayBuffer[BitMask]()

  def addBitMask(fieldName: String): BitMask = {
    val bitField = new BitMask(fieldName, masks.length + 1)
    masks += bitField
    bitField
  }

  def getAsString(b: BitMaskObject): String = getAsString(b.get)

  def getAsString(value: Int): String = {
    var toReturn = ""

    for (i <- 0 to masks.length by 1) {
      if ((value & 1 << i) != 0) {
        if (toReturn == "")
          toReturn = masks(i).name
        else
          toReturn = toReturn  + " " + masks(i).name
      }
    }
    toReturn
  }
}
