package com.KttG.BoxRox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Random;

/*
* Lets you select from a list of ships
* */
public class SelectScreen extends Game implements Screen{
	Game game;
	Table table = new Table();
	Stage stage = new Stage(){
		public boolean keyDown(int keycode){
			if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
                game.setScreen(new Menu(game));
            }
			return super.keyDown(keycode);
		}
	};
	float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight(), buttonWidth = (w/11), buttonHeight = (h/12);
	static int shipCount = 8;
	int frame = 0;
	String s = null;
	Image[][] ship = new Image[shipCount][2], fire = new Image[shipCount][2];
	static Image selectionImage = new Image(Assets.shipSkin.getDrawable("selectImage"));
	static Image selectedImage = new Image();
	static Image selectedFire = new Image();
	TextButton back = new TextButton("Accept", Assets.styleButtons);
	float tempX, tempY;
	static Vector2[][] shipLocation = new Vector2[shipCount][2];
	Random r = new Random();
	Group shipGroup = new Group(), starGroup = new Group();
	
	
	public SelectScreen(final Game game){
		this.game = game;
		for (int i=0;i<shipCount;i++){
					ship[i][0] = new Image(Assets.shipSkin.getDrawable("ship"+i+"-color0"));
					fire[i][0] = new Image(Assets.shipSkin.getDrawable("ship"+i+"-fire0"));
		}
		ship[0][1] = new Image(Assets.shipSkin.getDrawable("ship0-color1"));
		ship[1][1] = new Image(Assets.shipSkin.getDrawable("ship1-color1"));
		ship[2][1] = new Image(Assets.shipSkin.getDrawable("ship1-color1"));
		ship[3][1] = new Image(Assets.shipSkin.getDrawable("ship3-color1"));
		ship[4][1] = new Image(Assets.shipSkin.getDrawable("ship4-color1"));
		ship[5][1] = new Image(Assets.shipSkin.getDrawable("ship5-color1"));
		ship[6][1] = new Image(Assets.shipSkin.getDrawable("ship6-color1"));
		ship[7][1] = new Image(Assets.shipSkin.getDrawable("ship7-color1"));
		
		fire[0][1] = new Image(Assets.shipSkin.getDrawable("ship0-fire1"));
		fire[1][1] = new Image(Assets.shipSkin.getDrawable("ship1-fire1"));
		fire[2][1] = new Image(Assets.shipSkin.getDrawable("ship1-fire1"));
		fire[3][1] = new Image(Assets.shipSkin.getDrawable("ship3-fire1"));
		fire[4][1] = new Image(Assets.shipSkin.getDrawable("ship1-fire1"));
		fire[5][1] = new Image(Assets.shipSkin.getDrawable("ship5-fire1"));
		fire[6][1] = new Image(Assets.shipSkin.getDrawable("ship6-fire1"));
		fire[7][1] = new Image(Assets.shipSkin.getDrawable("ship7-fire1"));
//		stage.addActor(ship[0][0]);
//		stage.addActor(ship[1][0]);
//		stage.addActor(ship[2][0]);
//		stage.addActor(ship[0][1]);
//		stage.addActor(ship[1][1]);
//		stage.addActor(ship[2][1]);
		table.setFillParent(true);
//		table.setDebug(true);
		for (int i = 0;i<ship.length;i++){
			for (int k = 0;k<ship[i].length;k++){
				shipLocation[i][k] = new Vector2();
			}
		}
		for (int i = 0;i<ship.length;i++){
			for (int k = 0;k<ship[i].length;k++){
//				System.out.println(i+" "+k);
				ship[i][k].addListener(new InputListener() {
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						selectionImage.setPosition((event.getListenerActor().getX()-selectionImage.getWidth()/8.5f)+w/2, (event.getListenerActor().getY()-selectionImage.getHeight()/7.5f)+h/2);
						for (int i = 0;i<ship.length;i++){
							for (int k = 0;k<ship[i].length;k++){
								if (selectionImage.getX() == (ship[i][k].getX()-selectionImage.getWidth()/8.5f)+w/2 && selectionImage.getY() == (ship[i][k].getY()-selectionImage.getHeight()/7.5f)+h/2){
									shipLocation[i][k].x = (ship[i][k].getX()-selectionImage.getWidth()/8.5f)+w/2;
									shipLocation[i][k].y = (ship[i][k].getY()-selectionImage.getHeight()/7.5f)+h/w;
									selectedImage.setDrawable(ship[i][k].getDrawable());
									selectedFire.setDrawable(fire[i][k].getDrawable());
									GameScreen.b.setDrawable(selectedImage.getDrawable());
									GameScreen.bF.setDrawable(selectedFire.getDrawable());
//									System.out.println("Selected Fire = "+selectedFire.getDrawable().toString());
								}
							}
						}
						Assets.saveGame();
						return false;
					}
				});
			}
		}
		back.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new Menu(game));
				return false;
			}
		});
		table.setWidth(w);
		table.setHeight(h);
		table.add(ship[0][0]).padRight(w/16).size(w/16, h/16).padBottom(10);
		table.add(ship[1][0]).padRight(w/16).size(w/16, h/16).padBottom(10);
		table.add(ship[2][0]).padRight(w/16).size(w/16, h/16).padBottom(10);
		table.add(ship[3][0]).padRight(w/16).size(w/16, h/16).padBottom(10);
		table.row().padBottom(h/15.5f);
		table.add(ship[0][1]).padRight(w/16).size(w/16, h/16);
		table.add(ship[1][1]).padRight(w/16).size(w/16, h/16);
		table.add(ship[2][1]).padRight(w/16).size(w/16, h/16); ship[2][1].setColor(1, 1, 1, 0);
		table.add(ship[3][1]).padRight(w/16).size(w/16, h/16);
		table.row().padBottom(h/72f);
		table.add(ship[4][0]).padRight(w/16).size(w/16, h/16);
		table.add(ship[5][0]).padRight(w/16).size(w/16, h/16);
		table.add(ship[6][0]).padRight(w/16).size(w/16, h/16);
		table.add(ship[7][0]).padRight(w/16).size(w/16, h/16);
		table.row().padBottom(h/7.5f);
		table.add(ship[4][1]).padRight(w/16).size(w/16, h/16);
		table.add(ship[5][1]).padRight(w/16).size(w/16, h/16);
		table.add(ship[6][1]).padRight(w/16).size(w/16, h/16);
		table.add(ship[7][1]).padRight(w/16).size(w/16, h/16);
		table.setVisible(true);
		ship[2][1].setVisible(false);
		table.setVisible(true);
		table.setPosition(w/2, h/2);
		back.setWidth(w/3.5f);
		back.setHeight(h/12);
		back.setPosition((w/2)-back.getWidth()/2, (h/11)-back.getHeight()/2);
		for (int i=0;i<Assets.starList.size();i++){
			starGroup.addActor(Assets.starList.get(i).b);
		}
		shipGroup.addActor(back);
		shipGroup.addActor(table);
		shipGroup.addActor(selectionImage);
		shipGroup.addActor(selectedImage);
		stage.addActor(starGroup);
		stage.addActor(shipGroup);
		selectionImage.setSize(w/12, h/12);
		tempX = ship[0][0].getY();
		tempY = ship[0][0].getY();
//		selectionImage.setPosition(tempX, tempY);
		
//		selectionImage.setPosition(table.getCell();
		Gdx.input.setInputProcessor(stage);
	}
	@Override
	public void show() {
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0f, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		Assets.makeStar(10, 3, starGroup);
		Assets.updateStar();
		frame++;
		if (frame == 10){
			selectionImage.setDrawable(Assets.shipSkin.getDrawable("selectImage2"));
		} else if (frame == 20){
			frame = 0;
			selectionImage.setDrawable(Assets.shipSkin.getDrawable("selectImage"));
		}
		
		
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}
}