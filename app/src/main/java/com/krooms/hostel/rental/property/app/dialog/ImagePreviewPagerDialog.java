package com.krooms.hostel.rental.property.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krooms.hostel.rental.property.app.adapter.PreViewImagePagerAdapter;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.indicator.CirclePageIndicator;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.R;

import java.util.ArrayList;

/**
 * Created by user on 3/25/2016.
 */
public class ImagePreviewPagerDialog extends DialogFragment {

    private FragmentActivity mFActivity = null;
    ArrayList<PropertyPhotoModal> modalList = new ArrayList<>();
    int index = 0;

    public ImagePreviewPagerDialog(){

    }

    public void getPerameter(FragmentActivity activity,ArrayList<PropertyPhotoModal> modal, int position) {
        this.mFActivity = activity;
        this.modalList.addAll(modal);
        this.index = position;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        View view = inflater.inflate(R.layout.image_preview_pager_dialog, container);

        ViewPager previewViewPager =(ViewPager) view.findViewById(R.id.previewViewPager);
        previewViewPager.setAdapter(new PreViewImagePagerAdapter(mFActivity, modalList));
        previewViewPager.setCurrentItem(index);
        CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.previewPagerIndicator);
        indicator.setViewPager(previewViewPager);


        return view;
    }


    private static boolean permissionDialogShown = false;

    @Override
    public void show(FragmentManager manager, String tag) {
        if (permissionDialogShown) {
            return;
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        super.show(manager, tag);
        permissionDialogShown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        permissionDialogShown = false;
        this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onDismiss(dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //do your stuff
                LogConfig.logd("BBBBBBBBBBBBBB onCreateDialog ", "onBackPressed ");
                ImagePreviewPagerDialog.this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                dismiss();
            }
        };
    }



}
