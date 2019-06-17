package com.example.virginia.jcmachines.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import com.example.virginia.jcmachines.R;

public class DoWhenNetWorkIsActive {
Context mContext;
    public DoWhenNetWorkIsActive(Runnable doWhenNetworkIsOnRunable, Runnable onNetworkLostRunnable,Context context, Activity activity){
            mContext=context;
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            NetworkRequest.Builder builder=new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            NetworkRequest networkRequest=builder.build();
            //Setting up networkcallback for system changes. In here the actions for when user is
            //connected and disconnedted online are listed
            ConnectivityManager.NetworkCallback mCallback=new ConnectivityManager.NetworkCallback(){
                @Override
                public void onAvailable(Network network) {
                    new SendALongToast(context,context.getString(R.string.have_internet)).show();
                    activity.runOnUiThread(doWhenNetworkIsOnRunable);
                    super.onAvailable(network);
                }

                @Override
                public void onLost(Network network) {
                    activity.runOnUiThread(onNetworkLostRunnable);
                    super.onLost(network);
                }

                @Override
                public void onUnavailable() {
                    activity.runOnUiThread(onNetworkLostRunnable);
                    super.onUnavailable();
                }
            };
            //Asking to be notified of connect/disconnects from internet
            connectivityManager.registerNetworkCallback(networkRequest,mCallback);
    }

    public Boolean FirstCheck(){
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
       return  isConnected;
    }

}
