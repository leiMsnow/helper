package com.tongban.im.activity.topic;

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
import com.tongban.im.adapter.TopicAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;

/**
 * 搜索话题
 *
 * @author zhangleilei
 * @createTime 2015/8/11
 */
public class SearchTopicActivity extends BaseToolBarActivity implements SearchView.OnQueryTextListener,
        View.OnClickListener {

    private SearchView searchView;
    private TextView tvHistory;
    private FlowLayout flHistorySearch;
    private ListView lvTopicList;

    private TopicAdapter mAdapter;

    private String mKeys;
    private final int mKeyCount = 10;

    private int mCursor = 0;
    private int mPageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_search;
    }

    @Override
    protected void initView() {
        tvHistory = (TextView) findViewById(R.id.tv_history);
        flHistorySearch = (FlowLayout) findViewById(R.id.fl_history_search);
        lvTopicList = (ListView) findViewById(R.id.lv_topic_list);
    }

    @Override
    protected void initData() {

        mAdapter = new TopicAdapter(mContext, R.layout.item_topic_list, null);
        lvTopicList.setAdapter(mAdapter);
        //初始化
        initHistoryKey();
    }

    //初始化历史搜索key
    private void initHistoryKey() {
        //从SP中查找历史搜索记录
        mKeys = SPUtils.get(mContext, Consts.HISTORY_SEARCH_TOPIC, "").toString();
        if (TextUtils.isEmpty(mKeys)) {
            tvHistory.setVisibility(View.GONE);
            flHistorySearch.setVisibility(View.GONE);
            return;
        }
        setSearchKeyView();
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

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_topic, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("搜索话题关键字");
        searchView.onActionViewExpanded();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            if (query.contains(";"))
                query = query.replace(";", "");
            saveSearchKey(query);
            TopicApi.getInstance().searchTopicList(query, mCursor, mPageSize, this);
        }
        return false;
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


    public void onEventMainThread(BaseEvent.TopicListEvent topicListEvent) {
        mCursor++;
        lvTopicList.setVisibility(View.VISIBLE);
        mAdapter.replaceAll(topicListEvent.getTopicList());
    }
}
