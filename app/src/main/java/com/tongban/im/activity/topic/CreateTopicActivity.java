package com.tongban.im.activity.topic;

import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.CreateTopicImgAdapter;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.MultiUploadFileCallback;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.UploadFileCallback;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.CameraView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends BaseToolBarActivity implements View.OnClickListener {

    private GridView gvTopicImg;
    private EditText tvTitle;
    private EditText tvContent;
    private ImageView ivSend;

    private CreateTopicImgAdapter adapter;
    private CameraView mCameraView;

    private final static int IMAGE_COUNT = 15;
    //当前选择的图片数量
    private List<String> selectedFile = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_topic;
    }

    @Override
    protected void initView() {
        setTitle(R.string.create_topic);
        tvTitle = (EditText) findViewById(R.id.et_topic_name);
        tvContent = (EditText) findViewById(R.id.et_topic_content);
        gvTopicImg = (GridView) findViewById(R.id.gv_add_img);
    }

    @Override
    protected void initData() {
        adapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);
        adapter.setImgCount(IMAGE_COUNT);
        adapter.add("");
        gvTopicImg.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        adapter.setOnClickListener(this);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.view_create_button);
        Toolbar.LayoutParams lp = (Toolbar.LayoutParams)
                getSupportActionBar().getCustomView().getLayoutParams();
        lp.gravity = Gravity.RIGHT;
        int margins = DensityUtils.dp2px(mContext, 8);
        lp.setMargins(margins, margins, margins, margins);
        getSupportActionBar().getCustomView().setLayoutParams(lp);
        ivSend = (ImageView) getSupportActionBar().getCustomView().findViewById(R.id.iv_send);
        ivSend.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == ivSend) {
            if (selectedFile.size() > 0) {
                uploadImage();
            } else {
                TopicApi.getInstance().createTopic(tvTitle.getText().toString().trim(),
                        tvContent.getText().toString().trim(), new ArrayList<ImageUrl>(),
                        CreateTopicActivity.this);
            }
        } else {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.iv_topic_img:
                    createDialog();
                    break;
            }
        }
    }

    // 打开相机的提示框
    protected void createDialog() {
        if (mCameraView == null) {
            mCameraView = new CameraView(mContext);
        }
        mCameraView.show();
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

    //批量上传图片,成功后将发表话题
    private void uploadImage() {
        mDialog.setMessage("正在上传图片...");
        mDialog.show();
        FileUploadApi.getInstance().uploadFile(new ArrayList<ImageUrl>(), 0, selectedFile,
                FileUploadApi.IMAGE_SIZE_300, FileUploadApi.IMAGE_SIZE_500,
                new MultiUploadFileCallback() {
                    @Override
                    public void uploadSuccess(List<ImageUrl> urls) {
                        if (mDialog != null && mDialog.isShowing())
                            mDialog.dismiss();
                        TopicApi.getInstance().createTopic(tvTitle.getText().toString().trim(),
                                tvContent.getText().toString().trim(), urls,
                                CreateTopicActivity.this);
                    }
                }, null);
    }

    //刷新图片Adapter
    public void notifyChange(String picturePath) {
        if (adapter == null) {
            return;
        }
        selectedFile.clear();
        if (adapter.getCount() == adapter.getImgCount()) {
            adapter.remove(adapter.getCount() - 1, false);
        }
        adapter.add(0, picturePath);

        for (int i = 0; i < adapter.getCount(); i++) {
            if (!TextUtils.isEmpty(adapter.getItem(i).toString()))
                selectedFile.add(0, adapter.getItem(i).toString());
        }
    }

    public void onEventMainThread(BaseEvent.CreateTopicEvent obj) {
        ToastUtil.getInstance(mContext).showToast("话题发表成功");
        finish();
    }
}
