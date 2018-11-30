package com.example.virginia.jcmachines.Data;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class converterFeatures {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<keyFeatures> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<keyFeatures>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<keyFeatures> someObjects) {
        return gson.toJson(someObjects);
    }
}