package com.xiaomi.smarthome.camera;

/**
 * Created by weijiantao on 2018/3/19.
 */

public interface XmMp4Record {
    public interface IRecordListener {

        public void onSuccess(String file);

        public void onFailed(int error, String info);
    }

    /**
     * ApiLevel: 62
     * 开始录制Mp4文件 目前声音支持AAC 视频支持h265 h264
     *
     * @param fileName    文件全路径
     * @param videoType   视频流类型 参考 VideoFrame
     * @param width       视频宽度  不可更改
     * @param height      视频高度 不可更改
     * @param audioSample 声音的频率 默认8000
     */
    public void startRecord(String fileName, int videoType, int width, int height, int audioSample);

    /**
     * ApiLevel: 62
     * 写入视频流文件
     *
     * @param data     数据
     * @param length   数据长度
     * @param isIFrame 是否是I帧
     * @param time     时间戳
     */
    public void writeVideoData(byte[] data, int length, boolean isIFrame, int time);

    /**
     * ApiLevel: 62
     * 写入音频数据
     *
     * @param data   数据
     * @param length 数据长度
     */
    public void writeAAcData(byte[] data, int length);

    /**
     * ApiLevel: 62
     * 结束录制
     *
     * @param listener 结果回调是否录制成功
     */
    public void stopRecord(final IRecordListener listener);
}
