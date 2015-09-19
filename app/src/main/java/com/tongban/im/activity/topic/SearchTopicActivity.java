package com.tongban.im.activity.topic;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tongban.corelib.utils.NetUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.SuggestionsBaseActivity;
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
public class SearchTopicActivity extends SuggestionsBaseActivity implements
        View.OnClickListener {


    private TextView tvHistory;
    private FlowLayout flHistorySearch;
    private View llHistoryParent;
    private View vHistoryList;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_search;
    }

    @Override
    protected void initView() {
        super.initView();
        llHistoryParent = findViewById(R.id.ll_history_parent);
        tvHistory = (TextView) findViewById(R.id.tv_hot_category);
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
        mHistoryKeys = SPUtils.get(mContext, Consts.HISTORY_SEARCH_TOPIC, "").toString();
        if (TextUtils.isEmpty(mHistoryKeys)) {
            tvHistory.setVisibility(View.GONE);
            flHistorySearch.setVisibility(View.GONE);
            return;
        }
        setSearchKeyView();
    }

    //设置历史搜索key和控件
    private void setSearchKeyView() {
        String[] keyList = mHistoryKeys.split(";");
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
    protected int getMenuInflate() {
        return R.menu.menu_search_topic;
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
            llHistoryParent.setVisibility(View.VISIBLE);
            searchView.onActionViewExpanded();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!NetUtils.isConnected(mContext)) {
            return false;
        }
        if (!TextUtils.isEmpty(query)) {
            suggestionsListView.setVisibility(View.GONE);
            saveSearchKey(query);

            TopicApi.getInstance().searchTopicList(query, 0, 15, this);
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        //点击搜索历史
        if (v.getId() == R.id.tv_history_key) {
            isShowSuggestions = false;
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
        super.onEventMainThread(obj);
        llHistoryParent.setVisibility(View.GONE);
        vHistoryList.setVisibility(View.VISIBLE);
    }

}
