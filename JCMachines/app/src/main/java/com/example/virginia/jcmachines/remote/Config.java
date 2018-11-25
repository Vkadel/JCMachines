package com.example.virginia.jcmachines.remote;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static final URL BASE_URL;
    private static String TAG = Config.class.toString();

    static {
        URL url = null;
        try {
            url = new URL("https://raw.githubusercontent.com/Vkadel/JCMachines/master/JCMachines/app/assets/jc_machines.json");
        } catch (MalformedURLException ignored) {
            Log.e(TAG, "Please check your internet connection.");
        }

        BASE_URL = url;
    }
}
