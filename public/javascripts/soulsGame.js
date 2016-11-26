var Renderer;
(function (Renderer) {
    var renderer = PIXI.autoDetectRenderer(1280, 720, { backgroundColor: 0x10bb99 });
    document.body.appendChild(renderer.view);
    var stage = new PIXI.Container();
    animate();
    function animate() {
        requestAnimationFrame(animate);
        renderer.render(stage);
    }
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
        console.log("json: " + json);
        var type = json.type;
        var html;
        if (type == "textMessage") {
            html = json.user + ": " + json.message;
        }
        else {
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
})(Websocket || (Websocket = {}));
var GameObjects;
(function (GameObjects) {
    var PlayerData = (function () {
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
        }
        PlayerData.prototype.loadDeck = function () {
        };
        return PlayerData;
    }());
    var Card = (function () {
        function Card(entityID, cardID) {
            this.entityID = entityID;
            this.cardID = cardID;
        }
        Card.prototype.addToStage = function () {
        };
        return Card;
    }());
    var CardZone = (function () {
        function CardZone(isVisible) {
            this.isVisible = isVisible;
            this.cardList = [];
        }
        CardZone.prototype.addCard = function (card) {
            this.cardList.push(card);
        };
        CardZone.prototype.getLength = function () {
            return this.cardList.length;
        };
        return CardZone;
    }());
})(GameObjects || (GameObjects = {}));
