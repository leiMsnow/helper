package com.voice.tongban.utils;

import android.content.Context;
import android.net.Uri;

import io.rong.imkit.util.IVoiceHandler;

/**
 * 语音播放类
 * 实现了语音的播放功能，使用的是融云的接口
 * Created by zhangleilei on 10/10/15.
 */
public class VoicePlayUtils implements IVoiceHandler.OnPlayListener {

    private Context mContext;
    private IVoiceHandler mVoiceHandler;

    private IVoicePlayListener playListener;

    public VoicePlayUtils(Context context, IVoicePlayListener playListener) {

        this.mContext = context;
        this.playListener = playListener;

        mVoiceHandler = new IVoiceHandler.VoiceHandler(context);
        mVoiceHandler.setPlayListener(this);
    }

    public void play(Uri uri) {
        mVoiceHandler.play(mContext, uri);
    }

    public void stop() {
        mVoiceHandler.stop();
    }

    @Override
    public void onVoicePlay(Context context, long timeout) {
        if (playListener!=null){
            playListener.onVoicePlay(timeout);
        }
    }

    @Override
    public void onVoiceCover(boolean limited) {

    }

    @Override
    public void onVoiceStop() {
        if (playListener!=null){
            playListener.onVoiceFinish();
        }
    }


    public interface IVoicePlayListener {
        void onVoicePlay(long timeout);

        void onVoiceFinish();
    }

}
