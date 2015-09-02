package com.tongban.im.activity.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.DepthPageTransformer;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.base.UserBaseActivity;
import com.tongban.im.adapter.UserInfoAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

import io.rong.imkit.RongIM;

/**
 * 用户中心（他人的）
 *
 * @author fushudi
 */
public class UserCenterActivity extends UserBaseActivity implements View.OnClickListener {

    private CheckBox chbFocus;
    private ImageView ivPrivateChat;

    @Override
    protected void initView() {
        super.initView();
        ivPrivateChat = (ImageView) headView.findViewById(R.id.iv_private_chat);
        chbFocus = (CheckBox) headView.findViewById(R.id.chb_focus);

    }

    @Override
    protected void initData() {
        getUserInfo();
    }

    @Override
    protected void onNewIntent(Intent intent) {
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
        chbFocus.setOnClickListener(this);
        ivPrivateChat.setOnClickListener(this);
    }

    /**
     * 用户中心（他人）Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.UserCenterEvent obj) {
        setDataInfo(obj.user);
        tvSetChildInfo.setVisibility(View.GONE);
        chbFocus.setVisibility(View.VISIBLE);
        chbFocus.setChecked(mUserInfo.is_focused());
        if (mUserInfo.is_focused()) {
            ivPrivateChat.setVisibility(View.VISIBLE);
        } else {
            ivPrivateChat.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        //添加/取消关注（为checkbox设置的onClick事件的原因，逻辑正好相反）
        if (v == chbFocus) {
            UserCenterApi.getInstance().focusUser(chbFocus.isChecked(),
                    mUserInfo.getUser_id(), this);
            chbFocus.setChecked(false);
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

        chbFocus.setChecked(obj.isFocus);
        if (obj.isFocus) {
            ivPrivateChat.setVisibility(View.VISIBLE);
            ToastUtil.getInstance(mContext).showToast("关注成功");
        } else {
            ivPrivateChat.setVisibility(View.GONE);
            ToastUtil.getInstance(mContext).showToast("取消成功");
        }
    }
}
