package com.krooms.hostel.rental.property.app.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.activity.PropertyRCUActivity;
import com.krooms.hostel.rental.property.app.modal.FeatureListModal;
import com.krooms.hostel.rental.property.app.modal.RCUDetailModal;
import com.krooms.hostel.rental.property.app.modal.RoomBedModal;
import com.krooms.hostel.rental.property.app.callback.TwoCallBackMathod;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class Common {

    public static final int GET = 1;
    public static final int POST = 2;
    public static boolean _isAPP_FORGROUND = false;
    public static String DEVICE_TOKEN = "";
    public static String DEVICE_TYPE = "Android";
    public static String WEB_OR_MOBILE = "M";
    public static String profileImagePath = null;
    public static String TimeOut_Message = "Due to low network connection, your request timeout. Please try again.";

    public static Boolean _isFragmnet_Finish = false;
   public static String reciveOTPCode = "";


    private String[] featureList = {
            "Power Backup",
            "Security / Fire Alarm",
            "Intercom Facility",
            "RO + UV Water Purifier",
            "Lift(s)",
            "A.C.",
            "Cooler",
            "Water Gyser",
            "Dining Table",
            "Sofa Set",
            "Wardrob",
            "Tea Table",
            "Fan",
            "Modular Kitchen",
            "Fridge",
            "Washing Machine",
            "Cctv Camera",
            "L.E.D Screen",
            "Disc Connection",
            "Wi-Fi",
            "Bed and Mattresses Set",
            "Computer Table",
            "Chairs",
            "Fire Exit",
            "Inter Comm",
            "Conference Hall",
            "Projector",
            "Reception",
            "Piped Gas",
            "Water Storage",
            "Internet",
            "Visitor Parking",
            "Swimming Pool",
            "Security Personnel",
            "Park",
            "Maintenance Staff",
            "Fitness Centre / GYM",
            "Watter Softening Plant",
            "Club House / Community Center",
            "Shopping Centre",
            "Security",
            "Rainwater Harvesting",
            "Maintenance",
            "Intercom",
            "Garden",
            "Community Hall",
            "Almerah",
            "Bed",
            "Mess",
            "Table",
            "Chair",
            "Tube-light",
            "Study Table",
            "Solar Geezer",
            "Water Cooler",
            "Parking 2 Wheeler",
            "Parking 4 Wheeler",
            "Food- Breakfast",
            "Lunch",
            "Dinner",
            "Attendance Machine",
            "Lunch Box",
            "Indoor Games",
            "Outdoor Games",
            "First Aid",
            "Doctor on Call",
            "ATM",
            "Transport Facility",
            "House Keeping",
            "Fax and Photocopy",
            "Parents Accommodation",
            "Temple",
            "Library",
            "Cafeteria",
            "Night Lamp",
            "Dustbin",
            "DVD Hooked up to T.V.",
            "Laundry Services",
            "Toaster",
            "Tea Kettle"
    };

    private int[] featureiconList = {
            R.drawable.power_backup,
            R.drawable.fire_alarm,
            R.drawable.intercom_facility,
            R.drawable.water_purifier,
            R.drawable.lift,
            R.drawable.ac,
            R.drawable.cooler,
            R.drawable.water_geyser,
            R.drawable.dining_table,
            R.drawable.sofa_set,
            R.drawable.wardrobe,
            R.drawable.tea_table,
            R.drawable.fan,
            R.drawable.modular_kitchen,
            R.drawable.fridge,
            R.drawable.washing_machine,
            R.drawable.cctv_camera,
            R.drawable.tv,
            R.drawable.disc_connection,
            R.drawable.wifi,
            R.drawable.bed_sheets,
            R.drawable.computer_table,
            R.drawable.chairs,
            R.drawable.fire_exit,
            R.drawable.inter_comm,
            R.drawable.conference_hall,
            R.drawable.projector,
            R.drawable.reception,
            R.drawable.piped_gas,
            R.drawable.water_storage,
            R.drawable.internet,
            R.drawable.visitor_parking,
            R.drawable.swimming_pool,
            R.drawable.security_personnel,
            R.drawable.park,
            R.drawable.maintenance_staff,
            R.drawable.fitness_center_gym,
            R.drawable.water_softening_plant,
            R.drawable.commonity_center,
            R.drawable.shopping_center,
            R.drawable.security,
            R.drawable.rainwater_harvesting,
            R.drawable.maintenance_staff,
            R.drawable.inter_comm,
            R.drawable.garden,
            R.drawable.commonity_center,
            R.drawable.fridge,
            R.drawable.bed_sheets,
            R.drawable.dining_table,
            R.drawable.table,
            R.drawable.chairs,
            R.drawable.tube_light,
            R.drawable.study_table,
            R.drawable.solar_geezer,
            R.drawable.water_cooler,
            R.drawable.parking_2_wheeler,
            R.drawable.parking_4_wheeler,
            R.drawable.lunch_box,
            R.drawable.lunch,
            R.drawable.dinner,
            R.drawable.attendance_machine,
            R.drawable.lunch_box,
            R.drawable.indoor_games,
            R.drawable.outdoor_games,
            R.drawable.first_aid,
            R.drawable.doctor_on_call,
            R.drawable.atm,
            R.drawable.transport_facility,
            R.drawable.house_keeping,
            R.drawable.fax_and_photocopy,
            R.drawable.parents_accommodation,
            R.drawable.temple,
            R.drawable.library,
            R.drawable.cafeteria,
            R.drawable.night_lamp,
            R.drawable.dustbin,
            R.drawable.dvd_hooked,
            R.drawable.laundry_services,
            R.drawable.toaster,
            R.drawable.tea_kettle
    };


    public ArrayList<FeatureListModal> feartueSetUpMethod() {
        ArrayList<FeatureListModal> completeFeatureList = new ArrayList<>();
        for (int i = 0; i < featureList.length; i++) {
            FeatureListModal modal = new FeatureListModal();
            modal.setFeatureTitle(featureList[i]);
            modal.setFeatureIcon(featureiconList[i]);
            completeFeatureList.add(modal);
        }
        return completeFeatureList;
    }


    //public static String Notification_Count = "";
    public static int timeoutConnection = 60 * 1000;//30 second
    public static int timeoutSocket = 60 * 1000;//30 second

    public static ArrayList<RCUDetailModal> rcuDetails = new ArrayList<>();

    public final static int CAMERA_CAPTURE_REQUEST = 101;
    public final static int CAMERA_CAPTURE_REQUEST_EDIT = 111;
    public final static int CAMERA_CAPTURE_REQUEST_IDPROOF = 1001;
    public final static int SHARE_REQUEST = 110;
    public final static int PIC_CROP_REQUEST = 103;
    public final static int GALLERY_REQUEST = 102;

    public static int selectedIndex, selectedSubCategoryIndex;
    public static int subcategoryPagerIndex = 0;
    public static String DropBoxFile;
    public static int pagerViewIndexSelection = 0;

    public static ArrayList<RoomBedModal> selectedBedInfo = new ArrayList<>();
    public static ArrayList<PropertyModal> CommonPropertList = new ArrayList<PropertyModal>();
    public static ArrayList<PropertyModal> CommonPropertListViewMore = new ArrayList<PropertyModal>();
    public static boolean isDatabaseSyncingInProgress = false;
    public static boolean _isDatabaseSyncingFromBackground = false;
//    public static boolean _isDatabaseSyncingActiveted = false;


    public static boolean searchResultTypeTitleShown = false;


    public static int getDeviceResolution(Activity mActivity) {

        WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static void hideKeybord(Activity mActivity, View view) {
        mActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static Location getMyCurrentLocation(Context context) {

        GPSTracker tracker = new GPSTracker(context);
        return tracker.getLocation();


    }


    public static boolean isAlertVisible = false;

    public void displayAlert(final Activity mActivity, String message, final Boolean action) {

        final Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Panel);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_with_callback);
        dialog.setCancelable(false);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wmlp);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        txtTitle.setText("Alert!");

        TextView txtSms = (TextView) dialog.findViewById(R.id.categoryNameInput);
        txtSms.setText(message);

        Button btnOk = (Button) dialog.findViewById(R.id.alertOkBtn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (action) {
                    mActivity.finish();
                }
                dialog.dismiss();
                isAlertVisible = false;
            }
        });

        if (!dialog.isShowing() && !isAlertVisible) {
            dialog.show();
            isAlertVisible = true;
        }
    }

    public static void loadImage(final Context context, final String imagePath, final ImageView view) {

        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.ic_default_background)
                .error(R.drawable.ic_default_background)
                .noFade().resize(500, 500)
                .into(view);

    }

    public static void loadCirculerImage(final Activity context, final String imagePath, final ImageView view) {

        Picasso.with(context)
                .load(imagePath)
                .placeholder(R.drawable.ic_default_background)
                .error(R.drawable.ic_default_background)
                .transform(new RoundedTransformation(context))
                .noFade().resize(500, 500)
                .into(view);

    }


    public void displayAlertWithOption(final Activity mActivity, String message, String btnFirstTitle, String btnSecondTitle, final TwoCallBackMathod callBack) {


        final Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Panel);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_two_btn_dialog);
        dialog.setCancelable(false);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wmlp);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        txtTitle.setText("Message!");

        TextView txtMessage = (TextView) dialog.findViewById(R.id.categoryNameInput);
        txtMessage.setText(message);

        if (!dialog.isShowing())
            dialog.show();

        Button btnYes = (Button) dialog.findViewById(R.id.alertYesBtn);
        btnYes.setText(btnFirstTitle);
        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callBack.firstBtnClick();
                dialog.dismiss();
            }
        });

        Button btnNo = (Button) dialog.findViewById(R.id.alertNoBtn);
        btnNo.setText(btnSecondTitle);
        btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callBack.secondBtnClick();
                dialog.dismiss();
            }
        });


    }

    public static void Config(String s) {
        System.out.println(s);
    }


    @SuppressLint("InlinedApi")
    public static void displayAlertExitApp(final Activity mActivity, String title, String message, boolean action) {

        final Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Panel);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_two_btn_dialog);
        dialog.setCancelable(false);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wmlp);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        txtTitle.setText(title);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.categoryNameInput);
        txtMessage.setText(message);

        if (!dialog.isShowing())
            dialog.show();

        Button btnYes = (Button) dialog.findViewById(R.id.alertYesBtn);
        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mActivity.finish();
                dialog.dismiss();
            }
        });

        Button btnNo = (Button) dialog.findViewById(R.id.alertNoBtn);
        btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    public String getDeviceId(Activity mActivity) {
        String android_id = "";
        android_id = Secure.getString(mActivity.getContentResolver(), Secure.ANDROID_ID);
        return android_id;
    }

    @SuppressWarnings("deprecation")
    public String getDeviceResolution(Context mActivity) {
        Display display = ((Activity) mActivity).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();  // deprecated

        return "" + width;
    }

    public static void startLoginAction(Context context, String action) {

        Intent loginAction = new Intent("com.business.LOGIN_ACTION");
        loginAction.putExtra("action", action);
        context.sendBroadcast(loginAction);

    }

    public static void exitAppAction(Context context) {

        Intent loginAction = new Intent("com.business.EXIT_ACTION");
        context.sendBroadcast(loginAction);

    }

    public static void startSignUpAction(Context context) {

        Intent loginAction = new Intent("com.business.SIGNUP_ACTION");
        context.sendBroadcast(loginAction);

    }

    public void displayAlertFinishFragment(final FragmentActivity mActivity, String message) {

        final Dialog dialog = new Dialog(mActivity, android.R.style.Theme_Panel);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_with_callback);
        dialog.setCancelable(false);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wmlp);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        txtTitle.setText("Alert!");

        TextView txtSms = (TextView) dialog.findViewById(R.id.categoryNameInput);
        txtSms.setText(message);

        Button btnOk = (Button) dialog.findViewById(R.id.alertOkBtn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mActivity.getFragmentManager().getBackStackEntryCount() > 0) {
                    mActivity.getFragmentManager().popBackStack();
                } else {

                }
                dialog.dismiss();
            }
        });

        if (!dialog.isShowing())
            dialog.show();
    }


    public static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }


    public String downloadImageAndSave(String Url, String fileName) {

        String filepath = "";
        try {
            URL url = new URL(Url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
//            String filename="downloadedFile.png";
            Log.i("Local filename:", "" + fileName);

            File folder = new File(SDCardRoot, "Krooms");
            folder.mkdir();
            File file = new File(folder, fileName);
            if (file.createNewFile()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
            }
            fileOutput.close();
            if (downloadedSize == totalSize) filepath = file.getPath();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            filepath = null;
        }
        Log.i("filepath:", " " + filepath);
        return filepath;

    }


}
