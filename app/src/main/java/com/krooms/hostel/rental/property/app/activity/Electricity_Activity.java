package com.krooms.hostel.rental.property.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.Payment_Paid_Unpaid_Adapter;
import com.krooms.hostel.rental.property.app.modal.ElectricityModel;
import com.krooms.hostel.rental.property.app.modal.PaymentPaidUnpaidModel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 4/20/2018.
 */

public class Electricity_Activity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout monthlayout, yearlayout, add_layout, update_layout, add_mainlayout, update_mainlayout;
    Dialog month_dialog;
    TextView monthtextview, yeartextview;
    Button btnsubmit;
    String[] month_data = {
            "January",
            "Febuary",
            "March",
            "April",
            "May",
            "June",
            "July", "August", "September", "October", "November", "December"
    };


    String[] monthid = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    String[] year_data = {
            "2016",
            "2017",
            "2018",
            "2019",
            "2020",
            "2021",
            "2022", "2023", "2024", "2025", "2026", "2027"
    };

    String monthname = "", propertyid = "", month_id = "";
    Intent in;
    private List list;
    public Electricity_Adapter electricity_adapter;
    Context context;
    ArrayList<ElectricityModel> arraylist;
    ArrayList<ElectricityModel> arraylistupdate;
    ListView listView, updatelistView;
    String yearvalue = "", monthvalue = "";
    Calendar calnder;
    String timeanddate = "", monthyear = "", type = "", getyear = "";
    RelativeLayout back_button;

    public static ArrayList<ElectricityModel> articalshow_list_value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electricity_layout);
        in = getIntent();
        context = getApplicationContext();
        arraylist = new ArrayList<>();
        arraylistupdate = new ArrayList<>();
        list = new ArrayList<Integer>();
        propertyid = in.getStringExtra("Propertyid");
        findViewById();
        monthlayout.setOnClickListener(this);
        yearlayout.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        add_layout.setOnClickListener(this);
        update_layout.setOnClickListener(this);
        monthtextview.setText("Select Month");
        yeartextview.setText("Select Year");
        add_layout.setBackgroundResource(R.color.grey);
        update_layout.setBackgroundResource(R.color.lightgray);
        add_mainlayout.setVisibility(View.VISIBLE);
        update_mainlayout.setVisibility(View.GONE);
        type = "1";
        calnder = Calendar.getInstance();
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        timeanddate = df3.format(calnder.getTime());
        calnder = Calendar.getInstance();
        SimpleDateFormat df4 = new SimpleDateFormat("yyyy", Locale.US);
        getyear = df4.format(calnder.getTime());

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Electricity_Activity.this, HostelListActivity.class);
                startActivity(i);

            }
        });

    }

    private void findViewById() {
        monthlayout = (LinearLayout) findViewById(R.id.monthlayout);
        monthtextview = (TextView) findViewById(R.id.month);
        btnsubmit = (Button) findViewById(R.id.btn_submit);
        listView = (ListView) findViewById(R.id.list);
        updatelistView = (ListView) findViewById(R.id.list_update);
        yeartextview = (TextView) findViewById(R.id.yeartextview);
        yearlayout = (LinearLayout) findViewById(R.id.yearlayout);
        add_layout = (LinearLayout) findViewById(R.id.add_layout);
        update_layout = (LinearLayout) findViewById(R.id.update_layout);
        add_mainlayout = (LinearLayout) findViewById(R.id.main_layout);
        update_mainlayout = (LinearLayout) findViewById(R.id.main_layout_update);
        back_button = (RelativeLayout) findViewById(R.id.back_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.monthlayout:
                monthdialogMethod();
                arraylist.clear();
                arraylistupdate.clear();
                if (type.equals("1")) {
                    add_mainlayout.setVisibility(View.GONE);
                } else {
                    update_mainlayout.setVisibility(View.GONE);
                }
                break;

            case R.id.yearlayout:
                yeardialogMethod();
                if (type.equals("1")) {
                    add_mainlayout.setVisibility(View.GONE);
                } else {
                    update_mainlayout.setVisibility(View.GONE);
                }
                break;

            case R.id.btn_submit:
                monthvalue = monthtextview.getText().toString().trim();
                yearvalue = yeartextview.getText().toString().trim();
                if (monthvalue.equals("Select Month")) {
                    Toast.makeText(Electricity_Activity.this, "Please Select Month", Toast.LENGTH_SHORT).show();
                } else {
                    if (type.equals("1")) {
                        add_mainlayout.setVisibility(View.VISIBLE);

                    } else {
                        update_mainlayout.setVisibility(View.VISIBLE);
                    }
                    monthyear = month_id + "/" + getyear;
                    if (NetworkConnection.isConnected(Electricity_Activity.this)) {
                        if (type.equals("1")) {
                            getTeanntdetails();
                        } else {
                            getTeanntdetailsUpdate();
                        }
                    } else {
                        Toast.makeText(context, "Please Check Internet", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.add_layout:
                add_layout.setBackgroundResource(R.color.grey);
                update_layout.setBackgroundResource(R.color.lightgray);
                add_mainlayout.setVisibility(View.VISIBLE);
                update_mainlayout.setVisibility(View.GONE);
                type = "1";
                arraylist.clear();
                break;


            case R.id.update_layout:
                add_layout.setBackgroundResource(R.color.lightgray);
                update_layout.setBackgroundResource(R.color.grey);
                add_mainlayout.setVisibility(View.GONE);
                update_mainlayout.setVisibility(View.VISIBLE);
                type = "2";
                arraylistupdate.clear();
                break;
        }
    }

    private void monthdialogMethod() {

        month_dialog = new Dialog(Electricity_Activity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Month Name");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Electricity_Activity.this, month_data);
        month_list.setAdapter(adapter_data_month);

        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String month_value = month_data[position].toString();
                month_id = monthid[position].toString();
                monthtextview.setText(month_value);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();


    }

    private void getTeanntdetails() {

        final ProgressDialog dialog = new ProgressDialog(Electricity_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "electricityuserlist");
            params.put("property_id", propertyid);
            params.put("monthyear", monthyear);
            params.put("month_id", month_id);
            params.put("year", getyear);
            params.put("type", "1");
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseData(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseData(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            arraylist = new ArrayList();
            arraylist.clear();
            articalshow_list_value = new ArrayList<>();
            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    ElectricityModel recordmodel = new ElectricityModel();
                    String fname = jsonobj.getString("tenant_fname");
                    String lname = jsonobj.getString("tenant_lname");
                    String fullname = fname + " " + lname;
                    recordmodel.setTenant_name(fullname);
                    recordmodel.setRoom_no(jsonobj.getString("room_no"));
                    recordmodel.setTenant_id(jsonobj.getString("id"));
                    recordmodel.setUnit(jsonobj.getString("unit"));//
                    arraylist.add(recordmodel);
                }
                electricity_adapter = new Electricity_Adapter(Electricity_Activity.this, arraylist, list, articalshow_list_value, propertyid, monthname, yearvalue);
                listView.setAdapter(electricity_adapter);
                electricity_adapter.notifyDataSetChanged();

            } else {
                try {
                    Toast.makeText(Electricity_Activity.this, message, Toast.LENGTH_SHORT).show();
                    electricity_adapter = new Electricity_Adapter(Electricity_Activity.this, arraylist, list, articalshow_list_value, propertyid, monthname, yearvalue);
                    listView.setAdapter(electricity_adapter);
                    electricity_adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //get tenant details update//
    private void getTeanntdetailsUpdate() {

        final ProgressDialog dialog = new ProgressDialog(Electricity_Activity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "electricityuserlist");
            params.put("property_id", propertyid);
            params.put("monthyear", monthyear);
            params.put("month_id", month_id);
            params.put("year", yearvalue);
            params.put("type", "2");
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDataupdate(result);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                }
            });
            request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request_json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResponseDataupdate(String result) {
        try {
            String status, message;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            arraylistupdate = new ArrayList();
            arraylistupdate.clear();
            articalshow_list_value = new ArrayList<>();
            if (status.equalsIgnoreCase("Y")) {

                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    ElectricityModel recordmodel = new ElectricityModel();
                    recordmodel.setTenant_name(jsonobj.getString("name"));
                    recordmodel.setRoom_no(jsonobj.getString("roomno"));
                    recordmodel.setTenant_id(jsonobj.getString("tenantid"));
                    recordmodel.setUnit(jsonobj.getString("unit"));
                    arraylistupdate.add(recordmodel);
                }
                electricity_adapter = new Electricity_Adapter(Electricity_Activity.this, arraylistupdate, list, articalshow_list_value, propertyid, monthname, yearvalue);
                updatelistView.setAdapter(electricity_adapter);
                electricity_adapter.notifyDataSetChanged();

            } else {
                try {
                    Toast.makeText(Electricity_Activity.this, message, Toast.LENGTH_SHORT).show();
                    electricity_adapter = new Electricity_Adapter(Electricity_Activity.this, arraylistupdate, list, articalshow_list_value, propertyid, monthname, yearvalue);
                    updatelistView.setAdapter(electricity_adapter);
                    electricity_adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //

    //year layout
    public void yeardialogMethod() {
        month_dialog = new Dialog(Electricity_Activity.this);
        month_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        month_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) month_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Year");
        ListView month_list = (ListView) month_dialog.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Electricity_Activity.this, year_data);
        month_list.setAdapter(adapter_data_month);
        yearvalue = yeartextview.getText().toString();
        RelativeLayout month_cross_layout = (RelativeLayout) month_dialog.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month_dialog.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String month_value = year_data[position].toString();
                yeartextview.setText(month_value);
                month_dialog.dismiss();
            }
        });
        month_dialog.show();
    }


    public class Electricity_Adapter extends BaseAdapter {

        Context context;
        ArrayList<ElectricityModel> arraylist;
        private List list;
        //private List list;
        LayoutInflater mInflater;
        ArrayList<ElectricityModel> articalshow_list_value;
        String propertyid, monthname, year;

        public Electricity_Adapter(Context context, ArrayList<ElectricityModel> arraylist, List list1, ArrayList<ElectricityModel> articalshow_list_value, String propertyid, String monthname, String yearvalue) {
            this.context = context;
            this.arraylist = arraylist;
            this.list = list1;
            this.articalshow_list_value = articalshow_list_value;
            this.propertyid = propertyid;
            this.monthname = monthname;
            year = yearvalue;
        }

        @Override
        public int getCount() {
            return arraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return (position);
        }

        @Override
        public long getItemId(int position) {
            return (position);
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }


        //new
        @Override
        public View getView(final int position, View vi, ViewGroup parent) {
            View v = vi;
            final ViewHolder holder;
            if (vi == null) {
                mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.electricity_item, null);
                holder = new ViewHolder();
                holder.caption = (EditText) v.findViewById(R.id.txtunit);
                holder.submit = (TextView) v.findViewById(R.id.btnsave);
                holder.name = (TextView) v.findViewById(R.id.txtname);
                holder.roomno = (TextView) v.findViewById(R.id.txtroomno);
                holder.roomno.setText(arraylist.get(position).getRoom_no());
                holder.name.setText(arraylist.get(position).getTenant_name());
                holder.caption.setText(arraylist.get(position).getUnit());
                v.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            final String codevalue = holder.caption.getText().toString().trim();
            String itemnamevalue = arraylist.get(position).getTenant_name();
            String item_id_new_value = arraylist.get(position).getRoom_no();
            String unit = arraylist.get(position).getUnit();

            holder.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    monthvalue = monthtextview.getText().toString().trim();
                    yearvalue = yeartextview.getText().toString().trim();
                    String unitvalue = holder.caption.getText().toString().trim();
                    if (unitvalue.length() != 0) {
                        String roomno = arraylist.get(position).getRoom_no();
                        String name = arraylist.get(position).getTenant_name();
                        String tenantid = arraylist.get(position).getTenant_id();
                        ElectricityModel recordmodel = new ElectricityModel();
                        recordmodel.setTenant_id(tenantid);
                        recordmodel.setRoom_no(roomno);
                        recordmodel.setTenant_name(name);
                        recordmodel.setUnit(unitvalue);
                        recordmodel.setSavedate(timeanddate);
                        Electricity_Activity.articalshow_list_value.add(recordmodel);
                        Sendtoserver(name, tenantid, roomno, unitvalue, timeanddate, monthname, yearvalue);
                    } else {
                        Toast.makeText(context, "Please Enter Unit", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return v;
        }

        private void Sendtoserver(String name, String tenantid, String roomno, String unitvalue, String timeanddate1, String monthname1, String year) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

            try {
                JSONObject params = new JSONObject();
                params.put("action", "saveelectricityunit");
                params.put("property_id", propertyid);
                params.put("tenant_id", tenantid);
                params.put("name", name);
                params.put("unit", unitvalue);
                params.put("roomno", roomno);
                params.put("added_date", timeanddate);
                params.put("month", monthvalue);
                params.put("year", getyear);
                params.put("month_id", month_id);

                String url = WebUrls.MAIN_URL;
                JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        String result = response.toString();
                        getResponseData(result);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                });
                request_json.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request_json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void getResponseData(String result) {
            try {
                String status, message;
                JSONObject jsmain = new JSONObject(result);
                status = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (status.equalsIgnoreCase("Y")) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    class ViewHolder {
        EditText caption;
        TextView submit;
        TextView name, roomno;
    }


    class ViewHolderr {
        EditText caption;
        TextView submit;
        TextView name, roomno;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(Electricity_Activity.this, HostelListActivity.class);
                startActivity(i);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
