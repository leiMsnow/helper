package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.adapter.MyCommentTopicAdapter;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.topic.TopicComment;
import com.tongban.im.widget.view.TopicInputView;

import java.util.List;

/**
 * 我的话题 - 回复我的话题
 *
 * @author fushudi
 */
public class MyCommentTopicFragment extends BaseToolBarFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, OnLoadMoreListener, TopicInputView.IOnClickCommentListener
        , TopicInputView.IKeyboardListener {
    private LoadMoreListView mListView;
    private MyCommentTopicAdapter mAdapter;
    private TopicInputView topicInputView;

    private String mTopicId;
    private int mCursor = 0;
    private int mPageSize = 10;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_my_receive_topic;
    }

    @Override
    protected void initView() {
        mListView = (LoadMoreListView) mView.findViewById(R.id.lv_receive_topic_list);
        topicInputView = (TopicInputView) mView.findViewById(R.id.topic_input);
    }

    @Override
    protected void initData() {
        mAdapter = new MyCommentTopicAdapter(mContext, R.layout.item_my_comment_topic_list, null);
        mAdapter.setOnClickListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setPageSize(mPageSize);
        UserCenterApi.getInstance().fetchReplyTopicList(mCursor, mPageSize, this);
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
        mListView.setOnLoadMoreListener(this);
        topicInputView.setOnClickCommentListener(this);
        topicInputView.setKeyboardListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_portrait:
                String visitorId = v.getTag(Integer.MAX_VALUE).toString();
                TransferCenter.getInstance().startUserCenter(visitorId);
                break;
            case R.id.tv_comment:
                TopicComment comment = (TopicComment) v.getTag();
                mTopicId = comment.getTopic_info().getTopic_id();
                topicInputView.setCommentInfo(comment.getComment_id(),
                        comment.getUser_info().getNick_name(),
                        comment.getUser_info().getUser_id());

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance()
                .startTopicDetails(mAdapter.getItem(position).getTopic_info());
    }

    /**
     * 回复我的话题列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CommentTopicListEvent obj) {
        mCursor++;
        mAdapter.addAll(obj.commentTopicList);
        mListView.setResultSize(obj.commentTopicList.size());
    }

    @Override
    public void onLoadMore() {
        UserCenterApi.getInstance().fetchReplyTopicList(mCursor, mPageSize, this);
    }

    @Override
    public void onClickComment(String commentContent, String repliedCommentId,
                               String repliedName, String repliedUserId, List<ImageUrl> selectedFile) {
        TopicApi.getInstance().createCommentForTopic(mTopicId, commentContent, repliedCommentId,
                repliedName, repliedUserId, selectedFile, this);
    }

    /**
     * 话题评论成功事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {
        topicInputView.clearCommentInfo();
        KeyBoardUtils.closeKeyboard(topicInputView.getEtComment(), mContext);
    }

    @Override
    public void boardStatus(boolean isUp) {
        if (!isUp) {
            topicInputView.setVisibility(View.INVISIBLE);
        } else {
            topicInputView.setVisibility(View.VISIBLE);
        }
    }
}
