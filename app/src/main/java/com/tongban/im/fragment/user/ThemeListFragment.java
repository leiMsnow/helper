package com.tongban.im.fragment.user;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.adapter.ThemeListAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;

/**
 * 专题列表页(查询和收藏复用)
 *
 * @author Cheneey
 * @createTime 2015/8/27
 */
public class ThemeListFragment extends BaseToolBarFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, OnLoadMoreListener {
    private LoadMoreListView lvTheme;
    private ThemeListAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 10;
    private int type;

    /**
     * 构造方法
     *
     * @param type 类型 0:搜索结果列表 1:收藏专题列表
     * @return ThemeListFragment
     */
    public static ThemeListFragment newInstance(int type) {
        ThemeListFragment f = new ThemeListFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        f.setArguments(args);
        return f;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_theme_list;
    }

    @Override
    protected void initView() {
        lvTheme = (LoadMoreListView) mView.findViewById(R.id.lv_theme);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(this);
        lvTheme.setOnItemClickListener(this);
        lvTheme.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        mAdapter = new ThemeListAdapter(mContext, R.layout.item_theme_list, null);
        lvTheme.setAdapter(mAdapter);
        lvTheme.setPageSize(mPageSize);
        type = getArguments().getInt("type", 0);
        if (type == 0) {
            // Fragment用于搜索
        } else if (type == 1) {
            // Fragment用于展示收藏的专题
            UserCenterApi.getInstance().fetchCollectMultipleTopicList(mCursor, mPageSize, this);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startThemeDetails(mAdapter.getItem(position).getTheme_id());
    }

    public void searchTheme(String keyword) {
        ProductApi.getInstance().searchTheme(keyword, mCursor, mPageSize, this);
    }

    /**
     * 获取收藏的专题列表Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchCollectedThemeEvent event) {
        mCursor++;
        mAdapter.addAll(event.mThemeList);
        lvTheme.setResultSize(event.mThemeList.size());
        lvTheme.setVisibility(View.VISIBLE);
    }

    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(ProductApi.SEARCH_THEME)) {
            mAdapter.clear();
            lvTheme.setVisibility(View.GONE);
        }
    }

    /**
     * 搜索专题成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchThemeResultEvent event) {
        mAdapter.replaceAll(event.mThemes);
        lvTheme.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadMore() {
        if (type == 0) {
            // Fragment用于搜索
        } else if (type == 1) {
            // Fragment用于展示收藏的专题
            UserCenterApi.getInstance().fetchCollectMultipleTopicList(mCursor, mPageSize, this);
        }
    }
}
