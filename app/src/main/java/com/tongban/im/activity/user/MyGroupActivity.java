package com.tongban.im.activity.user;


import android.net.Uri;

import com.tb.api.UserCenterApi;
import com.tb.api.model.BaseEvent;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.impl.GroupListenerImpl;

import butterknife.Bind;

/**
 * 个人中心（我的圈子）
 *
 * @author fushudi
 */
public class MyGroupActivity extends AppBaseActivity implements
        OnLoadMoreListener {
    @Bind(R.id.lv_my_group_list)
    LoadMoreListView lvMyGroupList;

    private GroupListAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 10;
    private String mUserId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initData() {

        setTitle(R.string.group);

        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        lvMyGroupList.setAdapter(mAdapter);
        lvMyGroupList.setPageSize(mPageSize);

        if (getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            mUserId = uri.getQueryParameter(Consts.USER_ID);
            UserCenterApi.getInstance().fetchMyGroupsList(mCursor, mPageSize, mUserId, this);
        }

        lvMyGroupList.setOnLoadMoreListener(this);
        if (SPUtils.get(mContext, Consts.USER_ID, "").equals(mUserId)) {
            mAdapter.setOnClickListener(new GroupListenerImpl(mContext));
        }
    }

    /**
     * 获取我加入的圈子列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.MyGroupListEvent obj) {
        mCursor++;
        mAdapter.addAll(obj.myGroupList);
        lvMyGroupList.setResultSize(obj.myGroupList.size());
    }


    @Override
    public void onLoadMore() {
        UserCenterApi.getInstance().fetchMyGroupsList(mCursor, mPageSize, mUserId, this);
    }
}
