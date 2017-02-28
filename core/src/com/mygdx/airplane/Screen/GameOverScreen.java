package com.mygdx.airplane.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.airplane.Airplane;

import java.io.FileNotFoundException;

/**
 * Created by Kwa on 2016-08-11.
 */
public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;

    public GameOverScreen(Game game, Integer score){
        this.game = game;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, ((Airplane) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", font);
        Label scoreLabel = new Label("Score: " + String.format("%03d", score), font);
        Label playAgainLabel = new Label("Click to Play Again", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(scoreLabel).expandX().padTop(10f);
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
                game.setScreen(new PlayScreen((Airplane) game, null));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }
}
