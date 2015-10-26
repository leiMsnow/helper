package com.tongban.im.activity.user;


import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tongban.im.R;
import com.tongban.im.activity.base.UserBaseActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.BaseEvent;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * 用户中心（他人的）
 *
 * @author fushudi
 */
public class UserCardActivity extends UserBaseActivity {

    @Override
    protected void initData() {
        super.initData();
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            String visitorId = uri.getQueryParameter("visitorId");
            UserCenterApi.getInstance().fetchUserCenterInfo(visitorId, this);
        }

        ivFocus.setOnClickListener(this);
        ivCancelFocus.setOnClickListener(this);
        ivPrivateChat.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
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
