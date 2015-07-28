package com.tongban.im.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;
import com.tongban.im.util.ImageUtils;
import com.tongban.im.util.Utils;
import com.tongban.im.widget.view.AlertView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends BaseToolBarActivity implements View.OnClickListener {
    private ImageView ivSetGroupIcon;
    private Button btnSubmit;
    private EditText etGroupName;

    private AlertView dialog;
    private LinearLayout mCamera;
    private LinearLayout mGallery;

    private int mGroupType;
    private String titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(titleName.equals(getString(R.string.create_group)) ? getString(R.string.create_group) :
                getString(R.string.create) + titleName);
    }

    @Override
    protected int getLayoutRes() {
        mGroupType = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE, 0);
        titleName = getIntent().getExtras().getString(Consts.KEY_GROUP_TYPE_NAME, "");
        switch (mGroupType) {
            case GroupType.CITY:
                setTheme(R.style.AppTheme_Blue_Base);
                break;
            case GroupType.AGE:
                setTheme(R.style.AppTheme_Red_Base);
                break;
        }
        return R.layout.activity_create_group;
    }

    @Override
    protected void initView() {
        ivSetGroupIcon = (ImageView) findViewById(R.id.iv_group_icon);
        btnSubmit = (Button) findViewById(R.id.btn_create);
        etGroupName = (EditText) findViewById(R.id.et_group_name);
    }

    @Override
    protected void initListener() {
        ivSetGroupIcon.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == ivSetGroupIcon) {
            createDialog();
        } else if (v == btnSubmit) {
            String groupName = etGroupName.getText().toString().trim();
            if (TextUtils.isEmpty(groupName)) {
                ToastUtil.getInstance(mContext).showToast("请输入圈子名称");
                return;
            }
            GroupApi.getInstance().createGroup(groupName, mGroupType, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == Utils.OPEN_CAMERA) {
            File file = Utils.getImageFile();
            if (file.exists()) {
                if (file.length() > 100) {
                    String newFile = ImageUtils.saveToSD(file
                            .getAbsolutePath());

                    ivSetGroupIcon.setImageBitmap(getLocalBitmap(newFile));
                }
            }
        } else if (requestCode == Utils.OPEN_ALBUM) {
            String picturePath = ImageUtils.searchUriFile(mContext, data);
            if (picturePath != null) {
                String newFile = ImageUtils.saveToSD(picturePath);
                ivSetGroupIcon.setImageBitmap(getLocalBitmap(newFile));
            }
        }
    }

    private Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 打开相机的提示框
    private void createDialog() {
        dialog = new AlertView(this);
        mCamera = (LinearLayout) dialog.findViewById(R.id.camera);
        mGallery = (LinearLayout) dialog.findViewById(R.id.gallery);

        mCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.takePhoto(mContext, null);
                dialog.cancel();
            }
        });
        mGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.openPhotoAlbum(mContext, null);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void onEventMainThread(Object obj) {
        if (obj instanceof Group) {
            ToastUtil.getInstance(mContext).showToast("创建成功");
            finish();
        }
    }
}
