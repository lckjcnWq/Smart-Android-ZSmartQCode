// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.theswitchbot.common.widget.web;

import android.content.Intent;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.theswitchbot.common.R;
import com.theswitchbot.common.databinding.ActivityWebviewBinding;
import com.theswitchbot.common.ui.BaseActivity;
import com.theswitchbot.common.util.AppUtil;
import com.theswitchbot.common.util.TipUtil;

import org.jetbrains.annotations.NotNull;

/**
 * This Activity is used as a fallback when there is no browser installed that supports
 * Chrome Custom Tabs
 */
public class WebviewActivity extends BaseActivity<ActivityWebviewBinding> {
    public static final String EXTRA_URL = "extra.url";
    @NotNull
    @Override
    public ActivityWebviewBinding binding() {
        return ActivityWebviewBinding.inflate(this.getLayoutInflater());
    }

    @Override
    public void setup() {
        switchStatusBarTextColor(false);

        String url = getIntent().getStringExtra(EXTRA_URL);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setTitle(url);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        webView.loadUrl(url);
        QMUITipDialog loading = TipUtil.INSTANCE.showLoading(this,
                R.string.net_requesting,
                QMUITipDialog.Builder.ICON_TYPE_LOADING);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                if (loading != null){
                    try {
                        loading.dismiss();
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                //如果是alexa授权链接，则跳转到指定activity（用于处理oppo默认浏览器App Links无法工作的问题）
                if (url.startsWith(AppUtil.ALEXA_CODE_URL)){
                    Intent intent = new Intent();
                    intent.setClassName(getPackageName(),
                            "com.theswitchbot.device.impl.common.setting.accountLink.AlexaLinkActivity");
                    intent.setData(request.getUrl());
                    startActivity(intent);
                    finish();
                } else if (url.startsWith(AppUtil.ALEXA_WEB_CANCEL)) {
                    finish();
                }
                return super.shouldOverrideUrlLoading(view,request);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
