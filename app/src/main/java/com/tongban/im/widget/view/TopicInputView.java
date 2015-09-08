package com.tongban.im.widget.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.CreateTopicImgAdapter;

/**
 * 话题评论自定义控件
 * Created by fushudi on 2015/8/14.
 */
public class TopicInputView extends LinearLayout implements View.OnClickListener,
        TextWatcher {

    private View rootView;
    private ImageView ivAddImg;
    private EditText etComment;
    private ImageView ivComment;
    private TextView tvCommentLength;
    private GridView gvReplyImg;

    private CameraView mCameraView;
    private CreateTopicImgAdapter mAdapter;
    private Context mContext;

    private onClickCommentListener onClickCommentListener;

    private String repliedName;
    private String repliedUserId;
    private String repliedCommentId;

    private boolean isFirst = true;
    private boolean isUp = false;
    private int mRootLocation;

    private int mCommentLength = 500;

    public EditText getEtComment() {
        return etComment;
    }

    public void setOnClickCommentListener(onClickCommentListener onClickCommentListener) {
        this.onClickCommentListener = onClickCommentListener;
    }

    public void setAdapterImgCount(int imgCount) {
        this.mAdapter.setImgCount(imgCount);
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
        LayoutInflater.from(mContext).inflate(R.layout.include_topic_input, this);
        rootView = findViewById(R.id.ll_input_root);
        ivAddImg = (ImageView) findViewById(R.id.iv_add_img);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvCommentLength = (TextView) findViewById(R.id.tv_comment_length);
        ivComment = (ImageView) findViewById(R.id.btn_comment);
        gvReplyImg = (GridView) findViewById(R.id.gv_reply_img);
        mAdapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);

        tvCommentLength.setText(String.valueOf(mCommentLength));
    }

    private void initListener() {
        ivAddImg.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        etComment.addTextChangedListener(this);
        mAdapter.setOnClickListener(this);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 第一次记录控件位置
                        if (isFirst) {
                            mRootLocation = TopicInputView.this.getTop();
                            isFirst = false;
                        }
                        // 如果键盘抬起,记录up状态
                        if (TopicInputView.this.getTop() < mRootLocation) {
                            isUp = true;
                        }
                        //如果是up并且高度变回来了，就清除回复状态
                        if (isUp && TopicInputView.this.getTop() == mRootLocation) {
                            isUp = false;
                            clearCommentInfo();
                        }
                    }
                });
    }

    private void initData() {
        gvReplyImg.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == ivComment) {
            if (etComment.getText().toString().length() > 0) {
                if (onClickCommentListener != null)
                    onClickCommentListener.onClickComment(etComment.getText().toString(),
                            repliedCommentId, repliedName, repliedUserId);
            }
        } else if (v == ivAddImg) {
            if (gvReplyImg.getVisibility() == View.VISIBLE) {
                gvReplyImg.setVisibility(View.GONE);
            } else {
                if (mAdapter.getCount() == 0) {
                    mAdapter.add("");
                }
                gvReplyImg.setVisibility(View.VISIBLE);
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

    //刷新图片Adapter
    public void notifyChange(String picturePath) {
        if (mAdapter == null) {
            return;
        }
        if (mAdapter.getCount() == mAdapter.getImgCount()) {
            mAdapter.remove(mAdapter.getCount() - 1, false);
        }
        mAdapter.add(0, picturePath);
    }

    // 打开相机的提示框
    protected void createDialog() {
        if (mCameraView == null) {
            mCameraView = new CameraView(mContext);
        }
        mCameraView.show();
    }

    /**
     * 清除回复内容
     */
    public void clearCommentInfo() {

        repliedName = null;
        repliedUserId = null;
        repliedCommentId = null;

        etComment.setText("");
        etComment.setHint(mContext.getResources().getString(R.string.create_comment));
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
        int currentLength = mCommentLength - etComment.getText().length();
        if (currentLength < 0) {
            currentLength = 0;
        }
        tvCommentLength.setText(String.valueOf(currentLength));
    }

    /**
     * 回复按钮点击监听
     */
    public interface onClickCommentListener {
        void onClickComment(String commentContent, String repliedCommentId,
                            String repliedName, String repliedUserId);
    }
}
