package com.tongban.im.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.TopicAdapter;
import com.tongban.im.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题列表界面
 *
 * @author fushudi
 */
public class TopicActivity extends BaseToolBarActivity implements AbsListView.OnItemClickListener, View.OnClickListener {

    private ListView mListView;
    private TopicAdapter mAdapter;
    private TextView tvTopicByHot;
    private TextView tvTopicByMe;
    private View moveLine;
    private int screenWidth;
    private float moveLineFrom;
    private float moveLineTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic;
    }

    @Override
    protected void initView() {
        mListView = (ListView) findViewById(R.id.lv_chat_list);
        tvTopicByHot = (TextView) findViewById(R.id.tv_topic_by_hot);
        tvTopicByMe = (TextView) findViewById(R.id.tv_topic_by_me);
        moveLine = findViewById(R.id.move_line);
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
        tvTopicByMe.setOnClickListener(this);
        tvTopicByHot.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        screenWidth = ScreenUtils.getScreenWidth(mContext);
        ViewGroup.LayoutParams params = moveLine.getLayoutParams();
        params.width = screenWidth / 2;
        params.height = 4;

        moveLine.setLayoutParams(params);


        List<Topic> listsByHot = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Topic topic = new Topic();
            topic.setTopicContent("RayRay的爸爸：#食物中含有硫酸锌？酸奶？#" + i);
            topic.setTopicName("什么食物中含有硫酸锌？" + i);
            topic.setTopicReplyNum(String.valueOf(i));
            listsByHot.add(topic);
        }
        mAdapter = new TopicAdapter(mContext, R.layout.item_topic_list, listsByHot);
        mListView.setAdapter(mAdapter);
        tvTopicByHot.setSelected(true);
        tvTopicByMe.setSelected(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_topic_settings) {

            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        if (v == tvTopicByHot) {
            moveLineFrom = moveLine.getX();
            moveLineTo =moveLineFrom - moveLine.getWidth();
            ObjectAnimator moveLeftLineAnim = ObjectAnimator.ofFloat(moveLine, "translationX", moveLineFrom, moveLineTo);
            moveLeftLineAnim.setDuration(300);
            moveLeftLineAnim.start();

            mAdapter.clear();
            for (int i = 0; i < 10; i++) {
                Topic topic = new Topic();
                topic.setTopicContent("RayRay的爸爸：#食物中含有硫酸锌？酸奶？#" + i);
                topic.setTopicName("什么食物中含有硫酸锌？" + i);
                topic.setTopicReplyNum(String.valueOf(i));
                mAdapter.add(topic);
            }
            tvTopicByHot.setSelected(true);
            tvTopicByMe.setSelected(false);
        } else if (v == tvTopicByMe) {
            moveLineFrom = moveLine.getX();
            moveLineTo = moveLineFrom + moveLine.getWidth();
            ObjectAnimator moveRightLineAnim = ObjectAnimator.ofFloat(moveLine, "translationX", moveLineFrom, moveLineTo);
            moveRightLineAnim.setDuration(300);
            moveRightLineAnim.start();

            mAdapter.clear();
            for (int i = 0; i < 4; i++) {
                Topic topic = new Topic();
                topic.setTopicContent("飞飞的妈妈：#大家的宝宝都是几个月会说话的？#" + i);
                topic.setTopicName("大家的宝宝都是几个月会说话的" + i);
                topic.setTopicReplyNum(String.valueOf(i));
                mAdapter.add(topic);
            }
            tvTopicByHot.setSelected(false);
            tvTopicByMe.setSelected(true);
        }
    }
}
