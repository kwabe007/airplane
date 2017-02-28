package com.mygdx.airplane.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.airplane.Airplane;
import com.mygdx.airplane.Screen.PlayScreen;

/**
 * Created by Kwa on 2016-08-09.
 */
public class Wall {
    protected World world;
    protected Body body;
    protected Vector2 velocity;

    private Fixture fixture;
    private float width;
    private float height;
    private float borderSize;
    private boolean destroyed;

    public Wall (PlayScreen screen, float x, float y, float w, float h, float borderSize) {
        world = screen.getWorld();
        defineBody(x, y, w / 2, h / 2);
        velocity = new Vector2(0,0);

        width = w;
        height = h;
        if (borderSize >= 0 && !(borderSize * 2 >= width || borderSize * 2 >= height)) {
            this.borderSize = borderSize;
        } else {
            this.borderSize = 0;
        }
        destroyed = false;
    }

    private void defineBody(float x, float y, float w, float h) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(x,y));
        body = world.createBody(bodyDef);

        /* Define the shape of the fixture as a triangle*/
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w, h);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        /*Set collision category to wall*/
        fixtureDef.filter.categoryBits = Airplane.WALL_BIT;
        fixtureDef.filter.maskBits = Airplane.PLANE_BIT;

        fixture = body.createFixture(fixtureDef);

    }

    public void render(ShapeRenderer renderer) {
        renderer.setColor(Color.BLACK);
        renderer.rect(body.getPosition().x - width / 2,body.getPosition().y - height / 2, width, height);
        renderer.setColor(Color.BLACK);
        renderer.rect(body.getPosition().x - width / 2 + borderSize, body.getPosition().y - height / 2 + borderSize,
                width - borderSize * 2, height - borderSize * 2);
    }

    public void update(float dt) {
        body.setLinearVelocity(velocity);
    }

    public void setVelocity(Vector2 vector2) {
        velocity = vector2;
    }

    public Body getBody() {
        return body;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void destroy() {
        if (!destroyed) world.destroyBody(body);
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
