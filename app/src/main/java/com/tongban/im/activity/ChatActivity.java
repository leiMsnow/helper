package com.tongban.im.activity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

import io.rong.imkit.RongContext;
import io.rong.imkit.model.Event;

/**
 * 聊天界面
 *
 * @author zhangleilei
 * @createTime 2015/7/16
 */
public class ChatActivity extends BaseToolBarActivity implements View.OnClickListener {

    private String targetId;
    private String title;

    private View topicLayout;
    private ImageView ivTopic;
    private TextView tvTopic;
    private TextView tvTopicDetails;

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
            targetId = uri.getQueryParameter("targetId");
            title = uri.getQueryParameter("title");
            if (!TextUtils.isEmpty(title)) {
                setTitle(title);
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
        if (itemId == R.id.action_chat_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == ivTopic) {
            if (tvTopic.getVisibility() == View.VISIBLE) {
                tvTopic.setVisibility(View.INVISIBLE);
                tvTopicDetails.setVisibility(View.VISIBLE);
            } else {
                tvTopic.setVisibility(View.VISIBLE);
                tvTopicDetails.setVisibility(View.INVISIBLE);
            }
        } else if (v == tvTopic || v == tvTopicDetails) {
            Intent intent = new Intent(mContext, TopicActivity.class);
            startActivity(intent);
        }
    }

    private void setTopicInfoAnimator() {
        float start = tvTopic.getRight();
        float end = topicLayout.getRight();
        ObjectAnimator topicAnimator = ObjectAnimator.ofFloat(tvTopicDetails, "x", start, end);
        if (tvTopic.getVisibility() != View.VISIBLE) {
            start = topicLayout.getRight();
            end = tvTopic.getRight();
        }else{
            tvTopicDetails.setVisibility(View.VISIBLE);
            topicAnimator = ObjectAnimator.ofFloat(tvTopicDetails, "scaleX", 0.0f, 1.0f);
        }
        topicAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        topicAnimator.setDuration(500);
        topicAnimator.start();
    }

    public void onEventMainThread(Event.LastTopicNameEvent topicNameEvent){
        tvTopicDetails.setText(topicNameEvent.getTopicName());
        tvTopicDetails.setVisibility(View.INVISIBLE);
        tvTopic.setVisibility(View.VISIBLE);
    }
}
