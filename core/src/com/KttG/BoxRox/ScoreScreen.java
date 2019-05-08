package com.KttG.BoxRox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ScoreScreen extends Game implements Screen{
	Game game;
	Stage stage = new Stage(){
		public boolean keyDown(int keycode){
			if ((keycode == Keys.BACK) || (keycode == Keys.ESCAPE)) {
                game.setScreen(new Menu(game));
            }
			return super.keyDown(keycode);
		}
	};
	Group starGroup = new Group();
	TextButton back = new TextButton("Back", Assets.styleButtons);
	Label topScoreLabel = new Label("High Scores", Assets.styleLabel);
	static Label[] rank = new Label[10];
	
	static int tempInt;
	
	Table table = new Table();
	float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
	
	ScoreScreen(final Game game){
//		topScore[1] = 50;
		Gdx.input.setInputProcessor(stage);
//		Assets.topScore[7] = 50;
		this.game = game;
//		table.debug();
		table.setSize(w, h);
		table.setPosition((w/2)-table.getWidth()/2, (h/2)-table.getHeight()/2);
		table.add(topScoreLabel).padBottom(40);
		table.row();
		for (int i = 0; i<rank.length;i++){
			sort();
			Assets.topScoreString[i] = String.valueOf(Assets.topScore[i]);
			rank[i] = new Label(Assets.topScoreString[i], Assets.styleLabel);
			table.add(rank[i]);
			table.row();
		}
		back.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new Menu(game));
				return false;
			}
		});
		back.setWidth(w/3.5f);
		back.setHeight(h/12);
		back.setPosition((w/2)-back.getWidth()/2, (h/11)-back.getHeight()/2);
		for (int i=0;i<Assets.starList.size();i++){
			starGroup.addActor(Assets.starList.get(i).b);
		}
		stage.addActor(starGroup);
		stage.addActor(back);
		stage.addActor(table);
	}
	public static void sort(){
		for (int i = 0; i<rank.length;i++){
			for (int k=0;k<rank.length;k++){
				if (Assets.topScore[i] > Assets.topScore[k]){
					tempInt = Assets.topScore[i];
					Assets.topScore[i] = Assets.topScore[k];
					Assets.topScore[k] = tempInt;
				}
			}
		}
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		Assets.makeStar(10, 3, starGroup);
		Assets.updateStar();
	}

	@Override
	public void hide() {
		
	}
	
}