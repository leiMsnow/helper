package com.tongban.im.fragment.group;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.viewpager.JellyViewPager;
import com.tongban.im.R;
import com.tongban.im.adapter.GroupDisplayAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseApiFragment {

    private JellyViewPager jellyViewPager;

    private GroupDisplayAdapter mAdapter;

    private boolean mIsFromMain = false;
    private String mKeyword;
    private int mCursor = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recommend_group;
    }

    @Override
    protected void initView() {
        jellyViewPager = (JellyViewPager) mView.findViewById(R.id.vp_group);
    }


    @Override
    protected void initData() {
        if (getArguments() != null)
            mIsFromMain = getArguments().getBoolean(Consts.KEY_IS_MAIN, false);
        if (mIsFromMain) {
            GroupApi.getInstance().recommendGroupList(mCursor, 20, this);
        }
    }

    @Override
    protected void initListener() {
    }

    /**
     * 推荐圈子事件回调
     *
     * @param list
     */
    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        if (mIsFromMain) {
            mAdapter = new GroupDisplayAdapter(getChildFragmentManager(), list.groupList);
            jellyViewPager.setAdapter(mAdapter);
        }
    }

    public void onEventMainThread(ApiErrorResult obj) {
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
            mAdapter = new GroupDisplayAdapter(getChildFragmentManager(), searchGroupEvent.groups);
            jellyViewPager.setAdapter(mAdapter);
        }
    }
}
