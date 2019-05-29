package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.fragments.SearchPropertyListMapFragment;
import com.krooms.hostel.rental.property.app.fragments.SearchPropertyListFragment;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.dialog.SearchFilterDialog;
import com.krooms.hostel.rental.property.app.dialog.SortKeyWordSelectionDialog;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;

import java.util.ArrayList;

public class SearchedPropertyListActivity extends FragmentActivity implements View.OnClickListener {

    public static ArrayList<PropertyModal> list = new ArrayList<>();
    private Common mCommon = null;
    private TextView mapText;
    private RelativeLayout filterBtnLayout, sortBtnLayout, mapBtnLayout;
    public static Activity searchPropertyActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_property_list);
        searchPropertyActivity = this;
        if (list != null) {
            list.clear();
        }
        mCommon = new Common();
        filterBtnLayout = (RelativeLayout) findViewById(R.id.filterBtnLayout);
        sortBtnLayout = (RelativeLayout) findViewById(R.id.sortBtnLayout);
        mapBtnLayout = (RelativeLayout) findViewById(R.id.mapBtnLayout);
        mapText = (TextView) findViewById(R.id.mapText);
        filterBtnLayout.setOnClickListener(this);
        sortBtnLayout.setOnClickListener(this);
        mapBtnLayout.setOnClickListener(this);

        TextView txtBack = (TextView) findViewById(R.id.textView_title);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchedPropertyListActivity.this.finish();
                SearchedPropertyListActivity.this.overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
            }
        });

        displayView(1, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.filterBtnLayout:
                SearchFilterDialog filterDialog = new SearchFilterDialog();
                filterDialog.getPerameter(this);
                filterDialog.show(this.getSupportFragmentManager(), "searchDialog");
                break;
            case R.id.sortBtnLayout:
               /* SortKeyWordSelectionDialog keyWordDialog = new SortKeyWordSelectionDialog() {
                    @Override
                    public void goBtnEvent(String str) {

                        displayView(1, str);

                    }
                };
                keyWordDialog.getPerameter(this);
                keyWordDialog.show(this.getSupportFragmentManager(), "sortKeyword");
*/

                CustomDialogClassBack cdd = new CustomDialogClassBack(SearchedPropertyListActivity.this, R.style.full_screen_dialog);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.getPerameter(this);
                cdd.show();

                break;
            case R.id.mapBtnLayout:

                if (mapText.getText().equals("Map")) {
                    mapText.setText("List");
                    displayView(0, "");
                } else {
                    mapText.setText("Map");
                    displayView(1, "");
                }

                break;
        }
    }



    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClassBack extends Dialog {

        private FragmentActivity mFActivity = null;

        private RadioGroup keywordSelection = null;
        private RadioButton name, budget;
        private Button goBtn = null;
        String keyWord;

        public CustomDialogClassBack(SearchedPropertyListActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }


        public void getPerameter(FragmentActivity activity) {
            this.mFActivity = activity;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.sort_keyword_selection);
            keywordSelection = (RadioGroup) findViewById(R.id.selectSortOption);

            name = (RadioButton) findViewById(R.id.sortName);
            budget = (RadioButton) findViewById(R.id.sortBudget);
            goBtn = (Button) findViewById(R.id.goBtn);
            goBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keyWord  = name.isChecked()?"property_name":budget.isChecked()?"rent_amount":"";
                    displayView(1,keyWord);
                    dismiss();
                }
            });
        }
    }

    ///nnnn


    private void displayView(int position, String sortKeyWord) {

        // update the main content by replacing fragments
        Fragment mFragment = null;


        switch (position) {
            case 0:
                SearchPropertyListMapFragment mFragment1 = new SearchPropertyListMapFragment();
                mFragment1.getPerameter(list);
                mFragment = mFragment1;
                break;
            case 1:
                SearchPropertyListFragment mFragment2 = new SearchPropertyListFragment();
                mFragment2.getPerameter(sortKeyWord);
                mFragment = mFragment2;
                break;
            default:
                SearchPropertyListFragment mFragment3 = new SearchPropertyListFragment();
                mFragment3.getPerameter(sortKeyWord);
                mFragment = mFragment3;
                break;
        }

        if (null != mFragment) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                clearBackStack(fragmentManager);
            }
            fragmentManager.beginTransaction().replace(R.id.hostelList, mFragment).commit();
        }

    }

    public void clearBackStack(FragmentManager manager) {
        int rootFragment = manager.getBackStackEntryAt(0).getId();
        manager.popBackStack(rootFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        if(mapText.getText().equals("List")){
            mapText.setText("Map");
            displayView(1,"");
        }else{
            finish();

        }
    }

}