package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.ChatAdapter;
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
    private ChatAdapter mAdapter;
    private TextView tvTopicByHot;
    private TextView tvTopicByMe;
    private View moveLine;
    private int screenWidth;
    private Button btnCreateCircle;

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
        btnCreateCircle = (Button) findViewById(R.id.btn_create_circle);
        // moveLine = findViewById(R.id.move_line);
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
        tvTopicByMe.setOnClickListener(this);
        tvTopicByHot.setOnClickListener(this);
        btnCreateCircle.setOnClickListener(this);
    }

    @Override
    protected void initData() {

//        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.width = screenWidth / 2;
//        params.height=4;
//        moveLine.setLayoutParams(params);

        List<Topic> listsByHot = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Topic topic = new Topic();
            topic.setTopicContext("youyong" + i);
            topic.setTopicName("name" + i);
            topic.setTopicPersonNum(String.valueOf(i));
            listsByHot.add(topic);
        }
        mAdapter = new ChatAdapter(mContext, R.layout.item_chat_list, listsByHot);
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
            startActivity(new Intent(mContext, GroupLabelActivity.class));

            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_topic_by_hot:
                mAdapter.clear();
                for (int i = 0; i < 10; i++) {
                    Topic topic = new Topic();
                    topic.setTopicContext("youyong" + i);
                    topic.setTopicName("name" + i);
                    topic.setTopicPersonNum(String.valueOf(i));
                    mAdapter.add(topic);
                }
                tvTopicByHot.setSelected(true);
                tvTopicByMe.setSelected(false);
                break;
            case R.id.tv_topic_by_me:
                mAdapter.clear();
                for (int i = 0; i < 4; i++) {
                    Topic topic = new Topic();
                    topic.setTopicContext("youyong" + i);
                    topic.setTopicName("name" + i);
                    topic.setTopicPersonNum(String.valueOf(i));
                    mAdapter.add(topic);
                }
                tvTopicByHot.setSelected(false);
                tvTopicByMe.setSelected(true);
                break;
            case R.id.btn_create_circle:
                startActivity(new Intent(mContext, CreateGroupActivity.class));
                break;
        }
    }
}
