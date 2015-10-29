package com.tongban.im.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tb.api.model.topic.Topic;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.common.Consts;

import java.util.List;

/**
 * 话题适配器
 * Created by fushudi on 2015/7/16.
 */
public class TopicListAdapter extends QuickAdapter<Topic> {

    private View.OnClickListener onClickListener;


    public TopicListAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {

        if (item.getUser_info() != null) {
            //用户信息
            if (item.getUser_info().getPortraitUrl() != null) {
                helper.setImageBitmap(R.id.iv_user_portrait,
                        item.getUser_info().getPortraitUrl().getMin(),
                        Consts.getUserDefaultPortrait());
                helper.setTag(R.id.iv_user_portrait,
                        Integer.MAX_VALUE, item.getUser_info().getUser_id());
            } else {
                helper.setImageBitmap(R.id.iv_user_portrait, Consts.getUserDefaultPortrait());
            }

            helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
            //头像点击
            helper.setTag(R.id.iv_user_portrait, Integer.MAX_VALUE, item.getUser_info().getUser_id());
            helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);
        }
        helper.setText(R.id.tv_create_time, item.getC_time(mContext));
        helper.setText(R.id.tv_topic_title, item.getTopic_title());

        Drawable iconType = mContext.getResources().getDrawable(com.voice.tongban.R.color.transparent);

        if (item.getTopicContent() != null) {
            if (item.getTopicContent().getTopic_img_url() != null
                    && item.getTopicContent().getTopic_content_voice() != null) {
                iconType = mContext.getResources().getDrawable(com.voice.tongban.R.mipmap.ic_voice_pic);

            } else if (item.getTopicContent().getTopic_img_url() != null) {
                iconType = mContext.getResources().getDrawable(com.voice.tongban.R.mipmap.ic_pic);

            } else if (item.getTopicContent().getTopic_content_voice() != null) {
                iconType = mContext.getResources().getDrawable(com.voice.tongban.R.mipmap.ic_voice);

            }
            iconType.setBounds(0, 0, iconType.getMinimumWidth(), iconType.getMinimumHeight());
            ((TextView) helper.getView(com.voice.tongban.R.id.tv_topic_title))
                    .setCompoundDrawables(null, null, iconType, null);
        }

        //回复、收藏、地址
        helper.setText(R.id.tv_comment_count, item.getComment_amount() + "人回答");
        helper.setText(R.id.tv_collect_count, item.getCollect_amount()+ "人同问");
        setUserPortrait(helper, item);
    }

    private void setUserPortrait(BaseAdapterHelper helper, Topic item) {
        ((FlowLayout) helper.getView(R.id.fl_portrait_container)).setIsSingleLine(true);
        ((FlowLayout) helper.getView(R.id.fl_portrait_container)).removeAllViews();
        for (int i = 0; i < 10; i++) {
            ImageView tv = (ImageView) LayoutInflater.from(mContext)
                    .inflate(R.layout.include_user_portrait
                            , ((FlowLayout) helper.getView(R.id.fl_portrait_container)), false);
            tv.setImageResource(Consts.getUserDefaultPortrait());
            ((FlowLayout) helper.getView(R.id.fl_portrait_container)).addView(tv);
        }
    }

}
