package com.voice.tongban.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.utils.LogUtil;
import com.voice.tongban.R;
import com.voice.tongban.model.FinalResult;
import com.voice.tongban.model.Understander;
import com.voice.tongban.utils.UnderstanderRecognitionUtils;

public class VoiceInputActivity extends BaseApiActivity implements
        UnderstanderRecognitionUtils.SemanticListener {

    ListView lvVoiceResults;
    ImageView ivSpeak;
    ImageView ivVolumeChanged;

    UnderstanderRecognitionUtils semanticRecognition;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_voice_input;
    }

    @Override
    protected void initData() {

        semanticRecognition = new UnderstanderRecognitionUtils(mContext);
        semanticRecognition.setSemanticListener(this);
        lvVoiceResults = (ListView) findViewById(R.id.lv_voice_results);
        ivSpeak = (ImageView) findViewById(R.id.iv_speak);
        ivVolumeChanged = (ImageView) findViewById(R.id.iv_volume_changed);

        ivSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semanticRecognition.startUnderstanding();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        semanticRecognition.destroy();
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
                ivVolumeChanged.setImageResource(R.color.transparent);
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_1);
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_2);
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_3);
                break;
            default:
                ivVolumeChanged.setImageResource(R.mipmap.ic_volume_changed_4);
                break;
        }
    }

    @Override
    public void onEndSpeech(Understander understander) {

        LogUtil.d("result");
        ivSpeak.setVisibility(View.VISIBLE);
        ivVolumeChanged.setVisibility(View.GONE);

        // 结果出问题
        if (understander == null) {
            return;
        }
        if (understander.getRc() == 0) {
            return;
        }
        // 智能问答
//        if (understander.getOperation().equals(OperationType.OPERATION_ANSWER)) {
//
//            boolean isSetFirst = false;
//            List<MoreResults> answerList = new ArrayList<>();
//            if (understander.getAnswer() != null) {
//                MoreResults firstResult = new MoreResults();
//                firstResult.setAnswer(understander.getAnswer());
//                isSetFirst = true;
//                firstResult.setIsFirst(isSetFirst);
//                answerList.add(firstResult);
//            }
//            if (understander.getMoreResults() != null && understander.getMoreResults().size() > 0) {
//                for (int i = 0; i < understander.getMoreResults().size(); i++) {
//                    if (!isSetFirst && i > 0) {
//                        understander.getMoreResults().get(i).setIsFirst(true);
//                    }
//                    answerList.add(understander.getMoreResults().get(i));
//                }
//            }
//            VoiceInputAdapter adapter = new VoiceInputAdapter(mContext, answerList,
//                    VoiceLayout);
//            lvVoiceResults.setAdapter(adapter);
//        }
        // 语义抽取
//        else {
//
//        }
    }


    private IMultiItemTypeSupport VoiceLayout = new IMultiItemTypeSupport<FinalResult>() {

        @Override
        public int getLayoutId(int position, FinalResult item) {
            switch (item.getFinalType()) {
                case FinalResult.USER_QUESTION:
                    return R.layout.item_list_question_text;
                case FinalResult.ANSWER:
                    return R.layout.item_list_question_text;
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


}
