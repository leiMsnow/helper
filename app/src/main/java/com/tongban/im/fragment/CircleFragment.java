package com.tongban.im.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.JoinGroupActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.BaseEvent;
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
     * 圈子数据
     */
    private List<Group> groups;

    private GroupListAdapter adapter;

    private AlertDialog dialog_join_group, dialog_create_group;

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
                adapter.setOnItemClickLitener(new GroupListAdapter.OnItemClickLitener() {
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

    /**
     * 加入圈子成功的回调
     *
     * @param event JoinGroupEvent
     */
    public void onEventMainThread(BaseEvent.JoinGroupEvent event) {
        ToastUtil.getInstance(mContext).showToast("加入圈子成功");
        GroupApi.getInstance().fetchPersonalGroupList(this);
    }

    /**
     * 创建圈子成功的回调
     *
     * @param group
     */
    public void onEventMainThread(Group group) {
        ToastUtil.getInstance(mContext).showToast("创建圈子成功");
        GroupApi.getInstance().fetchPersonalGroupList(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_group) {
            if (dialog_create_group == null) {
                View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_input_group, null);
                final EditText et = (EditText) layout.findViewById(R.id.et_group);
                et.setHint("请输入圈子名称");
                dialog_create_group = new AlertDialog.Builder(mContext).setTitle("创建圈子")
                        .setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String group_name = et.getText().toString().trim();
                                GroupApi.getInstance().createGroup(group_name, CircleFragment.this);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
            }
            dialog_create_group.show();
            return true;
        } else if (item.getItemId() == R.id.action_join_group) {
//            if (dialog_join_group == null) {
//                View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_input_group, null);
//                final EditText et = (EditText) layout.findViewById(R.id.et_group);
//                et.setHint("请输入圈子id");
//                dialog_join_group = new AlertDialog.Builder(mContext).setTitle("加入圈子")
//                        .setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String group_id = et.getText().toString().trim();
//                                if (TextUtils.isEmpty(group_id)) {
//                                    ToastUtil.getInstance(mContext).showToast("圈子id不能为空");
//                                } else {
//                                    GroupApi.getInstance().joinGroup(group_id, CircleFragment.this);
//                                    dialog.dismiss();
//                                }
//                            }
//                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create();
//            }
//            dialog_join_group.show();
            Intent intent = new Intent(mContext, JoinGroupActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
