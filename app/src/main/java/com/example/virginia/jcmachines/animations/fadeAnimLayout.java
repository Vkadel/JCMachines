package com.example.virginia.jcmachines.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.virginia.jcmachines.R;

public class fadeAnimLayout {
    LinearLayout mlinearLayout;
    int shortAnimationDuration;
    Context mContext;

    public fadeAnimLayout(LinearLayout linearLayout, Context context){
        mContext=context;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);
        mlinearLayout =linearLayout;
        shortAnimationDuration=mContext.getResources().getInteger(R.integer.layout_animation_duration);

    }
    public void animate(){
        mlinearLayout.animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mlinearLayout.setVisibility(View.GONE);
                    }
                });
    }

}
