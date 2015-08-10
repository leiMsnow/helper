package com.tongban.im.activity;

import android.view.LayoutInflater;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.AuthorityTopicDetailAdapter;
import com.tongban.im.adapter.TopicReplyAdapter;
import com.tongban.im.model.Product;
import com.tongban.im.model.TopicReply;

import java.util.ArrayList;
import java.util.List;

/**
 * 官方话题评论界面
 *
 * @author fushudi
 */
public class AuthorityTopicDetailActivity extends BaseToolBarActivity {
    private ListView lvReplyList, lvProductList;
    private TopicReplyAdapter mAdapter;
    private AuthorityTopicDetailAdapter mAuthorityTopicDetailAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_authority_topic_detail;
    }

    @Override
    protected void initView() {
        lvReplyList = (ListView) findViewById(R.id.lv_reply_list);
        lvProductList = (ListView) findViewById(R.id.lv_authority_topic_detail);
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
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            product.setProductAdvantage("好看的回复客户身份" + i);
            product.setProductCollectNum("3" + i);
            product.setProductDisAdvantage("斯蒂芬斯蒂芬森防" + i);
            product.setProductIntroduction("据了解基金基金基金");
            product.setProductName("似懂非懂是");
            product.setProductParameter("大范甘迪郭德纲");
            productList.add(product);
        }
        mAdapter = new TopicReplyAdapter(mContext, R.layout.item_topic_details_list, replyList);
        lvReplyList.addHeaderView(LayoutInflater.from(mContext).
                inflate(R.layout.activity_authority_topic_reply_header, null));
        mAuthorityTopicDetailAdapter = new AuthorityTopicDetailAdapter(mContext, R.layout.item_authority_topic_detail, productList);
        lvProductList.setAdapter(mAuthorityTopicDetailAdapter);
        lvReplyList.setAdapter(mAdapter);

    }

    @Override
    protected void initListener() {

    }
}
