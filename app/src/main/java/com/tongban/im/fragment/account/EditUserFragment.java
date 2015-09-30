package com.tongban.im.fragment.account;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dd.CircularProgressButton;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.api.callback.UploadFileCallback;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.user.EditUser;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.user.OtherRegister;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.CameraView;


/**
 * 设置头像/填写用户昵称
 */
public class EditUserFragment extends BaseToolBarFragment implements
        View.OnClickListener, CommonImageResultActivity.IPhotoListener {
    private ImageView ivPortrait;
    private EditText etNickName;
    private CircularProgressButton btnSubmit;

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
    protected void initView() {
        ivPortrait = (ImageView) mView.findViewById(R.id.iv_portrait);
        etNickName = (EditText) mView.findViewById(R.id.et_input_school);
        btnSubmit = (CircularProgressButton) mView.findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            String mOtherInfo = getArguments().getString(Consts.OTHER_REGISTER_INFO);
            if (!TextUtils.isEmpty(mOtherInfo)) {
                String mOtherType = getArguments().getString(Consts.OTHER_REGISTER_TYPE);
                otherRegister = JSON.parseObject(mOtherInfo,
                        new TypeReference<OtherRegister>() {
                        });
                otherRegister.setType(mOtherType);
                setUserPortrait(otherRegister.getUrls().getMid(), ivPortrait);
                etNickName.setText(otherRegister.getNickName());
                mNickName = etNickName.getText().toString().trim();
                updateUser(otherRegister.getUrls(), false);
            } else {
                ivPortrait.setImageResource((Integer) SPUtils.get(mContext,
                        SPUtils.NO_CLEAR_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0));
            }
        }
    }

    @Override
    protected void initListener() {
        ivPortrait.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //设置头像
        if (v == ivPortrait) {
            createDialog();
        }
        //点击提交按钮
        else if (v == btnSubmit) {
            mNickName = etNickName.getText().toString().trim();
            if (TextUtils.isEmpty(mNickName)) {
                ToastUtil.getInstance(mContext).showToast("请输入昵称");
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 使用用户上传的资料
                    if (mIcon != null) {
                        FileUploadApi.getInstance().uploadFile(mIcon, null,
                                FileUploadApi.IMAGE_SIZE_300,
                                FileUploadApi.IMAGE_SIZE_500,
                                new UploadFileCallback() {

                                    @Override
                                    public void uploadSuccess(ImageUrl url) {
                                        updateUser(url);
                                    }

                                    @Override
                                    public void uploadFailed(String error) {
                                        updateUser(null);
                                    }

                                });
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
                            mIcon = CameraUtils.Bitmap2Bytes(bitmap);
                            FileUploadApi.getInstance().uploadFile(mIcon, null,
                                    FileUploadApi.IMAGE_SIZE_300,
                                    FileUploadApi.IMAGE_SIZE_500,
                                    new UploadFileCallback() {

                                        @Override
                                        public void uploadSuccess(ImageUrl url) {
                                            updateUser(url);
                                        }

                                        @Override
                                        public void uploadFailed(String error) {
                                            updateUser(null);
                                        }

                                    });
                        }
                    }
                }
            }, 1 * 1000);

        }

    }

    /**
     * 修改用户信息
     *
     * @param url        头像地址
     * @param isCallback 是否需要回调
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

}
