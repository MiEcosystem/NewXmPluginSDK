package com.xiaomi.smarthome.camera;

import android.media.MediaPlayer;

/**
 * Created by weijiantao on 2018/3/19.
 */

public interface XmMp4Player {

    /**
     * 主要用来回调视图是否初始化完成
     */
    public static interface RenderCallback {
        public void onInitialed();
    }

    /**
     * 如果播放Mp4时候选择了使用FFmpeg播放需要用到这个回调
     */
    public interface AudioListener {
        public void onAudioData(byte[] data, long timestamp, int type);

        /**
         * 播放结束
         *
         * @param errorCode 0代表正常结束 其他值代表播放错误
         */
        public void onFinish(int errorCode);
    }

    /**
     * ApiLevel: 62
     * 软解的时候需要设置音频回调接口
     *
     * @param listener 返回解析出来的音频数据
     */
    public void setAudioListener(AudioListener listener);

    /**
     * ApiLevel: 62
     * 是否初始化完成的回调,使用该接口的内容需要RenderCallback回调后
     *
     * @param callback
     */
    public void setRenderCallback(RenderCallback callback);

    /**
     * ApiLevel: 62
     * @param preparedListener
     * @See android.media.MediaPlayer
     */
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener preparedListener);

    /**
     * ApiLevel: 62
     * @param listener
     * @See android.media.MediaPlayer
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener);

    /**
     * @param listener
     * @See android.media.MediaPlayer
     */
    public void setOnErrorListener(MediaPlayer.OnErrorListener listener);

    /**
     * ApiLevel: 62
     * 更换播放Mp4资源文件
     *
     * @param path 新Mp4的路径
     */
    public abstract void changeSource(final String path);

    /**
     * ApiLevel: 62
     * 开始播放Mp4文件
     *
     * @param path Mp4的路径
     */
    public abstract void openVideoPlayer(String path);

    /**
     * ApiLevel: 62
     * 当前播放器是否正在播放文件
     *
     * @return
     */
    public abstract boolean isPlaying();

    /**
     * ApiLevel: 62
     * 暂停播放
     */
    public abstract void pause();

    /**
     * ApiLevel: 62
     * 跳转到指定时间
     *
     * @param pos position in milliseconds
     */
    public abstract void seekTo(int pos);

    /**
     * ApiLevel: 62
     * 获取当前播放位置
     *
     * @return the current position in milliseconds
     */
    public abstract int getCurrentPosition();

    /**
     * ApiLevel: 62
     * 获取Mp4文件的时长
     *
     * @return milliseconds
     */
    public abstract int getDuration();

    /**
     * ApiLevel: 62
     * 设置音量是否需要声音
     *
     * @param volume 0无声音 1播放声音
     */
    public abstract void setVolume(int volume);
}
