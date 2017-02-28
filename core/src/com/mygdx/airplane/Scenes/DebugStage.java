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
import com.mygdx.airplane.Screen.PlayScreen;

/**
 * Created by Kwa on 2016-09-13.
 */
public class DebugStage extends Stage {
    private Integer finalScore;
    private Integer debugInt;

    private Label bodyCountLabel;
    private Label wallCountLabel; //Label for debug stuff
    private PlayScreen screen;

    public DebugStage(PlayScreen screen, SpriteBatch spriteBatch) {
        super(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                new OrthographicCamera()), spriteBatch);

        this.screen = screen;

        /*Generate a dynamic font based on font resource*/
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cour.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (Gdx.graphics.getHeight() / 20f); /*The font should take up about 1/20th of the screen (horizontally)*/
        parameter.color = Color.WHITE;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); //Don't forget to dispose to avoid memory leaks!
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        bodyCountLabel = new Label(String.format("body_count: %1d", 0), style);
        wallCountLabel = new Label(String.format("wall_count: %1d", 0), style);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        table.right().bottom();

        table.add(bodyCountLabel);
        table.row();
        table.add(wallCountLabel);

        addActor(table);
    }

    public void update(float dt) {
        int bodyCount = screen.getWorld().getBodyCount();
        int wallCount = screen.getWallCount();

        bodyCountLabel.setText(String.format("body_count: %1d", bodyCount));
        wallCountLabel.setText(String.format("wall_count: %1d", wallCount));

    }
}
