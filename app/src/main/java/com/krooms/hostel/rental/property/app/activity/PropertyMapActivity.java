package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.util.AddMarker;
import com.krooms.hostel.rental.property.app.R;

public class PropertyMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isGotMyLocation = false;
    private CameraPosition cameraPosition;
    private Marker myPosition;
    private Marker selectedProperty;
    private String mPropertyTypeid = "";
    private String mPropertyid = "";
    private Double lat;
    private Double lng;
    private String propertyRent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_map);
        lat = getIntent().getDoubleExtra("lat", 22.7253);
        lng = getIntent().getDoubleExtra("long", 75.8656);
        propertyRent = getIntent().getStringExtra("property_rent");
        mPropertyTypeid = getIntent().getExtras().getString("property_id_type");
        mPropertyid = getIntent().getExtras().getString("property_id");
        LogConfig.logd("onCreate ", "lat " + lat + " lng " + lng);
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(this);
            //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap(); // getMap is deprecated
            // Check if we were successful in obtaining the map.
           /* if (mMap != null) {
                setUpMap();
            }*/
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        LatLng sydney = new LatLng(lat, lng);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
        setUpMap();

    }

    public void setUpMap() {
        if (myPosition != null) {
            myPosition = null;
        }
        if (myPosition == null) {
            myPosition = mMap.addMarker(new MarkerOptions().title("")
                    .snippet("").position(new LatLng(lat, lng)));
            new AddMarker(myPosition, R.drawable.pin/*"1200rs"*/, this);
        }

        cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lng)).zoom(14).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myPosition.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                if (!isGotMyLocation) {
                    cameraPosition = new CameraPosition.Builder().target(myPosition.getPosition()).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    isGotMyLocation = true;
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

            }
        });

        addPropertyMarker();
    }


    public void addPropertyMarker(){

        selectedProperty = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Selected Property"));

        if(mPropertyTypeid.equals("1")){
            selectedProperty.setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_PURPLE, propertyRent + "")));
        }else if(mPropertyTypeid.equals("2")){

            selectedProperty.setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_DEFAULT, propertyRent + "")));
        }else if(mPropertyTypeid.equals("3")){

            selectedProperty.setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_ORANGE, propertyRent + "")));
        }else if(mPropertyTypeid.equals("4")){

            selectedProperty.setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_GREEN, propertyRent + "")));
        }else if(mPropertyTypeid.equals("5")){

            selectedProperty.setIcon(BitmapDescriptorFactory.fromBitmap(getNewCustomMarker(IconGenerator.STYLE_WHITE, propertyRent + "")));
        }
    }

    public Bitmap getNewCustomMarker(int style,String str){
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(style);
        //*options.icon(BitmapDescriptorFactory.fromBitmap(*//*
        Bitmap mBitmap = iconFactory.makeIcon(str);
//		options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        return mBitmap;
    }

}
