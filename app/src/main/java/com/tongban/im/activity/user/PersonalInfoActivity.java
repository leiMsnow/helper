package com.tongban.im.activity.user;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.UploadFileCallback;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.AddChildInfo;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.EditUser;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.User;
import com.tongban.im.widget.view.CameraView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 个人资料界面
 *
 * @author fushudi
 */
public class PersonalInfoActivity extends CameraResultActivity implements View.OnClickListener,
        CameraResultActivity.IPhotoListener {
    private RelativeLayout llUpdateUserPortrait, llNickName, llChildSex, llChildAge, llChildConstellation;
    private ImageView ivUserIcon;
    private TextView tvNickName, tvPhoneNum, tvChildAge,
            tvChildSex, tvChildConstellation, tvChildSchool;

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
    protected void initView() {
        setTitle("个人资料");
        llUpdateUserPortrait = (RelativeLayout) findViewById(R.id.ll_update_user_portrait);
        llNickName = (RelativeLayout) findViewById(R.id.ll_nickname);
        llChildSex = (RelativeLayout) findViewById(R.id.ll_sex);
        llChildAge = (RelativeLayout) findViewById(R.id.ll_child_age);
        llChildConstellation = (RelativeLayout) findViewById(R.id.ll_child_constellation);

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
        UserCenterApi.getInstance().fetchUserDetailInfo(true, this);
    }

    @Override
    protected void initListener() {
        setmPhotoListener(this);

        llUpdateUserPortrait.setOnClickListener(this);
        llNickName.setOnClickListener(this);
        llChildSex.setOnClickListener(this);
        llChildAge.setOnClickListener(this);
        llChildConstellation.setOnClickListener(this);

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
            mDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);

                    if (month.length() == 1) month = "0" + month;
                    if (day.length() == 1) day = "0" + day;

                    mChildBirthday = year + "-" + month + "-" + day;
//                    tvChildBirthday.setText(mChildBirthday);
                }
            }, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        }
        Calendar max = Calendar.getInstance();
        max.add(Calendar.YEAR, 0);
        max.add(Calendar.MONTH, 0);
        max.add(Calendar.DAY_OF_MONTH, 0);
        mDatePickerDialog.getDatePicker().setMaxDate(max.getTime().getTime());
        mDatePickerDialog.show();
        mDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!SPUtils.get(mContext, Consts.CHILD_BIRTHDAY, "").equals(mChildBirthday)) {
                    int mChildSex = (int) SPUtils.get(mContext, Consts.CHILD_SEX, 1);
                    AddChildInfo childInfo = new AddChildInfo();
                    childInfo.setBirthday(mChildBirthday);
                    childInfo.setSex(mChildSex);
                    List<AddChildInfo> children = new ArrayList<>();
                    children.add(childInfo);
                    UserCenterApi.getInstance().setChildInfo(SPUtils.get(mContext, Consts.USER_ID, "")
                            .toString(), children, null);
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
        UserCenterApi.getInstance().fetchPersonalCenterInfo(true, this);
        UserCenterApi.getInstance().fetchUserDetailInfo(true, this);
    }
}
