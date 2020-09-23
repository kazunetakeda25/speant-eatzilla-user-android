package com.speant.user.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;

import com.speant.user.Common.SessionManager;
import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_faq);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        toolbar.setTitle("FAQ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadWebViewLoad(webView);
        Log.e("TAG", "onCreate: "+sessionManager.getFaq() );
    }

    private void loadWebViewLoad(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new SSLTolerentWebViewClient());
        webView.loadUrl(sessionManager.getFaq());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage(getResources().getString(R.string.txt_load_web));
            builder.setPositiveButton(getResources().getString(R.string.txt_continue), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                    onBackPressed();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}
