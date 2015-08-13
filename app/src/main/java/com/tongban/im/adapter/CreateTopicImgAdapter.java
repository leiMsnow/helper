package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.AlertView;

import java.util.List;

/**
 * 创建话题添加图片Adapter
 * Created by fushudi on 2015/7/16.
 */
public class CreateTopicImgAdapter extends QuickAdapter<String> {
    private AlertView dialog;
    private LinearLayout mCamera;
    private LinearLayout mGallery;

    public int getImgCount() {
        return imgCount;
    }

    private int imgCount = 3;
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }

    public CreateTopicImgAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper helper, final String item) {
        if (TextUtils.isEmpty(item)) {
            helper.setImageBitmap(R.id.iv_topic_img, R.mipmap.ic_menu_add);
        } else {
            helper.setImageBitmap(R.id.iv_topic_img, item);
        }
        helper.setOnClickListener(R.id.iv_topic_img, onClickListener);


    }

}
