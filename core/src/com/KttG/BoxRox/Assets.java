package com.KttG.BoxRox;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Assets {
	static TextureAtlas atlasButtons = new TextureAtlas(Gdx.files.internal("buttons/colorButtons.pack"));
	static TextureAtlas atlasStars = new TextureAtlas(Gdx.files.internal("textures/stars/stars.pack"));
	static TextureAtlas atlasLarge = new TextureAtlas(Gdx.files.internal("textures/large/large.pack"));
	static TextureAtlas atlasMinions = new TextureAtlas(Gdx.files.internal("textures/minions/minions.pack"));
	static TextureAtlas atlasShips = new TextureAtlas(Gdx.files.internal("textures/ships&ui/ships.pack"));
	static TextButtonStyle styleButtons;
	static TextButtonStyle hudStyle;
	static LabelStyle styleLabel, labelTest;
	static Skin buttonSkin = new Skin();
	static Skin shipSkin = new Skin();
	static Skin minionSkin = new Skin();
	static Skin largeSkin = new Skin();
	static Skin starSkin = new Skin();
	static BitmapFont fontBlack;
	static ProgressBarStyle pBarStyle;
	static Preferences prefs = Gdx.app.getPreferences("com-KttG-BoxRox-savedData");
	static String[] topScoreString = new String[10];
	static public int[] topScore = new int[10];
	static ArrayList<Star> starList = new ArrayList<Star>();
	static Star tempStar;
	static float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
	static Random r = new Random();
	
	public static void create(){
		fontBlack = new BitmapFont(Gdx.files.internal("fonts/whiteSmall.fnt"), false);
		minionSkin.addRegions(atlasMinions);
		largeSkin.addRegions(atlasLarge);
		starSkin.addRegions(atlasStars);
		buttonSkin.addRegions(atlasButtons);
		shipSkin.addRegions(atlasShips);
		styleButtons = new TextButtonStyle();
		styleButtons.down = buttonSkin.getDrawable("whiteButtonPressed");
		styleButtons.up = buttonSkin.getDrawable("whiteButton");
		styleButtons.font = fontBlack;
		hudStyle = new TextButtonStyle();
		hudStyle.font = fontBlack;
		hudStyle.up = shipSkin.getDrawable("hud");
		styleLabel = new LabelStyle();
		styleLabel.font = fontBlack;
		styleLabel.fontColor = Color.WHITE;
		styleLabel.background = shipSkin.getDrawable("hud");
		labelTest = new LabelStyle();
		labelTest.font = fontBlack;
		pBarStyle = new ProgressBarStyle();
		pBarStyle.background = shipSkin.getDrawable("pBar");
		pBarStyle.knob = shipSkin.getDrawable("pBar-knob");
		pBarStyle.knobBefore = shipSkin.getDrawable("pBar-before");
		pBarStyle.knobAfter = shipSkin.getDrawable("pBar-after");
		initStar();
	}
	static void initStar(){
		for (int i=0;i<80;i++){
			Assets.tempStar = createStar(2);
			Menu.starGroup.addActor(Assets.tempStar.b);
			starList.add(tempStar);
			tempStar = null;
		}
	}
	static void makeStar(int frequency,int type, Group starGroup){
		if (r.nextInt(frequency) == 0){
			tempStar = createStar(type);//(r.nextInt(2));
			starGroup.addActor(tempStar.b);
			starList.add(tempStar);
			tempStar = null;
		}
	}
	static void updateStar(){
		for (int i = 0; i<starList.size();i++){
			starList.get(i).update();
			if (starList.get(i).b.getY()<h-h-10){
				starList.get(i).b.setPosition(-10000, -10000);
//				stage.removeActor(starList.get(i).b);
				starList.get(i).b = null;
				starList.remove(i);
				starList.trimToSize();
			}
		}
	}
	static Star createStar(int i){
		Star k = new Star(i);
		return k;
	}
	public static void saveGame(){
		System.out.println("Saving... ");
		prefs.putInteger("topScore", GameScreen.topScore);
		prefs.putString("selectedShip", SelectScreen.selectedImage.getDrawable().toString());
		prefs.putString("selectedFire",  SelectScreen.selectedFire.getDrawable().toString());
		prefs.putFloat("selectionLocationX", SelectScreen.selectionImage.getX());
		prefs.putFloat("selectionLocationY", SelectScreen.selectionImage.getY());
		System.out.println(prefs.getString("selectedShip"));
		for (int i=0;i<10;i++){
			prefs.putInteger("topScoreList"+i, topScore[i]);
		}
		prefs.flush();
	}
	public static void loadGame(){
		System.out.println("Loading... "+prefs.getString("selectedShip"));
		if (prefs.getString("selectedShip") != ""){
			SelectScreen.selectedImage.setDrawable(shipSkin.getDrawable(prefs.getString("selectedShip")));
			SelectScreen.selectedFire.setDrawable(shipSkin.getDrawable(prefs.getString("selectedFire")));
			SelectScreen.selectionImage.setPosition(prefs.getFloat("selectionLocationX"), prefs.getFloat("selectionLocationY"));
		} else {
			SelectScreen.selectedImage.setDrawable(shipSkin.getDrawable("ship0-color0"));
			SelectScreen.selectedFire.setDrawable(shipSkin.getDrawable("ship0-fire0"));
			GameScreen.b.setDrawable(shipSkin.getDrawable("ship0-color0"));
			GameScreen.bF.setDrawable(shipSkin.getDrawable("ship0-fire0"));
			prefs.putString("selectedShip", SelectScreen.selectedImage.getDrawable().toString());
			prefs.putString("selectedFire", SelectScreen.selectedFire.getDrawable().toString());
			SelectScreen.selectionImage.setPosition(-100, -100);
		}
		for (int i=0;i<10;i++){
			topScore[i] = prefs.getInteger("topScoreList"+i);
		}
		System.out.println("Selected Fire = "+SelectScreen.selectedFire.getDrawable().toString());
		ScoreScreen.sort();
		
	}
}
