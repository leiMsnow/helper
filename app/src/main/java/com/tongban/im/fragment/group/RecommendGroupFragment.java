package com.tongban.im.fragment.group;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import io.rong.imkit.RongIM;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendGroupFragment extends BaseApiFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private ListView lvGroupList;

    private GroupListAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recommend_circle;
    }

    @Override
    protected void initView() {
        lvGroupList = (ListView) mView.findViewById(R.id.lv_group_list);
    }

    @Override
    protected void initListener() {
        lvGroupList.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

        GroupApi.getInstance().recommendGroupList(0, 10, this);

        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        mAdapter.setOnClickListener(this);
        mAdapter.setDisplayModel(false);
        lvGroupList.setAdapter(mAdapter);
    }

    public void onEventMainThread(BaseEvent.RecommendGroupListEvent list) {
        mAdapter.replaceAll(list.getGroupList());
        lvGroupList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_join:
                Group group = (Group) v.getTag();
                // TODO 加入群，不需要验证
                GroupApi.getInstance().joinGroup(group.getGroup_id(), group.getGroup_name(),
                        group.getUser_info().getUser_id(), this);
                break;
        }
    }

    /**
     * 加入群组成功的事件回调
     *
     * @param joinGroupEvent
     */
    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        ToastUtil.getInstance(mContext).showToast(joinGroupEvent.getMessage());
        GroupApi.getInstance().recommendGroupList(0, 10, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.getItem(position).isAllow_add()) {
            RongIM.getInstance().startGroupChat(mContext, mAdapter.getItem(position).getGroup_id(),
                    mAdapter.getItem(position).getGroup_name());
        }
    }
}
