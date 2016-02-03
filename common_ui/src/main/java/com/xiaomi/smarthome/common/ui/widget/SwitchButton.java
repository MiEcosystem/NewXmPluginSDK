
package com.xiaomi.smarthome.common.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.CheckBox;

import com.xiaomi.common.R;
import com.xiaomi.smarthome.common.plug.utils.ApiHelper;

public class SwitchButton extends CheckBox {

    protected static final int FULL_ALPHA = 255;

    protected static final int ANIMATION_DURATION = 180;

    protected static final int SCALE_ANIMATION_DELAY = 100;

    protected Drawable mFrame;

    protected Drawable mSliderOn;

    protected int mSliderOnAlpha;

    protected Drawable mSliderOff;

    protected Bitmap mBarOff;

    protected Paint mBarOffPaint;

    protected Bitmap mBarOn;

    protected Paint mBarOnPaint;

    protected Bitmap mMask;

    protected int mWidth;

    protected int mHeight;

    protected int mSliderWidth;

    protected int mSliderPositionStart;

    protected int mSliderPositionEnd;

    protected int mSliderOffset;

    protected int mLastX;

    protected int mOriginalTouchPointX;

    protected boolean mTracking;

    protected boolean mSliderMoved;

    protected int mTapThreshold;

    protected OnCheckedChangeListener mOnPerformCheckedChangeListener;

    protected Rect mTmpRect = new Rect();

    protected Animator mAnimator;

    protected Animator.AnimatorListener mAnimatorListener;

    /**
     * 是否处理Touch事件, add by chenhao
     */
    protected boolean mOnTouchEventEnable = true;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (ApiHelper.HAS_ANDROID_ANIMATION) {
            mAnimatorListener = new AnimatorListenerAdapter() {
                private boolean mCanceled;

                @Override
                public void onAnimationStart(Animator animation) {
                    mCanceled = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCanceled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mCanceled) {
                        return;
                    }
                    mAnimator = null;
                    final boolean isChecked = mSliderOffset >= mSliderPositionEnd;
                    if (isChecked != isChecked()) {
                        setChecked(isChecked);
                        if (mOnPerformCheckedChangeListener != null) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mOnPerformCheckedChangeListener != null) {
                                        mOnPerformCheckedChangeListener.onCheckedChanged(SwitchButton.this, isChecked);
                                    }
                                }
                            });
                        }
                    }
                }
            };

        }
        setDrawingCacheEnabled(false);
        mTapThreshold = ViewConfiguration.get(context).getScaledTouchSlop() / 2;

        mFrame = getResources().getDrawable(R.drawable.sliding_btn_frame_light);
        mSliderOn = getResources().getDrawable(R.drawable.sliding_btn_slider_on_light);
        mSliderOff = getResources().getDrawable(R.drawable.sliding_btn_slider_off_light);
        setBackgroundResource(R.drawable.sliding_btn_bg_light);

        mWidth = mFrame.getIntrinsicWidth();
        mHeight = mFrame.getIntrinsicHeight();

        mSliderWidth = Math.min(mWidth, mSliderOn.getIntrinsicWidth());
        mSliderPositionStart = 0;
        mSliderPositionEnd = mWidth - mSliderWidth;
        mSliderOffset = mSliderPositionStart;

        BitmapDrawable slideOff = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.sliding_btn_bar_off_light));
        mBarOff = Bitmap.createScaledBitmap(slideOff.getBitmap(),
                mWidth * 2 - mSliderWidth,
                mHeight,
                true);

        BitmapDrawable slidingOn = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.sliding_btn_bar_on_light));
        mBarOn = Bitmap.createScaledBitmap(slidingOn.getBitmap(),
                mWidth * 2 - mSliderWidth,
                mHeight,
                true);
        mFrame.setBounds(0, 0, mWidth, mHeight);

        Drawable maskDrawable = getResources().getDrawable(R.drawable.sliding_btn_mask_light);
        maskDrawable.setBounds(0, 0, mWidth, mHeight);
        mMask = convertToAlphaMask(maskDrawable);

        mBarOffPaint = new Paint();
        mBarOnPaint = new Paint();
        mBarOnPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mBarOffPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    /*
     *@hide
     */
    public void setOnPerformCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnPerformCheckedChangeListener = listener;
    }

    protected Bitmap convertToAlphaMask(Drawable drawable) {
        final Rect rect = drawable.getBounds();
        Bitmap mask = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(mask);
        drawable.draw(canvas);
        return mask;
    }

    @Override
    public void setChecked(boolean checked) {
        boolean oldState = isChecked();

        if (oldState != checked) {
            super.setChecked(checked);
            mSliderOffset = checked ? mSliderPositionEnd : mSliderPositionStart;
            mBarOnPaint.setAlpha(checked ? FULL_ALPHA : 0);
            mBarOffPaint.setAlpha(!checked ? FULL_ALPHA : 0);
            mSliderOnAlpha = checked ? FULL_ALPHA : 0;
            invalidate();
        }
    }

    @Override
    public void setButtonDrawable(Drawable d) {
        // do nothing
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mSliderOn.setState(getDrawableState());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || !mOnTouchEventEnable) {
            return false;
        }

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        final Rect sliderFrame = mTmpRect;
        sliderFrame.set(mSliderOffset, 0, mSliderOffset + mSliderWidth, mHeight);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (sliderFrame.contains(x, y)) {
                    mTracking = true;
                    setPressed(true);
                } else {
                    mTracking = false;
                }
                mLastX = x;
                mOriginalTouchPointX = x;
                mSliderMoved = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTracking) {
                    moveSlider(x - mLastX);
                    mLastX = x;
                    if (Math.abs(x - mOriginalTouchPointX) >= mTapThreshold) {
                        mSliderMoved = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mTracking) {
                    if (!mSliderMoved) {
                        animateToggle();
                    } else {
                        animateToState(mSliderOffset >= mSliderPositionEnd / 2);
                    }
                } else {
                    animateToggle();
                }
                mTracking = false;
                mSliderMoved = false;
                setPressed(false);
                break;

            case MotionEvent.ACTION_CANCEL:
                mTracking = false;
                mSliderMoved = false;
                setPressed(false);
                animateToState(mSliderOffset >= mSliderPositionEnd / 2);
                break;
        }

        return true;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        invalidate();
    }

    protected void animateToggle() {
        animateToState(!isChecked());
    }

    @SuppressLint("NewApi")
    protected void animateToState(final boolean isChecked) {
        if (ApiHelper.HAS_ANDROID_ANIMATION) {
            if (mAnimator != null) {
                mAnimator.cancel();
                mAnimator = null;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator slidingAnimator = ObjectAnimator
                .ofInt(this, "SliderOffset", isChecked ? mSliderPositionEnd : mSliderPositionStart);
            ObjectAnimator scaleAnimator = ObjectAnimator.ofInt(this, "SliderOnAlpha", isChecked ? FULL_ALPHA : 0);
            scaleAnimator.setDuration(ANIMATION_DURATION);
            slidingAnimator.setDuration(ANIMATION_DURATION);
            animatorSet.play(scaleAnimator).after(slidingAnimator).after(SCALE_ANIMATION_DELAY);
            mAnimator = animatorSet;
            mAnimator.addListener(mAnimatorListener);
            mAnimator.start();
        }
        else {
            if (isChecked != isChecked()) {
                setChecked(isChecked);
                if (mOnPerformCheckedChangeListener != null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnPerformCheckedChangeListener != null) {
                                mOnPerformCheckedChangeListener.onCheckedChanged(SwitchButton.this, isChecked);
                            }
                        }
                    });
                }
            }
        }
    }

    protected void moveSlider(int offsetX) {
        // check the edge condition
        mSliderOffset += offsetX;
        if (mSliderOffset < mSliderPositionStart) {
            mSliderOffset = mSliderPositionStart;
        } else if (mSliderOffset > mSliderPositionEnd) {
            mSliderOffset = mSliderPositionEnd;
        }
        setSliderOffset(mSliderOffset);
    }

    /**
     * @hide
     */
    public int getSliderOffset() {
        return mSliderOffset;
    }

    /**
     * @hide
     */
    public void setSliderOffset(int sliderOffset) {
        mSliderOffset = sliderOffset;
        invalidate();
    }

    /**
     * @hide
     */
    public int getSliderOnAlpha() {
        return mSliderOnAlpha;
    }

    /**
     * @hide
     */
    public void setSliderOnAlpha(int sliderOnAlpha) {
        mSliderOnAlpha = sliderOnAlpha;
        invalidate();
    }

    /**
     * 是否处理Touch事件, add by chenhao
     *
     * @param enable
     */
    public void setOnTouchEnable(boolean enable) {
        mOnTouchEventEnable = enable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int alpha = isEnabled() ? FULL_ALPHA : FULL_ALPHA / 2;
        canvas.saveLayerAlpha(0, 0, mMask.getWidth(), mMask.getHeight(), alpha, Canvas.ALL_SAVE_FLAG);
        // draw mask
        canvas.drawBitmap(mMask, 0, 0, null);
        // draw the background (on or off)
        drawBar(canvas);
        // draw the frame
        mFrame.draw(canvas);
        // draw the slider
        if (mSliderOnAlpha <= FULL_ALPHA) {
            mSliderOff.setBounds(mSliderOffset, 0, mSliderWidth + mSliderOffset, mHeight);
            mSliderOff.draw(canvas);
        }

        mSliderOn.setAlpha(mSliderOnAlpha);
        mSliderOn.setBounds(mSliderOffset, 0, mSliderWidth + mSliderOffset, mHeight);
        mSliderOn.draw(canvas);
        canvas.restore();
    }

    protected void drawBar(Canvas canvas) {
        if (mBarOnPaint.getAlpha() != 0) {
            canvas.drawBitmap(mBarOn, 0, 0, mBarOnPaint);
        }

        if (mBarOffPaint.getAlpha() != 0) {
            canvas.drawBitmap(mBarOff, 0, 0, mBarOffPaint);
        }
    }
}
