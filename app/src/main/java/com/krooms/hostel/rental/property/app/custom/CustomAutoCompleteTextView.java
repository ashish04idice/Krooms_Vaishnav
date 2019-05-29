package com.krooms.hostel.rental.property.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.krooms.hostel.rental.property.app.R;


/**
 * Created by user on 1/4/2016.
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {

    Context context;

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public CustomAutoCompleteTextView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

