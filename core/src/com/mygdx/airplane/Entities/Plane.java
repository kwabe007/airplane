package com.mygdx.airplane.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.airplane.Airplane;
import com.mygdx.airplane.Screen.PlayScreen;

/**
 * Created by Kwa on 2016-08-09.
 */
public class Plane extends Entity {

    public enum State {ALIVE, DEAD};

    public State state;

    private Animation planeAnim;

    static final private float ROTATION_SPEED = 0.06f;
    /* Minimum and maximum rotation angles for the plane*/
    static final private float MIN_ROTATION = (float) (0.65f * Math.PI);
    static final private float MAX_ROTATION = (float) (1.35f * Math.PI);
    static final private float ROTATION_SPAN = MAX_ROTATION-MIN_ROTATION;
    static final private int ROTATION_STATES = 4; //Denotes amount of distinguishable state of rotations for each direction (left and right). Used for discretization of the angle.
    static final private float SPEEDFACTOR = 18f;

    public Plane(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setupSprite();
        state = State.ALIVE;
    }

    @Override
    protected void defineBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(x,y));
        body = world.createBody(bodyDef);

        /* Define the shape of the main fixture as a circle*/
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.25f);

        /*Set fixture definition for main fixture which will represent the part of plane which will
        interact with other objects*/
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;


        if (PlayScreen.CAN_DIE) {
        /*Set collision category to plane*/
            fixtureDef.filter.categoryBits = Airplane.PLANE_BIT;
            fixtureDef.filter.maskBits = Airplane.WALL_BIT;
        } else {
            /*Set collision category to plane*/
            fixtureDef.filter.categoryBits = Airplane.PLANE_BIT;
            fixtureDef.filter.maskBits = 0;
        }

        mainFixture = body.createFixture(fixtureDef);
        mainFixture.setUserData(this);

        /*Dispose shape to avoid memory leak, this is to be done after creating the fixture*/
        circleShape.dispose();

        /* Define the shape of the visual fixture as a triangle. */
        PolygonShape polygonShape = new PolygonShape();
        Vector2[] vertices = new Vector2[3];
        vertices[0] = new Vector2(0, 1.8f);
        vertices[1] = new Vector2(-0.9f, -0.9f);
        vertices[2] = new Vector2(0.9f, -0.9f);
        polygonShape.set(vertices);

        /*Set definition for visual fixture which will not interact with
        the environment. (meant for debug renderer)*/
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = Airplane.PLANE_BIT;
        fixtureDef.filter.maskBits = 0; /* Do not collide with anything*/

        body.createFixture(fixtureDef);

        /*Dispose shape to avoid memory leak, this is to be done after creating the fixture*/
        polygonShape.dispose();

        body.setTransform(body.getPosition(), (float)Math.toRadians(180));
    }

    /*Gets the discretized value of plane's rotation based on ROTATION_STATES.
    * A negative value means the plane is facing left.
    * A positive value means the plane is facing right.
    * Zero means the plane is facing straight down.*/
    public int getRotationIndex() {
        float angle = body.getAngle();
        int totalIndices = (ROTATION_STATES * 2) + 1; //Total number of discrete rotation points
        for (int i = 1; i < totalIndices; ++i) {
            float section = MIN_ROTATION + i * (ROTATION_SPAN / totalIndices);
            if (angle < section) {
                return i - (ROTATION_STATES + 1);
            }
        }
        return ROTATION_STATES;
    }

    /*Gets the appropriate texture based on the rotation index*/
    public TextureRegion getAnimationFrame() {
        int index = getRotationIndex();
        boolean reverse = false;
        if (index < 0) {
            reverse = true;
            index = index * -1;
        }
        TextureRegion region = (TextureRegion)planeAnim.getKeyFrames()[index];
        if (reverse && !region.isFlipX())
            region.flip(true,false);
        else if (!reverse && region.isFlipX())
            region.flip(true,false);
        return region;
    }

    @Override
    public void draw (SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    protected void setupSprite() {
        Texture texture = new Texture("plane.png");
        sprite = new Sprite(texture);
        sprite.setBounds(0, 0, 3.5f, 2.625f);
        planeAnim = addAnimation(texture, 5, 0, 0, 640, 480);
    }

    @Override
    public void update(float dt) {
        if (setToDestroy && !isDestroyed())
            destroy();
        else if (!isDestroyed()) {
            sprite.setRegion(getAnimationFrame());
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
            body.setLinearVelocity(new Vector2((float) -Math.sin(body.getAngle()) * SPEEDFACTOR, 0));
        } else {

        }
    }

    @Override
    public void takeDamage(int damage) {
        body.setTransform(body.getPosition(), (float) Math.toRadians(90));
    }

    @Override
    protected void destroy() {

        /*Configure collision filter so plane does not collide with anything when destroyed*/
        Filter filter = new Filter();
        filter.categoryBits = Airplane.PLANE_BIT;
        filter.maskBits = 0;
        mainFixture.setFilterData(filter);

        body.setLinearVelocity(new Vector2(0,0));

        world.destroyBody(body);

        destroyed = true;
        state = State.DEAD;
    }

    public void rotateToPoint(float x, float y) {
        float bodyAngle = body.getAngle();
        Vector2 clickedPoint = new Vector2(x, y);
        Vector2 toTarget = new Vector2(clickedPoint.x - body.getPosition().x, clickedPoint.y - body.getPosition().y);
        float desiredAngle = (float) Math.atan2( -toTarget.x, toTarget.y );
        body.setTransform(body.getPosition(), desiredAngle);
    }

    public void rotateLeft() {
        if (destroyed) return;
        float bodyAngle = body.getAngle();
        float newAngle = body.getAngle() - ROTATION_SPEED;
        newAngle = newAngle < MIN_ROTATION ? MIN_ROTATION : newAngle;
        body.setTransform(body.getPosition(), newAngle);
    }

    public void rotateRight() {
        if (destroyed) return;
        float bodyAngle = body.getAngle();
        float newAngle = body.getAngle() + ROTATION_SPEED;
        newAngle = newAngle > MAX_ROTATION ? MAX_ROTATION : newAngle;
        body.setTransform(body.getPosition(), newAngle);
    }
}
