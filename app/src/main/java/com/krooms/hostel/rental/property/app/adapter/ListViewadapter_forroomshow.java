package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.Roommodel;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 8/2/2017.
 */
public class ListViewadapter_forroomshow extends ArrayAdapter {

    ArrayList<Roommodel> roomnostudentarraylist = new ArrayList<Roommodel>();
    Activity context;
    String[] Lath_bath = {"Lath Bath", "Yes", "No"};
    String[] Vacant_bed = {"Vacant Bed", "1", "2", "3", "4"};
    String[] bed = {"Bed", "1", "2", "3", "4"};
    TextView room_no_id;
    EditText amount, roomno;
    Button createrooms;
    String Vacant_bed_value;
    String Lath_bath_value, forwhom;
    String bed_value;
    String propertyid;
    RelativeLayout layout_top;
    LinearLayout vacant_bed_layout, bedselection_layout;
    Spinner lath_bath, vacant_bed, bed_selection;
    String label;
    TextView vacant_bed_text;
    TextView bed_selection_text;

    VIewHolder holder;

    public ListViewadapter_forroomshow(Activity context, ArrayList<Roommodel> roomnostudentarraylist, String propertyid, String forwhom) {
        super(context, R.layout.add_room_list_item, roomnostudentarraylist);
        this.context = context;
        this.roomnostudentarraylist = roomnostudentarraylist;
        this.propertyid = propertyid;
        this.forwhom = forwhom;
    }

    @Override
    public int getCount() {
        return roomnostudentarraylist.size();
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class VIewHolder {
        private EditText amount;
        private EditText roomno;
        private TextView vacant_bed;
        private TextView bed_selection;
        private Spinner lath_bath;
        private TextView room_no_id;
        private Button createrooms;
        private LinearLayout vacant_bed_layout;
        private RelativeLayout layout_top;
        private LinearLayout bedselection_layout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            holder = new VIewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.add_room_list_item, parent, false);
            holder.layout_top = (RelativeLayout) v.findViewById(R.id.layout_top);
            holder.room_no_id = (TextView) v.findViewById(R.id.room_no_id);
            holder.lath_bath = (Spinner) v.findViewById(R.id.lat_bath_selector);
            holder.roomno = (EditText) v.findViewById(R.id.roomNo_input);
            holder.amount = (EditText) v.findViewById(R.id.amount_of_bed);
            holder.vacant_bed = (TextView) v.findViewById(R.id.vacat_no_bed);
            holder.bed_selection = (TextView) v.findViewById(R.id.no_bed);
            holder.createrooms = (Button) v.findViewById(R.id.createrooms);
            holder.bedselection_layout = (LinearLayout) v.findViewById(R.id.bedselection_layout);
            holder.vacant_bed_layout = (LinearLayout) v.findViewById(R.id.vacant_bed_layout);

            holder.vacant_bed_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int bed_no = position;
                    Log.d("clickListener", position + "," + holder);
                    String bed_no_value = roomnostudentarraylist.get(position).getNobed();
                    Log.d("calling", bed_no + "," + bed_no_value);

                    if (bed_no_value.equals("0")) {
                        Toast.makeText(context, "Please Select No. of Beds First", Toast.LENGTH_SHORT).show();
                    } else {
                        showDialog(bed_no, bed_no_value);
                    }
                }
            });

            holder.bedselection_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int bed_no = position;
                    Log.d("clickListener", position + "," + holder);
                    showDialog_bed(bed_no);
                }
            });

            holder.createrooms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textviewroom = roomnostudentarraylist.get(position).getSequenceno();
                    String roomno_value = roomnostudentarraylist.get(position).getRoomno();
                    String amount_value = roomnostudentarraylist.get(position).getAmount();
                    String vacant_value = roomnostudentarraylist.get(position).getVacant_bed();
                    String Lathbath_value = roomnostudentarraylist.get(position).getLath_bath();
                    String bedvalue = roomnostudentarraylist.get(position).getNobed();
                    if (bedvalue.equals("Bed")) {
                        bedvalue = "0";
                    }
                    if (vacant_value.equals("Vacant Bed")) {
                        vacant_value = "0";
                    }
                    if (Lathbath_value.equals("Lat-Bath")) {
                        Lathbath_value = "No";
                    }
                    saveRoomno(textviewroom, roomno_value, amount_value, vacant_value, Lathbath_value, bedvalue);

                }
            });


            holder.lath_bath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int positionInSpinner, long id) {
                    // TODO Auto-generated method stub
                    Lath_bath_value = Lath_bath[positionInSpinner];
                    roomnostudentarraylist.get(position).setLath_bath(Lath_bath_value);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });


            holder.roomno.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    roomnostudentarraylist.get(position).setRoomno(s.toString());
                }
            });

            holder.amount.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    roomnostudentarraylist.get(position).setAmount(s.toString());
                }
            });


            v.setTag(holder);
        } else {
            holder = (VIewHolder) convertView.getTag();
        }


        holder.room_no_id.setText(roomnostudentarraylist.get(position).getSequenceno());
        holder.roomno.setText(roomnostudentarraylist.get(position).getRoomno());
        String roombed = roomnostudentarraylist.get(position).getNobed();
        holder.vacant_bed.setText(roomnostudentarraylist.get(position).getVacant_bed());
        holder.bed_selection.setText(roomnostudentarraylist.get(position).getNobed());
        if (roomnostudentarraylist.get(position).getAmount().equals("0")) {
            holder.amount.setText("");
        } else {
            holder.amount.setText(roomnostudentarraylist.get(position).getAmount());
        }

        ArrayAdapter adapter_bath = new ArrayAdapter(context, android.R.layout.simple_spinner_item, Lath_bath);
        holder.lath_bath.setAdapter(adapter_bath);

        if (roomnostudentarraylist.get(position).getLath_bath().equals("No")) {
            Lath_bath_value = roomnostudentarraylist.get(position).getLath_bath();
            int spinnerPosition = adapter_bath.getPosition(roomnostudentarraylist.get(position).getLath_bath());
            holder.lath_bath.setSelection(spinnerPosition);
        } else {
            Lath_bath_value = roomnostudentarraylist.get(position).getLath_bath();
            int spinnerPosition = adapter_bath.getPosition(roomnostudentarraylist.get(position).getLath_bath());
            holder.lath_bath.setSelection(spinnerPosition);
        }

        return v;
    }

    public void showDialog_bed(final int bed_no) {
        final Dialog vacantbed = new Dialog(context);
        vacantbed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        vacantbed.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) vacantbed.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Bed");
        ListView bed_list = (ListView) vacantbed.findViewById(R.id.list_country);
        ImageView student_loader = (ImageView) vacantbed.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) vacantbed.findViewById(R.id.country_cross_layout);

        final ArrayList<OwnerStudentNameModel> ownerstudent_arraylist = new ArrayList<>();
        OwnerStudentNameModel model = new OwnerStudentNameModel();
        model.setTanentusername("1");
        ownerstudent_arraylist.add(model);
        OwnerStudentNameModel model1 = new OwnerStudentNameModel();
        model1.setTanentusername("2");
        ownerstudent_arraylist.add(model1);
        OwnerStudentNameModel model2 = new OwnerStudentNameModel();
        model2.setTanentusername("3");
        ownerstudent_arraylist.add(model2);
        OwnerStudentNameModel model3 = new OwnerStudentNameModel();
        model3.setTanentusername("4");
        ownerstudent_arraylist.add(model3);

        RoomspinnerAdapter adapter_data_month = new RoomspinnerAdapter(context, ownerstudent_arraylist);
        bed_list.setAdapter(adapter_data_month);

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vacantbed.dismiss();
            }
        });
        bed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String vacantbedvalue = ownerstudent_arraylist.get(position).getTanentusername().toString();
                roomnostudentarraylist.get(bed_no).setNobed("" + vacantbedvalue);
                roomnostudentarraylist.get(bed_no).setVacant_bed("0");
                notifyDataSetChanged();
                vacantbed.dismiss();
            }
        });
        vacantbed.show();
    }

    public void showDialog(final int bed_no, final String bed_count) {
        final Dialog vacantbed = new Dialog(context);
        vacantbed.requestWindowFeature(Window.FEATURE_NO_TITLE);
        vacantbed.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) vacantbed.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Vacant Bed");
        ListView vacantbed_list = (ListView) vacantbed.findViewById(R.id.list_country);
        ImageView student_loader = (ImageView) vacantbed.findViewById(R.id.student_loader);
        RelativeLayout month_cross_layout = (RelativeLayout) vacantbed.findViewById(R.id.country_cross_layout);

        final ArrayList<OwnerStudentNameModel> ownerstudent_arraylist = new ArrayList<>();
        Log.d("qq", bed_no + "," + bed_count);
        String roombed = bed_count;
        if (roombed.equals("1")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);

        } else if (roombed.equals("2")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);
            OwnerStudentNameModel model1 = new OwnerStudentNameModel();
            model1.setTanentusername("2");
            ownerstudent_arraylist.add(model1);

        } else if (roombed.equals("3")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);
            OwnerStudentNameModel model1 = new OwnerStudentNameModel();
            model1.setTanentusername("2");
            ownerstudent_arraylist.add(model1);
            OwnerStudentNameModel model2 = new OwnerStudentNameModel();
            model2.setTanentusername("3");
            ownerstudent_arraylist.add(model2);
        } else if (roombed.equals("4")) {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("1");
            ownerstudent_arraylist.add(model);
            OwnerStudentNameModel model1 = new OwnerStudentNameModel();
            model1.setTanentusername("2");
            ownerstudent_arraylist.add(model1);
            OwnerStudentNameModel model2 = new OwnerStudentNameModel();
            model2.setTanentusername("3");
            ownerstudent_arraylist.add(model2);
            OwnerStudentNameModel model3 = new OwnerStudentNameModel();
            model3.setTanentusername("4");
            ownerstudent_arraylist.add(model3);

        } else {
            OwnerStudentNameModel model = new OwnerStudentNameModel();
            model.setTanentusername("Vacant Bed");
            ownerstudent_arraylist.add(model);
        }

        RoomspinnerAdapter adapter_data_month = new RoomspinnerAdapter(context, ownerstudent_arraylist);
        vacantbed_list.setAdapter(adapter_data_month);

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vacantbed.dismiss();
            }
        });
        vacantbed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String vacantbedvalue = ownerstudent_arraylist.get(position).getTanentusername().toString();

                roomnostudentarraylist.get(bed_no).setVacant_bed("" + vacantbedvalue);
                notifyDataSetChanged();
                vacantbed.dismiss();
            }
        });
        vacantbed.show();
    }

    public void saveRoomno(final String sequenceno, final String roomno_value, final String amount_value, final String vacant_value, final String Lathbath_value, final String bedvalue) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = WebUrls.MAIN_URL;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        getResponsedata(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.getMessage() != null) {
                            Toast.makeText(context, "error VOLLEY " + error.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "addPropertyRoosSeprate");
                params.put("sequenceno", sequenceno);
                params.put("roomno", roomno_value);
                params.put("amount", amount_value);
                params.put("vacant", vacant_value);
                params.put("Lathbath", Lathbath_value);
                params.put("bed", bedvalue);
                params.put("property_id", propertyid);
                return params;
            }
        };

        strRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(strRequest);
    }

    public void getResponsedata(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            result = jo.getString("flag");
            String message = jo.getString("message");

            if (result.equalsIgnoreCase("Y")) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
            Log.d("result..", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
