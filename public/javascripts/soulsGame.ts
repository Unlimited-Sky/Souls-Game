/// <reference path="pixi.js.d.ts" />
/// <reference path="jquery.d.ts" />

//Renderer - all rendering related code
module Renderer {
	let renderer = PIXI.autoDetectRenderer(1280, 720, {backgroundColor : 0x10bb99});
	document.body.appendChild(renderer.view);

	let stage = new PIXI.Container();

	animate();

	function animate() {
		requestAnimationFrame(animate);
		renderer.render(stage);
	}
}

//Websocket - the onmessage func will handle a lot of logic
module Websocket {
	let websocket;

	export function initWebsocket(websocketPath: string) {
		let websocketURL = "ws://" + window.location.host + websocketPath;
		websocket = new WebSocket(websocketURL);

		websocket.onopen = (evt) => onOpen(evt);
		websocket.onclose = (evt) => onClose(evt);
		websocket.onmessage = (evt) => onMessage(evt);
		websocket.onerror = (evt) => onError(evt);
	}

	function onOpen(evt) {
		writeToScreen("You have connected.");
	}

	function onClose(evt) {
		writeToScreen("You have disconnected.");
	}

	function onMessage(evt) {
		let json = jQuery.parseJSON(evt.data);
		console.log("json: " + json);
		let type = json.type;
		let html;

		if(type == "textMessage") {
				html = json.user + ": " + json.message;
		} else {
				html = "MSG ERROR: " + json.message;
		}

		writeToScreen(html);
	}

	function onError(evt) {
		writeToScreen("ERROR: " + evt.data);
	}

	function writeToScreen(html) {
		var htmlLine = html + "\n";
			$('#gameLog').append(htmlLine);
	}
}

//Game Objects - helper objects for game related things
module GameObjects {
	//Class definitions
	//PlayerData class - holds all player related data
	class PlayerData {
		playerName: string;
		currentLife: number;
		intensity: string;
		power: number;
		personaCount: number;
		driveCount: number;
		hand: CardZone;
		terminus: CardZone;
		void: CardZone;
		inPlay: CardZone;

		constructor(playerName: string) {
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
		}

		//Loads all of the images for the deck
		loadDeck() {
		}
	}

	//Card class - A does a lot of stuff
	class Card {
		entityID: number;
		cardID: number;

		constructor(entityID: number, cardID: number) {
			this.entityID = entityID;
			this.cardID = cardID;
		}
			
		addToStage() {
		}
	}

	//CardZone class - basically, a list of cards
	class CardZone {
		isVisible: boolean;
		cardList: Card[];
		
		constructor(isVisible: boolean) {
			this.isVisible = isVisible;
			this.cardList = [];
		}

		addCard(card: Card) {
			this.cardList.push(card);
		}

		getLength() {
			return this.cardList.length;
		}
	}
}