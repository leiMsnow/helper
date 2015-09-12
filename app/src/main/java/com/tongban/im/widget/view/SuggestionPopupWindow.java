package com.tongban.im.widget.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tongban.corelib.widget.view.BasePopupWindowForListView;
import com.tongban.im.R;
import com.tongban.im.adapter.QuerySuggestionsAdapter;

import java.util.List;

public class SuggestionPopupWindow extends BasePopupWindowForListView<String> {

    private ListView listView;
    private QuerySuggestionsAdapter mAdapter;

    public SuggestionPopupWindow(int width, int height,
                                 List<String> data, View convertView) {
        super(convertView, width, height, true, data);
    }

    @Override
    public void initViews() {
        listView = (ListView) findViewById(R.id.lv_tips_list);
        mAdapter = new QuerySuggestionsAdapter(mContext, R.layout.item_suggestions_list, null);
        listView.setAdapter(mAdapter);
    }


    public void adapterUpdate(List<String> keywords) {
        mAdapter.replaceAll(keywords);
    }

    public interface OnKeywordSelectListener {
        void keywordSelected(String keyword);
    }

    private OnKeywordSelectListener mSelected;

    public void setOnKeywordSelected(OnKeywordSelectListener selected) {
        this.mSelected = selected;
    }

    @Override
    public void initEvents() {
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SuggestionPopupWindow.this.dismiss();
                if (mSelected != null) {
                    mSelected.keywordSelected(mData.get(position));
                }
            }
        });
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
        // TODO Auto-generated method stub
    }

}
