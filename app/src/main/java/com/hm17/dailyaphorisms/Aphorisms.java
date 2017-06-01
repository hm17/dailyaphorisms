package com.hm17.dailyaphorisms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aphorisms extends AppCompatActivity {

    private WebserviceController controller;

    private int dailyCount = 0;

    // Users are limited to 3 quotes a day.
    // How to restrict users from resetting dailycount when app restarts?
    private int hardLimit = 2;

    // TODO: Put in config file (get fr server)
    private final static int QUOTES_TOTAL = 30;

    private final static String ERROR_LIMIT_MSG = "Sorry! You've reached your limit for today!";

    private final static String COLOR_PINK = "#FFCCFF";
    private final static String COLOR_TEAL = "#1FC4BA";

    Map<Integer, String> dictionary = new HashMap<>();
    Map<Integer, Integer> cache = new HashMap<>();
    Map<Integer, String> colorCache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aphorisms);

        controller = new WebserviceController(this);

        populateDictionary();
        setText();

        buildColorCache();

    }

    public void nextAphorism(View view) {
        changeBackgroundColor();
        setText();
        dailyCount++;

        // TODO: Send random number w/ QUOTE_TOTAL as limit
        controller.get(new VolleyCallback(){
            @Override
            public void onSuccess(String result){

                String resultParsed = "";

                // Extract result from in between []
                Pattern p = Pattern.compile("\\[(.*?)\\]");
                Matcher m = p.matcher(result);
                while(m.find()) {
                    resultParsed = m.group(1);
                }

                // Extract aphorism info from {}
                List<String> aInfo = new ArrayList<>();
                p = Pattern.compile("\\{([^}]*)\\}");
                m = p.matcher(resultParsed);
                while (m.find()) {
                    aInfo.add(m.group(1));
                }
                
                for(String info : aInfo)
                    System.out.println(info);
/*
                // Convert to List<Map>
                List<Map> aphorisms = new ArrayList<>();
                for(String value : resultList){
                    System.out.println("-----value: " + value);
                    value = value.substring(1, value.length()-1);           //remove curly brackets
                    String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
                    Map<String,String> map = new HashMap<>();

                    for(String pair : keyValuePairs)                        //iterate over the pairs
                    {
                        System.out.println("-------pair: " + pair);
                        String[] entry = pair.split(":");
                        System.out.println("-------entry: " + entry);
                        map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                    }
                    aphorisms.add(map);
                }

                System.out.println(aphorisms);
                */
            }
        });
    }

    private void changeBackgroundColor(){
        View someView = findViewById(R.id.OuterRelativeLayout);

        // Find the root view
        View root = someView.getRootView();

        // Pick a random color
        //int color = getColor(getRandomNumber());

        // Set the color
        root.setBackgroundColor(Color.parseColor(COLOR_PINK));
    }

    /**
    private int getColor(int colorId){
        // get color from cache
        return colorCache.get(colorId);
    }
    **/

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

        // Set width to be parent
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        textView.getLayoutParams().width = width;
    }

    private int getQuoteId(){
        return (int )(Math.random() * dictionary.size());
    }

    private int getRandomNumber() {return (int )(Math.random() * dictionary.size());}

    private void populateDictionary(){
        dictionary.put(0, "I crave a love so deep the ocean would be jealous.");
        dictionary.put(1, "You make my dopamine levels go all silly.");
        dictionary.put(2, "Just say yes.");
        dictionary.put(3, "When you are stuck in a place where you feel you are not good enough, " +
                "remember that what is right with you is always more than what is wrong with you.");
        dictionary.put(4, "Anything is possibleeeeee!");
        dictionary.put(5, "Your vibe attracts your tribe.");
    }

    // TODO: Get colors from DB configuration
    private void buildColorCache(){
        colorCache.put(0, COLOR_PINK);
        colorCache.put(1, COLOR_TEAL);
    }

}
