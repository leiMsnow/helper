package com.tongban.im.activity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.TopicReplyAdapter;
import com.tongban.im.model.TopicReply;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题评论界面
 *
 * @author fushudi
 */
public class TopicDetailsActivity extends BaseToolBarActivity implements View.OnClickListener{
    private ListView lvReplyList;
    private Button btnCollect;
    private TopicReplyAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void initView() {
        lvReplyList = (ListView) findViewById(R.id.lv_reply_list);
    }

    @Override
    protected void initData() {
        List<TopicReply> replyList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TopicReply topicReply = new TopicReply();
            topicReply.setReplyAge("1" + i);
            topicReply.setReplyContent("说的很有道理，讲的很有道理，写的很有道理" + i);
            topicReply.setReplyNickName("打不死的小强");
            topicReply.setReplyNum("赞" + i);
            topicReply.setReplySex("男");
            topicReply.setReplyTime("08-01 14:28");
            replyList.add(topicReply);
        }
        mAdapter = new TopicReplyAdapter(mContext, R.layout.item_topic_reply_list, replyList);
        lvReplyList.addHeaderView(LayoutInflater.from(mContext).
                inflate(R.layout.header_topic_details, null));
        lvReplyList.setAdapter(mAdapter);
        btnCollect= (Button) findViewById(R.id.btn_collect);
        btnCollect.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        btnCollect.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
