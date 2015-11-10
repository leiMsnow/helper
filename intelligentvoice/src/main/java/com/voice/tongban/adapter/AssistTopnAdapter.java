package com.voice.tongban.adapter;

import android.content.Context;
import android.view.View;

import com.tb.api.model.AssistTopn;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.voice.tongban.R;

import java.util.List;

/**
 * 热词adapter
 * Created by zhangleilei on 10/14/15.
 */
public class AssistTopnAdapter extends QuickAdapter<AssistTopn> {


    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AssistTopnAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, AssistTopn item) {
        helper.setText(R.id.btn_topn, item.getIssue());
    }
}