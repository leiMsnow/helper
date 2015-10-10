package com.tongban.im.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
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

import io.rong.imkit.util.IVoiceHandler;

/**
 * 科大讯飞语音识别实现类
 * Created by zhangleilei on 10/10/15.
 */
public class KDXFRecognizerUtils implements IVoiceHandler.OnPlayListener {

    private Context mContext;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    private String playUri;

    private boolean isVoice = false;

    public String getVoiceUrl() {
        if (isVoice)
            return playUri;
        else return "";
    }

    VoiceUtils voiceUtils;

    private RecognizerResultListener resultListener;

    private PlayResultListener playListener;

    public void setPlayListener(PlayResultListener playListener) {
        this.playListener = playListener;
    }

    public void setResultListener(RecognizerResultListener resultListener) {
        this.resultListener = resultListener;
    }


    public KDXFRecognizerUtils(Context context) {

        this.mContext = context;
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(mContext, mInitListener);
        playUri = Environment.getExternalStorageDirectory() + "/msc/iat.wav";

        voiceUtils = new VoiceUtils(mContext,this);
    }

    public void startRecognizer() {
        mIatResults.clear();
        setParam();
        // 显示听写对话框
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
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
        mIat.setParameter(SpeechConstant.VAD_EOS, "2000");
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

    public void onDestroy() {
        voiceUtils.stop();
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
    public void playRecognizer() {
        File file = new File(playUri);
        if (file.exists())
            voiceUtils.play(Uri.fromFile(file));
    }

    @Override
    public void onPlay(Context context, long timeout) {
        //播放录音开始
        if (playListener != null)
            playListener.onPlayStart(timeout);
    }

    @Override
    public void onCover(boolean limited) {

    }

    @Override
    public void onStop() {
        //播放录音结束
        if (playListener != null)
            playListener.onPlayEnd();
    }

    public interface RecognizerResultListener {
        void recognizerResult(String result);
    }

    public interface PlayResultListener {
        void onPlayStart(long timeout);

        void onPlayEnd();
    }
}
