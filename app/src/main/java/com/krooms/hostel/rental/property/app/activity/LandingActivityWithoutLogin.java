package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.InformationPagerAdapter;

public class LandingActivityWithoutLogin extends AppCompatActivity {

    private ViewPager pager;
    private Button signUpBtn;
    private Button signInBtn;
    private TextView skipBtn;
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_landing_activity_without_login);
        initView();
    }

    public void initView() {

        pager = (ViewPager) findViewById(R.id.informationPager);
        signInBtn = (Button) findViewById(R.id.signInBtn);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        skipBtn = (TextView) findViewById(R.id.skipBtn);

        pager.setAdapter(new InformationPagerAdapter(this));

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LandingActivityWithoutLogin.this, LoginActivity.class);
                startActivity(it);

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LandingActivityWithoutLogin.this, RegistrationActivity.class);
                startActivity(it);

            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LandingActivityWithoutLogin.this, HostelListActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(LandingActivityWithoutLogin.this);
        this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
    }
}
