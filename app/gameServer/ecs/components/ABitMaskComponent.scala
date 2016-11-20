package ecs.components

abstract class ABitMaskComponent(n: Int) extends AComponent {
  val typ = new BitMaskObject(n)
}
