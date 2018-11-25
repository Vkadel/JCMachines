package com.example.virginia.jcmachines.Data;

import android.arch.persistence.room.TypeConverter;

import com.example.virginia.jcmachines.Data.keyFeatures;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class converterInstructionalVids {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<instructionalVids> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<instructionalVids>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<instructionalVids> someObjects) {
        return gson.toJson(someObjects);
    }
}