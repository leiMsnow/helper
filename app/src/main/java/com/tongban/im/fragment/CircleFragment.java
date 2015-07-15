package com.tongban.im.fragment;


import android.content.DialogInterface;
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
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.CircleListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.List;

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

    private CircleListAdapter adapter;

    private AlertDialog dialog_join_group;

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
        LogUtil.d("initData", "请求接口");
        GroupApi.getInstance().fetchPersonalGroupList("android02", this);
    }

    public void onEventMainThread(List<Group> list) {
        LogUtil.d("onEventMainThread", "进来了");
        if (list.size() > 0) {
            LogUtil.d("onEventMainThread", "有数据");
            //设置adapter
            if (adapter == null) {
                groups.addAll(list);
                adapter = new CircleListAdapter(mContext, groups);
                recyclerView.setAdapter(adapter);
            } else {
                groups.clear();
                groups.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void onEventMainThread(BaseEvent.JoinGroupEvent event) {
        ToastUtil.getInstance(mContext).showToast("加入群组成功");
        GroupApi.getInstance().fetchPersonalGroupList("android02", this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LogUtil.d("onCreateOptionsMenu", "Fragment收到menu回调");
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogUtil.d("onOptionsItemSelected", "CircleFragment收到menu选择回调");
        if (item.getItemId()==R.id.action_create_group){
            //RongIM.getInstance().startGroupChat(mContext,"group01","");
        } else if (item.getItemId()==R.id.action_join_group){
            LogUtil.d("onOptionsItemSelected", "加入群组");
            if (dialog_join_group == null) {
                View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_input_group_id , null);
                final EditText et = (EditText) layout.findViewById(R.id.et_group_id);
                dialog_join_group = new AlertDialog.Builder(mContext).setTitle("加入群组")
                        .setView(layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String group_id = et.getText().toString().trim();
                                if (TextUtils.isEmpty(group_id)) {
                                    ToastUtil.getInstance(mContext).showToast("群组id不能为空");
                                } else {
                                    GroupApi.getInstance().joinGroup(group_id, "android02", CircleFragment.this);
                                    dialog.dismiss();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
            }
            dialog_join_group.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
