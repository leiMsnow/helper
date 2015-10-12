package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.model.topic.Topic;

import java.util.List;

/**
 * 话题适配器
 * Created by fushudi on 2015/7/16.
 */
public class TopicListAdapter extends QuickAdapter<Topic> {

    private View.OnClickListener onClickListener;

    private int[] images = new int[]{R.id.iv_small_img_1, R.id.iv_small_img_2, R.id.iv_small_img_3};

    public TopicListAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {
        if (helper.getPosition() % 2 == 0) {
            helper.getConvertView().setBackgroundResource(R.drawable.selector_item_list_white);
        } else {
            helper.getConvertView().setBackgroundResource(R.drawable.selector_item_list_grey);
        }
        if (item.getUser_info() != null) {
            //用户信息
            if (item.getUser_info().getPortrait_url() != null) {
                helper.setImageBitmap(R.id.iv_user_portrait,
                        item.getUser_info().getPortrait_url().getMin(),
                        Consts.getUserDefaultPortrait());
                helper.setTag(R.id.iv_user_portrait,
                        Integer.MAX_VALUE, item.getUser_info().getUser_id());
            } else {
                helper.setImageBitmap(R.id.iv_user_portrait, Consts.getUserDefaultPortrait());
            }

            helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
            helper.setTag(R.id.iv_user_portrait, Integer.MAX_VALUE, item.getUser_info().getUser_id());
            helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);
        }
        helper.setText(R.id.tv_create_time, item.getC_time(mContext));
        helper.setText(R.id.tv_topic_title, item.getTopic_title());
        // 语音内容
        if (!TextUtils.isEmpty(item.getTopicContent().getTopic_content_voice())) {
            helper.setVisible(R.id.iv_topic_voice,View.VISIBLE);
        } else {
            helper.setVisible(R.id.iv_topic_voice, View.GONE);
        }
        // 文本内容
        helper.setText(R.id.tv_topic_content, item.getTopicContent().getTopic_content_text());
        if (item.getContentType() == Topic.IMAGE) {
            setImagesVisibleAndUrl(helper, item);
            helper.setVisible(R.id.ll_small_img_parent, View.VISIBLE);
        } else {
            helper.setVisible(R.id.ll_small_img_parent, View.GONE);
        }
        //回复、收藏、地址
        helper.setText(R.id.tv_comment_count, String.valueOf(item.getComment_amount()));
        helper.setText(R.id.tv_collect_count, String.valueOf(item.getCollect_amount()));

    }

    //设置图片的显示/隐藏和src
    private void setImagesVisibleAndUrl(final BaseAdapterHelper helper, final Topic item) {
        int count = item.getTopicContent().getTopic_img_url().size() > 3 ? 3
                : item.getTopicContent().getTopic_img_url().size();
        for (int i = 0; i < images.length; i++) {
            helper.setVisible(images[i], View.INVISIBLE);
            if (i < count) {
                helper.setVisible(images[i], View.VISIBLE);
                helper.setImageBitmap(images[i],
                        item.getTopicContent().getTopic_img_url()
                                .get(i).getMin(), R.mipmap.ic_default_image);
                helper.setTag(images[i], Integer.MAX_VALUE,
                        item.getTopicContent().getTopic_img_url());
                helper.setOnClickListener(images[i], onClickListener);
            }
        }
    }
}
