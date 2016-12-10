package helpers

import ecs.components._
import ecs._

class CardLoader(val em: EntityManager, val cm: ComponentManager) {

  def loadDeck(): CardStack = {
    val toReturn = new CardStack()

    // 1 Master Soldier
    val masterSoldier = createBasicCard("Master Soldier", TypeBitMaskHelper.soul, 4, "WWWW", "If you control another Soldier, gain +1+1", "")
    cm.createComponent[HasAttackPower](masterSoldier, new HasAttackPower(4))
    cm.createComponent[HasHP](masterSoldier, new HasHP(4))
    cm.createComponent[HasClass](masterSoldier, new HasClass(ClassBitMaskHelper.soldier))
    toReturn.addCard(masterSoldier);

    // 1 Spirit of the Legend
    // 2 Weapon Master
    // 2 Protecting Angel
    // 2 Blinding Light
    // 3 Enforcer
    // 3 General
    // 2 Shieldmaster
    // 3 Guardian
    // 2 Holy Power
    // 2 Flower Power
    // 2 Wings
    // 2 Scholar of Yore
    // 3 Military Soldier
    val ms1 = createBasicCard("Military Soldier", TypeBitMaskHelper.soul, 1, "WWWW", "If you control another Soldier, gain +1+1", "")
    cm.createComponent[HasAttackPower](masterSoldier, new HasAttackPower(2))
    cm.createComponent[HasHP](ms1, new HasHP(4))
    toReturn.addCard(ms1)
    cm.createComponent[HasClass](ms1, new HasClass(ClassBitMaskHelper.soldier))
    val ms2 = createBasicCard("Military Soldier", TypeBitMaskHelper.soul, 1, "WWWW", "If you control another Soldier, gain +1+1", "")
    cm.createComponent[HasAttackPower](masterSoldier, new HasAttackPower(2))
    cm.createComponent[HasHP](ms2, new HasHP(4))
    cm.createComponent[HasClass](ms2, new HasClass(ClassBitMaskHelper.soldier))
    toReturn.addCard(ms2)
    val ms3 = createBasicCard("Military Soldier", TypeBitMaskHelper.soul, 1, "WWWW", "If you control another Soldier, gain +1+1", "")
    cm.createComponent[HasAttackPower](masterSoldier, new HasAttackPower(2))
    cm.createComponent[HasHP](ms3, new HasHP(4))
    cm.createComponent[HasClass](ms3, new HasClass(ClassBitMaskHelper.soldier))
    toReturn.addCard(ms3)

    toReturn
  }

  def loadDriveDeck(): CardStack = {
    val toReturn = new CardStack()
    for (i <- 1 to 10) {
      toReturn.addCard(createBasicCard("Hope Source", TypeBitMaskHelper.drive, 0, "", "Add H to your moxie, and gain 1 power", ""))
    }
    toReturn
  }

  private def createBasicCard(cardName: String, cardType: Int, cost: Int, intensity: String, cardText: String, flavorText: String): Entity = {
      val newCard = em.generateEntity()
      cm.createComponent[HasName](newCard, new HasName(cardName))
      cm.createComponent[HasType](newCard, new HasType(cardType))
      cm.createComponent[CardData](newCard, new CardData(cost, intensity, cardText, flavorText))
      newCard
  }
}