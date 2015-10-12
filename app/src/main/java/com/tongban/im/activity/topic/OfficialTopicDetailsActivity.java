package com.tongban.im.activity.topic;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.base.TopicDetailsBaseActivity;
import com.tongban.im.adapter.OfficialTopicDetailsAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.discover.ProductBook;
import com.tongban.im.model.topic.OfficialTopic;
import com.tongban.im.model.topic.Topic;
import com.tongban.im.model.topic.TopicComment;
import com.tongban.im.widget.view.ChildGridView;

import butterknife.Bind;


/**
 * 官方话题评论界面official
 *
 * @author fushudi
 */
public class OfficialTopicDetailsActivity extends TopicDetailsBaseActivity {

    private View mHeader;
    //头布局 top
    CircleImageView ivOfficialPortrait;
    TextView tvOfficialName;
    TextView tvCreateTime;
    //中间布局 content
    TextView tvOfficialTopicTitle;
    TextView tvOfficialTopicContent;


    private OfficialTopicDetailsAdapter mAdapter;

    @Override
    protected void initData() {

        super.initData();

        if (!TextUtils.isEmpty(mTopicId)) {
            mHeader = LayoutInflater.from(mContext).
                    inflate(R.layout.header_official_topic_details, null);

            ivOfficialPortrait = (CircleImageView) mHeader.findViewById(R.id.iv_user_portrait);
            tvOfficialName = (TextView) mHeader.findViewById(R.id.tv_user_name);
            tvCreateTime = (TextView) mHeader.findViewById(R.id.tv_create_time);

            tvOfficialTopicTitle = (TextView) mHeader.findViewById(R.id.tv_topic_title);
            tvOfficialTopicContent = (TextView) mHeader.findViewById(R.id.tv_topic_content);
            btnPlay = (Button) mHeader.findViewById(R.id.btn_topic_voice);

            //获取产品接口
            TopicApi.getInstance().getOfficialTopicInfo(mTopicId, mCursor, mPage, this);
            mAdapter = new OfficialTopicDetailsAdapter(mContext, null,
                    new IMultiItemTypeSupport<OfficialTopic>() {
                        @Override
                        public int getLayoutId(int position, OfficialTopic o) {
                            //官方商品
                            if (o.getItemType() == OfficialTopic.PRODUCT) {
                                return R.layout.item_official_topic_details_content;
                            }
                            //评论（回复）数量
                            else if (o.getItemType() == OfficialTopic.REPLY_NUM) {
                                return R.layout.item_official_topic_details_reply_num;
                            }
                            //评论（回复）列表
                            else if (o.getItemType() == OfficialTopic.REPLY) {
                                return R.layout.item_topic_comment_list;
                            } else {
                                return 0;
                            }
                        }

                        @Override
                        public int getViewTypeCount() {
                            return 3;
                        }

                        @Override
                        public int getItemViewType(int position, OfficialTopic o) {
                            return o.getItemType();
                        }
                    });
            lvReplyList.addHeaderView(mHeader);
            lvReplyList.setAdapter(mAdapter);

            ivOfficialPortrait.setOnClickListener(this);
            btnPlay.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        //跳转到用户中心界面
        if (v == ivOfficialPortrait) {
            TransferCenter.getInstance().startUserCenter(mTopicInfo.getUser_info().getUser_id());
        } else {
            super.onClick(v);
        }
    }

    /**
     * 查看官方话题详情Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.OfficialTopicInfoEvent obj) {
        for (int i = 0; i < obj.productBookList.size(); i++) {
            OfficialTopic officialTopic = new OfficialTopic();
            ProductBook productBook = obj.productBookList.get(i);
            productBook.setProductIndex(String.valueOf(i + 1));
            officialTopic.setItemType(OfficialTopic.PRODUCT);
            officialTopic.setProduct(productBook);
            mAdapter.getDataAll().add(officialTopic);
        }
        mAdapter.notifyDataSetChanged();
        //获取话题详情接口
        TopicApi.getInstance().getTopicInfo(mTopicId, this);
    }

    /**
     * 获取评论列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCommentListEvent obj) {
        for (int i = 0; i < obj.topicCommentList.size(); i++) {
            OfficialTopic officialTopic = new OfficialTopic();
            TopicComment topicComment = obj.topicCommentList.get(i);
            officialTopic.setItemType(OfficialTopic.REPLY);
            officialTopic.setTopicReply(topicComment);
            for (int j = 0; j < mAdapter.getDataAll().size(); j++) {
                // 判断是否是评论的数据
                if (mAdapter.getDataAll().get(j).getItemType() != OfficialTopic.REPLY) {
                    continue;
                }
                // 删除相同的评论
                if (mAdapter.getDataAll().get(j).getTopicReply().getComment_id().equals(
                        topicComment.getComment_id())) {
                    mAdapter.getDataAll().remove(j);
                }
            }
            mAdapter.getDataAll().add(officialTopic);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取话题详情Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent obj) {
        super.onEventMainThread(obj);
        if (mTopicInfo != null) {
            if (mTopicInfo.getUser_info().getPortrait_url().getMin() != null) {
                setUserPortrait(mTopicInfo.getUser_info().getPortrait_url().
                        getMin(), ivOfficialPortrait);
            } else {
                ivOfficialPortrait.setImageResource(Consts.getUserDefaultPortrait());
            }
            tvOfficialName.setText(mTopicInfo.getUser_info().getNick_name());
            tvCreateTime.setText(mTopicInfo.getC_time(mContext));
            tvOfficialTopicTitle.setText(mTopicInfo.getTopic_title());
            if (!TextUtils.isEmpty(mTopicInfo.getTopicContent().getTopic_content_voice())) {
                btnPlay.setTag(mTopicInfo.getTopicContent().getTopic_content_voice());
                btnPlay.setVisibility(View.VISIBLE);
            }else{
                btnPlay.setVisibility(View.GONE);
            }
            tvOfficialTopicContent.setText(mTopicInfo.getTopicContent().getTopic_content_text());

            OfficialTopic officialTopic = new OfficialTopic();
            Topic topic = mTopicInfo;
            officialTopic.setItemType(OfficialTopic.REPLY_NUM);
            officialTopic.setTopic(topic);
            mAdapter.getDataAll().add(officialTopic);
            mAdapter.notifyDataSetChanged();
            lvReplyList.setVisibility(View.VISIBLE);
            //获取评论接口
            TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mPage, this);
        }
    }

    /**
     * 话题评论成功事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {
        super.onEventMainThread(obj);
        mCursor = 0;
        TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mAdapter.getCount() + 1, this);
    }
}
