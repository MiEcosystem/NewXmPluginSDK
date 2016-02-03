package com.xiaomi.smarthome.common.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

import com.xiaomi.common.R;

public class AutoTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private static final String TAG = AutoTextView.class.getSimpleName();

    private boolean mIsDown = false;
    private Context mContext;
    private Handler mHandler;
    private Timer mAnimTimer;
    private String mUpText;
    private String mDownText;
    private float mTextSize;
    private int mTextColor;

    //mInDownAnim,mOutDownAnim分离构成向下翻页的进出动画
    private Rotate3dAnimation mInDownAnim;
    private Rotate3dAnimation mOutDownAnim;

    public AutoTextView(Context context) {
        this(context, null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mHandler = new Handler(mContext.getMainLooper());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoTextView);
        mTextSize = a.getInt(R.styleable.AutoTextView_autoTextSize, 13);
        Log.e(TAG, "mTextSize: " + mTextSize);
        mTextColor = a.getColor(R.styleable.AutoTextView_autoTextColor, 0xFF999999);
        a.recycle();
        setFactory(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mInDownAnim = createAnim(90, 0, h, w, true, false);
        mOutDownAnim = createAnim(0, -90, h, w, false, false);
    }

    private Rotate3dAnimation createAnim(float start, float end, float height, float width, boolean turnIn, boolean turnUp) {
        Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, height, width, turnIn, turnUp);
        rotation.setDuration(1500);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateDecelerateInterpolator());
        return rotation;
    }

    //这里返回的TextView，就是我们看到的View
    @Override
    public View makeView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(mTextSize);
        textView.setTextColor(mTextColor);
        textView.setSingleLine(true);
        return textView;
    }

    public String getUpText() {
        return mUpText;
    }

    public void setUpText(String upText) {
        mUpText = upText;
    }

    public String getDownText() {
        return mDownText;
    }

    public void setDownText(String downText) {
        mDownText = downText;
    }

    private boolean mIsRun = false;

    public void startAnim() {
        if (mIsRun) {
            return;
        }
        mIsRun = true;

        TimerTask animTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    	if(TextUtils.isEmpty(mDownText) && TextUtils.isEmpty(mUpText)){
                    		return;
                    	}
                        setInAnimation(mInDownAnim);
                        setOutAnimation(mOutDownAnim);
                        if (mIsDown) {
                            mIsDown = false;
                            setText(mDownText);
                        } else {
                            mIsDown = true;
                            setText(mUpText);
                        }
                    }
                });
            }
        };
        mAnimTimer = new Timer();
        mAnimTimer.schedule(animTask, 0, 4000);
    }

    public void stopAnim() {
        if (mAnimTimer != null) {
            mAnimTimer.cancel();
            mAnimTimer = null;
        }
        mIsRun = false;
    }

    private static class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(float fromDegrees, float toDegrees, float height, float width, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mCenterY = height / 2;
            mCenterX = width / 2;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
