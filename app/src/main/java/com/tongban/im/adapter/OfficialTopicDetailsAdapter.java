package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.OfficialTopic;

import java.util.List;


/**
 * 官方话题评论Adapter
 * Created by fushudi on 2015/8/10.
 */
public class OfficialTopicDetailsAdapter extends QuickAdapter<OfficialTopic> {


    public OfficialTopicDetailsAdapter(Context context, List data, IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, OfficialTopic item) {
        //产品相关
        if (item.getContentType() == OfficialTopic.CONTENT) {
            helper.setImageBitmap(R.id.iv_product_icon, item.getProduct().getProduct_icon_url());
            helper.setImageBitmap(R.id.iv_product_img, item.getProduct().getProduct_url());
            helper.setText(R.id.tv_product_name, item.getProduct().getProduct_name());
            helper.setText(R.id.tv_product_introduce_content, item.getProduct().getProductIntroduction());
            helper.setText(R.id.tv_product_parameters_content, item.getProduct().getProductParameter());
            helper.setText(R.id.tv_product_advantage_content, item.getProduct().getProductAdvantage());
            helper.setText(R.id.tv_product_disadvantage_content, item.getProduct().getProductDisAdvantage());
            helper.setText(R.id.tv_collect_num, item.getProduct().getProductCollectNum());
        }
        //数量（评论、点赞）相关
        else if (item.getContentType() == OfficialTopic.REPLY_NUM) {
            helper.setText(R.id.tv_praise_count, item.getTopic().getTopicPraiseNum());
            helper.setText(R.id.tv_reply_count, item.getTopic().getTopicReplyNum());
            helper.setText(R.id.tv_location, item.getTopic().getTopicAddress());
        }
        //评论相关
        else if (item.getContentType() == OfficialTopic.REPLY) {
            helper.setImageBitmap(R.id.iv_user_portrait, item.getTopicReply().getPortrait_url());
            helper.setText(R.id.tv_reply_time, item.getTopicReply().getReplyTime());
            helper.setText(R.id.tv_reply_content, item.getTopicReply().getReplyContent());
            helper.setText(R.id.tv_user_name, item.getTopicReply().getReplyNickName());
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }
}