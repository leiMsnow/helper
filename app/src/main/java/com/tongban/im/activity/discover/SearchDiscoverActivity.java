package com.tongban.im.activity.discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ExpandableListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.base.SuggestionsBaseActivity;
import com.tongban.im.adapter.DiscoverTagListAdapter;
import com.tongban.im.api.CommonApi;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Tag;
import com.tongban.im.model.TagType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 搜索首页
 *
 * @author zhangleilei
 * @createTime 2015/8/12
 */
public class SearchDiscoverActivity extends SuggestionsBaseActivity {

    private ExpandableListView mTagListView;
    private DiscoverTagListAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_discover_search;
    }

    @Override
    protected void initView() {
        super.initView();
        isExpanded = false;
        mTagListView = (ExpandableListView) findViewById(R.id.elv_tags);
    }

    @Override
    protected void initData() {
        // 获取商品相关的标签
        CommonApi.getInstance().fetchTags(0, 12, TagType.PRODUCT_TAG, this);
    }


    @Override
    protected void initListener() {
        super.initListener();
        // 禁用折叠
        mTagListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

    }

    @Override
    protected int getMenuInflate() {
        return R.menu.menu_search_topic;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            suggestionsListView.setVisibility(View.GONE);
            TransferCenter.getInstance().startThemeSearchResult(true,query);
        }
        return false;
    }

    /**
     * 获取搜索标签成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchTags event) {
        Map<String, List<Tag>> datas = new HashMap<>();
        String[] type = {"童书", "玩具", "早教"};
        if ("5".equals(event.type) && event.tags != null && event.tags.size() > 0) {
            for (String key : type) {
                datas.put(key, new LinkedList<Tag>());
            }
            for (Tag tag : event.tags) {
                if ("1".equals(tag.getTag_subtype())) {
                    datas.get(type[0]).add(tag);
                } else if ("2".equals(tag.getTag_subtype())) {
                    datas.get(type[1]).add(tag);
                } else if ("3".equals(tag.getTag_subtype())) {
                    datas.get(type[2]).add(tag);
                }
            }
            mAdapter = new DiscoverTagListAdapter(mContext, datas, type);
            mTagListView.setAdapter(mAdapter);
            // 默认展开
            for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                mTagListView.expandGroup(i);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }
}