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
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
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
public class UserCenterActivity extends BaseToolBarActivity implements View.OnClickListener {
    private PullToZoomScrollViewEx lvUserCenter;

    private TextView tvTags, tvUserName;
    private CheckBox chbFocus;
    private LinearLayout llMyTopic, llMyCollect;
    private ImageView ivSex, ivPrivateChat, ivClose, ivUserPortrait;
    private TextView tvFansNum, tvFocusNum, tvGroupNum;
    private TextView tvFans, tvFocus, tvGroup;

    private User mUserInfo;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initView() {
        lvUserCenter = (PullToZoomScrollViewEx) findViewById(R.id.sv_user_center);
        View headView = LayoutInflater.from(this).inflate(R.layout.ptz_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.ptz_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.ptz_content_view, null, false);
        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);

        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvTags = (TextView) findViewById(R.id.tv_declaration);
        tvTags.setVisibility(View.GONE);
        llMyTopic = (LinearLayout) contentView.findViewById(R.id.ll_my_topic);
        llMyCollect = (LinearLayout) contentView.findViewById(R.id.ll_my_collect);
        llMyTopic.setVisibility(View.GONE);
        llMyCollect.setVisibility(View.GONE);
        ivSex = (ImageView) headView.findViewById(R.id.iv_sex);
        tvUserName = (TextView) headView.findViewById(R.id.tv_user_name);
        tvFansNum = (TextView) headView.findViewById(R.id.tv_fans_num);
        tvFocusNum = (TextView) headView.findViewById(R.id.tv_follow_num);
        tvGroupNum = (TextView) headView.findViewById(R.id.tv_group_num);
        ivUserPortrait = (ImageView) headView.findViewById(R.id.iv_user_portrait);

        ivPrivateChat = (ImageView) headView.findViewById(R.id.iv_private_chat);

        tvFans = (TextView) findViewById(R.id.tv_fans);
        tvFocus = (TextView) findViewById(R.id.tv_follow);
        tvGroup = (TextView) findViewById(R.id.tv_group);

        chbFocus = (CheckBox) headView.findViewById(R.id.chb_focus);


        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);
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
        chbFocus.setOnClickListener(this);
        tvFans.setOnClickListener(this);
        tvGroup.setOnClickListener(this);
        tvFocus.setOnClickListener(this);
        tvFansNum.setOnClickListener(this);
        tvFocusNum.setOnClickListener(this);
        tvGroupNum.setOnClickListener(this);
        ivPrivateChat.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    /**
     * 用户中心（他人）Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.UserCenterEvent obj) {
        mUserInfo = obj.user;
        Glide.with(mContext).load(mUserInfo.getPortrait_url().getMin())
                .placeholder(R.drawable.rc_default_portrait).into(ivUserPortrait);
        if (mUserInfo.getSex().equals("男")) {
            ivSex.setImageResource(R.mipmap.ic_boy);
        } else {
            ivSex.setImageResource(R.mipmap.ic_girl);
        }
        tvUserName.setText(mUserInfo.getChild_info().get(0).getNick_name() + " " +
                mUserInfo.getChild_info().get(0).getAge()
                + " " + mUserInfo.getChild_info().get(0).getConstellation());
        tvFansNum.setText(mUserInfo.getFans_amount());
        tvFocusNum.setText(mUserInfo.getFocused_amount());
        tvGroupNum.setText(mUserInfo.getJoined_group_amount());

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
        if (v == chbFocus) {
            //添加/取消关注（为checkbox设置的onClick事件的原因，逻辑正好相反）
            UserCenterApi.getInstance().focusUser(chbFocus.isChecked(),
                    new String[]{mUserInfo.getUser_id()}, this);
            chbFocus.setChecked(false);
        }
        //粉丝列表
        else if (v == tvFans || v == tvFansNum) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Tag", "Fans");
            bundle.putString(Consts.USER_ID, mUserInfo.getUser_id());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        //关注列表
        else if (v == tvFocus || v == tvFocusNum) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Tag", "Follow");
            bundle.putString(Consts.USER_ID, mUserInfo.getUser_id());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        //圈子列表
        else if (v == tvGroup || v == tvGroupNum) {
            Intent intent = new Intent(this, MyGroupActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Consts.USER_ID, mUserInfo.getUser_id());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        //私聊
        else if (v == ivPrivateChat) {
            RongIM.getInstance().startPrivateChat(mContext, mUserInfo.getUser_id(),
                    mUserInfo.getNick_name());
        }
        //关闭用户中心
        else if (v == ivClose) {
            finish();
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
