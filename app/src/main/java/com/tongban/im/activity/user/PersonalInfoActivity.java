package com.tongban.im.activity.user;


import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.User;

/**
 * 个人资料界面
 *
 * @author fushudi
 */
public class PersonalInfoActivity extends BaseToolBarActivity {
    private ImageView ivUserIcon;
    private TextView tvNickName, tvPhoneNum, tvWork, tvChildAge,
            tvChildSex, tvChildConstellation, tvChildSchool, tvAddress;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initView() {
        setTitle("个人资料");
        ivUserIcon = (ImageView) findViewById(R.id.iv_user_icon);
        tvNickName = (TextView) findViewById(R.id.tv_nickname);
        tvPhoneNum = (TextView) findViewById(R.id.tv_phone_num);
        tvWork = (TextView) findViewById(R.id.tv_work);
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

    }

    public void onEventMainThread(User user) {
        tvNickName.setText(user.getNick_name());
        tvPhoneNum.setText(user.getMobile_phone());
        tvWork.setText(user.getDeclaration());
        tvChildAge.setText(user.getAge());
        tvChildSex.setText(user.getChild_info().get(0).getSex());
        tvChildConstellation.setText(user.getChild_info().get(0).getBirthday());
        tvChildSchool.setText(user.getChild_info().get(0).getSchool());
        tvAddress.setText(user.getAddress());
    }
}
