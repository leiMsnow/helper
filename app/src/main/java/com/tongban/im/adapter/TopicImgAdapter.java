package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * 话题列表--图片展示Adapter
 * Created by fushudi on 2015/7/16.
 */
public class TopicImgAdapter extends QuickAdapter<String> {


    private View.OnClickListener imgClickListener;
    private int tagKey = Integer.MAX_VALUE;
    /**
     * 图片点击监听
     *
     * @param imgClickListener
     */
    public void setImgClickListener(View.OnClickListener imgClickListener) {
        this.imgClickListener = imgClickListener;
    }

    public TopicImgAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper helper, final String item) {
        helper.setImageBitmap(R.id.iv_topic_img, item);
        helper.setTag(R.id.iv_topic_img,tagKey, getDataAll());
        helper.setOnClickListener(R.id.iv_topic_img, imgClickListener);
    }
}
