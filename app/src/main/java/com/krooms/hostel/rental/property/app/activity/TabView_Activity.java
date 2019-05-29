package com.krooms.hostel.rental.property.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.fragments.LastIn;
import com.krooms.hostel.rental.property.app.fragments.LastOut;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ashish on 3/4/17.
 */

public class TabView_Activity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar;
    RelativeLayout flback_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_tab_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tenant_Records");

        viewPager = (ViewPager) findViewById(R.id.pager1);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

       // tabLayout.setTabMode(TabLayout.MODE_FIXED);
       // tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       // flback_button=(RelativeLayout)toolbar.findViewById(R.id.flback_button_tool);

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

       /* flback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii=new Intent(TabView_Activity.this,Tenant_Details_Activity.class);
                startActivity(ii);
            }
        });*/
    }

    private void setupViewPager(ViewPager viewPager) {

        Toast.makeText(TabView_Activity.this, "Pager", Toast.LENGTH_SHORT).show();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
         adapter.addFrag(new LastIn(),"Last In");
          adapter.addFrag(new LastOut(),"Last Out");
            viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
