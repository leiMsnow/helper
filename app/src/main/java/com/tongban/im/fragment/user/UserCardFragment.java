package com.tongban.im.fragment.user;


import android.net.Uri;
import android.view.View;

import com.tb.api.UserCenterApi;
import com.tb.api.model.BaseEvent;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * 个人中心（他人）界面
 *
 * @author fushudi
 */
public class UserCardFragment extends UserBaseFragment {
    @Override
    protected void initData() {
        super.initData();
        if (getActivity().getIntent() != null) {
            Uri uri = getActivity().getIntent().getData();
            String visitorId = uri.getQueryParameter("visitorId");
            UserCenterApi.getInstance().fetchUserCenterInfo(visitorId, this);
        }

        ivFocus.setOnClickListener(this);
        ivCancelFocus.setOnClickListener(this);
        ivPrivateChat.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 用户中心（他人）Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.UserCenterEvent obj) {
        setDataInfo(obj.user);
        ivFocus.setVisibility(View.VISIBLE);
        if (mUserInfo.is_focused()) {
            ivPrivateChat.setVisibility(View.VISIBLE);
            ivCancelFocus.setVisibility(View.VISIBLE);
            ivFocus.setVisibility(View.GONE);
        } else {
            ivPrivateChat.setVisibility(View.GONE);
            ivCancelFocus.setVisibility(View.GONE);
            ivFocus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        //关注
        if (v == ivFocus) {
            UserCenterApi.getInstance().focusUser(true, mUserInfo.getUser_id(), this);
        }
        //取消关注
        else if (v == ivCancelFocus) {
            UserCenterApi.getInstance().focusUser(false, mUserInfo.getUser_id(), this);
        }
        //私聊
        else if (v == ivPrivateChat) {
            RongIM.getInstance().startPrivateChat(mContext, mUserInfo.getUser_id(),
                    mUserInfo.getNick_name());
        } else {
            super.onClick(v);
        }
    }

    /**
     * 关注/取消关注事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {

        if (obj.isFocus) {
            ivPrivateChat.setVisibility(View.VISIBLE);
            ivCancelFocus.setVisibility(View.VISIBLE);
            ivFocus.setVisibility(View.GONE);
        } else {
            ivPrivateChat.setVisibility(View.GONE);
            ivCancelFocus.setVisibility(View.GONE);
            ivFocus.setVisibility(View.VISIBLE);
        }
    }
}
