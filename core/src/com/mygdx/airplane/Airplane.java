package com.mygdx.airplane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.airplane.Dialogs.ErrorDialog;
import com.mygdx.airplane.Screen.MenuScreen;
import com.mygdx.airplane.Screen.PlayScreen;
import com.mygdx.airplane.Tools.Function;


//PRIMARY FOR INITIAL RELEASE
//TODO: CREATE MORE DIFFICULT WALLPATTERNS

//SECONDARY FOR A LESS SHITTY GAMEPLAY EXPERIENCE
//TODO: ADD MENU MUSIC
//TODO: ADD PLAYING MUSIC
//TODO: ADD SOUND WHEN PASSING WALL
//TODO: ADD ANIMATION WHEN PLANE GOES R.I.P
//TODO: ADD SMOOTH TRANSITION BETWEEN PLAYING AND GAME OVER SCREEN
//TODO: ADD SMOOTH TRANSITION BETWEEN GAME OVER SCREEN AND NEW GAME
//TODO: ADD SPLASH SCREEN(S)
//TODO: ADD ANIMATED TEXT IN MENU SCREEN

//OTHER
//TODO: MAYBE START GITHUBBING THIS PROJECT (at least locally)???



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
