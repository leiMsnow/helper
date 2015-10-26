package com.voice.tongban.fragment;


import android.app.Fragment;
import android.media.Image;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.voice.tongban.R;
import com.voice.tongban.adapter.IntelligentVoiceAdapter;
import com.voice.tongban.model.FinalResult;
import com.voice.tongban.model.MoreResults;
import com.voice.tongban.model.OperationType;
import com.voice.tongban.model.Understander;
import com.voice.tongban.model.VoiceTransfer;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoiceResultFragment extends BaseApiFragment implements
        View.OnClickListener {

    ListView lvVoiceResults;
    IntelligentVoiceAdapter mAdapter;
    ImageView ivSpeak;

    public static VoiceResultFragment getInstance() {
        VoiceResultFragment resultFragment = new VoiceResultFragment();
        return resultFragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_voice_result;
    }

    @Override
    protected void initData() {

        lvVoiceResults = (ListView) mView.findViewById(R.id.lv_voice_results);
        ivSpeak = (ImageView) mView.findViewById(R.id.iv_speak);
        mAdapter = new IntelligentVoiceAdapter(mContext, null, VoiceLayout);
        lvVoiceResults.setAdapter(mAdapter);

        mAdapter.setOnClickListener(this);
        ivSpeak.setOnClickListener(this);

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
            EventBus.getDefault().post(new VoiceTransfer(false));
        } else {
            if (v.getId() == R.id.tv_answer) {

            }
        }
    }

    public void onEventMainThread(Understander understander) {

        if (understander.getRc() != 0) {
//            mSpeechSynthesizer.onSpeak("我没有听清你说的话");
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
//                mSpeechSynthesizer.onSpeak(understander.getAnswer().getText());
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
//                        mSpeechSynthesizer.onSpeak(understander.getAnswer().getText());
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
}
