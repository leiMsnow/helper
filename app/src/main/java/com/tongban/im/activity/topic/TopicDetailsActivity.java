package com.tongban.im.activity.topic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.fragment.PhotoViewFragment;
import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.CommonImageResultActivity;
import com.tongban.im.activity.PhotoViewPagerActivity;
import com.tongban.im.adapter.TopicCommentAdapter;
import com.tongban.im.adapter.TopicImgAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TopicListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicComment;
import com.tongban.im.widget.view.TopicInputView;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题评论界面
 *
 * @author fushudi
 */
public class TopicDetailsActivity extends CommonImageResultActivity implements View.OnClickListener,
        TopicInputView.onClickCommentListener {

    //头布局控件
    private View mHeader;
    //头布局 top
    private ImageView ivUserPortrait;
    private TextView tvUserName;
    private TextView tvAge;
    private TextView tvTime;
    //中间布局 content
    private TextView tvTopicTitle;
    private TextView tvTopicContent;
    private GridView gvContent;
    //底布局 bottom
    private ImageView ivComment;
    private TextView tvComment;
    private ImageView ivCollect;
    private TextView tvCollect;


    private ListView lvReplyList;

    private TopicImgAdapter mTopicImgAdapter;
    private TopicCommentAdapter mAdapter;

    private Topic mTopicInfo;
    private String mTopicId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void initView() {

        lvReplyList = (ListView) findViewById(R.id.lv_reply_list);

        topicInputView = (TopicInputView) findViewById(R.id.topic_input);

        //添加头布局
        mHeader = LayoutInflater.from(mContext).inflate(R.layout.header_topic_details, null);
        ivUserPortrait = (ImageView) mHeader.findViewById(R.id.iv_user_portrait);
        tvAge = (TextView) mHeader.findViewById(R.id.tv_child_age);
        tvUserName = (TextView) mHeader.findViewById(R.id.tv_user_name);
        tvTime = (TextView) mHeader.findViewById(R.id.tv_create_time);

        tvTopicTitle = (TextView) mHeader.findViewById(R.id.tv_topic_title);
        tvTopicContent = (TextView) mHeader.findViewById(R.id.tv_topic_content);
        gvContent = (GridView) mHeader.findViewById(R.id.gv_content);
        gvContent.setVisibility(View.VISIBLE);

        ivComment = (ImageView) mHeader.findViewById(R.id.iv_comment);
        tvComment = (TextView) mHeader.findViewById(R.id.tv_comment_count);
        ivCollect = (ImageView) mHeader.findViewById(R.id.iv_collect);
        tvCollect = (TextView) mHeader.findViewById(R.id.tv_collect_count);

        lvReplyList.addHeaderView(mHeader);
    }

    @Override
    protected void initData() {
        topicInputView.setAdapterImgCount(3);
        topicInputView.setOnClickCommentListener(this);

        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTopicId = uri.getQueryParameter(Consts.KEY_TOPIC_ID);
            if (!TextUtils.isEmpty(mTopicId)) {
                TopicApi.getInstance().getTopicInfo(mTopicId, this);
                TopicApi.getInstance().getTopicCommentList(mTopicId, 0, 10, this);

                mAdapter = new TopicCommentAdapter(mContext, R.layout.item_topic_comment_list, null);
                mAdapter.setOnClickListener(this);
                lvReplyList.setAdapter(mAdapter);

                mTopicImgAdapter = new TopicImgAdapter(mContext, R.layout.item_topic_grid_img,
                        null);
                mTopicImgAdapter.setImgClickListener(this);
                gvContent.setAdapter(mTopicImgAdapter);
            }
        }
    }

    @Override
    protected void initListener() {
        ivUserPortrait.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //重置回复话题
        if (v == ivComment) {
            topicInputView.clearCommentInfo();
            KeyBoardUtils.openKeybord(topicInputView.getEtComment(), mContext);
        }
        // 收藏话题
        else if (v == ivCollect) {
            TopicApi.getInstance().collectTopic(!mTopicInfo.isCollect_status(), mTopicId, this);
        }
        // 用户信息查看
        else if (v == ivUserPortrait) {
            TransferCenter.getInstance().startUserCenter(mTopicInfo.getUser_info().getUser_id());
        } else {
            switch (v.getId()) {
                //回复评论
                case R.id.tv_comment:
                    TopicComment comment = (TopicComment) v.getTag();
                    topicInputView.setCommentInfo(comment.getComment_id(),
                            comment.getUser_info().getNick_name(),
                            comment.getUser_info().getUser_id());
                    break;
                case R.id.iv_topic_img:
                    List<ImageUrl> imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                    TopicListenerImpl.startPhotoView(mContext,
                            TopicListenerImpl.setImageUrls(imageUrls), 0);
                    break;
                case R.id.iv_user_portrait:
                    String userId = v.getTag(Integer.MAX_VALUE).toString();
                    TransferCenter.getInstance().startUserCenter(userId);
                    break;
            }
        }

    }

    @Override
    public void onClickComment(String commentContent, String repliedCommentId,
                               String repliedName, String repliedUserId) {
        TopicApi.getInstance().createCommentForTopic(mTopicId, commentContent, repliedCommentId,
                repliedName, repliedUserId, this);
    }

    /**
     * 话题详情事件回调
     *
     * @param topicInfoEvent
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        mTopicInfo = topicInfoEvent.topic;
        if (mTopicInfo != null) {
            setTitle(mTopicInfo.getTopic_title());
            if (mTopicInfo.getUser_info() != null) {
                tvUserName.setText(mTopicInfo.getUser_info().getNick_name());
                Glide.with(TopicDetailsActivity.this).load(mTopicInfo.getUser_info().getPortrait_url().getMin())
                        .placeholder(R.drawable.rc_default_portrait).into(ivUserPortrait);
            }

            tvAge.setText(mTopicInfo.getUser_info().getChild_info().get(0).getAge() + "岁" +
                    mTopicInfo.getUser_info().getChild_info().get(0).getSex() + "宝宝");
            tvTime.setText(mTopicInfo.getC_time(mContext));

            tvTopicTitle.setText(mTopicInfo.getTopic_title());
            tvTopicContent.setText(mTopicInfo.getTopic_content());

            if (mTopicInfo.getUser_info().getUser_id().equals(SPUtils.get(mContext, Consts.USER_ID, ""))) {
                ivCollect.setEnabled(false);
                ivCollect.setImageResource(R.mipmap.ic_topic_collect_pressed);
            } else {
                if (mTopicInfo.isCollect_status()) {
                    ivCollect.setImageResource(R.mipmap.ic_topic_collect_pressed);
                } else {
                    ivCollect.setImageResource(R.drawable.selector_topic_collect);
                }
            }
            tvComment.setText(mTopicInfo.getComment_amount());
            tvCollect.setText(mTopicInfo.getCollect_amount());

            mTopicImgAdapter.replaceAll(mTopicInfo.getTopic_img_url());
        } else {
            lvReplyList.removeHeaderView(mHeader);
        }
    }


    /**
     * 话题评论列表事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCommentListEvent obj) {
        mAdapter.replaceAll(obj.topicCommentList);
    }

    /**
     * 话题评论成功事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {

        int commentCount = Integer.parseInt(tvComment.getText().toString());
        tvComment.setText(String.valueOf(commentCount + 1));
        topicInputView.clearCommentInfo();
        KeyBoardUtils.closeKeybord(topicInputView.getEtComment(), mContext);

        TopicApi.getInstance().getTopicCommentList(mTopicId, 0, 10, this);
        ToastUtil.getInstance(mContext).showToast(obj.message);
    }

    /**
     * 收藏话题事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCollect obj) {
        int collectCount = Integer.parseInt(tvCollect.getText().toString());
        mTopicInfo.setCollect_status(obj.status);
        if (obj.status) {
            ToastUtil.getInstance(mContext).showToast("收藏成功");
            ivCollect.setImageResource(R.mipmap.ic_topic_collect_pressed);
            tvCollect.setText(String.valueOf(collectCount + 1));
        } else {
            ToastUtil.getInstance(mContext).showToast("取消成功");
            ivCollect.setImageResource(R.drawable.selector_topic_collect);
            tvCollect.setText(String.valueOf(collectCount - 1));

        }
    }
}
