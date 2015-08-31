package com.tongban.im.activity.user;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ptz.PullToZoomBase;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseToolBarActivity implements View.OnClickListener {

    private PullToZoomScrollViewEx lvUserCenter;
    private ImageView ivUserIcon, ivClose;
    private TextView tvUserName, tvDeclaration;
    private RelativeLayout rlUserInfo;
    private RelativeLayout rlFansNum, rlFollowNum, rlGroupNum;
    private TextView tvFansCount, tvFollowCount, tvGroupCount;
    private TextView tvFans, tvFollow, tvGroup;
    private LinearLayout llMyTopic, llMyCollect;
    private ImageView ivZoomTop, ivZoomBottom;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        lvUserCenter = (PullToZoomScrollViewEx) findViewById(R.id.sv_user_center);
        View headView = LayoutInflater.from(this).inflate(R.layout.ptz_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.ptz_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.ptz_content_view, null, false);
        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);

        ivZoomTop = (ImageView) zoomView.findViewById(R.id.iv_zoom_top);
        ivZoomBottom = (ImageView) zoomView.findViewById(R.id.iv_zoom_bottom);

        rlUserInfo = (RelativeLayout) headView.findViewById(R.id.rl_user_info);
        ivUserIcon = (ImageView) headView.findViewById(R.id.iv_user_portrait);
        tvDeclaration = (TextView) headView.findViewById(R.id.tv_declaration);
        tvUserName = (TextView) headView.findViewById(R.id.tv_user_name);
        rlFansNum = (RelativeLayout) headView.findViewById(R.id.rl_fans_num);
        rlFollowNum = (RelativeLayout) headView.findViewById(R.id.rl_follow_num);
        rlGroupNum = (RelativeLayout) headView.findViewById(R.id.rl_group_num);

        tvFansCount = (TextView) headView.findViewById(R.id.tv_fans_num);
        tvFollowCount = (TextView) headView.findViewById(R.id.tv_follow_num);
        tvGroupCount = (TextView) headView.findViewById(R.id.tv_group_num);

        tvFans = (TextView) headView.findViewById(R.id.tv_fans);
        tvFollow = (TextView) headView.findViewById(R.id.tv_follow);
        tvGroup = (TextView) headView.findViewById(R.id.tv_group);

        llMyTopic = (LinearLayout) contentView.findViewById(R.id.ll_my_topic);
        llMyCollect = (LinearLayout) contentView.findViewById(R.id.ll_my_collect);

        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);

    }

    @Override
    protected void initData() {
        UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
    }

    @Override
    protected void initListener() {
        ivClose.setOnClickListener(this);

        rlUserInfo.setOnClickListener(this);
        rlFansNum.setOnClickListener(this);
        rlFollowNum.setOnClickListener(this);
        rlGroupNum.setOnClickListener(this);

        llMyCollect.setOnClickListener(this);
        llMyTopic.setOnClickListener(this);

        tvFans.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        tvGroup.setOnClickListener(this);

        lvUserCenter.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
                ObjectAnimator zoomBottomAnim = ObjectAnimator.ofFloat(ivZoomBottom, "alpha", 1.0f, 0.0f);
                ObjectAnimator zoomTopAnim = ObjectAnimator.ofFloat(ivZoomTop, "alpha", 0.0f, 1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(zoomBottomAnim).with(zoomTopAnim);
                animatorSet.setDuration(500);
                animatorSet.start();
            }

            @Override
            public void onPullZoomEnd() {
            }
        });
    }


    @Override
    public void onClick(View v) {
        //跳转到个人资料界面
        if (v == rlUserInfo) {
            startActivity(new Intent(this, PersonalInfoActivity.class));
        }
        //跳转到粉丝界面
        else if (v == rlFansNum || v == tvFans) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Tag", "Fans");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        //跳转到关注列表界面
        else if (v == rlFollowNum || v == tvFollow) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Tag", "Follow");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        //跳转到我的圈子界面
        else if (v == rlGroupNum || v == tvGroup) {
            startActivity(new Intent(this, MyGroupActivity.class));
        }
        //跳转到话题界面
        else if (v == llMyTopic) {
            startActivity(new Intent(this, MyTopicActivity.class));
        }
        //跳转到我的收藏界面
        else if (v == llMyCollect) {
            startActivity(new Intent(this, MyCollectActivity.class));
        }
        //关闭个人中心
        else if (v == ivClose) {
            finish();
        }

    }

    /**
     * 返回个人中心数据Event
     */
    public void onEventMainThread(BaseEvent.PersonalCenterEvent obj) {
        this.user = obj.user;
        if (user.getPortrait_url() != null) {
            Glide.with(mContext).load(user.getPortrait_url().getMin()).into(ivUserIcon);
        } else {
            ivUserIcon.setImageResource(R.drawable.rc_default_portrait);
        }
        if (user.getChild_info().size() > 0) {
            tvUserName.setText(user.getChild_info().get(0).getNick_name() + " " +
                    user.getChild_info().get(0).getAge()
                    + " " + user.getChild_info().get(0).getConstellation());
        } else {
            tvUserName.setText("");
        }
        tvUserName.setText(user.getChild_info().get(0).getNick_name() + " " +
                user.getChild_info().get(0).getAge()
                + " " + user.getChild_info().get(0).getConstellation());
        tvDeclaration.setText(user.getTags());
        tvFansCount.setText(user.getFans_amount() + "");
        tvFollowCount.setText(user.getFocused_amount() + "");
        tvGroupCount.setText(user.getJoined_group_amount() + "");

    }

    /**
     * 关注Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {

        if (obj.isFocus) {
            tvFollowCount.setText(String.valueOf(Integer.parseInt(tvFollowCount.getText().toString()) + 1));
        } else {
            tvFollowCount.setText(String.valueOf(Integer.parseInt(tvFollowCount.getText().toString()) - 1));
        }
    }
}
