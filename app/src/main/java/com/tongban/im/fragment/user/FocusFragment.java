package com.tongban.im.fragment.user;


import android.content.Intent;
import android.view.View;
import android.widget.ListView;
/**
 * 关注界面
 * @author fushudi
 */
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.user.UserCenterActivity;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.common.TransferPathPrefix;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

import java.util.List;

public class FocusFragment extends BaseApiFragment implements View.OnClickListener {
    private ListView lvFocusList;
    private UserAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_focus;
    }

    @Override
    protected void initView() {
        lvFocusList = (ListView) mView.findViewById(R.id.lv_focus_list);
    }

    @Override
    protected void initData() {
        mAdapter = new UserAdapter(mContext, R.layout.item_my_info_list, null);
        mAdapter.setOnClickListener(this);
        lvFocusList.setAdapter(mAdapter);
        UserCenterApi.getInstance().fetchFocusUserList(0, 10, this);

    }

    @Override
    protected void initListener() {

    }

    //我的关注Event
    public void onEventMainThread(BaseEvent.MyFollowListEvent obj) {
        mAdapter.replaceAll(obj.getMyFollowList());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_follow:
                String focusId = v.getTag().toString();
                UserCenterApi.getInstance().focusUser(new String[]{focusId}, this);
                break;
            case R.id.iv_user_icon:
                String visitorId = v.getTag(Integer.MAX_VALUE).toString();
                TransferCenter.getInstance().startUserCenter(visitorId);
                break;
        }
    }
}
