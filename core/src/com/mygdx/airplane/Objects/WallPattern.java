package com.mygdx.airplane.Objects;

import com.mygdx.airplane.Tools.Triple;

import java.util.ArrayList;
import java.util.Scanner;



/**
 * Created by Kwa on 2016-09-21.
 */
public class WallPattern {

    protected ArrayList<Triple<Float, Float, Float>> tripleArray;


    public WallPattern(Scanner scanner) {
        tripleArray = new ArrayList<Triple<Float, Float, Float>>();

        /*Read how many walls (int)*/
        int noOfWalls = scanner.nextInt();

        /*Read each walls (relative) length and position*/
        for (int i = 0; i < noOfWalls; ++i) {
            String lengthString = scanner.next();
            String[] stringArray = lengthString.split("-");
            Float relStartCoordinate = Float.valueOf(stringArray[0]);
            Float relEndCoordinate = Float.valueOf(stringArray[1]);
            Triple<Float, Float, Float> triple = new Triple<Float, Float, Float>(relStartCoordinate, relEndCoordinate, 0.f); //height will be set later
            tripleArray.add(triple);
        }

        /*Read each walls (relative) height*/
        for (int i = 0; i < noOfWalls; ++i) {
            Triple<Float, Float, Float> triple = tripleArray.get(i);
            Float height = scanner.nextFloat();
            triple.setThird(height);
        }

    }


}
