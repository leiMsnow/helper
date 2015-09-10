package com.tongban.im.activity.topic;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.adapter.OfficialTopicDetailsAdapter;
import com.tongban.im.adapter.TopicCommentAdapter;
import com.tongban.im.adapter.TopicImgAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.OfficialTopic;
import com.tongban.im.model.ProductBook;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicComment;
import com.tongban.im.widget.view.TopicInputView;

import java.util.ArrayList;
import java.util.List;


/**
 * 官方话题评论界面official
 *
 * @author fushudi
 */
public class OfficialTopicDetailsActivity extends CommonImageResultActivity implements View.OnClickListener {
    private ListView lvAuthorityTopicDetails;
    private OfficialTopicDetailsAdapter mAdapter;
    private View mHeader;
    private ImageView ivOfficialPortrait;
    private TextView tvOfficialName, tvCreateTime, tvOfficialTopicTitle, tvOfficialTopicContent;

    private String mTopicId;
    private int mCursor = 0;
    private int mPage = 10;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_official_topic_details;
    }

    @Override
    protected void initView() {
        mHeader = LayoutInflater.from(mContext).
                inflate(R.layout.activity_official_topic_details_header, null);
        lvAuthorityTopicDetails = (ListView) findViewById(R.id.lv_authority_topic_details);
        topicInputView = (TopicInputView) findViewById(R.id.topic_input);
        topicInputView.setAdapterImgCount(3);


        ivOfficialPortrait = (ImageView) mHeader.findViewById(R.id.iv_user_portrait);
        tvOfficialName = (TextView) mHeader.findViewById(R.id.tv_child_age);
        tvCreateTime = (TextView) mHeader.findViewById(R.id.tv_create_time);
        tvOfficialTopicTitle = (TextView) mHeader.findViewById(R.id.tv_topic_title);
        tvOfficialTopicContent = (TextView) mHeader.findViewById(R.id.tv_topic_content);

    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTopicId = uri.getQueryParameter(Consts.KEY_TOPIC_ID);
            if (!TextUtils.isEmpty(mTopicId)) {
                //获取话题详情接口
                TopicApi.getInstance().getTopicInfo(mTopicId, this);
                //获取评论接口
                TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mPage, this);
                //获取产品接口
                TopicApi.getInstance().getOfficialTopicInfo(mTopicId, 0, 10, this);


                mAdapter = new OfficialTopicDetailsAdapter(mContext, null,
                        new IMultiItemTypeSupport<OfficialTopic>() {
                            @Override
                            public int getLayoutId(int position, OfficialTopic o) {
                                //官方话题内容
                                if (o.getContentType() == OfficialTopic.CONTENT) {
                                    return R.layout.item_official_topic_details_content;
                                }
                                //评论（回复）数量
                                else if (o.getContentType() == OfficialTopic.REPLY_NUM) {
                                    return R.layout.item_official_topic_details_reply_num;
                                }
                                //评论（回复）列表(o.getContentType() == OfficialTopic.REPLY)
                                else {
                                    return R.layout.item_topic_comment_list;
                                }
                            }

                            @Override
                            public int getViewTypeCount() {
                                return 3;
                            }

                            @Override
                            public int getItemViewType(int position, OfficialTopic o) {
                                return o.getContentType();
                            }
                        });
                lvAuthorityTopicDetails.addHeaderView(mHeader);
                mAdapter.setOnClickListener(this);
                lvAuthorityTopicDetails.setAdapter(mAdapter);
            }
        }
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 查看官方话题详情Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.OfficialTopicInfoEvent obj) {
        Log.d("onComplete", "onComplete2");
        for (int i = 0; i < obj.productBookList.size(); i++) {
            Log.d("onComplete", "onComplete3");
            OfficialTopic officialTopic = new OfficialTopic();
            ProductBook productBook = obj.productBookList.get(i);
            officialTopic.setContentType(OfficialTopic.CONTENT);
            officialTopic.setProduct(productBook);
            mAdapter.getDataAll().add(officialTopic);
            Log.d("onComplete", "onComplete4");
        }
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
            officialTopic.setContentType(OfficialTopic.REPLY);
            officialTopic.setTopicReply(topicComment);
            mAdapter.getDataAll().add(officialTopic);
        }
    }

    /**
     * 获取话题详情Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent obj) {
        if (obj.topic.getUser_info().getPortrait_url().getMin() != null) {
            Glide.with(mContext).load(obj.topic.getUser_info().getPortrait_url().getMin()).into(ivOfficialPortrait);
        } else {
            ivOfficialPortrait.setImageResource(R.drawable.rc_default_portrait);
        }
        tvOfficialName.setText(obj.topic.getUser_info().getNick_name());
        tvCreateTime.setText(obj.topic.getC_time(mContext));
        tvOfficialTopicTitle.setText(obj.topic.getTopic_title());
        tvOfficialTopicContent.setText(obj.topic.getTopic_content());

        OfficialTopic officialTopic=new OfficialTopic();
        Topic topic=obj.topic;
        topic.setContentType(OfficialTopic.REPLY_NUM);
        officialTopic.setTopic(topic);
        mAdapter.getDataAll().add(officialTopic);
    }
}
