package com.mygdx.airplane.Tools;

/**
 * Created by Kwa on 2016-08-29.
 */

/*This class is meant act as a function from a set of x values to a set of values y where
if x1 < x2 then f(x1) ≥ f(x2). So, as x increases the function gives either the same value
or less but won't give a value less than a constant value k (lowerBound). The function will also
 never give a value higher than a constant value 'l' (higherBound)

  */
public class Function {

    float start;
    float end;
    int steps;
    float lowerBound;
    float higherBound;

    public Function(float end, int steps, float lowerBound, float higherBound) {
        this.start = 0f;
        this.end = end;
        this.steps = steps;
        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
    }

    /*Takes the step given by the number and uses it in a 1/x function to create a factor which will
    be used to give the value in the bound decided by lower/higherBound.
     */
    public float multiplicationInverse(float number) {
        int step = getStep(number);
        float inverse = 1f / step;
        float scale = inverse * (higherBound - lowerBound);
        float result = scale + lowerBound;
        return result;
    }

    /*Takes a number in the range and gives the corrisponding discrete step.
    * For example, if steps is 10 and the range is from 0-20, numbers 0 and 1 will give step 1
    * 2 and 3 will give step 2 and so on.*/
    public int getStep (float number) {
        float sizeOfSteps = (end - start) / steps;
        int step = (int) (number / sizeOfSteps) + 1;
        if (step > steps)
            step = steps;
        return step;
    }
}
