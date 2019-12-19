package com.aaron.test.factory;

import com.aaron.annotation.Factory;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
@Factory(id = "pingpong", type = Ball.class)
public class PingPong implements Ball {

    private static final String TAG = "PingPong";

    @Override
    public void play() {
        System.out.println(String.format("Play %s", TAG));
    }
}
