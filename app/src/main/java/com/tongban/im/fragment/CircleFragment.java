package com.tongban.im.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.JoinGroupActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.Group;

import java.util.LinkedList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 圈子页
 * author: chenenyu 15/7/13
 */
public class CircleFragment extends BaseApiFragment {
    /**
     * 圈子列表
     */
    private RecyclerView recyclerView;

    /**
     * 用户加入的群组数据
     */
    private List<Group> groups;

    private GroupListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_group);
        //设置布局管理器
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        GroupApi.getInstance().fetchPersonalGroupList(this);
    }

    /**
     * 获取圈子(个人群组列表)数据的回调
     *
     * @param list List<Group>
     */
    public void onEventMainThread(List<Group> list) {
        if (list.size() > 0) {
            //设置adapter
            if (adapter == null) {
                groups = new LinkedList<>();
                groups.addAll(list);
                adapter = new GroupListAdapter(mContext, groups);
                // 点击监听
                adapter.setOnItemClickListener(new GroupListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String group_id = groups.get(position).getGroup_id();
                        String group_name = groups.get(position).getGroup_name();
                        RongIM.getInstance().startGroupChat(mContext, group_id, group_name);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                groups.clear();
                groups.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_circle_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_join_group) {
            Intent intent = new Intent(mContext, JoinGroupActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
