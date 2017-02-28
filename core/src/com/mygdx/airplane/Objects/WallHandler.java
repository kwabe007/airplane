package com.mygdx.airplane.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.mygdx.airplane.Screen.PlayScreen;
import com.mygdx.airplane.Tools.Function;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Kwa on 2016-08-10.
 */
public class WallHandler {

    private PlayScreen screen;
    private float stateTimer;
    LinkedList<WallSet> wallSetLinkedList;
    private static WallPattern[] patterns;

    private WallSet triggerWallSet;
    private boolean firstWallCreated;
    private boolean active;
    private Wall leftWall;
    private Wall rightWall;
    private ShapeRenderer renderer;
    private float triggerDistance;
    private Function function;
    private Random random;


    private static final String PATH_TO_PATTERNS = "wallpatterns/";
    private static final String[] WALLPATTERNS = {
            "pattern00.txt",
            "pattern01.txt",
            "pattern02.txt",
            "pattern03.txt",
            "pattern04.txt",
    };

    private static final float FIRST_WALL_TIMER = 3f;
    public static final float SPEEDFACTOR = 15f;
    private static final float STATIC_WALL_WIDTH = 2f;
    private static final float STATIC_WALL_BORDER = 0.3f;

    private static final float WALLSET_CHANGE_DISTANCE = 300f; /*This denotes for how long the
    wall sets will be able to change. After the plane passes this distance, the spawned wallset will
    remain the same.*/
    private static final float DYNAMIC_WALL_WIDTH = 14f;
    private static final float DYNAMIC_WALL_HEIGHT = 1f;
    private static final float DYNAMIC_WALL_BORDER = 0.2f;

    public WallHandler(PlayScreen screen) throws FileNotFoundException {
        this.screen = screen;
        renderer = new ShapeRenderer();
        wallSetLinkedList = new LinkedList<WallSet>();
        triggerWallSet = null;
        firstWallCreated = false;
        active = true;
        triggerDistance = 0f;
        //function = new Function(WALLSET_CHANGE_DISTANCE, WALLPATTERNS.length, 0,0);
        random = new Random();

        /*Create the static walls*/
        leftWall = new Wall(screen, screen.getCamera().position.x - 12, screen.getCamera().position.y,
                STATIC_WALL_WIDTH, screen.getCamera().viewportHeight, STATIC_WALL_BORDER);
        leftWall.getFixture().setUserData(this);
        rightWall = new Wall(screen, screen.getCamera().position.x + 12, screen.getCamera().position.y,
                STATIC_WALL_WIDTH, screen.getCamera().viewportHeight, STATIC_WALL_BORDER);
        rightWall.getFixture().setUserData(this);

        /*Set wallpatterns from files*/
        patterns = new WallPattern[WALLPATTERNS.length];
        for (int i = 0; i < patterns.length; ++i) {
            FileHandle file = Gdx.files.internal(PATH_TO_PATTERNS + WALLPATTERNS[i]);
            String fileContents = file.readString();
            Scanner scanner = new Scanner(fileContents);
            scanner.useLocale(Locale.US); //Default locale is racist against float dot notation #DOTLIVESMATTER
            patterns[i] = new WallPattern(scanner);
            scanner.close();
        }

    }

    private WallSet createWallSet() {
        WallSet wallSet = null;
        float yCoordinate = screen.getYCoordinate();
        WallPattern wallPattern = patterns[random.nextInt(patterns.length)];
        wallSet = new WallSet(screen, this, wallPattern);
        wallSetLinkedList.add(wallSet);
        return wallSet;
    }

    public void update(float dt) {
        if (!active) return;
        stateTimer += dt;
        triggerDistance += dt * ((float) -Math.cos(screen.getPlane().getBody().getAngle()) * PlayScreen.PLANE_SPEED);
        if (stateTimer > FIRST_WALL_TIMER && !firstWallCreated) {
            createWallSet(); /*Set this wall to trigger the creation of next wall*/
            firstWallCreated = true;
            triggerDistance = 0f;
        }
        else if (firstWallCreated && triggerDistance > 15f) {
            createWallSet();
            triggerDistance = 0f;
        }

        for (Iterator<WallSet> iterator = wallSetLinkedList.iterator(); iterator.hasNext();) {
            WallSet wallSet = iterator.next();
            wallSet.update(dt);

            /*Remove wall set if it's empty*/
            if (wallSet.isEmpty()) {
                iterator.remove();
            }
        }

    }

    public void render(Matrix4 projectionMatrix) {
        renderer.setProjectionMatrix(projectionMatrix);
        renderer.begin(ShapeType.Filled);
        for (WallSet wallSet : wallSetLinkedList) {
            wallSet.render(renderer);
        }
        leftWall.render(renderer);
        rightWall.render(renderer);

        renderer.end();
    }

    /*Gets the x-point on the right edge of the left wall*/
    public float getLeftWallX() {
        float xPos = leftWall.getBody().getPosition().x + STATIC_WALL_WIDTH / 2;
        return xPos;
    }

    /*Gets the x-point on the left edge of the right wall*/
    public float getRightWallX() {
        float xPos = rightWall.getBody().getPosition().x - STATIC_WALL_WIDTH / 2;
        return xPos;
    }

    /*Gets the absolute x-position between the two main walls based on a relative position.
     */
    public float getAbsoluteXPositionBetweenWalls(float relativeXPosition) {
        float lowerXBound = getLeftWallX();
        float higherXBound = getRightWallX();
        float xRange = higherXBound - lowerXBound;
        float scale = xRange * relativeXPosition;
        return lowerXBound + scale;
    }

    public float getDistanceBetweenWalls() {
        return getRightWallX() - getLeftWallX();
    }

    public float getMiddlePointXBetweenWalls() {
        return getLeftWallX() + getDistanceBetweenWalls() / 2;
    }

    public void deactivate() {
        active = false;
        for (WallSet wallSet : wallSetLinkedList) {
            wallSet.deactivate();
        }
    }

    public int getWallCount() {
        int count = 0;
        for(WallSet w : wallSetLinkedList) {
            count += w.size();
        }
        return count;
    }
}
