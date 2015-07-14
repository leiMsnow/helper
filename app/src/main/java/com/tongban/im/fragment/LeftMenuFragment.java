package com.tongban.im.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.im.corelib.model.DrawerLayoutMenu;
import com.tongban.corelib.base.fragment.BaseUIFragment;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.LeftMenuAdapter;


import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imlib.model.Conversation;

/**
 * 左侧菜单fragment
 */
public class LeftMenuFragment extends BaseUIFragment implements AbsListView.OnItemClickListener {

    private final static int MENU_ITEM_SIZE = 3;
    private List<DrawerLayoutMenu> mItems = new ArrayList<DrawerLayoutMenu>();
    private ListView mListView;
    private LeftMenuAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_leftmenu_list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtil.d("onItemClick:" + mAdapter.getItem(position));
        EventBus.getDefault().post(mAdapter.getItem(position));
        mAdapter.setSelected(position);
    }


    @Override
    protected void initView() {
        mListView = (ListView) mView.findViewById(android.R.id.list);
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        DrawerLayoutMenu menuItem1 = new DrawerLayoutMenu(getResources().getStringArray(R.array.array_left_menu)[0], "android02", Conversation.ConversationType.PRIVATE, R.mipmap.ic_launcher, true);
        mItems.add(menuItem1);
        DrawerLayoutMenu menuItem2 = new DrawerLayoutMenu(getResources().getStringArray(R.array.array_left_menu)[1], "group1", Conversation.ConversationType.GROUP, R.mipmap.ic_launcher, false);
        mItems.add(menuItem2);
        DrawerLayoutMenu menuItem3 = new DrawerLayoutMenu(getResources().getStringArray(R.array.array_left_menu)[2], "group1", Conversation.ConversationType.SYSTEM, R.mipmap.ic_launcher, false);
        mItems.add(menuItem3);

        mAdapter = new LeftMenuAdapter(mContext, R.layout.item_left_menu, mItems);
        mListView.setAdapter(mAdapter);
    }
}
