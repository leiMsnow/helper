package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.TopicAdapter;
import com.tongban.im.model.Topic;
import com.tongban.im.model.User;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.topic_list);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic;
    }

    @Override
    protected void initView() {
        mListView = (ListView) findViewById(R.id.lv_topic_list);
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        List<Topic> listsByHot = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Topic topic = new Topic();
            topic.setContentType(Topic.TEXT);
            if (i % 2 == 0) {
                topic.setContentType(Topic.IMAGE);
                List<String> smallUrls = new ArrayList<>();
                smallUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
                topic.setSmallUrl(smallUrls);
            }
//            List<String> bigUrls = new ArrayList<>();
//            bigUrls.add("http://img2.3lian.com/2014/f7/5/d/22.jpg");
//            topic.setBigUrl(bigUrls);
            topic.setTopicContent("RayRay的爸爸：#食物中含有硫酸锌？酸奶？#" + i);
            topic.setTopicName("什么食物中含有硫酸锌？" + i);
            topic.setTopicReplyNum("评论" + i);
            topic.setTopicPraiseNum("赞" + i);
            topic.setTopicTime("2015-08-05");
            User user = new User();
            user.setSex("男");
            user.setAddress("北京");
            user.setAge(i + "岁");
            user.setPortrait_url("http://b.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4a244792ca38b87d6277ff942.jpg");
            user.setNick_name("小明" + i);
            topic.setUser(user);
            listsByHot.add(topic);
        }
        mAdapter = new TopicAdapter(mContext, listsByHot);
        mListView.setAdapter(mAdapter);
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
        startActivity(new Intent(mContext, TopicDetailActivity.class));
    }

    @Override
    public void onClick(View v) {

    }
}
