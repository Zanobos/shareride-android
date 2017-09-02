package com.zano.shareride.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zano.shareride.network.checkpath.CheckPathRequest;
import com.zano.shareride.network.checkpath.CheckPathResponse;
import com.zano.shareride.util.PropertiesReader;

import java.util.Properties;

/**
 * Created by Zano on 16/07/2017, 11:11.
 */

public class NetworkController {

    protected static String TAG = "NetworkController";

    private static final String CHECK_PATH_URL = "/bookingService/checkPath";
    private static final int TIMEOUT_MS = 10000;
    private static final int MAX_RETRIES = 0;

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
        request.setRetryPolicy( new DefaultRetryPolicy(TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void addCheckPathRequest(CheckPathRequest checkPathRequest, Response.Listener<CheckPathResponse> listener, Response.ErrorListener errorListener){

        String url = serverBasePath + CHECK_PATH_URL;
        GsonRequest request = new GsonRequest(Request.Method.POST, url, checkPathRequest, listener, errorListener != null ? errorListener : new DefaultErrorListener(), CheckPathResponse.class);
        Log.d(TAG,"Enqueuing a request to: " + url + ", REQUEST: " + checkPathRequest);
        addToRequestQueue(request);
    }

    private class DefaultErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "onErrorResponse:" + error.getMessage(),error);
        }
    }
}
