package com.tongban.im.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;

import io.rong.imkit.model.Event;

/**
 * 聊天界面
 *
 * @author zhangleilei
 * @createTime 2015/7/16
 */
public class ChatActivity extends BaseToolBarActivity implements View.OnClickListener {

    private String mTargetId;
    private String mTitle;

    private View topicLayout;
    private ImageView ivTopic;
    private TextView tvTopic;
    private TextView tvTopicDetails;
    //是否有话题可以展开
    private boolean isTopicContent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        topicLayout = findViewById(R.id.fl_topic);
        ivTopic = (ImageView) findViewById(R.id.iv_topic);
        tvTopic = (TextView) findViewById(R.id.tv_topic);
        tvTopicDetails = (TextView) findViewById(R.id.tv_topic_details);
    }

    @Override
    protected void initListener() {
        ivTopic.setOnClickListener(this);
        tvTopic.setOnClickListener(this);
        tvTopicDetails.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTargetId = uri.getQueryParameter("targetId");
            mTitle = uri.getQueryParameter("title");
            if (!TextUtils.isEmpty(mTitle)) {
                setTitle(mTitle);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.next_step) {
            Intent intent = new Intent(mContext,GroupInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Consts.KEY_GROUP_ID,mTargetId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == ivTopic) {
            if (isTopicContent) {
                if (tvTopic.getVisibility() == View.VISIBLE) {
                    setTopicInfoAnimator(true);
                } else {
                    setTopicInfoAnimator(false);
                }
            }
        }else if (v == tvTopic || v == tvTopicDetails) {
//            Intent intent = new Intent(mContext, TopicActivity.class);
//            startActivity(intent);
        }
    }

    private void setTopicInfoAnimator(final boolean isOpen) {

        ObjectAnimator rotation1 = ObjectAnimator.ofFloat(ivTopic, "rotation", 90, 0);
        rotation1.setDuration(300);

        tvTopic.setPivotX(tvTopic.getLeft());
        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(tvTopic, "scaleX", 1.0f, 0.0f);
        scaleX1.setDuration(300);
        scaleX1.start();

        ObjectAnimator rotation = ObjectAnimator.ofFloat(ivTopic, "rotation", 0, 90);
        rotation.setDuration(300).setStartDelay(300);

        ObjectAnimator topicAnimator = ObjectAnimator.ofFloat(tvTopicDetails, "scaleX", 0.0f, 1.0f);
        topicAnimator.setDuration(300).setStartDelay(500);

        if (isOpen) {
            tvTopic.setPivotX(tvTopicDetails.getLeft());
        } else {
            rotation1 = ObjectAnimator.ofFloat(ivTopic, "rotation", 0, 90);
            rotation1.setStartDelay(300);
            rotation = ObjectAnimator.ofFloat(ivTopic, "rotation", 90, 0);
            topicAnimator = ObjectAnimator.ofFloat(tvTopicDetails, "scaleX", 1.0f, 0.0f);

            tvTopic.setPivotX(tvTopic.getLeft());
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvTopic, "scaleX", 0.0f, 1.0f);
            scaleX.setDuration(300).setStartDelay(300);
            scaleX.start();
        }
        rotation1.start();
        rotation.start();
        topicAnimator.start();

        rotation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    tvTopic.setVisibility(View.INVISIBLE);
                    tvTopicDetails.setVisibility(View.VISIBLE);
                } else {
                    tvTopic.setVisibility(View.VISIBLE);
                    tvTopicDetails.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void onEventMainThread(Event.LastTopicNameEvent topicNameEvent) {
        isTopicContent = true;
        tvTopicDetails.setText(topicNameEvent.getTopicName());
        tvTopicDetails.setVisibility(View.INVISIBLE);
        tvTopic.setVisibility(View.VISIBLE);
    }
}
