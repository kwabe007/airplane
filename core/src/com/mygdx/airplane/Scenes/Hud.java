package com.mygdx.airplane.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.airplane.Airplane;
import com.mygdx.airplane.Screen.PlayScreen;

/**
 * Created by Kwa on 2016-07-26.
 */
public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer score;
    private Float debugFloat;

    private Label scoreLabel;
    private Label debugLabel; //Label for debug stuff
    private PlayScreen screen;

    public Hud(PlayScreen screen, SpriteBatch sb) {
        this.screen = screen;
        score = 0;

        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        /*Generate a dynamic font based on font resource*/
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SIMPLIFICA_Typeface.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = (int) (Gdx.graphics.getHeight() / 10f); /*The font should take up about 1/10th of the screen (horizontally)*/
        parameter.color = Color.WHITE;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        LabelStyle style = new LabelStyle();
        style.font = font;

        scoreLabel = new Label(String.format("%01d", score), style);
        debugLabel = new Label(String.format("%.2f", debugFloat), style);

        table.add(scoreLabel).expandX().padTop(2);
        table.row();
        if (Airplane.DEBUG_MODE) table.add(debugLabel).expandX().padTop(2);

        stage.addActor(table);
    }

    public void update(float dt) {
        score = screen.getWallsPassed();
        scoreLabel.setText(String.format("%01d", score));

        /*debugInt = screen.getPlane().getRotationIndex();*/
        /*debugInt = screen.getBackdrop().getAmountOfSprites();*/

        debugLabel.setText(String.format("%.3f", screen.getYCoordinate()));


        /*Vector2 velocityVec = screen.getPlane().getBody().getLinearVelocity();

        if (velocityStateTimer >= 0.08f) {
            velocity = Math.sqrt(Math.pow(velocityVec.x, 2) + Math.pow(velocityVec.y, 2));
            velocity = Math.abs(velocityVec.x);
            velocityLabel.setText(String.format("%.2f", velocity));
            velocityStateTimer = 0f;
        }*/
    }

    public void setDebug() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
