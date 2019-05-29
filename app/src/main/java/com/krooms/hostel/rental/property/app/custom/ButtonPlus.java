package com.krooms.hostel.rental.property.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.krooms.hostel.rental.property.app.R;


/**
 * Created by user on 1/4/2016.
 */
public class ButtonPlus extends Button {

    public ButtonPlus(Context context) {
        super(context);
        init(null);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void init(AttributeSet attr) {

        if (attr != null) {
            TypedArray a = getContext().obtainStyledAttributes(attr, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_TextSize);

            Typeface face = Typeface.createFromAsset(getContext().getAssets(),"fonts/roboto_light.ttf");
            this.setTypeface(face);
            a.recycle();
        }
    }

}