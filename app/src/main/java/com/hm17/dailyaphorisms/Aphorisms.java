package com.hm17.dailyaphorisms;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hm17.dailyaphorisms.constants.Colors;
import com.hm17.dailyaphorisms.services.OnSwipeTouchListener;
import com.hm17.dailyaphorisms.services.Sanitizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aphorisms extends AppCompatActivity {

    private WebserviceDao webserviceDao;

    private Sanitizer sanitizer;

    private int dailyCount = 0;

    private View view;
    private TextView textViewMain;

    // TODO: Remove users' quote limits, every 5 quotes show an ad
    private int hardLimit = 50;

    // TODO: Put in config file (get fr server)
    private final static int QUOTES_TOTAL = 30;

    private final static String ERROR_LIMIT_MSG = "Sorry! You've reached your limit for today!";

    Map<Integer, String> dictionary = new HashMap<>();
    Map<Integer, Integer> cache = new HashMap<>();
    Map<Integer, String> colorCache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aphorisms);

        webserviceDao = new WebserviceDao(this);
        sanitizer = new Sanitizer();

        initializeDictionary();

        textViewMain = (TextView) findViewById(R.id.textDisplay);
        setText();

        // Set up swipe on view
        view = findViewById(R.id.OuterRelativeLayout);
        view.setOnTouchListener(new OnSwipeTouchListener(Aphorisms.this) {
            public void onSwipeRight() {
                // TODO: FUTURE ENHANCEMENT - Go to previous quote
            }
            public void onSwipeLeft() {
                nextAphorism(textViewMain);
            }
        });

        buildColorCache();

    }

    public void nextAphorism(View view) {
        changeBackgroundColor();
        setText();
        dailyCount++;

        // TODO: Send random number w/ QUOTE_TOTAL as limit

    }

    /**
     * Call to Webservice and initialize dictionary with aphorisms.
     */
    private void initializeDictionary(){
        webserviceDao.get(new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                //System.out.println("result: " + result);
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

                // Split pairs on commas outside of ""
                Map<Integer, String> aphorisms = new HashMap<>();
                for(String info : aInfo) {
                    String[] pairs = info.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    // Put pairs into map
                    Map<String, Object> aphorism = new HashMap<>();
                    for(String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        String key = keyValue[0].replace("\"", "");
                        String value = keyValue[1].replace("\"", "");
                        if(key.equalsIgnoreCase("id")){
                            aphorism.put(key, Integer.parseInt(value));
                        }
                        else {
                            aphorism.put(key, value);
                        }
                    }

                    // Sanitize quote and add to master map
                    String quote = sanitizer.sanitize((String) aphorism.get("aphorism"));
                    aphorisms.put((Integer) aphorism.get("id"), quote);
                }

                dictionary = aphorisms;
            }
        });
    }

    private void changeBackgroundColor(){
        View someView = findViewById(R.id.OuterRelativeLayout);

        // Find the root view
        View root = someView.getRootView();

        // Pick a random color
        String color = getColorFromCache(getRandomNumber(colorCache.size()));

        // Set the color
        root.setBackgroundColor(Color.parseColor(color));
    }


    private String getColorFromCache(int colorId){
        // get color from cache
        return colorCache.get(colorId);
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

        textViewMain.setText(text);
        textViewMain.setGravity(Gravity.CENTER_HORIZONTAL);

        // Set width to be parent
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        textViewMain.getLayoutParams().width = width;
    }

    private int getQuoteId(){
        return getRandomNumber(dictionary.size());
    }

    private int getRandomNumber(int dictionarySize) {
        return (int )(Math.random() * dictionarySize);
    }

    private void buildColorCache(){
        colorCache.put(0, Colors.COLOR_PINK_HAZEL);
        colorCache.put(1, Colors.COLOR_TEAL_KENNY);
        colorCache.put(2, Colors.COLOR_LAVENDER_AARON);
        colorCache.put(3, Colors.COLOR_GRAY_JUDSON);
    }

}
