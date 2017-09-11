package com.zano.shareride.network.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 * Created by Zano on 15/08/2017, 18:51.
 */

public class LocalTimeTypeConverter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.hourMinuteSecond();

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        LocalTime result = null;
        if (!json.getAsString().isEmpty())
            result = TIME_FORMAT.parseLocalTime(json.getAsString());
        return result;

    }

    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {

        String result = "";
        if (src != null) {
            result = TIME_FORMAT.print(src);
        }
        return new JsonPrimitive(result);
    }
}
