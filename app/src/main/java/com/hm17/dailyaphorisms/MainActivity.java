package com.hm17.dailyaphorisms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Map;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    int dailyCount = 0;

    // Users are limited to 3 quotes a day.
    int hardLimit = 3;

    Map<Integer, String> dictionary = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateDictionary();

    }

    public void nextAphorism(View view) {
        String text;

        if(dailyCount >= hardLimit){
            text = "Sorry! You've reached your limit for today!";
        }
        else {
            // Need to keep track of duplicate numbers so hard limit is not reached with dups
            int random = (int )(Math.random() * dictionary.size() + 1);
            text = dictionary.get(random);
        }
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(text);
        dailyCount++;
    }

    private void populateDictionary(){
        dictionary.put(0, "This is the first aphorism.");
        dictionary.put(1, "All that glitters is not gold");
        dictionary.put(2, "Just say yes.");
        dictionary.put(3, "You can do it!");
        dictionary.put(4, "Anything is possibleeeeee!");
    }
}
