package com.tongban.im.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.R;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * 话题适配器
 * Created by fushudi on 2015/7/16.
 */
public class TopicAdapter extends QuickAdapter<Topic> {

    private View.OnClickListener onClickListener;

    private int[] images = new int[]{R.id.iv_small_img_1, R.id.iv_small_img_2, R.id.iv_small_img_3};

    public TopicAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {
        //用户信息
        helper.setImageBitmap(R.id.iv_topic_icon, item.getUser().getPortrait_url());
        helper.setText(R.id.tv_nickname, item.getUser().getNick_name());
        helper.setText(R.id.tv_age, item.getUser().getAge());
        helper.setText(R.id.tv_time, item.getTopicTime());
        //话题内容
        helper.setText(R.id.tv_topic_name, item.getTopicName());
        helper.setText(R.id.tv_topic_content, item.getTopicContent());

        setImagesVisibleAndUrl(helper, item);
        //点赞、评论、地址
        helper.setText(R.id.tv_praise_count, item.getTopicPraiseNum());
        helper.setText(R.id.tv_reply_count, item.getTopicReplyNum());
        helper.setText(R.id.tv_location, item.getTopicAddress());
    }

    //设置图片的显示/隐藏和src
    private void setImagesVisibleAndUrl(final BaseAdapterHelper helper, final Topic item) {
        for (int i = 0; i < images.length; i++) {
            if (item.getContentType() == Topic.TEXT) {
                helper.setVisible(images[i], View.GONE);
            } else {
                helper.setVisible(images[i], View.VISIBLE);
                helper.setImageBitmap(images[i], item.getSmallUrl().get(i));
                helper.setTag(images[i], Integer.MAX_VALUE, item.getSmallUrl());
                helper.setOnClickListener(images[i], onClickListener);
            }
        }
    }
}
