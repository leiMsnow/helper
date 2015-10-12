package com.tongban.im.activity.base;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.im.R;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TopicVoiceTimerCount;
import com.tongban.im.impl.TopicListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.topic.OfficialTopic;
import com.tongban.im.model.topic.Topic;
import com.tongban.im.model.topic.TopicComment;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.utils.VoiceUtils;
import com.tongban.im.widget.view.TopicInputView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.rong.imkit.util.IVoiceHandler;

/**
 * 通用的topicDetails父类
 * Created by fushudi on 2015/8/13.
 */
public abstract class TopicDetailsBaseActivity extends CommonImageResultActivity implements
        CommonImageResultActivity.ImageResultListener
        , TopicInputView.IOnClickCommentListener
        , View.OnClickListener
        , IVoiceHandler.OnPlayListener {


    @Bind(R.id.lv_reply_list)
    protected LoadMoreListView lvReplyList;
    @Bind(R.id.topic_input)
    TopicInputView topicInputView;

    protected Button btnPlay;

    private MenuItem menuItem;

    protected Topic mTopicInfo;
    protected String mTopicId;

    protected int mCursor = 0;
    protected int mPage = 10;

    private VoiceUtils voiceUtils;
    private TopicVoiceTimerCount voiceTimerCount;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_details;
    }

    @Override
    protected void initData() {

        setTitle("");
        setImageResultListener(this);
        voiceUtils = new VoiceUtils(mContext, this);

        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTopicId = uri.getQueryParameter(Consts.KEY_TOPIC_ID);
        }
        topicInputView.setOnClickCommentListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        menuItem = menu.findItem(R.id.menu_collect);
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_collect) {
            menuItem.setEnabled(false);
            TopicApi.getInstance().collectTopic(!mTopicInfo.isCollect_status(), mTopicId, this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // 先收起plus面板
        if (topicInputView.gridViewVisibility(true))
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        // 播放录音
        if (v == btnPlay) {
            String playUrl = btnPlay.getTag().toString();
            voiceUtils.play(Uri.parse(playUrl));
        } else {
            switch (v.getId()) {
                // 回复评论
                case R.id.rl_comment_parent:
                    TopicComment comment = (TopicComment) v.getTag();
                    topicInputView.setCommentInfo(comment.getComment_id(),
                            comment.getUser_info().getNick_name(),
                            comment.getUser_info().getUser_id());
                    break;
                // 查看图片
                case R.id.iv_topic_img:
                    List<ImageUrl> imageUrls = (List<ImageUrl>) v.getTag(Integer.MAX_VALUE);
                    int position = (int) v.getTag(Integer.MIN_VALUE);
                    TopicListenerImpl.startPhotoView(mContext,
                            TopicListenerImpl.setImageUrls(imageUrls), position);
                    break;
                // 查看用户
                case R.id.iv_user_portrait:
                    String userId = v.getTag(Integer.MAX_VALUE).toString();
                    TransferCenter.getInstance().startUserCenter(userId);
                    break;
                // 查看详情
                case R.id.btn_check_detail:
                    OfficialTopic productBook = (OfficialTopic) v.getTag();
                    TransferCenter.getInstance().startProductBook(productBook.
                            getProduct().getProduct_id());
                    break;
            }
        }
    }


    @Override
    public void cameraResult(String newFile) {
        if (topicInputView != null) {
            topicInputView.notifyChange(newFile);
        }
    }

    @Override
    public void albumResult(ArrayList<String> picturePaths) {
        for (int i = picturePaths.size() - 1; i >= 0; i--) {
            String newFile = CameraUtils.saveToSD(picturePaths.get(i));
            cameraResult(newFile);
        }
    }

    /**
     * 回复评论
     *
     * @param commentContent   回复内容
     * @param repliedCommentId 评论id
     * @param repliedName      回复评论者名字
     * @param repliedUserId    回复评论者Id
     * @param selectedFile     图片集合
     */
    @Override
    public void onClickComment(String commentContent, String repliedCommentId,
                               String repliedName, String repliedUserId, List<ImageUrl> selectedFile) {
        TopicApi.getInstance().createCommentForTopic(mTopicId, commentContent, repliedCommentId,
                repliedName, repliedUserId, selectedFile, this);
    }


    /**
     * 话题获取成功事件回调
     *
     * @param topicInfoEvent
     */
    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        mTopicInfo = topicInfoEvent.topic;
        if (mTopicInfo != null) {
            topicInputView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuItem.setVisible(true);
                    if (mTopicInfo.isCollect_status()) {
                        menuItem.setIcon(R.mipmap.ic_menu_collected);
                    } else {
                        menuItem.setIcon(R.mipmap.ic_menu_collect);
                    }
                }
            }, 300);
        }
    }

    /**
     * 收藏话题事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCollect obj) {
        mTopicInfo.setCollect_status(obj.status);
        menuItem.setEnabled(true);
        if (obj.status) {
            menuItem.setIcon(R.mipmap.ic_menu_collected);
            ToastUtil.getInstance(mContext).showToast("收藏成功");
        } else {
            menuItem.setIcon(R.mipmap.ic_menu_collect);
            ToastUtil.getInstance(mContext).showToast("已取消收藏");
        }
    }


    /**
     * 话题评论成功事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateTopicCommentEvent obj) {
        topicInputView.clearCommentInfo();
        topicInputView.gridViewVisibility(true);
        KeyBoardUtils.closeKeyboard(topicInputView.getEtComment(), mContext);
    }

    @Override
    public void onVoicePlay(Context context, long timeout) {
        voiceTimerCount = new TopicVoiceTimerCount(btnPlay, timeout);
        voiceTimerCount.start();
    }

    @Override
    public void onVoiceCover(boolean limited) {

    }

    @Override
    public void onVoiceStop() {
        btnPlay.setText(getString(R.string.topic_content_play));
        btnPlay.setSelected(false);
        if (voiceTimerCount != null) {
            voiceTimerCount.cancel();
            voiceTimerCount = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        voiceUtils.stop();
    }
}
