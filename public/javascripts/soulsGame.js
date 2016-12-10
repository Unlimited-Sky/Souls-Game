var width = 1280;
var height = 720;
var Renderer;
(function (Renderer) {
    var renderer = PIXI.autoDetectRenderer(width, height, { backgroundColor: 0x10bb99 });
    document.body.appendChild(renderer.view);
    var stage = new PIXI.Container();
    var nextButton;
    PIXI.loader
        .add('nextButton', '../assets/images/nextButton.png')
        .add('cardBackground', '../assets/images/cardBackground.png')
        .add('cardBackface', '../assets/images/cardBackface.png')
        .once('complete', initScreen);
    PIXI.loader.load();
    function initScreen() {
        initNextButton();
        gameLoop();
    }
    function gameLoop() {
        Game.update();
        renderer.render(stage);
        requestAnimationFrame(gameLoop);
    }
    function initNextButton() {
        nextButton = new GameObjects.Button(new Howl({ src: ['../assets/sounds/Magic Game Proceed UI Tap.wav'] }), new PIXI.Sprite(PIXI.loader.resources.nextButton.texture));
        nextButton.sprite.position.x = 1100;
        nextButton.sprite.position.y = 550;
        nextButton.onClick = function () {
            Websocket.doSend({ msgType: "nextButtonClicked" });
        };
        stage.addChild(nextButton.sprite);
    }
    function addToStage(toAdd) {
        stage.addChild(toAdd);
    }
    Renderer.addToStage = addToStage;
})(Renderer || (Renderer = {}));
var Websocket;
(function (Websocket) {
    var websocket;
    function initWebsocket(websocketPath) {
        var websocketURL = "ws://" + window.location.host + websocketPath;
        websocket = new WebSocket(websocketURL);
        websocket.onopen = function (evt) { return onOpen(evt); };
        websocket.onclose = function (evt) { return onClose(evt); };
        websocket.onmessage = function (evt) { return onMessage(evt); };
        websocket.onerror = function (evt) { return onError(evt); };
    }
    Websocket.initWebsocket = initWebsocket;
    function onOpen(evt) {
        writeToScreen("You have connected.");
    }
    function onClose(evt) {
        writeToScreen("You have disconnected.");
    }
    function onMessage(evt) {
        var json = jQuery.parseJSON(evt.data);
        var type = json.type;
        var html;
        if (type == "textMessage") {
            html = json.user + ": " + json.message;
        }
        else if (type == "gameStateMessage") {
            Game.onRecGameState(json.message);
        }
        else if (type == "playerUIDMessage") {
            html = "You are Player UID: " + json.uid;
            Game.setThisPlayerUID(json.uid);
        }
        else {
            html = "MSG ERROR: " + json.message;
        }
        if (html != undefined)
            writeToScreen(html);
    }
    function onError(evt) {
        writeToScreen("ERROR: " + evt.data);
    }
    function writeToScreen(html) {
        var htmlLine = html + "\n";
        $('#gameLog').append(htmlLine);
    }
    function doSend(data) {
        websocket.send(JSON.stringify(data));
    }
    Websocket.doSend = doSend;
})(Websocket || (Websocket = {}));
var Game;
(function (Game) {
    var players = Array();
    var cards = Array();
    var thisPlayerUID;
    function update() {
        players.forEach(function (p) {
            p.playerZones.update();
        });
    }
    Game.update = update;
    function onRecGameState(gs) {
        players = [];
        cards = [];
        var json = jQuery.parseJSON(gs);
        var playerUIDs = Array();
        var cardUIDs = Array();
        Object.keys(json.CardData).forEach(function (cKey) { return cardUIDs.push(cKey); });
        cardUIDs.forEach(function (c) {
            cards.push(buildCardFromGameState(c, json));
        });
        Object.keys(json.HasPlayerData).forEach(function (pKey) { return playerUIDs.push(pKey); });
        playerUIDs.forEach(function (p) {
            players.push(buildPlayerDataFromGameState(p, json));
        });
        players.forEach(function (p) { return console.log(p); });
    }
    Game.onRecGameState = onRecGameState;
    function setThisPlayerUID(uid) {
        thisPlayerUID = uid;
        console.log("Player UID: " + thisPlayerUID);
    }
    Game.setThisPlayerUID = setThisPlayerUID;
    function getThisPlayerUID() {
        return thisPlayerUID;
    }
    Game.getThisPlayerUID = getThisPlayerUID;
    function buildPlayerDataFromGameState(uid, gs) {
        var pd = new GameObjects.PlayerData(gs.HasName[uid].name, uid);
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
    function buildCardListFromGameStateArray(data) {
        var toReturn = new Array();
        var ids = data.map(function (d) { return d.UID; });
        ids.forEach(function (id) {
            cards.forEach(function (c) {
                if (id == c.UID) {
                    c.loadRenderData();
                    toReturn.push(c);
                }
            });
        });
        return toReturn;
    }
    function buildCardFromGameState(uid, gs) {
        var cd = new GameObjects.Card(gs.CardData[uid]);
        cd.UID = uid;
        cd.name = gs.HasName[uid].name;
        cd.type = Helpers.getTypeString(gs.HasType[uid].type);
        cd.intensity = gs.CardData[uid].intensity;
        cd.cost = gs.CardData[uid].cost;
        cd.cardText = gs.CardData[uid].cardText;
        cd.flavorText = gs.CardData[uid].flavorText;
        return cd;
    }
})(Game || (Game = {}));
var GameObjects;
(function (GameObjects) {
    var PlayerData = (function () {
        function PlayerData(playerName, uid) {
            this.UID = uid;
            this.playerName = playerName;
            this.currentLife = 0;
            this.intensity = "";
            this.power = 0;
            this.personaCount = 0;
            this.driveCount = 0;
            this.playerZones = new PlayerZones(uid == Game.getThisPlayerUID());
        }
        return PlayerData;
    }());
    GameObjects.PlayerData = PlayerData;
    var Card = (function () {
        function Card(id) {
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
            this.sound = new Howl({ src: ['../assets/sounds/cardSlide6.ogg'] });
        }
        Card.prototype.onCardDown = function () {
            this.sound.play();
        };
        Card.prototype.loadRenderData = function () {
            this.dName = new PIXI.Text(this.name);
            this.dBackground = new PIXI.Sprite(PIXI.loader.resources.cardBackground.texture);
            this.dArt = new PIXI.Sprite();
            this.dType = new PIXI.Text(this.type, Helpers.typeTextStyle);
            this.dCost = new PIXI.Text(this.cost.toString(), Helpers.costTextStyle);
            this.dIntensity = new PIXI.Text("RRR", Helpers.intensityTextStyle);
            this.dText = new PIXI.Text(this.cardText, Helpers.textStyle);
            this.dFlavorText = new PIXI.Text(this.flavorText, Helpers.flavorTextStyle);
            this.container.addChild(this.dBackground);
            this.container.addChild(this.dName);
            this.container.addChild(this.dType);
            this.container.addChild(this.dText);
            this.container.addChild(this.dCost);
            this.container.addChild(this.dIntensity);
            this.container.addChild(this.dFlavorText);
            this.container.addChild(this.dArt);
            this.dName.position.x = 35;
            this.dName.position.y = 30;
            this.dType.position.x = 45;
            this.dType.position.y = 270;
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
            Renderer.addToStage(this.container);
        };
        Card.prototype.update = function () {
            if (this.isFaceUp == true) {
                this.makeFaceUp();
            }
            else if (this.isFaceUp == false) {
                this.makeFaceDown();
            }
        };
        Card.prototype.setPosition = function (xPos, yPos) {
            this.container.x = xPos;
            this.container.y = yPos;
        };
        Card.prototype.makeFaceUp = function () {
            this.dName.visible = true;
            this.dBackground.visible = true;
            this.dArt.visible = true;
            this.dType.visible = true;
            this.dCost.visible = true;
            this.dIntensity.visible = true;
            this.dText.visible = true;
            this.dFlavorText.visible = true;
            this.dBackface.visible = false;
        };
        Card.prototype.makeFaceDown = function () {
            this.dName.visible = false;
            this.dBackground.visible = false;
            this.dArt.visible = false;
            this.dType.visible = false;
            this.dCost.visible = false;
            this.dIntensity.visible = false;
            this.dText.visible = false;
            this.dFlavorText.visible = false;
            this.dBackface.visible = true;
        };
        return Card;
    }());
    GameObjects.Card = Card;
    var PlayerZones = (function () {
        function PlayerZones(isThisPlayer) {
            var yOffset = 0;
            if (!isThisPlayer) {
                yOffset = -(height / 2);
            }
            this.deck = new CardZone(true, false, false, 40, 0, 560 + yOffset);
            this.driveDeck = new CardZone(true, false, false, 40, 110, 560 + yOffset);
            this.terminus = new CardZone(true, true, false, 10, 0, 200 + yOffset);
            this.voidZone = new CardZone(true, true, false, 20, 200, 200 + yOffset);
            this.hand = new CardZone(true, true, true, 500, 220, 560 + yOffset);
        }
        PlayerZones.prototype.update = function () {
            this.deck.update();
            this.driveDeck.update();
            this.terminus.update();
            this.voidZone.update();
            this.hand.update();
        };
        return PlayerZones;
    }());
    var CardZone = (function () {
        function CardZone(visible, faceUp, horizontal, inSize, x, y) {
            this.isVisible = visible;
            this.isHorizontal = horizontal;
            this.size = inSize;
            this.cardList = [];
            this.isFaceUp = faceUp;
            this.xPos = x;
            this.yPos = y;
        }
        CardZone.prototype.update = function () {
            this.updateCardPositions();
        };
        CardZone.prototype.updateCardPositions = function () {
            var len = this.cardList.length;
            var step = this.size / len;
            for (var i = 0; i < len; i++) {
                if (this.isHorizontal == true) {
                    this.cardList[i].setPosition(this.xPos + i * step, this.yPos);
                }
                else if (this.isHorizontal == false) {
                    this.cardList[i].setPosition(this.xPos, this.yPos - i * step);
                }
                this.cardList[i].update();
            }
        };
        CardZone.prototype.addCard = function (card) {
            this.cardList.push(card);
        };
        CardZone.prototype.getLength = function () {
            return this.cardList.length;
        };
        CardZone.prototype.setVisible = function (visible) {
            if (this.isVisible == visible)
                return;
            this.cardList.forEach(function (c) { return c.container.visible = visible; });
        };
        CardZone.prototype.setFacedown = function (faceUp) {
            if (this.isFaceUp == faceUp)
                return;
            this.cardList.forEach(function (c) { return c.isFaceUp == faceUp; });
        };
        return CardZone;
    }());
    var Button = (function () {
        function Button(sound, spr) {
            this.clickSound = sound;
            this.sprite = spr;
            this.sprite.interactive = true;
            this.sprite.on('mousedown', this.onButtonDown, this).on('touchstart', this.onButtonDown, this);
        }
        Button.prototype.onButtonDown = function () {
            this.clickSound.play();
            this.onClick();
        };
        return Button;
    }());
    GameObjects.Button = Button;
})(GameObjects || (GameObjects = {}));
var Helpers;
(function (Helpers) {
    function getTypeString(type) {
        var toReturn = "";
        if (type & (1 << 0))
            toReturn += "Aura";
        if (type & (1 << 1))
            toReturn += "Drive";
        if (type & (1 << 2))
            toReturn += "Skill";
        if (type & (1 << 3))
            toReturn += "Soul";
        return toReturn;
    }
    Helpers.getTypeString = getTypeString;
    Helpers.textStyle = {
        wordWrap: true,
        wordWrapWidth: 330
    };
    Helpers.flavorTextStyle = {
        wordWrap: true,
        wordWrapWidth: 330
    };
    Helpers.typeTextStyle = {};
    Helpers.costTextStyle = {};
    Helpers.intensityTextStyle = {};
})(Helpers || (Helpers = {}));
