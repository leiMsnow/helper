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
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.AddChildInfo;
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

    private String mNickName;

    private CameraView mCameraView;

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
//            int childSex = getArguments().getInt(Consts.CHILD_SEX);
//            String childBirthday = getArguments().getString(Consts.CHILD_BIRTHDAY);
//            AddChildInfo childInfo = new AddChildInfo();
//            childInfo.setBirthday(childBirthday);
//            childInfo.setSex(childSex);
//            List<AddChildInfo> children = new ArrayList<>();
//            children.add(childInfo);
//            UserCenterApi.getInstance().setChildInfo(true, children, this);
//            getActivity().finish();


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
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivPortrait.setImageBitmap(bitmap);
    }

}
