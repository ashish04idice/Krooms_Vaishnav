package com.krooms.hostel.rental.property.app.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.krooms.hostel.rental.property.app.R;

/**
 * Created by user on 2/27/2016.
 */
public abstract class SortKeyWordSelectionDialog extends DialogFragment {

    private FragmentActivity mFActivity = null;

    private RadioGroup keywordSelection = null;
    private RadioButton name, budget;

    private Button goBtn = null;

    public SortKeyWordSelectionDialog() {

    }

    public void getPerameter(FragmentActivity activity) {
        this.mFActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sort_keyword_selection, container);
        createView(view);
        return view;

    }

    String keyWord;
    boolean bool = false;

    public void createView(View v) {

        keywordSelection = (RadioGroup) v.findViewById(R.id.selectSortOption);

        name = (RadioButton) v.findViewById(R.id.sortName);
        budget = (RadioButton) v.findViewById(R.id.sortBudget);
        goBtn = (Button) v.findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(name.isChecked()){

                    keyWord = "property_name";

                }else if(budget.isChecked()){

                    keyWord = "rent_amount";

                }*/
                keyWord = name.isChecked() ? "property_name" : budget.isChecked() ? "rent_amount" : "";

               /* Common.Config("  keyword  "+bool);
                Common.Config("  name  "+keyWord);
                Common.Config("  budget  "+budget.isChecked());*/
                goBtnEvent(keyWord);
                dismiss();
            }
        });


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
        super.onDismiss(dialog);
    }

    public abstract void goBtnEvent(String str);

}
