package com.tongban.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.CreateTopicImgAdapter;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.AlertView;

import java.io.File;

/**
 * 通用的图片添加父类
 * Created by fushudi on 2015/8/13.
 */
public class CommonImageResultActivity extends BaseToolBarActivity {

    protected CreateTopicImgAdapter adapter;
    protected AlertView dialog;
    protected LinearLayout mCamera;
    protected LinearLayout mGallery;

    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    //刷新图片Adapter
    private void notifyChange(String picturePath) {
        if (adapter == null)
            return;
        if (adapter.getCount() == adapter.getImgCount()) {
            adapter.remove(adapter.getCount() - 1, false);
        }
        adapter.add(0, picturePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == CameraUtils.OPEN_CAMERA) {
            File file = CameraUtils.getImageFile();
            if (file.exists()) {
                if (file.length() > 100) {
                    String newFile = CameraUtils.saveToSD(file
                            .getAbsolutePath());
                    notifyChange(newFile);
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
            String picturePath = CameraUtils.searchUriFile(mContext, data);
            if (picturePath == null) {
                picturePath = data.getData().getPath();
            }
            String newFile = CameraUtils.saveToSD(picturePath);
            notifyChange(newFile);
        }
    }

    // 打开相机的提示框
    protected void createDialog() {
        if (dialog == null) {
            dialog = new AlertView(mContext);
            mCamera = (LinearLayout) dialog.findViewById(R.id.camera);
            mGallery = (LinearLayout) dialog.findViewById(R.id.gallery);

            mCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraUtils.takePhoto(mContext);
                    dialog.cancel();
                }
            });
            mGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraUtils.openPhotoAlbum(mContext);
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }
}
