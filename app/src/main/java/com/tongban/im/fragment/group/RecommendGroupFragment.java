package com.tongban.im.fragment.group;

import android.view.View;

import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.header.RentalsSunHeaderView;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.utils.PTRHeaderUtils;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import io.rong.imkit.RongIM;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseToolBarFragment implements PtrHandler,
        OnLoadMoreListener {

    private PtrFrameLayout ptrFrameLayout;
    private LoadMoreListView lvGroupList;

    private GroupListAdapter mAdapter;

    private boolean mIsMainEvent = false;
    private String mKeyword;
    private int mCursor = 0;
    private int mPageSize = 20;
    //是否是下拉刷新操作
    private boolean mIsPull = false;

    public void setKeyword(String mKeyword) {
        this.mKeyword = mKeyword;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recommend_group;
    }

    @Override
    protected void initView() {
        ptrFrameLayout = (PtrFrameLayout) mView.findViewById(R.id.fragment_ptr_home_ptr_frame);
        lvGroupList = (LoadMoreListView) mView.findViewById(R.id.lv_group_list);
    }

    @Override
    protected void initData() {
        if (getArguments() != null)
            mIsMainEvent = getArguments().getBoolean(Consts.KEY_IS_MAIN, false);
        if (mIsMainEvent) {
            PTRHeaderUtils.getMaterialView(mContext, ptrFrameLayout);
            ptrFrameLayout.setPtrHandler(this);
            onRequest();
        }
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        mAdapter.setDisplayModel(false);
        lvGroupList.setAdapter(mAdapter);
        lvGroupList.setPageSize(mPageSize);

    }

    @Override
    protected void initListener() {
        setRequestApiListener(this);
        lvGroupList.setOnLoadMoreListener(this);
        mAdapter.setOnClickListener(new GroupListenerImpl(mContext));
    }

    /**
     * 推荐圈子事件回调
     *
     * @param list
     */
    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        if (mIsPull) {
            ptrFrameLayout.refreshComplete();
            lvGroupList.resetLoad();
            mIsPull = false;
            mAdapter.clear();
        }
        lvGroupList.setResultSize(list.groupList.size());
        mAdapter.addAll(list.groupList);
        lvGroupList.setVisibility(View.VISIBLE);
    }

    public void onEventMainThread(ApiErrorResult obj) {
        if (mIsMainEvent) {
            ptrFrameLayout.refreshComplete();
            if (mAdapter != null && mAdapter.getCount() > 0) {
                hideEmptyView();
            }
        } else {
            if (mAdapter != null) {
                mAdapter.clear();
                lvGroupList.setVisibility(View.GONE);
            }
        }
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
            if (mIsMainEvent) {
                mIsPull = true;
                GroupApi.getInstance().recommendGroupList(mCursor, mAdapter.getCount(), this);
            } else {
                GroupApi.getInstance().searchGroupList(mKeyword, mCursor, mAdapter.getCount(), this);
            }
        }
    }

    /**
     * 搜索圈子成功的事件
     *
     * @param searchGroupEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupListEvent searchGroupEvent) {
        if (!mIsMainEvent) {
            mAdapter.replaceAll(searchGroupEvent.groups);
            lvGroupList.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view1) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frameLayout) {
        mIsPull = true;
        mCursor = 0;
        GroupApi.getInstance().recommendGroupList(mCursor, mPageSize, this);
    }

    @Override
    public void onRequest() {
        ptrFrameLayout.autoRefresh(true);
    }

    @Override
    public void onLoadMore() {
        mCursor++;
        if (mIsMainEvent)
            GroupApi.getInstance().recommendGroupList(mCursor, mPageSize, this);
    }
}
