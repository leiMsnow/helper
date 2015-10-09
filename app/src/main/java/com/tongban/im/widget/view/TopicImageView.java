package com.tongban.im.widget.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.adapter.CreateTopicImgAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题评论自定义grid控件
 * Created by fushudi on 2015/8/14.
 */
public class TopicImageView extends LinearLayout implements View.OnClickListener {

    // 发表图片最大数量
    public final static int IMAGE_COUNT_CREATE = 15;
    // 评论图片最大数量
    public final static int IMAGE_COUNT_REPLY = 3;

    private Context mContext;
    private FlowLayout gvReplyImg;
    private CameraView mCameraView;

    private CreateTopicImgAdapter mAdapter;

    private int selectIndex = 0;


    //当前选择的图片数量
    private List<String> selectedFile = new ArrayList<>();

    public List<String> getSelectedFile() {
        selectedFile.clear();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (!TextUtils.isEmpty(mAdapter.getItem(i)))
                selectedFile.add(0, mAdapter.getItem(i));
        }
        return selectedFile;
    }

    public void setAdapterImgCount(int imgCount) {
        this.mAdapter.setImgCount(imgCount);
    }

    public TopicImageView(Context context) {
        this(context, null);
    }

    public TopicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initData();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_topic_image_flow, this);
        gvReplyImg = (FlowLayout) findViewById(R.id.fl_reply_img);
        selectedFile.add("");
        mAdapter = new CreateTopicImgAdapter(mContext, 0, selectedFile);
        selectedFile.clear();
    }

    private void initData() {
        gvReplyImg.removeAllViews();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (i == mAdapter.getImgCount()) {
                break;
            }
            gvReplyImg.addView(mAdapter.getChildView(i, gvReplyImg, this));
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.iv_topic_img:
                boolean isEmpty = (boolean) v.getTag(Integer.MAX_VALUE);
                selectIndex = (int) v.getTag(Integer.MIN_VALUE);
                //判断是删除还是增加
                if (isEmpty) {
                    createDialog();
                } else {
                    notifyChange("item_remove");
                }
                break;
        }
    }

    //刷新图片Adapter
    public void notifyChange(String picturePath) {
        if (mAdapter == null) {
            return;
        }
        // 如果点击的不是（加号+）清除当前选中项
        if (picturePath.equals("item_remove")) {
            if (mAdapter.getCount() == mAdapter.getImgCount()) {
                if (!mAdapter.getItem(mAdapter.getCount() - 1).equals(""))
                    mAdapter.add("");
            }
            mAdapter.remove(selectIndex);
        }
        // 添加子项
        else {
            if ((selectIndex + 1) == mAdapter.getImgCount()) {
                mAdapter.set(selectIndex, picturePath);
            } else {
                mAdapter.add(selectIndex, picturePath);
            }
        }
        initData();
        //是否已经达到最大数量
        if ((mAdapter.getCount()) == (mAdapter.getImgCount() + 1)) {
            if (mAdapter.getItem(mAdapter.getCount() - 1).equals(""))
                mAdapter.remove(mAdapter.getCount() - 1, false);
            return;
        }
    }

    // 打开相机的提示框
    private void createDialog() {
        if (mCameraView == null) {
            mCameraView = new CameraView(mContext);
        }
        int current = mAdapter.getCount() - 1;
        mCameraView.setCurrent(current);
        mCameraView.setMaxSelect(mAdapter.getImgCount());
        mCameraView.show();
    }

    /**
     * 清除图片
     */
    public void clearImageInfo() {
        selectedFile.clear();
        mAdapter.clear();
        mAdapter.add("");
        initData();
    }
}
