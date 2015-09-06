package com.tongban.im.activity.user;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

/**
 * 个人资料界面
 *
 * @author fushudi
 */
public class PersonalInfoActivity extends BaseToolBarActivity implements View.OnClickListener {
    private ImageView ivUserIcon;
    private TextView tvNickName, tvPhoneNum, tvChildAge,
            tvChildSex, tvChildConstellation, tvChildSchool, tvAddress;

    private User user;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initView() {
        setTitle("个人资料");
        ivUserIcon = (ImageView) findViewById(R.id.iv_user_icon);
        tvNickName = (TextView) findViewById(R.id.tv_user_name);
        tvPhoneNum = (TextView) findViewById(R.id.tv_phone_num);
        tvChildAge = (TextView) findViewById(R.id.tv_child_age);
        tvChildSex = (TextView) findViewById(R.id.tv_child_sex);
        tvChildConstellation = (TextView) findViewById(R.id.tv_child_constellation);
        tvChildSchool = (TextView) findViewById(R.id.tv_chile_school);
        tvAddress = (TextView) findViewById(R.id.tv_address);

    }

    @Override
    protected void initData() {
        UserCenterApi.getInstance().fetchUserDetailInfo(this);
    }

    @Override
    protected void initListener() {
        ivUserIcon.setOnClickListener(this);
        ivUserIcon.setOnClickListener(this);
        tvNickName.setOnClickListener(this);
        tvPhoneNum.setOnClickListener(this);
        tvChildAge.setOnClickListener(this);
        tvChildSex.setOnClickListener(this);
        tvChildConstellation.setOnClickListener(this);
        tvChildSchool.setOnClickListener(this);
        tvAddress.setOnClickListener(this);

    }

    //返回个人资料数据
    public void onEventMainThread(BaseEvent.UserInfoEvent obj) {
        this.user = obj.user;
        if (user.getPortrait_url()!=null){
            Glide.with(mContext).load(user.getPortrait_url().getMin()).into(ivUserIcon);
        }else {
            ivUserIcon.setImageResource(R.drawable.rc_default_portrait);
        }
        tvNickName.setText(user.getNick_name());
        tvPhoneNum.setText(user.getMobile_phone());
        if (user.getChild_info() != null && user.getChild_info().size() > 0) {
            tvChildAge.setText(user.getChild_info().get(0).getAge());
            tvChildSex.setText(user.getChild_info().get(0).StrSex());
            tvChildConstellation.setText(user.getChild_info().get(0).getConstellation());
            tvChildSchool.setText(user.getChild_info().get(0).getSchool());
        }
        tvAddress.setText(user.getAddress());
    }

    @Override
    public void onClick(View v) {

    }
}
