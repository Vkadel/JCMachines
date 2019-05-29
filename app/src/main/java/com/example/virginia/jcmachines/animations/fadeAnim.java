package com.example.virginia.jcmachines.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.example.virginia.jcmachines.R;

public class fadeAnim {
    LinearLayout mlinearLayout;
    int shortAnimationDuration;
    Context mContext;

    public fadeAnim(LinearLayout linearLayout, Context context){
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
