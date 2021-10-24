package com.mygdx.simplegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

//import java.awt.Rectangle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;


public class MyGdxGame extends ApplicationAdapter {
	public static int VIRTUAL_WIDTH = 500;
	public static int VIRTUAL_HEIGHT = 500;
	SpriteBatch batch;
	Texture backgroud;
	Texture[] man;
	Texture coin;
	Texture bomb;
	Texture dizzy;
	Texture gameOver;
	ArrayList<Integer> coinXs=new ArrayList<>();
	ArrayList<Integer> coinYs=new ArrayList<>();
	ArrayList<Rectangle> coinRectangle=new ArrayList<>();

	ArrayList<Integer> bombXs=new ArrayList<>();
	ArrayList<Integer> bombYs=new ArrayList<>();
	ArrayList<Rectangle> bombRectangle=new ArrayList<>();

	Rectangle manRectangle;

	Random random;
	int coinCount=0;
	int bombCount=0;

	int manState=0;
	int pause=0;

	float gravity=0.2f;
	float velocity=0;
	int manY=0;

	int score=0;
	BitmapFont font;

	int gamestate=0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		backgroud=new Texture("bacground.png");
		man=new Texture[4];
		man[0]=new Texture("man1.png");
		man[1]=new Texture("man2.png");
		man[2]=new Texture("man3.png");
		man[3]=new Texture("man4.png");
		coin=new Texture("coin.png");
		bomb=new Texture("bombtest.png");
		dizzy=new Texture("blast.png");
		gameOver=new Texture("gameover.png");

		random=new Random();
		manY=Gdx.graphics.getHeight()/2;

		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}
	public void makeCoin(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add((int) height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(backgroud,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gamestate==1){
			//game is live
			if (coinCount<75){
				coinCount++;
			}else {
				coinCount=0;
				makeCoin();
			}
			coinRectangle.clear();
			for(int i=0;i<coinXs.size();i++){
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-4);
				coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
			}

			if(bombCount<200){
				bombCount++;
			}else {
				bombCount=0;
				makeBomb();
			}
			bombRectangle.clear();
			for(int i=0;i<bombXs.size();i++){
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-4);
				bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
			}

			if(Gdx.input.justTouched()){
				velocity-=10;
			}

			if(pause<8){
				pause++;
			}else {
				pause=0;
				if(manState<3){
					manState++;
				}else {
					manState=0;
				}
			}
			velocity+=gravity;
			manY-=velocity;
			if(manY<=150){
				manY=150;
			}

		}else if(gamestate==0){
			//waiting for player to start
			if(Gdx.input.justTouched()){
				gamestate=1;
			}
		}else if(gamestate==2){
			//game over
			if(Gdx.input.justTouched()){
				gamestate=1;
				manY=Gdx.graphics.getHeight()/2-150;
				score=0;
				velocity=0;
				coinXs.clear();
				coinYs.clear();
				coinCount=0;
				coinRectangle.clear();
				bombXs.clear();
				bombYs.clear();
				bombCount=0;
				bombRectangle.clear();
			}
		}

        if (gamestate==2){
			batch.draw(dizzy,Gdx.graphics.getWidth()/2 - dizzy.getWidth()-150,manY+200);
			batch.draw(gameOver,Gdx.graphics.getWidth() - gameOver.getWidth()-230,gameOver.getHeight()+200);
		}else {
			batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()-150,manY);

		}


		batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()-150,manY);
		manRectangle=new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()-150,manY,man[manState].getWidth(),man[manState].getHeight());
		for (int i=0;i<coinRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
//			Gdx.app.log("coin!!","collision");
				score++;
			coinRectangle.remove(i);
			coinXs.remove(i);
			coinYs.remove(i);
			break;
			}
		}
		for (int i=0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				Gdx.app.log("bomb!!","collision");
				gamestate=2;
			}
		}
		font.draw(batch,String.valueOf(score),500,200);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
