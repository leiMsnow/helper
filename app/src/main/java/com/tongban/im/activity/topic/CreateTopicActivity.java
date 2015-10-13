package com.tongban.im.activity.topic;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.callback.MultiUploadFileCallback;
import com.tongban.im.api.callback.UploadVoiceCallback;
import com.tongban.im.common.TopicVoiceTimerCount;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.topic.TopicContent;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.TopicImageView;
import com.voice.tongban.utils.KDXFRecognizerUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 发表话题界面
 *
 * @author fushudi
 */
public class CreateTopicActivity extends CommonImageResultActivity implements
        CommonImageResultActivity.ImageResultListener
        , KDXFRecognizerUtils.RecognizerResultListener
        , KDXFRecognizerUtils.PlayResultListener
         {

    @Bind(R.id.et_topic_name)
    EditText etTitle;
    @Bind(R.id.et_topic_content)
    EditText etContent;
    @Bind(R.id.ll_add_img)
    TopicImageView gvTopicImg;
    @Bind(R.id.btn_voice)
    Button btnRecognizer;
    @Bind(R.id.btn_play)
    Button btnPlay;

    private MenuItem menuCreate;

    private KDXFRecognizerUtils kdxfRecognizer;
    private TopicVoiceTimerCount voiceTimerCount;


    private String voiceUrl = "";
    private List<ImageUrl> mUrls;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_topic;
    }

    @Override
    protected void initData() {
        setTitle(R.string.create_topic);

        gvTopicImg.setAdapterImgCount(TopicImageView.IMAGE_COUNT_CREATE);
        setImageResultListener(this);

        kdxfRecognizer = new KDXFRecognizerUtils(mContext);
        kdxfRecognizer.setResultListener(this);
        kdxfRecognizer.setPlayListener(this);
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
            } else if (!TextUtils.isEmpty(etContent.getText().toString().trim())) {
                uploadVoice();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (etTitle.getText().toString().length() > 0 ||
                etContent.getText().toString().length() > 0 ||
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

    @OnClick({R.id.btn_voice, R.id.btn_play})
    public void onSRListener(View v) {
        if (v == btnRecognizer) {
            kdxfRecognizer.startRecognizer();
        } else if (v == btnPlay) {
            if (!btnPlay.isSelected()) {
                kdxfRecognizer.playRecognizer();
            } else {
                kdxfRecognizer.onVoiceStop();
            }
        }
    }


    //刷新图片Adapter
    public void notifyChange(String picturePath) {
        gvTopicImg.notifyChange(picturePath);
        afterTextChanged(null);
    }

    //批量上传图片
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
                        mUrls = urls;
                        uploadVoice();
                    }

                    @Override
                    public void uploadFailed(String error) {
                        hideProgress();
                        ToastUtil.getInstance(mContext).showToast("图片上传失败");
                    }
                }, null);
    }

    // 如果有录音，将会上传录音
    private void uploadVoice() {
        showProgress();
        if (!TextUtils.isEmpty(kdxfRecognizer.getVoiceUrl())) {
            FileUploadApi.getInstance().uploadVoice(kdxfRecognizer.getVoiceUrl(),
                    new UploadVoiceCallback() {
                        @Override
                        public void uploadSuccess(String url) {
                            voiceUrl = url;
                            sendNewTopic();
                        }

                        @Override
                        public void uploadFailed(String error) {
                            hideProgress();
                            ToastUtil.getInstance(mContext).showToast("录音上传失败");
                        }
                    });
        } else {
            sendNewTopic();
        }
    }


    private void sendNewTopic() {

        TopicContent content = new TopicContent();
        content.setTopic_content_text(etContent.getText().toString().trim());
        content.setTopic_content_voice(voiceUrl);
        content.setTopic_img_url(mUrls);

        TopicApi.getInstance().createTopic(
                etTitle.getText().toString().trim()
                , content
                , this);
    }


    public void onEventMainThread(BaseEvent.CreateTopicEvent obj) {
        ToastUtil.getInstance(mContext).showToast("发表成功");
        finish();
    }

    @OnTextChanged({R.id.et_topic_name, R.id.et_topic_content})
    public void afterTextChanged(Editable s) {
        if (etTitle.getText().toString().length() > 0 &&
                (etContent.getText().toString().length() > 0 ||
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

    @Override
    public void recognizerResult(String result) {
        etContent.requestFocus();
        etContent.setText(result);
        etContent.setSelection(etContent.getText().length());

        btnRecognizer.setVisibility(View.INVISIBLE);
        btnPlay.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        kdxfRecognizer.onDestroy();
    }

    @Override
    public void onPlayStart(long timeout) {
        voiceTimerCount = new TopicVoiceTimerCount(btnPlay, timeout);
        voiceTimerCount.start();
    }

    @Override
    public void onPlayEnd() {
        btnPlay.setText(getString(R.string.topic_content_play));
        btnPlay.setSelected(false);
        if (voiceTimerCount != null) {
            voiceTimerCount.cancel();
            voiceTimerCount = null;
        }
    }

}
