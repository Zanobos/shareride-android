package com.zano.shareride.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zano.shareride.network.checkpath.CheckPathRequest;
import com.zano.shareride.network.serialization.LocalDateTypeConverter;
import com.zano.shareride.network.serialization.LocalTimeTypeConverter;
import com.zano.shareride.util.PropertiesReader;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

/**
 * Created by Zano on 16/07/2017, 11:11.
 */

public class NetworkController {

    protected static String TAG = "NetworkController";

    public static final String CHECK_PATH_URL = "/bookingService/checkPath";
    private static NetworkController instance;

    private RequestQueue requestQueue;
    private String serverBasePath;

    private NetworkController(Context context) {
        requestQueue  = Volley.newRequestQueue(context.getApplicationContext());
        Properties properties = new PropertiesReader(context).getProperties();
        serverBasePath = properties.getProperty("server.address") + properties.getProperty("server.base.path");
    }

    public static NetworkController getInstance(Context context) {
        if(instance == null) {
            instance = new NetworkController(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

    public void addCheckPathRequest(CheckPathRequest checkPathRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeConverter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeConverter());
        Gson gson = builder.create();

        String json = gson.toJson(checkPathRequest);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "error in marshalling:" + e.getMessage(),e);
        }
        String url = serverBasePath + CHECK_PATH_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObj, listener, errorListener != null ? errorListener : new DefaultErrorListener());
        Log.d(TAG,"Enqueuing a request to: " + url + ", REQUEST: " + jsonObj);
        addToRequestQueue(request);
    }

    private class DefaultErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "onErrorResponse:" + error.getMessage(),error);
        }
    }
}
