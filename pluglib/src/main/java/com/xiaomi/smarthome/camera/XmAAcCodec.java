package com.xiaomi.smarthome.camera;

public interface XmAAcCodec {


    /**
     * ApiLevel: 89
     * 解码AAC音频为PCM格式
     *
     * @return PCM音频格式文件
     */
    int decode(byte[] inData, int inDataPos, int inDataSize, byte[] outData,
               int outDataSize);

    /**
     * ApiLevel: 89
     * 对PCM音频文件进行编码
     *
     * @return 编码后的AAC音频文件
     */
    byte[] encode(byte[] inData, int inDataPos, int inDataSize);

    /**
     * ApiLevel: 89
     * 获取一帧的大小含有多少个音频数据
     *
     * @return
     */
    int getOneFrameSamplesCount();

    /**
     * ApiLevel: 89
     * 使用之后必须释放
     */
    void release();
}
