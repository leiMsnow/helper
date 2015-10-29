package com.tongban.im.activity.user;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tb.api.FileUploadApi;
import com.tb.api.UserCenterApi;
import com.tb.api.callback.UploadFileCallback;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.ImageUrl;
import com.tb.api.model.user.AddChildInfo;
import com.tb.api.model.user.EditUser;
import com.tb.api.model.user.User;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.widget.view.CameraView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人资料界面
 *
 * @author fushudi
 */
public class MyInfoActivity extends CommonImageResultActivity implements
        CommonImageResultActivity.IPhotoListener {

    @Bind(R.id.iv_user_icon)
    CircleImageView ivUserIcon;
    @Bind(R.id.ll_update_user_portrait)
    RelativeLayout llUpdateUserPortrait;
    @Bind(R.id.tv_user_name)
    TextView tvNickName;
    @Bind(R.id.ll_nickname)
    RelativeLayout llNickName;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.tv_child_sex)
    TextView tvChildSex;
    @Bind(R.id.ll_sex)
    RelativeLayout llChildSex;
    @Bind(R.id.tv_child_age)
    TextView tvChildAge;
    @Bind(R.id.ll_child_age)
    RelativeLayout llChildAge;
    @Bind(R.id.tv_child_constellation)
    TextView tvChildConstellation;
    @Bind(R.id.ll_child_constellation)
    RelativeLayout llChildConstellation;
    @Bind(R.id.tv_chile_school)
    TextView tvChildSchool;

    private User user;
    private CameraView mCameraView;
    private EditUser editUser = new EditUser();
    private DatePickerDialog mDatePickerDialog;

    private byte[] mIcon;
    private String mChildBirthday;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initData() {
        setTitle("个人资料");

        UserCenterApi.getInstance().fetchUserDetailInfo(this);
        setmPhotoListener(this);
    }

    //返回个人资料数据
    public void onEventMainThread(BaseEvent.UserInfoEvent obj) {
        this.user = obj.user;
        if (user.getPortraitUrl() != null) {
            setUserPortrait(user.getPortraitUrl().getMin(), ivUserIcon);
        } else {
            ivUserIcon.setImageResource(Consts.getUserDefaultPortrait());
        }
        tvNickName.setText(user.getNick_name());
        tvPhoneNum.setText(user.getMobile_phone());
        if (user.getChildInfo() != null && user.getChildInfo().size() > 0) {
            String childAge = user.getChildInfo().get(0).getAge();
            tvChildAge.setText(user.getChildInfo().get(0).StrChildAge(childAge));

            tvChildSex.setText(user.getChildInfo().get(0).StrSex());
            tvChildConstellation.setText(user.getChildInfo().get(0).getConstellation());
            tvChildSchool.setText(user.getChildInfo().get(0).getSchool());
        }
    }

    @OnClick({R.id.ll_update_user_portrait, R.id.ll_nickname, R.id.ll_sex
            , R.id.ll_child_age
            , R.id.ll_child_constellation})
    public void onClick(View v) {
        //修改头像
        if (v == llUpdateUserPortrait) {
            createDialog();
        }
        //修改昵称
        else if (v == llNickName) {
            Intent intent = new Intent(mContext, UpdatePersonalInfoActivity.class);
            intent.putExtra(Consts.KEY_UPDATE_PERSONAL_INFO, Consts.KEY_UPDATE_NICKNAME);
            startActivity(intent);
        }
        //修改性别
        else if (v == llChildSex) {
            Intent intent = new Intent(mContext, UpdatePersonalInfoActivity.class);
            intent.putExtra(Consts.KEY_UPDATE_PERSONAL_INFO, Consts.KEY_UPDATE_SEX);
            startActivity(intent);
        }
        //修改年龄
        else if (v == llChildAge) {
            openDatePicker();
        }
        //修改星座
        else if (v == llChildConstellation) {
            openDatePicker();
        }
    }

    //打开时间选择器
    private void openDatePicker() {
        if (mDatePickerDialog == null) {
            Calendar c = Calendar.getInstance();
            if (user.getChildInfo() != null) {
                String[] value = user.getChildInfo().get(0).getBirthday().split("\\-");
                c.set(Calendar.YEAR, Integer.parseInt(value[0]));
                c.set(Calendar.MONTH, Integer.parseInt(value[1]));
                c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(value[2]));
            }

            mDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);

                    if (month.length() == 1) month = "0" + month;
                    if (day.length() == 1) day = "0" + day;

                    mChildBirthday = year + "-" + month + "-" + day;
                }
            }, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        }
        mDatePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTime().getTime());
        mDatePickerDialog.show();
        mDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!user.getChildInfo().get(0).getBirthday().equals(mChildBirthday)) {
                    int mChildSex = (int) SPUtils.get(mContext, Consts.CHILD_SEX, 1);
                    AddChildInfo childInfo = new AddChildInfo();
                    childInfo.setBirthday(mChildBirthday);
                    childInfo.setSex(mChildSex);
                    List<AddChildInfo> children = new ArrayList<>();
                    children.add(childInfo);
                    UserCenterApi.getInstance().setChildInfo(getUserId(), children, null);
                }
            }
        });
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
        FileUploadApi.getInstance().uploadFile(mIcon
                , null
                , FileUploadApi.IMAGE_SIZE_300
                , FileUploadApi.IMAGE_SIZE_500
                , new UploadFileCallback() {

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
        UserCenterApi.getInstance().updateUserInfo(editUser, MyInfoActivity.this);
    }

    /**
     * 更新个人资料Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.EditUserEvent obj) {
        UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
        UserCenterApi.getInstance().fetchUserDetailInfo(this);
    }
}
