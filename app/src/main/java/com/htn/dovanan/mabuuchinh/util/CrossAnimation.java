package com.htn.dovanan.mabuuchinh.util;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.htn.dovanan.mabuuchinh.listener.AnimationEndListener;

/*
Custom animation
slide up
slide down
fade in
fade out
 */
public class CrossAnimation {

    public static String TAG = "Cross_Animation";

    public static void slideUp(final View view) {
        view.setVisibility(View.VISIBLE);
        int toYdelta = -view.getHeight();
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,              // fromYDelta
                toYdelta);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void slideUp(final View view, final AnimationEndListener endAnimation) {
        view.setVisibility(View.VISIBLE);
        int toYdelta = -view.getHeight();
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,              // fromYDelta
                toYdelta);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        /*
            Listener animation
         */
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /*
                when finish animation
                header gone
                searchMain visible
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                endAnimation.endAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    public static void slideDown(final View view) {
        int toYdelta = -view.getHeight();
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                toYdelta,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    public static void slideDown(final View view, final AnimationEndListener endAnimation) {
        view.setVisibility(View.VISIBLE);
        int toYdelta = -view.getHeight();
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                toYdelta,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                endAnimation.endAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    /* di chuyen sang trai */
    public static void slideLeft(final View view) {
        view.setVisibility(View.VISIBLE);
        int toXdelta = -view.getWidth() - 50;
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                toXdelta,                   // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(250);
        animate.setFillAfter(true);

        view.startAnimation(animate);

    }

    /* di chuyen sang phai */
    public static void slideRight(final View view) {
        view.setVisibility(View.VISIBLE);
        int fromXdelta = -view.getWidth() - 50;

        TranslateAnimation animate = new TranslateAnimation(
                fromXdelta,                 // fromXDelta
                0,                   // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(250);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void fadeIn(final View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);
        AnimationSet mAnimationSet = new AnimationSet(false);
        mAnimationSet.addAnimation(fadeIn);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(mAnimationSet);
    }

    public static void fadeIn(final View view, final AnimationEndListener endAnimation) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1500);
        AnimationSet mAnimationSet = new AnimationSet(false);
        mAnimationSet.addAnimation(fadeIn);
            mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.VISIBLE);
                    endAnimation.endAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        view.setAnimation(mAnimationSet);
    }

    public static void fadeOut(final View view) {
        Animation fadeOut = new AlphaAnimation(1.00f, 0.00f);
        fadeOut.setStartOffset(100);
        fadeOut.setDuration(500);
        view.startAnimation(fadeOut);
    }

    public static void fadeOut(final View view, final AnimationEndListener endAnimation) {
        Animation fadeOut = new AlphaAnimation(1.00f, 0.00f);
        fadeOut.setStartOffset(100);
        fadeOut.setDuration(500);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                endAnimation.endAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fadeOut);
    }

    public static void routate180(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setFillAfter(true);

        RotateAnimation anim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        animationSet.addAnimation(anim);
        view.startAnimation(animationSet);
    }

    public static void routate0(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setFillAfter(true);

        RotateAnimation anim = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        animationSet.addAnimation(anim);
        view.startAnimation(animationSet);
    }

}
