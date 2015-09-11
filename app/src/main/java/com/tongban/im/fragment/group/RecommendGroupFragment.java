package com.tongban.im.fragment.group;

import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.model.BaseEvent;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import io.rong.imkit.RongIM;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseApiFragment implements PtrHandler {

    private PtrFrameLayout ptrFrameLayout;
    private ListView lvGroupList;

    private GroupListAdapter mAdapter;

    private boolean mIsFromMain = false;
    private String mKeyword;
    private int mCursor = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recommend_group;
    }

    @Override
    protected void initView() {
        ptrFrameLayout = (PtrFrameLayout) mView.findViewById(R.id.fragment_ptr_home_ptr_frame);
        lvGroupList = (ListView) mView.findViewById(R.id.lv_group_list);
    }


    @Override
    protected void initData() {
        if (getArguments() != null)
            mIsFromMain = getArguments().getBoolean(Consts.KEY_IS_MAIN, false);
        if (mIsFromMain) {
            StoreHouseHeader header = new StoreHouseHeader(mContext);
            header.setTextColor(R.color.main_brown);
            header.setPadding(DensityUtils.dp2px(mContext, 16), DensityUtils.dp2px(mContext, 16),
                    DensityUtils.dp2px(mContext, 16), 0);
//            header.initWithString("Recommend...", DensityUtils.dp2px(mContext, 10));
            header.initWithPointList(getPointList());
            ptrFrameLayout.setHeaderView(header);
            ptrFrameLayout.addPtrUIHandler(header);
            ptrFrameLayout.setPtrHandler(this);
            ptrFrameLayout.autoRefresh();
        }
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        mAdapter.setDisplayModel(false);
        lvGroupList.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(new GroupListenerImpl(mContext));
    }

    /**
     * 推荐圈子事件回调
     *
     * @param list
     */
    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        if (mIsFromMain) {
            ptrFrameLayout.refreshComplete();
            mAdapter.replaceAll(list.groupList);
            lvGroupList.setVisibility(View.VISIBLE);
        }
    }

    public void onEventMainThread(ApiErrorResult obj) {
        ptrFrameLayout.refreshComplete();
        if (mAdapter != null) {
            if (mAdapter.getCount() > 0) {
                showEmptyText("", false);
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
            if (mIsFromMain) {
                GroupApi.getInstance().recommendGroupList(mCursor, mAdapter.getCount(), this);
            } else {
                GroupApi.getInstance().searchGroupList(mKeyword, mCursor, mAdapter.getCount(), this);
            }
        }
    }

    /**
     * 搜索keyword事件回调
     *
     * @param keyEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupKeyEvent keyEvent) {
        if (!mIsFromMain) {
            mCursor = 0;
            mKeyword = keyEvent.keyword;
            GroupApi.getInstance().searchGroupList(mKeyword, mCursor, 15, this);
        }
    }

    /**
     * 搜索圈子成功的事件
     *
     * @param searchGroupEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupListEvent searchGroupEvent) {
        if (!mIsFromMain) {
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
        GroupApi.getInstance().recommendGroupList(mCursor, 20, this);
    }
}
