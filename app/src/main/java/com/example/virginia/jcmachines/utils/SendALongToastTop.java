package com.example.virginia.jcmachines.utils;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virginia.jcmachines.R;

public class SendALongToastTop {
    Toast toast;
    public SendALongToastTop(Context context, String message)
    {
        toast=Toast.makeText(context,message,Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextSize(context.getResources().getDimension(R.dimen.send_long_toast_top));
        v.setTextColor(context.getResources().getColor(R.color.colorPrimary,context.getTheme()));
        if( v != null) toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);}
    public void show(){
        toast.show();
    }
}
