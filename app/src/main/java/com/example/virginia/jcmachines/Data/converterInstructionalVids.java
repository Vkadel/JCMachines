package com.example.virginia.jcmachines.Data;

import androidx.room.TypeConverter;

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
        return converterInstructionalVids.gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<instructionalVids> someObjects) {
        return converterInstructionalVids.gson.toJson(someObjects);
    }
}