package com.voice.tongban.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * 科大讯飞语音识别实现类
 * 1.使用dialog录入语音
 * 2.实现了播放当前录入的语音
 * Created by zhangleilei on 10/10/15.
 */
public class SpeechRecognitionUtils {

    private Context mContext;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    private String playUri;

    // 是否返回播放地址
    private boolean isVoice = false;

    public String getVoiceUrl() {
        return isVoice ? playUri : "";
    }

    VoicePlayUtils voicePlay;

    public void setVoicePlayUtils(VoicePlayUtils.IVoicePlayListener voicePlayListener) {
        this.voicePlay = new VoicePlayUtils(mContext, voicePlayListener);
    }

    private RecognizerResultListener resultListener;

    public SpeechRecognitionUtils(Context context, RecognizerResultListener resultListener) {

        this.mContext = context;
        this.resultListener = resultListener;
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(mContext, mInitListener);
        playUri = Environment.getExternalStorageDirectory() + "/msc/iat.wav";
        setParam();
    }

    public void startRecognizerDialog() {
        mIatResults.clear();
        // 显示听写对话框
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
    }

    public void startRecognizer() {
        if (mIat.isListening()) {
            return;
        }
        // 不显示听写对话框
        int ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            LogUtil.d("听写失败,错误码：" + ret);
        }
    }

    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1500");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, playUri);
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            ToastUtil.getInstance(mContext).showToast(error.getPlainDescription(true));
        }

    };

    /**
     * 听写监听器，无界面。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            LogUtil.d("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            LogUtil.d(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            LogUtil.d("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.d(results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {

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


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                ToastUtil.getInstance(mContext).showToast("初始化失败，错误码：" + code);
            }
        }
    };

    // 解析结果
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        LogUtil.d(resultBuffer.toString());

        if (resultListener != null) {
            isVoice = true;
            resultListener.recognizerResult(resultBuffer.toString());
        }
    }

    public void destroy() {
        voicePlay.stop();
        // 退出时释放连接
        if (mIat.isListening())
            mIat.cancel();
        mIat.destroy();
    }

    private static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /**
     * 播放录音
     */
    public void playRecording() {
        File file = new File(getVoiceUrl());
        if (file.exists()) {
            if (voicePlay != null)
                voicePlay.play(Uri.fromFile(file));
        }
    }

    public void stopRecording() {
        if (voicePlay != null) {
            voicePlay.stop();
        }
    }

    /**
     * 语音识别结果回调
     */
    public interface RecognizerResultListener {
        void recognizerResult(String result);
    }

}
