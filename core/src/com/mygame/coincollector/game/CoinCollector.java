package com.mygame.coincollector.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinCollector extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manstate , pause;
	float gravity = 0.2f;
	float velocity = 0;
	int manY= 0,manX=0;
	ArrayList<Integer> coinX = new ArrayList<>();
	ArrayList<Integer> coinY = new ArrayList<>();
	Texture coin;
	Random random;
	int coincount=0;
	int score=0;
	BitmapFont font;
	int gamestate=0;
	int gamestate1=0;
	Texture dizzy;

	ArrayList<Integer> bombX = new ArrayList<>();
	ArrayList<Integer> bombY = new ArrayList<>();
	Texture bomb;
	//Random random;
	int bombcount=0;

	ArrayList<Rectangle> coinRect = new ArrayList<>();
	ArrayList<Rectangle> bombRect = new ArrayList<>();

	Rectangle manRect;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;
		random = new Random();
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		font =new BitmapFont();
		font.setColor(Color.BLUE);
		font.getData().setScale(10);
		dizzy=new Texture("dizzy-1.png");
		//manX=Gdx.graphics.getWidth()/2;
	}

	public void makeCoin()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());
	}

	public void makebomb()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombY.add((int)height);
		bombX.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gamestate==1)
		{
			if (coincount<150)
			{
				coincount++;
			}
			else
			{
				coincount=0;
				makeCoin();
			}

			coinRect.clear();

			for (int i=0;i<coinX.size();i++)
			{
				batch.draw(coin,coinX.get(i),coinY.get(i));
				coinX.set(i,coinX.get(i)-4);
				coinRect.add(new Rectangle(coinX.get(i),coinY.get(i),coin.getWidth(),coin.getHeight()));
			}


			if (bombcount<300)
			{
				bombcount++;
			}
			else
			{
				bombcount=0;
				makebomb();
			}

			bombRect.clear();

			for (int i=0;i<bombX.size();i++)
			{
				batch.draw(bomb,bombX.get(i),bombY.get(i));
				bombX.set(i,bombX.get(i)-6);
				bombRect.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getHeight(),bomb.getWidth()));
			}


			if (Gdx.input.justTouched())
			{
				velocity=-10;
			}

			if (pause<8)
			{
				pause++;
			}
			else
			{
				pause=0;
				if (manstate<3)
				{
					manstate++;
				}
				else {
					manstate=0;
				}
			}

			if (manY<0)
			{
				manY=0;
			}

			int g=Gdx.graphics.getHeight()-man[manstate].getHeight()/2;
			if(manY>=g)
			{
				manY=Gdx.graphics.getHeight()-man[manstate].getHeight()/2;
			}
			velocity+=gravity;
			manY-=velocity;

		}

		if (gamestate==0)
		{
			if (Gdx.input.justTouched());
			gamestate=1;
		}

		if (gamestate==2)
		{
			if(Gdx.input.justTouched())
			{
				gamestate=1;
				manY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				coinX.clear();
				coinY.clear();
				coinRect.clear();
				coincount=0;

				bombX.clear();
				bombY.clear();
				bombRect.clear();
				bombcount=0;
			}
		}

		if(gamestate==2)
		{
			batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);
		}




		batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);

		manRect = new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,Gdx.graphics.getWidth(),man[manstate].getHeight());

		for(int i=0;i<coinRect.size();i++)
		{
			if(Intersector.overlaps(manRect,coinRect.get(i)))
			{
				Gdx.app.log("coin","collision");
				score++;
				coinRect.remove(i);
				coinX.remove(i);
				coinY.remove(i);
				break;
			}
			else
			{

			}
		}

		for(int i=0;i<bombRect.size();i++)
		{
			if(Intersector.overlaps(manRect,bombRect.get(i)))
			{
				gamestate=2;
			}
			else
			{

			}
		}

		font.draw(batch,String.valueOf(score),100,200);



		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
