package com.aaron.helloannotation.factory;

import com.aaron.annotation.Factory;

/**
 * @author Aaron aaronzzxup@gmail.com
 */
@Factory(id = "baseball", type = Ball.class)
public class BaseBall implements Ball {

    private static final String TAG = "BaseBall";

    @Override
    public void play() {
        System.out.println(String.format("Play %s", TAG));
    }
}
