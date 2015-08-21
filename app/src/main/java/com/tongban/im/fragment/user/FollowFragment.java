package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.ListView;
/**
 * 关注界面
 * @author fushudi
 */
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.User;

import java.util.List;

public class FollowFragment extends BaseApiFragment implements View.OnClickListener {
    private ListView lvFollowList;
    private UserAdapter mAdapter;

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
//        for (int i = 0; i < 17; i++) {
//            User user = new User();
//            user.setPortrait_url("http://h.hiphotos.baidu.com/image/pic/item/34fae6cd7b899e511857c31640a7d933c9950dd2.jpg");
//            user.setNick_name("小猪");
//            mFollowList.add(user);
//        }
        mAdapter = new UserAdapter(mContext, R.layout.item_my_info_list, null);
        mAdapter.setOnClickListener(this);
        lvFollowList.setAdapter(mAdapter);
        UserCenterApi.getInstance().fetchFocusUserList(0, 10, this);

    }

    @Override
    protected void initListener() {

    }

    public void onEventMainThread(List<User> mFollowList) {
        mAdapter.replaceAll(mFollowList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_follow:
//                String [] focusId=new String[]{v.getTag().toString()};
                String focusId = v.getTag().toString();
                UserCenterApi.getInstance().focusUser(new String[]{focusId}, this);
                break;
        }
    }
}
