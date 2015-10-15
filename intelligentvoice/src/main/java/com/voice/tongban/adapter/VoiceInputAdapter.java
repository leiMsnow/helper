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
public class VoiceInputAdapter extends QuickAdapter<FinalResult> {


    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public VoiceInputAdapter(Context context, List data, IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, FinalResult item) {
        switch (item.getFinalType()) {
            case FinalResult.USER_QUESTION:
                helper.setText(R.id.tv_question, item.getQuestion());
                break;
            case FinalResult.ANSWER:
                helper.setText(R.id.tv_answer, item.getMoreResults().getAnswer().getText());
                helper.setTag(R.id.tv_answer,item.getMoreResults().getAnswer().getText());
                helper.setOnClickListener(R.id.tv_answer, onClickListener);
                break;
        }
    }

}
