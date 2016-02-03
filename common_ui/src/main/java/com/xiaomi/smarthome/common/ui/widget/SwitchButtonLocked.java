package com.xiaomi.smarthome.common.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.xiaomi.common.R;

/**
 * Created by novia on 12/25/14.
 */
public class SwitchButtonLocked extends SwitchButton {
    public SwitchButtonLocked(Context context) {
        super(context);
        init();
    }

    public SwitchButtonLocked(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwitchButtonLocked(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mFrame = getResources().getDrawable(R.drawable.lock_switch_bg);
        mSliderOn = getResources().getDrawable(R.drawable.lock_switch_point_on_normal);
        mSliderOff = getResources().getDrawable(R.drawable.lock_switch_point_off_normal);
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
}
