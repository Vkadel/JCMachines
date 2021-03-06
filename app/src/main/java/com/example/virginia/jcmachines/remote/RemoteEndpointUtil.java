package com.example.virginia.jcmachines.remote;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import timber.log.Timber;

public class RemoteEndpointUtil {
    private static final String TAG = "RemoteEndpointUtil";
    private static String JsonString;
    private RemoteEndpointUtil() {
    }

    public static JSONArray fetchJsonArray() {
        String itemsJson = null;
        try {
            itemsJson = RemoteEndpointUtil.fetchPlainText(Config.BASE_URL);
            //Timber.d("Going to send this data from Fetch: %s", itemsJson);
        } catch (IOException e) {
            Timber.e(e, "Error fetching items JSON");
            return null;
        }

        // Parse JSON
        try {
            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            return (JSONArray) val;
        } catch (JSONException e) {
            Log.e(RemoteEndpointUtil.TAG, "Error parsing items JSON", e);
        }

        return null;
    }
    public static String fetchJsonString() {
        String itemsJson = null;
        try {
            itemsJson = RemoteEndpointUtil.fetchPlainText(Config.BASE_URL);
            //Timber.d("Going to send this data from Fetch: %s", itemsJson);
        } catch (IOException e) {
            Timber.e(e, "Error fetching items JSON");
            return null;
        }
        return itemsJson;
    }

    static String fetchPlainText(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String JsonString=response.body().string();
        return JsonString;
    }



}
