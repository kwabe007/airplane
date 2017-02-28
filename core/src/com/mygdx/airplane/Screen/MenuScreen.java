package com.mygdx.airplane.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.airplane.Airplane;

import java.io.FileNotFoundException;

/**
 * Created by Kwa on 2016-08-22.
 */
public class MenuScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Game game;
    private Music music;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite bgSprite;

    public MenuScreen(Game game) {
        this.game = game;
        batch = ((Airplane) game).batch;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, batch);

        texture = new Texture("MenuBackground.png");
        bgSprite = new Sprite(texture);
        bgSprite.setBounds(stage.getCamera().position.x - stage.getCamera().viewportWidth / 2,
                stage.getCamera().position.y - stage.getCamera().viewportHeight / 2, stage.getCamera().viewportWidth,
                stage.getCamera().viewportHeight);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("MENU", font);
        Label playAgainLabel = new Label("Click to Play", font);

        music = Gdx.audio.newMusic(Gdx.files.internal("audio/menu_song.mp3"));
        music.setVolume(0.25f);
        music.setLooping(true);
        music.play();


        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(20f);

        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            try {
                music.stop();
                game.setScreen(new PlayScreen((Airplane) game, null));
            } catch (FileNotFoundException e) {
                Airplane.crash(e);
            }
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        bgSprite.draw(batch);
        batch.end();

        stage.draw();
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

    @Override
    public void dispose() {
        stage.dispose();
        texture.dispose();
        music.dispose();
    }
}
