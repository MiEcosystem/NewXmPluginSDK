package com.xiaomi.smarthome.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.FloatRange;

/**
 * Created by weijiantao on 2018/3/19.
 */

public interface XmVideoViewGl {

    public static interface PhotoSnapCallback {
        void onSnap(Bitmap bitmap);
    }

    public interface IDrawBitmapCallback {
        void onBitmapCreated(int index);
    }

    public interface IVideoViewListener {
        void onVideoViewClick();
    }

    /**
     * ApiLevel: 62
     * 配置完各种参数后要调用初始化
     */
    public void initial();

    /**
     * ApiLevel: 62
     * 设置视频视图被点击的回调(单击)
     *
     * @param listener
     */
    public void setVideoViewListener(IVideoViewListener listener);

    /**
     * ApiLevel: 62
     * 截图回调返回一个Bitmap
     *
     * @param callback 结果回调
     */
    public void snap(PhotoSnapCallback callback);

    /**
     * ApiLevel: 62
     * 填充视频流数据数据
     *
     * @param frame 帧数据
     * @see VideoFrame
     */
    public void drawVideoFrame(VideoFrame frame);

    /**
     * ApiLevel: 62
     * 设置视频的背景色
     *
     * @param red
     * @param green
     * @param blue
     */
    public void setBg(float red, float green, float blue);


    /**
     * ApiLevel: 62
     * 设置背景的透明度
     *
     * @param alpha
     */
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha);

    /**
     * ApiLevel: 62
     * 设置畸变纠正保留范围,相对于右上角
     *
     * @param x
     * @param y
     */
    public void setDistort(float x, float y);

    /**
     * ApiLevel: 62
     * 设置一个显示的图像
     *
     * @param bitmap
     */
    public void setFirstBitmap(Bitmap bitmap);

    /**
     * ApiLevel: 62
     * 清除视频缓存
     */
    public void clearQueue();

    /**
     * 释放播放相关，视图销毁的时候必须调用
     */
    public void release();

    /**
     * ApiLevel: 62
     * 是否是硬解码
     *
     * @return
     */
    public boolean isGPUDecoder();

    /**
     * ApiLevel: 62
     * 是否处于基础的比例模式
     *
     * @return
     */
    public boolean isMiniScale();

    /**
     * ApiLevel: 62
     * 设置是否需要填充满整视图
     *
     * @param needMini true 会保持比例不会放大 false会放大
     */
    public void setMiniScale(boolean needMini);

    /**
     * 旋转的角度
     *
     * @param rotation (0,90,180,270)
     */
    public void setRotation(int rotation);

    /**
     * ApiLevel: 62
     * 设置是否是全屏模式
     *
     * @param isFull
     */
    public void setIsFull(boolean isFull);

    /**
     * ApiLevel: 62
     * 添加一个贴图水印
     *
     * @param listener 回调
     * @param bitmap   需要贴图的图片
     * @param rectF    贴图的范围
     * @return
     */
    public int drawBitmap(IDrawBitmapCallback listener, Bitmap bitmap, RectF rectF);

    /**
     * ApiLevel: 62
     * 更新渲染的内容
     *
     * @param index
     * @param bitmap
     * @param rectF
     */
    public void updateBitmap(int index, Bitmap bitmap, RectF rectF);

    /**
     * ApiLevel: 62
     * 移除渲染
     *
     * @param index
     */
    public void removeBitmap(int index);

    /**
     * ApiLevel: 62
     * 设置视图的大小
     *
     * @param width
     * @param height
     * @param isFull 是否需要拉伸填充满视图
     */
    public void setVideoFrameSize(int width, int height, boolean isFull);

    /**
     * ApiLevel: 62
     * 添加Mp4播放的接口，外部不可调用
     *
     * @param mp4Player
     */
    public void addMp4Player(XmMp4Player mp4Player);

    /**
     * ApiLevel: 62
     * 获取Mp4播放的接口
     *
     * @return
     * @see XmMp4Player
     */
    public XmMp4Player getMp4Player();

    /**
     * ApiLevel: 62
     * 设置视图是否可以接收触摸事件
     *
     * @param touch
     */
    public void setTouch(boolean touch);

    /**
     * ApiLevel: 62
     * 获取视图的 Context 对象
     *
     * @return
     */
    public Context getContext();
}
