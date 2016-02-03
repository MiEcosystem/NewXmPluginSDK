package com.xiaomi.smarthome.common.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
    private static final long TIME_INTERVAL = 300;
    private static final long SPREAD_TIME = 800;
    private static final long REFRESH_TIME = 10;

    private int mWidth = 0;
    private int mHeight = 0;
    private boolean mIsRunning = false;
    private int mStartR = 0;
    private int mEndR = 0;
    private long mStartTime = 0;
    private Paint mPaint;
    private int mColor = 0;

    static final int MSG_START = 1;
    static final int MSG_REFRESH = 2;
    static final int MSG_STOP = 3;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_START:
                    mHandler.sendEmptyMessage(MSG_REFRESH);
                    break;
                case MSG_REFRESH:
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(MSG_REFRESH, REFRESH_TIME);
                    break;
                case MSG_STOP:
                    mIsRunning = false;
                    mHandler.removeMessages(MSG_REFRESH);
                    invalidate();
                    break;
            }
        }
    };

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mIsRunning) {
            return;
        }

        long curTime = System.currentTimeMillis();
        long pastTime = curTime - mStartTime;

        int generatedCircle = (int) Math.floor(pastTime / (double) TIME_INTERVAL) + 1;
        double spreadSpeed = (mEndR - mStartR) / (double) SPREAD_TIME;

        for (int i = generatedCircle; i > 0; i--) {
            int move = (int) ((pastTime - TIME_INTERVAL * (i - 1)) * spreadSpeed);
            if (move >= (mEndR - mStartR)) {
                break;
            }
            float percent = move / (float) (mEndR - mStartR);
            int color = ((((int) ((1 - percent) * 255)) & 0xFF) << 24) | mColor;
            int radius = move + mStartR;
            mPaint.setColor(color);
            canvas.drawCircle(mWidth / 2.0f, mHeight / 2.0f, radius, mPaint);
        }
    }

    public void start(int startR, int endR, int color) {
        if (mIsRunning) {
            return;
        }

        mStartR = startR;
        mEndR = endR;
        mColor = color;

        mStartTime = System.currentTimeMillis();
        mHandler.sendEmptyMessage(MSG_START);
        mIsRunning = true;
    }

    public void stop() {
        if (!mIsRunning) {
            return;
        }
        mHandler.sendEmptyMessage(MSG_STOP);
    }
}