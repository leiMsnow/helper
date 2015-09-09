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

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.UploadFileCallback;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.VerifyTimerCount;
import com.tongban.im.model.AddChildInfo;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.User;
import com.tongban.im.widget.view.CameraView;

import java.util.ArrayList;
import java.util.List;


/**
 * 注册第二步 设置头像/填写用户昵称
 */
public class SecondRegisterFragment extends BaseApiFragment implements
        TextWatcher, View.OnClickListener, CameraResultActivity.IPhotoListener {
    private ImageView ivPortrait;
    private EditText etNickName;
    private Button btnSubmit;

    private String mPhoneNum, mPwd, mVerifyId, mVerifyCode, mNickName;

    private CameraView mCameraView;

    private byte[] mIcon;

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
            mPhoneNum = getArguments().getString(Consts.KEY_PHONE, "");
            mPwd = getArguments().getString(Consts.KEY_PWD, "");
            mVerifyId = getArguments().getString(Consts.KEY_VERIFY_ID, "");
            mVerifyCode = getArguments().getString(Consts.KEY_VERIFY_CODE, "");
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
            AccountApi.getInstance().register(mPhoneNum, mNickName, mPwd, mVerifyId,
                    mVerifyCode, this);
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
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivPortrait.setImageBitmap(bitmap);
    }

    public void onEventMainThread(User user) {
        SPUtils.put(mContext, Consts.USER_ACCOUNT, mPhoneNum);
//        FileUploadApi.getInstance().uploadFile(mIcon, null, FileUploadApi.IMAGE_SIZE_300,
//                FileUploadApi.IMAGE_SIZE_500, new UploadFileCallback() {
//
//                    @Override
//                    public void uploadSuccess(ImageUrl url) {
//
//                    }
//
//                    @Override
//                    public void uploadFailed(String error) {
//
//                    }
//
//                });
    }

    public void onEventMainThread(BaseEvent.RegisterEvent regEvent) {
        // 注册成功
        if (regEvent.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.REGISTER) {
            //添加宝宝信息
            int childSex = (int) SPUtils.get(mContext, Consts.CHILD_SEX, 1);
            String childBirthday = SPUtils.get(mContext, Consts.CHILD_BIRTHDAY, "").toString();
            AddChildInfo childInfo = new AddChildInfo();
            childInfo.setBirthday(childBirthday);
            childInfo.setSex(childSex);
            List<AddChildInfo> children = new ArrayList<>();
            children.add(childInfo);
            UserCenterApi.getInstance().setChildInfo(regEvent.user_id,children, this);
        }
    }

    public void onEventMainThread(BaseEvent.ChildCreateSuccessEvent obj) {
        AccountApi.getInstance().login(mPhoneNum, mPwd, this);
    }


}
