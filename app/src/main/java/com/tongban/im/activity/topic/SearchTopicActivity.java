package com.tongban.im.activity.topic;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.topic.TopicFragment;
import com.tongban.im.model.BaseEvent;

import de.greenrobot.event.EventBus;

/**
 * 搜索话题
 *
 * @author zhangleilei
 * @createTime 2015/8/11
 */
public class SearchTopicActivity extends BaseToolBarActivity implements
        SearchView.OnQueryTextListener, View.OnClickListener {

    //最大历史记录数
    private final static int mKeyCount = 10;

    private SearchView searchView;
    private TextView tvHistory;
    private FlowLayout flHistorySearch;
    private View llHistoryParent;
    private View vHistoryList;

    private String mKeys;

    private int mCursor = 0;
    private int mPageSize = 10;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_search;
    }

    @Override
    protected void initView() {
        llHistoryParent = findViewById(R.id.ll_history_parent);
        tvHistory = (TextView) findViewById(R.id.tv_history);
        flHistorySearch = (FlowLayout) findViewById(R.id.fl_history_search);
        vHistoryList = findViewById(R.id.fl_container);

        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,
                new TopicFragment()).commit();
    }

    @Override
    protected void initData() {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (vHistoryList.getVisibility() == View.VISIBLE) {
            vHistoryList.setVisibility(View.GONE);
            searchView.onActionViewExpanded();
            llHistoryParent.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
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
            BaseEvent.SearchTopicKeyEvent search = new BaseEvent.SearchTopicKeyEvent();
            search.keyword = query;
            EventBus.getDefault().post(search);
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

    public void onEventMainThread(BaseEvent.SearchTopicListEvent obj) {

        llHistoryParent.setVisibility(View.GONE);
        vHistoryList.setVisibility(View.VISIBLE);
        searchView.onActionViewCollapsed();
    }
}
