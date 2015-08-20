package com.tongban.im.fragment.user;


import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.FansAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝界面
 * @author fushudi
 */
public class FansFragment extends BaseApiFragment {
    private ListView lvFansList;
    private FansAdapter mAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_fans;
    }

    @Override
    protected void initView() {
        lvFansList = (ListView) mView.findViewById(R.id.lv_fans_list);
    }

    @Override
    protected void initData() {
//        mFansList = new ArrayList<>();
//        for (int i = 0; i < 17; i++) {
//            User user = new User();
//            user.setPortrait_url("http://h.hiphotos.baidu.com/image/pic/item/34fae6cd7b899e511857c31640a7d933c9950dd2.jpg");
//            user.setNick_name("小狗");
//            mFansList.add(user);
//        }
        UserCenterApi.getInstance().fetchFansUserList(0,10,this);
    }

    @Override
    protected void initListener() {

    }

    public void onEventMainThread(List<User> mFansList) {

        mAdapter = new FansAdapter(mContext, R.layout.item_my_info_list, mFansList);
        lvFansList.setAdapter(mAdapter);
    }
}
