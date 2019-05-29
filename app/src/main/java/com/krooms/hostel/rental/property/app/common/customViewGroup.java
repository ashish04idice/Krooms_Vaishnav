package com.krooms.hostel.rental.property.app.common;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by admin on 2/21/2018.
 */

public class customViewGroup extends ViewGroup {
    public customViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.v("customViewGroup", "**********Intercepted");
        return true;
    }
}