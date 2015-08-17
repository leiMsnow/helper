package com.tongban.im.fragment.user;


import android.widget.ListView;
/**
 * 关注界面
 * @author fushudi
 */
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.FansAdapter;
import com.tongban.im.model.User;

import java.util.ArrayList;
import java.util.List;

public class FollowFragment extends BaseApiFragment {
    private ListView lvFollowList;
    private FansAdapter mAdapter;

    private List<User> mFollowList;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_follow;
    }

    @Override
    protected void initView() {
        lvFollowList = (ListView) mView.findViewById(R.id.lv_follow_list);
    }

    @Override
    protected void initData() {
        mFollowList = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            User user = new User();
            user.setPortrait_url("http://h.hiphotos.baidu.com/image/pic/item/34fae6cd7b899e511857c31640a7d933c9950dd2.jpg");
            user.setNick_name("小猪");
            mFollowList.add(user);
        }
        mAdapter = new FansAdapter(mContext, R.layout.item_my_info_list, mFollowList);
        lvFollowList.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {

    }
}
