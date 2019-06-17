package com.example.virginia.jcmachines.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.virginia.jcmachines.R;

public class fadeText {
    TextView textView;
    int shortAnimationDuration;
    Context mContext;

    public fadeText(TextView text, Context context){
        mContext=context;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);
        textView =text;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);

    }
    public void animate(){
        textView.animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textView.setVisibility(View.GONE);
                    }
                });
    }

}
