package com.voice.tongban.utils;

import android.content.Context;
import android.net.Uri;

import io.rong.imkit.util.IVoiceHandler;

/**
 * 语音播放类
 * 实现了语音的播放功能，使用的是融云的接口
 * Created by zhangleilei on 10/10/15.
 */
public class VoicePlayUtils {

    private Context mContext;
    private IVoiceHandler mVoiceHandler;

    public VoicePlayUtils(Context context, IVoiceHandler.OnPlayListener onPlayListener) {
        this.mContext = context;
        mVoiceHandler = new IVoiceHandler.VoiceHandler(context);
        mVoiceHandler.setPlayListener(onPlayListener);
    }

    public void play(Uri uri) {
        mVoiceHandler.play(mContext, uri);
    }

    public void stop() {
        mVoiceHandler.stop();
    }


}
