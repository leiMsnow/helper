package com.tongban.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.im.R;
import com.tongban.im.model.AuthorityTopic;

import java.util.List;


/**
 * 官方话题评论Adapter
 * Created by fushudi on 2015/8/10.
 */
public class AuthorityTopicDetailsAdapter extends BaseAdapter {
    private LayoutInflater inflate;
    private Context context;
    private ViewHolder viewHolder;
    private List<AuthorityTopic> authorityTopicList;

    public AuthorityTopicDetailsAdapter(Context context, List<AuthorityTopic> authorityTopicList) {
        this.context = context;
        this.authorityTopicList = authorityTopicList;
        inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return authorityTopicList.size();
    }

    @Override
    public Object getItem(int position) {
        return authorityTopicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AuthorityTopic authorityTopic = authorityTopicList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();

            //产品相关
            if (authorityTopic.getContentType() == AuthorityTopic.CONTENT) {
                convertView = inflate.inflate(R.layout.item_authority_topic_details_content, parent, false);
                viewHolder.ivProductIcon = (ImageView) convertView.findViewById(R.id.iv_product_icon);
                viewHolder.ivProductImg = (ImageView) convertView.findViewById(R.id.iv_product_img);
                viewHolder.tvProductName = (TextView) convertView.findViewById(R.id.tv_product_name);
                viewHolder.tvProductAdvantage = (TextView) convertView.findViewById(R.id.tv_product_advantage_content);
                viewHolder.tvProductDisAdvantage = (TextView) convertView.findViewById(R.id.tv_product_disadvantage_content);
                viewHolder.tvProductParameter = (TextView) convertView.findViewById(R.id.tv_product_parameters_content);
                viewHolder.tvProductIntroduction = (TextView) convertView.findViewById(R.id.tv_product_introduce_content);
                viewHolder.tvProductCollectNum = (TextView) convertView.findViewById(R.id.tv_collect_num);
            }
            //数量（评论、点赞）相关
            else if (authorityTopic.getContentType() == AuthorityTopic.REPLY_NUM) {
                convertView = inflate.inflate(R.layout.item_authority_topic_details_reply_num, parent, false);
                viewHolder.tvPraiseNum = (TextView) convertView.findViewById(R.id.tv_praise_count);
                viewHolder.tvReplyNum = (TextView) convertView.findViewById(R.id.tv_reply_count);
                viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_location);
            }
            //评论相关
            else if (authorityTopic.getContentType() == AuthorityTopic.REPLY) {
                convertView = inflate.inflate(R.layout.item_topic_reply_list, parent, false);
                viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_user_icon);
                viewHolder.tvReplyTime = (TextView) convertView.findViewById(R.id.tv_reply_time);
                viewHolder.tvReplyContent = (TextView) convertView.findViewById(R.id.tv_reply_content);
                viewHolder.tvNickName = (TextView) convertView.findViewById(R.id.tv_user_name);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //产品相关
        if (authorityTopic.getContentType() == AuthorityTopic.CONTENT) {

            Glide.with(context).load(authorityTopic.getProduct().getProduct_icon_url()).
                    placeholder(R.drawable.rc_default_portrait).into(viewHolder.ivProductIcon);
            Glide.with(context).load(authorityTopic.getProduct().getProduct_img_url()).
                    placeholder(R.drawable.rc_default_portrait).into(viewHolder.ivProductImg);
            viewHolder.tvProductName.setText(authorityTopic.getProduct().getProductName());
            viewHolder.tvProductIntroduction.setText(authorityTopic.getProduct().getProductIntroduction());
            viewHolder.tvProductParameter.setText(authorityTopic.getProduct().getProductParameter());
            viewHolder.tvProductAdvantage.setText(authorityTopic.getProduct().getProductAdvantage());
            viewHolder.tvProductDisAdvantage.setText(authorityTopic.getProduct().getProductDisAdvantage());
            viewHolder.tvProductCollectNum.setText(authorityTopic.getProduct().getProductCollectNum());

        }
        //数量（评论、点赞）相关
        else if (authorityTopic.getContentType() == AuthorityTopic.REPLY_NUM) {

            viewHolder.tvPraiseNum.setText(authorityTopic.getTopic().getTopicPraiseNum());
            viewHolder.tvReplyNum.setText(authorityTopic.getTopic().getTopicReplyNum());
            viewHolder.tvAddress.setText(authorityTopic.getTopic().getTopicAddress());

        }
        //评论相关
        else if (authorityTopic.getContentType() == AuthorityTopic.REPLY) {

            Glide.with(context).load(authorityTopic.getTopicReply().getPortrait_url()).
                    placeholder(R.drawable.rc_default_portrait).into(viewHolder.ivIcon);
            viewHolder.tvReplyTime.setText(authorityTopic.getTopicReply().getReplyTime());
            viewHolder.tvReplyContent.setText(authorityTopic.getTopicReply().getReplyContent());
            viewHolder.tvNickName.setText(authorityTopic.getTopicReply().getReplyNickName());

        }

        return convertView;
    }

    class ViewHolder {
        /**
         * 产品相关
         */
        public ImageView ivProductIcon, ivProductImg;
        public TextView tvProductName, tvProductIntroduction, tvProductParameter,
                tvProductAdvantage, tvProductDisAdvantage, tvProductCollectNum;
        /**
         * 评论相关
         */
        public ImageView ivIcon;
        public TextView tvReplyTime, tvReplyContent, tvNickName;
        /**
         * 数量（评论、点赞）相关
         */
        public TextView tvPraiseNum, tvReplyNum, tvAddress;
    }
}