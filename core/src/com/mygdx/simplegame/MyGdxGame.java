package com.mygdx.simplegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

//import java.awt.Rectangle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaName;


public class MyGdxGame extends ApplicationAdapter{
	public static int VIRTUAL_WIDTH = 500;
	public static int VIRTUAL_HEIGHT = 500;
	SpriteBatch batch;
	Texture backgroud;
	Texture[] doraemon;
	Texture doracake;
	Texture mouse;
	Texture dizzy;
	Texture gameOver;
	ArrayList<Integer> doracakeXs=new ArrayList<>();
	ArrayList<Integer> doracakeYs=new ArrayList<>();
	ArrayList<Rectangle> doracakeRectangle=new ArrayList<>();
	private  Music game;
	private Music mouseaudio;
	private  Music pancake;
	ArrayList<Integer> mouseXs=new ArrayList<>();
	ArrayList<Integer> mouseYs=new ArrayList<>();
	ArrayList<Rectangle> mouseRectangle=new ArrayList<>();
	Rectangle doraemonRectangle;




	Random random;
	int doracakeCount=0;
	int mouseCount=0;

	int doraemonState=0;
	int pause=0;

	float gravity=0.3f;
	float velocity=0;
	int doraemonY=0;

	int score=0;
	BitmapFont font;

	int gamestate=0;

	Boolean flag=true;
	Boolean flag2=true;
	Texture tapToPlay;


	@Override
	public void create () {

		batch = new SpriteBatch();
		backgroud=new Texture("background.jpg");
		doraemon=new Texture[6];
		doraemon[0]=new Texture("doraemon1.png");
		doraemon[1]=new Texture("doraemon2.png");
		doraemon[2]=new Texture("doraemon3.png");
		doraemon[3]=new Texture("doraemon4.png");
		doraemon[4]=new Texture("doraemon5.png");
		doraemon[5]=new Texture("doraemon6.png");
		doracake=new Texture("pancake_newpng.png");
		mouse=new Texture("rat.png");
		dizzy=new Texture("oops.png");
		gameOver=new Texture("gameover.png");
		tapToPlay=new Texture("taptoplay.png");
		game= Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		mouseaudio = Gdx.audio.newMusic(Gdx.files.internal("mouse.mp3"));
		pancake = Gdx.audio.newMusic(Gdx.files.internal("eat.mp3"));

		random=new Random();
		doraemonY=Gdx.graphics.getHeight()/2;


		font=new BitmapFont();

		font.setColor(Color.WHITE);
		font.setOwnsTexture(true);
		font.getData().setScale(8);

	}
	public void makedoracake(){
		float height=random.nextFloat()*(Gdx.graphics.getHeight()-doraemon[doraemonState].getHeight()-160);
		doracakeYs.add((int) height);
		doracakeXs.add(Gdx.graphics.getWidth());
	}
	public void makemouse(){
		float height=random.nextFloat()*(Gdx.graphics.getHeight()-doraemon[doraemonState].getHeight()-170);
		mouseYs.add((int) height);
		mouseXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(backgroud,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gamestate==1){
			if(flag){
				game.setLooping(true);
				game.setVolume(0.25f);
				game.play();
			}
			//game is live

			if (doracakeCount<75){
				doracakeCount++;
			}else {
				doracakeCount=0;
				makedoracake();
			}
			doracakeRectangle.clear();
			for(int i=0;i<doracakeXs.size();i++){
				batch.draw(doracake,doracakeXs.get(i),doracakeYs.get(i));
				doracakeXs.set(i,doracakeXs.get(i)-4);
				doracakeRectangle.add(new Rectangle(doracakeXs.get(i),doracakeYs.get(i),doracake.getWidth()-100,doracake.getHeight()-100));
			}

			if(mouseCount<250){
				mouseCount++;
			}else {
				mouseCount=0;
				makemouse();
			}
			mouseRectangle.clear();
			for(int i=0;i<mouseXs.size();i++){
				batch.draw(mouse,mouseXs.get(i),mouseYs.get(i));
				mouseXs.set(i,mouseXs.get(i)-4);
				mouseRectangle.add(new Rectangle(mouseXs.get(i),mouseYs.get(i),mouse.getWidth()-70,mouse.getHeight()-70));
			}

			if(Gdx.input.justTouched()){

				velocity-=7;
			}

			if(pause<8){
				pause++;
			}else {
				pause=0;
				if(doraemonState<3){
					doraemonState++;
				}else {
					doraemonState=0;
				}
			}
			velocity+=gravity;
			doraemonY-=velocity;
			if(doraemonY<=150){
				doraemonY=150;
			}

		}else if(gamestate==0){
			batch.draw(tapToPlay,Gdx.graphics.getWidth() - tapToPlay.getWidth()-140,tapToPlay.getHeight()+800);
			//waiting for player to start
			if(Gdx.input.justTouched()){
				gamestate=1;
			}
		}

		else if(gamestate==2){

			if(flag) {
				mouseaudio.setLooping(false);
				mouseaudio.play();
				flag = false;
			}


			//game over
			if(Gdx.input.justTouched()){
				gamestate=1;
				doraemonY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				doracakeXs.clear();
				doracakeYs.clear();
				doracakeCount=0;
				doracakeRectangle.clear();
				mouseXs.clear();
				mouseYs.clear();
				mouseCount=0;
				mouseRectangle.clear();
			}
		}

        if (gamestate==2){

			batch.draw(dizzy,Gdx.graphics.getWidth()/2 - dizzy.getWidth(),doraemonY);
			batch.draw(gameOver,Gdx.graphics.getWidth() - gameOver.getWidth()-230,gameOver.getHeight()+150);
			batch.draw(tapToPlay,Gdx.graphics.getWidth() - tapToPlay.getWidth()-140,tapToPlay.getHeight()+800);
		}else {
			batch.draw(doraemon[doraemonState],Gdx.graphics.getWidth()/2 - doraemon[doraemonState].getWidth()-200,doraemonY);
		}
		batch.draw(doraemon[doraemonState],Gdx.graphics.getWidth()/2 - doraemon[doraemonState].getWidth()-200,doraemonY);
		doraemonRectangle=new Rectangle(Gdx.graphics.getWidth()/2 - doraemon[doraemonState].getWidth()-200,doraemonY,doraemon[doraemonState].getWidth()-60,doraemon[doraemonState].getHeight()-80);
		for (int i=0;i<doracakeRectangle.size();i++){
			if(Intersector.overlaps(doraemonRectangle,doracakeRectangle.get(i))){
//			Gdx.app.log("doracake!!","collision");
				flag2=true;
				if(flag2){
					pancake.play();
					flag2=false;
				}

				score++;
			doracakeRectangle.remove(i);
			doracakeXs.remove(i);
			doracakeYs.remove(i);
			break;
			}
		}
		for (int i=0;i<mouseRectangle.size();i++){
			if(Intersector.overlaps(doraemonRectangle,mouseRectangle.get(i))){
				Gdx.app.log("mouse!!","collision");
				gamestate=2;
			}
		}
		font.draw(batch,"Score : " + String.valueOf(score),340,2100);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
