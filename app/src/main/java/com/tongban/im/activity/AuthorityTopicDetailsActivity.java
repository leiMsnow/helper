package com.tongban.im.activity;

import android.view.LayoutInflater;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.AuthorityTopicDetailsAdapter;
import com.tongban.im.model.AuthorityTopic;
import com.tongban.im.model.Product;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicReply;

import java.util.ArrayList;
import java.util.List;


/**
 * 官方话题评论界面
 *
 * @author fushudi
 */
public class AuthorityTopicDetailsActivity extends BaseToolBarActivity {
    private ListView lvAuthorityTopicDetails;
    private AuthorityTopicDetailsAdapter mAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_authority_topic_details;
    }

    @Override
    protected void initView() {
        lvAuthorityTopicDetails = (ListView) findViewById(R.id.lv_authority_topic_details);


    }

    @Override
    protected void initData() {
        List<AuthorityTopic> authorityTopicList = new ArrayList<>();

        //产品相关
        AuthorityTopic authorityTopic = new AuthorityTopic();
        Product product = new Product();
        authorityTopic.setContentType(AuthorityTopic.CONTENT);
        product.setProductAdvantage("该产品的优势是舒适、环保，该产品的优势是舒适、环保该产品的" +
                "优势是舒适、环保，该产品的优势是舒适、环保该产品的优势是舒适、环保");
        product.setProductCollectNum("3");
        product.setProductDisAdvantage("无明显不足");
        product.setProductIntroduction("该产品属于当当的产品，产于1987年。该产品属于当当的产品，" +
                "产于1987年，该产品属于当当的产品，产于1987年。该产品属于当当的产品，" +
                "产于1987年该产品属于当当的产品，产于1987年。");
        product.setProductName("澳大利亚品牌");
        product.setProductParameter("175ml；产地：澳大利亚");
        product.setProduct_img_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
        product.setProduct_icon_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
        authorityTopic.setProduct(product);
        authorityTopicList.add(authorityTopic);

        //数量（评论、点赞）相关
        authorityTopic = new AuthorityTopic();
        Topic topic = new Topic();
        authorityTopic.setContentType(AuthorityTopic.REPLY_NUM);
        topic.setTopicPraiseNum("90");
        topic.setTopicReplyNum("100");
        authorityTopic.setTopic(topic);
        authorityTopicList.add(authorityTopic);


        //评论相关
        authorityTopic = new AuthorityTopic();
        TopicReply topicReply = new TopicReply();
        authorityTopic.setContentType(AuthorityTopic.REPLY);
        topicReply.setPortrait_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
        topicReply.setReplyNickName("小鹿妈妈");
        topicReply.setReplyContent("该产品属于当当的产品，产于1987年。该产于1987年，该产品属于当当的产品，产于1987年。");
        topicReply.setReplyTime("19:13");
        authorityTopic.setTopicReply(topicReply);
        authorityTopicList.add(authorityTopic);

        authorityTopic = new AuthorityTopic();
        topicReply = new TopicReply();
        authorityTopic.setContentType(AuthorityTopic.REPLY);
        topicReply.setPortrait_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
        topicReply.setReplyNickName("小鹿妈妈");
        topicReply.setReplyContent("该产品属于当当的产品，产于1987年。该产于1987年，该产品属于当当的产品，产于1987年。");
        topicReply.setReplyTime("19:13");
        authorityTopic.setTopicReply(topicReply);
        authorityTopicList.add(authorityTopic);

        mAdapter = new AuthorityTopicDetailsAdapter(mContext, authorityTopicList);
        lvAuthorityTopicDetails.addHeaderView(LayoutInflater.from(mContext).
                inflate(R.layout.activity_authority_topic_details_header, null));
        lvAuthorityTopicDetails.setAdapter(mAdapter);

    }

    @Override
    protected void initListener() {

    }
}
