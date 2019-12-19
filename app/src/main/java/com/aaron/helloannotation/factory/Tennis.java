package com.aaron.helloannotation.factory;

import com.aaron.annotation.Factory;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
@Factory(id = "tennis", type = Ball.class)
public class Tennis implements Ball {

    private static final String TAG = "Tennis";

    @Override
    public void play() {
        System.out.println(String.format("Play %s", TAG));
    }
}
