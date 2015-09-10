package com.tongban.im.activity.discover;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.ScrollableGridView;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.adapter.ProductBookImgPagerAdapter;
import com.tongban.im.adapter.ProductPriceAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ProductBook;

/**
 * 商品详情页(图书)
 *
 * @author Cheney
 * @date 8/20
 */
public class ProductBookActivity extends BaseApiActivity implements View.OnClickListener {
    private ImageView ivBack, ivShare, ivCollect;
    private ViewPager mViewPager; // 图集
    private TextView title;  // 名称
    private FlowLayout flTag;  // 标签
    private TextView author; // 作者
    private TextView desc;  // 简介
    private TextView publisher;  // 出版社
    private TextView isbn;  // isbn
    private TextView suitable;  // 适度人群
    private ScrollableGridView mGridView;

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
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivCollect = (ImageView) findViewById(R.id.iv_collect);
        mViewPager = (ViewPager) findViewById(R.id.vp_img);
        title = (TextView) findViewById(R.id.tv_title);
        flTag = (FlowLayout) findViewById(R.id.fl_tag);
        author = (TextView) findViewById(R.id.tv_author);
        desc = (TextView) findViewById(R.id.tv_desc);
        publisher = (TextView) findViewById(R.id.tv_publisher);
        isbn = (TextView) findViewById(R.id.tv_isbn);
        suitable = (TextView) findViewById(R.id.tv_suitable_for);
        mGridView = (ScrollableGridView) findViewById(R.id.gv_platform);
        mGridView.setExpanded(true);
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
        if (v == ivBack) {
            finish();
        } else if (v == ivCollect) {
            if (!TransferCenter.getInstance().startLogin()) {
                return;
            }
            if (mProductBook != null && mProductBook.isCollect_status()) {
                ProductApi.getInstance().noCollectProduct(productBookId, this);
            } else if (mProductBook != null && !mProductBook.isCollect_status()) {
                ProductApi.getInstance().collectProduct(productBookId, this);
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
        mPagerAdapter = new ProductBookImgPagerAdapter(mContext, mProductBook.getProduct_img_url());
        mViewPager.setAdapter(mPagerAdapter);
        title.setText(mProductBook.getProduct_name());
        flTag.removeAllViews();
        author.setText(mProductBook.getBook_author());
        publisher.setText(mProductBook.getPublisher());
        isbn.setText(mProductBook.getIsbn());
        suitable.setText(mProductBook.getSuitable_for());
        desc.setText(mProductBook.getBook_content_desc());
        String[] productTags = mProductBook.getProduct_tags().split(",");
        if (productTags.length > 0) {
            for (String tag : productTags) {
                TextView tv = new TextView(mContext);
                tv.setText(tag);
                tv.setTextColor(getResources().getColor(R.color.main_deep_orange));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 10, 0);
                tv.setLayoutParams(layoutParams);
                tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corners_bg_grey));
                flTag.addView(tv);
            }
        }
        mPriceAdapter = new ProductPriceAdapter(mContext, mProductBook.getPrice_info());
        mGridView.setAdapter(mPriceAdapter);
    }

    /**
     * 收藏商品成功的Event
     *
     * @param event CollectProductEvent
     */
    public void onEventMainThread(BaseEvent.CollectProductEvent event) {
        ToastUtil.getInstance(mContext).showToast("收藏成功");
        mProductBook.setCollect_status(true);
        ivCollect.setSelected(true);
    }

    /**
     * 取消收藏商品的Event
     *
     * @param event NoCollectProductEvent
     */
    public void onEventMainThread(BaseEvent.NoCollectProductEvent event) {
        ToastUtil.getInstance(mContext).showToast("已经取消收藏");
        mProductBook.setCollect_status(false);
        ivCollect.setSelected(false);
    }
}
