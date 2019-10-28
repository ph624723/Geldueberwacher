package com.example.emptytest.Views;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.emptytest.R;

public final class Fx {
    public static void slide_down(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        a.reset();

        v.clearAnimation();
        v.startAnimation(a);
    }

    public static void slide_up(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        a.reset();

        v.clearAnimation();
        v.startAnimation(a);
    }
}
