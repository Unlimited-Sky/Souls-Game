package ecs.components

//todo: add maxHP, and maybe bool for bound/unbound
//max HP values. will have to also adjust HPSystem
//to clamp these values too
class HasHP(
  var currHP: Int = 0
) extends AComponent
