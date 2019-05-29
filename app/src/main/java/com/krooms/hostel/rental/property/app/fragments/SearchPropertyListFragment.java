package com.krooms.hostel.rental.property.app.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.krooms.hostel.rental.property.app.activity.SearchedPropertyListActivity;
import com.krooms.hostel.rental.property.app.adapter.PropertyListAdapter;
import com.krooms.hostel.rental.property.app.asynctask.SearchPropertyServiceAsyncTask;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.HolstelListAdapter;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.common.WebUrls;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.PropertyPhotoModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 3/2/2016.
 */
public class SearchPropertyListFragment extends Fragment implements ServiceResponce {

    private FragmentActivity mActivity = null;
    private View convertView = null;
    Validation mValidation = null;
    private ListView hostelList = null;
    private PropertyListAdapter mAdapter = null;
    private Boolean _isSearechInable = false;
    private String state = "";
    private String city = "";
    private String area = "";
    private String budget = "";
    private String proidsearch = "";
    private String status = "";
    private String tanantType = "";
    private String featurelist = "";
    private String propertyType = "0";
    private HolstelListAdapter adapter;
    Common mCommon = null;
    private Boolean _isScroll = true;
    private int page_count = 0;
    String sortKeyWord;

    public SearchPropertyListFragment() {

    }

    public void getPerameter(/*ArrayList<PropertyModal> list,*/ String sort_type) {

        /*page_count= 0;*/
        sortKeyWord = sort_type;

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


        if (mActivity.getIntent().getExtras().getString("from").equals("filterDialog")) {
            propertyType = mActivity.getIntent().getExtras().getString("propertytype");
            state = mActivity.getIntent().getExtras().getString("state");
            city = mActivity.getIntent().getExtras().getString("city");
            area = mActivity.getIntent().getExtras().getString("landmark");
            tanantType = mActivity.getIntent().getExtras().getString("tenantType");
            status = mActivity.getIntent().getExtras().getString("status");
            featurelist = mActivity.getIntent().getExtras().getString("propertyFeature");
            budget = mActivity.getIntent().getExtras().getString("rentAmount");
            proidsearch = mActivity.getIntent().getExtras().getString("PropertyIdSearch");

        } else {

            city = mActivity.getIntent().getExtras().getString("city");
            area = mActivity.getIntent().getExtras().getString("area");
        }

        if (sortKeyWord.equals("")) {
            getPropertyList();
        } else {
            getSortedPropertyList();
        }

        return convertView;

    }

    public void createView() {

        hostelList = (ListView) convertView.findViewById(R.id.otherPropertyList);
        adapter = new HolstelListAdapter(mActivity, SearchedPropertyListActivity.list);
        hostelList.setAdapter(adapter);

        hostelList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (_isScroll && view.getLastVisiblePosition() >= SearchedPropertyListActivity.list.size() - 1) {
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
        });

    }

    @Override
    public void requestResponce(String result) {


        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                if (page_count == 0) {
                    SearchedPropertyListActivity.list.clear();
                }

                JSONObject jObject = jsonObject.getJSONObject(WebUrls.DATA_JSON);

                if (jObject.has("main_result")) {

                    JSONArray jArray = jObject.getJSONArray("main_result");
                    for (int j = 0; j < jArray.length(); j++) {

                        PropertyModal modal = new PropertyModal(Parcel.obtain());
                        modal.setProperty_id(jArray.getJSONObject(j).getString("property_id"));
                        modal.setProperty_name(jArray.getJSONObject(j).getString("property_name"));
                        modal.setProperty_address(jArray.getJSONObject(j).getString("owner_address"));
                        modal.setProperty_area(jArray.getJSONObject(j).getString("colony"));
                        modal.setProperty_city(jArray.getJSONObject(j).getString("city"));
                        modal.setProperty_state(jArray.getJSONObject(j).getString("state"));
                        modal.setColonyname(jArray.getJSONObject(j).getString("colony_name"));
                        modal.setProperty_rent_price(jArray.getJSONObject(j).getString("rent_amount"));
                       /*modal.setCapacity_hosteler(jArray.getJSONObject(j).getString("capacity_hosteler"));
                       modal.setVacancy_hosteler(jArray.getJSONObject(j).getString("vacancy_hosteler"));*/
                        modal.setNo_of_rooms(jArray.getJSONObject(j).getString("no_of_room"));
                        //modal.setProperty_phone(jArray.getJSONObject(j).getString("property_phone"));
                        modal.setProperty_type_id(jArray.getJSONObject(j).getString("property_type"));
                        modal.setOwner_id(jArray.getJSONObject(j).getString("owner_id"));
                        modal.setUser_id(jArray.getJSONObject(j).getString("user_id"));
                        modal.setSearchResult_type(0);

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
                        SearchedPropertyListActivity.list.add(modal);
                        adapter.notifyDataSetChanged();
                    }
                    _isScroll = true;
                } else {
                    if (SearchedPropertyListActivity.list.size() == 0) {
                        mCommon.displayAlert(mActivity, "No Result found", true);

                    } else {
                        mCommon.displayAlert(mActivity, "" + this.getResources()
                                .getString(R.string.str_no_more_data), false);
                    }
//                    mCommon.displayAlert(mActivity, "No Result found", false);
                }

            } else {
                if (SearchedPropertyListActivity.list.size() == 0) {
                    mCommon.displayAlert(mActivity, "" +
                            jsonObject.getString(WebUrls.MESSAGE_JSON), true);
                } else {
                    mCommon.displayAlert(mActivity, "" + this.getResources()
                            .getString(R.string.str_no_more_data), false);
                }
            }
        } catch (JSONException e) {
        }
    }

    public void getPropertyList() {


        SearchPropertyServiceAsyncTask serviceAsyncTask = new SearchPropertyServiceAsyncTask(mActivity);
        serviceAsyncTask.setCallBack(this);
         /*serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyType, state, city,
                area, tanantType, status, featurelist, "" + 10, budget, "" + page_count, "");
            */
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyType, state, city,
                area, tanantType, status, featurelist, "" + 10, budget, "" + page_count, "", proidsearch);


    }

    public void getSortedPropertyList() {

        SearchPropertyServiceAsyncTask serviceAsyncTask = new SearchPropertyServiceAsyncTask(mActivity);
        serviceAsyncTask.setCallBack(this);
       /*serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyType, state, city,
                area, tanantType, status, featurelist, "" + 10, budget, "" + page_count, sortKeyWord);
        */
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyType, state, city,
                area, tanantType, status, featurelist, "" + 10, budget, "" + page_count, sortKeyWord, proidsearch);

    }

}
