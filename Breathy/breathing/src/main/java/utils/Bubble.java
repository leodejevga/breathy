package utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import breathing.philipps.apps.com.breathing.R;

public class Bubble extends android.support.v7.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private BubbleListener mListerner;
    private ValueAnimator mAnimatior;

    public Bubble(Context context) {
        super(context);
    }

    public Bubble(Context context, int rawidth, int bubbleNumber) {
        super(context);


        mListerner = (BubbleListener) context;

        int rawHeight=rawidth;
        if (bubbleNumber ==1){
            this.setImageResource(R.drawable.bu01);}
        if (bubbleNumber ==2){
            this.setImageResource(R.drawable.bu02);}
        if (bubbleNumber ==3){
            this.setImageResource(R.drawable.bu03);}

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawidth, context);
        ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
       // int x = getLayoutParams().height;
    }

    public void releaseBubble(int screenHight, int duration){
        mAnimatior = new ValueAnimator();
        mAnimatior.setDuration(duration);
        mAnimatior.setFloatValues(screenHight, 0f);
        mAnimatior.setInterpolator(new LinearInterpolator());
        mAnimatior.setTarget(this);
        mAnimatior.addListener(this);
        mAnimatior.addUpdateListener(this);
        mAnimatior.start();
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {


    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setY((Float) animation.getAnimatedValue());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    public void setPop(boolean pop) {
        if (pop){
            mAnimatior.cancel();
        }


    }

    public interface BubbleListener{
        void popFish(Bubble bubble, boolean userTouch);
    }

}
