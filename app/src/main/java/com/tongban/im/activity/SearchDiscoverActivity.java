package com.tongban.im.activity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * 搜索首页
 *
 * @author zhangleilei
 * @createTime 2015/8/12
 */
public class SearchDiscoverActivity extends BaseToolBarActivity implements SearchView.OnQueryTextListener,
        View.OnClickListener {

    private SearchView searchView;
    private TextView tvHistory;
    private FlowLayout flHistorySearch;
    private ListView lvTopicList;

    private String mKeys;
    private final int mKeyCount = 10;
    private List<Topic> mTopicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_discover_search;
    }

    @Override
    protected void initView() {
        tvHistory = (TextView) findViewById(R.id.tv_history);
        flHistorySearch = (FlowLayout) findViewById(R.id.fl_history_search);
        lvTopicList = (ListView) findViewById(R.id.lv_topic_list);
    }

    @Override
    protected void initData() {
        mKeys = SPUtils.get(mContext, Consts.HISTORY_SEARCH_TOPIC, "").toString();
        if (TextUtils.isEmpty(mKeys)) {
            tvHistory.setVisibility(View.GONE);
            flHistorySearch.setVisibility(View.GONE);
            return;
        }
        setSearchKeyView();
    }


    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_topic, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("搜索关键字");
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            if (query.contains(";"))
                query = query.replace(";", "");
            saveSearchKey(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        //点击搜索历史
        if (v.getId() == R.id.tv_history_key) {
            String key = v.getTag().toString();
            searchView.setQuery(key, true);
        }
        //清除搜索记录
        else if (v.getId() == R.id.tv_key_clear) {
            tvHistory.setVisibility(View.GONE);
            flHistorySearch.setVisibility(View.GONE);
            SPUtils.put(mContext, Consts.HISTORY_SEARCH_TOPIC, "");
        }
    }


    //保存历史搜索key
    private void saveSearchKey(String query) {
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

    //设置历史搜索key和控件
    private void setSearchKeyView() {
        String[] keyList = mKeys.split(";");
        if (keyList.length > 0) {
            for (int i = 0; i < keyList.length; i++) {
                String key = keyList[i];
                TextView keyView = (TextView) LayoutInflater.from(mContext).
                        inflate(R.layout.item_history_topic, flHistorySearch, false);
                flHistorySearch.addView(keyView);
                keyView.setTag(key);
                keyView.setText(key);
                keyView.setOnClickListener(this);
            }
            TextView keyView = (TextView) LayoutInflater.from(mContext).
                    inflate(R.layout.item_history_topic_clear, flHistorySearch, false);
            flHistorySearch.addView(keyView);
            keyView.setOnClickListener(this);
        }
    }
}
