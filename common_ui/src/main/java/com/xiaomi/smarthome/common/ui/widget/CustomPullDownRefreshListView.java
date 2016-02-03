package com.xiaomi.smarthome.common.ui.widget;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaomi.common.R;

public class CustomPullDownRefreshListView extends XMBaseListView {

    private static final int MSG_BUNCING_BACK = 0;

    private final int FRAME_DURATION = 1000 / 60;

    private float mMaximumVelocity;

    private final float MAXIMUM_VELOCITY = 1.5f;

    private int mTriggerRefreshThreshold;

    private boolean isDown = false;

    private float mStartY = 0;

    private int mCurOffsetY = 0;

    private boolean mIsRefreshing = false;

    private Animation mAnimRotate, mAnimRotateBack;

    private BuncingHandler mHandler = new BuncingHandler(this);

    private boolean mCanRefresh = false;

    private OnRefreshListener mRefreshListener;

    private OnInterceptListener mInterceptListener;

    private View mHeader = null;

    private View mContainer = null;

    private ImageView mBkgImgView = null;

    private int mOriHeight = 0, mMaxHeaderHeight = Integer.MAX_VALUE;

    private boolean mPullDownEnabled = true;

    private boolean mCanPullDown = true;

    private boolean mShowRefreshProgress = true;

    private CharSequence mPullDownToRefreshText, mReleaseText;

    public CustomPullDownRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomPullDownRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPullDownRefreshListView(Context context) {
        super(context);
        init();
    }



    public void setRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public void setInterceptListener(OnInterceptListener listener) {
        mInterceptListener = listener;
    }

    public void setShowRefreshProgress(boolean show) {
        mShowRefreshProgress = show;
    }

    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    public void doRefresh() {
        if (!mIsRefreshing && (mRefreshListener != null)) {
            preRefresh();
            mRefreshListener.startRefresh();
        }
    }

    private void preRefresh() {
        mIsRefreshing = true;

        TextView extHeaderTxt = (TextView) findViewById(R.id.pull_header_txt);
        extHeaderTxt.setText(R.string.refreshing);
        if (mShowRefreshProgress) {
            View prog = findViewById(R.id.pull_header_prog);
            prog.setVisibility(View.VISIBLE);
        }
        View pullIndc = findViewById(R.id.pull_header_indc);
        pullIndc.clearAnimation();
        pullIndc.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = mContainer.getLayoutParams();
        if (mCurOffsetY == 0) {
            mCurOffsetY = getContext().getResources().getDimensionPixelSize(R.dimen.pull_down_header_height);
        }
        params.height = mCurOffsetY + mOriHeight;
        mContainer.setLayoutParams(params);
    }


    public void setProgressDrawable(Drawable drawable){
        ProgressBar prog = (ProgressBar)findViewById(R.id.pull_header_prog);
        prog.setIndeterminateDrawable(drawable);
    }

    public void postRefresh() {
        mIsRefreshing = false;

        TextView extHeaderTxt = (TextView) findViewById(R.id.pull_header_txt);
        extHeaderTxt.setText(mPullDownToRefreshText);
        View prog = findViewById(R.id.pull_header_prog);
        prog.setVisibility(View.GONE);
        View pullIndc = findViewById(R.id.pull_header_indc);
        pullIndc.clearAnimation();
        pullIndc.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams params = mContainer.getLayoutParams();
        params.height = mOriHeight + mCurOffsetY;
        mContainer.setLayoutParams(params);

        mHandler.sendEmptyMessageDelayed(MSG_BUNCING_BACK, FRAME_DURATION);
    }

    public void setHeaderBackground(Drawable drawable) {
        if (mBkgImgView != null) {
            mBkgImgView.setImageDrawable(drawable);
            int width = drawable.getMinimumWidth();
            int height = drawable.getMinimumHeight();
            if (width <= 0) {
                return;
            }
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int viewHeight = (height * dm.widthPixels) / width;
            ViewGroup.LayoutParams lp = mBkgImgView.getLayoutParams();
            lp.height = viewHeight;
            mMaxHeaderHeight = viewHeight;
            mBkgImgView.setLayoutParams(lp);
        }
    }

    public void setOriHeight(int height) {
        mOriHeight = height;
        ViewGroup.LayoutParams params = findViewById(R.id.pull_header).getLayoutParams();
        params.height = mOriHeight;
        View emptyView = mHeader.findViewById(R.id.empty_view);
        params = emptyView.getLayoutParams();
        params.height = mOriHeight;
    }

    public void setPullDownEnabled(boolean enabled) {
        mPullDownEnabled = enabled;
    }

    public void setPullDownHeaderVisibility(int visibility) {
        View pullHeader = findViewById(R.id.pull_header_container);
        pullHeader.setVisibility(visibility);
    }

    public void setPullDownTextColor(int color) {
        TextView extHeaderTxt = (TextView) findViewById(R.id.pull_header_txt);
        extHeaderTxt.setTextColor(color);
    }

    public void setPullDownTextSize(int size) {
        TextView extHeaderTxt = (TextView) findViewById(R.id.pull_header_txt);
        extHeaderTxt.setTextSize(size);
    }

    public void setPullDownText(CharSequence norTxt, CharSequence pullTxt) {
        mPullDownToRefreshText = norTxt;
        TextView extHeaderTxt = (TextView) findViewById(R.id.pull_header_txt);
        extHeaderTxt.setText(mPullDownToRefreshText);
        mReleaseText = pullTxt;
    }

    public void setPullDownTextColorLine2(int color) {
        TextView extHeaderTxtLine2 = (TextView) findViewById(R.id.pull_header_txt_line2);
        extHeaderTxtLine2.setTextColor(color);
    }

    public void setPullDownLine2Text(CharSequence text) {
        TextView extHeaderTxtLine2 = (TextView) findViewById(R.id.pull_header_txt_line2);
        if (!TextUtils.isEmpty(text)) {
            extHeaderTxtLine2.setVisibility(View.VISIBLE);
            extHeaderTxtLine2.setText(text);
        } else {
            extHeaderTxtLine2.setVisibility(View.GONE);
        }
    }

    public void setIndicatorDrawable(Drawable d) {
        ImageView indicator = (ImageView) findViewById(R.id.pull_header_indc);
        indicator.setImageDrawable(d);
    }

    public void setMaxPullDownFromRes(int resId) {
        mMaxHeaderHeight = getResources().getDimensionPixelSize(resId);
    }

    @SuppressLint("NewApi")
    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        if (isInEditMode()) {
            return;
        }
        final float density = getResources().getDisplayMetrics().density;
        mMaximumVelocity = MAXIMUM_VELOCITY * density + 0.5f;
        mPullDownToRefreshText = getContext().getString(R.string.pull_down_refresh);
        mReleaseText = getContext().getString(R.string.release_to_refresh);
        mTriggerRefreshThreshold = getResources().getDimensionPixelSize(R.dimen.pull_down_refresh_threshold);
        mHeader = LayoutInflater.from(getContext()).inflate(R.layout.pull_header, null);
        mContainer = mHeader.findViewById(R.id.pull_header);
        mBkgImgView = (ImageView) mHeader.findViewById(R.id.img_bkg);
        addHeaderView(mHeader);
    }

    public void setCanPullDown(boolean value) {
        mCanPullDown = value;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mCanPullDown) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mPullDownEnabled) {
                    mCanRefresh = false;
                    // 当处于列表最顶端时，记录下当前位置
                    if (!mIsRefreshing && (getFirstVisiblePosition() <= 0)) {
                        isDown = true;
                        mStartY = ev.getY();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDown) {
                    // 如果已经处于下拉状态，记录下当前的偏移
                    TextView extHeaderTxt = (TextView) findViewById(R.id.pull_header_txt);
                    ImageView indicator = (ImageView) findViewById(R.id.pull_header_indc);

                    float curY = ev.getY();
                    if ((curY - mStartY) > 10) { // 点击时有可能会滑动一点，选择大于10来防止把这种操作误判成滑动操作
                        ViewGroup.LayoutParams params = mContainer.getLayoutParams();
                        // 偏移位置以手在Y轴滑动的距离的一半为准
                        mCurOffsetY = (int) ((curY - mStartY) / 2);
                        if ((mCurOffsetY + mOriHeight) < mMaxHeaderHeight) {
                            params.height = mCurOffsetY + mOriHeight;
                            mContainer.setLayoutParams(params);
                            if (mCurOffsetY >= mTriggerRefreshThreshold) {
                                if (!mCanRefresh) {
                                    extHeaderTxt.setText(mReleaseText);
                                    if (mAnimRotate == null) {
                                        mAnimRotate = AnimationUtils.loadAnimation(getContext(), R.anim.v5_rotate_180);
                                        mAnimRotate.setFillAfter(true);
                                    }
                                    indicator.startAnimation(mAnimRotate);
                                    mCanRefresh = true;
                                }
                            } else {
                                if (mCanRefresh) {
                                    extHeaderTxt.setText(mPullDownToRefreshText);
                                    if (mAnimRotateBack == null) {
                                        mAnimRotateBack = AnimationUtils
                                                .loadAnimation(getContext(), R.anim.v5_rotate_back_180);
                                        mAnimRotateBack.setFillAfter(true);
                                    }
                                    indicator.startAnimation(mAnimRotateBack);
                                    mCanRefresh = false;
                                }
                            }
                        } else {
                            mCurOffsetY = Math.max(0, mMaxHeaderHeight - mOriHeight);
                        }
                        // 进入下拉刷新状态，需要disable掉listview的上下滚动，因此需要吃掉move消息
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.dispatchTouchEvent(ev);
                        return true;
                    }
                } else if (mPullDownEnabled && !isDown && !mIsRefreshing && (getFirstVisiblePosition() <= 0)
                        && (mHeader.getTop() >= 0)) {
                    // 当处于列表最顶端时，记录下当前位置
                    isDown = true;
                    mStartY = ev.getY();
                    mCanRefresh = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDown) {
                    if (mInterceptListener != null && mInterceptListener.needInterceptAnimation()) {
                        if (mCanRefresh) {
                            mInterceptListener.onInterceptAnimation();
                        } else {
                            mHandler.sendEmptyMessage(MSG_BUNCING_BACK);
                            isDown = false;
                        }
                    } else {
                        // 手指松开，如果滑动距离超过阈值，则触发刷新
                        mHandler.sendEmptyMessage(MSG_BUNCING_BACK);
                        if (mCanRefresh) {
                            doRefresh();
                        }
                        isDown = false;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                onViewHide();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    // 如果正处于下拉等待松手的状态时，界面被切换到后台，调用此方法。
    public void onViewHide() {
        if (isDown) {
            // 手指松开，如果滑动距离超过阈值，则触发刷新
            mHandler.sendEmptyMessage(MSG_BUNCING_BACK);
        }
        isDown = false;
    }
    
    public boolean getIsDown(){
    	if( !mIsRefreshing && (getFirstVisiblePosition() <= 0)
                        && (mHeader.getTop() >= 0))
    		return true;
    	else if(mIsRefreshing)
    		return true;
    	else return false;
    }

    public void resume() {
        onViewHide();
        doRefresh();
    }

    private void doAnimation() {
        final ViewGroup.LayoutParams params = mContainer.getLayoutParams();
        if (mCurOffsetY >= 0) {
            // 动画分为两部分，一部分是超过刷新距离后，回弹速度为2.0f
            // 距离小于刷新距离后，回弹速度为0.5f
            float velocity = mIsRefreshing ? mMaximumVelocity : mMaximumVelocity / 2;
            mCurOffsetY -= velocity * FRAME_DURATION;
            if (mIsRefreshing && mCurOffsetY <= mTriggerRefreshThreshold) {
                mCurOffsetY = mTriggerRefreshThreshold;
                params.height = mCurOffsetY + mOriHeight;
                mContainer.setLayoutParams(params);
                mHandler.removeMessages(MSG_BUNCING_BACK);
                return;
            }

            if (mCurOffsetY <= 0) {
                mCurOffsetY = 0;
                params.height = mCurOffsetY + mOriHeight;
                mContainer.setLayoutParams(params);
                mHandler.removeMessages(MSG_BUNCING_BACK);
                return;
            }
            params.height = mCurOffsetY + mOriHeight;
            mContainer.setLayoutParams(params);
        }
        // 处于弹回状态，发消息调度下一次弹回
        mHandler.sendEmptyMessageDelayed(MSG_BUNCING_BACK, FRAME_DURATION);
    }

    private static final class BuncingHandler extends Handler {
        private WeakReference<CustomPullDownRefreshListView> mReference;

        public BuncingHandler(CustomPullDownRefreshListView pullDownRefreshListView) {
            mReference = new WeakReference<CustomPullDownRefreshListView>(pullDownRefreshListView);
        }

        @Override
        public void handleMessage(Message msg) {
            CustomPullDownRefreshListView pullDownRefreshListView = mReference.get();
            if (pullDownRefreshListView == null) {
                return;
            }

            if (msg.what == MSG_BUNCING_BACK) {
                pullDownRefreshListView.doAnimation();
            }
            super.handleMessage(msg);
        }
    }

    public static interface OnRefreshListener {
        public void startRefresh();
    }

    public static interface OnInterceptListener {
        public void onInterceptAnimation();

        public boolean needInterceptAnimation();
    }
}
