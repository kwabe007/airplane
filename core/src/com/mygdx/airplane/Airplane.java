package com.mygdx.airplane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.airplane.Screen.MenuScreen;


//PRIMARY FOR SECOND RELEASE
//TODO: MAKE SURE THERE IS A NO-INPUT DELAY WHEN GAME IS OVER SO YOU CAN DON'T ACCIDENTALLY SKIP YOUR SCORE

//SECONDARY FOR A LESS SHITTY GAMEPLAY EXPERIENCE
//TODO: ADD PLAYING MUSIC
//TODO: ADD SMOOTH TRANSITION BETWEEN PLAYING AND GAME OVER SCREEN
//TODO: ADD SMOOTH TRANSITION BETWEEN GAME OVER SCREEN AND NEW GAME
//TODO: ADD SPLASH SCREEN(S)
//TODO: ADD ANIMATED TEXT IN MENU SCREEN



public class Airplane extends Game {
	public SpriteBatch batch;

	World world;
	Body ground;
	OrthogonalTiledMapRenderer mapRenderer;

	public static final int V_WIDTH = 32;
	public static final int V_HEIGHT = 18;
	public static final float CM = 0.1f;

	public static final short DEFAULT_BIT = 1;
	public static final short PLANE_BIT = 1 << 1;
	public static final short WALL_BIT = 1 << 2;

	public static final boolean DEBUG_MODE = false; //Activates debug-features
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		AssetManager manager = new AssetManager();
		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public static void crash(Exception e) {
		/*Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		ErrorDialog errorDialog = new ErrorDialog("Error", skin, e);*/
		throw new RuntimeException(e);
	}
}
