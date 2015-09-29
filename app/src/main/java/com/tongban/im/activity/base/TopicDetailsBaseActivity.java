package com.tongban.im.activity.base;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.im.R;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TopicListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.topic.OfficialTopic;
import com.tongban.im.model.topic.Topic;
import com.tongban.im.model.topic.TopicComment;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.widget.view.TopicInputView;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的topicDetails父类
 * Created by fushudi on 2015/8/13.
 */
public abstract class TopicDetailsBaseActivity extends CommonImageResultActivity implements
        CommonImageResultActivity.ImageResultListener, View.OnClickListener,
        TopicInputView.IOnClickCommentListener {


    private TopicInputView topicInputView;
    private MenuItem menuItem;

    protected Topic mTopicInfo;
    protected String mTopicId;

    protected int mCursor = 0;
    protected int mPage = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImageResultListener(this);
        setTitle("");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_details;
    }


    @Override
    protected void initView() {
        topicInputView = (TopicInputView) findViewById(R.id.topic_input);
        topicInputView.setAdapterImgCount(3);
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTopicId = uri.getQueryParameter(Consts.KEY_TOPIC_ID);
        }
        topicInputView.setOnClickCommentListener(this);
    }

    @Override
    protected void initListener() {

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
        switch (v.getId()) {
            // 回复评论
            case R.id.tv_comment:
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
     * @param commentContent
     * @param repliedCommentId
     * @param repliedName
     * @param repliedUserId
     * @param selectedFile
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
        } else {
            menuItem.setIcon(R.mipmap.ic_menu_collect);
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

}
