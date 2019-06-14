package com.example.virginia.jcmachines.utils;

import android.content.Context;

import com.example.virginia.jcmachines.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleTruncateZero {
    double truncatedNumber;
    public DoubleTruncateZero(double number, Context context){
        setTruncatedNumber(new BigDecimal(number)
                .setScale(context.getResources().getInteger(R.integer.rounding_places_noend), RoundingMode.HALF_EVEN)
                .doubleValue());
    }

    public double getTruncatedNumber() {
        return truncatedNumber;
    }

    public void setTruncatedNumber(double truncatedNumber) {
        this.truncatedNumber = truncatedNumber;
    }
}
