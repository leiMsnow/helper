package com.tongban.im.activity.topic;

import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.callback.MultiUploadFileCallback;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.TopicImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends CommonImageResultActivity implements View.OnClickListener,
        TextWatcher, CommonImageResultActivity.ImageResultListener {

    private TopicImageView gvTopicImg;
    private EditText tvTitle;
    private EditText tvContent;
    private ImageView ivSend;

    private final static int IMAGE_COUNT = 15;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_topic;
    }

    @Override
    protected void initView() {
        setTitle(R.string.create_topic);
        tvTitle = (EditText) findViewById(R.id.et_topic_name);
        tvContent = (EditText) findViewById(R.id.et_topic_content);
        gvTopicImg = (TopicImageView) findViewById(R.id.ll_add_img);
    }

    @Override
    protected void initData() {
        gvTopicImg.getmAdapter().setImgCount(IMAGE_COUNT);
    }

    @Override
    protected void initListener() {
        setImageResultListener(this);
        tvTitle.addTextChangedListener(this);
        tvContent.addTextChangedListener(this);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        if (getSupportActionBar() != null) {
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
            ivSend.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (tvTitle.getText().toString().length() > 0 ||
                tvContent.getText().toString().length() > 0 ||
                gvTopicImg.getSelectedFile().size() > 0) {
            BaseDialog.Builder dialog = new BaseDialog.Builder(mContext);
            dialog.setMessage("放弃发表?");
            dialog.setPositiveButton("我要离开",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });
            dialog.setNegativeButton("继续发表",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivSend) {
            if (gvTopicImg.getSelectedFile().size() > 0) {
                uploadImage();
            } else if (!TextUtils.isEmpty(tvContent.getText().toString().trim())) {
                TopicApi.getInstance().createTopic(tvTitle.getText().toString().trim(),
                        tvContent.getText().toString().trim(), new ArrayList<ImageUrl>(),
                        CreateTopicActivity.this);
            }
        }
    }

    //刷新图片Adapter
    public void notifyChange(String picturePath) {
        gvTopicImg.notifyChange(picturePath);
        afterTextChanged(null);
    }

    //批量上传图片,成功后将发表话题
    private void uploadImage() {
        showProgress();
        FileUploadApi.getInstance().uploadFile(
                new ArrayList<ImageUrl>(),
                0,
                gvTopicImg.getSelectedFile(),
                FileUploadApi.IMAGE_SIZE_300,
                FileUploadApi.IMAGE_SIZE_500,
                new MultiUploadFileCallback() {
                    @Override
                    public void uploadSuccess(List<ImageUrl> urls) {
                        TopicApi.getInstance().createTopic(tvTitle.getText().toString().trim(),
                                tvContent.getText().toString().trim(), urls,
                                CreateTopicActivity.this);
                    }

                    @Override
                    public void uploadFailed(String error) {
                        ToastUtil.getInstance(mContext).showToast("图片上传失败");
                    }
                }, null);
    }


    public void onEventMainThread(BaseEvent.CreateTopicEvent obj) {
        ToastUtil.getInstance(mContext).showToast("话题发表成功");
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (tvTitle.getText().toString().length() > 0 &&
                (tvContent.getText().toString().length() > 0 ||
                        gvTopicImg.getSelectedFile().size() > 0)) {
            ivSend.setEnabled(true);
        } else {
            ivSend.setEnabled(false);
        }
    }

    @Override
    public void cameraResult(String newFile) {
        notifyChange(newFile);
    }

    @Override
    public void albumResult(ArrayList<String> picturePaths) {
        for (int i = picturePaths.size() - 1; i >= 0; i--) {
            String newFile = CameraUtils.saveToSD(picturePaths.get(i));
            cameraResult(newFile);
        }
    }
}
