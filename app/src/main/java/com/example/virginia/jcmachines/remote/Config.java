package com.example.virginia.jcmachines.remote;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static final URL BASE_URL;
    private static final String TAG = Config.class.toString();

    static {
        URL url = null;
        try {
            url = new URL("https://firebasestorage.googleapis.com/v0/b/jcmachines-1542559889392.appspot.com/o/jc_machines.json?alt=media&token=442f2ad5-3aec-4644-9eb2-957ca88285ad");
        } catch (MalformedURLException ignored) {
            Log.e(Config.TAG, "Please check your internet connection.");
        }

        BASE_URL = url;
    }
}
