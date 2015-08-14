package com.tongban.im.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.CreateTopicImgAdapter;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.AlertView;
import com.tongban.im.widget.view.TopicInputView;

import java.io.File;

/**
 * 通用的图片添加父类
 * Created by fushudi on 2015/8/13.
 */
public class CommonImageResultActivity extends BaseToolBarActivity {
    protected TopicInputView topicInputView;

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
                    topicInputView.notifyChange(newFile);
                }
            }
        } else if (requestCode == CameraUtils.OPEN_ALBUM) {
            String picturePath = CameraUtils.searchUriFile(mContext, data);
            if (picturePath == null) {
                picturePath = data.getData().getPath();
            }
            String newFile = CameraUtils.saveToSD(picturePath);
            topicInputView.notifyChange(newFile);
        }
    }

}
