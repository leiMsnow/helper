package com.tongban.im.activity.user;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.UploadFileCallback;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.EditUser;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.User;
import com.tongban.im.widget.view.CameraView;

/**
 * 个人资料界面
 *
 * @author fushudi
 */
public class PersonalInfoActivity extends CameraResultActivity implements View.OnClickListener,
        CameraResultActivity.IPhotoListener {
    private LinearLayout llUpdateUserPortrait;
    private ImageView ivUserIcon;
    private TextView tvNickName, tvPhoneNum, tvChildAge,
            tvChildSex, tvChildConstellation, tvChildSchool;

    private User user;
    private CameraView mCameraView;
    private EditUser editUser = new EditUser();

    private byte[] mIcon;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initView() {
        setTitle("个人资料");
        llUpdateUserPortrait = (LinearLayout) findViewById(R.id.ll_update_user_portrait);
        ivUserIcon = (ImageView) findViewById(R.id.iv_user_icon);
        tvNickName = (TextView) findViewById(R.id.tv_user_name);
        tvPhoneNum = (TextView) findViewById(R.id.tv_phone_num);
        tvChildAge = (TextView) findViewById(R.id.tv_child_age);
        tvChildSex = (TextView) findViewById(R.id.tv_child_sex);
        tvChildConstellation = (TextView) findViewById(R.id.tv_child_constellation);
        tvChildSchool = (TextView) findViewById(R.id.tv_chile_school);

    }

    @Override
    protected void initData() {
        UserCenterApi.getInstance().fetchUserDetailInfo(this);
    }

    @Override
    protected void initListener() {
        setmPhotoListener(this);

        llUpdateUserPortrait.setOnClickListener(this);
        tvNickName.setOnClickListener(this);
        tvPhoneNum.setOnClickListener(this);
        tvChildAge.setOnClickListener(this);
        tvChildSex.setOnClickListener(this);
        tvChildConstellation.setOnClickListener(this);
        tvChildSchool.setOnClickListener(this);

    }

    //返回个人资料数据
    public void onEventMainThread(BaseEvent.UserInfoEvent obj) {
        this.user = obj.user;
        if (user.getPortrait_url() != null) {
            Glide.with(mContext).load(user.getPortrait_url().getMin()).into(ivUserIcon);
        } else {
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
    }

    @Override
    public void onClick(View v) {
        //修改头像
        if (v == llUpdateUserPortrait) {
            createDialog();

        }
    }

    // 打开相机的提示框
    private void createDialog() {
        if (mCameraView == null) {
            mCameraView = new CameraView(mContext);
        }
        mCameraView.show();
    }

    @Override
    public void sendPhoto(byte[] bytes) {
        mIcon = bytes;
        FileUploadApi.getInstance().uploadFile(mIcon, null, FileUploadApi.IMAGE_SIZE_300,
                FileUploadApi.IMAGE_SIZE_500, new UploadFileCallback() {

                    @Override
                    public void uploadSuccess(ImageUrl url) {
                        editUser.setPortrait_url(url);
                        updateUser();
                    }

                    @Override
                    public void uploadFailed(String error) {
                        updateUser();
                    }

                });

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivUserIcon.setImageBitmap(bitmap);
    }

    private void updateUser() {
        UserCenterApi.getInstance().updateUserInfo(editUser, PersonalInfoActivity.this);
    }

    /**
     * 更新个人资料Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.EditUserEvent obj) {
        UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
    }
}
