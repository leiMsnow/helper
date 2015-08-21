package com.tongban.im.activity.discover;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.DateUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.api.ProductApi;
import com.tongban.im.model.MultiProduct;
import com.tongban.im.model.ProductBook;
import com.tongban.im.model.User;

import java.util.List;

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
        setTitle("");
        // cey id是假的
        ProductApi.getInstance().fetchMultiProductInfo("55c9b0f5bbafae4478f5dac3", this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_multi_product, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.collect) {
            ToastUtil.getInstance(mContext).showToast("收藏接口还没做~~~");
            return true;
        } else if (itemId == R.id.share) {
            ToastUtil.getInstance(mContext).showToast("分享接口还没有~~~");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取专题信息成功的事件
     *
     * @param multiProduct MultiProduct
     */
    public void onEventMainThread(MultiProduct multiProduct) {
        // 获取专题发布人的信息
        AccountApi.getInstance().getUserInfoByUserId(multiProduct.getUser_id(), this);
        // 获取专题下的单品列表 cey 接口对不上UI,待分析
        //ProductApi.getInstance().fetchSimpleByMultiId(multiProduct.getTheme_id(), 0, 20, this);

        setTitle(multiProduct.getTheme_title());
        if (multiProduct.getTheme_img_url().length > 0) {
            Glide.with(mContext).load(multiProduct.getTheme_img_url()[0]).into(headImg);
        }
        title.setText(multiProduct.getTheme_title());
        multiTag.removeAllViews();
        String[] themeTags = multiProduct.getTheme_tags().split(",");
        if (themeTags.length > 0) {
            for (String tag : themeTags) {
                TextView tv = new TextView(mContext);
                tv.setText(tag);
                tv.setTextColor(getResources().getColor(R.color.main_deep_orange));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 5, 0);
                tv.setLayoutParams(layoutParams);
                tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corners_bg_grey));
                multiTag.addView(tv);
            }
        } else {
            multiTag.setVisibility(View.GONE);
        }
        multiDesc.setText(multiProduct.getTheme_content());
        createTime.setText(DateUtils.longToString(multiProduct.getC_time(), "MM-dd hh:mm a"));
    }

    /**
     * 获取专题发布人信息成功的事件
     *
     * @param user User
     */
    public void onEventMainThread(User user) {
        Glide.with(mContext).load(user.getPortrait_url()).into(userPortrait);
        userName.setText(user.getNick_name());
        userTag.setText(user.getTags());
    }

    /**
     * 获取专题下的图书列表信息成功的事件
     *
     * @param list 单品列表
     */
    public void onEventMainThread(List<ProductBook> list) {

    }


}
