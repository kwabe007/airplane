package com.mygdx.airplane.Entities.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.airplane.Background.ExtendedSprite;

import java.util.ArrayList;

/**
 * Created by kwabena on 2017-03-12.
 */



public class SpriteGroup implements Disposable {

    private static final float DEBRIS_WIDTH = 3.10f;
    private static final float DEBRIS_HEIGHT = 3.10f;

    private Vector2 velocityVector;
    private ArrayList<ExtendedSprite> array;
    Texture texture;

    public SpriteGroup() {
        texture = new Texture("plane_debris.png");

        array = new ArrayList<ExtendedSprite>();
        array.add(new ExtendedSprite(texture, new Vector2(-10f, -10f)));
        array.add(new ExtendedSprite(texture, new Vector2(-10f, 10f)));
        array.add(new ExtendedSprite(texture, new Vector2(10f, -10f)));
        array.add(new ExtendedSprite(texture, new Vector2(10f, 10f)));

    }

    public void update(float dt) {
        for (ExtendedSprite espr : array) {
            espr.update(dt);
        }
    }

    public void setBounds(float xPos, float yPos) {
        for (ExtendedSprite espr : array) {
        espr.setBounds(xPos, yPos, DEBRIS_WIDTH, DEBRIS_HEIGHT);
        }
    }

    public void draw(SpriteBatch batch) {
        for (ExtendedSprite espr : array) {
            espr.draw(batch);
        }

    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
