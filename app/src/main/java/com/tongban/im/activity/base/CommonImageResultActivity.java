package com.tongban.im.activity.base;

import android.content.Intent;

import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.TopicInputView;

import java.io.File;
import java.util.ArrayList;

/**
 * 通用的图片添加父类
 * Created by fushudi on 2015/8/13.
 */
public abstract class CommonImageResultActivity extends BaseToolBarActivity {

    protected TopicInputView topicInputView;

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
                    topicInputView.notifyChange(newFile);
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
//            String picturePath = CameraUtils.searchUriFile(mContext, data);
//            if (picturePath == null) {
//                picturePath = data.getData().getPath();
//            }
//            String newFile = CameraUtils.saveToSD(picturePath);
            ArrayList<String> picturePaths = data.getExtras().getStringArrayList("selectedImages");
            for (String picturePath : picturePaths) {
                String newFile = CameraUtils.saveToSD(picturePath);
                topicInputView.notifyChange(newFile);
            }
//            topicInputView.notifyChange(newFile);
        }
    }

}
