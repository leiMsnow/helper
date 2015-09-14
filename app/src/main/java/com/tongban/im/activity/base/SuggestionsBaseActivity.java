package com.tongban.im.activity.base;

import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.im.R;
import com.tongban.im.adapter.QuerySuggestionsAdapter;
import com.tongban.im.api.CommonApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;

import java.util.List;

/**
 * 通用的照片处理父类
 * 裁剪
 * 拍照
 * 相册
 * Created by fushudi on 2015/8/13.
 */
public abstract class SuggestionsBaseActivity extends BaseToolBarActivity implements
        SearchView.OnQueryTextListener {


    //    private SuggestionPopupWindow suggestionPopupWindow;
    protected LoadMoreListView suggestionsListView;
    private QuerySuggestionsAdapter mAdapter;

    //是否显示搜索建议
    protected boolean isShowSuggestions = true;
    protected SearchView searchView;

    protected String mQueryText;
    //最大历史记录数
    private final static int mKeyCount = 10;
    protected String mKeys;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenuInflate(), menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.setQuery(mQueryText, false);
            }
        }, 500);
        searchView.setQueryHint("输入关键字");
        searchView.onActionViewExpanded();
        return true;
    }

    protected abstract int getMenuInflate();

    @Override
    protected void initView() {
        suggestionsListView = (LoadMoreListView) findViewById(R.id.lv_tips_list);
        mAdapter = new QuerySuggestionsAdapter(mContext, R.layout.item_suggestions_list, null);
        suggestionsListView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                suggestionsListView.setVisibility(View.GONE);
                isShowSuggestions = false;
                String keyword = mAdapter.getItem(position);
                searchView.setQuery(keyword, true);
                saveSearchKey(keyword);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (suggestionsListView != null) {
            if (suggestionsListView.getVisibility() == View.GONE) {
                super.onBackPressed();
            } else {
                suggestionsListView.setVisibility(View.GONE);
            }
        } else {
            super.onBackPressed();
        }
    }

    //保存历史搜索key
    protected void saveSearchKey(String query) {
        if (TextUtils.isEmpty(mKeys))
            return;
        if (query.contains(";"))
            query = query.replace(";", "");
        String[] keyList = mKeys.split(";");
        if (!TextUtils.isEmpty(mKeys)) {
            for (int i = 0; i < keyList.length; i++) {
                if (keyList[i].equals(query)) {
                    mKeys = mKeys.replace(query + ";", "");
                    break;
                }
            }
            if (keyList.length == mKeyCount) {
                mKeys = mKeys.replace(keyList[keyList.length - 1] + ";", "");
            }
        }
        mKeys = query + ";" + mKeys;
        SPUtils.put(mContext, Consts.HISTORY_SEARCH_TOPIC, mKeys);
    }

    public void onEventMainThread(BaseEvent.SuggestionsEvent obj) {
        if (obj.keywords != null && obj.keywords.size() > 0) {
            adapterUpdate(mQueryText, obj.keywords);
            suggestionsListView.setVisibility(View.VISIBLE);
        } else {
            suggestionsListView.setVisibility(View.GONE);
        }
    }


    public void adapterUpdate(String queryText, List<String> keywords) {
        mAdapter.setQueryText(queryText);
        mAdapter.replaceAll(keywords);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            mQueryText = newText;
            if (isShowSuggestions) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        CommonApi.getInstance().getHotWordsList(mQueryText,
                                SuggestionsBaseActivity.this);
                    }
                }, 500);
            }
        } else {
            suggestionsListView.setVisibility(View.GONE);
        }

        return false;
    }

    public void onEventMainThread(ApiErrorResult obj) {
        isShowSuggestions = true;
    }

    public void onEventMainThread(BaseEvent.SearchTopicListEvent obj) {
        gonSearchResult();
    }

    public void onEventMainThread(BaseEvent.SearchGroupListEvent obj) {
        gonSearchResult();
    }
//    public void onEventMainThread(BaseEvent.SearchThemeResultEvent event) {
//        gonSearchResult();
//    }
//
//    public void onEventMainThread(BaseEvent.SearchProductResultEvent event) {
//        gonSearchResult();
//    }


    private void gonSearchResult() {
        isShowSuggestions = true;
        suggestionsListView.setVisibility(View.GONE);
        searchView.onActionViewCollapsed();
    }

}
