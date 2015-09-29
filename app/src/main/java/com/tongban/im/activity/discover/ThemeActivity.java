package com.tongban.im.activity.discover;

import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.DateUtils;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.ThemeBaseActivity;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.discover.ProductBook;
import com.tongban.im.model.discover.Theme;
import com.tongban.im.model.topic.Topic;

import java.util.List;

/**
 * 专题页
 * Created by Cheney on 15/8/17.
 */
public class ThemeActivity extends ThemeBaseActivity {

    private View mParent;
    // 专题头图
    private ImageView headImg;
    // 专题标题
    private TextView title;
    // 存在专题标题的布局
    private FlowLayout themeTag;
    // 创建时间
    private TextView createTime;
    // 专题描述
    private TextView themeDesc;
    // 单品列表
    private LinearLayout mProductList;
    // 相关话题的容器
    private LinearLayout mTopicContainer;
    // 相关话题列表
    private LinearLayout mTopicList;

    // 当前的专题id
    private String themeId;
    // 专题信息数据
    private Theme mTheme;
    // 商品列表
    private List<ProductBook> mProductBooks;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initView() {
        super.initView();
        mParent = findViewById(R.id.sl_parent);
        headImg = (ImageView) findViewById(R.id.iv_head);
        title = (TextView) findViewById(R.id.tv_title);
        themeTag = (FlowLayout) findViewById(R.id.fl_tag);
        createTime = (TextView) findViewById(R.id.tv_create_time);
        themeDesc = (TextView) findViewById(R.id.tv_desc);
        mProductList = (LinearLayout) findViewById(R.id.ll_product_list);
        mTopicContainer = (LinearLayout) findViewById(R.id.ll_topic);
        mTopicList = (LinearLayout) findViewById(R.id.ll_topic_list);
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            themeId = uri.getQueryParameter(Consts.KEY_THEME_ID);
            if (!TextUtils.isEmpty(themeId)) {
                ProductApi.getInstance().fetchThemeInfo(themeId, this);
            }
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    /**
     * 获取专题信息成功的事件
     *
     * @param theme Theme
     */
    public void onEventMainThread(Theme theme) {
        mParent.setVisibility(View.VISIBLE);
        this.mTheme = theme;
        // 获取专题下的单品列表
        ProductApi.getInstance().fetchProductListByThemeId(mTheme.getTheme_id(), 0, 20, this);
        // 获取专题下的相关话题,直接调用话题搜索的接口
        StringBuilder sb = new StringBuilder(mTheme.getTheme_title());
        if (mTheme.getTheme_tags() != null && mTheme.getTheme_tags().length() > 0)
            sb.append(",").append(mTheme.getTheme_tags());
        TopicApi.getInstance().searchTopicList(sb.toString(), 0, 5, this);

        // 判断是否是收藏状态
        if (mTheme.isCollect_status()) {
            ivCollect.setSelected(true);
        }
        setTitle(mTheme.getTheme_title());
        if (mTheme.getTheme_img_url() != null && mTheme.getTheme_img_url().size() > 0) {
            if (mTheme.getTheme_img_url().get(0).getMid() != null) {
                Glide.with(mContext).load(mTheme.getTheme_img_url().get(0).getMid()).into(headImg);
            } else if (mTheme.getTheme_img_url().get(0).getMin() != null) {
                Glide.with(mContext).load(mTheme.getTheme_img_url().get(0).getMin()).into(headImg);
            } else if (mTheme.getTheme_img_url().get(0).getMax() != null) {
                Glide.with(mContext).load(mTheme.getTheme_img_url().get(0).getMax()).into(headImg);
            }
        }
        title.setText(mTheme.getTheme_title());
        String[] themeTags = new String[]{"夏季", "防蚊", "止痒"};//mTheme.getTheme_tags().split(",");
        if (themeTags.length > 0) {
            themeTag.removeAllViews();
            findViewById(R.id.iv_mark).setVisibility(View.VISIBLE);
            for (String tag : themeTags) {
                TextView tv = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.include_theme_tips_text, themeTag, false);
                tv.setText(tag);
                themeTag.addView(tv);
            }
        } else {
            themeTag.setVisibility(View.GONE);
            findViewById(R.id.iv_mark).setVisibility(View.INVISIBLE);
        }
        themeDesc.setText(mTheme.getTheme_content());
        createTime.setText(DateUtils.formatDateTime(mTheme.getC_time(), mContext));
    }

    /**
     * 获取专题下的图书列表信息成功的事件
     *
     * @param themeProducts 单品列表
     */
    public void onEventMainThread(BaseEvent.FetchProductBooksInTheme themeProducts) {
        mProductBooks = themeProducts.list;
        if (mProductBooks != null && mProductBooks.size() > 0) {
            mProductList.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 32, 0, 32);
            View view;
            int pos = 1; // 商品序号
            for (final ProductBook productBook : mProductBooks) {
                view = getLayoutInflater().inflate(R.layout.item_product_list_in_theme, null, false);
                TextView cursor = (TextView) view.findViewById(R.id.tv_cursor);
                TextView title = (TextView) view.findViewById(R.id.tv_title);
                LinearLayout productImgs = (LinearLayout) view.findViewById(R.id.ll_product_img);
                TextView author = (TextView) view.findViewById(R.id.tv_author);
                TextView productDesc = (TextView) view.findViewById(R.id.tv_product_desc);
                TextView suitableFor = (TextView) view.findViewById(R.id.tv_suitable_for);
                TextView recommendCause = (TextView) view.findViewById(R.id.tv_recommend_cause);
                Button productDetail = (Button) view.findViewById(R.id.btn_detail);
                View lastLine = view.findViewById(R.id.v_last_line);
                if (pos == mProductBooks.size()) {
                    lastLine.setVisibility(View.GONE);
                }
                cursor.setText(String.valueOf("0" + pos));
                title.setText(productBook.getProduct_name());
                List<ImageUrl> imgList = productBook.getProduct_img_url();
                if (imgList != null && imgList.size() > 0) {
                    for (ImageUrl imgUrl : imgList) {
                        ImageView imageView = new ImageView(mContext);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 32, 0, 0);
                        imageView.setLayoutParams(lp);
                        Glide.with(mContext).load(imgUrl.getMid()).into(imageView);
                        productImgs.addView(imageView);
                    }
                }
                productDesc.setText(productBook.getBook_content_desc().trim());
                if (productBook.getBook_author() != null) {
                    author.setText("[" + productBook.getBook_author().trim() + "]");
                } else {
                    author.setVisibility(View.GONE);
                }
                suitableFor.setText(productBook.getSuitable_for().trim());
//                recommendCause.setText(productBook.getRecommend_cause().trim());
                recommendCause.setText("1.无味,低刺激性;\n2.防蚊,长达四小时;\n3.适用于敏感及柔嫩的皮肤。");
                productDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (productBook.getProduct_id() != null) {
                            TransferCenter.getInstance().startProductBook(productBook.getProduct_id());
                        }
                    }
                });
                mProductList.addView(view, layoutParams);
                pos++;
            }
        }
    }

    /**
     * 获取专题下的相关话题Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchTopicListEvent event) {
        List<Topic> topics = event.topicList;
        if (topics == null || topics.size() < 1) {
            mTopicContainer.setVisibility(View.GONE);
            return;
        }
        mTopicContainer.setVisibility(View.VISIBLE);
        for (int i = 0; i < topics.size(); i++) {
            final Topic topic = topics.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_topic_in_theme, null, false);
            final TextView title = (TextView) view.findViewById(R.id.tv_title);
            TextView commentCount = (TextView) view.findViewById(R.id.tv_comment_count);
            TextView collectCount = (TextView) view.findViewById(R.id.tv_collect_count);
            title.setText(topic.getTopic_title());
            commentCount.setText(topic.getComment_amount());
            collectCount.setText(topic.getCollect_amount());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title.setSelected(true);
                    TransferCenter.getInstance().startTopicDetails(topic);
                }
            });
            mTopicList.addView(view);
        }
    }

    /**
     * 收藏专题成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.CollectThemeEvent event) {
        mTheme.setCollect_status(true);
        ivCollect.setSelected(true);
        ToastUtil.getInstance(mContext).showToast("收藏成功");
    }

    /**
     * 取消收藏专题成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.NoCollectThemeEvent event) {
        mTheme.setCollect_status(false);
        ivCollect.setSelected(false);
        ToastUtil.getInstance(mContext).showToast("已取消收藏");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == ivCollect) {
            if (!TransferCenter.getInstance().startLogin()) {
                return;
            }
            if (mTheme != null && !mTheme.isCollect_status()) {
                // 未收藏时,点击收藏
                ProductApi.getInstance().collectTheme(themeId, this);
                ivCollect.setEnabled(false);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivCollect.setEnabled(true);
                    }
                }, 1500);
            } else if (mTheme != null && mTheme.isCollect_status()) {
                // 已收藏,点击取消收藏
                ProductApi.getInstance().noCollectTheme(themeId, this);
                ivCollect.setEnabled(false);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivCollect.setEnabled(true);
                    }
                }, 1500);
            }
        }
    }
}
