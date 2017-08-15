package com.zano.shareride.network.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 * Created by Zano on 15/08/2017, 18:51.
 */

public class LocalDateTypeConverter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.date();

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        LocalDate result = null;
        if(!json.getAsString().isEmpty())
            result = DATE_FORMAT.parseLocalDate(json.getAsString());
        return result;
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        String result = "";
        if(src!=null){
            result = DATE_FORMAT.print(src);
        }
        return new JsonPrimitive(result);
    }
}
