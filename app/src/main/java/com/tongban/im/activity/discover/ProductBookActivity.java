package com.tongban.im.activity.discover;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.corelib.widget.view.indicator.CirclePageIndicator;
import com.tongban.corelib.widget.view.transformer.DepthPageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.base.ThemeBaseActivity;
import com.tongban.im.adapter.ProductBookImgPagerAdapter;
import com.tongban.im.adapter.ProductPriceAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.discover.ProductBook;
import com.tongban.im.widget.view.ChildGridView;

/**
 * 商品详情页(图书)
 *
 * @author Cheney
 * @date 8/20
 */
public class ProductBookActivity extends ThemeBaseActivity implements View.OnClickListener {
    private View mParent;

    private CirclePageIndicator indicator;
    private ViewPager mViewPager; // 图集
    private TextView title;  // 名称
    private FlowLayout themeTag;  // 标签
    private TextView author; // 作者
    private TextView desc;  // 简介
    private TextView publisher;  // 出版社
    private TextView isbn;  // isbn
    private TextView suitable;  // 适度人群
    private ChildGridView mGridView;

    // 图书id
    private String productBookId;
    private ProductBook mProductBook;
    private ProductBookImgPagerAdapter mPagerAdapter;
    private ProductPriceAdapter mPriceAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_product_book;
    }

    @Override
    protected void initView() {
        super.initView();
        mParent = findViewById(R.id.sl_parent);
        mViewPager = (ViewPager) findViewById(R.id.vp_img);
        indicator = (CirclePageIndicator) findViewById(R.id.lpi_indicator);
        title = (TextView) findViewById(R.id.tv_title);
        themeTag = (FlowLayout) findViewById(R.id.fl_tag);
        author = (TextView) findViewById(R.id.tv_author);
        desc = (TextView) findViewById(R.id.tv_desc);
        publisher = (TextView) findViewById(R.id.tv_publisher);
        isbn = (TextView) findViewById(R.id.tv_isbn);
        suitable = (TextView) findViewById(R.id.tv_suitable_for);
        mGridView = (ChildGridView) findViewById(R.id.gv_platform);
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            productBookId = uri.getQueryParameter(Consts.KEY_PRODUCT_BOOK_ID);
            if (!TextUtils.isEmpty(productBookId))
                ProductApi.getInstance().fetchProductDetailInfo(productBookId, this);
        }
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == ivCollect) {
            if (!TransferCenter.getInstance().startLogin()) {
                return;
            }
            if (mProductBook != null && mProductBook.isCollect_status()) {
                ProductApi.getInstance().noCollectProduct(productBookId, this);
                setCollectEnable();
            } else if (mProductBook != null && !mProductBook.isCollect_status()) {
                ProductApi.getInstance().collectProduct(productBookId, this);
                setCollectEnable();
            }
        } else if (v == ivShare) {

        }
    }

    /**
     * 获取图书单品成功的Event
     *
     * @param productBook ProductBook
     */
    public void onEventMainThread(ProductBook productBook) {
        mProductBook = productBook;
        if (mProductBook.isCollect_status()) {
            ivCollect.setSelected(true);
        }
        mParent.setVisibility(View.VISIBLE);

        mPagerAdapter = new ProductBookImgPagerAdapter(mContext, mProductBook.getProduct_img_url());
        mViewPager.setAdapter(mPagerAdapter);
        indicator.setViewPager(mViewPager);
        if (mPagerAdapter.getCount() > 1) {
            indicator.setVisibility(View.VISIBLE);
        }
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        title.setText(mProductBook.getProduct_name());
        themeTag.removeAllViews();
        if (mProductBook.getBook_author() != null && !"".equals(mProductBook.getBook_author().trim())) {
            author.setText(mProductBook.getBook_author());
        }
        publisher.setText(mProductBook.getPublisher());
        isbn.setText(mProductBook.getIsbn());
        suitable.setText(mProductBook.getSuitable_for());
        desc.setText(mProductBook.getBook_content_desc());
        String[] productTags = mProductBook.getProduct_tags().split(",");
        if (productTags.length > 0) {
            themeTag.removeAllViews();
            findViewById(R.id.iv_mark).setVisibility(View.VISIBLE);
            for (String tag : productTags) {
                TextView tv = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.include_theme_tips_text, themeTag, false);
                tv.setText(tag);
                themeTag.addView(tv);
            }
        }
        mPriceAdapter = new ProductPriceAdapter(mContext, R.layout.item_product_book_price_grid
                , mProductBook.getPrice_info());
        mGridView.setAdapter(mPriceAdapter);
    }

    /**
     * 收藏商品成功的Event
     *
     * @param event CollectProductEvent
     */
    public void onEventMainThread(BaseEvent.CollectProductEvent event) {
        super.onEventMainThread(event);
        mProductBook.setCollect_status(true);
    }

    /**
     * 取消收藏商品的Event
     *
     * @param event NoCollectProductEvent
     */
    public void onEventMainThread(BaseEvent.NoCollectProductEvent event) {
        super.onEventMainThread(event);
        mProductBook.setCollect_status(false);
    }
}
