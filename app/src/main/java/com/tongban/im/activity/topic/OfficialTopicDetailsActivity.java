package com.tongban.im.activity.topic;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.base.adapter.IMultiItemTypeSupport;
import com.tongban.im.R;
import com.tongban.im.activity.CommonImageResultActivity;
import com.tongban.im.adapter.OfficialTopicDetailsAdapter;
import com.tongban.im.model.OfficialTopic;
import com.tongban.im.model.Product;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicComment;
import com.tongban.im.widget.view.TopicInputView;

import java.util.ArrayList;
import java.util.List;


/**
 * 官方话题评论界面official
 *
 * @author fushudi
 */
public class OfficialTopicDetailsActivity extends CommonImageResultActivity implements View.OnClickListener {
    private ListView lvAuthorityTopicDetails;
    private OfficialTopicDetailsAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_official_topic_details;
    }

    @Override
    protected void initView() {
        lvAuthorityTopicDetails = (ListView) findViewById(R.id.lv_authority_topic_details);
        topicInputView = (TopicInputView) findViewById(R.id.topic_input);
        topicInputView.setAdapterImgCount(3);
    }

    @Override
    protected void initData() {
        List<OfficialTopic> officialTopicList = new ArrayList<>();

        //产品相关
        OfficialTopic officialTopic = new OfficialTopic();
        Product product = new Product();
        officialTopic.setContentType(OfficialTopic.CONTENT);
        product.setProductAdvantage("该产品的优势是舒适、环保，该产品的优势是舒适、环保该产品的" +
                "优势是舒适、环保，该产品的优势是舒适、环保该产品的优势是舒适、环保");
        product.setProductCollectNum("3");
        product.setProductDisAdvantage("无明显不足");
        product.setProductIntroduction("该产品属于当当的产品，产于1987年。该产品属于当当的产品，" +
                "产于1987年，该产品属于当当的产品，产于1987年。该产品属于当当的产品，" +
                "产于1987年该产品属于当当的产品，产于1987年。");
        product.setProduct_name("澳大利亚品牌");
        product.setProductParameter("175ml；产地：澳大利亚");
        product.setProduct_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
        product.setProduct_icon_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
        officialTopic.setProduct(product);
        officialTopicList.add(officialTopic);

        //数量（评论、点赞）相关
        officialTopic = new OfficialTopic();
        Topic topic = new Topic();
        officialTopic.setContentType(OfficialTopic.REPLY_NUM);
//        topic.setTopicPraiseNum("90");
//        topic.setTopicReplyNum("100");
        officialTopic.setTopic(topic);
        officialTopicList.add(officialTopic);


        //评论相关
        officialTopic = new OfficialTopic();
        TopicComment topicReply = new TopicComment();
        officialTopic.setContentType(OfficialTopic.REPLY);
//        topicReply.setPortrait_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
//        topicReply.setReplyNickName("小鹿妈妈");
//        topicReply.setComment_content("该产品属于当当的产品，产于1987年。该产于1987年，该产品属于当当的产品，产于1987年。");
//        topicReply.setReplyTime("19:13");
        officialTopic.setTopicReply(topicReply);
        officialTopicList.add(officialTopic);

        officialTopic = new OfficialTopic();
        topicReply = new TopicComment();
        officialTopic.setContentType(OfficialTopic.REPLY);
//        topicReply.setPortrait_url("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d16633a4168f1deb48f8c54643e.jpg");
//        topicReply.setReplyNickName("小鹿妈妈");
//        topicReply.setComment_content("该产品属于当当的产品，产于1987年。该产于1987年，该产品属于当当的产品，产于1987年。");
//        topicReply.setReplyTime("19:13");
        officialTopic.setTopicReply(topicReply);
        officialTopicList.add(officialTopic);
        mAdapter = new OfficialTopicDetailsAdapter(mContext, officialTopicList, new IMultiItemTypeSupport<OfficialTopic>() {
            @Override
            public int getLayoutId(int position, OfficialTopic o) {
                if (o.getContentType() == OfficialTopic.CONTENT) {
                    return R.layout.item_official_topic_details_content;
                } else if (o.getContentType() == OfficialTopic.REPLY_NUM) {
                    return R.layout.item_official_topic_details_reply_num;
                } else if (o.getContentType() == OfficialTopic.REPLY) {
                    return R.layout.item_topic_reply_list;
                }
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 3;
            }

            @Override
            public int getItemViewType(int position, OfficialTopic o) {
                return o.getContentType();
            }
        });
        lvAuthorityTopicDetails.addHeaderView(LayoutInflater.from(mContext).
                inflate(R.layout.activity_official_topic_details_header, null));
        lvAuthorityTopicDetails.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void onClick(View v) {
    }
}
