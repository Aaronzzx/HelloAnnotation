package com.aaron.helloannotation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aaron.helloannotation.factory.BallFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
