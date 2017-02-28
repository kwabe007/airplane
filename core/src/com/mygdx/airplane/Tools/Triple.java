package com.mygdx.airplane.Tools;

/**
 * Created by Kwa on 2016-09-24.
 */
public class Triple<F, S, T> {
    private F first; //first member of triple
    private S second; //second member of triple
    private T third; //second member of triple

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public void setThird(T third) {
        this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }
}
