package com.mygdx.airplane.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.airplane.Airplane;
import com.mygdx.airplane.Entities.Plane;
import com.mygdx.airplane.Objects.WallHandler;
import com.mygdx.airplane.Background.Backdrop;
import com.mygdx.airplane.Scenes.DebugStage;
import com.mygdx.airplane.Scenes.GameOverScene;
import com.mygdx.airplane.Scenes.Hud;
import com.mygdx.airplane.Tools.WorldContactListener;

import java.io.FileNotFoundException;


/**
 * Created by Kwa on 2016-07-26.
 */
public class PlayScreen implements Screen, InputProcessor {

    private Airplane game;
    private AssetManager manager;

    private OrthographicCamera camera;
    private Hud hud;
    private GameOverScene gameOverScene;

    private World world;
    private Box2DDebugRenderer debug;
    private DebugStage debugStage;

    private Plane plane;
    private WallHandler wallHandler;
    private int passedWalls;
    private int currentHighScore;

    private Preferences preferences;

    float yCoordinate;

    private boolean rotateLeftCurrent;
    private int primaryTouchIndex;
    private int secondaryTouchIndex;

    private Sprite sprite;
    private Backdrop backdrop;

    private boolean gameOverShown;

    public static final boolean CAN_DIE = true;
    public static final float PLANE_SPEED = 15f;

    public static final String HIGH_SCORE_FILE = "score.txt";

    private Sound sound;

    public PlayScreen(Airplane game, AssetManager manager) throws FileNotFoundException {
        this.game = game;
        this.manager = manager;
        camera = new OrthographicCamera(Airplane.V_WIDTH, Airplane.V_HEIGHT);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        hud = new Hud(this, game.batch);
        gameOverScene = new GameOverScene(this, game.batch);
        backdrop = new Backdrop(this, game.batch);

        world = new World(new Vector2(0, 0), true);
        debug = new Box2DDebugRenderer();
        debugStage = new DebugStage(this, game.batch);

        world.setContactListener(new WorldContactListener());
        Gdx.input.setInputProcessor(this);

        /* Here comes entity initializations*/
        plane = new Plane(this, camera.position.x, camera.position.y + camera.viewportHeight / 4f);

        /*The WallHandler handles creation, movement and removal of walls*/
        wallHandler = new WallHandler(this);
        passedWalls = 0;

        /*yCoordinate virtually represents how far the plane has gone in y-coordinates. Virtually
        * because the plane does not actually move in the y-axis in the world.*/
        yCoordinate = 0f;

        rotateLeftCurrent = false;
        primaryTouchIndex = 0;
        secondaryTouchIndex = 0;

        Texture texture = new Texture("plane0.png");
        sprite = new Sprite(texture);
        sprite.setBounds(5,5,100,100);

        gameOverShown = false;

        preferences = Gdx.app.getPreferences("My Preferences");
        loadHighScore();

        sound = Gdx.audio.newSound(Gdx.files.internal("audio/Ting-Popup_Pixels-349896185.mp3"));
    }

    public void update(float dt) {

        handlePollInput(dt);

        world.step(1 / 60f, 6, 2);

        updateYcoordinate(dt);

        backdrop.update(dt);

        plane.update(dt);

        wallHandler.update(dt);

        hud.update(dt);

        if (Airplane.DEBUG_MODE) debugStage.update(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        backdrop.draw(game.batch);
        plane.draw(game.batch);
        sprite.draw(game.batch);
        game.batch.end();

        hud.stage.draw();

        wallHandler.render(camera.combined);

        if (Airplane.DEBUG_MODE) {
            debug.render(world, camera.combined);
            debugStage.draw();
        }

        if (isGameOver()) {

            gameOverScene.draw();

            if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                PlayScreen newPlayScreen = setupNewScreen();
                game.setScreen(newPlayScreen);
                dispose();
            }

        }
        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);


        if(isGameOver() && !isGameOverShown()) {
            updateHighscore();
            gameOverScene.setHighscore(currentHighScore);
            signalGameOverShown();
        }
    }

    @Override
    public void show() {

    }

    private PlayScreen setupNewScreen() {
        PlayScreen playScreen = null;
        try {
            playScreen = new PlayScreen(game, null);
        } catch (FileNotFoundException e) {
            Airplane.crash(e);
        }
        return playScreen;
    }

    public void handlePollInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && rotateLeftCurrent)
            plane.rotateLeft();
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            plane.rotateRight();

        else if (Gdx.input.isTouched(primaryTouchIndex) ) {
            if (Gdx.input.getX(primaryTouchIndex) < Gdx.graphics.getWidth() / 2)
                plane.rotateLeft();
            else if (Gdx.input.getX(primaryTouchIndex) > Gdx.graphics.getWidth() / 2) {
                plane.rotateRight();
            }
        }
    }

    public void updateYcoordinate(float dt) {
        yCoordinate += dt * ((float) -Math.cos(getPlane().getBody().getAngle()) * PlayScreen.PLANE_SPEED);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void passWall() {
        ++passedWalls;
        sound.play(0.3f);
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public World getWorld() {
        return this.world;
    }

    public Plane getPlane() {
        return this.plane;
    }

    public int getWallCount() {
        return wallHandler.getWallCount();
    }

    public int getWallsPassed() {
        return passedWalls;
    }

    public Backdrop getBackdrop() {
        return backdrop;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    /*Gets the absolute x-position in current viewport based on a relative position.
     Ex: If viewport is currently showing from x=50.0 to x=100.0, an input relative position of 0.5
     will yield 75.0;
     */
    public float getViewportAbsoluteXPosition(float relativeXPosition) {
        float lowerXBound = getCamera().position.x - getCamera().viewportWidth / 2;
        float higherXBound = getCamera().position.x + getCamera().viewportWidth / 2;
        float xRange = higherXBound - lowerXBound;
        float scale = xRange * relativeXPosition;
        return lowerXBound + scale;
    }

    /*Gets the absolute y-position in current viewport based on a relative position.
     Ex: If viewport is currently showing from y=20.0 to x=80.0, an input relative position of 0.5
     will yield 50.0;
     */
    public float getViewportAbsoluteYPosition(float relativeYPosition) {
        float lowerYBound = getCamera().position.y - getCamera().viewportHeight / 2;
        float higherYBound = getCamera().position.y + getCamera().viewportHeight / 2;
        float yRange = higherYBound - lowerYBound;
        float scale = yRange * relativeYPosition;
        return lowerYBound + scale;
    }

    public boolean isGameOver(){
        if(plane.state == Plane.State.DEAD){
            return true;
        }
        return false;
    }

    private void loadHighScore() {
        /*Get current highscore!*/
        currentHighScore = preferences.getInteger("highscore", -1);
        if(currentHighScore == -1) {
          System.err.println("No highscore found!");
        }
    }

    /**Checks whether the score is higher than highscore and updates highscore accordingly. Returns
     * true if highscore was updated. Otherwise returns false,
     */
    private boolean updateHighscore() {
        int score = getWallsPassed();
        if (score > currentHighScore) {
            currentHighScore = score;
            preferences.putInteger("highscore", currentHighScore);
            return true;
        }
        return false;
    }

    public int getHighscore() {
        return currentHighScore;
    }

    public boolean isGameOverShown(){
        return gameOverShown;
    }

    /*Signal that the game over screen has been shown*/
    private void signalGameOverShown(){
        gameOverShown = true;
    }

    public void showGameOver() {
        ;
    }

    @Override
    public void dispose() {
        world.dispose();
        debug.dispose();
        hud.dispose();
        backdrop.dispose();
        debugStage.dispose();
        sound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.LEFT)
            rotateLeftCurrent = true;
        else if (keycode == Input.Keys.RIGHT) {
            rotateLeftCurrent = false;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.LEFT)
            rotateLeftCurrent = false;
        else if (keycode == Input.Keys.RIGHT) {
            rotateLeftCurrent = true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        secondaryTouchIndex = primaryTouchIndex;
        primaryTouchIndex = pointer;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == primaryTouchIndex) {
            primaryTouchIndex = secondaryTouchIndex;
        } else if (pointer == secondaryTouchIndex) {
            secondaryTouchIndex = 0;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
