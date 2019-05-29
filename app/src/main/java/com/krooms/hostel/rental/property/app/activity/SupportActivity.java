package com.krooms.hostel.rental.property.app.activity;

import android.content.Intent;
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

import com.krooms.hostel.rental.property.app.AppError;
import com.krooms.hostel.rental.property.app.R;

/**
 * Created by user on 7/22/2016.
 */
public class SupportActivity extends FragmentActivity {

    private ImageButton backBtn;
    private TextView supportTextView;


    String supportText= "<html>    \n" +
            "    <body>\n" +
            "        <div >\n" +
            "            <h3 style=\"text-align:center\">Support</h3>\n" +
            "   <hr>         \n" +
            "<br/>\n" +
            "<h4>Krooms-Finest Rental Solutions</h4>\n" +
            "<p>Address :A-3, Pawansut Appts, Khamla road, Dev Nagar,\n" +
            "Nagpur - 440025, Maharashtra, India<br/>\n" +
            "Contact No. :9926914699,0731-4274131</p>\n" +
            "<p>Email :<a href=\"mailto:booking@krooms.in\">booking@krooms.in</a><br/>\n" +
            "Support :<a href=\"mailto:support@krooms.in\">support@krooms.in</a>\n" +
            "</p>\n" +
            "\n" +
            "        </div>\n" +
            "    </body>\n" +
            "</html>";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_activity);
        createView();


    }

    public void createView(){

        /*supportTextView = (TextView) findViewById(R.id.supportText);

        supportTextView.setText(Html.fromHtml(supportText));*/
        backBtn = (ImageButton) findViewById(R.id.main_header_back);

        WebView mWebView = (WebView) findViewById(R.id.supportWebView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setBackgroundColor(Color.WHITE);//0x00000000);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        /*mWebView.getSettings().setDefaultFontSize(getResources().getInteger(
                        R.integer.faqs_text_size));*/
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.loadUrl("file:///android_asset/support.html");

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
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // view.loadUrl(url);
            if (url.startsWith("mailto:")) {
                callEmailMethod();
            } else if (url.startsWith("http:") || url.startsWith("https:")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.apmocon.com"));
                SupportActivity.this.startActivity(browserIntent);
            }

            return true;
        }

        public void callEmailMethod() {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/html");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[] { "info@med108.com" });
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
    }

}
