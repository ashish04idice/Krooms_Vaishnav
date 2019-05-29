package com.krooms.hostel.rental.property.app.Circular;


import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.krooms.hostel.rental.property.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;



public class UniversalImageLoader {
	static DisplayImageOptions options;

	 protected static ImageLoader imageLoader ;


	 public static void initUniversalImageLoaderOptions(){
		 
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.user_xl)
				.showImageOnFail(R.drawable.user_xl)
				.showImageOnLoading(R.drawable.user_xl)
		      	.cacheInMemory(true)
				.cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(5))
				.build();
		
		  }
	 public static void initUniversalImageLoaderOptionsForRoundImage(){
		 
			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.user_xl)
					.showImageOnFail(R.drawable.user_xl)
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.showImageOnLoading(R.drawable.user_xl)
					.displayer(new RoundedBitmapDisplayer(100))
					.build();
			  }

	 public static void loadImageFromURI(ImageView img, final String url, final ProgressBar spinner)
	   {
		
		// img.setBackgroundColor(Color.TRANSPARENT);
		 //img.setImageResource(R.drawable.empty_photo);
		ImageLoader.getInstance().displayImage(url, img,options, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				if(spinner!=null){
					 spinner.setVisibility(View.VISIBLE);
					}
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				if(spinner!=null){
					 spinner.setVisibility(View.GONE);
					}
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				if(spinner!=null){
					 spinner.setVisibility(View.GONE);
					}
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				if(spinner!=null){
					 spinner.setVisibility(View.GONE);
					}
			}
		});
	}
	 
	 public static Bitmap loadImageInView(String url){
		 Bitmap bm = ImageLoader.getInstance().loadImageSync(url);
		 return bm;
	 }
}
