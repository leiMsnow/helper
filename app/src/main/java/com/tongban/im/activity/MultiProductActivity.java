package com.tongban.im.activity;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

/**
 * 专题页
 * Created by Cheney on 15/8/17.
 */
public class MultiProductActivity extends BaseToolBarActivity {
    // 专题头图
    private ImageView headImg;
    // 专题标题
    private TextView title;
    // 存在专题标题的布局
    private FlowLayout multiTag;
    // 发表人头像
    private CircleImageView userPortrait;
    // 发表人姓名
    private TextView userName;
    // 发表人标签
    private TextView userTag;
    // 创建时间
    private TextView createTime;
    // 专题描述
    private TextView multiDesc;
    // 单品列表和相关话题列表
    private ListView productList, topicList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_multi_product;
    }

    @Override
    protected void initView() {
        headImg = (ImageView) findViewById(R.id.iv_head);
        title = (TextView) findViewById(R.id.tv_title);
        multiTag = (FlowLayout) findViewById(R.id.fl_tag);
        userPortrait = (CircleImageView) findViewById(R.id.iv_user_portrait);
        userName = (TextView) findViewById(R.id.tv_user_name);
        userTag = (TextView) findViewById(R.id.tv_user_tag);
        createTime = (TextView) findViewById(R.id.tv_create_time);
        multiDesc = (TextView) findViewById(R.id.tv_desc);
        productList = (ListView) findViewById(R.id.lv_product);
        topicList = (ListView) findViewById(R.id.lv_topic);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
