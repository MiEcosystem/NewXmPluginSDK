package com.xiaomi.smarthome.camera;

public interface IXmStreamClient {

    public void streamStart(IMISSListener listener);

    public void streamStop(IMISSListener listener);

    public void streamStartCall(IMISSListener listener);

    public void streamStopCall(IMISSListener listener);

    public void streamResolution(int resolution, IMISSListener listener);

    public void streamDirection(int direction, IMISSListener listener);

    public void streamToggleAudio(boolean isMute, IMISSListener listener);

    public void streamSendMessage(int reqId, int resId, byte[] data, P2pResponse response, IMISSListener listener);

    public void setRDTTimelineListener(IRDTTimelineListener listener);

    public void setRDTPictureListener(IRDTPictureListener listener);

    public void streamPlaybackSpeed(String params, IMISSListener listener);

    public void streamPlayback(String params, IMISSListener listener);

    public void streamToggleRemoteVideo(boolean isEnabled, IMISSListener listener);

    public void streamToggleRemoteAudio(boolean isEnabled, IMISSListener listener);

    public void streamGetDeviceInfo(IMISSListener listener);

    public void setClientListener(IClientExListener listener);

    public void sendAudioData(byte[] data, int type);

    public boolean isPaused();

    public boolean isConnected();

    public void updateInfo();

    public void release(IClientExListener listener);
}