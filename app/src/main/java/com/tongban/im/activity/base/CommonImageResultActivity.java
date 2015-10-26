package com.tongban.im.activity.base;

import android.content.Intent;
import android.text.TextUtils;

import com.tongban.corelib.utils.ImageUtils;
import com.tongban.im.activity.ClipImageBorderViewActivity;
import com.tongban.im.utils.CameraUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 通用的图片添加父类
 * 裁剪
 * 拍照
 * 相册
 * Created by fushudi on 2015/8/13.
 */
public abstract class CommonImageResultActivity extends AppBaseActivity {

    private ImageResultListener imageResultListener;

    public void setImageResultListener(ImageResultListener imageResultListener) {
        this.imageResultListener = imageResultListener;
    }

    private boolean isCut = false;

    private IPhotoListener mPhotoListener;

    public void setmPhotoListener(IPhotoListener mPhotoListener) {
        isCut = true;
        this.mPhotoListener = mPhotoListener;
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
                    String newFile = ImageUtils.saveToSD(file
                            .getAbsolutePath());
                    if (isCut) {
                        opCutActivity(newFile);
                    } else {
                        if (imageResultListener != null) {
                            imageResultListener.cameraResult(newFile);
                        }
                    }
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
            ArrayList<String> picturePaths = data.getExtras().getStringArrayList("selectedImages");
            if (isCut) {
                if (picturePaths.size() > 0) {
                    String newFile = ImageUtils.saveToSD(picturePaths.get(0));
                    opCutActivity(newFile);
                }
            } else {
                if (imageResultListener != null) {
                    imageResultListener.albumResult(picturePaths);
                }
            }
        } else if (requestCode == CameraUtils.PHOTO_REQUEST_CUT) {
            byte[] bytes = data.getByteArrayExtra("bitmap");
            if (mPhotoListener != null) {
                mPhotoListener.sendPhoto(bytes);
            }
        }
    }

    private void opCutActivity(String newFile){
        if (!TextUtils.isEmpty(newFile)) {
            Intent intent = new Intent(mContext, ClipImageBorderViewActivity.class);
            intent.putExtra("newFile", newFile);
            startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_CUT);
        }
    }

    public interface ImageResultListener {

        void cameraResult(String newFile);

        void albumResult(ArrayList<String> picturePaths);
    }

    public interface IPhotoListener {
        void sendPhoto(byte[] file);
    }

}
