var renderer = PIXI.autoDetectRenderer(1280, 720,{backgroundColor : 0x1099bb});
document.body.appendChild(renderer.view);

// create the root of the scene graph
var stage = new PIXI.Container();

var loader = PIXI.loader;
loader.add('cardArt', 'images/cardArt.jpg');
loader.add('cardBackground', 'images/cardBackground.png');
loader.once('complete', onAssetsLoaded);
loader.load();

var card = {
	name: "Priest",
	cost: 3,
	intensity: "w",
	type: "Soul",
	rarity: "c",
	attackPower:  2,
	health: 2,
	text: "Rally: Heal 2 damage.",
	container: new PIXI.Container()
};

stage.addChild(card.container);
animate();

function onAssetsLoaded() {
	var cardBG = new PIXI.Sprite(PIXI.loader.resources.cardBackground.texture);
	var cardIMG = new PIXI.Sprite(PIXI.loader.resources.cardArt.texture);
	var scaleFactor = cardBG.width / cardIMG.width;
	cardIMG.scale.x = cardIMG.scale.y = scaleFactor;
	cardIMG.y = 40;
	var cardName = new PIXI.Text("White Dude");
	cardName.x = 35;
	cardName.y = 30;

	card.container.addChild(cardIMG);
	card.container.addChild(cardBG);
	card.container.addChild(cardName);
}

function animate() {
    requestAnimationFrame(animate);

    renderer.render(stage);
}