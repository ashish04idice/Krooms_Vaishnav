package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.krooms.hostel.rental.property.app.R;

/**
 * Created by admin on 2/20/2017.
 */
public class Googleview360_Owner extends Activity {
    Intent in;
    String Googleviewvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google360view_layout);

        in = getIntent();
        Googleviewvalue = in.getStringExtra("googleurl");
        RelativeLayout back_button = (RelativeLayout) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //for google 360 view link should be in embeded format
        String joytatoo = "https://www.google.com/maps/embed?pb=!1m0!3m2!1sen!2sin!4v1487310532361!6m8!1m7!1sRTv6lDZXEmkAAAQvxcKypw!2m2!1d22.72464824371679!2d75.88414493345408!3f3.4921393902731097!4f-0.9231359162419892!5f0.7820865974627469";
        String ranjeetkitchen = "https://www.google.com/maps/embed?pb=!1m0!3m2!1sen!2sin!4v1487310485359!6m8!1m7!1sMT_gxu0Swi0AAAQvxSAzJw!2m2!1d22.68920722888755!2d75.82832630533517!3f352!4f0!5f0.7820865974627469";
        String chiligarden = "https://www.google.com/maps/embed?pb=!1m0!3m2!1sen!2sin!4v1487310374837!6m8!1m7!1sgCsWLO8bQswAAAQvvliOvg!2m2!1d22.66551919948334!2d75.882919188703!3f356.30989870481596!4f-3.451565845728922!5f0.7820865974627469";
        String streatviewvalue = "https://www.google.com/maps/embed?pb=!1m0!3m2!1sen!2sin!4v1486719615893!6m8!1m7!1swTalOZKyTZQAAAQvxalMSQ!2m2!1d22.69843082144687!2d75.8443685108416!3f278!4f0!5f0.7820865974627469";

        String aa = "https://www.google.co.in/maps/uv?hl=en&pb=!1s0x3962fd3ad29589e5%3A0xb1b6f750f2a81b3a!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps%3A%2F%2Flh5.googleusercontent.com%2Fp%2FAF1QipMmrCWDlSvYeG5hhGXlCkEKFdyGimzCy4SVGms1%3Dw143-h160-k-no!5sallen%20kohinoor%20hostel%20indore%20-%20Google%20Search&imagekey=!1e10!2sAF1QipOOgpoqmvGnzlhRk4Qm7hYlzK0WdmPa1KYw_e2l&sa=X&ved=2ahUKEwjOqtSeuZHbAhUFMY8KHTt3Cu0QoiowDnoFCAEQnAE";

        WebView web = (WebView) findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);

        //zoom: 0.75
        String streetWeb = "<iframe src=" + Googleviewvalue + "width=\"500\" height=\"450\" frameborder=\"0\" \"class=\"youtube-player\" &zoom=30 style=\"border:0\"  allowfullscreen></iframe>";

        String html = "<html>"

                + "<head>"
                + "</head>"
                + "<body style=\"border: 0; padding: 0\">"
                + "<iframe "
                + "type=\"text/html\" "
                + "class=\"youtube-player\" "
                + "width= 100%\""
                + "zoom=30\" "
                + "height= 95%\""
                + "allowfullscreen\" "
                + "src=" + Googleviewvalue + "frameborder=\"0\"></iframe>"
                + "</body>"
                + "</html>";

        web.loadData(streetWeb, "text/html", null);
        web.getSettings().setBuiltInZoomControls(true);

    }
}
