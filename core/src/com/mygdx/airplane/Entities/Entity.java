package com.mygdx.airplane.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.airplane.Screen.PlayScreen;

/**
 * Created by Kwa on 2016-08-09.
 */
public abstract class Entity {

    protected boolean setToDestroy;
    protected boolean destroyed;
    protected Sprite sprite;
    protected Body body;
    protected Fixture mainFixture;
    protected World world;

    public Entity (PlayScreen screen, float x, float y) {
        world = screen.getWorld();
        destroyed = false;
        setToDestroy = false;
        defineBody(x, y);
    }
    protected abstract void defineBody(float x, float y);

    protected abstract void setupSprite();

    protected abstract void draw(SpriteBatch batch);

    public abstract void update(float dt);

    public void takeDamage() {
        takeDamage(1);
    }

    public abstract void takeDamage(int damage);

    public void setToDestroy() {
        setToDestroy = true;
    }

    protected abstract void destroy();

    public boolean isDestroyed() {
        return destroyed;
    }

    public void draw(Batch batch) {
        if(!destroyed)
            sprite.draw(batch);
    }

    protected Animation addAnimation(Texture texture, int amount, int xOffset, int yOffset, int x, int y) {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < amount; i++)
            frames.add(new TextureRegion(texture, xOffset + i * x, yOffset, x, y));
        Animation animation = new Animation(0.1f, frames);
        return animation;
    }

    public Body getBody() {
        return body;
    }
}
