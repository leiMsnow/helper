package com.voice.tongban.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.tongban.corelib.utils.LogUtil;

/**
 * 语音合成工具类
 * Created by zhangleilei on 10/15/15.
 */
public class SpeechSynthesizerUtils {

    private Context mContext;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    public SpeechSynthesizerUtils(Context mContext) {
        this.mContext = mContext;
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        setParam();
    }


    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            LogUtil.d("InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                LogUtil.d("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    public void onSpeak(String text) {
        mTts.startSpeaking(text, mTtsListener);
    }

    public void onStopSeak() {
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
    }

    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, ("50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, ("50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, ("50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, ("3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH
                , Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            LogUtil.d("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            LogUtil.d("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            LogUtil.d("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                LogUtil.d("播放完成");
            } else if (error != null) {
                LogUtil.d(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    public void destroy() {
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
        // 退出时释放连接
        mTts.destroy();
    }
}
