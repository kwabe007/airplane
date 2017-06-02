package com.mygdx.airplane.NetworkProcess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

/**
 * Created by kwabena on 2017-03-12.
 */

public class SendScore implements Net.HttpResponseListener {

    public SendScore() {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("http://127.0.0.1:3000")
                .content("q=libgdx&example=example").build();
        Gdx.net.sendHttpRequest(httpRequest, this);
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {

        System.out.println("SUCCESFUL HTTP REQUEST");
        System.out.println(httpResponse.getResultAsString());

    }

    @Override
    public void failed(Throwable t) {
        System.out.println("FAILED HTTP REQUEST");
        t.printStackTrace();

    }

    @Override
    public void cancelled() {

        System.out.println("HTTP REQUEST CANCELLED");

    }
}
