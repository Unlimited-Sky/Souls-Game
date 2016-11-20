var renderer = PIXI.autoDetectRenderer(1280, 720, {backgroundColor : 0x10bb99});
document.body.appendChild(renderer.view);

var stage = new PIXI.Container();

animate();

function animate() {
    requestAnimationFrame(animate);

    renderer.render(stage);
}

//Class definitions
//PlayerData class - holds all player related data
function PlayerData(playerName) {
	this.playerName = playerName;
	this.currentLife = 0;
	this.intensity = "";
	this.power = 0;

	this.personaCount = 0;
	this.driveCount = 0;
	this.hand = new CardZone(false);
	this.terminus = new CardZone(true);
	this.void = new CardZone(true);
	this.inPlay = new CardZone(true);

	//Loads all of the images for the deck
	this.loadDeck() = function() {

	}
};

//Card class - A does a lot of stuff
function Card(entityID, cardID) {
	this.entityID = entityID;
	this.cardID = cardID;

	//Start initialization logic

	//End initilization logic
	
	this.addToStage() = function() {

	}
};

//CardZone class - basically, a list of cards
function CardZone(isVisible) {
	this.isVisible = isVisible;
	this.cardList = [];

	this.addCard = function(card) {
		this.cardList.push(card);
	};

	this.getLength = function() {
		return cardList.length;
	};
};