package com.aaron.test.factory;

import com.aaron.annotation.Factory;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
@Factory(id = "basketball", type = Ball.class)
public class BasketBall implements Ball {

    private static final String TAG = "BasketBall";

    @Override
    public void play() {
        System.out.println(String.format("Play %s", TAG));
    }
}
