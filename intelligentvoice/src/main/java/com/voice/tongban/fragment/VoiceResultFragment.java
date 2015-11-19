package com.voice.tongban.fragment;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.tb.api.AssistApi;
import com.tb.api.TopicApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.Knowledge;
import com.tb.api.model.TalentInfo;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.voice.tongban.R;
import com.voice.tongban.adapter.IntelligentVoiceAdapter;
import com.voice.tongban.model.FinalResult;
import com.voice.tongban.model.MoreResults;
import com.voice.tongban.model.OperationType;
import com.voice.tongban.model.Understander;
import com.voice.tongban.model.VoiceTransfer;
import com.voice.tongban.utils.SpeechSynthesizerUtils;

import java.util.Random;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imkit.message.CardMessage;
import io.rong.imkit.tools.RongWebviewActivity;


/**
 * 语音搜索结果.
 */
public class VoiceResultFragment extends BaseApiFragment implements
        View.OnClickListener {

    ListView lvVoiceResults;
    IntelligentVoiceAdapter mAdapter;
    FloatingActionButton ivSpeak;

    SpeechSynthesizerUtils mSpeechSynthesizer;

    String currentSession;

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

        mSpeechSynthesizer = new SpeechSynthesizerUtils(mContext);

        lvVoiceResults = (ListView) mView.findViewById(R.id.lv_voice_results);
        ivSpeak = (FloatingActionButton) mView.findViewById(R.id.iv_speak);
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
                case FinalResult.ANSWER_ERROR:
                case FinalResult.ANSWER_TEXT:
                    return R.layout.item_list_answer_text;
                case FinalResult.ANSWER_TALENT:
                    return R.layout.item_list_answer_card;
                case FinalResult.ANSWER_KNOWLEDGES:
                    return R.layout.item_list_answer_line;
            }
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 5;
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

            } else if (v.getId() == R.id.fl_parent) {
                String user_id = v.getTag().toString();
                String name = v.getTag(Integer.MAX_VALUE).toString();
                String dealerId = v.getTag(Integer.MIN_VALUE).toString();
                AssistApi.getInstance().setAssistUpdate(currentSession, dealerId, this);
                RongIM.getInstance().startPrivateChat(mContext, user_id,
                        name);
            } else if (v.getId() == R.id.fl_parent_line) {
                String url = v.getTag().toString();
                Intent intent = new Intent(mContext, RongWebviewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        }
    }

    private void setErrorResult() {
        Random random = new Random();
        String answerList[] = getResources().getStringArray(R.array.voice_answer_error_list);
        int count = answerList.length;

        String resultCount = answerList[random.nextInt(count)];
        mSpeechSynthesizer.onSpeak(resultCount);

        FinalResult firstAnswerItem = new FinalResult();
        firstAnswerItem.setErrorInfo(resultCount);
        firstAnswerItem.setFinalType(FinalResult.ANSWER_ERROR);

        mAdapter.add(firstAnswerItem);
        lvVoiceResults.smoothScrollToPosition(mAdapter.getCount() - 1);
    }

    public void onEventMainThread(Understander understander) {

        //添加问题item数据
        FinalResult questionItem = new FinalResult();
        questionItem.setFinalType(FinalResult.USER_QUESTION);
        questionItem.setQuestion(understander.getText());

        mAdapter.add(questionItem);
        //语义无法理解提示
        if (understander.getRc() != 0) {

//            setErrorResult();
            AssistApi.getInstance()
                    .createAssistQuery(currentSession, understander.getText(), this);
            return;
        }

        // 智能问答
        if (understander.getOperation().equals(OperationType.OPERATION_ANSWER)) {

            // 记录是否有返还第一条数据
            boolean isSetFirst = false;
            // 解析默认第一条数据
            if (understander.getAnswer() != null) {

                if (parseAnswerText(understander.getAnswer().getText())) {
                    AssistApi.getInstance()
                            .createAssistQuery(currentSession, understander.getText(), this);

//                    TopicApi.getInstance().searchTopicList(understander.getText(), 0, 10, this);
                    return;
                } else {

                    MoreResults firstResult = new MoreResults();
                    firstResult.setAnswer(understander.getAnswer());
                    isSetFirst = true;
                    mSpeechSynthesizer.onSpeak(understander.getAnswer().getText());

                    FinalResult firstAnswerItem = new FinalResult();
                    firstAnswerItem.setMoreResults(firstResult);
                    firstAnswerItem.setFinalType(FinalResult.ANSWER_TEXT);

                    mAdapter.add(firstAnswerItem);
                }

            }
            // 解析其他数据集合
            if (understander.getMoreResults() != null && understander.getMoreResults().size() > 0) {
                for (int i = 0; i < understander.getMoreResults().size(); i++) {
                    if (!isSetFirst && i == 0) {

                        if (parseAnswerText(understander.getAnswer().getText())) {

                            AssistApi.getInstance()
                                    .createAssistQuery(currentSession, understander.getText(), this);

//                            TopicApi.getInstance().searchTopicList(understander.getText(), 0, 10, this);
                            return;
                        }
                        mSpeechSynthesizer.onSpeak(understander.getAnswer().getText());
                    }

                    FinalResult answerItem = new FinalResult();
                    answerItem.setMoreResults(understander.getMoreResults().get(i));
                    answerItem.setFinalType(FinalResult.ANSWER_TEXT);

                    mAdapter.add(answerItem);
                }
            }
        }
        //语义抽取
        else {

        }
        lvVoiceResults.smoothScrollToPosition(mAdapter.getCount() - 1);
    }

    // 判断是否为本系统的语义反馈
    private boolean parseAnswerText(String answer) {
//        if (answer.equals(OperationType.TB_TOPIC)) {
//            return true;
//        }

        return true;
    }

    public void onEventMainThread(BaseEvent.AssistAnswerEvent answers) {
        currentSession = answers.answers.getSession_id();

        FinalResult answerItem = new FinalResult();
        if (answers.answers.getAnswers().getProducers() != null) {

            for (TalentInfo talentInfo : answers.answers.getAnswers().getProducers()) {

                answerItem.setFinalType(FinalResult.ANSWER_TALENT);
                answerItem.setTalentInfo(talentInfo);

            }
        } else if (answers.answers.getAnswers().getKnowledges() != null) {
            for (Knowledge answers1 : answers.answers.getAnswers().getKnowledges()) {

                answerItem.setFinalType(FinalResult.ANSWER_KNOWLEDGES);
                answerItem.setKnowledgeAnswers(answers1);

            }
        }

        mAdapter.add(answerItem);

        lvVoiceResults.smoothScrollToPosition(mAdapter.getCount() - 1);
    }

    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(TopicApi.SEARCH_TOPIC_LIST)) {
            setErrorResult();
        }
    }
}
