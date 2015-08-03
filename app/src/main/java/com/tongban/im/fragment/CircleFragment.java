package com.tongban.im.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.ChooseGroupTypeActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.model.Group;

import java.util.LinkedList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

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

    private RadioGroup rg_circle;
    // 聊天,推荐
    private RadioButton rb_chat, rb_recommend;
    private FrameLayout fl_container;

    private FragmentManager fm;
    private ConversationListFragment chatFragment;
    private Fragment recommendFragment;

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
//        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_group);
//        //设置布局管理器
//        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
//        //设置Item增加、移除动画
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        rg_circle = (RadioGroup) mView.findViewById(R.id.rg_circle);
        rb_chat = (RadioButton) mView.findViewById(R.id.rb_chat);
        rb_recommend = (RadioButton) mView.findViewById(R.id.rb_recommend);
        fl_container = (FrameLayout) mView.findViewById(R.id.fl_container);

        fm = getChildFragmentManager();
        chatFragment = ConversationListFragment.getInstance();
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //私聊会话
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false") //群组
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //系统
                .build();
        chatFragment.setUri(uri);
        recommendFragment = new RecommendCircleFragment();
        fm.beginTransaction().add(R.id.fl_container, chatFragment).add(R.id.fl_container, recommendFragment)
                .hide(recommendFragment).commit();
    }

    @Override
    protected void initListener() {
        rg_circle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chat:
                        fm.beginTransaction().show(chatFragment).hide(recommendFragment).commit();
                        break;
                    case R.id.rb_recommend:
                        fm.beginTransaction().show(recommendFragment).hide(chatFragment).commit();
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
//        GroupApi.getInstance().fetchMyAllGroupList(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_circle_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_group) {
            Intent intent = new Intent(mContext, ChooseGroupTypeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}
