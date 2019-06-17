package com.example.virginia.jcmachines.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.virginia.jcmachines.R;

public class appearAnimLayout {
    LinearLayout mlinearLayout;
    int shortAnimationDuration;
    Context mContext;

    public appearAnimLayout(LinearLayout linearLayout, Context context){
        mContext=context;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);
        mlinearLayout =linearLayout;

    }
    public void animate(){
        mlinearLayout.setVisibility(View.VISIBLE);
        mlinearLayout.animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
    }

}
