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
import com.zano.shareride.network.checkpath.CheckPathRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zano on 16/07/2017, 11:11.
 */

public class NetworkController {

    protected static String TAG = "NetworkController";

    public static final String ADDRESS = "http://192.168.1.104";
    public static final String PORT = ":8080";
    public static final String BASE_URL = "/ShareRideServer/rest";
    public static final String CHECK_PATH_URL = "/bookingService/checkPath";

    private static NetworkController instance;

    private RequestQueue requestQueue;

    private NetworkController(Context context) {
        requestQueue  = Volley.newRequestQueue(context.getApplicationContext());
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

        String json = new Gson().toJson(checkPathRequest);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "error in marshalling:" + e.getMessage(),e);
        }
        String url = ADDRESS + PORT + BASE_URL + CHECK_PATH_URL;
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
