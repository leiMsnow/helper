package com.voice.tongban.utils;

import android.content.Context;
import android.net.Uri;

import io.rong.imkit.util.IVoiceHandler;

/**
 * Created by zhangleilei on 10/10/15.
 */
public class VoiceUtils {

    private Context mContext;
    private IVoiceHandler mVoiceHandler;

    public VoiceUtils(Context context, IVoiceHandler.OnPlayListener onPlayListener){
        this.mContext = context;
        mVoiceHandler = new IVoiceHandler.VoiceHandler(context);
        mVoiceHandler.setPlayListener(onPlayListener);
    }

    public void play(Uri uri){
        mVoiceHandler.play(mContext,uri);
    }

    public void stop(){
        mVoiceHandler.stop();
    }


}
