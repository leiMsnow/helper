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
    private LoadMoreListView listView;
    private QuerySuggestionsAdapter mAdapter;

    //是否显示搜索建议
    protected boolean isShowSuggestions = true;
    protected SearchView searchView;

    private String mQueryText;
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
        searchView.setQueryHint("输入关键字");
        searchView.onActionViewExpanded();
        return true;
    }

    protected abstract int getMenuInflate();

    @Override
    protected void initView() {
        listView = (LoadMoreListView) findViewById(R.id.lv_tips_list);
        mAdapter = new QuerySuggestionsAdapter(mContext, R.layout.item_suggestions_list, null);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listView.setVisibility(View.GONE);
                isShowSuggestions = false;
                String keyword = mAdapter.getItem(position);
                searchView.setQuery(keyword, true);
                saveSearchKey(keyword);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (listView.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            listView.setVisibility(View.GONE);
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

    //    private void initListPopupWindow(List<String> data) {
//        int mScreenHeight = ScreenUtils.getScreenHeight(mContext);
//        int toolBarHeight = mToolbar.getHeight();
//        suggestionPopupWindow = new SuggestionPopupWindow(
//                ViewGroup.LayoutParams.MATCH_PARENT, (mScreenHeight - toolBarHeight),
//                data, LayoutInflater.from(getApplicationContext())
//                .inflate(R.layout.view_suggestions_popup_window, null));
//        suggestionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                // 设置背景颜色
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                getWindow().setAttributes(lp);
//            }
//        });
//        suggestionPopupWindow.adapterUpdate(mQueryText, data);
//        suggestionPopupWindow.setOnKeywordSelected(this);
//        suggestionPopupWindow.showAsDropDown(mToolbar, 0, 0);
//
//        // 设置背景颜色变暗
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 1f;
//        getWindow().setAttributes(lp);
//    }

    public void onEventMainThread(BaseEvent.SuggestionsEvent obj) {
//        if (suggestionPopupWindow != null)
//            suggestionPopupWindow.dismiss();
//        if (obj.keywords != null && obj.keywords.size() > 0) {
//            initListPopupWindow(obj.keywords);
//        }
        if (obj.keywords != null && obj.keywords.size() > 0) {
            adapterUpdate(mQueryText, obj.keywords);
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
        }
    }


    public void adapterUpdate(String queryText, List<String> keywords) {
        mAdapter.setQueryText(queryText);
        mAdapter.replaceAll(keywords);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mQueryText = newText;
        if (!TextUtils.isEmpty(mQueryText)) {
            if (isShowSuggestions) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        CommonApi.getInstance().getHotWordsList(mQueryText,
                                SuggestionsBaseActivity.this);
                    }
                }, 500);
            }
        } else {
            listView.setVisibility(View.GONE);
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

    private void gonSearchResult() {
        isShowSuggestions = true;
        listView.setVisibility(View.GONE);
        searchView.onActionViewCollapsed();
        searchView.setQuery(mQueryText, false);
    }

}
