package com.tongban.im.activity.user;


import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

import static com.tongban.im.common.Consts.KEY_FOCUS;

/**
 * 用户中心（他人的）
 *
 * @author fushudi
 */
public class UserCenterActivity extends BaseToolBarActivity implements View.OnClickListener {
    private PullToZoomScrollViewEx lvUserCenter;

    private TextView tvTags, tvUserName;
    private ImageView ivFocus, ivCancelFocus;
    private LinearLayout llMyTopic, llMyCollect;
    private ImageView ivSex;
    private TextView tvFansNum, tvFocusNum, tvGroupNum;

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

        ivFocus = (ImageView) headView.findViewById(R.id.iv_focus);
        ivCancelFocus = (ImageView) headView.findViewById(R.id.iv_cancel_focus);


        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            String visitorId = uri.getQueryParameter("visitorId");
            UserCenterApi.getInstance().fetchUserCenterInfo(visitorId, this);
        }
    }

    @Override
    protected void initListener() {
        ivFocus.setOnClickListener(this);
        ivCancelFocus.setOnClickListener(this);
    }

    public void onEventMainThread(BaseEvent.UserCenterEvent obj) {
        User user = obj.getUser();
        if (user.getSex().equals("男")) {
            Glide.with(mContext).load(R.mipmap.ic_boy).into(ivSex);
        } else {
            Glide.with(mContext).load(R.mipmap.ic_girl).into(ivSex);
        }
        tvUserName.setText(user.getChild_info().get(0).getNick_name() + " " + user.getChild_info().get(0).getAge()
                + " " + user.getChild_info().get(0).getConstellation());
        tvFansNum.setText(user.getFans_amount());
        tvFocusNum.setText(user.getFocused_amount());
        tvGroupNum.setText(user.getJoined_group_amount());
        if (user.is_focused()) {
            ivCancelFocus.setVisibility(View.VISIBLE);
            ivCancelFocus.setTag(user.getUser_id());
        } else {
            ivFocus.setVisibility(View.VISIBLE);
            ivFocus.setTag(user.getUser_id());
            Log.d("user.getUser_id", user.getUser_id());
        }
    }

    @Override
    public void onClick(View v) {
        //添加关注
        if (v == ivFocus) {
            String focusId = v.getTag().toString();
            UserCenterApi.getInstance().focusUser(new String[]{focusId}, this);
        }
        //取消关注
        else if (v == ivCancelFocus) {
            String focusId = v.getTag().toString();
            UserCenterApi.getInstance().focusUser(new String[]{focusId}, this);
        }
    }
}
