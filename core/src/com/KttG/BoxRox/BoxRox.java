package com.KttG.BoxRox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.Random;

public class BoxRox extends Game {
	public Game game;

	public void create() {
		game = this;
		Assets.create();
		Assets.loadGame();
		setScreen(new Menu(game));
	}

}

/*
* Controls game logic, needs to be split into multiple files
* */

class Menu implements Screen {
	Stage stage = new Stage() {
		public boolean keyDown(int keycode) {
			if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
				Gdx.app.exit();
			}
			return super.keyDown(keycode);
		}
	};
	Game game;
	boolean start = true;
	static float h = Gdx.graphics.getHeight();
	static float w = Gdx.graphics.getWidth();
	TextButton play = new TextButton("Play", Assets.styleButtons);
	TextButton select = new TextButton("Select Ship", Assets.styleButtons);
	TextButton scoreScreen = new TextButton("High Scores", Assets.styleButtons);
	Label test;
	Random r = new Random();
	Group buttonGroup = new Group();
	static Group starGroup = new Group();
	static Camera cam;
	static StretchViewport viewport;

	public Menu(Game game) {
		this.game = game;
	}

	public void show() {
		cam = CameraControl.GetCamera(CameraControl.width, CameraControl.height);
		viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
		stage.setViewport(viewport);
		test = new Label("**This is a work in progress version**\n\n**All coding and art (excluding font)\n was done by Jesse Bilinski**\nVirtual h/w: "+CameraControl.width+" "+CameraControl.height+"\nCamera w/h: "
				+viewport.getScreenWidth()+" "+viewport.getScreenHeight(), Assets.labelTest);
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		play.setWidth(w / 5);
		play.setHeight(h / 12);
		play.setPosition((w / 2) - (play.getWidth() / 2), (h / 1.3f) - (play.getHeight() / 2));
		play.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new GameScreen(game));
				return false;
			}
		});
		select.setWidth(w/4);
		select.setHeight(h/12);
		select.setPosition((w/2)-select.getWidth()/2, (h/1.5f)-select.getHeight()/1.6f);
		select.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new SelectScreen(game));
				return false;
			}
		});
		scoreScreen.setWidth(w/4);
		scoreScreen.setHeight(h/12);
		scoreScreen.setPosition((w/2)-(scoreScreen.getWidth()/2), h/1.95f);
		scoreScreen.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new ScoreScreen(game));
				return false;
			}
		});
		test.setPosition(w/2-test.getWidth()/2, h/3-test.getHeight()/2);
		for (int i = 0; i<Assets.starList.size();i++){
			starGroup.addActor(Assets.starList.get(i).b);
		}
		buttonGroup.addActor(play);
		buttonGroup.addActor(select);
		buttonGroup.addActor(scoreScreen);
		buttonGroup.addActor(test);
		stage.addActor(starGroup);
		stage.addActor(buttonGroup);
	}

	public void render(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Assets.makeStar(10, 3, starGroup);
		Assets.updateStar();
		test.setText("**This is a work in progress version**\n\n**All coding and art (excluding font)\n was done by Jesse Bilinski**\nVirtual h/w: "+CameraControl.width+" "+CameraControl.height+"\nCamera w/h: "
				+viewport.getScreenWidth()+" "+viewport.getScreenHeight());
		stage.act();
		stage.draw();
	}
	
	
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void hide() {
		dispose();

	}

	public void pause() {

	}

	public void resume() {
		
	}

	public void dispose() {

	}
}

class GameScreen extends Game implements Screen {
	static Game game;
	static Stage stage = new Stage(){
		public boolean keyDown(int keycode){
			if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
				Assets.saveGame();
                game.setScreen(new Menu(game));
            }
			return super.keyDown(keycode);
		}
	};
//	static Texture box = Assets.shipSkin.getDrawable("box.png");
//	static Texture k = new Texture(Gdx.files.internal("Textures/boxinvul.png"));
	static Image b = new Image(SelectScreen.selectedImage.getDrawable());
	static Image bF = new Image(SelectScreen.selectedFire.getDrawable());
	static Image[] invulImage = new Image[2];
	boolean isMoving = false, hasMoved = false, spawn = false, paused = false;
	static boolean buffed = false, poisoned = false;
	static boolean invul = false;
	int toggle = 0, spinTimer = 0, buffedBulletTimer = 0, cycleImageTimer;
	static int difficulty = 30;
	public static boolean power0 = false, cycleImage = false, hitBuff = false;
	int diffTimer = 0;
	static int power0Timer = 150;
	public static boolean explode = false;
	public static boolean power1;
	boolean buffedBullet;
	static int power1Timer = 200;
	static int minDifficulty = 10;
	int power2Timer = 0;
	static int bounty = 0;
	int deathCount = 0;
	static int intScore = 0;
	static int topScore = 0;
	int bossTimer = 0;
	int itemTimer = 0, poisonCount = 0;
	String dir = "";
	float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight(), bx = (w/2)-(b.getWidth()/2), by = (h/2)-(b.getHeight()/2), speed = 1, lx, ly, tempx, tempy;
	float accelX = Gdx.input.getAccelerometerX();
	static float gravity =.8f;
	float accelY = Gdx.input.getAccelerometerY();
	Rectangle bArea = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight()), lBarrier = new Rectangle(0, 0, w/44, h);
	Rectangle invulImagebArea;
	Rectangle tBarrier = new Rectangle(0,h-(h/44),w,h/44);
	Rectangle rBarrier = new Rectangle(w-(w/44),0,w/44,h), bBarrier = new Rectangle(0,0,w,h/44);
	static BadBox tempBox;
	static BadBox bossBox = null;
	Items tempItem;
	bullet tempBullet;
	Items bulletItem;
	TextButton scoreText = new TextButton("Score: "+0, Assets.hudStyle), bountyText = new TextButton("Bounty: 0", Assets.hudStyle);
	TextButton topScoreText = new TextButton("Top Score: "+topScore, Assets.hudStyle);
	static ArrayList<BadBox> enemyList = new ArrayList<BadBox>();
	ArrayList<Items> itemList = new ArrayList<Items>();
	ArrayList<bullet> bulletList = new ArrayList<bullet>();
	ArrayList<laser> laserList = new ArrayList<laser>();
	laser tempLaser;
	Random r = new Random();
	long startTime = System.currentTimeMillis()/1000, currentTime = System.currentTimeMillis()/1000, tempTime;
	static ProgressBar pBar = new ProgressBar(0, 150, 1, false, Assets.pBarStyle);
	public static boolean bossBattle = false;
	static Group uiGroup = new Group(), minionGroup = new Group(), bossGroup = new Group(), starGroup = new Group(), playerGroup = new Group(), itemGroup = new Group();
	static int health = 2, laserType = 0, laserTimer = 0, gamePhase = 0, hitBuffTimer = 0, hitBuffCounter = 0;
	static Camera cam;
	static StretchViewport viewport;
	
	GameScreen(Game g) {
		GameScreen.game = g;
	}

	@Override
	public void create() {

	}

	@Override
	public void show() {
		cam = CameraControl.GetCamera(CameraControl.width, CameraControl.height);
		viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
//		stage.setViewport(viewport);
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		invulImage[0] = new Image(Assets.shipSkin.getDrawable("invul"));
		invulImage[1] = new Image(Assets.shipSkin.getDrawable("invul2"));
		invulImagebArea = new Rectangle(b.getX(), b.getY(), invulImage[0].getWidth(), invulImage[0].getHeight());
		b.setPosition((w/2)+(b.getWidth()/2), (h/2)+(b.getHeight()/2));
//		bF.setPosition(b.getX()-b.getHeight()-bF.getHeight(), b.getY()-b.getHeight()-bF.getHeight());
		scoreText.setWidth(w/3);
		bountyText.setWidth(w/3);
		topScoreText.setWidth(w/3);
		topScoreText.setPosition(w-w, h-h);
		scoreText.setPosition(topScoreText.getX()+topScoreText.getWidth(), h-h);
		bountyText.setPosition(scoreText.getX()+scoreText.getWidth(), h-h);
		uiGroup.addActor(bountyText);
		uiGroup.addActor(scoreText);
		uiGroup.addActor(topScoreText);                                			   // USE GROUP TO DRAW ACTORS UNDERNEATH
		playerGroup.addActor(b);
		pBar.setVisible(false);
		pBar.setWidth(b.getWidth());
		pBar.setValue(0);
		playerGroup.addActor(bF);
		playerGroup.addActor(pBar);
		playerGroup.addActor(invulImage[0]);
		playerGroup.addActor(invulImage[1]);
		invulImage[0].setVisible(false);
		invulImage[1].setVisible(false);
		topScore = Assets.topScore[0];
		topScoreText.setText("Top Score: "+topScore);
		for (int i=0;i<Assets.starList.size();i++){
			starGroup.addActor(Assets.starList.get(i).b);
		}
		stage.addActor(starGroup);
		stage.addActor(minionGroup);
		stage.addActor(itemGroup);
		stage.addActor(bossGroup);
		stage.addActor(uiGroup);
		stage.addActor(playerGroup);
		
	}
	public void render(float delta) {
		//System.out.println("w: "+w);
		//System.out.println("h: "+h);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (paused == false){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		isMoving = false;
		if (laserTimer < 100) laserTimer++;
		if (Gdx.input.isKeyPressed(Keys.SPACE)){
			if (laserTimer >= 15){
				tempLaser = makeLaser(b.getX()+b.getWidth()/2, b.getY()+b.getHeight()/2, laserType);
				laserTimer = 0;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
			by += speed;
			dir = "n";
			isMoving = true;
			hasMoved = true;
		}
		if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			by -= speed;
			dir = "s";
			isMoving = true;
			hasMoved = true;
		}
		if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
			bx += speed;
			dir = "e";
			isMoving = true;
			hasMoved = true;
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			bx -= speed;
			dir = "w";
			isMoving = true;
			hasMoved = true;
		}
		if (isMoving == false && speed > 1) {
			if (toggle <= 4) {
				if (lx > bx)
					bx -= speed;
				if (lx < bx)
					bx += speed;
				if (ly > by)
					by -= speed;
				if (ly < by)
					by += speed;
			}
			if (toggle >= 5) {
				if (tempx > bx)
					bx -= speed;
				if (tempx < bx)
					bx += speed;
				if (tempy > by)
					by -= speed;
				if (tempy < by)
					by += speed;
			}

		}
		if (isMoving) toggle++;
		if (toggle > 6)
			toggle = 0;
		if (toggle <= 3) {
			tempx = bx;
			tempy = by;
		}
		if (toggle >= 4) {
			lx = bx;
			ly = by;
		}
		if (speed < 6 && isMoving == true)
			speed += .40;
		else if (isMoving == false && speed > 1)
			speed -= .40;
//		System.out.println(bulletList.size());
		b.setPosition(bx, by);
		invulImage[0].setPosition(bx-7f, by-9);
		invulImage[1].setPosition(bx-7f, by-9);
		invulImagebArea.setPosition(bx-5, by-5);
		accelX = Gdx.input.getAccelerometerX();
		accelY = Gdx.input.getAccelerometerY();
		hasMoved = false;
		itemTimer = r.nextInt(1000)+r.nextInt(6);
		if (itemTimer >= 999){
			itemTimer = 0;
			tempItem = createItem();
			itemGroup.addActor(tempItem.getB());
			itemList.add(tempItem);
		}
		if (bossBattle == false){
			diffTimer++;
		}
		if (diffTimer == difficulty){
			diffTimer = 0;
			spinTimer++;
			if (difficulty > minDifficulty){
				if (spinTimer == 4){
					spinTimer = 0;
					difficulty--;
				}
			}
			tempBox = createBox(false, false, 0);
			bounty+=1;
			enemyList.add(tempBox);
			cleanList();
		}
		//Damage enemies
		for (int i=0;i < laserList.size();i++){
			for (int k=0; k < enemyList.size();k++){
				if (laserList.get(i).bArea.overlaps(enemyList.get(k).bArea) && enemyList.get(k).deadly == true){
					laserList.get(i).alive = false;
					if (enemyList.get(k).vulnerable == true){
						enemyList.get(k).health-=1;
//					System.out.println("Hitting : "+enemyList.get(k).minionDir);
//					System.out.println("Enemy Pos: "+enemyList.get(k).x+" "+enemyList.get(k).y);
						laserTimer+=5;
					}
						if (enemyList.get(k).health == 0){
							enemyList.get(k).alive = false;
//							System.out.println("Enemy Pos: "+enemyList.get(k).x+" "+enemyList.get(k).y);
							if (enemyList.get(k).boss == true){
								GameScreen.intScore+=300;
								GameScreen.bossBattle = false;
								GameScreen.difficulty+=15;
								gamePhase+=1;
								health+=1;
							}
							cleanList();
						}
				}
			}
		}
		for (int i=0;i <enemyList.size();i++) enemyList.get(i).update();
		updatePositions();
		checkDeath();
		lifetime();
		if (power0) power0();
		if (power1){
			power1 = false;
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 0);
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 1);
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 2);
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 3);
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 4);
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 5);
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 6);
				tempBullet = makeBullet(bulletItem.area.getX(), bulletItem.area.getY(), 7);	
				if (poisoned){
					poisoned = false;
					poisonCount = r.nextInt(8);
					if (poisonCount < 4) poisonCount+=4;
					for (int i = 0; i<poisonCount;i++){
						int tempInt = r.nextInt(bulletList.size());
						bulletList.get(tempInt).b.setPosition(-10000, -10000);
						bulletList.get(tempInt).r.setPosition(-10000,-10000);
						bulletList.remove(tempInt);
						bulletList.trimToSize();
//						System.out.println("Poisoned");
					}
				}
		}
		if (bulletList.size() == 0){
			explode = false;
			buffedBullet = false;
		}
		if (explode){
			power1();
			if (buffed){
				buffedBullet = true;
				buffed = false;
			}
			if (buffedBullet){
				buffedBulletTimer+=1;
				
				for (int i = 0;i<bulletList.size();i++){
					if (buffedBulletTimer < 5){
						bulletList.get(i).x = (bulletList.get(i).x+speed);
						bulletList.get(i).y = (bulletList.get(i).y+speed);
					}
					else if (buffedBulletTimer < 10){
						bulletList.get(i).x = (bulletList.get(i).x+speed);
						bulletList.get(i).y = (bulletList.get(i).y-speed);
					}
					else if (buffedBulletTimer < 15){
						bulletList.get(i).x = (bulletList.get(i).x-speed);
						bulletList.get(i).y = (bulletList.get(i).y-speed);
					}
					else if (buffedBulletTimer < 20){
						bulletList.get(i).x = (bulletList.get(i).x-speed);
						bulletList.get(i).y = (bulletList.get(i).y+speed);
				}
					else buffedBulletTimer = 0;
				}

			}
		}
		pBar.setPosition(b.getX(), b.getY()-pBar.getHeight()-12f);
		currentTime = (System.currentTimeMillis() - startTime)/1000;
		if (tempTime != currentTime){
		intScore+=1;
		tempTime = currentTime;
		}
		if (cycleImage){
			cycleImageTimer+=1;
			if (cycleImageTimer <= 10){
				invulImage[0].setVisible(true);
				invulImage[1].setVisible(false);
			} else {
				invulImage[0].setVisible(false);
				invulImage[1].setVisible(true);
			}
			if (cycleImageTimer > 20) cycleImageTimer = 0;
		}
		for(int i=0;i<itemList.size();i++){
			itemList.get(i).update();
		}
		if (SelectScreen.selectedImage.getDrawable() == Assets.shipSkin.getDrawable("ship2-color0")){
			bF.setPosition(b.getX()-1, b.getY()-9);
		} else {
			bF.setPosition(b.getX(), b.getY()-bF.getHeight());
		}
		scoreText.setText("Score: "+intScore);
		bountyText.setText("Bounty: "+bounty);
		for (int i=0;i<Assets.starList.size();i++){
			Assets.starList.get(i).update();
		}
		for (int i=0;i<laserList.size();i++){
			laserList.get(i).update();
			if (laserList.get(i).b.getY()>h+10 || laserList.get(i).alive == false){
				laserList.get(i).b.setPosition(-10000, -10000);
//				stage.removeActor(starList.get(i).b);
				laserList.get(i).b = null;
				laserList.remove(i);
				laserList.trimToSize();
			}
		}
		//Boss Battle -------------************----------------****************--------------------**********************------------------------************
		//Boss Battle -------------************----------------****************--------------------**********************------------------------************
		//Boss Battle -------------************----------------****************--------------------**********************------------------------************
		//Boss Battle -------------************----------------****************--------------------**********************------------------------************
		if (bossBattle == false) bossTimer++;
		if (bossTimer == 2000){
			bossTimer = 0;
			bossBox = createBox(true, false, 0);
			bounty+=10;
			enemyList.add(bossBox);
			cleanList();
		}
		if (r.nextInt(2) == 0){
			Assets.makeStar(15, gamePhase+4, starGroup);
		}else if (r.nextInt(2) == 0){
			Assets.makeStar(15, gamePhase+4, starGroup);
		} else if (r.nextInt(2) == 0){
			Assets.makeStar(15, gamePhase+4, starGroup);
		} else if (r.nextInt(2) == 0){
			Assets.makeStar(15, gamePhase+4, starGroup);
		} else Assets.makeStar(15, r.nextInt(2), starGroup);
		Assets.updateStar();
		if (hitBuff == true){
			hitBuffTimer++;
			hitBuffCounter++;
			if (hitBuffTimer < 5){
				b.setColor(1, 1, 1, 0);
			} else if (hitBuffTimer < 10) b.setColor(1, 1, 1, 1);
			else hitBuffTimer = 0;
			if (hitBuffCounter == 70){
				b.setColor(1, 1, 1, 1);
				hitBuff = false;
				hitBuffTimer = 0;
				hitBuffCounter = 0;
			}
		}
		stage.act();
		stage.draw();
		}
	}
	
	static void takeDamage(){ //Player can survive 1 hit before dying
			health -= 1;
			if (health == 0){
				
			}
	}
	
	static void cleanList(){
		for (int i=0; i < enemyList.size();i++){
			if (enemyList.get(i).alive == false){
				enemyList.get(i).b.setPosition(-10000, -10000);
				enemyList.get(i).bArea.setPosition(-10000, -10000);
				enemyList.get(i).b.remove();
				enemyList.get(i).b.clear();
				enemyList.remove(i);
			}
		}
		//enemyList.trimToSize();
	}
	static BadBox createBox(boolean b, boolean j, int q){
		BadBox k = new BadBox(b, j, q);
		return k;
	}
	Items createItem(){
		Items k = new Items();
		return k;
	}
	void updatePositions(){
		bArea.setPosition(b.getX(), b.getY());
		for (int i=0;i<itemList.size();i++){
			if (itemList.get(i).area.overlaps(lBarrier)) itemList.get(i).b.setX(lBarrier.getX()+(lBarrier.getWidth())); 
			if (itemList.get(i).area.overlaps(rBarrier)) itemList.get(i).b.setX(rBarrier.getX()-(rBarrier.getWidth()*2));
//			if (itemList.get(i).area.overlaps(bBarrier)) itemList.get(i).b.setY(bBarrier.getY()+bBarrier.getHeight());
			if (itemList.get(i).area.overlaps(tBarrier)) itemList.get(i).b.setY(tBarrier.getY()-tBarrier.getHeight()*2);
			itemList.get(i).area.setPosition(itemList.get(i).b.getX(), itemList.get(i).b.getY());
		}
		if (bArea.overlaps(lBarrier)){
			b.setX(lBarrier.getX()+(lBarrier.getWidth()));
			lx = (lBarrier.getX()+lBarrier.getWidth());
			bx = (lBarrier.getX()+lBarrier.getWidth());
			tempx = (lBarrier.getX()+lBarrier.getWidth());
		}
		if (bArea.overlaps(rBarrier)){
			b.setX(rBarrier.getX()-b.getWidth());
			lx = (rBarrier.getX()-b.getWidth());
			bx = (rBarrier.getX()-b.getWidth());
			tempx = (rBarrier.getX()-b.getWidth());
		}
		if (bArea.overlaps(bBarrier)){
			b.setY(bBarrier.getY()+(bBarrier.getHeight()));
			ly = (bBarrier.getY()+(bBarrier.getHeight()));
			by = (bBarrier.getY()+(bBarrier.getHeight()));
			tempy = (bBarrier.getY()+(bBarrier.getHeight()));
		}
		if (bArea.overlaps(tBarrier)){
			b.setY(tBarrier.getY()-(b.getHeight()));
			ly = (tBarrier.getY()-(b.getHeight()));
			by = (tBarrier.getY()-(b.getHeight()));
			tempy = (tBarrier.getY()-(b.getHeight()));
		}
		for (int i =0;i<itemList.size();i++){
			if (bArea.overlaps(itemList.get(i).area)){
				bulletItem = itemList.get(i);
				itemList.get(i).powerUp();
			}
		}
		for (int i=0; i < bulletList.size(); i++){
			tempBullet = bulletList.get(i);
			for (int k=0; k < enemyList.size();k++){
//				if (tempBullet.r.overlaps(enemyList.get(k).bArea)){
//				if (bulletList.get(i) != null && enemyList.get(k) != null)
				if (bulletList.get(i).r.overlaps(enemyList.get(k).bArea) && enemyList.get(k) != bossBox && enemyList.get(k).vulnerable){
//					enemyList.get(k).b.setPosition(-10000, -10000);
//					enemyList.get(k).bArea.setPosition(-10000, -10000);
					enemyList.get(k).alive = false;
					intScore+=5;
					if (enemyList.get(k).big == 5){
						intScore+=bounty;
						bounty = 0;
					} else bounty+=1;
					cleanList();
				}
//				}
			}
		}
		for (int i=0; i < bulletList.size(); i++){
			if (bulletList.get(i).r.getX() >= w+50 || bulletList.get(i).r.getX() <= (w-w)-50 || bulletList.get(i).r.getY() >= h+50 || bulletList.get(i).r.getY() <= (h-h)-50){
				bulletList.get(i).r.setPosition(-10000, -10000);
				bulletList.get(i).b.setPosition(-10000, -10000);
				bulletList.get(i).b.clear();
				bulletList.remove(bulletList.get(i));
				bulletList.trimToSize();
			}
		}
		
	}
	void checkDeath(){
		for (int i = 0;i<4;i++){
			 if((bossBattle && bossBox.bossType == 2 && bossBox.lArea[i].overlaps(bArea)) && invul == false){
				 if (hitBuff == false) health-=1;
				 if (health == 0) reset();
					else hitBuff = true;
//				 buffed = false;
//				 power0Timer = 149;
				 
					
			 }
		}
		for (int i=0; i<enemyList.size();i++){
			if (enemyList.get(i).bArea.overlaps(bArea) && invul == false && enemyList.get(i).deadly == true){
				if (hitBuff == false) health-=1;
				if (health == 0) reset();
				else hitBuff = true;
				break;
		}
			if (enemyList.get(i).bArea.overlaps(invulImagebArea) && invul == true && enemyList.get(i) != bossBox && enemyList.get(i).vulnerable == true && (enemyList.get(i).minion == false)){
			enemyList.get(i).alive = false;
			intScore+=5;
			if (enemyList.get(i).big == 5){
				intScore+=bounty;
				bounty = 0;
			} else bounty+=1;
			cleanList();
		}
		}
	}
	void reset(){
		b.setPosition((w/2)-(b.getWidth()/2), (h/2)-(b.getHeight()/2));
		bx = b.getX();
		by = b.getY();
		lx = b.getX();
		ly = b.getY();
		difficulty = 30;
		diffTimer = 0;
		bossTimer = 0;
		tempx = b.getX();
		tempy = b.getY();
		deathCount++;
		buffed = false;
		poisoned = false;
		bossBattle = false;
		bossBox = null;
		gamePhase = 0;
		health = 2;
		for (int i=0; i<10;i++){
			if (intScore > Assets.topScore[i]){
				Assets.topScore[9] = intScore;
				ScoreScreen.sort();
				break;
			}
		}
		if (intScore > topScore) topScore = intScore;
		topScoreText.setText("Top Score: "+topScore);
		Assets.saveGame();
		intScore = 0;
		bounty = 0;
		for (int i=0; i<enemyList.size();i++){
			enemyList.get(i).dispose();
		}
		for (int i=0; i<itemList.size();i++){
			itemList.get(i).b.setPosition(-10000, -10000);
		}
		for (int i=0; i<Assets.starList.size();i++){
			Assets.starList.get(i).b.setPosition(-10000, -10000);
		}
		bossGroup.setPosition(-10000, -10000);
		bossGroup.clear();
		Assets.starList.clear();
		itemList.clear();
		enemyList.clear();
		Assets.initStar();
		for (int i = 0; i<Assets.starList.size();i++){
			starGroup.addActor(Assets.starList.get(i).b);
		}
	}
	void lifetime(){
		for(int i = 0; i < itemList.size();i++){
			itemList.get(i).life--;
			if (itemList.get(i).life <= 0) {
				itemList.get(i).alive = false;
//				System.out.println("Removing");
			}
		}
		for(int i=0;i<itemList.size();i++){
			if (itemList.get(i).alive == false){
				itemList.get(i).b.setPosition(-10000, -10000);
				itemList.get(i).b.remove();
				itemList.get(i).b = null;
//				itemList.get(i).t = null;
				itemList.remove(i);
//				System.out.println("Removing");
			}
		}
			itemList.trimToSize();
		
		}
	void power0(){
		power0Timer++;
		pBar.setValue(pBar.getValue()-1);
		Items.pBarValue-=1;
		if (power0Timer == 150){
			pBar.setVisible(false);
			power0 = false;
			invul = false;
			cycleImage = false;
			invulImage[0].setVisible(false);
			invulImage[1].setVisible(false);
		}
	}
	void power1(){
		for (int i = 0;i<bulletList.size();i++){
			bulletList.get(i).move();
		}
	}
		

	public void hide() {
		//paused = true;
	}
	bullet makeBullet(float x, float y, int typ){
		bullet k = new bullet(x, y, typ);
		itemGroup.addActor(k.b);
		bulletList.add(k);
		return k;
	}
	laser makeLaser(float x, float y, int typ){
		laser k = new laser(x, y, typ);
		playerGroup.addActor(k.b);
		laserList.add(k);
		return k;
	}
}

class bullet {
	Texture t;
	Image b;
	int type;
	Rectangle r;
	float x, y;
	float w = Gdx.graphics.getWidth();
	float h = Gdx.graphics.getHeight();
	bullet(float x, float y, int typ){
//		t = new Texture(Gdx.files.internal("Textures/item1.png"));
		b = new Image(Assets.shipSkin.getDrawable("item10"));
		b.setPosition(x,y);
		this.x = x;
		this.y = y;
		type = typ;
		r = new Rectangle(x, y, b.getWidth(), b.getHeight());
	}
	void move(){
		if (type == 0){
			y+=3;
		}
		if (type == 1){
			x+=3;
			y+=3;
		}
		if (type == 2){
			x+=3;
		}
		if (type == 3){
			x+=3;
			y-=3;
		}
		if (type == 4){
			y-=3;
		}
		if (type == 5){
			x-=3;
			y-=3;
		}
		if (type == 6){
			x-=3;
		}
		if (type == 7){
			x-=3;
			y+=3;
		}
		b.setPosition(x, y);
		r.setPosition(x, y);
	}
}

	
		class laser {  //Player Shooting
			float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight(), x, y;
			Image b;
			int type = 0;
			boolean alive = true;
			Rectangle bArea = new Rectangle();
			public laser(float xx, float yy, int typ){
				x = xx;
				y = yy;
				type = typ;
				if (type == 0){
					b = new Image(Assets.shipSkin.getDrawable("laser0"));
				}
				x-=b.getWidth()/2;
				bArea.setSize(b.getWidth(), b.getHeight());
			}
			public void update(){
				y+=7.5f;
				b.setPosition(x, y);
				bArea.setPosition(x, y);
			}
		}