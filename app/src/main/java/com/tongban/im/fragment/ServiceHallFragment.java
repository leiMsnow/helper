package com.tongban.im.fragment;


import android.content.Intent;
import android.view.View;

import com.tb.api.GroupApi;
import com.tb.api.model.BaseEvent;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.ServiceDetailsActivity;
import com.tongban.im.adapter.ServiceHallListAdapter;
import com.tongban.im.fragment.base.AppBaseFragment;
import com.tongban.im.impl.GroupListenerImpl;
import com.tongban.im.utils.PTRHeaderUtils;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.rong.imkit.RongIM;

/**
 * 服务大厅
 * author: zhangleilei 15/11/09
 */
public class ServiceHallFragment extends AppBaseFragment implements
        PtrHandler
        , OnLoadMoreListener {

    @Bind(R.id.lv_service_hall_list)
    LoadMoreListView lvServiceHallList;
    @Bind(R.id.fragment_ptr_home_ptr_frame)
    PtrFrameLayout ptrFrameLayout;

    private ServiceHallListAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 20;
    //是否是下拉刷新操作
    private boolean mIsPull = false;

    /**
     * 实例化服务大厅fragment
     *
     * @return
     */
    public static ServiceHallFragment getInstance() {
        return new ServiceHallFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_service_hall;
    }

    @Override
    protected void initData() {

        PTRHeaderUtils.getMaterialView(mContext, ptrFrameLayout);
        ptrFrameLayout.setPtrHandler(this);
        ptrFrameLayout.autoRefresh(true);

        mAdapter = new ServiceHallListAdapter(mContext, R.layout.item_service_hall, null);
        lvServiceHallList.setAdapter(mAdapter);
        lvServiceHallList.setPageSize(mPageSize);
        lvServiceHallList.setOnLoadMoreListener(this);
        mAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.rl_service_parent)
                    startActivity(new Intent(mContext, ServiceDetailsActivity.class));
            }
        });
    }

    /**
     * 事件回调
     *
     * @param list
     */
    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        if (mIsPull) {
            ptrFrameLayout.refreshComplete();
            lvServiceHallList.resetLoad();
            mIsPull = false;
            mAdapter.clear();
        }
        lvServiceHallList.setResultSize(list.groupList.size());
        mAdapter.addAll(list.groupList);
        lvServiceHallList.setVisibility(View.VISIBLE);
    }

    public void onEventMainThread(ApiErrorResult obj) {
        ptrFrameLayout.refreshComplete();
        if (mAdapter != null && mAdapter.getCount() > 0) {
            hideEmptyView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideEmptyView();
    }

    /**
     * 加入群组成功的事件回调
     *
     * @param joinGroupEvent
     */
    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        if (joinGroupEvent.is_verify) {
            ToastUtil.getInstance(mContext).showToast("已申请,等圈主确认后方可加入");
        } else {
            ToastUtil.getInstance(mContext).showToast("加入成功");
            RongIM.getInstance().startGroupChat(mContext, joinGroupEvent.group_id,
                    joinGroupEvent.group_name);
            mIsPull = true;
            GroupApi.getInstance().recommendGroupList(mCursor, mAdapter.getCount(), this);
        }
    }

    /**
     * 搜索圈子成功的事件
     *
     * @param searchGroupEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupListEvent searchGroupEvent) {
        mAdapter.replaceAll(searchGroupEvent.groups);
        lvServiceHallList.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frameLayout) {
        onRequest();
    }

    @Override
    public void onRequest() {
        mIsPull = true;
        mCursor = 0;
        GroupApi.getInstance().recommendGroupList(mCursor, mPageSize, this);
    }

    @Override
    public void onLoadMore() {
        mCursor++;
        GroupApi.getInstance().recommendGroupList(mCursor, mPageSize, this);
    }

}
