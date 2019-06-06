package com.example.virginia.jcmachines.Data;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class converteraugers {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<augers> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<augers>>() {}.getType();
        return converteraugers.gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<augers> someObjects) {
        return converteraugers.gson.toJson(someObjects);
    }
}