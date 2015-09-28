package com.tongban.im.widget.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.callback.MultiUploadFileCallback;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.ImageUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题评论自定义控件
 * Created by fushudi on 2015/8/14.
 */
public class TopicInputView extends LinearLayout implements View.OnClickListener,
        TextWatcher {

    private Context mContext;

    private View rootView;
    private ImageView ivAddImg;
    private EditText etComment;
    private ImageView ivComment;
    private TextView tvCommentLength;
    private TopicImageView gvReplyImg;

    private IKeyboardListener keyboardListener;
    private IOnClickCommentListener onClickCommentListener;

    private String repliedName;
    private String repliedUserId;
    private String repliedCommentId;

    private boolean isFirst = true;
    private boolean isUp = false;
    private boolean isClearImage = true;
    private int mRootLocation;

    private int mCommentLength = 500;

    public void setKeyboardListener(IKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public EditText getEtComment() {
        return etComment;
    }

    public void setOnClickCommentListener(IOnClickCommentListener onClickCommentListener) {
        this.onClickCommentListener = onClickCommentListener;
    }

    public void setAdapterImgCount(int imgCount) {
        gvReplyImg.setAdapterImgCount(imgCount);
    }

    public TopicInputView(Context context) {
        this(context, null);
    }

    public TopicInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();
        initData();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_topic_input, this);
        rootView = findViewById(R.id.ll_input_root);
        ivAddImg = (ImageView) findViewById(R.id.iv_add_img);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvCommentLength = (TextView) findViewById(R.id.tv_comment_length);
        ivComment = (ImageView) findViewById(R.id.btn_comment);
        gvReplyImg = (TopicImageView) findViewById(R.id.ll_reply_img);
        tvCommentLength.setText(String.valueOf(mCommentLength));

        gvReplyImg.getAdapter().setImgCount(3);
        ivComment.setEnabled(false);
    }

    private void initListener() {
        ivAddImg.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        etComment.addTextChangedListener(this);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if (TopicInputView.this.getVisibility() == VISIBLE) {
                            // 第一次记录控件位置
                            if (isFirst) {
                                mRootLocation = TopicInputView.this.getTop();
                                isFirst = false;
                            }
                            // 如果键盘抬起,记录up状态
                            if (TopicInputView.this.getTop() < mRootLocation) {
                                if (!TransferCenter.getInstance().startLogin()) {
                                    return;
                                }
                                isUp = true;
                            }
                            //如果是up并且高度变回来了，就清除回复状态
                            if (isUp && TopicInputView.this.getTop() == mRootLocation) {
                                isUp = false;
                                clearCommentInfo(isClearImage);
                            }
                            //键盘事件回调
                            if (keyboardListener != null) {
                                keyboardListener.boardStatus(isUp);
                            }
                        }
                    }
                });
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        if (v == ivComment) {
            if (gvReplyImg.getSelectedFile() != null && gvReplyImg.getSelectedFile().size() > 0) {
                uploadImage();
            } else {
                if (onClickCommentListener != null)
                    onClickCommentListener.onClickComment(etComment.getText().toString(),
                            repliedCommentId, repliedName, repliedUserId, null);
            }
        } else if (v == ivAddImg) {
            if (!TransferCenter.getInstance().startLogin()) {
                return;
            }
            isClearImage = gvReplyImg.getSelectedFile().size() == 0;
            gridViewVisibility(false);
        }
    }

    public boolean gridViewVisibility(boolean onBackPressed) {
        if (gvReplyImg.getVisibility() == View.VISIBLE) {
            gvReplyImg.setVisibility(View.GONE);
            return false;
        } else {
            if (!onBackPressed)
                gvReplyImg.setVisibility(View.VISIBLE);
            return true;
        }
    }

    //批量上传图片,成功后将发表评论

    private void uploadImage() {
        FileUploadApi.getInstance().uploadFile(
                new ArrayList<ImageUrl>(),
                0,
                gvReplyImg.getSelectedFile(),
                FileUploadApi.IMAGE_SIZE_300,
                FileUploadApi.IMAGE_SIZE_500,
                new MultiUploadFileCallback() {
                    @Override
                    public void uploadSuccess(List<ImageUrl> urls) {
                        if (onClickCommentListener != null)
                            onClickCommentListener.onClickComment(etComment.getText().toString(),
                                    repliedCommentId, repliedName, repliedUserId, urls);
                    }

                    @Override
                    public void uploadFailed(String error) {
                        ToastUtil.getInstance(mContext).showToast("图片上传失败");
                    }
                }, null);
    }

    //    //刷新图片Adapter
    public void notifyChange(String picturePath) {
        gvReplyImg.notifyChange(picturePath);
        afterTextChanged(null);
    }

    /**
     * 清除回复内容
     */
    public void clearCommentInfo(boolean isClearImage) {

        repliedName = null;
        repliedUserId = null;
        repliedCommentId = null;
        etComment.setText("");

        if (isClearImage)
            gvReplyImg.clearImageInfo();
    }

    public void clearCommentInfo() {
        clearCommentInfo(true);
    }


    /**
     * 设置回复评论信息
     *
     * @param repliedCommentId 回复的评论Id
     * @param repliedName      回复的用户昵称
     * @param repliedUserId    回复的用户Id
     */
    public void setCommentInfo(String repliedCommentId, String repliedName, String repliedUserId) {
        this.repliedCommentId = repliedCommentId;
        this.repliedName = repliedName;
        this.repliedUserId = repliedUserId;
        etComment.setHint(" 回复" + repliedName);
        etComment.setText("");
        focusEdit();
        gvReplyImg.clearImageInfo();
    }

    /**
     * 设置焦点
     */
    public void focusEdit() {
        etComment.setFocusable(true);
        etComment.requestFocus();
        KeyBoardUtils.openKeyboard(etComment, mContext);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etComment.getText().length() > 0 ||
                gvReplyImg.getSelectedFile().size() > 0) {
            ivComment.setEnabled(true);
        } else {
            ivComment.setEnabled(false);
        }
        int currentLength = mCommentLength - etComment.getText().length();
        if (currentLength < 0) {
            currentLength = 0;
        }
        tvCommentLength.setText(String.valueOf(currentLength));
    }

    /**
     * 回复按钮点击监听
     */
    public interface IOnClickCommentListener {
        void onClickComment(String commentContent, String repliedCommentId,
                            String repliedName, String repliedUserId, List<ImageUrl> selectedFile);
    }

    public interface IKeyboardListener {
        void boardStatus(boolean isUp);
    }

}
