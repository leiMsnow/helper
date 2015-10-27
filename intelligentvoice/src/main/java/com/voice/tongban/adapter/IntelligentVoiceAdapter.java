package com.voice.tongban.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.tb.api.model.topic.TopicType;
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
            case FinalResult.ANSWER_TOPIC:

                helper.setTag(R.id.fl_topic_parent, item.getTopic().getTopic_id());
                helper.setTag(R.id.fl_topic_parent, Integer.MAX_VALUE, item.getTopic().getTopic_type());
                helper.setOnClickListener(R.id.fl_topic_parent, onClickListener);

                helper.setText(R.id.tv_topic_title, item.getTopic().getTopic_title());
                helper.setText(R.id.tv_topic_content
                        , item.getTopic().getTopicContent().getTopic_content_text());
                helper.setText(R.id.tv_topic_type, parseTopicType(item.getTopic().getTopic_type()));

                Drawable iconType = mContext.getResources().getDrawable(R.color.transparent);

                if (item.getTopic().getTopicContent().getTopic_img_url() != null
                        && item.getTopic().getTopicContent().getTopic_content_voice() != null) {
                    iconType = mContext.getResources().getDrawable(R.mipmap.ic_voice_pic);

                } else if (item.getTopic().getTopicContent().getTopic_img_url() != null) {
                    iconType = mContext.getResources().getDrawable(R.mipmap.ic_pic);

                } else if (item.getTopic().getTopicContent().getTopic_content_voice() != null) {
                    iconType = mContext.getResources().getDrawable(R.mipmap.ic_voice);

                }
                iconType.setBounds(0, 0, iconType.getMinimumWidth(), iconType.getMinimumHeight());
                ((TextView) helper.getView(R.id.tv_topic_title))
                        .setCompoundDrawables(null, null, iconType, null);
                break;
        }
    }


    private String parseTopicType(String type) {
        if (type.equals(TopicType.THEME)) {
            return "专题";
        } else if (type.equals(TopicType.EVALUATION)) {
            return "问答";
        } else if (type.equals(TopicType.PRIVATE)) {
            return "个人";
        }
        return "";
    }

}