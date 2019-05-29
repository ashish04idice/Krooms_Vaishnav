package com.krooms.hostel.rental.property.app.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.HolstelListAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.util.AddMarker;

import java.util.ArrayList;

/**
 * Created by Anuj S on 2/28/2016.
 */
public class PropertyListMapCategoryWiseFragment extends Fragment implements OnMapReadyCallback {

    private FragmentActivity mActivity = null;
    // GoogleMap mMap;
//    SupportMapFragment mMap;
    Marker myPosition;
    private View convertView = null;
    private Gallery listView;
    private HolstelListAdapter adapter;
    private GoogleMap mMap;
    private MapView mMapView;

    ArrayList<PropertyModal> mPropertyList = new ArrayList<>();
    //    Gallery mGallery;
    boolean isGotMyLocation = false;
    CameraPosition cameraPosition;
    Location currentLocation;
    double lat, lng;

    public PropertyListMapCategoryWiseFragment() {

    }

    public void getParameter(ArrayList<PropertyModal> list) {
        mPropertyList = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();

        currentLocation = Common.getMyCurrentLocation(mActivity);
        if (currentLocation != null) {
            lat = currentLocation.getLatitude();
            lng = currentLocation.getLongitude();
        } else {
            lat = 22.7253;
            lng = 75.8656;
        }
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        convertView = inflater.inflate(R.layout.property_list_map_fragment, container, false);

        mMapView = (MapView) convertView.findViewById(R.id.mapView);
        MapsInitializer.initialize(mActivity);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        createView();

        return convertView;
    }


   /* @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
//        if (mMap != null)
        //setupMap(mMap);
        //Log.e("VIEW","onViewCreated");
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
          //  mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView)).getMap(); // getMap is deprecated
            // Check if we were successful in obtaining the map.

            SupportMapFragment mapFragment = (SupportMapFragment) mActivity.getSupportFragmentManager()
                    .findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(this);

            if (mMap != null)
                setUpMap(mMap);
        }
    }*/


    public void createView() {

        listView = (Gallery) convertView.findViewById(R.id.hostelDetailList);
        adapter = new HolstelListAdapter(mActivity, mPropertyList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cameraPosition = new CameraPosition.Builder().target(mPropertyList.get(position).getProperty_latlng()).zoom(12).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


    }

    public void setUpMap(GoogleMap mMap) {
        if (myPosition != null) {
            myPosition = null;
        }
        if (myPosition == null) {
            myPosition = this.mMap.addMarker(new MarkerOptions().title("").snippet("")
                    .position(new LatLng(lat, lng)));
            new AddMarker(myPosition, R.drawable.pin/*"1200rs"*/, getActivity());
        }

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.mMap.setMyLocationEnabled(true);

        this.mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {


                myPosition.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                if (!isGotMyLocation) {
                    cameraPosition = new CameraPosition.Builder().target(myPosition.getPosition()).zoom(12).build();
                    PropertyListMapCategoryWiseFragment.this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    isGotMyLocation = true;
                }
            }
        });

        this.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < mPropertyList.size(); i++) {
                    if (marker.getTitle().equals(mPropertyList.get(i).getProperty_id())) {
                        listView.setSelection(i);

                    }
                }

                return true;
            }
        });

        showAllMarker();

    }

    Marker otherMarkers[];

    public void showAllMarker() {


        otherMarkers = new Marker[mPropertyList.size()];
        for (int i = 0; i < mPropertyList.size(); i++) {


            otherMarkers[i] = mMap.addMarker(new MarkerOptions().title(mPropertyList.get(i).getProperty_id())
                    .position(mPropertyList.get(i).getProperty_latlng()));

            if (mPropertyList.get(i).getProperty_type_id().equals("1")) {
                otherMarkers[i].setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_PURPLE, mPropertyList.get(i).getProperty_rent_price())));

            } else if (mPropertyList.get(i).getProperty_type_id().equals("2")) {
                otherMarkers[i].setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_DEFAULT, mPropertyList.get(i).getProperty_rent_price())));
            } else if (mPropertyList.get(i).getProperty_type_id().equals("3")) {
                otherMarkers[i].setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_ORANGE, mPropertyList.get(i).getProperty_rent_price())));
            } else if (mPropertyList.get(i).getProperty_type_id().equals("4")) {
                otherMarkers[i].setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_GREEN, mPropertyList.get(i).getProperty_rent_price())));
            } else if (mPropertyList.get(i).getProperty_type_id().equals("5")) {
                otherMarkers[i].setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_WHITE, mPropertyList.get(i).getProperty_rent_price())));
            }

            if (i == mPropertyList.size() - 1) {
                cameraPosition = new CameraPosition.Builder().target(otherMarkers[i].getPosition()).zoom(8).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        }
    }


    public Bitmap getNewCustomMarker(int style, String price) {
        IconGenerator iconFactory = new IconGenerator(mActivity);
        iconFactory.setStyle(style);
        //*options.icon(BitmapDescriptorFactory.fromBitmap(*//*
        Bitmap mBitmap = iconFactory.makeIcon(price);
        //options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        return mBitmap;
    }


    //new code 25/02/2019
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap(mMap);

    }
}
