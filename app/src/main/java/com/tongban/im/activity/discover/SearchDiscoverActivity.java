package com.tongban.im.activity.discover;

import android.content.Intent;
import android.os.Bundle;
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
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Tag;
import com.tongban.im.model.TagType;

import java.util.LinkedList;
import java.util.List;

/**
 * 搜索首页
 *
 * @author zhangleilei
 * @createTime 2015/8/12
 */
public class SearchDiscoverActivity extends SuggestionsBaseActivity  {

    private ExpandableListView mTagListView;
    private List<Tag> mBooks, mToys, mChildEdus;
    private DiscoverTagListAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_discover_search;
    }

    @Override
    protected void initView() {
        super.initView();
        mTagListView = (ExpandableListView) findViewById(R.id.elv_tags);
    }

    @Override
    protected void initData() {
        // 获取商品相关的标签
        ProductApi.getInstance().fetchTags(0, 12, TagType.PRODUCT_TAG, this);
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
            Intent intent = new Intent(mContext, SearchResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Consts.KEY_SEARCH_VALUE, query);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return false;
    }

    /**
     * 获取搜索标签成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchTags event) {
        mBooks = new LinkedList<>();
        mToys = new LinkedList<>();
        mChildEdus = new LinkedList<>();
        if ("5".equals(event.type) && event.tags != null && event.tags.size() > 0) {
            for (Tag tag : event.tags) {
                if ("1".equals(tag.getTag_subtype())) {
                    mBooks.add(tag);
                } else if ("2".equals(tag.getTag_subtype())) {
                    mToys.add(tag);
                } else if ("3".equals(tag.getTag_subtype())) {
                    mChildEdus.add(tag);
                }
            }
            mAdapter = new DiscoverTagListAdapter(mContext, mBooks, mToys, mChildEdus);
            mTagListView.setAdapter(mAdapter);
            // 默认展开
            for (int i = 0; i < mAdapter.getGroupCount(); i++) {
                mTagListView.expandGroup(i);
            }
        }
    }
}
