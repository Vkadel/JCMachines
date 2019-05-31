package com.example.virginia.jcmachines.utils;

import android.content.Context;

import com.example.virginia.jcmachines.R;

import java.util.Calendar;

public class MDateFormating {
    String mdate;
    Context mContext;

    public MDateFormating(Context context) {
        mContext = context;
        String mAm_PM;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);

        mdate = context.getResources()
                .getString(R.string.date_for_calculation_format, day, month, year, hour, min);
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String convertMillisTodate(long dateLong) {
        String mAm_PM;
        String hourString;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        if(calendar.get(Calendar.AM_PM)==0){
            mAm_PM="am";
        }else{
            mAm_PM="pm";
        }
        if(hour<10){
            hourString=mContext.getString(R.string.hour_zero_add,hour);
        }else{
            hourString=String.valueOf(hour);
        }
        if(hour==0){
            hour=12;
        }
        mdate = mContext.getResources()
                .getString(R.string.date_for_calculation_format_nice, day, month, year, hour, min,mAm_PM);

        return mdate;
    }
}
