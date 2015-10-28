package com.tongban.im.fragment.account;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dd.CircularProgressButton;
import com.tb.api.FileUploadApi;
import com.tb.api.UserCenterApi;
import com.tb.api.callback.UploadFileCallback;
import com.tb.api.model.ImageUrl;
import com.tb.api.model.user.EditUser;
import com.tb.api.model.user.OtherRegister;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ImageUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.base.BaseToolBarFragment;

import com.tongban.im.widget.view.CameraView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 设置头像/填写用户昵称
 */
public class EditUserFragment extends BaseToolBarFragment implements
        CommonImageResultActivity.IPhotoListener {

    @Bind(R.id.iv_portrait)
    CircleImageView ivPortrait;
    @Bind(R.id.et_input_nickname)
    EditText etInputNickname;
    @Bind(R.id.btn_submit)
    CircularProgressButton btnSubmit;

    private CameraView mCameraView;

    private String mNickName;
    private byte[] mIcon;
    private EditUser editUser = new EditUser();

    private OtherRegister otherRegister;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CommonImageResultActivity) {
            ((CommonImageResultActivity) activity).setmPhotoListener(this);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_edit_user;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            btnSubmit.setIndeterminateProgressMode(true);
            String mOtherInfo = getArguments().getString(Consts.OTHER_REGISTER_INFO);
            // 第三方注册
            if (!TextUtils.isEmpty(mOtherInfo)) {
                String mOtherType = getArguments().getString(Consts.OTHER_REGISTER_TYPE);
                otherRegister = JSON.parseObject(mOtherInfo,
                        new TypeReference<OtherRegister>() {
                        });
                otherRegister.setType(mOtherType);
                setUserPortrait(otherRegister.getUrls().getMid(), ivPortrait);
                etInputNickname.setText(otherRegister.getNickName());
                mNickName = etInputNickname.getText().toString().trim();
                updateUser(otherRegister.getUrls(), false);
            } else {
                ivPortrait.setImageResource((Integer) SPUtils.get(mContext,
                        SPUtils.NO_CLEAR_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0));
            }
        }
    }

    @OnClick({R.id.iv_portrait, R.id.btn_submit})
    public void onClick(View v) {
        //设置头像
        if (v == ivPortrait) {
            createDialog();
        }
        //点击提交按钮
        else if (v == btnSubmit) {
            mNickName = etInputNickname.getText().toString().trim();
            if (TextUtils.isEmpty(mNickName)) {
                ToastUtil.getInstance(mContext).showToast("请输入昵称");
                return;
            }
            btnSubmit.setProgress(50);
            btnSubmit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 使用用户上传的资料
                    if (mIcon != null) {
                        uploadUserPortrait();
                    } else {
                        //使用第三方资料
                        if (otherRegister != null) {
                            updateUser(otherRegister.getUrls());
                        }
                        //使用系统默认资料
                        else {
                            int resId = (Integer) SPUtils.get(mContext,
                                    SPUtils.NO_CLEAR_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0);
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                                    resId);
                            mIcon = ImageUtils.Bitmap2Bytes(bitmap);
                            uploadUserPortrait();
                        }
                    }
                }
            }, 2 * 1000);
        }
    }

    private void uploadUserPortrait() {
        FileUploadApi.getInstance().uploadFile(mIcon
                , SPUtils.get(mContext, Consts.USER_ID, "").toString()
                , FileUploadApi.IMAGE_SIZE_300
                , FileUploadApi.IMAGE_SIZE_500
                , new UploadFileCallback() {

            @Override
            public void uploadSuccess(ImageUrl url) {
                updateUser(url);
            }

            @Override
            public void uploadFailed(String error) {
                btnSubmit.setProgress(0);
                ToastUtil.getInstance(mContext).showToast("头像上传失败,请重试");
            }

        });
    }

    /**
     * 修改用户信息
     *
     * @param url        头像地址
     * @param isCallback 是否需要回调，第三方注册不需要回调
     */
    private void updateUser(ImageUrl url, boolean isCallback) {
        editUser.setPortrait_url(url);
        editUser.setNick_name(mNickName);
        UserCenterApi.getInstance().updateUserInfo(editUser,
                isCallback ? EditUserFragment.this : null);
    }

    private void updateUser(ImageUrl url) {
        updateUser(url, true);
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
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivPortrait.setImageBitmap(bitmap);
    }


    public void onEventMainThread(ApiErrorResult obj) {
        btnSubmit.setProgress(0);
    }

}
