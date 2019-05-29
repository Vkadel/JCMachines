package com.example.virginia.jcmachines.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.virginia.jcmachines.R;

public class appearAnim {
    LinearLayout mlinearLayout;
    int shortAnimationDuration;
    Context mContext;

    public appearAnim(LinearLayout linearLayout,Context context){
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
