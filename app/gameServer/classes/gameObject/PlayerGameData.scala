// package gameObject

// import helpers.CardStack

// //This class holds all game-related data for the player
// class PlayerGameData {
//   var deck: CardStack = new CardStack()
//   var powersDeck: CardStack = new CardStack()
//   var terminus: CardStack = new CardStack()
//   var void: CardStack = new CardStack()
//   var hand: CardStack = new CardStack()

//   var playField: CardStack = new CardStack()

//   var hp: Int = -1
//   var power: Int = -1
//   var intensity: String = ""

//   def this(cs: CardStack) = {
//     this()
//     deck = new CardStack(cs)
//   }

//   def DrawCards(howMany: Int): Unit = {
//     hand.AddCards(deck.DrawCards(howMany))
//   }

//   def ToString(): String = {
//     var toReturn: String = ""
//     toReturn += "Deck Count: " + deck.Length() + "\n"
//     toReturn += "Hand Count: " + hand.Length() + "\n"
//     toReturn += s"HP: $hp Power: $power Intensity: $intensity"
//     toReturn
//   }
// }
