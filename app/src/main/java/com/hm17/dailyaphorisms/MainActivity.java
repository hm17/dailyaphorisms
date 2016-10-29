package com.hm17.dailyaphorisms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void nextAphorism(View view) {
        System.out.println("Hello world!");

        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText("This is the next aphorism!");
    }
}
