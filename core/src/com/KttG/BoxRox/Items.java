package com.KttG.BoxRox;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Items{
	float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
	float gravity = GameScreen.gravity;
	Random r = new Random();
	int type = r.nextInt(3), life = 300, frame, framePos;
	static int pBarValue = 0;
	boolean alive = true, good = false;
//	Texture t;
	Image b;
	Rectangle area;
	
	Items(){
//		t = new Texture(Gdx.files.internal("Textures/item"+type+".png"));
		b = new Image( Assets.shipSkin.getDrawable("item"+type+"0"));
		b.setPosition(r.nextInt((int) w), r.nextInt((int) h));
		area = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
	}
	public void update(){
		b.setY(b.getY()-gravity);
		if (type == 0){
			frame++;
			if (frame >= life/20){
				frame = 0;
				framePos+=1;
				if (framePos == 4) framePos = 0;
				b.setDrawable(Assets.shipSkin.getDrawable("item"+type+""+framePos));
			}
		}
	}

	public Image getB() {
		return b;
	}

	public void setB(Image b) {
		this.b = b;
	}
	void powerUp(){
		alive = false;
		if (r.nextBoolean() == true && GameScreen.difficulty <= 25)GameScreen.difficulty+=5;
		int tempInt;
		if(type == 0){
			GameScreen.invul = true;
//			GameScreen.b.setDrawable(Assets.shipSkin.getDrawable("boxinvul"));
			GameScreen.cycleImage = true;
//			GameScreen.invulImage.setVisible(true);
			GameScreen.power0  = true;
			GameScreen.power0Timer-=150;
			GameScreen.pBar.setVisible(true);
			pBarValue+=150;
			
			if (GameScreen.buffed){
				GameScreen.buffed = false;
				GameScreen.power0Timer-=75;
				pBarValue+=75;
			}
			if (GameScreen.poisoned){
				GameScreen.poisoned = false;
				GameScreen.power0Timer+=75;
				pBarValue-=75;
				tempInt = r.nextInt(76);
				GameScreen.power0Timer+=tempInt;
				pBarValue-=tempInt;
			}
//			GameScreen.pBar.setWidth(GameScreen.pBar.getValue());
			if (pBarValue != GameScreen.pBar.getMaxValue())
				GameScreen.pBar.setRange(0, pBarValue);
			GameScreen.pBar.setValue(pBarValue);

		}
		if(type == 1){
			GameScreen.explode = true;
			GameScreen.power1 = true;
		}
		if(type == 2){
			GameScreen.buffed = true;
		}
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}