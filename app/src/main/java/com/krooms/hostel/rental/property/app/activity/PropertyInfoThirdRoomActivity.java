package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.RoomNoAddAdapter;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.dialog.AlertDialogWithTwoCallback;
import com.krooms.hostel.rental.property.app.modal.RoomModal;
import com.krooms.hostel.rental.property.app.modal.RoomNoMdelNew;
import com.krooms.hostel.rental.property.app.modal.RoomViewsModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.RoomServiceResponce;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.asynctask.AddPropertyRoomsInFoAsynctask;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.Validation;
//import DataBaseAdapter;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PropertyInfoThirdRoomActivity extends AppCompatActivity implements View.OnClickListener, ServiceResponce, RoomServiceResponce {
    String result = null;
    private Spinner spinnerNoofRooms;
    private Spinner spinnerForWhom;
    private TextView txtBack;
    private Button btnNext, btnAddMore,createrooms;
    private LinearLayout mprogressBar;
    private ArrayList<StateModal> mArrayForWhom;
    private ArrayList<RoomViewsModal> mEditTextArrayList;
    private LinearLayout linearLayoutRooms;
    private ArrayList<RoomModal> mArrayRooms = new ArrayList<>();
    Handler handler;
    private ArrayList<RoomNoMdelNew> mArrayNoOfRooms;
    RoomNoAddAdapter mNoOfRoomsSpinnerAdapter;
    private StateSpinnerAdapter  mForWhomSpinnerAdapter;
    private int roomCount;
    private Button btnBrowseImgRoom;
    private String propertyId = "";
    private Validation mValidation;
    private Common mCommon;
    private SharedStorage mSharedStorage;
    public String flag;
    EditText no_of_rooms_selection_edit;
    private Boolean _isSetEditData = true, isSkipOption = false;
    public ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info_third_room);
        isSkipOption = getIntent().getBooleanExtra("isSkipOption", false);
        propertyId = getIntent().getStringExtra("PropertyId");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
            }else {
                requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }
        }

        flag = getIntent().getExtras().getString("flag");
        if (flag == null) {
            flag = "";
        }

        createViews();
        setListners(savedInstanceState);
        if (savedInstanceState != null) {
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            setDataOnSavedState(savedInstanceState);
            setDataIfHaving1();
        } else {

            setDataIfHaving();
        }

    }

    public void setDataIfHaving() {

        if (flag.equals("Edit")) {
            roomCount = Integer.parseInt(PropertyModal.getInstance().getNo_of_rooms());
           // spinnerNoofRooms.setSelection(roomCount);
            String value= String.valueOf(roomCount);
            if(value.equals("0"))
            {

                btnAddMore.setVisibility(View.VISIBLE);
                spinnerNoofRooms.setEnabled(false);
                roomCount = Integer.parseInt(PropertyModal.getInstance().getNo_of_rooms());
               // spinnerNoofRooms.setSelection(roomCount);

                no_of_rooms_selection_edit.setText("");

                getRoomInformation();
                if (PropertyModal.getInstance().getProperty_for_whom().equals("Boys")) {
                    spinnerForWhom.setSelection(1);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Girls")) {
                    spinnerForWhom.setSelection(2);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Family")) {
                    spinnerForWhom.setSelection(3);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Businessman")) {
                    spinnerForWhom.setSelection(4);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Employee")) {
                    spinnerForWhom.setSelection(5);
                } else {
                    spinnerForWhom.setSelection(0);
                }


                getDataIfHavingLocalDataBase();

            }else {

                btnAddMore.setVisibility(View.VISIBLE);
                no_of_rooms_selection_edit.setEnabled(false);
                createrooms.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
                spinnerNoofRooms.setEnabled(false);
                roomCount = Integer.parseInt(PropertyModal.getInstance().getNo_of_rooms());
                spinnerNoofRooms.setSelection(roomCount);

                no_of_rooms_selection_edit.setText(value);

                getRoomInformation();
                if (PropertyModal.getInstance().getProperty_for_whom().equals("Boys")) {
                    spinnerForWhom.setSelection(1);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Girls")) {
                    spinnerForWhom.setSelection(2);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Family")) {
                    spinnerForWhom.setSelection(3);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Businessman")) {
                    spinnerForWhom.setSelection(4);
                } else if (PropertyModal.getInstance().getProperty_for_whom().equals("Employee")) {
                    spinnerForWhom.setSelection(5);
                } else {
                    spinnerForWhom.setSelection(0);
                }


                getDataIfHavingLocalDataBase();
            }
        }
    }

    public void setDataIfHaving1() {


        if (flag.equals("Edit")) {
            btnAddMore.setVisibility(View.VISIBLE);
            spinnerNoofRooms.setEnabled(false);
            spinnerNoofRooms.setSelection(roomCount);
            no_of_rooms_selection_edit.setEnabled(false);
            createrooms.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
          //  no_of_rooms_selection_edit.setText(roomCount);

            getRoomInformation1();
            if (roomForWhomGlobal.equals("Boys")) {
                spinnerForWhom.setSelection(1);
            } else if (roomForWhomGlobal.equals("Girls")) {
                spinnerForWhom.setSelection(2);
            } else if (roomForWhomGlobal.equals("Family")) {
                spinnerForWhom.setSelection(3);
            } else if (roomForWhomGlobal.equals("Businessman")) {
                spinnerForWhom.setSelection(4);
            } else if (roomForWhomGlobal.equals("Employee")) {
                spinnerForWhom.setSelection(5);
            } else {
                spinnerForWhom.setSelection(0);
            }
            getDataIfHavingLocalDataBase();
        } else {
            spinnerNoofRooms.setSelection(roomCount);
            getRoomInformation1();
            if (roomForWhomGlobal.equals("Boys")) {
                spinnerForWhom.setSelection(1);
            } else if (roomForWhomGlobal.equals("Girls")) {
                spinnerForWhom.setSelection(2);
            } else if (roomForWhomGlobal.equals("Family")) {
                spinnerForWhom.setSelection(3);
            } else if (roomForWhomGlobal.equals("Businessman")) {
                spinnerForWhom.setSelection(4);
            } else if (roomForWhomGlobal.equals("Employee")) {
                spinnerForWhom.setSelection(5);
            } else {
                spinnerForWhom.setSelection(0);
            }
//            getDataIfHavingLocalDataBase();
        }
    }

    public void getDataIfHavingLocalDataBase() {

        if (flag.equals("Edit")) {
            mArrayRooms  = new ArrayList<>();
            mArrayRooms = PropertyModal.getInstance().getRoomList();




        }
    }

    public void setDataIfHavingRooms() {

        if(flag.equals("Edit")) {

            for(int i = 0; i < mArrayRooms.size(); i++) {

                try {
                    mEditTextArrayList.get(i).setStrRoomId(mArrayRooms.get(i).getRoomId());
                    mEditTextArrayList.get(i).getEdtRoomNo().setText("" + mArrayRooms.get(i).getRoomNo());
                    try {
                        if (mArrayRooms.get(i).getRoomType().equals("Bed") || mArrayRooms.get(i).getRoomType().equals("")) {
                            mEditTextArrayList.get(i).getSpinnerNoofRoom().setSelection(0);
                        } else {
                            mEditTextArrayList.get(i).getSpinnerNoofRoom().setSelection(
                                    Integer.parseInt(mArrayRooms.get(i).getRoomType()));
                        }

                        if (mArrayRooms.get(i).getVacantBed().equals("Vacant Bed") || mArrayRooms.get(i).getVacantBed().equals("") || mArrayRooms.get(i).getRoomType().equals("0")) {
                            mEditTextArrayList.get(i).getSpinnerVacantBad().setSelection(0);
                        } else {
                            mEditTextArrayList.get(i).getSpinnerVacantBad().setSelection(1 +
                                    Integer.parseInt(mArrayRooms.get(i).getVacantBed()));
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }

                    if (mArrayRooms.get(i).getLat_bath().equals("Lat-Bath")) {
                        mEditTextArrayList.get(i).getSpinnerlatbath().setSelection(0);
                    } else if (mArrayRooms.get(i).getLat_bath().equals("Yes")) {
                        mEditTextArrayList.get(i).getSpinnerlatbath().setSelection(1);
                    } else {
                        mEditTextArrayList.get(i).getSpinnerlatbath().setSelection(2);
                    }

                    if (mArrayRooms.get(i).getRoomImage() != null && mArrayRooms.get(i).getRoomImage().length() > 0) {
                        mEditTextArrayList.get(i).getBtnBrowse().setText(mArrayRooms.get(i).getRoomImage());
                    }

                    if (mArrayRooms.get(i).getBedPriceAmount() != null && mArrayRooms.get(i).getBedPriceAmount().length() > 0) {
                        mEditTextArrayList.get(i).getEdtRoomAmount().setText(mArrayRooms.get(i).getBedPriceAmount());
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();

                }
            }
        }

    }

    public void setDataIfHavingRooms1() {

        for (int i = 0; i < mArrayRooms.size(); i++) {

            try {
                mEditTextArrayList.get(i).setStrRoomId(mArrayRooms.get(i).getRoomId());
                mEditTextArrayList.get(i).getEdtRoomNo().setText("" + mArrayRooms.get(i).getRoomNo());

                if (mArrayRooms.get(i).getRoomType().equals("Bed")) {
                    mEditTextArrayList.get(i).getSpinnerNoofRoom().setSelection(0);
                } else if (mArrayRooms.get(i).getRoomType().equals("0")) {
                    mEditTextArrayList.get(i).getSpinnerNoofRoom().setSelection(0);
                } else {
                    mEditTextArrayList.get(i).getSpinnerNoofRoom().setSelection(
                            Integer.parseInt(mArrayRooms.get(i).getRoomType()));
                }

                if (mArrayRooms.get(i).getVacantBed().equals("Vacant Bed")) {
                    mEditTextArrayList.get(i).getSpinnerVacantBad().setSelection(0);
                } else {
                    mEditTextArrayList.get(i).getSpinnerVacantBad().setSelection(1 +
                            Integer.parseInt(mArrayRooms.get(i).getVacantBed()));
                }

                if (mArrayRooms.get(i).getLat_bath().equals("Lat-Bath")) {
                    mEditTextArrayList.get(i).getSpinnerlatbath().setSelection(0);
                } else if (mArrayRooms.get(i).getLat_bath().equals("Yes")) {
                    mEditTextArrayList.get(i).getSpinnerlatbath().setSelection(1);
                } else {
                    mEditTextArrayList.get(i).getSpinnerlatbath().setSelection(2);
                }

                if (mArrayRooms.get(i).getRoomImage() != null && mArrayRooms.get(i).getRoomImage().length() > 0) {
                    mEditTextArrayList.get(i).getBtnBrowse().setText(mArrayRooms.get(i).getRoomImage());
                }

                if (mArrayRooms.get(i).getBedPriceAmount() != null && mArrayRooms.get(i).getBedPriceAmount().length() > 0) {
                    mEditTextArrayList.get(i).getEdtRoomAmount().setText(mArrayRooms.get(i).getBedPriceAmount());
                }
            } catch (IndexOutOfBoundsException e) {

            }
        }

    }


    private void createViews() {

        no_of_rooms_selection_edit=(EditText)findViewById(R.id.no_of_rooms_selection_edit);
        createrooms=(Button)findViewById(R.id.createrooms);

        spinnerNoofRooms = (Spinner) findViewById(R.id.no_of_rooms_selection);
        spinnerForWhom = (Spinner) findViewById(R.id.for_whom_selection);

        mprogressBar = (LinearLayout) findViewById(R.id.layout_progressBar);
        mprogressBar.setVisibility(View.GONE);
        txtBack = (TextView) findViewById(R.id.textView_title);
        txtBack.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.button_submit);
        btnNext.setOnClickListener(this);
        btnAddMore = (Button) findViewById(R.id.button_add_more);
        btnAddMore.setOnClickListener(this);
        btnAddMore.setVisibility(View.GONE);

        String roomnocountvalue= String.valueOf(roomCount);
        if(flag.equals("Edit") || roomnocountvalue.equals("0")) {
            createrooms.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
        }else
        if(flag.equals("Edit") && !roomnocountvalue.equals("0"))
        {
            createrooms.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
        }

        createrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomForWhom = spinnerForWhom.getPrompt().toString();
                String noofrooms = no_of_rooms_selection_edit.getText().toString();
                if(noofrooms.equals(""))
                {
                    Toast.makeText(PropertyInfoThirdRoomActivity.this, "Please Enter No of Rooms", Toast.LENGTH_SHORT).show();

                }else if(roomForWhom.equals("For Whom"))
                {
                    mCommon.displayAlert(PropertyInfoThirdRoomActivity.this, "Please select for whom.", false);
                }else
                {
                    Toast.makeText(PropertyInfoThirdRoomActivity.this, "Room is Creating please Do not Touch on screen for Minit....", Toast.LENGTH_SHORT).show();
                    createrooms.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                   // btnNext.setText("Room is creating Please Do not Touch Any Where ");
                    roomCount = Integer.parseInt(noofrooms);
                    String roomnocountvalue= String.valueOf(roomCount);
                   if(!flag.equals("Edit")) {

                       getRoomInformation();
                    }else
                   {
                       getRoomInformation();
                   }

                }
            }
        });


        linearLayoutRooms = (LinearLayout) findViewById(R.id.linearLayout_rooms);
        mEditTextArrayList = new ArrayList<>();

    }


    public void setListners(final Bundle savedInstanceState) {

        mCommon = new Common();
        mValidation = new Validation(this);
        mSharedStorage = SharedStorage.getInstance(this);

        ArrayList<RoomNoMdelNew> mroomnoArray = new ArrayList<>();
        mArrayNoOfRooms = new ArrayList<>();
        RoomNoMdelNew mroomModal = new RoomNoMdelNew();
        mroomModal.setRoomnoqueantity("No of rooms");
        mroomnoArray.add(mroomModal);
        mArrayNoOfRooms.add(mroomModal);
        for (int i = 1; i < 100; i++) {
            RoomNoMdelNew mroomModal1 = new RoomNoMdelNew();
            mroomModal1.setRoomnoqueantity("" + i);
            mroomnoArray.add(mroomModal1);
            mArrayNoOfRooms.add(mroomModal1);
            //  mArrayNoOfRooms.addAll(String.valueOf(mStateModal));
        }

        mNoOfRoomsSpinnerAdapter = new RoomNoAddAdapter(this,R.id.no_of_rooms_selection,mArrayNoOfRooms);
        spinnerNoofRooms.setAdapter(mNoOfRoomsSpinnerAdapter);
        spinnerNoofRooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerNoofRooms.setPrompt("" + mArrayNoOfRooms.get(position).getRoomnoqueantity());
                if (position != 0) {
                    roomCount = Integer.parseInt(mArrayNoOfRooms.get(position).getRoomnoqueantity());
                    if (!flag.equals("Edit")) {
                        if (savedInstanceState == null) {
                            getRoomInformation();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mArrayForWhom = new ArrayList<>();
        mArrayForWhom.addAll(getForWhom());
        mForWhomSpinnerAdapter = new StateSpinnerAdapter(this, R.id.for_whom_selection, mArrayForWhom);
        spinnerForWhom.setAdapter(mForWhomSpinnerAdapter);
        spinnerForWhom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerForWhom.setPrompt("" + mArrayForWhom.get(position).getStrStateName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

// this code is working when loader is not apply

    private void getRoomInformation() {

        mEditTextArrayList.clear();
        linearLayoutRooms.removeAllViews();
        String valuedata= String.valueOf(roomCount);
        no_of_rooms_selection_edit.setText(valuedata);
       //no_of_rooms_selection_edit
        for(int i = 0; i < roomCount; i++)
        {
            addViewEditTextTop("" + (i + 1));
        }

        Thread t = new Thread() {
            public void run() {

                    if(_isSetEditData) {
                        _isSetEditData = false;
                        if(mArrayRooms.size()>0) {
                            setDataIfHavingRooms();
                            dialog.dismiss();
                        }else
                        {
                            dialog.dismiss();
                        }
                    }else
                    {
                        dialog.dismiss();
                    }


            }
        };
        t.start();

    }


    /*public void getRoomInformation() {

        mEditTextArrayList.clear();
        linearLayoutRooms.removeAllViews();
        for(int i = 0; i < roomCount; i++)
        {

            addViewEditTextTop("" + (i + 1));
        }


        if(mArrayRooms.size()>0) {
            dialog = new ProgressDialog(PropertyInfoThirdRoomActivity.this);
            dialog.setMessage("Please Wait Room No is Generating");
            dialog.show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(_isSetEditData) {
                    _isSetEditData = false;
                    if(mArrayRooms.size()>0) {
                        setDataIfHavingRooms();
                        dialog.dismiss();
                    }else
                    {
                        dialog.dismiss();
                    }
                }else
                {
                    dialog.dismiss();
                }
            }
        },1000);
    }
*/
    public void getRoomInformation1() {

        mEditTextArrayList.clear();
        linearLayoutRooms.removeAllViews();
        for (int i = 0; i < roomCount; i++) {
            addViewEditTextTop("" + (i + 1));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (_isSetEditData) {
                    _isSetEditData = false;
                    setDataIfHavingRooms1();
                }
            }
        }, 2000);
    }

    public void addViewEditTextTop(String text) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (View) inflater.inflate(R.layout.add_room_list_item, null);
        RelativeLayout linearRoot = (RelativeLayout) view;
        RelativeLayout linearInner = (RelativeLayout) linearRoot.getChildAt(0);
        TextView textView = (TextView) linearInner.getChildAt(0);
        textView.setText("Room " + text);
        RelativeLayout layoutRooms = (RelativeLayout) linearInner.getChildAt(1);
        RelativeLayout layoutRoomsDetail = (RelativeLayout) linearInner.getChildAt(2);
        RelativeLayout layoutChooseFile = (RelativeLayout) linearInner.getChildAt(3);

        final EditText editTextRoom = (EditText) layoutRooms.getChildAt(0);
        editTextRoom.setTag("Room No " + text);

        final Spinner spinnerNoofBad = (Spinner) layoutRooms.getChildAt(3);
        spinnerNoofBad.setTag("No of Bad " + text);

        final Spinner spinnerVacantBad = (Spinner) layoutRoomsDetail.getChildAt(0);
        spinnerVacantBad.setTag("Vacant Bad " + text);

        final EditText editTextAmount = (EditText) layoutRoomsDetail.getChildAt(3);
        editTextAmount.setTag("Room Amount " + text);

        final Spinner spinnerRoomlatbath = (Spinner) layoutChooseFile.getChildAt(0);
        spinnerRoomlatbath.setTag("Lat-Bath " + text);

        final Button btnBrowse = (Button) layoutChooseFile.getChildAt(3);
        btnBrowse.setTag("Browse image " + text);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(btnBrowse);
            }
        });

        setListner(spinnerNoofBad, spinnerVacantBad, spinnerRoomlatbath);

        RoomViewsModal mRoomViewsModal = new RoomViewsModal(Parcel.obtain());
        mRoomViewsModal.setStrRoomId("");
        mRoomViewsModal.setEdtRoomNo(editTextRoom);
        mRoomViewsModal.setSpinnerNoofRoom(spinnerNoofBad);
        mRoomViewsModal.setSpinnerVacantBad(spinnerVacantBad);
        mRoomViewsModal.setEdtRoomAmount(editTextAmount);
        mRoomViewsModal.setSpinnerlatbath(spinnerRoomlatbath);
        mRoomViewsModal.setBtnBrowse(btnBrowse);

        mEditTextArrayList.add(mRoomViewsModal);
        linearLayoutRooms.addView(view);


    }

    public void takePhoto(Button brouse) {
        btnBrowseImgRoom = brouse;
        Intent mIntent = new Intent(this, PhotoCaptureWithoutCropActivity.class);
        startActivityForResult(mIntent, Common.CAMERA_CAPTURE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Common.CAMERA_CAPTURE_REQUEST) {
                String imgRooms = Common.profileImagePath;
                if (btnBrowseImgRoom != null) {
                    btnBrowseImgRoom.setText("" + imgRooms);
                }
            }
        } catch (NullPointerException e) {
            LogConfig.logd("Exception =", "" + e.getMessage());
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LogConfig.logd("Exception =", "" + e.getMessage());
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        mCommon.hideKeybord(this, v);
        switch (v.getId()) {
            case R.id.button_submit:

                    callServcieAddRooms();
                break;
            case R.id.button_add_more:
                addMoreRooms();
                break;
            case R.id.textView_title:
                if (!isSkipOption) {
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {
                    Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                }
                break;
        }
    }

    private boolean addMoreRoomOptionActiveted = false;

    private void addMoreRooms() {

        if (roomCount == mEditTextArrayList.size()) {
            addViewEditTextTop("" + (roomCount + 1));
        } else {
            roomCount = roomCount + 1;
            addViewEditTextTop("" + (roomCount + 1));
        }

    }

    @Override
    public void requestRoomServiceResponce(String result) {

        mprogressBar.setVisibility(View.GONE);
        btnAddMore.setEnabled(true);
        LogConfig.logd("Property more rooms response =", "" + result);
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {

                    /*JSONArray jArray = jsonObject.getJSONArray("data");
                    DataBaseAdapter mDBAdapter = new DataBaseAdapter(this);
                    mDBAdapter.createDatabase();
                    mDBAdapter.open();
                    ContentValues cv = new ContentValues();
                    cv.put("owner_id", mSharedStorage.getUserId());
                    cv.put("property_id", propertyId);
                    cv.put("room_id", "" + jArray.getJSONObject(0).getString("room_id"));
                    cv.put("room_image", jArray.getJSONObject(0).getString("room_pic"));
                    cv.put("room_number", mEditTextArrayList.get(roomCount).getEdtRoomNo().getText().toString().trim());
                    cv.put("no_of_bads", mEditTextArrayList.get(roomCount).getSpinnerNoofRoom().getPrompt().toString());
                    cv.put("vacant_bad", mEditTextArrayList.get(roomCount).getSpinnerVacantBad().getPrompt().toString());
                    cv.put("room_amunt", mEditTextArrayList.get(roomCount).getEdtRoomAmount().getText().toString().trim());
                    cv.put("room_lat_bath", mEditTextArrayList.get(roomCount).getSpinnerlatbath().getPrompt().toString());
                    cv.put("status", "1");
                    mDBAdapter.savedRoomsInfo("add_rooms", cv);*/
                    roomCount = 1 + roomCount;
                    spinnerNoofRooms.setSelection(roomCount);
                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }

    String roomForWhomGlobal = "";

    // Add rooms service
    public void callServcieAddRooms() {

        boolean isDuplicateRecordFound = false;
        String roomForWhom = spinnerForWhom.getPrompt().toString();
        if (mEditTextArrayList.size() != 0) {
            JSONObject studentsObj = new JSONObject();
            try {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < mEditTextArrayList.size(); i++) {

                    String roomsNo = mEditTextArrayList.get(i).getEdtRoomNo().getText().toString().trim();
                    String roomsNoofBad = mEditTextArrayList.get(i).getSpinnerNoofRoom().getPrompt().toString();
                    String vacantBad = "";
                    if (mEditTextArrayList.get(i).getSpinnerVacantBad().getPrompt().toString().equals("Vacant Bed")) {
                        if (!roomsNoofBad.equals("Bed")) {
                            vacantBad = roomsNoofBad;
                        } else {
                            vacantBad = mEditTextArrayList.get(i).getSpinnerVacantBad().getPrompt().toString();
                        }
                    } else {
                        vacantBad = mEditTextArrayList.get(i).getSpinnerVacantBad().getPrompt().toString();
                    }
                    String roomAmount = mEditTextArrayList.get(i).getEdtRoomAmount().getText().toString().trim();
                    String roomLatBath = mEditTextArrayList.get(i).getSpinnerlatbath().getPrompt().toString();
                    String browseImg = mEditTextArrayList.get(i).getBtnBrowse().getText().toString().trim();

                    JSONObject roominfo = new JSONObject();

                    if (roomsNoofBad.equals("Bed")) {
                        roomsNoofBad = "0";
                    }
                    if (roomsNo.equals("")) {
                        roomsNo = "" + (i+1);
                    }
                    if (vacantBad.equals("Vacant Bed")) {
                        vacantBad = "0";
                    }
                    if (roomLatBath.equals("Lat-Bath")) {
                        roomLatBath = "No";
                    }


                    for (int j = 0; j < mEditTextArrayList.size(); j++) {

                        if (j != i) {
                            if (mEditTextArrayList.get(j).getEdtRoomNo().getText().toString().trim().equals(roomsNo)) {
                                Common.Config("duplicate record found   ");
                                isDuplicateRecordFound = true;
                                break;
                            }
                        }
                    }


                    if (roomsNoofBad.equals("Bed")) {
                        mCommon.displayAlert(this, "Please select no of bads in Room " + roomsNo, false);
                    } else {

                        roominfo.put("room_no", roomsNo);
                        roominfo.put("room_type", roomsNoofBad);
                        roominfo.put("vacant", vacantBad);
                        roominfo.put("amount", roomAmount);
                        roominfo.put("lat_bath", roomLatBath);
                        roominfo.put("room_id", mEditTextArrayList.get(i).getStrRoomId());
                        jsonArray.put(roominfo);
                    }

                }

                studentsObj.put("rooms", jsonArray);

                if (mValidation.checkNetworkRechability()) {
                    if (!isDuplicateRecordFound) {
                        mprogressBar.setVisibility(View.VISIBLE);
                        AddPropertyRoomsInFoAsynctask service = new AddPropertyRoomsInFoAsynctask(this);
                        service.setCallBack(this);
                        service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyId, "" + mEditTextArrayList.size(),
                                roomForWhom, jsonArray.toString());
                    }else{
                        mCommon.displayAlert(this, "Room no. can not be duplicate.", false);
                    }
                } else {
                    mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
            } catch (Exception e) {
            }


            Common.Config("room info       " + studentsObj);



            /*if (roomsNoofBad.equals("Bed")) {
                roomsNoofBad = "0";
            }
            if (vacantBad.equals("Vacant Bed")) {
                vacantBad = "0";
            }
            if (roomLatBath.equals("Lat-Bath")) {
                roomLatBath = "No";
            }
            if (browseImg.equals("Choose File")) {
                browseImg = "";
            }

            if (roomsNoofBad.equals("Bed")) {
                mCommon.displayAlert(this, "Please select no of bads in Room 1.", false);
            } else {
                mCommon.hideKeybord(this, txtBack);
                if (mValidation.checkNetworkRechability()) {
                    mprogressBar.setVisibility(View.VISIBLE);
                    AddPropertyRoomsInFoAsynctask service = new AddPropertyRoomsInFoAsynctask(this);
                    service.setCallBack(this);
                    service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyId, "" + mEditTextArrayList.size(),
                            roomForWhom, ""*//*, roomsNo, roomsNoofBad, vacantBad, roomAmount, roomLatBath, browseImg,
                            mEditTextArrayList.get(0).getStrRoomId()*//*);
                } else {
                    mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
                }
            }*/
        } else {
            mCommon.hideKeybord(this, txtBack);
            if (mValidation.checkNetworkRechability()) {
                mprogressBar.setVisibility(View.VISIBLE);
                AddPropertyRoomsInFoAsynctask service = new AddPropertyRoomsInFoAsynctask(this);
                service.setCallBack(this);
                service.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, propertyId, "0",
                        roomForWhom, "", "", "", "", "", "",
                        "");
            } else {
                mCommon.displayAlert(this, getResources().getString(R.string.str_no_network), false);
            }
        }

    }

    @Override
    public void requestResponce(String result) {
        mprogressBar.setVisibility(View.GONE);
        LogConfig.logd("Property add rooms response =", "" + result);
        System.out.println("  result     " + result);
        try {
            if (result.equalsIgnoreCase("ConnectTimeoutException")) {
                mCommon.displayAlert(this, Common.TimeOut_Message, false);
            } else {

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(WebUrls.STATUS_JSON);
                if (status.equals("true")) {

                    AlertDialogWithTwoCallback dialog = new AlertDialogWithTwoCallback() {
                        @Override
                        public void callBack() {


                            if (HostelDetailActivity.mActivity != null) {
                                HostelDetailActivity.mActivity.finish();
                            }
                            Common.Config("property_id" + mSharedStorage.getUserPropertyId());

                            Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelDetailActivity.class);
                            mIntent.putExtra("property_id", mSharedStorage.getUserPropertyId());
                            mIntent.putExtra("ShowRooms", true);
                            startActivity(mIntent);

                            finish();
                        }

                        @Override
                        public void cancelCallBack() {
//                            callSlider();
                            if (HostelDetailActivity.mActivity != null) {
                                HostelDetailActivity.mActivity.finish();
                            }

                            if (HostelListActivity.fActivity != null) {
                                HostelListActivity.fActivity.finish();
                            }
                            Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelListActivity.class);
                            startActivity(mIntent);
                            finish();
                        }
                    };
                    dialog.getPerameter(PropertyInfoThirdRoomActivity.this, "Rooms added successfully.", "Do you want to add room images or view your property ?");
                    dialog.show(PropertyInfoThirdRoomActivity.this.getSupportFragmentManager(), "");

                } else {
                    mCommon.displayAlert(this, jsonObject.getString(WebUrls.MESSAGE_JSON), false);
                }
            }
        } catch (JSONException e) {
            mCommon.displayAlert(this, "Sorry, Unable to perform action.", false);
        } catch (NullPointerException e) {
        }
    }

    private void displayAlert(final Activity mActivity, String message) {

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
                if (!isSkipOption) {
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {
                    Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                }
                dialog.dismiss();
            }
        });

        if (!dialog.isShowing())
            dialog.show();
    }

    /*public void callSlider() {

        mSharedStorage.setAddCount("1");
        Intent it = new Intent("com.krooms.hostel.rental.property.app.slider_ACTION");
        this.sendBroadcast(it);
    }*/

    public ArrayList<StateModal> getNoOfRooms() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("No of rooms");
        mArray.add(mStateModal);
        for (int i = 1; i < 100; i++) {
            mStateModal = new StateModal();
            mStateModal.setStrStateName("" + i);
            mArray.add(mStateModal);
        }

        return mArray;
    }

    public ArrayList<StateModal> getForWhom() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("For Whom");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Boys");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Girls");
        mArray.add(mStateModal);


        mStateModal = new StateModal();
        mStateModal.setStrStateName("Family");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Businessman");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Employee");
        mArray.add(mStateModal);

        return mArray;
    }

    public void setListner(final Spinner spinnerNoOfBed, final Spinner spinnerVacantBed, final Spinner spinnerAttachLatBath) {

        final StateSpinnerAdapter mNoOfBedSpinnerAdapter, mVacatBedSpinnerAdapter, mAttachLatBathSpinnerAdapter;
        final ArrayList<StateModal> mArraynoOfBed = new ArrayList<>();

        final ArrayList<StateModal> mArrayvacatBed = new ArrayList<>();
        mArrayvacatBed.addAll(getVacentBedWithFixedLength(0));
        mVacatBedSpinnerAdapter = new StateSpinnerAdapter(this, R.id.vacat_no_bed, mArrayvacatBed);
        mArraynoOfBed.addAll(getNoOfBed());
        mNoOfBedSpinnerAdapter = new StateSpinnerAdapter(this, R.id.no_bed, mArraynoOfBed);
        spinnerNoOfBed.setAdapter(mNoOfBedSpinnerAdapter);
        spinnerNoOfBed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String noOfBed = mArraynoOfBed.get(position).getStrStateName();
                if (!noOfBed.equals("Bed")) {
                    mArrayvacatBed.clear();
                    mArrayvacatBed.addAll(getVacentBedWithFixedLength(Integer.parseInt(noOfBed) + 1));
                    mVacatBedSpinnerAdapter.notifyDataSetChanged();
                } else {
                    mArrayvacatBed.clear();
                    mArrayvacatBed.addAll(getVacentBedWithFixedLength(0));
                    mVacatBedSpinnerAdapter.notifyDataSetChanged();
                }
                spinnerNoOfBed.setPrompt("" + noOfBed);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        final ArrayList<StateModal> mArrayvacatBed = new ArrayList<>();
//        mArrayvacatBed.addAll(getVacentBedWithFixedLength(0));

        spinnerVacantBed.setAdapter(mVacatBedSpinnerAdapter);
        spinnerVacantBed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < mArrayvacatBed.size()) {
                    String vacentBed = mArrayvacatBed.get(position).getStrStateName();
                    spinnerVacantBed.setPrompt("" + vacentBed);
                } else {
                    System.out.println("In else condion");
                    spinnerVacantBed.setPrompt("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayList<StateModal> mArrayattachLatBath = new ArrayList<>();
        mArrayattachLatBath.addAll(getAttachedLetBath());
        mAttachLatBathSpinnerAdapter = new StateSpinnerAdapter(this, R.id.lat_bath_selector, mArrayattachLatBath);
        spinnerAttachLatBath.setAdapter(mAttachLatBathSpinnerAdapter);
        spinnerAttachLatBath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String attachedLatBath = mArrayattachLatBath.get(position).getStrStateName();
                spinnerAttachLatBath.setPrompt("" + attachedLatBath);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public ArrayList<StateModal> getNoOfBed() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Bed");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("1");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("2");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("3");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("4");
        mArray.add(mStateModal);
        return mArray;

    }

    public ArrayList<StateModal> getVacentBedWithFixedLength(int length) {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Vacant Bed");
        mArray.add(mStateModal);

        for (int i = 0; i < length; i++) {
            mStateModal = new StateModal();
            mStateModal.setStrStateName("" + i);
            mArray.add(mStateModal);
        }
        return mArray;
    }

    public ArrayList<StateModal> getAttachedLetBath() {

        ArrayList<StateModal> mArray = new ArrayList<>();
        StateModal mStateModal = new StateModal();
        mStateModal.setStrStateName("Lat-Bath");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("Yes");
        mArray.add(mStateModal);

        mStateModal = new StateModal();
        mStateModal.setStrStateName("No");
        mArray.add(mStateModal);

        return mArray;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        mCommon.hideKeybord(this, txtBack);
       /* AlertDialogWithTwoBtn callbackDialog = new AlertDialogWithTwoBtn() {
            @Override
            public void callBack() {
                if (!isSkipOption) {
                    Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {

                    Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                }
            }
        };
        callbackDialog.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));
        callbackDialog.show(getSupportFragmentManager(), "Exit ");*/

CustomDialogClassBack cdd = new CustomDialogClassBack(PropertyInfoThirdRoomActivity.this, R.style.full_screen_dialog);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.getPerameter(this, getResources()
                        .getString(R.string.app_name),
                getResources().getString(R.string.str_back_message));        cdd.show();
    }


    //new ImageView dialog on click  View Image changes on 19/2/2019 by ashish
    public class CustomDialogClassBack extends Dialog {

        private FragmentActivity mFActivity = null;
        private TextView title;
        private TextView message;
        private Button alertYesBtn;
        private Button alertNoBtn;
        private String titleStr;
        private String messageStr;

        public CustomDialogClassBack(PropertyInfoThirdRoomActivity a, int full_screen_dialog) {
            super(a, full_screen_dialog);
            // TODO Auto-generated constructor stub
            //this.c = a;
        }


        public void getPerameter(FragmentActivity activity, String titleStr, String messageStr) {

            mFActivity = activity;
            this.titleStr = titleStr;
            this.messageStr = messageStr;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.mFActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            setContentView(R.layout.alert_two_btn_dialog);
            title = (TextView) findViewById(R.id.alertTitle);
            message = (TextView) findViewById(R.id.categoryNameInput);
            alertYesBtn = (Button) findViewById(R.id.alertYesBtn);
            alertNoBtn = (Button) findViewById(R.id.alertNoBtn);

            title.setText(titleStr);
            message.setText(messageStr);

            alertYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSkipOption) {
                        Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelListActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                    } else {

                        Intent mIntent = new Intent(PropertyInfoThirdRoomActivity.this, HostelListActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                    }
                    dismiss();
                }
            });

            alertNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

    }

    ///nnnn


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("propertyId", propertyId);
        outState.putString("flag", flag);
        outState.putBoolean("isSkipOption", isSkipOption);
        outState.putString("roomForWhomGlobal", spinnerForWhom.getPrompt().toString());

        outState.putParcelable("propertyModal", PropertyModal.getInstance());

//        outState.putParcelableArrayList("mEditTextArrayList", mEditTextArrayList);

        if (mArrayRooms != null) {
            mArrayRooms.clear();
        } else {
            mArrayRooms = new ArrayList();
        }


        for (int i = 0; i < mEditTextArrayList.size(); i++) {

            RoomModal modal = new RoomModal(Parcel.obtain());
            modal.setRoomId(mEditTextArrayList.get(i).getStrRoomId());
            modal.setRoomNo(mEditTextArrayList.get(i).getEdtRoomNo().getText().toString());
            modal.setRoomType(mEditTextArrayList.get(i).getSpinnerNoofRoom().getPrompt().toString());
            modal.setVacantBed(mEditTextArrayList.get(i).getSpinnerVacantBad().getPrompt().toString());
            modal.setBedPriceAmount(mEditTextArrayList.get(i).getEdtRoomAmount().getText().toString());
            modal.setRoomImage(mEditTextArrayList.get(i).getBtnBrowse().getText().toString());
            modal.setLat_bath(mEditTextArrayList.get(i).getSpinnerlatbath().getPrompt().toString());
            mArrayRooms.add(modal);

        }

        outState.putParcelableArrayList("mArrayRooms", mArrayRooms);
        outState.putInt("roomCount", roomCount);

    }

    public void setDataOnSavedState(Bundle savedInstanceState) {

        propertyId = savedInstanceState.getString("propertyId");
        flag = savedInstanceState.getString("flag");
        isSkipOption = savedInstanceState.getBoolean("isSkipOption");
        roomForWhomGlobal = savedInstanceState.getString("roomForWhomGlobal");
        PropertyModal.propertyModal = savedInstanceState.getParcelable("propertyModal");
//        roomCount = savedInstanceState.getInt("roomCount");
        if (mArrayRooms != null) {
            mArrayRooms.clear();
        }
        mArrayRooms = savedInstanceState.getParcelableArrayList("mArrayRooms");
        roomCount = mArrayRooms.size();
    }
}