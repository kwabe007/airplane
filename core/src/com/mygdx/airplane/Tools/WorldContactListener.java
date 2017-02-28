package com.mygdx.airplane.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.airplane.Airplane;
import com.mygdx.airplane.Entities.Plane;
import com.mygdx.airplane.Objects.WallHandler;

/**
 * Created by Kwa on 2016-07-28.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch (cDef) {

            case Airplane.PLANE_BIT | Airplane.WALL_BIT:
                if (fixA.getFilterData().categoryBits == Airplane.PLANE_BIT) {
                    ((Plane) fixA.getUserData()).setToDestroy();
                    ((WallHandler) fixB.getUserData()).deactivate();
                }
                else if (fixB.getFilterData().categoryBits == Airplane.PLANE_BIT) {
                    ((Plane) fixB.getUserData()).setToDestroy();
                    ((WallHandler) fixA.getUserData()).deactivate();
                }
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
