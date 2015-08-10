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
import com.tongban.im.R;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 *  话题适配器
 * Created by fushudi on 2015/7/16.
 */
public class TopicAdapter extends BaseAdapter {

    private LayoutInflater inflate;
    private Context context;
    private List<Topic> topicList;
    private ViewHolder viewHolder;
    private TopicImgAdapter adapter;

    public TopicAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
        inflate = LayoutInflater.from(context);
        adapter = new TopicImgAdapter(context, R.layout.item_topic_grid_img, null);
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Topic topic = topicList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflate.inflate(R.layout.item_topic_list, parent, false);
            //用户信息
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_topic_icon);
            viewHolder.tvNickName = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            //话题内容
            viewHolder.tvTopicName = (TextView) convertView.findViewById(R.id.tv_topic_name);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_topic_content);
            viewHolder.gvContent = (GridView) convertView.findViewById(R.id.gv_content);
            viewHolder.gvContent.setAdapter(adapter);
            //点赞、评论、地址
            viewHolder.tvPraiseNum = (TextView) convertView.findViewById(R.id.tv_praise_count);
            viewHolder.tvReplyNum = (TextView) convertView.findViewById(R.id.tv_reply_count);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_location);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(topic.getUser().getPortrait_url()).
                placeholder(R.drawable.rc_default_portrait).into(viewHolder.ivIcon);
        viewHolder.tvTopicName.setText(topic.getTopicName());
        viewHolder.tvPraiseNum.setText(topic.getTopicPraiseNum());
        viewHolder.tvTime.setText(topic.getTopicTime());
        viewHolder.tvAge.setText(topic.getUser().getAge());
        viewHolder.tvAddress.setText(topic.getUser().getAddress());
        viewHolder.tvNickName.setText(topic.getUser().getNick_name());
        viewHolder.tvReplyNum.setText(topic.getTopicReplyNum());

        if (topic.getContentType() == Topic.TEXT) {

            viewHolder.tvContent.setText(topic.getTopicContent());
            viewHolder.tvContent.setVisibility(View.VISIBLE);
            viewHolder.gvContent.setVisibility(View.GONE);

        } else {

            viewHolder.tvContent.setVisibility(View.GONE);
            viewHolder.gvContent.setVisibility(View.VISIBLE);
            adapter.replaceAll(topic.getSmallUrl());
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView ivIcon;
        public TextView tvNickName, tvAge, tvAddress, tvTime, tvTopicName,
                tvPraiseNum, tvReplyNum, tvContent;
        public GridView gvContent;
    }
}
