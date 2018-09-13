package com.xiaomi.smarthome.camera.exopackage;

import android.graphics.Bitmap;
import android.view.View;

public interface MJExoPlayer {

    public interface MJExoPlayerListener {
        void onVideoSizeChanged(int width, int height, int unAppliedRotationDegrees, float pixelWidthHeightRatio);

        void onVideoSurfaceViewClicked(View v);

        void onVideoSurfaceViewLongClicked(View v);

        void onRenderedFirstFrame();

        void onPlayerStateChanged(boolean playWhenReady, int playbackState);

        void onLoadingChanged(boolean isLoading);

        void onRepeatModeChanged(int repeatMode);

        void onShuffleModeEnabledChanged(boolean shuffleModeEnabled);

        void onPlayerError(int error);

        void onPositionDiscontinuity(int reason);

        void onSeekProcessed();

        void onPlaybackParametersChanged(float speed);
    }

    void setPlayerListener(MJExoPlayerListener listener);

    MJExoPlayerListener getPlayerListener();

    void startPlay(String playUrl);

    void pausePlay();

    void resumePlay();

    void seekTo(long positionMs);

    void stopPlay();

    int getBufferedPercentage();

    long getBufferedPosition();

    long getCurrentPosition();

    long getDuration();

    boolean getPlayWhenReady();

    void setPlayWhenReady(boolean playWhenReady);

    int getVideoScalingMode();

    float getVolume();

    void setVolume(float audioVolume);

    boolean isLoading();

    float getPlaybackSpeed();

    int getPlaybackState();

    void setPlaybackSpeed(float speed);

    Bitmap snapshot();
}
