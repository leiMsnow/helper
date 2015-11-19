package com.voice.tongban.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.voice.tongban.R;
import com.voice.tongban.model.FinalResult;

import java.util.List;

/**
 * Created by zhangleilei on 10/14/15.
 */
public class IntelligentVoiceAdapter extends QuickAdapter<FinalResult> {


    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public IntelligentVoiceAdapter(Context context, List data, IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, FinalResult item) {
        switch (item.getFinalType()) {
            case FinalResult.USER_QUESTION:
                helper.setText(R.id.tv_question, item.getQuestion());
                break;
            case FinalResult.ANSWER_ERROR:
                helper.setText(R.id.tv_answer, item.getErrorInfo());
                break;
            case FinalResult.ANSWER_TEXT:
                if (item.getMoreResults().getAnswer().getText().length() < 2) {
                }
                helper.setText(R.id.tv_answer, item.getMoreResults().getAnswer().getText());
//                helper.setTag(R.id.tv_answer, item.getMoreResults().getAnswer().getText());
//                helper.setOnClickListener(R.id.tv_answer, onClickListener);
                break;
            case FinalResult.ANSWER_TALENT:

                if (item.getTalentInfo().getUser() != null && item.getTalentInfo().getUser().getPortraitUrl() != null) {

                    helper.setImageBitmap(R.id.iv_user_portrait, item.getTalentInfo().getUser().getPortraitUrl().getMin());
                } else {
                    helper.setImageBitmap(R.id.iv_user_portrait, R.mipmap.ic_default_image);
                }

                helper.setText(R.id.tv_name, item.getTalentInfo().getProducer_name());
                helper.setRating(R.id.rb_score, item.getTalentInfo().getScore());
                helper.setText(R.id.tv_score, String.valueOf(item.getTalentInfo().getScore()));
                if (item.getTalentInfo().getProducer_desc() != null)
                    helper.setText(R.id.tv_desc, item.getTalentInfo().getProducer_desc().getDesc());

                    helper.setTag(R.id.fl_parent, item.getTalentInfo().getUser_id());
                    helper.setTag(R.id.fl_parent, Integer.MAX_VALUE, item.getTalentInfo().getProducer_name());
                    helper.setTag(R.id.fl_parent, Integer.MIN_VALUE,item.getTalentInfo().getProducer_id());
                    helper.setOnClickListener(R.id.fl_parent, onClickListener);


                break;
            case FinalResult.ANSWER_KNOWLEDGES:
                helper.setText(R.id.tv_desc, item.getKnowledgeAnswers().getKnowledgeAnswerses().getAnswer());

                helper.setTag(R.id.fl_parent_line,item.getKnowledgeAnswers().getKnowledgeAnswerses().getUrl());
                helper.setOnClickListener(R.id.fl_parent_line, onClickListener);
                break;
        }

    }

}