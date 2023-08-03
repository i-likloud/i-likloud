package com.ssafy.likloud.util

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout

/**
 * 버튼에 X축으로 움직이는 애니메이션을 적용시킵니다.
 */
fun makeButtonAnimationXWithDuration(button: View, values: Float, myDuration : Long) {
    ObjectAnimator.ofFloat(button, "translationX", values).apply {
//            interpolator = DecelerateInterpolator()
        interpolator = OvershootInterpolator()
        duration = myDuration
        start()
    }
}



/**
 * 버튼에 X축으로 움직이는 애니메이션을 적용시킵니다.
 */
fun makeButtonAnimationX(button: View, values: Float) {
    ObjectAnimator.ofFloat(button, "translationX", values).apply {
//            interpolator = DecelerateInterpolator()
        interpolator = OvershootInterpolator()
        duration = 500
        start()
    }
}

/**
 * 버튼에 Y축으로 움직이는 애니메이션을 적용시킵니다.
 */
fun makeButtonAnimationY(button: View, values: Float) {
    ObjectAnimator.ofFloat(button, "translationY", values).apply {
//            interpolator = DecelerateInterpolator()
        interpolator = OvershootInterpolator()
//            duration = 500
        start()
    }
}

/**
 * 버튼에 크기를 조절하는 애니메이션을 적용시킵니다.
 */
fun makeButtonAnimationScale(button: View, values: Float) {
    ObjectAnimator.ofFloat(button, "scaleX", values).apply {
        duration = 500
        start()
    }
    ObjectAnimator.ofFloat(button, "scaleY", values).apply {
        duration = 500
        start()
    }
}

/**
 * 레이아웃에 사라지고 나타나는 애니메이션을 적용시킵니다.
 */
fun makeAnimationFade(layout: FrameLayout, values: Float) {
    ObjectAnimator.ofFloat(layout, "alpha", values).apply {
        duration = 600
        start()
    }
    ObjectAnimator.ofFloat(layout, "scaleX", values).apply {
        duration = 1000
        start()
    }
    ObjectAnimator.ofFloat(layout, "scaleY", values).apply {
        duration = 1000
        start()
    }
}