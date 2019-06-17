package com.example.virginia.jcmachines.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.example.virginia.jcmachines.R;

public class appearAnimText {
    TextView mTextview;
    int shortAnimationDuration;
    Context mContext;

    public appearAnimText(TextView text, Context context){
        mContext=context;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);
        mTextview =text;

    }
    public void animate(){
        mTextview.setVisibility(View.VISIBLE);
        mTextview.animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
    }

}
