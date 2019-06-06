package com.example.virginia.jcmachines;


import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LargeDimensionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_dimensions);
        ImageView largeDimPictv;
        largeDimPictv=findViewById(R.id.large_dimens_iv);

    }
}
