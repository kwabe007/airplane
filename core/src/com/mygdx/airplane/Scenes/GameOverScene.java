package com.mygdx.airplane.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.airplane.Airplane;
import com.mygdx.airplane.Screen.PlayScreen;

/**
 * Created by Junior on 2016-08-26.
 */
public class GameOverScene extends Stage {
    private Viewport viewport;

    private Integer finalScore;
    private Integer highScore;
    private Integer debugInt;

    private Label scoreLabel;
    private Label highScoreLabel;
    private Label debugLabel; //Label for debug stuff
    private PlayScreen screen;

    public GameOverScene(PlayScreen screen, SpriteBatch batch) {
        this.screen = screen;
        finalScore = screen.getWallsPassed();
        highScore = screen.getHighscore();

        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());

        Table table = new Table();
        table.setFillParent(true);

        /*Generate a dynamic font based on font resource*/
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SIMPLIFICA_Typeface.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (Gdx.graphics.getHeight() / 10f); /*The font should take up about 1/10th of the screen (horizontally)*/
        parameter.color = Color.WHITE;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); //Don't forget to dispose to avoid memory leaks!

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        //scoreLabel = new Label(String.format("%01d", finalScore), style);
        highScoreLabel = new Label(String.format("%01d", highScore), style);
        debugLabel = new Label(String.format("%01d", debugInt), style);

        //table.add(scoreLabel).expandX();
        //table.row();
        table.add(highScoreLabel).expandX();
        table.row();
        if (Airplane.DEBUG_MODE) table.add(debugLabel).expandX();

        addActor(table);
    }

    public void setHighscore(int score) {
        highScoreLabel.setText("Score: " + String.format("%01d", score));
    }

    public void update(float dt) {

    }

    public void setDebug() {

    }
}
