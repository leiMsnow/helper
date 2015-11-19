package io.rong.imkit.tools;

import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tongban.corelib.base.activity.BaseToolBarActivity;

import io.rong.imkit.R;

/**
 * Created by weiqinxiao on 15/4/18.
 */
public class RongWebviewActivity extends BaseToolBarActivity {

    WebView mWebView;
    @Override
    protected int getLayoutRes() {
        return R.layout.rc_ac_webview;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        mWebView = (WebView) findViewById(R.id.rc_webview);

        mWebView.setVerticalScrollbarOverlay(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.setWebViewClient(new RongWebviewClient());

        mWebView.loadUrl(url);
    }

    private class RongWebviewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
