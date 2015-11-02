package com.tongban.im.activity.topic;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tb.api.TopicApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.utils.TransferCenter;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.base.TopicDetailsBaseActivity;
import com.tongban.im.adapter.TopicCommentAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.impl.TopicListenerImpl;

/**
 * 话题评论界面
 *
 * @author fushudi
 */
public class TopicDetailsActivity extends TopicDetailsBaseActivity implements
        OnLoadMoreListener {

    private int[] images = new int[]{R.id.iv_small_img_1, R.id.iv_small_img_2, R.id.iv_small_img_3};

    //头布局控件
    private View mHeader;
    //头布局 top
    CircleImageView ivUserPortrait;
    TextView tvUserName;
    TextView tvTime;
    //中间布局 content
    TextView tvTopicTitle;
    TextView tvTopicContent;
    //    ChildGridView gvContent;
    //底布局 bottom
    TextView tvComment;
    TextView tvCollect;

    View imageParent;

    //    private TopicImgAdapter mTopicImgAdapter;
    private TopicCommentAdapter mAdapter;

    @Override
    protected void initData() {
        super.initData();
        if (!TextUtils.isEmpty(mTopicId)) {

            mHeader = LayoutInflater.from(mContext).inflate(R.layout.header_topic_details, null);
            lvReplyList.addHeaderView(mHeader);
            // header - top
            ivUserPortrait = (CircleImageView) mHeader.findViewById(R.id.iv_user_portrait);
            tvUserName = (TextView) mHeader.findViewById(R.id.tv_user_name);
            tvTime = (TextView) mHeader.findViewById(R.id.tv_create_time);
            // header - content
            tvTopicTitle = (TextView) mHeader.findViewById(R.id.tv_topic_title);
            btnPlay = (Button) mHeader.findViewById(R.id.btn_topic_voice);
            tvTopicContent = (TextView) mHeader.findViewById(R.id.tv_topic_content);
//            gvContent = (ChildGridView) mHeader.findViewById(R.id.gv_content);
            // header - bottom
            tvComment = (TextView) mHeader.findViewById(R.id.tv_comment_count);
            tvCollect = (TextView) mHeader.findViewById(R.id.tv_collect_count);
//            mTopicImgAdapter = new TopicImgAdapter(mContext, R.layout.item_topic_grid_img, null);
//            gvContent.setAdapter(mTopicImgAdapter);
            imageParent = mHeader.findViewById(R.id.ll_small_img_parent);

            mAdapter = new TopicCommentAdapter(mContext, R.layout.item_topic_comment_list, null);
            lvReplyList.setResultSize(mPage);
            lvReplyList.setAdapter(mAdapter);

//            mTopicImgAdapter.setImgClickListener(this);
            mAdapter.setOnImgClickListener(new TopicListenerImpl(mContext));
            mAdapter.setOnClickListener(this);
            lvReplyList.setOnLoadMoreListener(this);
            ivUserPortrait.setOnClickListener(this);
            btnPlay.setOnClickListener(this);

            onRequest();
        }
    }


    @Override
    public void onRequest() {
        mCursor = 0;
        TopicApi.getInstance().getTopicInfo(mTopicId, this);
    }

    @Override
    public void onClick(View v) {
        // 用户信息查看
        if (v == ivUserPortrait) {
            TransferCenter.getInstance().startUserCenter(mTopicInfo.getUser_info().getUser_id());
        } else {
            super.onClick(v);
        }
    }

    /**
     * 话题详情事件回调
     *
     * @param topicInfoEvent
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        super.onEventMainThread(topicInfoEvent);
        if (mTopicInfo != null) {
            if (mTopicInfo.getUser_info() != null) {
                tvUserName.setText(mTopicInfo.getUser_info().getNick_name());
                if (mTopicInfo.getUser_info().getPortraitUrl() != null) {
                    setUserPortrait(mTopicInfo.getUser_info().getPortraitUrl().getMin()
                            , ivUserPortrait);
                } else {
                    ivUserPortrait.setImageResource(Consts.getUserDefaultPortrait());
                }
            }

            tvTime.setText(mTopicInfo.getC_time(mContext));
            tvTopicTitle.setText(mTopicInfo.getTopic_title());

            tvComment.setText(mTopicInfo.getComment_amount() + "人回答");
            tvCollect.setText("同问" + mTopicInfo.getCollect_amount() + "人");

            if (mTopicInfo.getTopicContent() != null) {

                if (!TextUtils.isEmpty(mTopicInfo.getTopicContent().getTopic_content_voice())) {
                    btnPlay.setTag(mTopicInfo.getTopicContent().getTopic_content_voice());
                    btnPlay.setVisibility(View.VISIBLE);
                } else {
                    btnPlay.setVisibility(View.GONE);
                }

                tvTopicContent.setText(mTopicInfo.getTopicContent().getTopic_content_text());

                if (mTopicInfo.getTopicContent().getTopic_img_url() != null) {
                    imageParent.setVisibility(View.VISIBLE);
                    int count = mTopicInfo.getTopicContent().getTopic_img_url().size() > 3 ? 3
                            : mTopicInfo.getTopicContent().getTopic_img_url().size();
                    for (int i = 0; i < images.length; i++) {
                        ImageView imageView = (ImageView) mHeader.findViewById(images[i]);
                        if (i < count) {
                            imageView.setVisibility(View.VISIBLE);
                            setImagePortrait(
                                    mTopicInfo.getTopicContent().getTopic_img_url().get(i).getMin()
                                    , imageView);
                            imageView.setTag(Integer.MAX_VALUE,
                                    mTopicInfo.getTopicContent().getTopic_img_url());
                            imageView.setOnClickListener(new TopicListenerImpl(mContext));
                        }
                    }
                }
            }
            lvReplyList.setVisibility(View.VISIBLE);
            // 查询话题评论列表
            TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mPage, this);
        } else {
            lvReplyList.setVisibility(View.GONE);
        }
    }

    /**
     * 话题评论成功事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {
        super.onEventMainThread(obj);
        //Integer.parseInt(tvComment.getText().toString());
        int commentCount = Integer.parseInt(mTopicInfo.getComment_amount());
        tvComment.setText(String.valueOf(commentCount + 1)+"回答");
        mCursor = 0;
        TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mAdapter.getCount() + 1, this);
    }


    /**
     * 话题评论列表事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCommentListEvent obj) {
        if (mCursor == 0) {
            mAdapter.replaceAll(obj.topicCommentList);
        } else {
            mAdapter.addAll(obj.topicCommentList);
        }
        mCursor++;
        lvReplyList.setResultSize(obj.topicCommentList.size());
    }


    @Override
    public void onLoadMore() {
        if (mTopicInfo != null) {
            TopicApi.getInstance().getTopicCommentList(mTopicId, mCursor, mPage, this);
        }
    }


}
