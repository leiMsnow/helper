package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;

import java.util.List;

public class QuerySuggestionsAdapter extends QuickAdapter<String> {

    public QuerySuggestionsAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, String item) {
        helper.setText(R.id.tv_suggestions_name, item);
    }
}