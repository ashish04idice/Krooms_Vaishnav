package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Articale_inventory_Adapter;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.OwnerStudentNameTanentAdapter;
import com.krooms.hostel.rental.property.app.adapter.RoomNo_Adapter;
import com.krooms.hostel.rental.property.app.adapter.Show_inventory_Adapter_list;
import com.krooms.hostel.rental.property.app.adapter.StudentName_Adapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.fragments.Add_Envertory_Fragment;
import com.krooms.hostel.rental.property.app.fragments.Assign_Enventory_Fragment;
import com.krooms.hostel.rental.property.app.modal.Articales_Model;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.Product;
import com.krooms.hostel.rental.property.app.util.NetworkConnection;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 4/18/2017.
 */
public class Enventory_Activity_Owner extends Activity {

    ImageView loader;
    ArrayList<Articales_Model> Articles_arraylist;
    public static ArrayList<Articales_Model> articalshow_list_value;
    ArrayList<OwnerStudentNameModel> roomnostudentarraylist, studentarraylist;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    LinearLayout show_enventry, listview_layout, assign_layout, addenventry;
    RelativeLayout lback_button;
    ScrollView scrollview_Addinventory, assign_inventory_scrollview;
    EditText item_name, quantity_item, price_item, item_code;
    Button submit_item, assign_button;
    LinearLayout room_no_layout, student_name_layout, artical_layout;
    TextView student_name_textview, roomno_textview, artical_textview;
    LinearLayout assign_layout_submit;
    Animation rotation;
    Intent in;
    RelativeLayout list_show_item;
    String mainitemname = "", mainitemcodevalue = "", item_id_main_value_api = "";
    String ownerid, propertyid_value;
    ListView roomno_list, student_list, artical_list;
    SharedStorage mShared;
    String item_name_value, quantity_item_value, price_item_value;
    Articale_inventory_Adapter adapter_data_month;
    Show_inventory_Adapter_list adapter_data_show_list;
    TextView artical_code_textview, statusvalue;
    String itemid_value = "", itmeid_main = "", itemcode_value = "";
    LinearLayout show_item_list_layout;
    ListView show_item_list_view;
    String roomno_value_main = "", roomno_id_value = "", tenant_value_main = "", tenant_id_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enventroy_activity_layout);
        mShared = SharedStorage.getInstance(this);
        in = getIntent();
        propertyid_value = in.getStringExtra("Propertyid");
        ownerid = in.getStringExtra("Ownerid");
        lback_button = (RelativeLayout) findViewById(R.id.lback_button);
        list_show_item = (RelativeLayout) findViewById(R.id.list_show_item);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        room_no_layout = (LinearLayout) findViewById(R.id.room_no_layout);
        student_name_layout = (LinearLayout) findViewById(R.id.student_name_layout);
        artical_layout = (LinearLayout) findViewById(R.id.artical_layout);
        student_name_textview = (TextView) findViewById(R.id.student_name_textview);
        roomno_textview = (TextView) findViewById(R.id.roomno_textview);
        artical_textview = (TextView) findViewById(R.id.artical_textview);
        assign_layout_submit = (LinearLayout) findViewById(R.id.assign_layout_submit);
        assign_button = (Button) findViewById(R.id.assign_button);
        artical_code_textview = (TextView) findViewById(R.id.artical_code_textview);
        statusvalue = (TextView) findViewById(R.id.statusvalue);
        addenventry = (LinearLayout) findViewById(R.id.addenventry);
        assign_layout = (LinearLayout) findViewById(R.id.assign_layout);
        assign_inventory_scrollview = (ScrollView) findViewById(R.id.assign_inventory_scrollview);
        scrollview_Addinventory = (ScrollView) findViewById(R.id.scrollview_Addinventory);
        item_name = (EditText) findViewById(R.id.item_name);
        quantity_item = (EditText) findViewById(R.id.quantity_item);
        price_item = (EditText) findViewById(R.id.price_item);
        submit_item = (Button) findViewById(R.id.submit_item);
        show_item_list_layout = (LinearLayout) findViewById(R.id.show_item_list_layout);
        show_item_list_view = (ListView) findViewById(R.id.show_item_list_view);

        lback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Enventory_Activity_Owner.this, HostelListActivity.class);
                startActivity(i);
            }
        });

        list_show_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_item_list_layout.setVisibility(View.VISIBLE);
                assign_inventory_scrollview.setVisibility(View.GONE);
                scrollview_Addinventory.setVisibility(View.GONE);
                addenventry.setBackgroundResource(R.color.lightgray);
                assign_layout.setBackgroundResource(R.color.lightgray);
                new ShowAdd_itemJson().execute();

            }
        });


        addenventry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                statusvalue.setText("");
                show_item_list_layout.setVisibility(View.GONE);
                student_name_textview.setText("");
                roomno_textview.setText("");
                artical_textview.setText("");
                addenventry.setBackgroundResource(R.color.grey);
                assign_layout.setBackgroundResource(R.color.lightgray);
                assign_inventory_scrollview.setVisibility(View.GONE);
                scrollview_Addinventory.setVisibility(View.VISIBLE);
            }
        });

        assign_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item_name.setText("");
                quantity_item.setText("");
                price_item.setText("");
                show_item_list_layout.setVisibility(View.GONE);
                addenventry.setBackgroundResource(R.color.lightgray);
                assign_layout.setBackgroundResource(R.color.grey);
                assign_inventory_scrollview.setVisibility(View.VISIBLE);
                scrollview_Addinventory.setVisibility(View.GONE);
            }
        });

        room_no_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetRoom_nodialog();
            }
        });

        student_name_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!roomno_id_value.equals("")) {
                    GetStudent_Name();
                } else {
                    Toast.makeText(Enventory_Activity_Owner.this, "Please Select Room No ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        artical_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String student_name_textview_value_data = student_name_textview.getText().toString();
                if (!student_name_textview_value_data.equals("")) {
                    GetArtical_List();
                } else {
                    Toast.makeText(Enventory_Activity_Owner.this, "Please Select Student", Toast.LENGTH_SHORT).show();
                }

            }
        });

        submit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item_name_value = item_name.getText().toString();
                quantity_item_value = quantity_item.getText().toString();
                price_item_value = price_item.getText().toString();
                if (item_name_value.equals("")) {
                    Toast.makeText(Enventory_Activity_Owner.this, "Please Enter Item Name", Toast.LENGTH_SHORT).show();
                } else if (quantity_item_value.equals("")) {
                    Toast.makeText(Enventory_Activity_Owner.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();

                } else {
                    new AddInventoryJsonParse().execute();
                }

            }
        });


        assign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String articales_id_value_main = artical_code_textview.getText().toString();
                String tenantidvalue_main = mainitemname;
                String roomin_id_main = roomno_id_value;
                String student_name_textview_value = student_name_textview.getText().toString();
                String roomno_textview_value = roomno_textview.getText().toString();
                String artical_textview_value = artical_textview.getText().toString();
                Calendar calendar = Calendar.getInstance();
                int dateO = calendar.get(Calendar.DAY_OF_MONTH);
                int monthO = calendar.get(Calendar.MONTH);
                int yearO = calendar.get(Calendar.YEAR);
                String currentdate_value_main = dateO + "/" + (monthO + 1) + "/" + yearO;

                if (roomno_textview_value.equals("")) {
                    Toast.makeText(Enventory_Activity_Owner.this, "Please Select Room No", Toast.LENGTH_SHORT).show();

                } else if (student_name_textview_value.equals("")) {

                    Toast.makeText(Enventory_Activity_Owner.this, "Please Select Student Name", Toast.LENGTH_SHORT).show();

                } else if (artical_textview_value.equals("")) {
                    Toast.makeText(Enventory_Activity_Owner.this, "Please Select Articals Name ", Toast.LENGTH_SHORT).show();

                } else {
                    new Assing_inventoryJsonparese(tenant_id_value, student_name_textview_value, roomno_textview_value, roomin_id_main, artical_textview_value, articales_id_value_main, currentdate_value_main).execute();
                }


            }
        });


    }


    private void GetStudent_Name() {

        final Dialog student_dialog = new Dialog(Enventory_Activity_Owner.this);
        student_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        student_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) student_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Select Student Name");
        student_list = (ListView) student_dialog.findViewById(R.id.list_country);
        loader = (ImageView) student_dialog.findViewById(R.id.student_loader);
        RelativeLayout student_cross_layout = (RelativeLayout) student_dialog.findViewById(R.id.country_cross_layout);
        student_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student_dialog.dismiss();
            }
        });

        if (NetworkConnection.isConnected(Enventory_Activity_Owner.this)) {
            new StudentNameJsonParse().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        student_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tenant_value_main = studentarraylist.get(position).getTanentusername();
                tenant_id_value = studentarraylist.get(position).getTanentid();
                student_name_textview.setText(tenant_value_main);
                student_dialog.dismiss();
            }
        });
        student_dialog.show();
    }


    private void GetRoom_nodialog() {

        final Dialog Roomno_dialog = new Dialog(Enventory_Activity_Owner.this);
        Roomno_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Roomno_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading = (TextView) Roomno_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Select Room No");
        roomno_list = (ListView) Roomno_dialog.findViewById(R.id.list_country);
        loader = (ImageView) Roomno_dialog.findViewById(R.id.student_loader);
        RelativeLayout roomno_cross_layout = (RelativeLayout) Roomno_dialog.findViewById(R.id.country_cross_layout);
        roomno_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roomno_dialog.dismiss();
            }
        });

        if (NetworkConnection.isConnected(Enventory_Activity_Owner.this)) {
            new RoomNoJsonParse().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        roomno_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomno_value_main = roomnostudentarraylist.get(position).getTenantroomno();
                roomno_id_value = roomnostudentarraylist.get(position).getTanentroomid();
                roomno_textview.setText(roomno_value_main);
                Roomno_dialog.dismiss();
            }
        });
        Roomno_dialog.show();
    }


    private void GetArtical_List() {

        final Dialog artical_dialog = new Dialog(Enventory_Activity_Owner.this);
        artical_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        artical_dialog.setContentView(R.layout.articals_inventory_dialog);
        Button canclebtn = (Button) artical_dialog.findViewById(R.id.alertCancelBtn);
        Button alertAddBtn = (Button) artical_dialog.findViewById(R.id.alertAddBtn);
        artical_list = (ListView) artical_dialog.findViewById(R.id.listView_inventory);
        loader = (ImageView) artical_dialog.findViewById(R.id.student_loader);

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artical_dialog.dismiss();
            }
        });


        if (NetworkConnection.isConnected(Enventory_Activity_Owner.this)) {
            new ArticalesJsonParse().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        alertAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (articalshow_list_value.size() > 0) {
                    for (int i = 0; i < articalshow_list_value.size(); i++) {

                        String item_id_main_value = articalshow_list_value.get(i).getItem_id();
                        String namevalue = articalshow_list_value.get(i).getItem_name();
                        String mainitemcode = articalshow_list_value.get(i).getCode();
                        mainitemname = mainitemname + "," + namevalue;
                        mainitemcodevalue = mainitemcodevalue + "," + mainitemcode;
                        item_id_main_value_api = item_id_main_value_api + "," + item_id_main_value;
                    }

                }
                String student_name_textview_value = student_name_textview.getText().toString();
                statusvalue.setText("For " + student_name_textview_value + " Selected Items is " + mainitemname);
                artical_dialog.dismiss();
                artical_textview.setText(mainitemname);
                artical_code_textview.setText(mainitemcodevalue);


            }
        });

        artical_dialog.show();
    }

    //get student name
    private class StudentNameJsonParse extends AsyncTask<String, String, String> {

        String name, result, message;
        private boolean IsError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                studentarraylist = new ArrayList<OwnerStudentNameModel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "userListOfAPropertyNew"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mShared.getUserPropertyId()));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("status");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("true")) {

                    JSONArray jsonarray = jsmain.getJSONArray("data");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        String statusvalue = jsonobj.getString("status");
                        String statusacvalue = jsonobj.getString("status_active");
                        if (statusvalue.equals("1") && statusacvalue.equals("")) {
                            OwnerStudentNameModel ownerstudentmodel = new OwnerStudentNameModel();
                            String roomidvalue = jsonobj.getString("room_id");
                            if (roomidvalue.equals(roomno_id_value)) {
                                ownerstudentmodel.setTanentusername(jsonobj.getString("tenant_fname") + " " + jsonobj.getString("tenant_lname"));
                                ownerstudentmodel.setTanentuserid(jsonobj.getString("user_id"));
                                ownerstudentmodel.setTanentroomid(jsonobj.getString("room_id"));
                                ownerstudentmodel.setTanentid(jsonobj.getString("id"));
                                ownerstudentmodel.setParentid(jsonobj.getString("parent_id"));
                                ownerstudentmodel.setTanentlistownerid(jsonobj.getString("owner_id"));
                                ownerstudentmodel.setHiredate(jsonobj.getString("property_hire_date"));
                                ownerstudentmodel.setTenantkeyid(jsonobj.getString("key_rcu_booking"));
                                ownerstudentmodel.setOwner_id(jsonobj.getString("owner_id"));
                                studentarraylist.add(ownerstudentmodel);
                            }

                        }
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
            loader.clearAnimation();
            loader.setVisibility(View.GONE);
            if (result.equalsIgnoreCase("true")) {
                if (studentarraylist.size() > 0) {

                    StudentName_Adapter adapter_data_month = new StudentName_Adapter(Enventory_Activity_Owner.this, studentarraylist);
                    student_list.setAdapter(adapter_data_month);
                }
            } else if (result.equalsIgnoreCase("false")) {
                Toast.makeText(Enventory_Activity_Owner.this, "No Tenant Avaliable", Toast.LENGTH_LONG).show();
            }
        }
    }


    //Get room no details from api
    private class RoomNoJsonParse extends AsyncTask<String, String, String> {

        String name, result, message;
        private boolean IsError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                roomnostudentarraylist = new ArrayList<OwnerStudentNameModel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "roomnolist"));
                nameValuePairs.add(new BasicNameValuePair("property_id", mShared.getUserPropertyId()));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        //{
                        OwnerStudentNameModel ownerstudentmodel = new OwnerStudentNameModel();
                        ownerstudentmodel.setTanentroomid(jsonobj.getString("id"));
                        ownerstudentmodel.setTenantroomno(jsonobj.getString("room_no"));
                        roomnostudentarraylist.add(ownerstudentmodel);
                        // }
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
            loader.clearAnimation();
            loader.setVisibility(View.GONE);
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                    if (roomnostudentarraylist.size() > 0) {

                        RoomNo_Adapter adapter_data_month = new RoomNo_Adapter(Enventory_Activity_Owner.this, roomnostudentarraylist);
                        roomno_list.setAdapter(adapter_data_month);
                    }
                } else if (result.equalsIgnoreCase("N")) {
                    Toast.makeText(Enventory_Activity_Owner.this, "No Data Found", Toast.LENGTH_LONG).show();
                }
            } else {
            }
        }
    }

    // add inventory item
    private class AddInventoryJsonParse extends AsyncTask<String, String, String> {

        String name, result, message;
        private boolean IsError = false;
        private ProgressDialog dialog = new ProgressDialog(Enventory_Activity_Owner.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                studentarraylist = new ArrayList<OwnerStudentNameModel>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "addinventoryitem"));
                nameValuePairs.add(new BasicNameValuePair("owner_id", ownerid));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_value));
                nameValuePairs.add(new BasicNameValuePair("item_name", item_name_value));
                nameValuePairs.add(new BasicNameValuePair("item_quantity", quantity_item_value));
                nameValuePairs.add(new BasicNameValuePair("price", price_item_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

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
            dialog.dismiss();
            if (!IsError) {
                if (result.equalsIgnoreCase("Y")) {
                    item_name.setText("");
                    quantity_item.setText("");
                    price_item.setText("");
                    Toast.makeText(Enventory_Activity_Owner.this, message, Toast.LENGTH_SHORT).show();

                } else if (result.equalsIgnoreCase("N")) {
                    Toast.makeText(Enventory_Activity_Owner.this, "please check network Connection", Toast.LENGTH_LONG).show();
                }
            } else {
            }
        }
    }

    //articale list
    //Get room no details from api
    private class ArticalesJsonParse extends AsyncTask<String, String, String> {

        String name, result, message;
        private boolean IsError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                Articles_arraylist = new ArrayList<Articales_Model>();
                articalshow_list_value = new ArrayList<>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "showinventoryitemlist"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        Articales_Model articlemodel = new Articales_Model();
                        articlemodel.setItem_id(jsonobj.getString("item_id"));
                        articlemodel.setItem_name(jsonobj.getString("item_name"));
                        articlemodel.setItem_quantity(jsonobj.getString("item_quantity"));
                        articlemodel.setItem_assign_quantity(jsonobj.getString("assign_item_quantity"));
                        Articles_arraylist.add(articlemodel);
                    }
                }
            } catch (Exception e) {
                IsError = true;
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loader.clearAnimation();
            loader.setVisibility(View.GONE);

            if (result.equalsIgnoreCase("Y")) {
                if (Articles_arraylist.size() > 0) {

                    adapter_data_month = new Articale_inventory_Adapter(Enventory_Activity_Owner.this, Articles_arraylist, articalshow_list_value, artical_list);
                    artical_list.setAdapter(adapter_data_month);
                }
            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(Enventory_Activity_Owner.this, "No item is avaliable", Toast.LENGTH_LONG).show();
            }

        }
    }

    //Get room no details from api
    private class Assing_inventoryJsonparese extends AsyncTask<String, String, String> {

        String name, result, message;
        private boolean IsError = false;
        String tenant_id_value, student_name_textview_value, currentdate_value_main, roomno_textview_value, roomin_id_main, artical_textview_value, articales_id_value_main;
        private ProgressDialog dialog = new ProgressDialog(Enventory_Activity_Owner.this);

        public Assing_inventoryJsonparese(String tenant_id_value, String student_name_textview_value, String roomno_textview_value, String roomin_id_main, String artical_textview_value, String articales_id_value_main, String currentdate_value_main) {
            this.tenant_id_value = tenant_id_value;
            this.student_name_textview_value = student_name_textview_value;
            this.roomno_textview_value = roomno_textview_value;
            this.roomin_id_main = roomin_id_main;
            this.artical_textview_value = artical_textview_value;
            this.articales_id_value_main = articales_id_value_main;
            this.currentdate_value_main = currentdate_value_main;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                Articles_arraylist = new ArrayList<Articales_Model>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "assigninventoryitem"));
                nameValuePairs.add(new BasicNameValuePair("room_id", roomin_id_main));
                nameValuePairs.add(new BasicNameValuePair("room_no", roomno_textview_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_name", student_name_textview_value));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenant_id_value));
                nameValuePairs.add(new BasicNameValuePair("artical_id", item_id_main_value_api));
                nameValuePairs.add(new BasicNameValuePair("artical_code", mainitemcodevalue));
                nameValuePairs.add(new BasicNameValuePair("artical_name", mainitemname));
                nameValuePairs.add(new BasicNameValuePair("assign_date", currentdate_value_main));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                if (result.equalsIgnoreCase("Y")) {

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
            dialog.dismiss();

            if (result.equalsIgnoreCase("Y")) {
                Toast.makeText(Enventory_Activity_Owner.this, message, Toast.LENGTH_SHORT).show();
                student_name_textview.setText("");
                roomno_textview.setText("");
                artical_textview.setText("");
                mainitemcodevalue = "";
                mainitemname = "";
                item_id_main_value_api = "";
                statusvalue.setText("");

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(Enventory_Activity_Owner.this, "No item is avaliable", Toast.LENGTH_LONG).show();
            }

        }
    }

    // for list showing about add item
    //Get room no details from api
    private class ShowAdd_itemJson extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Enventory_Activity_Owner.this);


        String name, result, message;
        private boolean IsError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {
                Articles_arraylist = new ArrayList<Articales_Model>();
                articalshow_list_value = new ArrayList<>();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "showinventoryitemlist"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid_value));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                if (result.equalsIgnoreCase("Y")) {

                    JSONArray jsonarray = jsmain.getJSONArray("records");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);

                        Articales_Model articlemodel = new Articales_Model();
                        articlemodel.setItem_id(jsonobj.getString("item_id"));
                        articlemodel.setItem_name(jsonobj.getString("item_name"));
                        articlemodel.setItem_quantity(jsonobj.getString("item_quantity"));
                        articlemodel.setItem_assign_quantity(jsonobj.getString("assign_item_quantity"));
                        articlemodel.setItem_price(jsonobj.getString("price"));
                        String qq = String.valueOf("101");
                        articlemodel.setItem_remaining_quantity(qq);
                        Articles_arraylist.add(articlemodel);
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result.equalsIgnoreCase("Y")) {
                if (Articles_arraylist.size() > 0) {

                    adapter_data_show_list = new Show_inventory_Adapter_list(Enventory_Activity_Owner.this, Articles_arraylist);
                    show_item_list_view.setAdapter(adapter_data_show_list);
                }
            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(Enventory_Activity_Owner.this, "No item is avaliable", Toast.LENGTH_LONG).show();
            }
        }
    }
    //.......
}