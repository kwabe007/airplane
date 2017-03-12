package com.mygdx.airplane.Background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.airplane.Screen.PlayScreen;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Kwa on 2016-08-20.
 */
public class Backdrop implements Disposable {
    ShapeRenderer renderer;
    OrthographicCamera camera;
    Array<Texture> textureArray;
    LinkedList<ExtendedSprite> bgSpriteLinkedList;
    private Random random;

    private static final float CLOUD_WIDTH = 3.2f;
    private static final float CLOUD_HEIGHT = 1.5f;
    private static final float TIMER_BREAK = 1f;
    private static final int CLOUD_AMOUNT = 10; //Decides how many clouds can be shown at any given moment

    public Backdrop(PlayScreen screen, Batch batch) {
        renderer = new ShapeRenderer();
        camera = screen.getCamera();

        bgSpriteLinkedList = new LinkedList<ExtendedSprite>();

        textureArray = new Array<Texture>();
        textureArray.add(new Texture("cloud.png"));

        random = new Random();

        for (int i = 0; i < CLOUD_AMOUNT; ++i) {
            generateCloud(true);
        }

    }

    private void generateCloud () {
        generateCloud(false);
    }

    private void generateCloud (boolean withX) {
        ExtendedSprite cloudSprite = new Cloud(textureArray.get(0), new Vector2(-2f, 0));

        float lowerYBound = camera.position.y - camera.viewportHeight / 2 - CLOUD_HEIGHT / 2;
        float higherYBound = camera.position.y + camera.viewportHeight / 2 - CLOUD_HEIGHT / 2;
        float yRange = higherYBound - lowerYBound;
        float yScale = random.nextFloat() * yRange;
        float yPos = yScale + lowerYBound;
        float xPos = camera.viewportWidth;

       if (withX) {

           float lowerXBound = camera.position.x - camera.viewportWidth / 2 - CLOUD_WIDTH / 2;
           float higherXBound = camera.position.x + camera.viewportWidth / 2 - CLOUD_WIDTH / 2;
           float xRange = higherXBound - lowerXBound;
           float xScale = random.nextFloat() * xRange;
           xPos = xScale + lowerXBound;

       }

        cloudSprite.setBounds(xPos, yPos, CLOUD_WIDTH, CLOUD_HEIGHT);

        bgSpriteLinkedList.add(cloudSprite);
    }

    public void update (float dt) {
        if (bgSpriteLinkedList.size() < CLOUD_AMOUNT ) {
            generateCloud();
        }

        for (Iterator<ExtendedSprite> iterator = bgSpriteLinkedList.iterator(); iterator.hasNext();) {
            ExtendedSprite cloudSprite = iterator.next();
            cloudSprite.update(dt);

            /*Remove wall if it's past the top of screen shown by camera*/
            if (cloudSprite.getX() < camera.position.x - camera.viewportWidth / 2 - cloudSprite.getWidth()) {
                iterator.remove();
            }

        }

    }

    public void draw (SpriteBatch batch) {
        batch.end(); //End the sprite batch which is presumably active. This is is done due to the activation of the shaperenderer

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.SKY);
        renderer.rect(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2,
                camera.viewportWidth, camera.viewportHeight);
        renderer.end();

        batch.begin();

        for (ExtendedSprite cloudSprite : bgSpriteLinkedList) {
            cloudSprite.draw(batch);
        }

    }

    public int getAmountOfSprites() {
        return bgSpriteLinkedList.size();
    }

    @Override
    public void dispose() {

        for (Texture texture : textureArray) {
            texture.dispose();
        }

    }
}
