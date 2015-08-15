package com.tongban.im.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tongban.im.R;
import com.tongban.im.adapter.CreateTopicImgAdapter;

/**
 * 话题评论自定义控件
 * Created by fushudi on 2015/8/14.
 */
public class TopicInputView extends LinearLayout implements View.OnClickListener {
    private ImageView ivAddImg;
    private GridView gvReplyImg;
    private CreateTopicImgAdapter mAdapter;
    private CameraView mCameraView;
    private Context mContext;

    public void setAdapterImgCount(int imgCount) {
        this.mAdapter.setImgCount(imgCount);
    }

    public TopicInputView(Context context) {
        this(context, null);
    }

    public TopicInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.include_topic_input, this);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        ivAddImg = (ImageView) findViewById(R.id.iv_add_img);
        gvReplyImg = (GridView) findViewById(R.id.gv_reply_img);
        mAdapter = new CreateTopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);
    }

    private void initListener() {
        ivAddImg.setOnClickListener(this);
        mAdapter.setOnClickListener(this);
    }

    private void initData() {
        gvReplyImg.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == ivAddImg) {
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
}