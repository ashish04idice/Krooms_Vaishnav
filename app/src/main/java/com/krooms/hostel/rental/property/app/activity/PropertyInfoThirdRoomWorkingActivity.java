package com.krooms.hostel.rental.property.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.RoomNoAddAdapter;
import com.krooms.hostel.rental.property.app.adapter.StateSpinnerAdapter;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.LogConfig;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.RoomModal;
import com.krooms.hostel.rental.property.app.modal.RoomNoMdelNew;
import com.krooms.hostel.rental.property.app.modal.RoomViewsModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;

import java.util.ArrayList;

/**
 * Created by admin on 3/1/2017.
 */
public class PropertyInfoThirdRoomWorkingActivity extends Activity
{
    String roomForWhomGlobal = "";
    private Spinner spinnerNoofRooms;
    private Spinner spinnerForWhom;
    private String propertyId = "";
    public String flag="";
    private Boolean _isSetEditData = true, isSkipOption = false;
    ProgressBar mprogressBar;
    TextView txtBack;
    Button btnNext,btnAddMore;
    private int roomCount;
    private Button btnBrowseImgRoom;
    private ArrayList<RoomViewsModal> mEditTextArrayList;
    private ArrayList<RoomModal> mArrayRooms = new ArrayList<>();
    private LinearLayout linearLayoutRooms;
    private ArrayList<RoomNoMdelNew> mArrayNoOfRooms;
    private ArrayList<StateModal> mArrayForWhom;
    RoomNoAddAdapter mNoOfRoomsSpinnerAdapter;
    private StateSpinnerAdapter mForWhomSpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_info_third_room);
        isSkipOption = getIntent().getBooleanExtra("isSkipOption", false);
        propertyId = getIntent().getStringExtra("PropertyId");
        flag = getIntent().getExtras().getString("flag");

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

        spinnerNoofRooms = (Spinner) findViewById(R.id.no_of_rooms_selection);
        spinnerForWhom = (Spinner) findViewById(R.id.for_whom_selection);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
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

        linearLayoutRooms = (LinearLayout) findViewById(R.id.linearLayout_rooms);
        mEditTextArrayList = new ArrayList<>();
        txtBack = (TextView) findViewById(R.id.textView_title);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSkipOption) {
                    finish();
                    overridePendingTransition(R.anim.slide_back_in, R.anim.slide_back_out);
                } else {
                    Intent mIntent = new Intent(PropertyInfoThirdRoomWorkingActivity.this, HostelListActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                }

            }
        });
        btnNext = (Button) findViewById(R.id.button_submit);
        btnAddMore = (Button) findViewById(R.id.button_add_more);
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMoreRooms();

            }
        });

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
            btnAddMore.setVisibility(View.VISIBLE);
            spinnerNoofRooms.setEnabled(false);
            roomCount = Integer.parseInt(PropertyModal.getInstance().getNo_of_rooms());
            spinnerNoofRooms.setSelection(roomCount);
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


    public void setDataIfHavingRooms() {

        if (flag.equals("Edit")) {

            for (int i = 0; i < mArrayRooms.size(); i++) {

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

                }
            }
        }
    }

    public void setDataIfHaving1() {


        if (flag.equals("Edit")) {
            btnAddMore.setVisibility(View.VISIBLE);
            spinnerNoofRooms.setEnabled(false);
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

    public void getRoomInformation() {

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
                    setDataIfHavingRooms();
                }
            }
        }, 2000);
    }

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

        //setListner(spinnerNoofBad, spinnerVacantBad, spinnerRoomlatbath);

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

    private void addMoreRooms() {

        if (roomCount== mEditTextArrayList.size()) {
            addViewEditTextTop("" + (roomCount + 1));
        } else {
            roomCount = roomCount + 1;
            addViewEditTextTop("" + (roomCount + 1));
        }

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
