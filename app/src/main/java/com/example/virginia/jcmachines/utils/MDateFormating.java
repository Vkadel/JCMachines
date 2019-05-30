package com.example.virginia.jcmachines.utils;

import android.content.Context;

import com.example.virginia.jcmachines.R;

import java.util.Calendar;

public class MDateFormating {
    String mdate;
    Context mContext;

   public MDateFormating(Context context){
       mContext=context;
       String mAm_PM;
       int day= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
       int month=Calendar.getInstance().get(Calendar.MONTH);
       int year=Calendar.getInstance().get(Calendar.YEAR);
       int hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
       int min=Calendar.getInstance().get(Calendar.MINUTE);

       mdate=context.getResources()
               .getString(R.string.date_for_calculation_format,day,month,year,hour,min);
   }
    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }
}
