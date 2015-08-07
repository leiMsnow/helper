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
    public int getItemViewType(int position) {
        if (topicList.get(position).getContentType() == Topic.TEXT) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Topic topic = topicList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (topic.getContentType() == Topic.TEXT) {
                convertView = inflate.inflate(R.layout.item_topic_list_text, parent, false);
                viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_topic_content);
            } else if (topic.getContentType() == Topic.IMAGE) {
                convertView = inflate.inflate(R.layout.item_topic_list_img, parent, false);
                viewHolder.gvContent = (GridView) convertView.findViewById(R.id.gv_content);
            }
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_topic_icon);
            viewHolder.tvNickName = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.ivTopicLabel = (ImageView) convertView.findViewById(R.id.iv_topic_label);
            viewHolder.tvSex = (TextView) convertView.findViewById(R.id.tv_initiate_topic);
            viewHolder.tvPraiseNum = (TextView) convertView.findViewById(R.id.tv_praise_num);
            viewHolder.tvReplyNum = (TextView) convertView.findViewById(R.id.tv_reply_num);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvTopicName = (TextView) convertView.findViewById(R.id.tv_topic_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(topic.getUser().getPortrait_url()).
                placeholder(R.drawable.rc_default_portrait).into(viewHolder.ivIcon);
        viewHolder.tvTopicName.setText(topic.getTopicName());
        viewHolder.tvPraiseNum.setText(topic.getTopicPraiseNum());
        viewHolder.tvSex.setText(topic.getUser().getSex());
        viewHolder.tvTime.setText(topic.getTopicTime());
        viewHolder.ivTopicLabel.setImageResource(R.drawable.rc_default_portrait);
        viewHolder.tvNickName.setText(topic.getUser().getNick_name());
        viewHolder.tvReplyNum.setText(topic.getTopicReplyNum());

        if (topic.getContentType() == Topic.TEXT) {
            viewHolder.tvContent.setText(topic.getTopicContent());
        } else {
            viewHolder.gvContent.setAdapter(adapter);
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView ivIcon, ivTopicLabel;
        public TextView tvNickName, tvSex, tvTime, tvTopicName,
                tvPraiseNum, tvReplyNum, tvContent;
        public GridView gvContent;
    }
}
