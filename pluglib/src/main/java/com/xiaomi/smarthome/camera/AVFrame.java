/******************************************************************************
 * *
 * Copyright (c) 2011 by TUTK Co.LTD. All Rights Reserved.                    *
 * *
 * *
 * Class: AVFrame.java                                                    *
 * *
 * Author: joshua ju                                                          *
 * *
 * Date: 2011-05-14                                                           *
 * *
 ******************************************************************************/

package com.xiaomi.smarthome.camera;

public class AVFrame {

    /**
     * ApiLevel: 89
     */
    public static final int AUDIO_CODEC_G711 = 0x8A;
    public static final int AUDIO_CODEC_AAC = 0x88;

    public static final int FRAMEINFO_SIZE = 24;

    // Codec ID
    public static final int MEDIA_CODEC_UNKNOWN = 0x00;
    public static final int MEDIA_CODEC_VIDEO_MPEG4 = 0x4C;
    public static final int MEDIA_CODEC_VIDEO_H263 = 0x4D;
    public static final int MEDIA_CODEC_VIDEO_H264 = 0x4E;
    public static final int MEDIA_CODEC_VIDEO_MJPEG = 0x4F;
    public static final int MEDIA_CODEC_VIDEO_HEVC = 0x50;

    public static final int MEDIA_CODEC_AUDIO_ADPCM = 0x8B;
    public static final int MEDIA_CODEC_AUDIO_PCM = 0x8C;
    public static final int MEDIA_CODEC_AUDIO_SPEEX = 0x8D;
    public static final int MEDIA_CODEC_AUDIO_MP3 = 0x8E;
    public static final int MEDIA_CODEC_AUDIO_G726 = 0x8F;

    // Frame Flag
    public static final int IPC_FRAME_FLAG_PBFRAME = 0x00; // A/V P/B frame.
    public static final int IPC_FRAME_FLAG_IFRAME = 0x01; // A/V I frame.
    public static final int IPC_FRAME_FLAG_MD = 0x02; // For motion detection.
    public static final int IPC_FRAME_FLAG_IO = 0x03; // For AlarmSet IO detection.

    // audio sample rate
    public static final int AUDIO_SAMPLE_8K = 0x00;
    public static final int AUDIO_SAMPLE_11K = 0x01;
    public static final int AUDIO_SAMPLE_12K = 0x02;
    public static final int AUDIO_SAMPLE_16K = 0x03;
    public static final int AUDIO_SAMPLE_22K = 0x04;

    public static final int AUDIO_SAMPLE_24K = 0x05;
    public static final int AUDIO_SAMPLE_32K = 0x06;
    public static final int AUDIO_SAMPLE_44K = 0x07;
    public static final int AUDIO_SAMPLE_48K = 0x08;

    // audio sample data bit
    public static final int AUDIO_DATABITS_8 = 0;
    public static final int AUDIO_DATABITS_16 = 1;

    // audio channel number
    public static final int AUDIO_CHANNEL_MONO = 0;
    public static final int AUDIO_CHANNEL_STERO = 1;

    // -----------------------------------------------------
    public static final byte FRM_STATE_UNKOWN = -1;
    public static final byte FRM_STATE_COMPLETE = 0;
    public static final byte FRM_STATE_INCOMPLETE = 1;
    public static final byte FRM_STATE_LOSED = 2;

    // -----------------------------------------------------
    private short codec_id = 0; // UINT16 codec_id;
    // Media codec type defined in sys_mmdef.h,
    // MEDIA_CODEC_AUDIO_PCMLE16 for audio,
    // MEDIA_CODEC_VIDEO_H264 for video.

    // private int camIndex;
    private byte flags = -1;// Combined with IPC_FRAME_xxx.
    private byte onlineNum = 0;
    private int timestamp = 0; // Timestamp of the frame, in milliseconds.
    private int videoWidth = 0;
    private int videoHeight = 0;
    // -----------------------------

    private long frmNo = -1;
    private byte frmState = 0; // 0:complete; 1:incomplete; 2: losed
    private int frmSize = 0; // Raw data size in bytes.
    private boolean decrypt = false;
    public byte[] frmData = null; // Raw data of the frame.

    public long cam_index;

    public int timestamp_ms;

    public byte usecount;

    public byte isPlayback;

    public byte isShowTime;

    public byte isHaveLogo;

    public AVFrame(byte frmState, byte[] frameInfo, byte[] frmData,
                   int frmSize, boolean isBigOrder) {
        this.codec_id = byteArrayToShort(frameInfo, 0, isBigOrder);
        this.flags = frameInfo[2];
        this.cam_index = frameInfo[3];
        this.onlineNum = frameInfo[4];
        this.usecount = frameInfo[5];
        this.frmNo = byteArrayToShort(frameInfo, 6, isBigOrder);
        this.videoWidth = byteArrayToShort(frameInfo, 8, isBigOrder);
        this.videoHeight = byteArrayToShort(frameInfo, 10, isBigOrder);
        this.timestamp = byteArrayToInt(frameInfo, 12, isBigOrder);
        byte info = frameInfo[16];
        this.isPlayback = (byte) (info & 0x1);
        this.isShowTime = (byte) (info >> 1 & 0x1);
        this.isHaveLogo = (byte) (info >> 2 & 0x1);
        this.frmState = frmState;
        this.frmSize = frmSize;
        this.frmData = frmData;
        if (frameInfo.length > 20) {
            this.timestamp_ms = byteArrayToInt(frameInfo, 20, isBigOrder);
        }
    }

    public static int getSamplerate(byte flags) {
        int nSamplerate = 0;
        switch (flags >>> 2) {
            case AVFrame.AUDIO_SAMPLE_8K:
                nSamplerate = 8000;
                break;
            case AVFrame.AUDIO_SAMPLE_11K:
                nSamplerate = 11025;
                break;

            case AVFrame.AUDIO_SAMPLE_12K:
                nSamplerate = 12000;
                break;

            case AVFrame.AUDIO_SAMPLE_16K:
                nSamplerate = 16000;
                break;

            case AVFrame.AUDIO_SAMPLE_22K:
                nSamplerate = 22050;
                break;

            case AVFrame.AUDIO_SAMPLE_24K:
                nSamplerate = 24000;
                break;

            case AVFrame.AUDIO_SAMPLE_32K:
                nSamplerate = 32000;
                break;

            case AVFrame.AUDIO_SAMPLE_44K:
                nSamplerate = 44100;
                break;

            case AVFrame.AUDIO_SAMPLE_48K:
                nSamplerate = 48000;
                break;
            default:
                nSamplerate = 8000;
        }
        return nSamplerate;
    }

    public boolean isIFrame() {
        return ((flags & IPC_FRAME_FLAG_IFRAME) == IPC_FRAME_FLAG_IFRAME);
    }

    public byte getFlags() {
        return flags;
    }

    public byte getFrmState() {
        return frmState;
    }

    public long getFrmNo() {
        return frmNo;
    }

    public int getFrmSize() {
        return frmSize;
    }

    public void setFrmSize(int size) {
        frmSize = size;
    }

    public byte getOnlineNum() {
        return onlineNum;
    }

    public short getCodecId() {
        return codec_id;
    }

    /**
     * 视频编码方式
     * 1 h264
     * 2 h265
     */
    public int getVideoType() {
        if (codec_id == MEDIA_CODEC_VIDEO_H264) {
            return 1;
        } else {
            return 2;
        }
    }

    public int getTimeStamp() {
        return timestamp;
    }

    public int getTimeStampReal() {
        return timestamp_ms;
    }

    public boolean isPlayback() {
        return isPlayback != 0;
    }

    public boolean isWartTime() {
        return isShowTime != 0;
    }

    public boolean isHaveLogo() {
        return isHaveLogo != 0;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public final short byteArrayToShort(byte byt[], int nBeginPos, boolean bigOrder) {
        if (bigOrder) {
            return (short) ((0xff & byt[nBeginPos]) << 8 | 0xff & byt[nBeginPos + 1]);
        } else {
            return (short) ((0xff & byt[nBeginPos]) | ((0xff & byt[nBeginPos + 1]) << 8));
        }
    }

    public final int byteArrayToInt(byte byt[], int nBeginPos, boolean bigOrder) {
        if (bigOrder) {
            return (0xff & byt[nBeginPos]) << 24 | (0xff & byt[nBeginPos + 1]) << 16 | (0xff & byt[nBeginPos + 2]) << 8 | 0xff & byt[nBeginPos + 3];
        } else {
            return (0xff & byt[nBeginPos]) | (0xff & byt[nBeginPos + 1]) << 8 | (0xff & byt[nBeginPos + 2]) << 16 | (0xff & byt[nBeginPos + 3]) << 24;

        }
    }

}
