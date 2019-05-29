package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.krooms.hostel.rental.property.app.adapter.MultiChoiceAdapter;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.util.AddMarker;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.Common;

import java.util.ArrayList;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private boolean isGotMyLocation = false;
    private CameraPosition cameraPosition;
    private Marker myPosition;
    private Location currentLocation;
    private double lat, lng;
    private String city = "Indore";
    private String city_id = "1";

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        noLocation();
        currentLocation = Common.getMyCurrentLocation(this);

        if (!getIntent().getExtras().getString("city").equals("")) {
            city = getIntent().getExtras().getString("city");
        }

        if (!getIntent().getExtras().getString("city_id").equals("")) {
            city_id = getIntent().getExtras().getString("city_id");
        }
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment supportMapFragment = ((SupportMapFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mapView)); // getMap is deprecated
            supportMapFragment.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }


    }

    public void convertAddress(String city) {

        if (city_id.equals("1")) {
            lat = 22.7196;
            lng = 75.8577;
        } else if (city_id.equals("2")) {
            lat = 25.2138;
            lng = 75.8648;
        } else if (city_id.equals("3")) {
            lat = 23.2599;
            lng = 77.4126;
        } else if (city_id.equals("4")) {

            lat = 26.9124;
            lng = 75.7873;

        } else if (city_id.equals("5")) {

            lat = 24.5854;
            lng = 73.7125;

        } else if (city_id.equals("6")) {

            lat = 12.9716;
            lng = 77.5946;

        } else if (city_id.equals("7")) {

            lat = 18.5204;
            lng = 73.8567;

        } else if (city_id.equals("8")) {

            lat = 28.4744;
            lng = 77.5040;

        } else if (city_id.equals("9")) {

            lat = 28.7041;
            lng = 77.1025;

        }
        //Jabalpur
        else if (city_id.equals("10")) {
            lat = 23.1815;
            lng = 79.9864;

        }
        //Gwalior
        else if (city_id.equals("11")) {
            lat = 26.2183;
            lng = 78.1828;

        }

        //Ujjain
        else if (city_id.equals("12")) {
            lat = 23.1793;
            lng = 75.7849;

        } else if (city_id.equals("13")) {
            lat = 23.8388;
            lng = 78.7378;

        } else if (city_id.equals("14")) {
            lat = 22.9623;
            lng = 76.0508;

        } else if (city_id.equals("15")) {
            lat = 24.6005;
            lng = 80.8322;

        } else if (city_id.equals("16")) {
            lat = 23.3342;
            lng = 75.0376;

        } else if (city_id.equals("17")) {
            lat = 24.5373;
            lng = 81.3042;

        } else if (city_id.equals("18")) {
            lat = 23.8308;
            lng = 80.4072;

        } else if (city_id.equals("19")) {
            lat = 24.1992;
            lng = 82.6645;

        } else if (city_id.equals("20")) {
            lat = 21.3194;
            lng = 76.2224;

        } else if (city_id.equals("21")) {
            lat = 21.8257;
            lng = 76.3526;

        } else if (city_id.equals("22")) {
            lat = 26.4450;
            lng = 78.7476;

        } else if (city_id.equals("23")) {
            lat = 22.0574;
            lng = 78.9382;

        } else if (city_id.equals("24")) {
            lat = 24.6348;
            lng = 77.2980;

        } else if (city_id.equals("25")) {
            lat = 25.4358;
            lng = 77.6651;

        } else if (city_id.equals("26")) {
            lat = 23.9430;
            lng = 77.8367;

        } else if (city_id.equals("27")) {
            lat = 24.9164;
            lng = 79.5812;

        } else if (city_id.equals("28")) {
            lat = 23.8381;
            lng = 79.4422;

        } else if (city_id.equals("29")) {
            lat = 24.0768;
            lng = 75.0693;

        } else if (city_id.equals("30")) {
            lat = 21.9029;
            lng = 75.8069;

        } else if (city_id.equals("31")) {
            lat = 24.4764;
            lng = 74.8624;

        } else if (city_id.equals("32")) {
            lat = 22.6133;
            lng = 75.6823;

        } else if (city_id.equals("33")) {
            lat = 22.7441;
            lng = 77.7370;

        } else if (city_id.equals("34")) {
            lat = 22.6055;
            lng = 77.7535;

        } else if (city_id.equals("35")) {
            lat = 22.9473;
            lng = 77.1025;

        } else if (city_id.equals("36")) {
            lat = 21.9672;
            lng = 77.7452;

        } else if (city_id.equals("37")) {
            lat = 22.0869;
            lng = 79.5435;
        } else if (city_id.equals("38")) {

            lat = 25.6845;
            lng = 78.5661;
        } else if (city_id.equals("39")) {
            lat = 23.4455;
            lng = 75.4170;

        }
        //Ajmer
        else if (city_id.equals("40")) {
            lat = 26.4499;
            lng = 74.6399;

        } else if (city_id.equals("41")) {
            lat = 27.5530;
            lng = 76.6346;

        } else if (city_id.equals("42")) {
            lat = 23.5461;
            lng = 74.4350;

        } else if (city_id.equals("43")) {
            lat = 25.1011;
            lng = 76.5132;

        } else if (city_id.equals("44")) {
            lat = 25.7532;
            lng = 71.4181;

        } else if (city_id.equals("45")) {
            lat = 27.2170;
            lng = 77.4895;

        } else if (city_id.equals("46")) {
            lat = 25.3214;
            lng = 74.5870;

        } else if (city_id.equals("47")) {
            lat = 28.0229;
            lng = 73.3119;
        } else if (city_id.equals("48")) {
            lat = 25.4305;
            lng = 75.6499;
        } else if (city_id.equals("49")) {
            lat = 24.8887;
            lng = 74.6269;
        } else if (city_id.equals("50")) {
            lat = 28.2920;
            lng = 74.9618;
        } else if (city_id.equals("51")) {
            lat = 26.8932;
            lng = 76.3375;
        } else if (city_id.equals("52")) {
            lat = 26.7025;
            lng = 77.8934;
        } else if (city_id.equals("53")) {
            lat = 23.8417;
            lng = 73.7147;
        } else if (city_id.equals("54")) {
            lat = 29.5815;
            lng = 74.3294;
        } else if (city_id.equals("55")) {
            lat = 26.9157;
            lng = 70.9083;
        } else if (city_id.equals("56")) {
            lat = 25.1257;
            lng = 72.1416;
        } else if (city_id.equals("57")) {
            lat = 24.5973;
            lng = 76.1610;
        } else if (city_id.equals("58")) {
            lat = 28.1289;
            lng = 75.3995;
        } else if (city_id.equals("59")) {
            lat = 26.2389;
            lng = 73.0243;
        } else if (city_id.equals("60")) {
            lat = 26.4883;
            lng = 74.6399;
        } else if (city_id.equals("61")) {
            lat = 27.1991;
            lng = 73.7409;
        } else if (city_id.equals("62")) {
            lat = 25.7711;
            lng = 73.3234;
        } else if (city_id.equals("63")) {
            lat = 25.8973;
            lng = 81.9453;
        } else if (city_id.equals("64")) {
            lat = 25.2235;
            lng = 73.7478;
        } else if (city_id.equals("65")) {
            lat = 26.0378;
            lng = 76.3522;
        } else if (city_id.equals("66")) {
            lat = 27.6094;
            lng = 75.1399;
        } else if (city_id.equals("67")) {
            lat = 24.7467;
            lng = 72.8043;
        } else if (city_id.equals("68")) {
            lat = 29.9038;
            lng = 73.8772;
        } else if (city_id.equals("69")) {
            lat = 26.1624;
            lng = 75.6208;
        }//Visakhapatnam
        else if (city_id.equals("70")) {
            lat = 17.6868;
            lng = 83.2185;
        } else if (city_id.equals("71")) {
            lat = 16.5062;
            lng = 80.6480;
        } else if (city_id.equals("72")) {
            lat = 16.3067;
            lng = 80.4365;
        } else if (city_id.equals("73")) {
            lat = 14.4426;
            lng = 79.9865;
        } else if (city_id.equals("74")) {
            lat = 15.8281;
            lng = 78.0373;
        } else if (city_id.equals("75")) {
            lat = 14.4674;
            lng = 78.8241;
        } else if (city_id.equals("76")) {
            lat = 17.0005;
            lng = 81.8040;
        } else if (city_id.equals("77")) {
            lat = 16.9891;
            lng = 82.2475;
        } else if (city_id.equals("78")) {
            lat = 13.6288;
            lng = 79.4192;
        } else if (city_id.equals("79")) {
            lat = 14.6819;
            lng = 77.6006;
        } else if (city_id.equals("80")) {
            lat = 18.4059;
            lng = 83.3362;
        } else if (city_id.equals("81")) {
            lat = 16.7107;
            lng = 81.0952;
        } else if (city_id.equals("82")) {
            lat = 15.5057;
            lng = 80.0499;
        } else if (city_id.equals("83")) {
            lat = 15.4786;
            lng = 78.4831;
        } else if (city_id.equals("84")) {
            lat = 16.1905;
            lng = 81.1362;
        } else if (city_id.equals("85")) {
            lat = 15.6322;
            lng = 77.2728;
        } else if (city_id.equals("86")) {
            lat = 16.2395;
            lng = 80.6493;
        } else if (city_id.equals("87")) {
            lat = 14.7492;
            lng = 78.5532;
        } else if (city_id.equals("88")) {
            lat = 13.2218;
            lng = 79.1010;
        } else if (city_id.equals("89")) {
            lat = 13.8185;
            lng = 77.4989;
        } else if (city_id.equals("90")) {
            lat = 16.5449;
            lng = 81.5212;
        } else if (city_id.equals("91")) {
            lat = 13.5603;
            lng = 78.5036;
        } else if (city_id.equals("92")) {
            lat = 15.1674;
            lng = 77.3736;
        } else if (city_id.equals("93")) {
            lat = 18.4285;
            lng = 84.0167;
        } else if (city_id.equals("94")) {
            lat = 14.4137;
            lng = 77.7126;
        } else if (city_id.equals("95")) {
            lat = 16.4410;
            lng = 80.9926;
        } else if (city_id.equals("96")) {
            lat = 16.2354;
            lng = 80.0479;
        } else if (city_id.equals("97")) {
            lat = 14.9070;
            lng = 78.0093;
        } else if (city_id.equals("98")) {
            lat = 16.8138;
            lng = 81.5212;
        } else if (city_id.equals("99")) {
            lat = 20.9374;
            lng = 77.7796;
        } else if (city_id.equals("100")) {
            lat = 16.0924;
            lng = 80.1624;

        }//Along
        else if (city_id.equals("101")) {
            lat = 28.1628;
            lng = 94.8054;
        } else if (city_id.equals("102")) {
            lat = 27.9833;
            lng = 94.6666;
        } else if (city_id.equals("103")) {
            lat = 27.2645;
            lng = 92.4159;
        } else if (city_id.equals("104")) {
            lat = 27.7422;
            lng = 96.6424;
        } else if (city_id.equals("105")) {
            lat = 27.9863;
            lng = 94.2205;
        } else if (city_id.equals("106")) {
            lat = 27.1935;
            lng = 95.4699;
        } else if (city_id.equals("107")) {
            lat = 27.0844;
            lng = 93.6053;
        } else if (city_id.equals("108")) {
            lat = 27.3513;
            lng = 96.0163;
        } else if (city_id.equals("109")) {
            lat = 26.9929;
            lng = 95.5014;
        } else if (city_id.equals("110")) {
            lat = 27.0986;
            lng = 93.6949;
        } else if (city_id.equals("111")) {
            lat = 27.6666;
            lng = 95.8699;
        } else if (city_id.equals("112")) {
            lat = 28.0619;
            lng = 95.3259;
        } else if (city_id.equals("113")) {
            lat = 28.1429;
            lng = 95.8431;
        } else if (city_id.equals("114")) {
            lat = 27.3610;
            lng = 93.0401;
        } else if (city_id.equals("115")) {
            lat = 27.6325;
            lng = 91.7539;
        } else if (city_id.equals("116")) {
            lat = 27.9339;
            lng = 96.1580;
        } else if (city_id.equals("117")) {
            lat = 27.5448;
            lng = 93.8197;
            //Barpeta
        } else if (city_id.equals("118")) {
            lat = 26.3216;
            lng = 90.9821;
        } else if (city_id.equals("119")) {
            lat = 26.5010;
            lng = 90.5352;
        } else if (city_id.equals("120")) {
            lat = 27.4728;
            lng = 94.9120;
        } else if (city_id.equals("121")) {
            lat = 26.1445;
            lng = 91.7362;
        } else if (city_id.equals("122")) {
            lat = 26.7465;
            lng = 94.2026;
        } else if (city_id.equals("123")) {
            lat = 26.2997;
            lng = 92.6984;
        } else if (city_id.equals("124")) {
            lat = 24.8333;
            lng = 92.7789;
        } else if (city_id.equals("125")) {
            lat = 27.4922;
            lng = 95.3468;
        } else if (city_id.equals("126")) {
            lat = 26.6528;
            lng = 92.7926;
        }//Aurangabad
        else if (city_id.equals("136")) {
            lat = 24.7500;
            lng = 84.3700;
        } else if (city_id.equals("137")) {
            lat = 25.5560;
            lng = 84.6603;
        } else if (city_id.equals("138")) {
            lat = 27.1222;
            lng = 84.0722;
        } else if (city_id.equals("139")) {
            lat = 25.5647;
            lng = 83.9777;
        } else if (city_id.equals("140")) {
            lat = 26.8028;
            lng = 84.5170;
        } else if (city_id.equals("141")) {
            lat = 25.1982;
            lng = 85.5149;
        } else if (city_id.equals("142")) {
            lat = 25.2533;
            lng = 86.9890;
        } else if (city_id.equals("143")) {
            lat = 25.7796;
            lng = 84.7499;
        } else if (city_id.equals("144")) {
            lat = 26.1119;
            lng = 85.8960;
        } else if (city_id.equals("145")) {
            lat = 25.6117;
            lng = 85.0467;
        } else if (city_id.equals("146")) {
            lat = 26.2993;
            lng = 87.2666;
        } else if (city_id.equals("147")) {
            lat = 24.7800;
            lng = 84.9818;
        } else if (city_id.equals("148")) {
            lat = 25.6858;
            lng = 85.2146;
        } else if (city_id.equals("149")) {
            lat = 25.3127;
            lng = 86.4906;
        } else if (city_id.equals("150")) {
            lat = 25.1516;
            lng = 84.9818;
        } else if (city_id.equals("151")) {
            lat = 26.0917;
            lng = 87.9384;
        } else if (city_id.equals("152")) {
            lat = 25.5520;
            lng = 87.5719;
        } else if (city_id.equals("153")) {
            lat = 25.3747;
            lng = 86.4735;
        } else if (city_id.equals("154")) {
            lat = 26.6470;
            lng = 84.9089;
        } else if (city_id.equals("155")) {
            lat = 26.1209;
            lng = 85.3647;
        } else if (city_id.equals("156")) {
            lat = 24.7426;
            lng = 85.5200;
        } else if (city_id.equals("157")) {
            lat = 25.5941;
            lng = 85.1376;
        } else if (city_id.equals("158")) {
            lat = 25.7711;
            lng = 87.4821;
        } else if (city_id.equals("159")) {
            lat = 25.8835;
            lng = 86.6006;
        } else if (city_id.equals("160")) {
            lat = 24.9535;
            lng = 84.0117;
        } else if (city_id.equals("161")) {
            lat = 26.2196;
            lng = 84.3567;
        } else if (city_id.equals("162")) {
            lat = 26.5952;
            lng = 85.4808;
        } else if (city_id.equals("163")) {
            lat = 23.1355;
            lng = 83.1818;
        } else if (city_id.equals("164")) {
            lat = 21.1938;
            lng = 81.3509;
        } else if (city_id.equals("165")) {
            lat = 22.0796;
            lng = 82.1391;
        } else if (city_id.equals("166")) {
            lat = 21.7384;
            lng = 81.9480;
        } else if (city_id.equals("167")) {
            lat = 21.6894;
            lng = 81.5596;
        } else if (city_id.equals("168")) {
            lat = 23.2713;
            lng = 82.5565;
        } else if (city_id.equals("169")) {
            lat = 23.1880;
            lng = 82.3542;
        } else if (city_id.equals("170")) {
            lat = 22.0320;
            lng = 82.6537;
        } else if (city_id.equals("171")) {
            lat = 20.7015;
            lng = 81.5542;
        } else if (city_id.equals("172")) {
            lat = 20.5831;
            lng = 81.0810;
        } else if (city_id.equals("173")) {
            lat = 21.1802;
            lng = 80.7602;
        } else if (city_id.equals("174")) {
            lat = 20.97099;
            lng = 81.83163;
        } else if (city_id.equals("175")) {
            lat = 19.0741;
            lng = 82.0080;
        } else if (city_id.equals("176")) {
            lat = 22.0090;
            lng = 81.2243;
        } else if (city_id.equals("177")) {
            lat = 20.1990;
            lng = 81.0755;
        } else if (city_id.equals("178")) {
            lat = 19.5959;
            lng = 81.6638;
        } else if (city_id.equals("179")) {
            lat = 21.1091;
            lng = 82.0979;
        } else if (city_id.equals("180")) {
            lat = 22.0685;
            lng = 81.6857;
        } else if (city_id.equals("181")) {
            lat = 23.2139;
            lng = 82.2013;
        } else if (city_id.equals("182")) {
            lat = 22.0193;
            lng = 82.5700;
        } else if (city_id.equals("183")) {
            lat = 21.2514;
            lng = 81.6296;
        } else if (city_id.equals("184")) {
            lat = 21.0971;
            lng = 81.0302;
        } else if (city_id.equals("185")) {
            lat = 22.0078;
            lng = 83.3362;
        } else if (city_id.equals("186")) {
            lat = 21.5528;
            lng = 81.7842;
        } else if (city_id.equals("187")) {
            lat = 15.5889;
            lng = 73.9654;
        } else if (city_id.equals("188")) {
            lat = 14.9931;
            lng = 74.0476;
        } else if (city_id.equals("189")) {
            lat = 15.1742;
            lng = 73.9828;
        } else if (city_id.equals("190")) {
            lat = 15.2417;
            lng = 74.1123;
        } else if (city_id.equals("191")) {
            lat = 15.6002;
            lng = 73.8125;
        } else if (city_id.equals("192")) {
            lat = 15.2832;
            lng = 73.9862;
        } else if (city_id.equals("193")) {
            lat = 15.3874;
            lng = 73.8154;
        } else if (city_id.equals("194")) {
            lat = 15.4909;
            lng = 73.8278;
        } else if (city_id.equals("195")) {
            lat = 15.7198;
            lng = 73.7963;
        } else if (city_id.equals("196")) {
            lat = 15.3991;
            lng = 74.0124;
        } else if (city_id.equals("197")) {
            lat = 15.2282;
            lng = 74.0647;
        } else if (city_id.equals("198")) {
            lat = 15.2302;
            lng = 74.1504;
        } else if (city_id.equals("199")) {
            lat = 15.5583;
            lng = 74.0124;
        } else if (city_id.equals("200")) {
            lat = 15.5300;
            lng = 74.1301;
        } else if (city_id.equals("201")) {
            lat = 23.0225;
            lng = 72.5714;
        } else if (city_id.equals("202")) {
            lat = 21.6032;
            lng = 71.2221;
        } else if (city_id.equals("203")) {
            lat = 22.5645;
            lng = 72.9289;
        } else if (city_id.equals("204")) {
            lat = 25.2235;
            lng = 73.7478;
        } else if (city_id.equals("205")) {
            lat = 24.3455;
            lng = 71.7622;
        } else if (city_id.equals("206")) {
            lat = 21.7051;
            lng = 72.9959;
        } else if (city_id.equals("207")) {
            lat = 21.7645;
            lng = 72.1519;
        } else if (city_id.equals("208")) {
            lat = 22.1704;
            lng = 71.6684;
        } else if (city_id.equals("209")) {
            lat = 22.3085;
            lng = 74.0120;
        } else if (city_id.equals("210")) {
            lat = 22.8379;
            lng = 74.2531;
        } else if (city_id.equals("211")) {
            lat = 20.8254;
            lng = 73.7007;
        } else if (city_id.equals("212")) {
            lat = 22.1232;
            lng = 69.3831;
        } else if (city_id.equals("213")) {
            lat = 23.2156;
            lng = 72.6369;
        } else if (city_id.equals("214")) {
            lat = 21.0119;
            lng = 70.7168;
        } else if (city_id.equals("215")) {
            lat = 22.4707;
            lng = 70.0577;
        } else if (city_id.equals("216")) {
            lat = 21.5222;
            lng = 70.4579;
        } else if (city_id.equals("217")) {
            lat = 23.7337;
            lng = 69.8597;
        } else if (city_id.equals("218")) {
            lat = 22.9251;
            lng = 72.9933;
        } else if (city_id.equals("219")) {
            lat = 23.1711;
            lng = 73.5594;
        } else if (city_id.equals("220")) {
            lat = 23.5880;
            lng = 72.3693;
        } else if (city_id.equals("221")) {
            lat = 22.8120;
            lng = 70.8236;
        } else if (city_id.equals("222")) {
            lat = 21.8757;
            lng = 73.5594;
        } else if (city_id.equals("223")) {
            lat = 20.9467;
            lng = 72.9520;
        } else if (city_id.equals("224")) {
            lat = 22.8011;
            lng = 73.5594;
        } else if (city_id.equals("225")) {
            lat = 23.8493;
            lng = 72.1266;
        } else if (city_id.equals("226")) {
            lat = 21.6417;
            lng = 69.6293;
        } else if (city_id.equals("227")) {
            lat = 22.3039;
            lng = 70.8022;
        } else if (city_id.equals("228")) {
            lat = 23.8477;
            lng = 72.9933;
        } else if (city_id.equals("229")) {
            lat = 21.1702;
            lng = 72.8311;
        } else if (city_id.equals("230")) {
            lat = 22.7739;
            lng = 71.6673;
        } else if (city_id.equals("231")) {
            lat = 21.0885;
            lng = 73.4487;
        } else if (city_id.equals("232")) {
            lat = 22.3072;
            lng = 73.1812;
        } else if (city_id.equals("233")) {
            lat = 20.5992;
            lng = 72.9342;
        } else if (city_id.equals("234")) {
            lat = 30.3782;
            lng = 76.7767;
        } else if (city_id.equals("235")) {
            lat = 28.7752;
            lng = 75.9928;
        } else if (city_id.equals("236")) {
            lat = 28.6924;
            lng = 76.9240;
        } else if (city_id.equals("237")) {
            lat = 28.4089;
            lng = 77.3178;
        } else if (city_id.equals("238")) {
            lat = 28.4595;
            lng = 77.0266;
        } else if (city_id.equals("239")) {
            lat = 29.1492;
            lng = 75.7217;
        } else if (city_id.equals("240")) {
            lat = 29.3211;
            lng = 76.3058;
        } else if (city_id.equals("241")) {
            lat = 29.7857;
            lng = 76.3985;
        } else if (city_id.equals("242")) {
            lat = 29.6857;
            lng = 76.9905;
        } else if (city_id.equals("243")) {
            lat = 29.3909;
            lng = 76.9635;
        } else if (city_id.equals("244")) {
            lat = 30.6942;
            lng = 76.8606;
        } else if (city_id.equals("245")) {
            lat = 28.1487;
            lng = 77.3320;
        } else if (city_id.equals("246")) {
            lat = 28.8955;
            lng = 76.6066;
        } else if (city_id.equals("247")) {
            lat = 28.1928;
            lng = 76.6239;
        } else if (city_id.equals("248")) {
            lat = 29.9696;
            lng = 76.8198;
        } else if (city_id.equals("249")) {
            lat = 30.1290;
            lng = 77.2674;
        } else if (city_id.equals("250")) {
            lat = 31.1521;
            lng = 76.9686;
        } else if (city_id.equals("251")) {
            lat = 30.9578;
            lng = 76.7914;
        } else if (city_id.equals("252")) {
            lat = 32.4725;
            lng = 75.9245;
        } else if (city_id.equals("253")) {
            lat = 31.6377;
            lng = 77.3441;
        } else if (city_id.equals("254")) {
            lat = 31.6098;
            lng = 76.5676;
        } else if (city_id.equals("255")) {
            lat = 31.8843;
            lng = 77.1456;
        } else if (city_id.equals("256")) {
            lat = 31.3407;
            lng = 76.6875;
        } else if (city_id.equals("257")) {
            lat = 32.5534;
            lng = 76.1258;
        } else if (city_id.equals("258")) {
            lat = 30.9479;
            lng = 77.5901;
        } else if (city_id.equals("259")) {
            lat = 32.4312;
            lng = 76.0131;
        }//Dagshai Cantonment lat long Himachal Pradesh
        else if (city_id.equals("260")) {
            lat = 30.8864;
            lng = 77.0521;
        } else if (city_id.equals("261")) {
            lat = 32.5387;
            lng = 75.9710;
        } else if (city_id.equals("262")) {
            lat = 32.5478;
            lng = 75.9590;
        } else if (city_id.equals("263")) {
            lat = 31.7747;
            lng = 75.9985;
        } else if (city_id.equals("264")) {
            lat = 31.8818;
            lng = 76.2146;
        } else if (city_id.equals("265")) {
            lat = 32.2190;
            lng = 76.3234;
        } else if (city_id.equals("266")) {
            lat = 31.6622;
            lng = 76.0595;
        } else if (city_id.equals("267")) {
            lat = 31.4491;
            lng = 76.7048;
        } else if (city_id.equals("268")) {
            lat = 31.6798;
            lng = 76.5026;
        } else if (city_id.equals("269")) {
            lat = 32.1433;
            lng = 75.6871;
        } else if (city_id.equals("270")) {
            lat = 31.8752;
            lng = 76.3203;
        } else if (city_id.equals("271")) {
            lat = 31.4972;
            lng = 77.7080;
        } else if (city_id.equals("272")) {
            lat = 31.9912;
            lng = 76.7899;
        } else if (city_id.equals("273")) {
            lat = 31.1117;
            lng = 77.6665;
        } else if (city_id.equals("274")) {
            lat = 31.1048;
            lng = 77.1126;
        } else if (city_id.equals("275")) {
            lat = 32.0998;
            lng = 76.2691;
        } else if (city_id.equals("276")) {
            lat = 30.8981;
            lng = 76.9462;
        } else if (city_id.equals("277")) {
            lat = 31.1172;
            lng = 77.5409;
        } else if (city_id.equals("278")) {
            lat = 31.8246;
            lng = 77.4702;
        } else if (city_id.equals("279")) {
            lat = 32.2396;
            lng = 77.1887;
        } else if (city_id.equals("280")) {
            lat = 31.5892;
            lng = 76.9182;
        } else if (city_id.equals("281")) {
            lat = 31.3819;
            lng = 76.3441;
        } else if (city_id.equals("282")) {
            lat = 31.7785;
            lng = 76.3445;
        } else if (city_id.equals("283")) {
            lat = 32.1054;
            lng = 76.3789;
        } else if (city_id.equals("284")) {
            lat = 30.5599;
            lng = 77.2955;
        } else if (city_id.equals("285")) {
            lat = 31.3064;
            lng = 76.5358;
        } else if (city_id.equals("286")) {
            lat = 27.8974;
            lng = 78.0880;
        } else if (city_id.equals("287")) {
            lat = 31.2578;
            lng = 77.4602;
        } else if (city_id.equals("288")) {
            lat = 32.3001;
            lng = 75.8853;
        } else if (city_id.equals("289")) {
            lat = 32.1109;
            lng = 76.5363;
        } else if (city_id.equals("290")) {
            lat = 30.4453;
            lng = 77.6021;
        } else if (city_id.equals("291")) {
            lat = 30.8372;
            lng = 76.9614;
        } else if (city_id.equals("292")) {
            lat = 23.8509;
            lng = 76.7337;
        } else if (city_id.equals("293")) {
            lat = 28.7893;
            lng = 79.0250;
        } else if (city_id.equals("294")) {
            lat = 31.6322;
            lng = 76.8332;
        } else if (city_id.equals("295")) {
            lat = 31.2046;
            lng = 77.7524;
        } else if (city_id.equals("296")) {
            lat = 30.9754;
            lng = 76.9902;
        } else if (city_id.equals("297")) {
            lat = 31.3562;
            lng = 76.3226;
        } else if (city_id.equals("298")) {
            lat = 31.6990;
            lng = 76.7324;
        } else if (city_id.equals("299")) {
            lat = 22.0869;
            lng = 79.5435;
        } else if (city_id.equals("300")) {
            lat = 31.8933;
            lng = 77.1384;
        } else if (city_id.equals("301")) {
            lat = 31.1048;
            lng = 77.1734;
        } else if (city_id.equals("302")) {
            lat = 30.9045;
            lng = 77.0967;
        } else if (city_id.equals("303")) {
            lat = 31.5332;
            lng = 76.8923;
        }//Talai latitude logitude himachal pradesh
        else if (city_id.equals("304")) {
            lat = 32.0842;
            lng = 77.5711;
        } else if (city_id.equals("305")) {
            lat = 31.1183;
            lng = 77.3597;
        } else if (city_id.equals("306")) {
            lat = 31.8339;
            lng = 76.5055;
        } else if (city_id.equals("307")) {
            lat = 31.4684;
            lng = 76.2708;
        } else if (city_id.equals("308")) {
            lat = 32.1648;
            lng = 76.1959;
        } else if (city_id.equals("309")) {
            lat = 32.8995;
            lng = 74.7425;
        } else if (city_id.equals("310")) {
            lat = 34.1595;
            lng = 74.3587;
        } else if (city_id.equals("311")) {
            //32.56 74.95
            lat = 32.7679;
            lng = 75.0323;
        } else if (city_id.equals("312")) {
            lat = 33.7782;
            lng = 76.5762;
        } else if (city_id.equals("313")) {
            lat = 33.2427;
            lng = 75.2392;
        } else if (city_id.equals("314")) {
            lat = 32.5773;
            lng = 75.0144;
        } else if (city_id.equals("315")) {
            lat = 23.6693;
            lng = 86.1511;
        } else if (city_id.equals("316")) {
            lat = 23.7479;
            lng = 86.7869;
        } else if (city_id.equals("317")) {
            lat = 24.2065;
            lng = 84.8724;
        } else if (city_id.equals("318")) {
            lat = 22.6765;
            lng = 85.6255;
        } else if (city_id.equals("319")) {
            lat = 22.5474;
            lng = 85.8025;
        } else if (city_id.equals("320")) {
            lat = 24.4763;
            lng = 86.6913;
        } else if (city_id.equals("321")) {
            lat = 23.7957;
            lng = 86.4304;
        } else if (city_id.equals("322")) {
            lat = 24.2855;
            lng = 87.2419;
        } else if (city_id.equals("323")) {
            lat = 23.0441;
            lng = 84.5379;
        } else if (city_id.equals("324")) {
            lat = 23.8081;
            lng = 85.8230;
        } else if (city_id.equals("325")) {
            lat = 24.8255;
            lng = 87.2135;
        } else if (city_id.equals("326")) {
            lat = 24.2841;
            lng = 86.0937;
        } else if (city_id.equals("327")) {
            lat = 24.1549;
            lng = 83.7996;
        } else if (city_id.equals("328")) {
            lat = 23.9966;
            lng = 85.3691;
        } else if (city_id.equals("329")) {
            lat = 22.8046;
            lng = 86.2029;
        } else if (city_id.equals("330")) {
            lat = 24.4289;
            lng = 85.5355;
        } else if (city_id.equals("331")) {
            lat = 23.4338;
            lng = 84.6479;
        } else if (city_id.equals("332")) {
            lat = 24.2654;
            lng = 86.6480;
        } else if (city_id.equals("333")) {
            lat = 24.0420;
            lng = 84.0907;
        } else if (city_id.equals("334")) {
            lat = 24.6337;
            lng = 87.8501;
        } else if (city_id.equals("335")) {
            lat = 23.7623;
            lng = 86.0021;
        } else if (city_id.equals("336")) {
            lat = 23.6524;
            lng = 85.5612;
        } else if (city_id.equals("337")) {
            lat = 23.3441;
            lng = 85.3096;
        } else if (city_id.equals("338")) {
            lat = 24.9802;
            lng = 87.6186;
        } else if (city_id.equals("339")) {
            lat = 23.6586;
            lng = 85.3445;
        } else if (city_id.equals("340")) {
            lat = 15.8497;
            lng = 74.4977;
        } else if (city_id.equals("341")) {
            lat = 13.8330;
            lng = 75.7081;
        } else if (city_id.equals("342")) {
            lat = 15.1394;
            lng = 76.9214;
        } else if (city_id.equals("343")) {
            lat = 17.9149;
            lng = 77.5046;
        } else if (city_id.equals("344")) {
            lat = 15.9186;
            lng = 75.6761;
        } else if (city_id.equals("345")) {
            lat = 16.8302;
            lng = 75.7100;
        } else if (city_id.equals("346")) {
            lat = 17.8721;
            lng = 76.9470;
        } else if (city_id.equals("347")) {
            lat = 13.1623;
            lng = 75.8679;
        } else if (city_id.equals("348")) {
            lat = 17.8721;
            lng = 76.9470;
        } else if (city_id.equals("349")) {
            lat = 14.1823;
            lng = 76.5488;
        } else if (city_id.equals("350")) {
            lat = 12.6492;
            lng = 77.2003;
        } else if (city_id.equals("351")) {
            lat = 14.4663;
            lng = 75.9238;
        } else if (city_id.equals("352")) {
            lat = 15.4589;
            lng = 75.0078;
        } else if (city_id.equals("353")) {
            lat = 13.2417;
            lng = 77.7137;
        } else if (city_id.equals("354")) {
            lat = 17.3297;
            lng = 76.8343;
        } else if (city_id.equals("355")) {
            lat = 15.4319;
            lng = 76.5315;
        } else if (city_id.equals("356")) {
            lat = 15.4325;
            lng = 75.6380;
        } else if (city_id.equals("357")) {
            lat = 15.4026;
            lng = 75.6208;
        } else if (city_id.equals("358")) {
            lat = 13.0068;
            lng = 76.0996;
        } else if (city_id.equals("359")) {
            lat = 15.3350;
            lng = 76.4600;
        } else if (city_id.equals("360")) {
            lat = 15.2689;
            lng = 76.3909;
        } else if (city_id.equals("361")) {
            lat = 15.3647;
            lng = 75.1240;
        } else if (city_id.equals("362")) {
            lat = 14.5305;
            lng = 75.8011;
        } else if (city_id.equals("363")) {
            lat = 13.0730;
            lng = 77.7967;
        } else if (city_id.equals("364")) {
            lat = 13.2130;
            lng = 75.9942;
        } else if (city_id.equals("365")) {
            lat = 15.9563;
            lng = 76.1146;
        } else if (city_id.equals("366")) {
            lat = 12.9585;
            lng = 78.2710;
        } else if (city_id.equals("367")) {
            lat = 15.6219;
            lng = 76.1784;
        } else if (city_id.equals("368")) {
            lat = 13.1770;
            lng = 78.2020;
        } else if (city_id.equals("369")) {
            lat = 13.6316;
            lng = 74.6900;
        } else if (city_id.equals("370")) {
            lat = 14.8185;
            lng = 74.1416;
        } else if (city_id.equals("371")) {
            lat = 13.3605;
            lng = 74.7864;
        } else if (city_id.equals("372")) {
            lat = 12.9141;
            lng = 74.8560;
        } else if (city_id.equals("373")) {
            lat = 12.4244;
            lng = 75.7382;
        } else if (city_id.equals("374")) {
            lat = 12.2958;
            lng = 76.6394;
        } else if (city_id.equals("375")) {
            lat = 15.9488;
            lng = 75.8164;
        } else if (city_id.equals("376")) {
            lat = 12.9551;
            lng = 78.2699;
        } else if (city_id.equals("377")) {
            lat = 16.2120;
            lng = 77.3439;
        } else if (city_id.equals("378")) {
            lat = 14.6113;
            lng = 75.6383;
        } else if (city_id.equals("379")) {
            lat = 13.9299;
            lng = 75.5681;
        } else if (city_id.equals("380")) {
            lat = 15.0874;
            lng = 76.5477;
        } else if (city_id.equals("381")) {
            lat = 13.4198;
            lng = 75.2567;
        } else if (city_id.equals("382")) {
            lat = 12.4216;
            lng = 76.6931;
        } else if (city_id.equals("383")) {
            lat = 13.3392;
            lng = 77.1140;
        } else if (city_id.equals("384")) {
            lat = 13.3409;
            lng = 74.7421;
        } else if (city_id.equals("385")) {
            lat = 14.7937;
            lng = 74.6869;
        } else if (city_id.equals("386")) {
            lat = 16.7602;
            lng = 77.1428;
        } else if (city_id.equals("387")) {
            lat = 19.8762;
            lng = 75.3433;
        } else if (city_id.equals("388")) {
            lat = 20.7059;
            lng = 77.0219;
        } else if (city_id.equals("389")) {
            lat = 20.9042;
            lng = 74.7749;
        } else if (city_id.equals("390")) {
            lat = 21.0077;
            lng = 75.5626;
        } else if (city_id.equals("391")) {
            lat = 16.7050;
            lng = 74.2433;
        } else if (city_id.equals("392")) {
            lat = 20.5547;
            lng = 74.5100;
        } else if (city_id.equals("393")) {
            lat = 19.0760;
            lng = 72.8777;
        } else if (city_id.equals("394")) {
            lat = 21.1458;
            lng = 79.0882;
        } else if (city_id.equals("395")) {
            lat = 19.9975;
            lng = 73.7898;
        } else if (city_id.equals("396")) {
            lat = 17.6599;
            lng = 75.9064;
        } else if (city_id.equals("397")) {
            lat = 31.6340;
            lng = 74.8723;
        } else if (city_id.equals("398")) {
            lat = 30.2110;
            lng = 74.9455;
        } else if (city_id.equals("399")) {
            lat = 30.3819;
            lng = 75.5468;
        } else if (city_id.equals("400")) {
            lat = 30.9331;
            lng = 74.6225;
        } else if (city_id.equals("401")) {
            lat = 31.5143;
            lng = 75.9115;
        } else if (city_id.equals("402")) {
            lat = 31.3260;
            lng = 75.5762;
        } else if (city_id.equals("403")) {
            lat = 30.9010;
            lng = 75.8573;
        } else if (city_id.equals("404")) {
            lat = 30.7046;
            lng = 76.7179;
        } else if (city_id.equals("405")) {
            lat = 30.3398;
            lng = 76.3869;
        } else if (city_id.equals("406")) {
            lat = 32.2643;
            lng = 75.6421;
        } else if (city_id.equals("407")) {
            lat = 27.1767;
            lng = 78.0081;
        } else if (city_id.equals("408")) {
            lat = 25.4358;
            lng = 81.8463;
        } else if (city_id.equals("409")) {
            lat = 26.7880;
            lng = 82.1986;
        } else if (city_id.equals("410")) {
            lat = 25.8500;
            lng = 80.8987;
        } else if (city_id.equals("411")) {
            lat = 25.8500;
            lng = 80.8987;
        } else if (city_id.equals("412")) {
            lat = 26.7347;
            lng = 83.3362;
        } else if (city_id.equals("413")) {
            lat = 25.4484;
            lng = 78.5685;
        } else if (city_id.equals("414")) {
            lat = 26.4499;
            lng = 80.3319;
        } else if (city_id.equals("415")) {
            lat = 26.8467;
            lng = 80.9462;
        } else if (city_id.equals("416")) {
            lat = 28.9845;
            lng = 77.7064;
        } else if (city_id.equals("417")) {
            lat = 25.3176;
            lng = 82.9739;
        } else if (city_id.equals("418")) {
            lat = 22.5726;
            lng = 88.3639;
        } else if (city_id.equals("419")) {
            lat = 30.7333;
            lng = 76.7794;
        } else if (city_id.equals("420")) {
            lat = 13.0827;
            lng = 80.2707;
        } else if (city_id.equals("421")) {
            lat = 11.0168;
            lng = 76.9558;
        } else if (city_id.equals("422")) {
            lat = 9.9252;
            lng = 78.1198;
        } else if (city_id.equals("423")) {
            lat = 11.4064;
            lng = 76.6932;
        } else if (city_id.equals("424")) {
            lat = 9.2876;
            lng = 79.3129;
        } else if (city_id.equals("425")) {
            lat = 10.7905;
            lng = 78.7047;
        } else if (city_id.equals("426")) {
            lat = 11.1085;
            lng = 77.3411;
        }

    }


    public void setUpMap() {
        if (myPosition != null) {
            myPosition = null;
        }
        if (myPosition == null) {
            convertAddress(city + ",India");
            myPosition = mMap.addMarker(new MarkerOptions().title("").snippet("")
                    .position(new LatLng(lat, lng)));

            cameraPosition = new CameraPosition.Builder()
                    .target(myPosition.getPosition())
                    .zoom(13) // Set your preferred zoom level here
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            //cameraPosition = new CameraPosition.Builder().target(myPosition.getPosition()).zoom(12).build();
            //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            new AddMarker(myPosition, R.drawable.pin/*"1200rs"*/, this);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

               /* AlertDialogWithTwoCallback dialog = new AlertDialogWithTwoCallback() {
                    @Override
                    public void callBack() {
                        Intent intent = new Intent();
                        intent.putExtra("lat", "" + latLng.latitude);
                        intent.putExtra("lng", "" + latLng.longitude);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void cancelCallBack() {

                    }
                };

*/
                CustomDialogClass VehicleRcId = new CustomDialogClass(MapViewActivity.this, R.style.full_screen_dialog);
                VehicleRcId.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                VehicleRcId.getPerameter(MapViewActivity.this, "Location selection",
                        "Do you want make your selected location as your property's location?", latLng);
                VehicleRcId.show();

               /* dialog.getPerameter(MapViewActivity.this, "Location selection",
                        "Do you want make your selected location as your property's location?");
                dialog.show(getSupportFragmentManager(), "login alert");*/

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }


    // check whether gps is enabled
    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();
            enableLoc();

            return true;
        } else {

            return false;
        }

    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(MapViewActivity.this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {


                        }
                    }).build();
            googleApiClient.connect();

            // LocationRequest locationRequest = LocationRequest.create();
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
                    .setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {


                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        (Activity) MapViewActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    //new code changes b/c alert dialog not working show animites by ashish 20-02-2019


    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private TextView message;
        private Button alertYesBtn;
        private Button alertNoBtn;
        private String titleStr;
        private String messageStr;
        private LatLng latLng;


        public CustomDialogClass(MapViewActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
            this.mFActivity = a;
        }


        public void getPerameter(FragmentActivity activity, String titleStr, String messageStr, LatLng latLng) {
            mFActivity = activity;
            this.titleStr = titleStr;
            this.messageStr = messageStr;
            this.latLng = latLng;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.alert_two_btn_dialog);
            //c.setTheme(R.style.CustomDialog);

            mFActivity.setTheme(R.style.CustomDialog);
            //c.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
            createView();
            setListner();


        }

        public void createView() {
            title = (TextView) findViewById(R.id.alertTitle);
            message = (TextView) findViewById(R.id.categoryNameInput);
            alertYesBtn = (Button) findViewById(R.id.alertYesBtn);
            alertNoBtn = (Button) findViewById(R.id.alertNoBtn);

            title.setText(titleStr);
            message.setText(messageStr);
        }

        public void setListner() {
            alertYesBtn.setOnClickListener(this);
            alertNoBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alertYesBtn:
                    Intent intent = new Intent();
                    intent.putExtra("lat", "" + latLng.latitude);
                    intent.putExtra("lng", "" + latLng.longitude);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    dismiss();
                    break;
                case R.id.alertNoBtn:
                    dismiss();
                    break;
                default:
                    break;
            }
            //dismiss();
        }
    }

    //end

}