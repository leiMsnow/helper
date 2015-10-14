package com.voice.tongban.adapter;

import android.content.Context;

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


    public VoiceInputAdapter(Context context, List data, IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, FinalResult item) {
        switch (item.getFinalType()) {
            case FinalResult.USER_QUESTION:
                helper.setText(R.id.tv_question,item.getQuestion());
                break;
            case FinalResult.ANSWER:
                helper.setText(R.id.tv_answer,item.getMoreResults().getAnswer().getText());
                break;
        }
    }
}
