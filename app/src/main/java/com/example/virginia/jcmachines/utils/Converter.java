package com.example.virginia.jcmachines.utils;


import androidx.databinding.InverseMethod;

public class Converter {
    @InverseMethod("stringToDouble")
    public String doubleToString(double value) {
        // Converts long to String.

        return String.valueOf(value);
    }

    public double stringToDouble(String value) {
        // Converts String to long..
        // Converts long to String.
        double number=Double.valueOf(value);
        return number;
    }
}
