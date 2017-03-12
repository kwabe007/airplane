package com.mygdx.airplane.Background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Kwa on 2016-08-22.
 */
public class ExtendedSprite extends Sprite {

    protected Vector2 velocityVector;

    public ExtendedSprite(Texture texture, Vector2 velocityVector) {
        super(texture);
        this.velocityVector = velocityVector;
    }

    public void setSpeed(Vector2 velocityVector) {
        this.velocityVector = velocityVector;
    }

    public void update(float dt) {
        setPosition(getX() + velocityVector.x * dt, getY() + velocityVector.y * dt);
    }

}
