package com.zano.shareride.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.zano.shareride.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by a.zanotti on 8/18/2017.
 */

public class PropertiesReader {

    private static final String PREFIX = "config.";
    private static final String SUFFIX = ".properties";

    private Context context;
    private Properties properties;

    public PropertiesReader(Context context) {
        this.context = context;
        this.properties = new Properties();
    }

    public Properties getProperties() {

        AssetManager assetManager = context.getAssets();

        String fileName = PREFIX + BuildConfig.ENVIRONMENT + SUFFIX;
        try {
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            Log.e("PropertiesReader",e.getMessage(),e);
        }
        return properties;
    }
}
