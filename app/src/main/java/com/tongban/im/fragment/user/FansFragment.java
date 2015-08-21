package com.tongban.im.fragment.user;


import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.User;

import java.util.List;

/**
 * 粉丝界面
 *
 * @author fushudi
 */
public class FansFragment extends BaseApiFragment {
    private ListView lvFansList;
    private UserAdapter mAdapter;


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
        mAdapter = new UserAdapter(mContext, R.layout.item_my_info_list, null);
        lvFansList.setAdapter(mAdapter);
        UserCenterApi.getInstance().fetchFansUserList(0, 10, this);
    }

    @Override
    protected void initListener() {

    }

    public void onEventMainThread(List<User> mFansList) {
        mAdapter.replaceAll(mFansList);

    }
}
