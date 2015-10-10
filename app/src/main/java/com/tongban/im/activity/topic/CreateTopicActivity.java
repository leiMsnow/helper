package com.tongban.im.activity.topic;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

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

import butterknife.Bind;
import butterknife.OnTextChanged;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends CommonImageResultActivity implements
        CommonImageResultActivity.ImageResultListener {

    @Bind(R.id.et_topic_name)
    EditText tvTitle;
    @Bind(R.id.et_topic_content)
    EditText tvContent;
    @Bind(R.id.ll_add_img)
    TopicImageView gvTopicImg;
    @Bind(R.id.btn_sr)
    Button btn;

    private MenuItem menuCreate;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_topic;
    }

    @Override
    protected void initData() {
        setTitle(R.string.create_topic);
        gvTopicImg.setAdapterImgCount(TopicImageView.IMAGE_COUNT_CREATE);
        setImageResultListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_topic, menu);
        menuCreate = menu.findItem(R.id.menu_create);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.menu_create) {
            if (gvTopicImg.getSelectedFile().size() > 0) {
                uploadImage();
            } else if (!TextUtils.isEmpty(tvContent.getText().toString().trim())) {
                TopicApi.getInstance().createTopic(tvTitle.getText().toString().trim(),
                        tvContent.getText().toString().trim(), new ArrayList<ImageUrl>(),
                        CreateTopicActivity.this);
            }
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
                        hideProgress();
                        ToastUtil.getInstance(mContext).showToast("图片上传失败");
                    }
                }, null);
    }


    public void onEventMainThread(BaseEvent.CreateTopicEvent obj) {
        ToastUtil.getInstance(mContext).showToast("话题发表成功");
        finish();
    }

    @OnTextChanged({R.id.et_topic_name, R.id.et_topic_content})
    public void afterTextChanged(Editable s) {
        if (tvTitle.getText().toString().length() > 0 &&
                (tvContent.getText().toString().length() > 0 ||
                        gvTopicImg.getSelectedFile().size() > 0)) {
            menuCreate.setEnabled(true);
        } else {
            menuCreate.setEnabled(false);
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
