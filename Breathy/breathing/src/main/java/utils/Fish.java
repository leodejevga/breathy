package utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import breathing.philipps.apps.com.breathing.R;

public class Fish extends android.support.v7.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private FishListener mListerner;
    private ValueAnimator mAnimatior;
    private boolean mPopped;

    public Fish(Context context) {
        super(context);
    }

    public Fish(Context context, int rawidth, int fishNumber) {
        super(context);


        mListerner = (FishListener) context;

        int rawHeight=0;
        if (fishNumber ==1){
            rawHeight = (int )(rawidth* 0.48);
            this.setImageResource(R.drawable.f11);
        }
        if (fishNumber ==2){
            rawHeight = (int )(rawidth* 0.48);
            this.setImageResource(R.drawable.f2);}
        if (fishNumber ==3){
            rawHeight = (int )(rawidth* 0.52);
            this.setImageResource(R.drawable.f3);}
        if (fishNumber ==4){
            rawHeight = (int )(rawidth* 0.62);
            this.setImageResource(R.drawable.f4);}
        if (fishNumber ==5){
            rawHeight = (int )(rawidth* 0.51);
            this.setImageResource(R.drawable.f5);}

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawidth, context);
        ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);

    }

    public void releaseFish(int screenWidth, int duration){
        mAnimatior = new ValueAnimator();
        mAnimatior.setDuration(duration);
        mAnimatior.setFloatValues(0f, screenWidth);
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
        if (!mPopped){
            mListerner.popFish(this, false);
        }

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setX((Float) animation.getAnimatedValue());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mPopped && event.getAction() == event.ACTION_DOWN) {
            mListerner.popFish(this, true);
            mPopped = true;
            mAnimatior.cancel();

        }
        return super.onTouchEvent(event);

    }

    public void setPop(boolean pop) {
        mPopped = pop;
        if (pop){
            mAnimatior.cancel();
        }


    }

    public interface FishListener{
        void popFish(Fish fish, boolean userTouch);
    }

}
