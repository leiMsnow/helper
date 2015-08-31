package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * 话题列表--图片展示Adapter
 * Created by fushudi on 2015/7/16.
 */
public class TopicImgAdapter extends QuickAdapter<ImageUrl> {


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
    protected void convert(final BaseAdapterHelper helper, final ImageUrl item) {
        helper.setImageBitmap(R.id.iv_topic_img, item.getMid(), R.drawable.rc_ic_def_rich_content);

        helper.setTag(R.id.iv_topic_img, tagKey, getDataAll());
        helper.setOnClickListener(R.id.iv_topic_img, imgClickListener);
    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        ViewGroup.LayoutParams lp = helper.getView(R.id.iv_topic_img).getLayoutParams();
        lp.height = mScreenWidth;
        lp.width = mScreenWidth;
        helper.getView(R.id.iv_topic_img).setLayoutParams(lp);
    }
}
