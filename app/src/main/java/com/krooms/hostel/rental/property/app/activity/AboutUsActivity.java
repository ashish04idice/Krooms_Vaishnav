package com.krooms.hostel.rental.property.app.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;

/**
 * Created by user on 7/22/2016.
 */
public class AboutUsActivity extends FragmentActivity {
    private ImageButton backBtn;
    private TextView aboutUsContent;
    private String textContent = "<html>    \n" +
            "    <body>\n" +
            "        <div >\n" +
            "            <h3 style=\"text-align:center\">  ABOUT KROOMS</h3>\n" +
            "            \n" +
            "\n" +
            "<p style=\"text-align:center\">The KROOMS mobile app is the world’s first personal Hostel Room service app that allows guests staying at KROOMS to order room service directly from their smartphones. Unlike many other Real-estate websites or applications, KROOMS is focused on hostels, service apartments, paying guests and other residential rooms. Bookings can be confirmed after payments through our payment gateways.</p>\n" +
            "\n" +
            "\n" +
            "\n" +
            "        </div>\n" +
            "    </body>\n" +
            "</html>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.about_us_activity);
        createView();
    }
    public void createView() {
        backBtn = (ImageButton) findViewById(R.id.main_header_back);
        WebView mWebView = (WebView) findViewById(R.id.aboutUsWebView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setBackgroundColor(Color.WHITE);//0x00000000);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.loadUrl("file:///android_asset/aboutus.html");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // view.loadUrl(url);
            if (url.startsWith("mailto:")) {
                callEmailMethod();
            } else if (url.startsWith("http:") || url.startsWith("https:")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.krooms.com"));
                AboutUsActivity.this.startActivity(browserIntent);
            }
            return true;
        }

        public void callEmailMethod() {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/html");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@krooms.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
    }
}