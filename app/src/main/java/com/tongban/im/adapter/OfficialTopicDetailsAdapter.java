package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

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

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OfficialTopicDetailsAdapter(Context context, List data, IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, OfficialTopic item) {
        //产品相关
        if (item.getContentType() == OfficialTopic.CONTENT) {
            if (item.getProduct() != null) {
                if (item.getProduct().getProduct_img_url() != null) {
                    helper.setImageBitmap(R.id.iv_product_icon, item.getProduct().getProduct_img_url().get(0).getMin());
                    helper.setImageBitmap(R.id.iv_product_img, item.getProduct().getProduct_img_url().get(0).getMin());
                } else {
                    helper.setImageResource(R.id.iv_product_icon, R.drawable.rc_default_portrait);
                    helper.setImageResource(R.id.iv_product_img, R.drawable.rc_default_portrait);
                }
                helper.setText(R.id.tv_product_name, item.getProduct().getProduct_name());
                helper.setText(R.id.tv_product_introduce_content, item.getProduct().getProduct_tags());
                helper.setText(R.id.tv_product_parameters_content, item.getProduct().getProduct_tags());
                helper.setText(R.id.tv_product_advantage_content, item.getProduct().getRecommend_cause());
                helper.setText(R.id.tv_product_disadvantage_content, item.getProduct().getWeakness());
                helper.setText(R.id.tv_collect_num, String.valueOf(item.getProduct().getCollect_amount()));
            } else {
                return;
            }
        }
        //数量（评论、点赞）相关
        else if (item.getContentType() == OfficialTopic.REPLY_NUM) {
//            helper.setText(R.id.tv_praise_count, item.getTopic().getTopicPraiseNum());
            helper.setText(R.id.tv_comment_count, item.getTopic().getComment_amount());
//            helper.setText(R.id.tv_location, item.getTopic().getTopicAddress());
        }
        //评论列表相关
        else if (item.getContentType() == OfficialTopic.REPLY) {
            if (item.getTopicReply().getUser_info().getPortrait_url() != null) {
                helper.setImageBitmap(R.id.iv_user_portrait, item.getTopicReply().getUser_info().getPortrait_url().getMin());
            } else {
                helper.setImageResource(R.id.iv_user_portrait, R.drawable.rc_default_portrait);
            }
            helper.setText(R.id.tv_comment_time, item.getTopicReply().getC_time(mContext));
            helper.setText(R.id.tv_comment_content, item.getTopicReply().getComment_content());
            helper.setText(R.id.tv_user_name, item.getTopicReply().getUser_info().getNick_name());
        }
    }

}