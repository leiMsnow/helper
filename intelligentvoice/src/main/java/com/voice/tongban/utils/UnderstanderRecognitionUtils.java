package com.voice.tongban.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ToastUtil;
import com.voice.tongban.model.Understander;

/**
 * 语义识别实现类
 * 实现了用户输入语音，自动反馈结果的接口
 * http://www.xfyun.cn/index.php/mycloud/osp
 * Created by zhangleilei on 10/13/15.
 */
public class UnderstanderRecognitionUtils {

    private static String TAG = UnderstanderRecognitionUtils.class.getSimpleName();

    private Context mContext;
    // 语义理解对象（语音到语义）。
    private SpeechUnderstander mSpeechUnderstander;

    private SemanticListener mSemanticListener;


    public UnderstanderRecognitionUtils(Context context,SemanticListener mSemanticListener) {
        this.mContext = context;
        this.mSemanticListener = mSemanticListener;
        // 初始化对象
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(mContext, mSpeechUdrInitListener);
        // 设置参数
        setParam();
    }


    public void setParam() {
        // 设置语言
        mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, "1500");
        // 设置标点符号，默认：1（有标点）
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH
                , Environment.getExternalStorageDirectory() + "/msc/sud.wav");
    }

    public void startUnderstanding() {
        // 开始前检查状态
        if (mSpeechUnderstander.isUnderstanding()) {
            mSpeechUnderstander.stopUnderstanding();
        } else {
            int ret = mSpeechUnderstander.startUnderstanding(mSpeechUnderstanderListener);
            if (ret != 0) {
                LogUtil.d("语义理解失败,错误码:" + ret);
            }
        }
    }

    /**
     * 初始化监听器（语音到语义）。
     */
    private InitListener mSpeechUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtil.d(TAG, "speechUnderstanderListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                ToastUtil.getInstance(mContext).showToast("初始化失败,错误码：" + code);
            }
        }
    };
    /**
     * 语义理解回调。
     */
    private SpeechUnderstanderListener mSpeechUnderstanderListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                Log.d(TAG, result.getResultString());

                Understander text = JSON.parseObject(result.getResultString(),
                        new TypeReference<Understander>() {
                        });

                if (text != null) {
                    if (mSemanticListener != null) {
                        mSemanticListener.onEndSpeech(text);
                    }
                }
            } else {
                LogUtil.d("识别结果不正确。");
                if (mSemanticListener != null) {
                    mSemanticListener.onEndSpeech(null);
                }
            }
        }

        // 当前音量值，范围[0-30]
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            LogUtil.d("当前正在说话，音量大小：" + volume);
            Log.d(TAG, data.length + "");
            if (mSemanticListener != null) {
                mSemanticListener.onVolumeChanged(volume);
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            if (mSemanticListener != null) {
                mSemanticListener.onStartSpeech();
            }
        }

        @Override
        public void onError(SpeechError error) {
            LogUtil.d(error.getPlainDescription(true));
            if (mSemanticListener != null) {
                mSemanticListener.onEndSpeech(null);
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    public void destroy() {
        // 退出时释放连接
        mSpeechUnderstander.cancel();
        mSpeechUnderstander.destroy();
    }

    public interface SemanticListener {
        void onStartSpeech();

        void onVolumeChanged(int volume);

        void onEndSpeech(Understander result);
    }
}