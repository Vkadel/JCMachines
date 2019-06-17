package com.example.virginia.jcmachines.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;

import com.example.virginia.jcmachines.R;

public class fadeAnimBar {
    ProgressBar myProgressBar;
    int shortAnimationDuration;
    Context mContext;

    public fadeAnimBar(ProgressBar progressBar, Context context){
        mContext=context;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);
        myProgressBar =progressBar;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);

    }
    public void animate(){
        myProgressBar.animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        myProgressBar.setVisibility(View.GONE);
                    }
                });
    }

}
