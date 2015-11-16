package com.voice.tongban.fragment;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tb.api.AssistApi;
import com.tb.api.model.AssistTopn;
import com.tb.api.model.BaseEvent;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.voice.tongban.R;
import com.voice.tongban.adapter.AssistTopnAdapter;
import com.voice.tongban.model.Understander;
import com.voice.tongban.model.VoiceTransfer;
import com.voice.tongban.utils.SpeechSynthesizerUtils;
import com.voice.tongban.utils.UnderstanderRecognitionUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 提问界面
 */
public class VoiceInputFragment extends BaseApiFragment implements
        UnderstanderRecognitionUtils.SemanticListener
        , View.OnClickListener {


    View ivVolumeChanged;
    TextView tvWelcome;
    FloatingActionButton ivSpeak;
    ListView lvTopn;

    AssistTopnAdapter assistTopnAdapter;

    // 语义理解
    UnderstanderRecognitionUtils mSemanticRecognition;
    // 语音合成
    SpeechSynthesizerUtils mSpeechSynthesizer;

    public static VoiceInputFragment getInstance() {
        return new VoiceInputFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_voice_input;
    }

    @Override
    protected void initData() {

        mSemanticRecognition = new UnderstanderRecognitionUtils(mContext, this);

        mSpeechSynthesizer = new SpeechSynthesizerUtils(mContext);
//        mSpeechSynthesizer.onSpeak("");


        ivVolumeChanged = mView.findViewById(R.id.v_volume);
        tvWelcome = (TextView) mView.findViewById(R.id.tv_welcome);
        ivSpeak = (FloatingActionButton) mView.findViewById(R.id.iv_speak);
        lvTopn = (ListView) mView.findViewById(R.id.lv_topn_list);

        ivSpeak.setVisibility(View.VISIBLE);
        ivSpeak.setOnClickListener(this);

        assistTopnAdapter = new AssistTopnAdapter(mContext, R.layout.item_assist_topn, null);
        lvTopn.setAdapter(assistTopnAdapter);

        AssistApi.getInstance().getAssistTopn(this);
        mSemanticRecognition.startUnderstanding();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mSemanticRecognition.destroy();
        mSpeechSynthesizer.destroy();
    }

    @Override
    public void onClick(View view) {
        if (view == ivSpeak) {
            mSpeechSynthesizer.onStopSpeak();
            mSemanticRecognition.startUnderstanding();
        }
    }

    @Override
    public void onStartSpeech() {
        tvWelcome.setText("倾听中...");
        ivSpeak.setEnabled(false);
        ivVolumeChanged.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVolumeChanged(final int volume) {

        float endScale = ((float) volume) / 30.0f;
        if (endScale > 0) {
            endScale = (endScale + 2.3f) < 3f ? (endScale + 2.3f) : 3f;
            setIvVolumeChangedAnim(endScale);
        }

    }

    float startScale = 1.0f;

    private void setIvVolumeChangedAnim(float endScale) {
        if (endScale - startScale >= 0.3 || endScale == 0.0f) {
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", startScale, endScale);
            PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", startScale, endScale);
            ObjectAnimator.ofPropertyValuesHolder(ivVolumeChanged, pvhY, pvhZ)
                    .setDuration(500).start();
            if (endScale > startScale) {
                startScale = endScale - 0.8f;
            } else {
                startScale = endScale + 0.8f;
            }
        }
    }

    @Override
    public void onFinishSpeech(Understander understander) {
        tvWelcome.setText("你可以这样问我");
        ivSpeak.setEnabled(true);
        setIvVolumeChangedAnim(0.0f);
        // 结果出问题
        if (understander == null) {
//            mSpeechSynthesizer.onSpeak("你好像没有说话");
            return;
        }
        EventBus.getDefault().post(understander);
        EventBus.getDefault().post(new VoiceTransfer(true));

    }

    public void onEventMainThread(VoiceTransfer obj) {

        if (!obj.isResult)
            mSemanticRecognition.startUnderstanding();

    }


    public void onEventMainThread(BaseEvent.AssistTopnEvent assistTopn) {

        assistTopnAdapter.replaceAll(assistTopn.talentInfo);

    }


}
