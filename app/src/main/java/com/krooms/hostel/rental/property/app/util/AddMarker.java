package com.krooms.hostel.rental.property.app.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.krooms.hostel.rental.property.app.R;


public class AddMarker {

	public Marker marker = null;
	public String facebookID = "";
    public String nameTag = "";
	public int mIcon;
//  public static TextView markerTag;
	ImageView markerIcon;

	View markerView;
	Context context;



	Bitmap markerBeforeBitmap = null;
	Bitmap markerViewBitmap = null;
	Bitmap roundedProfilePicBitmap = null;

    public AddMarker(final Marker marker, int icon /*String nameTag*/, final Context context){
        this.marker = marker;
        /*this.nameTag = nameTag;*/
		this.mIcon = icon;
        this.context = context;

        markerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_marker, null);
		markerIcon  = (ImageView) markerView.findViewById(R.id.marker_icon);
		markerIcon.setImageResource(mIcon);
       /* markerTag = (TextView) markerView.findViewById(R.id.nameLabelTag);
        markerTag.setText(nameTag);

        if(!nameTag.equals("")) {


            markerTag.setVisibility(View.VISIBLE);
        }else{


            markerTag.setVisibility(View.GONE);
        }*/

        marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, markerView)));
    }

//	public Bitmap getNewCustomMarker(){
//		IconGenerator iconFactory = new IconGenerator(context);
//		iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
//		/*options.icon(BitmapDescriptorFactory.fromBitmap(*/
//		Bitmap mBitmap = iconFactory.makeIcon("hello");
////		options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
//
//
//
//		return mBitmap;
//	}


    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

	public Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}

	public void recycleBitmaps() {
		if (markerBeforeBitmap != null) {
			markerBeforeBitmap.recycle();
			markerBeforeBitmap = null;
		}

		if (markerViewBitmap != null) {
			markerViewBitmap.recycle();
			markerViewBitmap = null;
		}

		if (roundedProfilePicBitmap != null) {
			roundedProfilePicBitmap.recycle();
			roundedProfilePicBitmap = null;

		}
	}

}
