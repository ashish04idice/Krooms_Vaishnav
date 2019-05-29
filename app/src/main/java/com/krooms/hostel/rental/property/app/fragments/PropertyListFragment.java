package com.krooms.hostel.rental.property.app.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.PropertyListAdapter;
import com.krooms.hostel.rental.property.app.asynctask.GetHostelServiceAsyncTask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.AddsModel;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2/13/2016.
 */
public class PropertyListFragment extends Fragment implements ServiceResponce/*, YouTubePlayer.OnInitializedListener*/ {

    private FragmentActivity mActivity = null;
    ArrayList<AddsModel> videoaddarraylist;
    private View convertView = null;
    String topdata;

    Validation mValidation = null;
    public static ArrayList<Integer> propertyListIndex = new ArrayList<>();

    private ListView hostelList = null;
    //    private RecyclerAdapter mAdapter = null;
    private PropertyListAdapter mAdapter = null;
    private Boolean _isSearechInable = false;

    boolean isHavingAdsHeader = true;
    boolean isAdtypeVideoHeader = true;
    boolean isHavingAdsFooter = true;
    boolean isAdtypeVideoFooter = false;

    public static final String API_KEY = "AIzaSyC0BdnIc5Wu8P1NymGnmUkXdN99wjg95P8";

    //http://youtu.be/<VIDEO_ID>
    public static final String VIDEO_ID = "VbApAYADK6E";
    public static final String VIDEO_ID_FOOTER = "Hrah6NS_pRk";

    public String IMAGE_URL = "http://www.joomlaworks.net/images/demos/galleries/abstract/7.jpg";
    public String IMAGE_URL_FOOTER = "http://www.keenthemes.com/preview/conquer/assets/plugins/jcrop/demos/demo_files/image1.jpg";


    HashMap<String, ArrayList<PropertyModal>> propertyModal = new HashMap<String, ArrayList<PropertyModal>>();


    ArrayList<PropertyModal> hostelDetailList = new ArrayList<>();
    ArrayList<PropertyModal> pgDetailList = new ArrayList<>();
    ArrayList<PropertyModal> serviceAppDetailList = new ArrayList<>();
    ArrayList<PropertyModal> roomsDetailList = new ArrayList<>();
    ArrayList<PropertyModal> studioDetailList = new ArrayList<>();

//    private DisplayImageOptions options = null;

    Common mCommon = null;
    private Boolean _isScroll = true;
    private int page_count = 0;
    ArrayList<AddsModel> addsarraylist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        convertView = inflater.inflate(R.layout.property_list_fragment, container, false);
        createView();
        mValidation = new Validation(mActivity);

        if (!mValidation.checkNetworkRechability()) {

            mCommon.displayAlert(mActivity, mActivity.getResources().getString(R.string.str_no_network), false);

        } else {
            getPropertyList();
        }

       /* options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.user_xl)
                .showImageForEmptyUri(R.drawable.user_xl)
                .showImageOnFail(R.drawable.user_xl)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                        // .imageScaleType(ImageScaleType.NONE)
                .displayer(new RoundedBitmapDisplayer(100))
                .bitmapConfig(Bitmap.Config.RGB_565).build();*/

        return convertView;
    }


    public void createView() {
        Log.e("view", convertView + "");
        hostelList = (ListView) convertView.findViewById(R.id.hostelList);
//        mAdapter = new PropertyListAdapter(mActivity, propertyModal);
//        hostelList.setAdapter(mAdapter);

        hostelList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        mCommon = new Common();
    }


    @Override
    public void requestResponce(String result) {

        Common.Config("result    " + result);

        if (result.equals("ConnectTimeoutException")) {

            /*AlertDialogWithTwoCallback dialog = new AlertDialogWithTwoCallback() {
                @Override
                public void callBack() {
                    getPropertyList();
                }

                @Override
                public void cancelCallBack() {

                }
            };
            dialog.getPerameter(mActivity, "Connection Timeout", "Please check your interent connection and try again.");
            dialog.show(mActivity.getSupportFragmentManager(), "");*/


            Toast.makeText(mActivity, "Please check your interent connection and try again.", Toast.LENGTH_SHORT).show();


        } else if (result.trim().length() != 0) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {
                    if (page_count == 0) {
                        Common.CommonPropertList.clear();
                        propertyListIndex.clear();
                    }
                    JSONObject dataObject = jsonObject.getJSONObject(WebUrls.DATA_JSON);
                    if (dataObject.has("top_three")) {
                        JSONArray jArray = dataObject.getJSONArray("top_three");
                        for (int j = 0; j < jArray.length(); j++) {

                            PropertyModal modal = new PropertyModal(Parcel.obtain());
                            modal.setProperty_id(jArray.getJSONObject(j).getString("property_id"));
                            modal.setProperty_name(jArray.getJSONObject(j).getString("property_name"));
                            modal.setProperty_address(jArray.getJSONObject(j).getString("owner_address"));
                            modal.setProperty_area(jArray.getJSONObject(j).getString("colony"));
                            modal.setColonyname(jArray.getJSONObject(j).getString("colony_name"));
                            modal.setProperty_city(jArray.getJSONObject(j).getString("city"));
                            modal.setProperty_state(jArray.getJSONObject(j).getString("state"));
                            modal.setProperty_rent_price(jArray.getJSONObject(j).getString("rent_amount"));
                            modal.setNo_of_rooms(jArray.getJSONObject(j).getString("no_of_room"));
                            modal.setProperty_type_id(jArray.getJSONObject(j).getString("property_type"));
                            modal.setOwner_id(jArray.getJSONObject(j).getString("owner_id"));
                            modal.setUser_id(jArray.getJSONObject(j).getString("user_id"));

                            if (jArray.getJSONObject(j).has("property_images")) {
                                JSONArray propertyImageArray = jArray.getJSONObject(j).getJSONArray("property_images");
                                ArrayList<PropertyPhotoModal> images = new ArrayList<>();


                                if (propertyImageArray.length() != 0) {
                                    for (int i = 0; i < propertyImageArray.length(); i++) {
                                        PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                        photoModal.setPhoto_id(propertyImageArray.getJSONObject(i).getString("id"));
                                        photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(i).getString("path"));
                                        images.add(photoModal);
                                    }
                                }/*else{
                                    PropertyPhotoModal photoModal = new PropertyPhotoModal();
                                    photoModal.setPhoto_id("1");
                                    photoModal.setPhoto_Url(WebUrls.demo_image);

\

                                }*/

                                modal.setPropertyImage(images);

                            } else {
                                ArrayList<PropertyPhotoModal> images = new ArrayList<>();
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id("1");
                                photoModal.setPhoto_Url("Not Available");
                                images.add(photoModal);

                                modal.setPropertyImage(images);

                            }


                            double lat = 0.0;
                            double lng = 0.0;


                            if (jArray.getJSONObject(j).getString("lat").equals("") && jArray.getJSONObject(j).getString("long").equals("")) {
                                lat = 0.0;
                                lng = 0.0;
                            } else if (jArray.getJSONObject(j).getString("lat").equals("lat") && jArray.getJSONObject(j).getString("long").equals("long")) {
                                lat = 0.0;
                                lng = 0.0;
                            } else {
                                lat = Double.parseDouble(jArray.getJSONObject(j).getString("lat"));
                                lng = Double.parseDouble(jArray.getJSONObject(j).getString("long"));
                            }
                            modal.setProperty_latlng(new LatLng(lat, lng));


                            if (modal.getProperty_type_id().equals("1")) {
                                hostelDetailList.add(modal);
                            } else if (modal.getProperty_type_id().equals("2")) {
                                pgDetailList.add(modal);
                            } else if (modal.getProperty_type_id().equals("3")) {
                                serviceAppDetailList.add(modal);
                            } else if (modal.getProperty_type_id().equals("4")) {
                                roomsDetailList.add(modal);
                            } else {
                                studioDetailList.add(modal);
                            }
                            Common.CommonPropertList.add(modal);

                        }
                    }

                    if (dataObject.has("ads")) {
                        addsarraylist = new ArrayList<AddsModel>();
                        videoaddarraylist = new ArrayList<AddsModel>();
                        JSONArray adsArray = dataObject.getJSONArray("ads");
                        for (int i = 0; i < adsArray.length(); i++) {
                            AddsModel admodel = new AddsModel();
                            JSONObject jsonobjAds = adsArray.getJSONObject(i);
                            if (jsonobjAds.getString("media_type").equals("image")) {
                                admodel.setId(jsonobjAds.getString("id"));
                                admodel.setMediatype(jsonobjAds.getString("media_type"));
                                admodel.setUrl(jsonobjAds.getString("url"));
                                admodel.setTop_advertisement(jsonobjAds.getString("top_advertisement"));
                                admodel.setName(jsonobjAds.getString("name"));
                                addsarraylist.add(admodel);

                            } else {
                                admodel.setId(jsonobjAds.getString("id"));
                                admodel.setMediatype(jsonobjAds.getString("media_type"));
                                admodel.setUrl(jsonobjAds.getString("url"));
                                admodel.setTop_advertisement(jsonobjAds.getString("top_advertisement"));
                                admodel.setName(jsonobjAds.getString("name"));
                                videoaddarraylist.add(admodel);

                            }

                        }

                        //  {"status":"true","message":"Get Data Successfull","data":
                        // {"ads":[
                        // {"id":"1","name":"abc","media_type":"image","url":"uploads\/advertisement\/1483623548Jhinjhari.JPG","top_advertisement":"0","check":null,"status":"1"},
                        // {"id":"2","name":"xyz","media_type":"image","url":"uploads\/advertisement\/1483623586Bilhary.JPG","top_advertisement":"0","check":null,"status":"1"},{"id":"3","name":"New Advertisement","media_type":"image","url":"uploads\/advertisement\/1484288748Jhinjhari.JPG","top_advertisement":"0","check":null,"status":"1"},{"id":"4","name":"Advertisement","media_type":"image","url":"uploads\/advertisement\/1484288768Vijaraghavgarh.JPG","top_advertisement":"0","check":null,"status":"1"}]}}


                        // JSONArray adsArray = dataObject.getJSONArray("ads");
                        int lenghtvalue = adsArray.length() - 1;
                        for (int i = 0; i < adsArray.length(); i++) {

                            Common.Config("ad    " + adsArray.getJSONObject(i).getString("url"));

                            if (i == 0) {
                                isHavingAdsHeader = true;
                                topdata = adsArray.getJSONObject(0).get("media_type").toString();
                                if (adsArray.getJSONObject(i).get("media_type").equals("image")) {
                                    isAdtypeVideoHeader = false;
                                } else {
                                    isAdtypeVideoHeader = true;
                                }

                                LayoutInflater inflaterHeader = mActivity.getLayoutInflater();
                                if (isHavingAdsHeader) {
                                    ViewGroup header = (ViewGroup) inflaterHeader.inflate(R.layout.header, null, false);
                                    ArrayList images_arraylist = new ArrayList();
                                    ViewFlipper simpleViewFlipper = (ViewFlipper) header.findViewById(R.id.simpleViewFlipper); // get the reference of ViewFlipper
                                    ImageView adImage = (ImageView) header.findViewById(R.id.adminCreatedAdImage);
                                    RelativeLayout youtubesupportfragment_layout = (RelativeLayout) header.findViewById(R.id.youtubesupportfragment_layout);
                                    final YouTubePlayerSupportFragment fragment = (YouTubePlayerSupportFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.youtubesupportfragment);

                                    if (isAdtypeVideoHeader) {
                                        adImage.setVisibility(View.GONE);

                                        String videovalue = getYoutubeVideoId(videoaddarraylist.get(0).getUrl().toString());
                                        String videoUrl = getYoutubeVideoId(adsArray.getJSONObject(i).getString("url"));
                                        String subString = videovalue.substring(videovalue.lastIndexOf("/") + 1);
                                        String videoKey = "";

                                        if (subString.contains("watch?v=")) {
                                            videoKey = subString.substring(subString.lastIndexOf("watch?v=") + 1);
                                        } else {
                                            videoKey = "" + subString;

                                        }
                                        final String finalVideoKey = videoKey;
                                        mActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                PlayerInitializer listner = new PlayerInitializer(finalVideoKey);
                                                fragment.initialize(API_KEY, listner);
                                            }
                                        });
                                        youtubesupportfragment_layout.setVisibility(View.VISIBLE);
                                    } else {
                                        adImage.setVisibility(View.GONE);
                                       /* Common.callImageLoaderSmall(mActivity, WebUrls.IMG_URL + "" + adsArray.getJSONObject(i).getString("url"), adImage, options);*/
                                        adImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                        String imagePath = adsArray.getJSONObject(i).getString("url").replace(" ", "%20");
                                        images_arraylist.add(imagePath);
                                        Common.loadImage(mActivity, WebUrls.AD_IMG_URL + "" + imagePath, adImage);
                                        /*Picasso.with(mActivity)
                                                .load(WebUrls.AD_IMG_URL + "" + adsArray.getJSONObject(i).getString("url"))
                                                .placeholder(R.drawable.user_xl)
                                                .error(R.drawable.user_xl)
                                                .transform(new RoundedTransformation(0, 0))
                                                .into(adImage);*/

                                        // loop for creating ImageView's
                                        for (int j = 0; j < addsarraylist.size(); j++) {
                                            // create the object of ImageView
                                            ImageView imageView = new ImageView(mActivity);
                                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                            imageView.setLayoutParams(new ViewFlipper.LayoutParams(ViewFlipper.LayoutParams.FILL_PARENT, ViewFlipper.LayoutParams.FILL_PARENT));

                                            String imagelink = addsarraylist.get(j).getUrl();
                                            Common.loadImage(mActivity, WebUrls.AD_IMG_URL + "" + imagelink, imageView);
                                            //  imageView.setImageResource(Integer.parseInt(imagelink)); // set image in ImageView
                                            simpleViewFlipper.addView(imageView); // add the created ImageView in ViewFlipper
                                        }
                                        // Declare in and out animations and load them using AnimationUtils class
                                        Animation in = AnimationUtils.loadAnimation(mActivity, android.R.anim.slide_in_left);
                                        Animation out = AnimationUtils.loadAnimation(mActivity, android.R.anim.slide_out_right);
                                        // set the animation type's to ViewFlipper
                                        simpleViewFlipper.setInAnimation(in);
                                        simpleViewFlipper.setOutAnimation(out);
                                        // set interval time for flipping between views
                                        simpleViewFlipper.setFlipInterval(3000);
                                        // set auto start for flipping between views
                                        simpleViewFlipper.setAutoStart(true);

                                        youtubesupportfragment_layout.setVisibility(View.GONE);
                                    }
                                    hostelList.addHeaderView(header);
                                }

                            } else if (i == lenghtvalue) {
                                isHavingAdsFooter = true;
                                if (adsArray.getJSONObject(i).get("media_type").equals("image")) {
                                    isAdtypeVideoFooter = false;
                                } else {
                                    isAdtypeVideoFooter = true;
                                }

                                if (!topdata.equals(adsArray.getJSONObject(i).get("media_type"))) {
                                    if (isHavingAdsFooter) {
                                        LayoutInflater inflaterHeader = mActivity.getLayoutInflater();
                                        ViewGroup footer = (ViewGroup) inflaterHeader.inflate(R.layout.footer, null, false);
                                        ViewFlipper simpleViewFlipper = (ViewFlipper) footer.findViewById(R.id.simpleViewFlipperfooter); // get the reference of ViewFlipper
                                        ImageView adImageFooter = (ImageView) footer.findViewById(R.id.adminCreatedAdImage_footer);
                                        adImageFooter.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        RelativeLayout youtubesupportfragment_layoutfooter = (RelativeLayout) footer.findViewById(R.id.youtubesupportfragment_layout_footer);
                                        final YouTubePlayerSupportFragment fragmentfooter = (YouTubePlayerSupportFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.youtubesupportfragment_footer);

                                        if (isAdtypeVideoFooter) {
                                            adImageFooter.setVisibility(View.GONE);
                                            String videovalue = getYoutubeVideoId(videoaddarraylist.get(0).getUrl().toString());
                                            String videoUrl = getYoutubeVideoId(adsArray.getJSONObject(i).getString("url"));
                                            String subString = videovalue.substring(videovalue.lastIndexOf("/") + 1);
                                            String videoKey = "";

                                            if (subString.contains("watch?v=")) {
                                                videoKey = subString.substring(subString.lastIndexOf("watch?v=") + 1);
                                            } else {
                                                videoKey = "" + subString;
                                            }
                                            final String finalVideoKey = videoKey;
                                            mActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Log.d("UI thread", "I am the UI thread");
                                                    PlayerInitializer listner1 = new PlayerInitializer(finalVideoKey);
                                                    fragmentfooter.initialize(API_KEY, listner1);
                                                }
                                            });
                                            youtubesupportfragment_layoutfooter.setVisibility(View.VISIBLE);
                                        } else {
                                            youtubesupportfragment_layoutfooter.setVisibility(View.GONE);
                                            adImageFooter.setVisibility(View.GONE);
//                                        Common.callImageLoaderSmall(mActivity, WebUrls.IMG_URL + "" + adsArray.getJSONObject(i).getString("url"), adImageFooter, options);

                                            String imagePath = adsArray.getJSONObject(i).getString("url").replace(" ", "%20");

                                            Common.loadImage(mActivity, WebUrls.AD_IMG_URL + "" + imagePath, adImageFooter);
                                        /*Picasso.with(mActivity)
                                                .load()
                                                .placeholder(R.drawable.user_xl)
                                                .error(R.drawable.user_xl)
                                                .transform(new RoundedTransformation(0, 0))
                                                .into(adImageFooter);*/


                                            // loop for creating ImageView's
                                            for (int j = 0; j < addsarraylist.size(); j++) {
                                                // create the object of ImageView

                                                ImageView imageView = new ImageView(mActivity);
                                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                                imageView.setLayoutParams(new ViewFlipper.LayoutParams(ViewFlipper.LayoutParams.FILL_PARENT, ViewFlipper.LayoutParams.FILL_PARENT));

                                                String imagelink = addsarraylist.get(j).getUrl();
                                                Common.loadImage(mActivity, WebUrls.AD_IMG_URL + "" + imagelink, imageView);
                                                //  imageView.setImageResource(Integer.parseInt(imagelink)); // set image in ImageView
                                                simpleViewFlipper.addView(imageView); // add the created ImageView in ViewFlipper
                                            }
                                            // Declare in and out animations and load them using AnimationUtils class
                                            Animation in = AnimationUtils.loadAnimation(mActivity, android.R.anim.slide_in_left);
                                            Animation out = AnimationUtils.loadAnimation(mActivity, android.R.anim.slide_out_right);
                                            // set the animation type's to ViewFlipper
                                            simpleViewFlipper.setInAnimation(in);
                                            simpleViewFlipper.setOutAnimation(out);
                                            // set interval time for flipping between views
                                            simpleViewFlipper.setFlipInterval(3000);
                                            // set auto start for flipping between views
                                            simpleViewFlipper.setAutoStart(true);

                                        }
                                        hostelList.addFooterView(footer);
                                    }
                                }

                            }

                        }
                    } else {


                        Common.Config(" no ads availeble    ");

                    }

                    if (hostelDetailList.size() != 0) {
                        propertyListIndex.add(0);
                        propertyModal.put("Hostels", hostelDetailList);
                    }
                    if (pgDetailList.size() != 0) {
                        propertyListIndex.add(1);
                        propertyModal.put("PGs", pgDetailList);

                    }
                    if (serviceAppDetailList.size() != 0) {
                        propertyListIndex.add(2);
                        propertyModal.put("Service Apartments", serviceAppDetailList);
                    }
                    if (roomsDetailList.size() != 0) {
                        propertyListIndex.add(3);
                        propertyModal.put("Rooms", roomsDetailList);
                    }
                    if (studioDetailList.size() != 0) {
                        propertyListIndex.add(4);
                        propertyModal.put("Studio Apartments", studioDetailList);
                    }
                    if (mAdapter == null) {
                        mAdapter = new PropertyListAdapter(mActivity, propertyModal);
                        hostelList.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (Common.CommonPropertList.size() == 0) {
                        mCommon.displayAlert(mActivity, "" +
                                jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                    } else {
                        mCommon.displayAlert(mActivity, "" + this.getResources()
                                .getString(R.string.str_no_more_data), false);
                    }
                }
            } catch (JSONException e) {
            }
        }
    }

    public void getPropertyList() {

        _isSearechInable = false;
        GetHostelServiceAsyncTask serviceAsyncTask = new GetHostelServiceAsyncTask(mActivity);
        serviceAsyncTask.setCallBack(this);
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", "" + 10, "" + page_count);

    }


   /* @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        */

    /**
     * add listeners to YouTubePlayer instance
     **//*

    }*/



    public class PlayerInitializer implements YouTubePlayer.OnInitializedListener {

        public String video_id;

        public PlayerInitializer(String videoId) {
            Common.Config("vidoe key  .....   " + videoId);
            this.video_id = videoId;
        }


        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
            youTubePlayer.setPlaybackEventListener(playbackEventListener);

            /** Start buffering **/
            if (!b) {
                youTubePlayer.cueVideo(this.video_id);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            //Toast.makeText(mActivity, "Failured to Initialize!", Toast.LENGTH_LONG).show();
        }

        private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

            @Override
            public void onBuffering(boolean arg0) {
            }

            @Override
            public void onPaused() {
            }

            @Override
            public void onPlaying() {
            }

            @Override
            public void onSeekTo(int arg0) {
            }

            @Override
            public void onStopped() {
            }

        };

        private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

            @Override
            public void onAdStarted() {
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason arg0) {
            }

            @Override
            public void onLoaded(String arg0) {
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onVideoEnded() {
            }

            @Override
            public void onVideoStarted() {
            }
        };
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {

            YouTubePlayerSupportFragment fragmentheader = (YouTubePlayerSupportFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.youtubesupportfragment);
            if (fragmentheader != null)
                getFragmentManager().beginTransaction().remove(fragmentheader).commit();

            YouTubePlayerSupportFragment fragmentfooter = (YouTubePlayerSupportFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.youtubesupportfragment_footer);
            if (fragmentfooter != null)
                getFragmentManager().beginTransaction().remove(fragmentfooter).commit();
        } catch (Exception e) {
        }
    }

    public String getYoutubeVideoId(String youtubeUrl) {

        return youtubeUrl.substring((youtubeUrl.lastIndexOf("=") + 1), youtubeUrl.length());

    }

}
