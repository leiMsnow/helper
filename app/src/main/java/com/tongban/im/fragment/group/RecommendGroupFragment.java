package com.tongban.im.fragment.group;

import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseApiFragment {

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
        lvGroupList = (ListView) mView.findViewById(R.id.lv_group_list);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (getArguments() != null)
            mIsFromMain = getArguments().getBoolean(Consts.KEY_IS_MAIN, false);
        if (mIsFromMain) {
            GroupApi.getInstance().recommendGroupList(mCursor, 15, this);
        }
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        mAdapter.setOnClickListener(new GroupListenerImpl(mContext));
        mAdapter.setDisplayModel(false);
        lvGroupList.setAdapter(mAdapter);
    }

    /**
     * 推荐圈子事件回调
     *
     * @param list
     */
    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        mAdapter.replaceAll(list.groupList);
        lvGroupList.setVisibility(View.VISIBLE);
    }


    /**
     * 加入群组成功的事件回调
     *
     * @param joinGroupEvent
     */
    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        RongIM.getInstance().startGroupChat(mContext, joinGroupEvent.group_id,
                joinGroupEvent.group_name);
        if (mIsFromMain) {
            GroupApi.getInstance().recommendGroupList(mCursor, mAdapter.getCount(), this);
        } else {
            GroupApi.getInstance().searchGroupList(mKeyword, mCursor, mAdapter.getCount(), this);
        }
    }

    /**
     * 搜索keyword事件回调
     *
     * @param keyEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupKeyEvent keyEvent) {
        mKeyword = keyEvent.keyword;
        GroupApi.getInstance().searchGroupList(mKeyword, mCursor, 15, this);
    }

    /**
     * 搜索圈子成功的事件
     *
     * @param searchGroupEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupListEvent searchGroupEvent) {
        mAdapter.replaceAll(searchGroupEvent.groups);
        lvGroupList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }
}
