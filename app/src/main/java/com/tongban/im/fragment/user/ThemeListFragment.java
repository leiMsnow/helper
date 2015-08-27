package com.tongban.im.fragment.user;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.ThemeListAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 专题列表页(查询和收藏复用)
 *
 * @author Cheneey
 * @createTime 2015/8/27
 */
public class ThemeListFragment extends BaseApiFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    private ListView lvTheme;
    private ThemeListAdapter mAdapter;

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
        lvTheme = (ListView) mView.findViewById(R.id.lv_theme);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(this);
        lvTheme.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mAdapter = new ThemeListAdapter(mContext, R.layout.item_theme_list, null);
        lvTheme.setAdapter(mAdapter);
        int type = getArguments().getInt("type", 0);
        if (type == 0) {
            // Fragment用于搜索
        } else if (type == 1) {
            // Fragment用于展示收藏的专题
            UserCenterApi.getInstance().fetchCollectMultipleTopicList(0, 10, this);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startThemeDetails(mAdapter.getItem(position).getTheme_id());
    }

    /**
     * 获取收藏的专题列表
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchCollectedThemeEvent event) {
        mAdapter.replaceAll(event.mThemeList);
    }

    /**
     * 通知Fragment执行搜索
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchThemeAndProductEvent event) {
        ProductApi.getInstance().searchTheme(event.keyword, 0, 15, this);
    }

    /**
     * 搜索专题成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchThemeResultEvent event) {
        if (event.mThemes.size() == 0) {
            ToastUtil.getInstance(mContext).showToast("换个搜索词试试吧~");
        } else {
            mAdapter.replaceAll(event.mThemes);
        }
    }
}
