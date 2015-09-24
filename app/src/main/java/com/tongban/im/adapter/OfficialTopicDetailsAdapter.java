package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.model.topic.OfficialTopic;

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

    public OfficialTopicDetailsAdapter(Context context, List data,
                                       IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, OfficialTopic item) {
        //产品相关
        if (item.getItemType() == OfficialTopic.PRODUCT) {
            helper.setText(R.id.tv_cursor, item.getProduct().getProductIndex());
            if (item.getProduct().getProduct_img_url() != null) {
                helper.setImageBitmap(R.id.iv_product_img, item.getProduct()
                        .getProduct_img_url().get(0).getMin());
            } else {
                helper.setImageResource(R.id.iv_product_img, R.mipmap.ic_default_image);
            }
            helper.setText(R.id.tv_title, item.getProduct().getProduct_name());
            helper.setText(R.id.tv_product_author, item.getProduct().getBook_author());
            if (!TextUtils.isEmpty(item.getProduct().getRecommend_cause())) {
                helper.setText(R.id.tv_product_recommend_cause, item.getProduct().getRecommend_cause());
                helper.setVisible(R.id.tv_recommend_cause, View.VISIBLE);
            } else {
                helper.setVisible(R.id.tv_recommend_cause, View.GONE);
            }
            helper.setTag(R.id.btn_check_detail, item);
            helper.setOnClickListener(R.id.btn_check_detail, onClickListener);
        }
        //数量（评论、点赞）相关
        else if (item.getItemType() == OfficialTopic.REPLY_NUM) {
            helper.setText(R.id.tv_comment_count, item.getTopic().getComment_amount());
        }
        //评论列表相关
        else if (item.getItemType() == OfficialTopic.REPLY) {
            if (item.getTopicReply().getUser_info().getPortrait_url() != null) {
                helper.setImageBitmap(R.id.iv_user_portrait, item.getTopicReply()
                        .getUser_info().getPortrait_url().getMin());
            } else {
                helper.setImageResource(R.id.iv_user_portrait, Consts.getUserDefaultPortrait());
            }
            helper.setText(R.id.tv_comment_time, item.getTopicReply().getC_time(mContext));
            helper.setText(R.id.tv_comment_content, item.getTopicReply().getComment_content());
            helper.setText(R.id.tv_user_name, item.getTopicReply().getUser_info().getNick_name());
        }
    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper, OfficialTopic item) {
        if (item.getItemType() == OfficialTopic.PRODUCT) {
            ViewGroup.LayoutParams lp = helper.getView(R.id.iv_product_img).getLayoutParams();
            lp.height = ScreenUtils.getScreenWidth(mContext) / 4 * 3;
            helper.getView(R.id.iv_product_img).setLayoutParams(lp);
        }
    }
}