package com.krooms.hostel.rental.property.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.R;


/**
 * Created by user on 2/10/2016.
 */
public abstract class ImagePreviewDialog extends DialogFragment {

    private FragmentActivity mFActivity = null;
    private TextView title;
    private ImageView imageView;
    private String titleStr;
    private TextView message;
    private Button downloadImageBtn;
    private Button alertNoBtn;
    private Boolean downloadBtnHide;

    public ImagePreviewDialog() {
    }

    public void getPerameter(FragmentActivity activity, String titleStr, Boolean flag) {
        this.mFActivity = activity;
        this.titleStr = titleStr;
        this.downloadBtnHide = flag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        View view = inflater.inflate(R.layout.list_group, container);

        title = (TextView) view.findViewById(R.id.dailogTitiel);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        downloadImageBtn = (Button) view.findViewById(R.id.downloadImageBtn);

        try {
            showImage(imageView);
        }catch (Exception e){

        }

        title.setText(titleStr);

        if (downloadBtnHide) {
            downloadImageBtn.setVisibility(View.GONE);
        } else {
            downloadImageBtn.setVisibility(View.VISIBLE);
        }

        downloadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage();
            }
        });

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
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                //do your stuff
                LogConfig.logd("BBBBBBBBBBBBBB onCreateDialog ", "onBackPressed ");
                ImagePreviewDialog.this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                dismiss();
            }
        };
    }

    public abstract void showImage(ImageView img);

    public abstract void downloadImage();

}
