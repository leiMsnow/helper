package com.tongban.im.activity.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tongban.im.activity.ClipImageBorderViewActivity;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.TopicInputView;

import java.io.File;

/**
 * 通用的照片处理父类
 * 裁剪
 * 拍照
 * 相册
 * Created by fushudi on 2015/8/13.
 */
public class CameraResultActivity extends AccountBaseActivity {


    private IPhotoListener mPhotoListener;

    public void setmPhotoListener(IPhotoListener mPhotoListener) {
        this.mPhotoListener = mPhotoListener;
    }

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
                    Intent intent = new Intent(mContext, ClipImageBorderViewActivity.class);
                    intent.putExtra("newFile", newFile);
                    startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_CUT);
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
            String picturePath = CameraUtils.searchUriFile(mContext, data);
            if (picturePath == null) {
                picturePath = data.getData().getPath();
            }
            String newFile = CameraUtils.saveToSD(picturePath);
            Intent intent = new Intent(mContext, ClipImageBorderViewActivity.class);
            intent.putExtra("newFile", newFile);
            startActivityForResult(intent, CameraUtils.PHOTO_REQUEST_CUT);
        } else if (requestCode == CameraUtils.PHOTO_REQUEST_CUT) {
            byte[] bytes = data.getByteArrayExtra("bitmap");
//            String photo = bytes.toString();
            if (mPhotoListener != null) {
                mPhotoListener.sendPhoto(bytes);
            }
        }
    }

    public interface IPhotoListener {
        void sendPhoto(byte[] file);
    }

}
