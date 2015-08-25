package com.tongban.im.activity.discover;

import android.view.View;
import android.widget.ImageView;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.im.R;

/**
 * 商品详情页(图书)
 *
 * @author Cheney
 * @date 8/20
 */
public class ProductBookActivity extends BaseApiActivity implements View.OnClickListener {
    private ImageView ivBack, ivShare, ivCollect;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_product_book;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivCollect = (ImageView) findViewById(R.id.chb_collect);

    }

    @Override
    protected void initData() {

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
}
