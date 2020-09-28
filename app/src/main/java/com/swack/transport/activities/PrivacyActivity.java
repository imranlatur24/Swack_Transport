package com.swack.transport.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.swack.transport.R;

import es.dmoral.toasty.Toasty;

public class PrivacyActivity extends AppCompatActivity {

    private WebView pdfWebView;
    private String pdf;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        getSupportActionBar().setTitle("Privacy Policy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        progressBar = ProgressDialog.show(PrivacyActivity.this, "PDF File", "Loading...");
        pdf = "http://swack.in/swack/TransportApi/TraPrivacyPolice.pdf";

        pdfWebView = (WebView) findViewById(R.id.pdfWebView);
        pdfWebView.getSettings().setJavaScriptEnabled(true);
        pdfWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        pdfWebView.getSettings().setAllowFileAccess(true);
        pdfWebView.setScrollbarFadingEnabled(false);
        pdfWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toasty.error(PrivacyActivity.this, "Oh no! " + description, Toasty.LENGTH_SHORT).show();

            }
        });
        pdfWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
