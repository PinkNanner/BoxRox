package com.KttG.BoxRox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Random;

public 	class Star {
	Image b;
	float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight(), x, y;
	int typ, frame, num0;
	float fade = 1;
	Random r = new Random();
	boolean up = true, down = false, formation = false;
	
	Star(int type){
		typ = type;
		fade = ((float)r.nextInt(10)/100)+.5f;
		 if (typ < 1 || typ > 8){
				if (r.nextInt(1000)<= 910) b = new Image(Assets.starSkin.getDrawable("star"+r.nextInt(7)+""+r.nextInt(2)));
				else {
					b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
					b.setSize(r.nextInt(40)+5, r.nextInt(40)+5);
					formation = true;
				}
				b.setPosition((float)r.nextInt((int)w), h+50);
			} else if (typ == 1){
			b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
			b.setPosition((float)r.nextInt((int)w), h+50);
			formation = true;
		}
		else if (typ == 2){
			b = new Image(Assets.starSkin.getDrawable("star0"+r.nextInt(2)));
			b.setPosition((float)r.nextInt((int)w), (float)r.nextInt((int)h));
		}
		else if (typ == 3){
			if (r.nextInt(1000)<= 980) b = new Image(Assets.starSkin.getDrawable("star0"+""+r.nextInt(2)));
			else {
				b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
				b.setSize(r.nextInt(40)+5, r.nextInt(40)+5);
				formation = true;
			}
			b.setPosition((float)r.nextInt((int)w), h+50);
		} else if (typ == 4){
			if (r.nextBoolean() == true)num0 = 0; else num0 = 4;
			if (r.nextInt(1000)<= 980) b = new Image(Assets.starSkin.getDrawable("star"+num0+""+r.nextInt(2)));
			else {
				b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
				b.setSize(r.nextInt(40)+5, r.nextInt(40)+5);
				formation = true;
			}
			b.setPosition((float)r.nextInt((int)w), h+50);
		} else if (typ == 5){
			if (r.nextBoolean() == true)num0 = 2; else num0 = 4;
			if (r.nextInt(1000)<= 980) b = new Image(Assets.starSkin.getDrawable("star"+num0+""+r.nextInt(2)));
			else {
				b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
				b.setSize(r.nextInt(40)+5, r.nextInt(40)+5);
				formation = true;
			}
			b.setPosition((float)r.nextInt((int)w), h+50);
		} else if (typ == 6){
			if (r.nextBoolean() == true)num0 = 2; else num0 = 3;
			if (r.nextInt(1000)<= 980) b = new Image(Assets.starSkin.getDrawable("star"+num0+""+r.nextInt(2)));
			else {
				b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
				b.setSize(r.nextInt(40)+5, r.nextInt(40)+5);
				formation = true;
			}
			b.setPosition((float)r.nextInt((int)w), h+50);
		} else if (typ == 7){
			if (r.nextBoolean() == true)num0 = 5; else num0 = 3;
			if (r.nextInt(1000)<= 980) b = new Image(Assets.starSkin.getDrawable("star"+num0+""+r.nextInt(2)));
			else {
				b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
				b.setSize(r.nextInt(40)+5, r.nextInt(40)+5);
				formation = true;
			}
			b.setPosition((float)r.nextInt((int)w), h+50);
		}  else if (typ == 8){
			if (r.nextBoolean() == true)num0 = 5; else num0 = 6;
			if (r.nextInt(1000)<= 980) b = new Image(Assets.starSkin.getDrawable("star"+num0+""+r.nextInt(2)));
			else {
				b = new Image(Assets.starSkin.getDrawable("starFormation"+r.nextInt(8)));
				b.setSize(r.nextInt(40)+5, r.nextInt(40)+5);
				formation = true;
			}
			b.setPosition((float)r.nextInt((int)w), h+50);
		}
		b.setRotation(r.nextInt(361));
		x = b.getX();
		y = b.getY();
		
	}
	
	public void update(){
		y-=.4f;
		b.setPosition(x, y);
		b.setColor(1, 1, 1, fade);
		if (r.nextInt(40) == 0 && formation == false){
		if (up == true){
			fade+=.1f;
			if (fade >= 1.5f){
				up = false;
				down = true;
			}
		}
		if (down == true){
			fade-=.1f;
			if (fade <= 0){
				down = false;
				up = true;
			}	
		}
	}
	}
}