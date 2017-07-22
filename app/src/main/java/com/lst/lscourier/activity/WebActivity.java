package com.lst.lscourier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.lst.lscourier.R;


/**
 * Created by lst718-011 on 2017/6/13.
 * 价格说明
 */
public class WebActivity extends Activity {
    private String result,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_price_show);
         result = getIntent().getStringExtra("weburl");
         title = getIntent().getStringExtra("title");
        initTitle();
        initView();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(title);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.this.finish();
            }
        });
    }

    private void initView() {
        WebView mWebView = (WebView) findViewById(R.id.web_show);
        mWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        // 加载需要显示的网页
        mWebView.loadUrl(result);
        // 设置Web视图
        mWebView.setWebViewClient(new HelloWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
    }
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
