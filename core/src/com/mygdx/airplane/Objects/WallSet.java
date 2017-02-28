package com.mygdx.airplane.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.airplane.Screen.PlayScreen;
import com.mygdx.airplane.Tools.Pair;
import com.mygdx.airplane.Tools.Triple;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;


/**
 * Created by Kwa on 2016-09-07.
 */
public class WallSet {
    LinkedList<Wall> walls;
    PlayScreen screen;
    private WallHandler wallHandler;
    private boolean passed;

    static final float DEFAULT_HEIGHT = 1f;
    static final float DEFAULT_WALL_BORDER = 0.2f;

    public WallSet(PlayScreen screen, WallHandler wallHandler, WallPattern wallPattern) {
        this.screen = screen;
        this.wallHandler = wallHandler;
        walls = new LinkedList<Wall>();
        passed = false;

        Random random = new Random();
        boolean mirror = random.nextBoolean();

        for (Triple<Float, Float, Float> triple : wallPattern.tripleArray) {
            createWall(triple, mirror);
        }

    }

    private void createWall(Triple<Float, Float, Float> lengthPositionAndHeight, boolean mirror) {

        float relativeXPosition = (lengthPositionAndHeight.getSecond() + lengthPositionAndHeight.getFirst()) / 2;
        float absoluteXPosition = wallHandler.getAbsoluteXPositionBetweenWalls(relativeXPosition);
        float relativeWidth = lengthPositionAndHeight.getSecond() - lengthPositionAndHeight.getFirst();
        float absoluteWidth = relativeWidth * wallHandler.getDistanceBetweenWalls();

        float relativeHeight = lengthPositionAndHeight.getThird();
        float absoluteHeight = screen.getViewportAbsoluteYPosition(relativeHeight);

        float absoluteYPosition = screen.getCamera().position.y - screen.getCamera().viewportHeight / 2 - absoluteHeight;

        if (mirror) {
            float middlePoint = wallHandler.getMiddlePointXBetweenWalls();
            float offsetFromMiddle = middlePoint - absoluteXPosition;
            absoluteXPosition = middlePoint + offsetFromMiddle;
        }

        Wall wall = new Wall(screen, absoluteXPosition, absoluteYPosition, absoluteWidth, absoluteHeight, DEFAULT_WALL_BORDER);
        wall.getFixture().setUserData(wallHandler);
        walls.add(wall);

    }

    public void update(float dt) {
        for (Iterator<Wall> iterator = walls.iterator(); iterator.hasNext();) {
            Wall wall = iterator.next();

            /*Set wall y-velocity to depend on plane's rotation and a speedfactor*/
            Body planeBody = screen.getPlane().getBody();
            float yVelocity = (float) -Math.cos(planeBody.getAngle()) * PlayScreen.PLANE_SPEED;
            wall.setVelocity(new Vector2(0, yVelocity));

            wall.update(dt);

            /*Increase passwall-counter and set wall as passed when it passes the plane*/
            if ((wall.getBody().getPosition().y > screen.getPlane().getBody().getPosition().y)) {
                pass();
            }

            /*Remove wall if it's past the top of screen shown by camera*/
            if (wall.getBody().getPosition().y > screen.getCamera().position.y + wall.getHeight() / 2 + screen.getCamera().viewportHeight / 2) {
                wall.destroy();
                iterator.remove();

            }

        }

    }

    public void render (ShapeRenderer renderer) {
        for (Wall wall : walls) {
            wall.render(renderer);
        }
    }

    public void pass() {
        if (passed) return;
        passed = true;
        screen.passWall();
    }

    public boolean isEmpty() {
        return walls.isEmpty();
    }

    /*Stops the movement of walls in this wallset*/
    public void deactivate() {
        for (Wall wall : walls) {
            wall.setVelocity(new Vector2(0,0));

            /*Since walls will stop being updated we should also stop body's velocity directly*/
            wall.getBody().setLinearVelocity(new Vector2(0, 0));
        }
    }

    public int size() {
        return walls.size();
    }

}
