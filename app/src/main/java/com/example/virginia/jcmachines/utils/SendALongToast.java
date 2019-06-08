package com.example.virginia.jcmachines.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class SendALongToast {
    Toast toast;
    public SendALongToast(Context context, String message)
    {
        toast=Toast.makeText(context,message,Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);

    }
    public void show(){
        toast.show();
    }
}
