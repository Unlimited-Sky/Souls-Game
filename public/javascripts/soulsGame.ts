/// <reference path="pixi.js.d.ts" />
/// <reference path="jquery.d.ts" />
/// <reference path="howler.d.ts" />
/// <reference path="gameStateMessage.d.ts" />

const width = 1280;
const height = 720;


//Renderer - all rendering related code
module Renderer {
    let renderer = PIXI.autoDetectRenderer(width, height, { backgroundColor: 0x10bb99 });

    document.body.appendChild(renderer.view);

    let stage = new PIXI.Container();
    let nextButton: GameObjects.Button;

    //load all the sprites...
    PIXI.loader
        .add('nextButton', '../assets/images/nextButton.png')
        .add('cardBackground', '../assets/images/cardBackground.png')
        .add('cardBackface', '../assets/images/cardBackface.png')
        .once('complete', initScreen);

    PIXI.loader.load();
    //end load sprites

    function initScreen() {
        initNextButton();

        gameLoop();
    }

    function gameLoop() {

        Game.update();

        renderer.render(stage);
        requestAnimationFrame(gameLoop);
    }

    //Buttons
    function initNextButton() {
        nextButton = new GameObjects.Button(new Howl({ src: ['../assets/sounds/Magic Game Proceed UI Tap.wav'] }), new PIXI.Sprite(PIXI.loader.resources.nextButton.texture));
        nextButton.sprite.position.x = 1100;
        nextButton.sprite.position.y = 550;

        nextButton.onClick = function() {
            Websocket.doSend({ msgType: "nextButtonClicked" })
        }

        stage.addChild(nextButton.sprite);
    }

    export function addToStage(toAdd: PIXI.DisplayObject) {
        stage.addChild(toAdd);
    }
}

//Websocket - the onmessage func will handle a lot of forwarding of logic
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
        let type = json.type;
        let html;

        if (type == "textMessage") {
            html = json.user + ": " + json.message;
        }
        else if (type == "gameStateMessage") {
            //html = type + ": " + json.message;
            Game.onRecGameState(json.message)
        } else if (type == "playerUIDMessage") {
            html = "You are Player UID: " + json.uid;
            Game.setThisPlayerUID(json.uid)
        } else {
            html = "MSG ERROR: " + json.message;
        }

        if (html != undefined)
            writeToScreen(html);
    }

    function onError(evt) {
        writeToScreen("ERROR: " + evt.data);
    }

    function writeToScreen(html: string) {
        let htmlLine = html + "\n";
        $('#gameLog').append(htmlLine);
    }

    export function doSend(data) {
        websocket.send(JSON.stringify(data));
    }
}

module Game {
    let players = Array<GameObjects.PlayerData>();
    let cards = Array<GameObjects.Card>();
    let thisPlayerUID: string;

    //one "tick" of the game
    export function update() {
        players.forEach(p => {
            p.playerZones.update()
        })
    }

    export function onRecGameState(gs: string) {
        players = [];
        cards = [];
        let json = jQuery.parseJSON(gs);
        let playerUIDs = Array<string>();

        //Object.keys(json.HasName).forEach(v => console.log(json.HasName[v].name));
        //Object.keys(json.HasHP).forEach(v => console.log(json.HasHP[v].currHP));

        //make cards
        let cardUIDs = Array<string>();
        Object.keys(json.CardData).forEach(cKey => cardUIDs.push(cKey));

        cardUIDs.forEach(c => {
            cards.push(buildCardFromGameState(c, json));
        });

        Object.keys(json.HasPlayerData).forEach(pKey => playerUIDs.push(pKey));

        playerUIDs.forEach(p => {
            players.push(buildPlayerDataFromGameState(p, json));
        });

        players.forEach(p => console.log(p));
    }

    export function setThisPlayerUID(uid: string) {
        thisPlayerUID = uid;
        console.log("Player UID: " + thisPlayerUID);
    }

    export function getThisPlayerUID() {
        return thisPlayerUID;
    }

    function buildPlayerDataFromGameState(uid: string, gs: any) {
        let pd = new GameObjects.PlayerData(gs.HasName[uid].name, uid);
        pd.currentLife = gs.HasHP[uid].currHP;
        pd.intensity = gs.HasPlayerData[uid].intensity;
        pd.power = gs.HasPlayerData[uid].power;
        pd.personaCount = gs.HasPlayerData[uid].deck.length;
        pd.driveCount = gs.HasPlayerData[uid].driveDeck.length;
        pd.playerZones.driveDeck.cardList = buildCardListFromGameStateArray(gs.HasPlayerData[uid].driveDeck);
        pd.playerZones.terminus.cardList = buildCardListFromGameStateArray(gs.HasPlayerData[uid].terminus);
        pd.playerZones.voidZone.cardList = buildCardListFromGameStateArray(gs.HasPlayerData[uid].void);
        pd.playerZones.deck.cardList = buildCardListFromGameStateArray(gs.HasPlayerData[uid].deck);
        pd.playerZones.hand.cardList = buildCardListFromGameStateArray(gs.HasPlayerData[uid].hand);

        return pd;
    }

    function buildCardListFromGameStateArray(data: any) {
        let toReturn = new Array<GameObjects.Card>();

        let ids: Array<string> = data.map(d => d.UID);

        //todo: maybe use filter ??
        ids.forEach(id => {
            cards.forEach(c => {
                if (id == c.UID) {
                    c.loadRenderData();
                    toReturn.push(c);
                }
            })
        })

        return toReturn;
    }

    function buildCardFromGameState(uid: string, gs: any) {
        let cd = new GameObjects.Card(gs.CardData[uid]);
        cd.UID = uid;

        cd.name = gs.HasName[uid].name;
        cd.type = Helpers.getTypeString(gs.HasType[uid].type)
        //from CardData
        cd.intensity = gs.CardData[uid].intensity;
        cd.cost = gs.CardData[uid].cost;
        cd.cardText = gs.CardData[uid].cardText;
        cd.flavorText = gs.CardData[uid].flavorText;

        //todo add more stuff for souls/auras/other card types

        return cd;
    }
}

//Game Objects - helper objects for game related things
module GameObjects {
    //Class definitions
    //PlayerData class - holds all player related data
    export class PlayerData {
        UID: string;
        playerName: string;
        currentLife: number;
        intensity: string;
        power: number;
        personaCount: number;
        driveCount: number;
        playerZones: PlayerZones;

        constructor(playerName: string, uid: string) {
            this.UID = uid;
            this.playerName = playerName;
            this.currentLife = 0;
            this.intensity = "";
            this.power = 0;
            this.personaCount = 0;
            this.driveCount = 0;

            this.playerZones = new PlayerZones(uid == Game.getThisPlayerUID());
        }
    }

    //Card class - A does a lot of stuff
    export class Card {
        UID: string;
        name: string;
        type: string;
        isFaceUp: boolean;
        //from CardData
        intensity: string;
        cost: number;
        cardText: string;
        flavorText: string;

        container: PIXI.Container;

        dName: PIXI.Text;
        dType: PIXI.Text;
        dBackground: PIXI.Sprite;
        dArt: PIXI.Sprite;
        dCost: PIXI.Text;
        dIntensity: PIXI.Text;
        dText: PIXI.Text;
        dFlavorText: PIXI.Text;

        dBackface: PIXI.Sprite;

        sound: Howl;

        constructor(id: string) {
            this.UID = id;
            this.container = new PIXI.Container;

            this.isFaceUp = false;
            this.dBackface = new PIXI.Sprite(PIXI.loader.resources.cardBackface.texture);
            this.dBackface.visible = true;

            this.container.addChild(this.dBackface);

            this.container.pivot.x = 0.5;
            this.container.pivot.y = 0.5;

            this.container.interactive = true;
            this.container.on('mousedown', this.onCardDown, this).on('touchstart', this.onCardDown, this);

            this.sound = new Howl({ src: ['../assets/sounds/cardSlide6.ogg'] })
        }

        onCardDown() {
            //this.isFaceUp = !this.isFaceUp;
            this.sound.play();
        }

        loadRenderData() {
            this.dName = new PIXI.Text(this.name);
            this.dBackground = new PIXI.Sprite(PIXI.loader.resources.cardBackground.texture);
            this.dArt = new PIXI.Sprite();
            this.dType = new PIXI.Text(this.type, Helpers.typeTextStyle);
            this.dCost = new PIXI.Text(this.cost.toString(), Helpers.costTextStyle);
            //this.dIntensity = new  PIXI.Text(this.intensity, Helpers.intensityTextStyle);
            this.dIntensity = new PIXI.Text("RRR", Helpers.intensityTextStyle);
            this.dText = new PIXI.Text(this.cardText, Helpers.textStyle);
            this.dFlavorText = new PIXI.Text(this.flavorText, Helpers.flavorTextStyle);

            this.container.addChild(this.dBackground)
            this.container.addChild(this.dName)
            this.container.addChild(this.dType);
            this.container.addChild(this.dText)
            this.container.addChild(this.dCost)
            this.container.addChild(this.dIntensity)
            this.container.addChild(this.dFlavorText)
            this.container.addChild(this.dArt)

            this.dName.position.x = 35;
            this.dName.position.y = 30;

            this.dType.position.x = 45;
            this.dType.position.y = 270

            this.dText.position.x = 35;
            this.dText.position.y = 400;

            this.dCost.position.x = 300;
            this.dCost.position.y = 30;

            this.dIntensity.position.x = 300;
            this.dIntensity.position.y = 70;

            this.dFlavorText.position.x = 35;
            this.dFlavorText.position.y = 470;

            this.container.scale = new PIXI.Point(0.25, 0.25);

            this.makeFaceDown();
            Renderer.addToStage(this.container)
        }

        update() {
            if (this.isFaceUp == true) {
                this.makeFaceUp();
            } else if (this.isFaceUp == false) {
                this.makeFaceDown();
            }
        }

        setPosition(xPos: number, yPos: number) {
            this.container.x = xPos;
            this.container.y = yPos;
        }

        makeFaceUp() {
            this.dName.visible = true;
            this.dBackground.visible = true;
            this.dArt.visible = true;
            this.dType.visible = true;
            this.dCost.visible = true;
            this.dIntensity.visible = true;
            this.dText.visible = true;
            this.dFlavorText.visible = true;

            this.dBackface.visible = false;
        }

        makeFaceDown() {
            this.dName.visible = false;
            this.dBackground.visible = false;
            this.dArt.visible = false;
            this.dType.visible = false;
            this.dCost.visible = false;
            this.dIntensity.visible = false;
            this.dText.visible = false;
            this.dFlavorText.visible = false;

            this.dBackface.visible = true;
        }
    }

    class PlayerZones {
        deck: CardZone;
        driveDeck: CardZone;
        terminus: CardZone;
        voidZone: CardZone;
        hand: CardZone;

        constructor(isThisPlayer: boolean) {
            let yOffset = 0;
            if (!isThisPlayer) {
                yOffset = -(height / 2)
            }
            this.deck = new CardZone(true, false, false, 40, 0, 560 + yOffset);
            this.driveDeck = new CardZone(true, false, false, 40, 110, 560 + yOffset);
            this.terminus = new CardZone(true, true, false, 10, 0, 200 + yOffset);
            this.voidZone = new CardZone(true, true, false, 20, 200, 200 + yOffset);
            this.hand = new CardZone(true, true, true, 500, 220, 560 + yOffset);
        }

        update() {
            this.deck.update();
            this.driveDeck.update();
            this.terminus.update();
            this.voidZone.update();
            this.hand.update();
        }
    }

    //CardZone class - basically, a list of cards
    class CardZone {
        private isVisible: boolean;
        private isFaceUp: boolean;
        isHorizontal: boolean;
        size: number;
        xPos: number;
        yPos: number;
        cardList: Array<Card>;

        constructor(visible: boolean, faceUp: boolean, horizontal: boolean, inSize: number, x: number, y: number) {
            this.isVisible = visible;
            this.isHorizontal = horizontal;
            this.size = inSize;
            this.cardList = [];

            this.isFaceUp = faceUp;

            this.xPos = x;
            this.yPos = y;
        }

        update() {
            this.updateCardPositions();
        }

        private updateCardPositions() {
            let len = this.cardList.length;
            let step = this.size / len;
            for (let i = 0; i < len; i++) {
                if (this.isHorizontal == true) {
                    this.cardList[i].setPosition(this.xPos + i * step, this.yPos)
                } else if (this.isHorizontal == false) {
                    this.cardList[i].setPosition(this.xPos, this.yPos - i * step)
                }
                this.cardList[i].update()
            }
        }

        addCard(card: Card) {
            this.cardList.push(card);
        }

        getLength() {
            return this.cardList.length;
        }

        setVisible(visible: boolean) {
            if (this.isVisible == visible)
                return;

            this.cardList.forEach(c => c.container.visible = visible)
        }

        setFacedown(faceUp: boolean) {
            if (this.isFaceUp == faceUp)
                return

            this.cardList.forEach(c => c.isFaceUp == faceUp)
        }
    }

    export class Button {
        clickSound: Howl;
        sprite: PIXI.Sprite;
        onClick: Function;

        constructor(sound: Howl, spr: PIXI.Sprite) {
            this.clickSound = sound;
            this.sprite = spr;
            this.sprite.interactive = true;

            this.sprite.on('mousedown', this.onButtonDown, this).on('touchstart', this.onButtonDown, this);
        }

        onButtonDown() {
            this.clickSound.play();

            this.onClick();
        }
    }
}

//Helpers and global values
module Helpers {
    export function getTypeString(type: number) {
        let toReturn = ""

        if (type & (1 << 0))
            toReturn += "Aura"
        if (type & (1 << 1))
            toReturn += "Drive"
        if (type & (1 << 2))
            toReturn += "Skill"
        if (type & (1 << 3))
            toReturn += "Soul"

        return toReturn
    }

    export let textStyle: PIXI.TextStyle = {
        wordWrap: true,
        wordWrapWidth: 330
    }

    export let flavorTextStyle: PIXI.TextStyle = {
        wordWrap: true,
        wordWrapWidth: 330
    }

    export let typeTextStyle: PIXI.TextStyle = {

    }

    //these dont work...
    export let costTextStyle: PIXI.TextStyle = {
        //align: 'right'
    }

    export let intensityTextStyle: PIXI.TextStyle = {
        //align: 'right'
    }
}