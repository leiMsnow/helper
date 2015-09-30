package com.tongban.im.activity.user;


import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tongban.im.R;
import com.tongban.im.activity.base.UserBaseActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.BaseEvent;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * 用户中心（他人的）
 *
 * @author fushudi
 */
public class UserCenterActivity extends UserBaseActivity {

    private Button ivFocus, ivCancelFocus;
    private Button ivPrivateChat;

    @Override
    protected void initView() {
        super.initView();
        ivPrivateChat = (Button) headView.findViewById(R.id.iv_private_chat);
        ivFocus = (Button) headView.findViewById(R.id.iv_focus);
        ivCancelFocus = (Button) headView.findViewById(R.id.iv_cancel_focus);

    }

    @Override
    protected void initData() {
        getUserInfo();
    }

    /**
     * 获取用户资料
     */
    private void getUserInfo() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            String visitorId = uri.getQueryParameter("visitorId");
            UserCenterApi.getInstance().fetchUserCenterInfo(visitorId, this);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
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
        tvSetChildInfo.setVisibility(View.GONE);
        ivFocus.setVisibility(View.VISIBLE);
//        ivFocus.setChecked(mUserInfo.is_focused());
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
        super.onClick(v);
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
