FastVideo 使用说明
ApiLevel 62

目前需要使用SmartHome_camera_debug.apk 进行调试

一、视频播放
	接口 XmPluginHostApi
	创建一个播放实时流的视图 返回操作视图的接口
 
     /**
     * 
     * 创建一个播放视频流的播放视图
     *
     * @param context
     * @param original 父容器
     * @param useHard  是否优先使用硬解码
     * @param type     视频流编码类型 1==h264 2==h265
     * @return
     * @see com.xiaomi.smarthome.camera.VideoFrame
     */
	public abstract XmVideoViewGl createVideoView(Context context, FrameLayout original, boolean useHard, int type);
	
	使用例子：		
    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
    FrameLayout layout = new FrameLayout(activity());
    mVideoView = XmPluginHostApi.instance().createVideoView(activity(), layout, true, VideoFrame.VIDEO_H264);
	layout就是需要显示视频的视图（可以是New的也可以是在XML布局中的）
 
	创建一个播放Mp4的视图 返回操作视图的接口
    /**
     *
     * 创建一个用来播放本地Mp4的视图 返回操作视图的接口
     *	Mp4播放的控制 XmVideoViewGl getMp4Player() 方法会返回一个Mp4控制的接口
     * @param context
     * @param original 父容器
     * @param useHard  true MediaPlayer播放mp4，false 使用ffmpeg 播放mp4
     * @return
     */
   ·public abstract XmVideoViewGl createMp4View(Context context, FrameLayout original, boolean useHard);

    使用例子：
	FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    FrameLayout layout = new FrameLayout(activity());
	XmVideoViewGl xmVideoViewGl = XmPluginHostApi.instance().createMp4View(activity(), layout, true);    
	XmMp4Player xmMp4Player = xmVideoViewGl.getMp4Player();
	xmVideoViewGl 用来操作视频播放相关功能
	xmMp4Player 用来控制Mp4相关功能
	layout 就是要显示的视图 需要是FrameLayout
	如果手机本身支持Mp4中视频流的播放，useHard填充true  手机本身不支持Mp4中视频流的播放(低版本手机不支持h265)，useHard填充false
	
	
二、畸变纠正 
	如果设备无法进行畸变纠正SDK提供基础的畸变纠正功能,在提供VideoFrame 时候该构造函数需要传递插件需要的畸变类型
	 
	//0不畸变纠正 1 畸变纠正无时间水印 2畸变纠正有时间水印
    public static final int DISTORT_NONE = 0;
    public static final int DISTORT_ALL = 1;
    public static final int DISTORT_PART = 2;
	     /**
     * 发送视频流
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
					  }

   /**
     * 如果畸变纠正有范围不需要纠正 （用于纠正后实时流时间戳显示问题）
     *
     * @param x 相对于左上角X轴
     * @param y 相对于左上角Y轴
     */
    public void setDistort(float x, float y) {
        mDistortX = x;
        mDistortY = y;
    }					  


三、Mp4的合成
	1.调用 XmPluginHostApi.createMp4Record() 会返回一个XmMp4Record 接口用来操作Mp4的合成各个接口

	2.XmMp4Record  合成时候视频流支持（h264/h265） 音频支持（aac）如果设备传递过来的是其他音频格式需要插件自己转换成aac初始化时候需要提供 mp4合成的参数
    /**
     * 
     * @param fileName 合成Mp4的文件全路径
     * @param videoType 视频流类型(h264-h265) @see H264Decoder
     * @param width 视频流宽度
     * @param height 视频流高度
     * @param audioSample 音频采样率
     */
	public void startRecord(String fileName, int videoType, int width, int height, int audioSample);
     /**
     * 
     * @param data 写入的视频流数据(一帧)
     * @param length (数据长度)
     * @param isIFrame （是否是I 帧）
     * @param time 时间戳
     */
	public  void writeVideoData(byte[] data, int length, boolean isIFrame, int time);
  
	/**
     * 写入音频数据
     * @param data 数据
     * @param length 数据长度
     */
	public  void writeAAcData(byte[] data, int length);
  
	/**
     * 结束录制
     * @param listener 结果回调是否录制成功
     */
	public void stopRecord(final IRecordListener listener);

四、Mp4的播放
	通过调用 XmPluginHostApi.instance().createMp4View(Context context, FrameLayout original, boolean useHard) 返回一个Mp4的播放视图接口 XmVideoViewGl
	然后通过调用XmVideoViewGl.getMp4Player() 返回XmMp4Player 用来操作Mp4播放的各个接口

	SDK提供了两种Mp4播放模式(FFmpeg播放、MediaPlayer播放)
	1.MediaPlayer 播放：
	
	使用VideoGlSurfaceView 进行渲染,初始化的时候需要使用 VideoPlayerRender进行解码相关
	
	2.FFmpeg播放: 手机本身不支持Mp4中视频流的播放(低版本手机不支持h265)
	使用VideoGlSurfaceView 进行渲染,初始化的时候需要使用 VideoPlayerFFmpeg进行解码相关
