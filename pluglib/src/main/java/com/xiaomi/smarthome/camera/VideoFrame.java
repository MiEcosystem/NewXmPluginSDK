
package com.xiaomi.smarthome.camera;


public class VideoFrame {
    //0不畸变纠正 1 畸变纠正无时间水印 2畸变纠正有时间水印
    public static final int DISTORT_NONE = 0;
    public static final int DISTORT_ALL = 1;
    public static final int DISTORT_PART = 2;
    public static final int VIDEO_H264 = 1;
    public static final int VIDEO_H265 = 2;
    public byte[] data;
    public long num;
    public int size;
    public int width;
    public int height;
    public long timeStamp;//毫秒
    public boolean isIFrame;
    public boolean isReal = false;//是否实时播放
    public int videoType;
    public int distrot = 0;


    /**
     * ApiLevel: 64
     * 发送视频流 只支持H264
     *
     * @param data        数据
     * @param frameNumber 帧序号
     * @param frameSize   大小
     * @param width       宽
     * @param height      高
     * @param timestamp   时间戳
     * @param isIFrame    是否是I帧
     */
    public VideoFrame(byte[] data,
                      long frameNumber,
                      int frameSize,
                      int width,
                      int height,
                      long timestamp,
                      boolean isIFrame) {
        this.data = data;
        this.num = frameNumber;
        this.size = frameSize;
        this.width = width;
        this.height = height;
        this.timeStamp = timestamp;
        this.isIFrame = isIFrame;
        this.videoType = 1;
    }

    /**
     * ApiLevel: 64
     * 发送视频流 支持视频流编码类型 h264 h265
     *
     * @param data        数据
     * @param frameNumber 帧序号
     * @param frameSize   大小
     * @param width       宽
     * @param height      高
     * @param timestamp   时间戳
     * @param videoType   视频流编码1:h264 2:h265
     * @param isIFrame    是否是I帧
     */
    public VideoFrame(byte[] data,
                      long frameNumber,
                      int frameSize,
                      int width,
                      int height,
                      long timestamp,
                      int videoType,
                      boolean isIFrame) {
        this.data = data;
        this.num = frameNumber;
        this.size = frameSize;
        this.width = width;
        this.height = height;
        this.timeStamp = timestamp;
        this.isIFrame = isIFrame;
        if (videoType != VIDEO_H265) {
            this.videoType = VIDEO_H264;
        } else {
            this.videoType = VIDEO_H265;
        }
    }

    /**
     * ApiLevel: 64
     * 发送视频流 主要用于需要畸变的
     *
     * @param data        数据
     * @param frameNumber 帧序号
     * @param frameSize   大小
     * @param width       宽
     * @param height      高
     * @param timestamp   时间戳
     * @param videoType   视频流编码1:h264 2:h265
     * @param isIFrame    是否是I帧
     * @param distrot     畸变的状态
     * @param isReal      是否是实时流
     */
    public VideoFrame(byte[] data,
                      long frameNumber, int frameSize,
                      int width, int height,
                      long timestamp, int videoType, boolean isIFrame, int distrot, boolean isReal) {
        this.data = data;
        this.num = frameNumber;
        this.size = frameSize;
        this.width = width;
        this.height = height;
        this.timeStamp = timestamp;
        this.isIFrame = isIFrame;
        if (videoType != VIDEO_H265) {
            this.videoType = VIDEO_H264;
        } else {
            this.videoType = VIDEO_H265;
        }
        this.distrot = distrot;
        this.isReal = isReal;
    }

}
