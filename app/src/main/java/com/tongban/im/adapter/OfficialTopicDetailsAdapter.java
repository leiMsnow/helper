package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.tb.api.model.topic.OfficialTopic;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.common.Consts;

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


            helper.setImageBitmap(R.id.iv_product_img, item.getProduct()
                    .getProduct_img_url().get(0).getMin());

            helper.setText(R.id.tv_title, item.getProduct().getProduct_name());

            helper.setText(R.id.tv_product_author, item.getProduct().getBook_author());

            if (!TextUtils.isEmpty(item.getProduct().getRecommend_cause())) {
                helper.setText(R.id.tv_product_recommend_cause
                        , item.getProduct().getRecommend_cause());
                helper.setVisible(R.id.tv_recommend_cause, View.VISIBLE);
            } else {
                helper.setVisible(R.id.tv_recommend_cause, View.INVISIBLE);
            }
            // 设置序号的高度为0
            ViewGroup.LayoutParams lp = helper.getView(R.id.tv_cursor).getLayoutParams();
            lp.height = 0;
            helper.getView(R.id.tv_cursor).setLayoutParams(lp);

            helper.setTag(R.id.btn_check_detail, item);
            helper.setOnClickListener(R.id.btn_check_detail, onClickListener);
        }
        //数量（评论、点赞）相关
        else if (item.getItemType() == OfficialTopic.REPLY_NUM) {
            helper.setText(R.id.tv_comment_count, item.getTopic().getComment_amount());
        }
        //评论列表相关
        else if (item.getItemType() == OfficialTopic.REPLY) {
            if (item.getTopicReply().getUser_info().getPortraitUrl() != null) {
                helper.setImageBitmap(R.id.iv_user_portrait, item.getTopicReply()
                        .getUser_info().getPortraitUrl().getMin());
            } else {
                helper.setImageResource(R.id.iv_user_portrait, Consts.getUserDefaultPortrait());
            }
            helper.setText(R.id.tv_user_name, item.getTopicReply().getUser_info().getNick_name());
            helper.setText(R.id.tv_comment_time, item.getTopicReply().getC_time(mContext));

            if (item.getTopicReply() != null &&
                    item.getTopicReply().getCommentContent() != null) {
                helper.setText(R.id.tv_comment_content
                        , item.getTopicReply().getCommentContent().getComment_content_text());
            }

            String repliedName = TextUtils.isEmpty(item.getTopicReply().getReplied_comment_id()) ? "" :
                    "回复" + item.getTopicReply().getReplied_nick_name();
            helper.setText(R.id.tv_comment_name, repliedName);

            //点击头像
            helper.setTag(R.id.iv_user_portrait, Integer.MAX_VALUE, item.getTopicReply()
                    .getUser_info().getUser_id());
            helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);

            //是自己就不显示回复
            if (SPUtils.get(mContext, Consts.USER_ID, "").toString().equals(
                    item.getTopicReply().getUser_info().getUser_id())) {
                helper.setVisible(R.id.rl_comment_parent, View.GONE);
            } else {
                helper.setVisible(R.id.rl_comment_parent, View.VISIBLE);
                //回复
                helper.setTag(R.id.rl_comment_parent, item.getTopicReply());
                helper.setOnClickListener(R.id.rl_comment_parent, onClickListener);
            }
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