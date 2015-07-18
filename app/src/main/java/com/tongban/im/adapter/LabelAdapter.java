package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Label;

import java.util.List;

/**
 * Created by fushudi on 2015/7/18.
 */
public class LabelAdapter extends QuickAdapter<Label> {
    public LabelAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Label item) {
        helper.setText(R.id.tv_label_name, item.getLabelName());
    }
}
