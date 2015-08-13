package com.tongban.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * 话题适配器
 * Created by fushudi on 2015/7/16.
 */
public class TopicAdapter extends QuickAdapter<Topic> {

    private View.OnClickListener onClickListener;
    private TopicImgAdapter mAdapter;

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
        if (item.getContentType() == Topic.TEXT) {
            helper.setVisible(R.id.gv_content, View.GONE);
        } else {
            helper.setVisible(R.id.gv_content, View.VISIBLE);
            mAdapter.replaceAll(item.getSmallUrl());
        }
        //点赞、评论、地址
        helper.setText(R.id.tv_praise_count, item.getTopicPraiseNum());
        helper.setText(R.id.tv_reply_count, item.getTopicReplyNum());
        helper.setText(R.id.tv_location, item.getTopicAddress());
    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        mAdapter = new TopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);
        helper.setAdapter(R.id.gv_content, mAdapter);
        mAdapter.setImgClickListener(onClickListener);
    }
}
