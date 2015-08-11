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
        return R.layout.activity_authority_topic_detail;
    }

    @Override
    protected void initView() {
        lvAuthorityTopicDetails = (ListView) findViewById(R.id.lv_authority_topic_details);


    }

    @Override
    protected void initData() {
        List<AuthorityTopic> authorityTopicList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AuthorityTopic authorityTopic = new AuthorityTopic();
            Product product = new Product();
            TopicReply topicReply = new TopicReply();
            Topic topic = new Topic();
            if (i % 3 == 0) {
                authorityTopic.setContentType(AuthorityTopic.CONTENT);

                product.setProductAdvantage("该产品的优势是舒适、环保，该产品的优势是舒适、环保该产品的" +
                        "优势是舒适、环保，该产品的优势是舒适、环保该产品的优势是舒适、环保" + i);
                product.setProductCollectNum("3" + i);
                product.setProductDisAdvantage("无明显不足" + i);
                product.setProductIntroduction("该产品属于当当的产品，产于1987年。该产品属于当当的产品，" +
                        "产于1987年，该产品属于当当的产品，产于1987年。该产品属于当当的产品，" +
                        "产于1987年该产品属于当当的产品，产于1987年。");
                product.setProductName("澳大利亚品牌");
                product.setProductParameter("175ml；产地：澳大利亚");
            } else if (i % 3 == 1) {
                authorityTopic.setContentType(AuthorityTopic.REPLY);
                topicReply.setPortrait_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
                topicReply.setReplyNickName("小鹿妈妈");
                topicReply.setReplyContent("该产品属于当当的产品，产于1987年。该产于1987年，该产品属于当当的产品，产于1987年。");
                topicReply.setReplyTime("19:13");
            } else if (i % 3 == 2) {
                authorityTopic.setContentType(AuthorityTopic.REPLY_NUM);
                topic.setTopicTime("19:09");
                topic.setTopicPraiseNum("90" + i);
                topic.setTopicName("宝宝专用");
            }


            authorityTopic.setProduct(product);
            authorityTopic.setTopic(topic);
            authorityTopic.setTopicReply(topicReply);
            authorityTopicList.add(authorityTopic);
        }
        mAdapter = new AuthorityTopicDetailsAdapter(mContext, authorityTopicList);
        lvAuthorityTopicDetails.addHeaderView(LayoutInflater.from(mContext).
                inflate(R.layout.activity_authority_topic_detail_header, null));
        lvAuthorityTopicDetails.setAdapter(mAdapter);

    }

    @Override
    protected void initListener() {

    }
}
