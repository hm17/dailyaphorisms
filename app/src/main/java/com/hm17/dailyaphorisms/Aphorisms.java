package com.hm17.dailyaphorisms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class Aphorisms extends AppCompatActivity {
    private int dailyCount = 0;

    // Users are limited to 3 quotes a day.
    // How to restrict users from resetting dailycount when app restarts?
    private int hardLimit = 2;

    private final static String ERROR_LIMIT_MSG = "Sorry! You've reached your limit for today!";

    Map<Integer, String> dictionary = new HashMap<>();
    Map<Integer, Integer> cache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aphorisms);

        populateDictionary();
        setText();

    }

    public void nextAphorism(View view) {
        setText();
        dailyCount++;
    }

    private void setText(){
        String text;

        if(dailyCount >= hardLimit){
            TextView textView = (TextView) findViewById(R.id.textInfo);
            textView.setText(ERROR_LIMIT_MSG);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);

            text = "";
        }
        else {
            int random = getQuoteId();
            int numAttempts = 0;
            Boolean onlyDupsLeft = false;

            // Check cache for dups up to 3 times
            while(cache.containsKey(random)){
                random = getQuoteId();
                if(numAttempts >= hardLimit){
                    onlyDupsLeft = true;
                    break;
                }
            }
            if(onlyDupsLeft){
                text = ERROR_LIMIT_MSG;
            }
            else {
                text = dictionary.get(random);
                cache.put(random, 1);
            }

        }
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        //textView.setWidth();
    }

    private int getQuoteId(){
        return (int )(Math.random() * dictionary.size());
    }

    private void populateDictionary(){
        dictionary.put(0, "I crave a love so deep the ocean would be jealous.");
        dictionary.put(1, "You make my dopamine levels go all silly.");
        dictionary.put(2, "Just say yes.");
        dictionary.put(3, "When you are stuck in a place where you feel you are not good enough, " +
                "remember that what is right with you is always more than what is wrong with you.");
        dictionary.put(4, "Anything is possibleeeeee!");
        dictionary.put(5, "Your vibe attracts your tribe.");
    }

}
