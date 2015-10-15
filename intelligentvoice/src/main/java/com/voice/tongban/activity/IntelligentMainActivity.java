package com.voice.tongban.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.voice.tongban.R;
import com.voice.tongban.adapter.IntelligentVoiceAdapter;
import com.voice.tongban.model.FinalResult;
import com.voice.tongban.model.MoreResults;
import com.voice.tongban.model.OperationType;
import com.voice.tongban.model.Understander;
import com.voice.tongban.utils.SpeechSynthesizerUtils;
import com.voice.tongban.utils.UnderstanderRecognitionUtils;

public class IntelligentMainActivity extends BaseApiActivity implements
        UnderstanderRecognitionUtils.SemanticListener
        , View.OnClickListener {

    ListView lvVoiceResults;
    ImageView ivSpeak;
    ImageView ivVolumeChanged;

    // 语义理解
    UnderstanderRecognitionUtils mSemanticRecognition;
    // 语音合成
    SpeechSynthesizerUtils mSpeechSynthesizer;

    IntelligentVoiceAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_voice_input;
    }

    @Override
    protected void initData() {


        mSemanticRecognition = new UnderstanderRecognitionUtils(mContext, this);

        mSpeechSynthesizer = new SpeechSynthesizerUtils(mContext);
        mSpeechSynthesizer.onSpeak("本宝宝等你很久啦");

        lvVoiceResults = (ListView) findViewById(R.id.lv_voice_results);
        ivSpeak = (ImageView) findViewById(R.id.iv_speak);
        ivVolumeChanged = (ImageView) findViewById(R.id.iv_volume_changed);

        ivSpeak.setOnClickListener(this);

        mAdapter = new IntelligentVoiceAdapter(mContext, null, VoiceLayout);
        mAdapter.setOnClickListener(this);
        lvVoiceResults.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mSemanticRecognition.destroy();
        mSpeechSynthesizer.destroy();
    }

    @Override
    public void onStartSpeech() {

        ivSpeak.setVisibility(View.GONE);
        ivVolumeChanged.setVisibility(View.VISIBLE);

    }

    @Override
    public void onVolumeChanged(int volume) {
        switch (volume) {
            case 0:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_0);
                break;
            case 1:
            case 2:
            case 3:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_1);
                break;
            case 4:
            case 5:
            case 6:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_2);
                break;
            case 7:
            case 8:
            case 9:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_3);
                break;
            case 10:
            case 11:
            case 12:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_4);
                break;
            default:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_5);
                break;
        }
    }

    @Override
    public void onFinishSpeech(Understander understander) {

        ivSpeak.setVisibility(View.VISIBLE);
        ivVolumeChanged.setVisibility(View.INVISIBLE);

        // 结果出问题
        if (understander == null) {

            return;
        }
        if (understander.getRc() != 0) {

            return;
        }

        //添加问题item数据
        FinalResult questionItem = new FinalResult();
        questionItem.setFinalType(FinalResult.USER_QUESTION);
        questionItem.setQuestion(understander.getText());

        mAdapter.add(questionItem);

        // 智能问答
        if (understander.getOperation().equals(OperationType.OPERATION_ANSWER)) {

            boolean isSetFirst = false;
            if (understander.getAnswer() != null) {
                MoreResults firstResult = new MoreResults();
                firstResult.setAnswer(understander.getAnswer());
                isSetFirst = true;
                firstResult.setIsFirst(isSetFirst);
                // 说话
                mSpeechSynthesizer.onSpeak(understander.getAnswer().getText());
                FinalResult firstAnswerItem = new FinalResult();
                firstAnswerItem.setMoreResults(firstResult);
                firstAnswerItem.setFinalType(FinalResult.ANSWER);

                mAdapter.add(firstAnswerItem);
            }
            if (understander.getMoreResults() != null && understander.getMoreResults().size() > 0) {
                for (int i = 0; i < understander.getMoreResults().size(); i++) {
                    if (!isSetFirst && i > 0) {
                        understander.getMoreResults().get(i).setIsFirst(true);
                        // 说话
                        mSpeechSynthesizer.onSpeak(understander.getAnswer().getText());
                    }

                    FinalResult answerItem = new FinalResult();
                    answerItem.setMoreResults(understander.getMoreResults().get(i));
                    answerItem.setFinalType(FinalResult.ANSWER);
                    mAdapter.add(answerItem);
                }
            }
        }
        //语义抽取
        else {

        }
        lvVoiceResults.smoothScrollToPosition(mAdapter.getCount() - 1);
    }


    private IMultiItemTypeSupport VoiceLayout = new IMultiItemTypeSupport<FinalResult>() {

        @Override
        public int getLayoutId(int position, FinalResult item) {
            switch (item.getFinalType()) {
                case FinalResult.USER_QUESTION:
                    return R.layout.item_list_question_text;
                case FinalResult.ANSWER:
                    return R.layout.item_list_answer_text_result;
            }
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position, FinalResult item) {
            // 用户提问类型
            return item.getFinalType();
        }

    };


    @Override
    public void onClick(View v) {
        if (v == ivSpeak) {
            mSpeechSynthesizer.onStopSpeak();
            mSemanticRecognition.startUnderstanding();
        } else {
            if (v.getId() == R.id.tv_answer) {
                String text = v.getTag().toString();
                mSpeechSynthesizer.onSpeak(text);
            }
        }
    }
}
