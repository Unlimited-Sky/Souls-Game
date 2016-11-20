package ecs.systems

//Works as a "view" of the data, which a system
//uses to do all processing logic. Is built from
//multiple components
//Example: Node(PositionComponent, VelocityComponent)
//plugs into a MovementSystem processor
abstract class ANode(
  val processOnce: Boolean,
  private[ecs] val components: Array[Any]
  )
