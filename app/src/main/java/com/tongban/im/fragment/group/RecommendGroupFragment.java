package com.tongban.im.fragment.group;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.group.ChooseGroupTypeActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.BaseEvent;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseApiFragment {
    private ListView lvGroupList;
    private FloatingActionButton mFab;

    private GroupListAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recommend_circle;
    }

    @Override
    protected void initView() {
        lvGroupList = (ListView) mView.findViewById(R.id.lv_group_list);
        mFab = (FloatingActionButton) mView.findViewById(R.id.fab_add);
    }

    @Override
    protected void initListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChooseGroupTypeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        mAdapter.setDisplayModel(false);
        lvGroupList.setAdapter(mAdapter);
        GroupApi.getInstance().recommendGroupList(0, 10, this);
    }

    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        mAdapter.replaceAll(list.getGroupList());
        lvGroupList.setVisibility(View.VISIBLE);
    }
}
