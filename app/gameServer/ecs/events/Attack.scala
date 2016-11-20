package ecs.events

import ecs.components.HasAttackPower
import ecs.{ Entity, ComponentManager, SystemManager, EventManager }

//When one entity attacks another, they should both deal their damage
//to each other (unless: no attack power component OR zero attack power)
class Attack(owner: Entity, val target: Entity) extends AEvent(owner) {

  def onEnqueue(): Unit = {
    println(s"entity: $owner attacks entity: $target")
  }

//Creates an "AdjustHP" event with a negative value to take damage, but only if
//the attacking entity has an attack power component. Example: Soul vs Soul and
//Player vs Soul
  def execute(em: EventManager, cm: ComponentManager, sm: SystemManager): Unit = {
    // cm.getComponent[HasAttackPower](owner) match {
    //   case Some(c) => em.enqueueNewEvent(new AdjustHP(owner, target, -1 * c.currAP))
    //   case None => //do nothing
    // }
    // cm.getComponent[HasAttackPower](target) match {
    //   case Some(c) => em.enqueueNewEvent(new AdjustHP(target, owner, -1 * c.currAP))
    //   case None => //do nothing
    // }
    cm.getComponent[HasAttackPower](owner).foreach(c => em.enqueueNewEvent(new AdjustHP(owner, target, -1 * c.currAP)))
    cm.getComponent[HasAttackPower](target).foreach(c => em.enqueueNewEvent(new AdjustHP(target, owner, -1 * c.currAP)))
  }

  def onDequeue(): Unit = {

  }
}
