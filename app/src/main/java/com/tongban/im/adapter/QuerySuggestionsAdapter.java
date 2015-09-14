package com.tongban.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;

import java.util.List;

public class QuerySuggestionsAdapter extends QuickAdapter<String> {

    private String queryText;

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public QuerySuggestionsAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, String item) {
        SpannableString ss = addForeColorSpan(item);
        helper.setText(R.id.tv_suggestions_name, ss);
    }

    /**
     * 文字颜色
     */
    private SpannableString addForeColorSpan(String allText) {
        SpannableString spanString = new SpannableString(allText);
        for (int i = 0; i < queryText.toCharArray().length; i++) {
            char text = queryText.toCharArray()[i];
            int start = allText.indexOf(text);
            if (start > -1) {
                int end = start + 1;
                ForegroundColorSpan span = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.main_deep_orange));
                spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanString;
    }
}