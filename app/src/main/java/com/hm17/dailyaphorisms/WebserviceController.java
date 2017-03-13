package com.hm17.dailyaphorisms;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Connect to server and perform HTTP requests.
 * <p>
 * Created by hmadolid on 3/12/17.
 */

public class WebserviceController {

    private RequestQueue queue;

    // Get from config file eventually
    private final static String SERVER_URL = "http://dailyaphorisms.hazella.co/api.php";

    public WebserviceController(Context context) {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context);

    }

    public void get(final VolleyCallback callback) {

        String url = SERVER_URL + "?action=get_app&id=1";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response is: " + response);
                        callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
