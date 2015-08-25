package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 关注界面
 *
 * @author fushudi
 */
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

    /**
     * 我的关注Event
     */
    public void onEventMainThread(BaseEvent.MyFollowListEvent obj) {
        mAdapter.replaceAll(obj.getMyFollowList());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消关注
            case R.id.btn_follow:
                String focusId = v.getTag().toString();
                UserCenterApi.getInstance().focusUser(false,new String[]{focusId}, this);
                break;
            //跳到用户中心（他人）
            case R.id.iv_user_icon:
                String visitorId = v.getTag(Integer.MAX_VALUE).toString();
                TransferCenter.getInstance().startUserCenter(visitorId);
                break;
        }
    }
}
