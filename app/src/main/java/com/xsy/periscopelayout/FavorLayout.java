package com.xsy.periscopelayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by xsy on 2017/9/11.
 * 参考：http://www.jianshu.com/p/03fdcfd3ae9c
 */

public class FavorLayout extends RelativeLayout {

    private Drawable[] drawables;
    private int dWidth;
    private int dHeight;
    private LayoutParams lp;
    private Random random = new Random();
    private int mWidth;
    private int mHeight;

    private Interpolator line = new LinearInterpolator();//线性
    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
    private Interpolator[] interpolators;

    public FavorLayout(Context context) {
        super(context);
        init();
    }

    public FavorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private void init() {
        drawables = new Drawable[4];
        Drawable red = getResources().getDrawable(R.mipmap.heart_red);
        Drawable purple = getResources().getDrawable(R.mipmap.heart_purple);
        Drawable yellow = getResources().getDrawable(R.mipmap.heart_shit);
        Drawable blue = getResources().getDrawable(R.mipmap.heart_blue);
        drawables[0] = red;
        drawables[1] = purple;
        drawables[2] = yellow;
        drawables[3] = blue;

        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;

        dWidth = red.getIntrinsicWidth();
        dHeight = red.getIntrinsicHeight();
        lp = new LayoutParams(dWidth, dHeight);
        lp.addRule(CENTER_HORIZONTAL,TRUE);
        lp.addRule(ALIGN_PARENT_BOTTOM,TRUE);
    }
    public void addHeart(Context ctx){
        ImageView imageView = new ImageView(ctx);
        imageView.setImageDrawable(drawables[random.nextInt(4)]);
        imageView.setLayoutParams(lp);
        addView(imageView);
        Animator animator = getAnimator(imageView);
        animator.start();
    }
    private Animator getAnimator(final View target){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1.0f);
        AnimatorSet inSet = new AnimatorSet();
        inSet.setDuration(300);
        inSet.setInterpolator(line);
        inSet.playTogether(alpha,scaleX,scaleY);
        inSet.setTarget(target);

        BezierEvaluator bezierEvaluator = new BezierEvaluator(getPointF(2),getPointF(1));
        ValueAnimator animator = ValueAnimator.ofObject(bezierEvaluator, new PointF((mWidth - dWidth) / 2, mHeight - dHeight), new PointF(random.nextInt(getWidth()), 0));
        animator.setTarget(target);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                target.setX(pointF.x);
                target.setY(pointF.y);
                target.setAlpha(1-animation.getAnimatedFraction()/2);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(inSet,animator);
        animatorSet.setInterpolator(interpolators[random.nextInt(4)]);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(target);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.setTarget(target);
        return animatorSet;
    }
    private PointF getPointF(int scale){
        PointF pointF = new PointF();
        pointF.x = random.nextInt(mWidth-300);
        pointF.y = random.nextInt(mHeight-100)/scale;
        return pointF;
    }
}
