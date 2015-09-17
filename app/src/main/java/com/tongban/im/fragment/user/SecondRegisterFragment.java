package com.tongban.im.fragment.user;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.callback.UploadFileCallback;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.EditUser;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.OtherRegister;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.CameraView;


/**
 * 注册第二步 设置头像/填写用户昵称
 */
public class SecondRegisterFragment extends BaseToolBarFragment implements
        TextWatcher, View.OnClickListener, CameraResultActivity.IPhotoListener {
    private ImageView ivPortrait;
    private EditText etNickName;
    private Button btnSubmit;

    private CameraView mCameraView;

    private String mNickName;
    private byte[] mIcon;
    private EditUser editUser = new EditUser();

    private String mOtherInfo;
    private String mOtherType;
    private OtherRegister otherRegister;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CameraResultActivity) {
            ((CameraResultActivity) activity).setmPhotoListener(this);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_second_register;
    }

    @Override
    protected void initView() {
        ivPortrait = (ImageView) mView.findViewById(R.id.iv_portrait);
        etNickName = (EditText) mView.findViewById(R.id.et_input_school);
        btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mOtherInfo = getArguments().getString(Consts.OTHER_REGISTER_INFO);
            if (!TextUtils.isEmpty(mOtherInfo)) {
                mOtherType = getArguments().getString(Consts.OTHER_REGISTER_TYPE);
                otherRegister = JSON.parseObject(mOtherInfo,
                        new TypeReference<OtherRegister>() {
                        });
                otherRegister.setType(mOtherType);
            }

        } else {
            ivPortrait.setImageResource((Integer) SPUtils.get(mContext,
                    SPUtils.VISIT_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0));
        }
    }

    @Override
    protected void initListener() {
        ivPortrait.setOnClickListener(this);
        etNickName.addTextChangedListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mNickName = etNickName.getText().toString().trim();
        if (!TextUtils.isEmpty(mNickName)) {
            btnSubmit.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        //设置头像
        if (v == ivPortrait) {
            createDialog();
        }
        //点击提交按钮
        else if (v == btnSubmit) {
            if (mIcon == null) {
                int resId = (Integer) SPUtils.get(mContext,
                        SPUtils.VISIT_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0);
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
                mIcon = CameraUtils.Bitmap2Bytes(bitmap);
            }
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

        }
    }

    private void updateUser() {
        editUser.setNick_name(mNickName);
        UserCenterApi.getInstance().updateUserInfo(editUser,
                SecondRegisterFragment.this);
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
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivPortrait.setImageBitmap(bitmap);
    }

}
