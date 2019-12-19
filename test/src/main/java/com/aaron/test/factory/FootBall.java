package com.aaron.test.factory;

import com.aaron.annotation.Factory;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
@Factory(id = "football", type = Ball.class)
public class FootBall implements Ball {

    private static final String TAG = "FootBall";

    @Override
    public void play() {
        System.out.println(String.format("Play %s", TAG));
    }
}
