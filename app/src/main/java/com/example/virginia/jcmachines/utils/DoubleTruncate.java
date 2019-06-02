package com.example.virginia.jcmachines.utils;

import android.content.Context;

import com.example.virginia.jcmachines.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleTruncate {
    double truncatedNumber;
    public DoubleTruncate(double number, Context context){
        setTruncatedNumber(new BigDecimal(number)
                .setScale(context.getResources().getInteger(R.integer.rounding_places), RoundingMode.HALF_UP)
                .doubleValue());
    }

    public double getTruncatedNumber() {
        return truncatedNumber;
    }

    public void setTruncatedNumber(double truncatedNumber) {
        this.truncatedNumber = truncatedNumber;
    }
}
