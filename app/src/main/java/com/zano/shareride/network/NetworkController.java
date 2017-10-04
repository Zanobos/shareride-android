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
import com.zano.shareride.network.confirmrequest.ConfirmRequestRequest;
import com.zano.shareride.network.confirmrequest.ConfirmRequestResponse;
import com.zano.shareride.network.userrequestlist.UserRequestListRequest;
import com.zano.shareride.network.userrequestlist.UserRequestListResponse;
import com.zano.shareride.util.PropertiesReader;

import java.util.Properties;

/**
 * Created by Zano on 16/07/2017, 11:11.
 */

public class NetworkController {

    protected static String TAG = "NetworkController";

    private static final String CHECK_PATH_URL = "/bookingService/checkPath";
    private static final String CONFIRM_REQUEST_URL = "/bookingService/confirmRequest";
    private static final String USER_REQUEST_LIST_URL = "/bookingService/userRequestList";
    private static NetworkController instance;

    private RequestQueue requestQueue;
    private String serverBasePath;
    private int maxRetries;
    private int timeOutMilliseconds ;

    private NetworkController(Context context) {
        requestQueue  = Volley.newRequestQueue(context.getApplicationContext());
        Properties properties = new PropertiesReader(context).getProperties();
        serverBasePath = properties.getProperty("server.address") + properties.getProperty("server.base.path");
        maxRetries = Integer.parseInt(properties.getProperty("server.max.retries"));
        timeOutMilliseconds = Integer.parseInt(properties.getProperty("server.timeout.milliseconds"));
    }

    public static NetworkController getInstance(Context context) {
        if(instance == null) {
            instance = new NetworkController(context);
        }
        return instance;
    }

    private <T> void addToRequestQueue(Request<T> request) {
        request.setRetryPolicy( new DefaultRetryPolicy(timeOutMilliseconds,maxRetries,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void addCheckPathRequest(CheckPathRequest checkPathRequest, Response.Listener<CheckPathResponse> listener, Response.ErrorListener errorListener){

        String url = serverBasePath + CHECK_PATH_URL;
        GsonRequest request = new GsonRequest(Request.Method.POST, url, checkPathRequest, listener, errorListener != null ? errorListener : new DefaultErrorListener(), CheckPathResponse.class);
        Log.d(TAG,"Enqueuing a request to: " + url + ", REQUEST: " + checkPathRequest);
        addToRequestQueue(request);
    }

    public void addConfirmRequestRequest(ConfirmRequestRequest confirmRequestRequest, Response.Listener<ConfirmRequestResponse> listener, Response.ErrorListener errorListener){

        String url = serverBasePath + CONFIRM_REQUEST_URL;
        GsonRequest request = new GsonRequest(Request.Method.POST, url, confirmRequestRequest, listener, errorListener != null ? errorListener : new DefaultErrorListener(), ConfirmRequestResponse.class);
        Log.d(TAG,"Enqueuing a request to: " + url + ", REQUEST: " + confirmRequestRequest);
        addToRequestQueue(request);
    }

    public void addUserRequestListRequest(UserRequestListRequest userRequestListRequest, Response.Listener<UserRequestListResponse> listener, Response.ErrorListener errorListener){

        String url = serverBasePath + USER_REQUEST_LIST_URL;
        GsonRequest request = new GsonRequest(Request.Method.POST, url, userRequestListRequest, listener, errorListener != null ? errorListener : new DefaultErrorListener(), UserRequestListResponse.class);
        Log.d(TAG,"Enqueuing a request to: " + url + ", REQUEST: " + userRequestListRequest);
        addToRequestQueue(request);
    }

    private class DefaultErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, "onErrorResponse:" + error.getMessage(),error);
        }
    }
}
