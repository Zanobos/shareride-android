package com.zano.shareride.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zano.shareride.network.serialization.LocalDateTypeConverter;
import com.zano.shareride.network.serialization.LocalTimeTypeConverter;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.UnsupportedEncodingException;

/**
 * Created by a.zanotti on 8/22/2017.
 */

public class GsonRequest<T> extends JsonRequest<T> {

    private final Class<T> type;

    private static Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeConverter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeConverter());
        gson = builder.create();
    }

    public GsonRequest(int method, String url, Object request, Response.Listener<T> listener, Response.ErrorListener errorListener, Class<T> type) {
        super(method, url, gson.toJson(request), listener, errorListener);
        this.type = type;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(gson.fromJson(jsonString,type),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
