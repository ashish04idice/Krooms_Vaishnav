package com.krooms.hostel.rental.property.app.fragments;

import android.app.ProgressDialog;
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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.PropertyListCategoryWiseActivity;
import com.krooms.hostel.rental.property.app.adapter.HolstelListAdapter;
import com.krooms.hostel.rental.property.app.asynctask.ViewMorePropertyList;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anuj S on 2/28/2016.
 */
public class PropertyListCategoryWiseFragment extends Fragment implements ServiceResponce {

    private FragmentActivity mActivity = null;

    public static ArrayList<PropertyModal> list;
    public ArrayList<PropertyModal> list_new_main;
    private View convertView = null;
    Validation mValidation = null;
    private ListView hostelList = null;
    private HolstelListAdapter mAdapter = null;
    private Boolean _isSearechInable = false;
    private Common mCommon = null;
    private Boolean _isScroll = true;
    private int page_count = 0;
    private String sortKeyWord;
    public ListView propertyList;
    public PropertyListCategoryWiseFragment() {

    }

    public void getPerameter(String sort_type) {
        this.sortKeyWord = sort_type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        convertView = inflater.inflate(R.layout.property_list_fragment_new, container, false);
        mCommon = new Common();
        createView();
        mValidation = new Validation(mActivity);

        if (sortKeyWord.equals("")) {
            getPropertyList();
        } else {
            getSortedPropertyList();
        }

        return convertView;

    }

    public void createView() {

        propertyList = (ListView) convertView.findViewById(R.id.otherPropertyList);

       /* propertyList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

               if (_isScroll && view.getLastVisiblePosition() >= PropertyListCategoryWiseActivity.list.size() - 1) {
                    _isScroll = false;
                    page_count = 1 + page_count;
                   if (sortKeyWord.equals("")) {
                        getPropertyList();
                    } else {
                        getSortedPropertyList();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/
    }

    @Override
    public void requestResponce(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("flag");

            if (status.equalsIgnoreCase("Y")) {
                if (page_count == 0) {
                    list.clear();
                }


                JSONArray jArray = jsonObject.getJSONArray("data");
                String jArrayvacantvaluesizedata = String.valueOf(jArray.length());
                // if(!jArrayvacantvaluesizedata.equals("0")) {
                for (int j = 0; j < jArray.length(); j++) {
                    PropertyModal modal = new PropertyModal(Parcel.obtain());
                    modal.setProperty_id(jArray.getJSONObject(j).getString("property_id"));
                    String propertyvalue = jArray.getJSONObject(j).getString("property_id");
                    modal.setProperty_name(jArray.getJSONObject(j).getString("property_name"));
                    modal.setProperty_address(jArray.getJSONObject(j).getString("owner_address"));
                    modal.setProperty_area(jArray.getJSONObject(j).getString("colony"));
                    modal.setColonyname(jArray.getJSONObject(j).getString("colony_name"));
                    modal.setProperty_city(jArray.getJSONObject(j).getString("city"));
                    modal.setProperty_state(jArray.getJSONObject(j).getString("state"));
                    modal.setProperty_rent_price(jArray.getJSONObject(j).getString("rent_amount"));
                    /*modal.setCapacity_hosteler(jArray.getJSONObject(j).getString("capacity_hosteler"));
                    modal.setVacancy_hosteler(jArray.getJSONObject(j).getString("vacancy_hosteler"));*/
                    modal.setNo_of_rooms(jArray.getJSONObject(j).getString("no_of_room"));
                    //modal.setProperty_phone(jArray.getJSONObject(j).getString("property_phone"));
                    modal.setProperty_type_id(jArray.getJSONObject(j).getString("property_type"));
                    modal.setOwner_id(jArray.getJSONObject(j).getString("owner_id"));
                    modal.setUser_id(jArray.getJSONObject(j).getString("user_id"));

                    if (jArray.getJSONObject(j).has("property_images")) {
                        JSONArray propertyImageArray = jArray.getJSONObject(j).getJSONArray("property_images");
                        ArrayList<PropertyPhotoModal> images = new ArrayList<>();

                        if (propertyImageArray.length() != 0) {
                            for (int z = 0; z < propertyImageArray.length(); z++) {
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id(propertyImageArray.getJSONObject(z).getString("id"));
                                photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(z).getString("path"));
                                images.add(photoModal);
                            }
                        } else {
                            PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                            photoModal.setPhoto_id("1");
                            photoModal.setPhoto_Url("Not Available");
                            images.add(photoModal);
                        }
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
                    list.add(modal);
                    // mAdapter.notifyDataSetChanged();
                }
                //  }

                JSONArray jArrayvacantroom = jsonObject.getJSONArray("maxvacant");
                //for vacant room
                String jArrayvacantvaluesize = String.valueOf(jArrayvacantroom.length());
                //  if(!jArrayvacantvaluesize.equals("0")) {
                for (int i = 0; i < jArrayvacantroom.length(); i++) {
                    PropertyModal modal = new PropertyModal(Parcel.obtain());
                    modal.setProperty_id(jArrayvacantroom.getJSONObject(i).getString("property_id"));
                    String propertyvalue1 = jArrayvacantroom.getJSONObject(i).getString("property_id");
                    modal.setProperty_name(jArrayvacantroom.getJSONObject(i).getString("property_name"));
                    modal.setProperty_address(jArrayvacantroom.getJSONObject(i).getString("owner_address"));
                    modal.setProperty_area(jArrayvacantroom.getJSONObject(i).getString("colony"));
                    modal.setColonyname(jArrayvacantroom.getJSONObject(i).getString("colony_name"));
                    modal.setProperty_city(jArrayvacantroom.getJSONObject(i).getString("city"));
                    modal.setProperty_state(jArrayvacantroom.getJSONObject(i).getString("state"));
                    modal.setProperty_rent_price(jArrayvacantroom.getJSONObject(i).getString("rent_amount"));
                    modal.setNo_of_rooms(jArrayvacantroom.getJSONObject(i).getString("no_of_room"));
                    modal.setProperty_type_id(jArrayvacantroom.getJSONObject(i).getString("property_type"));
                    modal.setOwner_id(jArrayvacantroom.getJSONObject(i).getString("owner_id"));
                    modal.setUser_id(jArrayvacantroom.getJSONObject(i).getString("user_id"));

                    if (jArrayvacantroom.getJSONObject(i).has("property_images")) {
                        JSONArray propertyImageArray = jArrayvacantroom.getJSONObject(i).getJSONArray("property_images");
                        ArrayList<PropertyPhotoModal> images = new ArrayList<>();

                        if (propertyImageArray.length() != 0) {
                            for (int k = 0; i < propertyImageArray.length(); k++) {
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id(propertyImageArray.getJSONObject(k).getString("id"));
                                photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(k).getString("path"));
                                images.add(photoModal);
                            }
                        } else {
                            PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                            photoModal.setPhoto_id("1");
                            photoModal.setPhoto_Url("Not Available");
                            images.add(photoModal);
                        }
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

                    if (jArrayvacantroom.getJSONObject(i).getString("lat").equals("") && jArrayvacantroom.getJSONObject(i).getString("long").equals("")) {
                        lat = 0.0;
                        lng = 0.0;
                    } else if (jArrayvacantroom.getJSONObject(i).getString("lat").equals("lat") && jArrayvacantroom.getJSONObject(i).getString("long").equals("long")) {
                        lat = 0.0;
                        lng = 0.0;
                    } else {
                        lat = Double.parseDouble(jArrayvacantroom.getJSONObject(i).getString("lat"));
                        lng = Double.parseDouble(jArrayvacantroom.getJSONObject(i).getString("long"));
                    }
                    modal.setProperty_latlng(new LatLng(lat, lng));
                    list.add(modal);
                    //  mAdapter.notifyDataSetChanged();
                }

                String listsize = String.valueOf(list.size());
                if (!listsize.equals("0")) {
                    mAdapter = new HolstelListAdapter(mActivity, list);
                    propertyList.setAdapter(mAdapter);
                } else {
                    Toast.makeText(getActivity(), "data is not available", Toast.LENGTH_SHORT).show();
                }


            } else {
                if (list.size() == 0) {
                    mCommon.displayAlert(mActivity, "" + jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                } else {
                    mCommon.displayAlert(mActivity, "" + this.getResources()
                            .getString(R.string.str_no_more_data), false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getPropertyList() {

        String type = mActivity.getIntent().getExtras().getString("property_type");
        String searchedKeyword = mActivity.getIntent().getExtras().getString("keyword");

        new searchpropertydataJson(searchedKeyword, type).execute();
       /* ViewMorePropertyList serviceAsyncTask = new ViewMorePropertyList(mActivity);
        serviceAsyncTask.setCallBack(this);
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchedKeyword, type, "" + 10, "" + page_count, "");
*/
    }

    public void getSortedPropertyList() {

        String type = mActivity.getIntent().getExtras().getString("property_type");
        String searchedKeyword = mActivity.getIntent().getExtras().getString("keyword");

        //for sorting data
        new SortdataJson(searchedKeyword, type, sortKeyWord).execute();
      /*  ViewMorePropertyList serviceAsyncTask = new ViewMorePropertyList(mActivity);
        serviceAsyncTask.setCallBack(this);
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchedKeyword, type, "" + 10, "" + page_count, sortKeyWord);
*/
    }


    private class SortdataJson extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";
        private ProgressDialog pDialog = null;
        String searchedKeyword, type, sortKeyWord;

        public SortdataJson(String searchedKeyword, String type, String sortKeyWord) {
            this.searchedKeyword = searchedKeyword;
            this.type = type;
            this.sortKeyWord = sortKeyWord;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mActivity);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            list = new ArrayList<>();

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "viewMoreSearchProperty"));
                nameValuePairs.add(new BasicNameValuePair("search_keyword", searchedKeyword));
                nameValuePairs.add(new BasicNameValuePair("property_type_id", type));
                nameValuePairs.add(new BasicNameValuePair("record_per_page", ""));
                nameValuePairs.add(new BasicNameValuePair("page_no", ""));
                nameValuePairs.add(new BasicNameValuePair("sort", sortKeyWord));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);
                JSONObject jsonObject = new JSONObject(object);
                result = jsonObject.getString("flag");


                if (result.equalsIgnoreCase("Y")) {
                    if (page_count == 0) {
                        list.clear();
                    }


                    JSONArray jArray = jsonObject.getJSONArray("data");
                    String jArrayvacantvaluesizedata = String.valueOf(jArray.length());
                    // if(!jArrayvacantvaluesizedata.equals("0")) {
                    for (int j = 0; j < jArray.length(); j++) {
                        PropertyModal modal = new PropertyModal(Parcel.obtain());
                        modal.setProperty_id(jArray.getJSONObject(j).getString("property_id"));
                        String propertyvalue = jArray.getJSONObject(j).getString("property_id");
                        modal.setProperty_name(jArray.getJSONObject(j).getString("property_name"));
                        modal.setProperty_address(jArray.getJSONObject(j).getString("owner_address"));
                        modal.setProperty_area(jArray.getJSONObject(j).getString("colony"));
                        modal.setColonyname(jArray.getJSONObject(j).getString("colony_name"));
                        modal.setProperty_city(jArray.getJSONObject(j).getString("city"));
                        modal.setProperty_state(jArray.getJSONObject(j).getString("state"));
                        modal.setProperty_rent_price(jArray.getJSONObject(j).getString("rent_amount"));
                    /*modal.setCapacity_hosteler(jArray.getJSONObject(j).getString("capacity_hosteler"));
                    modal.setVacancy_hosteler(jArray.getJSONObject(j).getString("vacancy_hosteler"));*/
                        modal.setNo_of_rooms(jArray.getJSONObject(j).getString("no_of_room"));
                        //modal.setProperty_phone(jArray.getJSONObject(j).getString("property_phone"));
                        modal.setProperty_type_id(jArray.getJSONObject(j).getString("property_type"));
                        modal.setOwner_id(jArray.getJSONObject(j).getString("owner_id"));
                        modal.setUser_id(jArray.getJSONObject(j).getString("user_id"));

                        if (jArray.getJSONObject(j).has("property_images")) {
                            JSONArray propertyImageArray = jArray.getJSONObject(j).getJSONArray("property_images");
                            ArrayList<PropertyPhotoModal> images = new ArrayList<>();

                            if (propertyImageArray.length() != 0) {
                                for (int z = 0; z < propertyImageArray.length(); z++) {
                                    PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                    photoModal.setPhoto_id(propertyImageArray.getJSONObject(z).getString("id"));
                                    photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(z).getString("path"));
                                    images.add(photoModal);
                                }
                            } else {
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id("1");
                                photoModal.setPhoto_Url("Not Available");
                                images.add(photoModal);
                            }
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
                        list.add(modal);
                    }
                    //  }

                    JSONArray jArrayvacantroom = jsonObject.getJSONArray("maxvacant");
                    //for vacant room
                    String jArrayvacantvaluesize = String.valueOf(jArrayvacantroom.length());
                    //  if(!jArrayvacantvaluesize.equals("0")) {
                    for (int i = 0; i < jArrayvacantroom.length(); i++) {
                        PropertyModal modal = new PropertyModal(Parcel.obtain());
                        modal.setProperty_id(jArrayvacantroom.getJSONObject(i).getString("property_id"));
                        String propertyvalue1 = jArrayvacantroom.getJSONObject(i).getString("property_id");
                        modal.setProperty_name(jArrayvacantroom.getJSONObject(i).getString("property_name"));
                        modal.setProperty_address(jArrayvacantroom.getJSONObject(i).getString("owner_address"));
                        modal.setProperty_area(jArrayvacantroom.getJSONObject(i).getString("colony"));
                        modal.setColonyname(jArrayvacantroom.getJSONObject(i).getString("colony_name"));
                        modal.setProperty_city(jArrayvacantroom.getJSONObject(i).getString("city"));
                        modal.setProperty_state(jArrayvacantroom.getJSONObject(i).getString("state"));
                        modal.setProperty_rent_price(jArrayvacantroom.getJSONObject(i).getString("rent_amount"));
                        modal.setNo_of_rooms(jArrayvacantroom.getJSONObject(i).getString("no_of_room"));
                        modal.setProperty_type_id(jArrayvacantroom.getJSONObject(i).getString("property_type"));
                        modal.setOwner_id(jArrayvacantroom.getJSONObject(i).getString("owner_id"));
                        modal.setUser_id(jArrayvacantroom.getJSONObject(i).getString("user_id"));

                        if (jArrayvacantroom.getJSONObject(i).has("property_images")) {
                            JSONArray propertyImageArray = jArrayvacantroom.getJSONObject(i).getJSONArray("property_images");
                            ArrayList<PropertyPhotoModal> images = new ArrayList<>();

                            if (propertyImageArray.length() != 0) {
                                for (int k = 0; i < propertyImageArray.length(); k++) {
                                    PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                    photoModal.setPhoto_id(propertyImageArray.getJSONObject(k).getString("id"));
                                    photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(k).getString("path"));
                                    images.add(photoModal);
                                }
                            } else {
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id("1");
                                photoModal.setPhoto_Url("Not Available");
                                images.add(photoModal);
                            }
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

                        if (jArrayvacantroom.getJSONObject(i).getString("lat").equals("") && jArrayvacantroom.getJSONObject(i).getString("long").equals("")) {
                            lat = 0.0;
                            lng = 0.0;
                        } else if (jArrayvacantroom.getJSONObject(i).getString("lat").equals("lat") && jArrayvacantroom.getJSONObject(i).getString("long").equals("long")) {
                            lat = 0.0;
                            lng = 0.0;
                        } else {
                            lat = Double.parseDouble(jArrayvacantroom.getJSONObject(i).getString("lat"));
                            lng = Double.parseDouble(jArrayvacantroom.getJSONObject(i).getString("long"));
                        }
                        modal.setProperty_latlng(new LatLng(lat, lng));
                        list.add(modal);
                        //  mAdapter.notifyDataSetChanged();
                    }

                }

            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                    mAdapter = new HolstelListAdapter(mActivity, list);
                    propertyList.setAdapter(mAdapter);
                } else if (result.equals("N")) {
                    Toast.makeText(getActivity(), "data is not availabe", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "please check network connection", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private class searchpropertydataJson extends AsyncTask<String, String, String> {

        String result = null;
        int count;
        String name;
        private boolean IsError = false;
        String message;
        String fullname = "";
        private ProgressDialog pDialog = null;
        String searchedKeyword, type, sortKeyWord;

        public searchpropertydataJson(String searchedKeyword, String type) {
            this.searchedKeyword = searchedKeyword;
            this.type = type;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mActivity);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            list = new ArrayList<>();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("action", "viewMoreSearchProperty"));
                nameValuePairs.add(new BasicNameValuePair("search_keyword", searchedKeyword));
                nameValuePairs.add(new BasicNameValuePair("property_type_id", type));
                nameValuePairs.add(new BasicNameValuePair("record_per_page", ""));
                nameValuePairs.add(new BasicNameValuePair("page_no", ""));
                nameValuePairs.add(new BasicNameValuePair("sort", ""));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("uu 3", "" + post);
                HttpResponse response = client.execute(post);
                Log.d("uu 4", "" + response);
                String object = EntityUtils.toString(response.getEntity());
                Log.d("uu ", "" + object);


                JSONObject jsonObject = new JSONObject(object);
                result = jsonObject.getString("flag");


                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jArray = jsonObject.getJSONArray("data");


                    String jArrayvacantvaluesizedata = String.valueOf(jArray.length());
                    // if(!jArrayvacantvaluesizedata.equals("0")) {
                    for (int j = 0; j < jArray.length(); j++) {

                        PropertyModal modal = new PropertyModal(Parcel.obtain());
                        modal.setProperty_id(jArray.getJSONObject(j).getString("property_id"));
                        String propertyvalue = jArray.getJSONObject(j).getString("property_id");
                        modal.setProperty_name(jArray.getJSONObject(j).getString("property_name"));
                        modal.setProperty_address(jArray.getJSONObject(j).getString("owner_address"));
                        modal.setProperty_area(jArray.getJSONObject(j).getString("colony"));
                        modal.setProperty_city(jArray.getJSONObject(j).getString("city"));
                        modal.setProperty_state(jArray.getJSONObject(j).getString("state"));
                        modal.setProperty_rent_price(jArray.getJSONObject(j).getString("rent_amount"));
                        modal.setColonyname(jArray.getJSONObject(j).getString("colony_name"));
                       /*modal.setCapacity_hosteler(jArray.getJSONObject(j).getString("capacity_hosteler"));
                        modal.setVacancy_hosteler(jArray.getJSONObject(j).getString("vacancy_hosteler"));*/
                        modal.setNo_of_rooms(jArray.getJSONObject(j).getString("no_of_room"));
                        //modal.setProperty_phone(jArray.getJSONObject(j).getString("property_phone"));
                        modal.setProperty_type_id(jArray.getJSONObject(j).getString("property_type"));
                        modal.setOwner_id(jArray.getJSONObject(j).getString("owner_id"));
                        modal.setUser_id(jArray.getJSONObject(j).getString("user_id"));


                        if (jArray.getJSONObject(j).has("property_images")) {
                            JSONArray propertyImageArray = jArray.getJSONObject(j).getJSONArray("property_images");
                            ArrayList<PropertyPhotoModal> images = new ArrayList<>();

                            if (propertyImageArray.length() != 0) {
                                for (int z = 0; z < propertyImageArray.length(); z++) {
                                    PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                    photoModal.setPhoto_id(propertyImageArray.getJSONObject(z).getString("id"));
                                    photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(z).getString("path"));
                                    images.add(photoModal);
                                }
                            } else {
                                PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                photoModal.setPhoto_id("1");
                                photoModal.setPhoto_Url("Not Available");
                                images.add(photoModal);
                            }
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
                        list.add(modal);
                    }
                    //  }

                    JSONArray jArrayvacantroom = jsonObject.getJSONArray("maxvacant");
                    //for vacant room
                    String jArrayvacantvaluesize = String.valueOf(jArrayvacantroom.length());
                    //  if(!jArrayvacantvaluesize.equals("0")) {
                    for (int i = 0; i < jArrayvacantroom.length(); i++) {
                        PropertyModal modal = new PropertyModal(Parcel.obtain());
                        modal.setProperty_id(jArrayvacantroom.getJSONObject(i).getString("property_id"));
                        // String propertyvalue1 = jArrayvacantroom.getJSONObject(i).getString("property_id");
                        modal.setProperty_name(jArrayvacantroom.getJSONObject(i).getString("property_name"));
                        modal.setProperty_address(jArrayvacantroom.getJSONObject(i).getString("owner_address"));
                        modal.setProperty_area(jArrayvacantroom.getJSONObject(i).getString("colony"));
                        modal.setProperty_city(jArrayvacantroom.getJSONObject(i).getString("city"));
                        modal.setProperty_state(jArrayvacantroom.getJSONObject(i).getString("state"));
                        modal.setColonyname(jArrayvacantroom.getJSONObject(i).getString("colony_name"));
                        modal.setProperty_rent_price(jArrayvacantroom.getJSONObject(i).getString("rent_amount"));
                        modal.setNo_of_rooms(jArrayvacantroom.getJSONObject(i).getString("no_of_room"));
                        modal.setProperty_type_id(jArrayvacantroom.getJSONObject(i).getString("property_type"));
                        modal.setOwner_id(jArrayvacantroom.getJSONObject(i).getString("owner_id"));
                        modal.setUser_id(jArrayvacantroom.getJSONObject(i).getString("user_id"));


                        PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());

                        ArrayList<PropertyPhotoModal> images = new ArrayList<>();

                        // modal.setUser_id(jArrayvacantroom.getJSONObject(i).getString("property_images"));

                        photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + jArrayvacantroom.getJSONObject(i).getString("property_images"));

                        //photoModal.setPhoto_Url(jArrayvacantroom.getJSONObject(i).getString("property_images"));

                        images.add(photoModal);

                        modal.setPropertyImage(images);

                        //if(jArrayvacantroom.getJSONObject(i).has("property_images")){


                        //}

                        // if (jArrayvacantroom.getJSONObject(i).has("property_images")) {
                        //  JSONArray propertyImageArray = jArrayvacantroom.getJSONObject(i).getJSONArray("property_images");
                        //ArrayList<PropertyPhotoModal> images = new ArrayList<>();

                        // if (propertyImageArray.length() != 0) {

                        //  PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());

                        // photoModal.setPhoto_Url(String.valueOf(jArrayvacantroom.getJSONObject(i).getJSONObject("property_images")));

                        //     images.add(photoModal);


                  /*              for (int k = 0; i < propertyImageArray.length(); k++) {
                                    //PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                                   // photoModal.setPhoto_id(propertyImageArray.getJSONObject(k).getString("id"));
                                    //photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(k).getString("path"));

                                    photoModal.setPhoto_Url(WebUrls.IMG_URL + "" + propertyImageArray.getJSONObject(k).getString("path"));

                                    images.add(photoModal);
                                }*/
                        //  } else {
                        //   PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                        //   photoModal.setPhoto_id("1");
                        //   photoModal.setPhoto_Url("Not Available");
                        // images.add(photoModal);
                        // }
                        //modal.setPropertyImage(images);

                        //}

                   /* else {
                            ArrayList<PropertyPhotoModal> images = new ArrayList<>();
                            PropertyPhotoModal photoModal = new PropertyPhotoModal(Parcel.obtain());
                            photoModal.setPhoto_id("1");
                            photoModal.setPhoto_Url("Not Available");
                            images.add(photoModal);
                            modal.setPropertyImage(images);

                        }*/


                        double lat = 0.0;
                        double lng = 0.0;

                        if (jArrayvacantroom.getJSONObject(i).getString("lat").equals("") && jArrayvacantroom.getJSONObject(i).getString("long").equals("")) {
                            lat = 0.0;
                            lng = 0.0;
                        } else if (jArrayvacantroom.getJSONObject(i).getString("lat").equals("lat") && jArrayvacantroom.getJSONObject(i).getString("long").equals("long")) {
                            lat = 0.0;
                            lng = 0.0;
                        } else {
                            lat = Double.parseDouble(jArrayvacantroom.getJSONObject(i).getString("lat"));
                            lng = Double.parseDouble(jArrayvacantroom.getJSONObject(i).getString("long"));
                        }
                        modal.setProperty_latlng(new LatLng(lat, lng));
                        list.add(modal);
                        //  mAdapter.notifyDataSetChanged();
                    }

                }

            } catch (Exception e) {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            // if(!IsError) {
            if (result.equalsIgnoreCase("Y")) {
                mAdapter = new HolstelListAdapter(mActivity, list);
                propertyList.setAdapter(mAdapter);
            } else if (result.equals("N")) {
                Toast.makeText(getActivity(), "data is not availabe", Toast.LENGTH_SHORT).show();
            }
            /*} else {
                Toast.makeText(getActivity(), "please check network connection", Toast.LENGTH_SHORT).show();
            }*/

        }


    }


}
