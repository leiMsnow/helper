package com.tongban.im.fragment.group;

import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import io.rong.imkit.RongIM;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseApiFragment {

    private ListView lvGroupList;

    private GroupListAdapter mAdapter;

    private boolean mIsFromMain = false;
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
        if (mIsFromMain) {
            GroupApi.getInstance().recommendGroupList(mCursor, 15, this);
            mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
            mAdapter.setOnClickListener(new GroupListenerImpl(mContext));
            mAdapter.setDisplayModel(false);
            lvGroupList.setAdapter(mAdapter);
        }
    }

    /**
     * 推荐圈子事件回调
     *
     * @param list
     */
    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        if (list.isMainEvent) {
            mAdapter.replaceAll(list.groupList);
            lvGroupList.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 加入群组成功的事件回调
     *
     * @param joinGroupEvent
     */
    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        RongIM.getInstance().startGroupChat(mContext, joinGroupEvent.getGroup_id(),
                joinGroupEvent.getGroup_name());
        GroupApi.getInstance().recommendGroupList(mCursor, mAdapter.getCount(), this);
    }

    /**
     * 搜索keyword事件回调
     *
     * @param keyEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupKeyEvent keyEvent) {
        GroupApi.getInstance().searchGroupList(keyEvent.keyword, 0, 15, this);
    }

    /**
     * 搜索群组成功的事件
     *
     * @param searchGroupEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupListEvent searchGroupEvent) {
        if (searchGroupEvent.isSearchEvent) {
            mAdapter.replaceAll(searchGroupEvent.groups);
            lvGroupList.setVisibility(View.VISIBLE);
        }
    }
}
