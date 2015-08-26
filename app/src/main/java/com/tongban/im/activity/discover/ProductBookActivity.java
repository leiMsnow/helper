package com.tongban.im.activity.discover;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.adapter.ProductBookImgPagerAdapter;
import com.tongban.im.adapter.ProductPriceAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.ProductBook;

/**
 * 商品详情页(图书)
 *
 * @author Cheney
 * @date 8/20
 */
public class ProductBookActivity extends BaseApiActivity implements View.OnClickListener {
    private ImageView ivBack, ivShare, ivCollect;
    private ViewPager mViewPager;
    private TextView title;
    private FlowLayout flTag;
    private TextView desc;
    private GridView mGridView;

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
        desc = (TextView) findViewById(R.id.tv_desc);
        mGridView = (GridView) findViewById(R.id.gv_platform);
    }

    @Override
    protected void initData() {
        productBookId = getIntent().getStringExtra(Consts.KEY_PRODUCY_BOOK_ID);
        ProductApi.getInstance().fetchProductDetailInfo(productBookId, this);
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
        } else if (v == ivShare) {

        } else if (v == ivCollect) {

        }
    }

    /**
     * 获取图书单品成功的Event
     *
     * @param productBook ProductBook
     */
    public void onEventMainThread(ProductBook productBook) {
        mProductBook = productBook;
        mPagerAdapter = new ProductBookImgPagerAdapter(mContext, mProductBook.getProduct_img_url());
        mViewPager.setAdapter(mPagerAdapter);
        title.setText(mProductBook.getProduct_name());
        desc.setText(mProductBook.getBook_content_desc());
        flTag.removeAllViews();
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
}
