package com.krooms.hostel.rental.property.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.common.Validation;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hi on 3/8/2017.
 */
public class Add_Other_Owner_Activity extends Activity implements View.OnClickListener {

    EditText namee, mobileno, emailid;
    Button btnsubmit;
    RelativeLayout btnbackpress;
    LinearLayout btnselectownername;
    Dialog dialogownername, month_dialog;
    TextView textownername;
    String hname, Mobileno, Emailid, Sownertype;
    private Validation mValidation = null;
    String userid, propertyid, ownerid, Userrole;

    String[] Ownertype ={
            "Accountant",
            "Warden"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_other_owner_layout);
        FindviewId();
        Intent i = getIntent();
        userid = i.getStringExtra("OuserId");
        propertyid = i.getStringExtra("OpropertyId");
        ownerid = i.getStringExtra("Ownerid");
        mValidation = new Validation(this);
        textownername.setText("Employee Type");
        btnbackpress.setOnClickListener(this);
        btnselectownername.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
    }

    private void FindviewId() {

        btnbackpress = (RelativeLayout) findViewById(R.id.lback_button);
        btnselectownername = (LinearLayout) findViewById(R.id.selectownertype);
        namee = (EditText) findViewById(R.id.editname);
        mobileno = (EditText) findViewById(R.id.editmobileno);
        emailid = (EditText) findViewById(R.id.editemailid);
        btnsubmit = (Button) findViewById(R.id.btn_submit);
        textownername = (TextView) findViewById(R.id.txtownername);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lback_button:
                Intent ii = new Intent(Add_Other_Owner_Activity.this, HostelListActivity.class);
                startActivity(ii);
                break;

            case R.id.selectownertype:
                GetOwnerTypeDailog();
                break;

            case R.id.btn_submit:
                SendtoServer();
                break;
        }
    }

    private void SendtoServer(){

        Sownertype = textownername.getText().toString().trim();
        hname = namee.getText().toString().trim();
        Mobileno = mobileno.getText().toString().trim();
        Emailid = emailid.getText().toString().trim();

        if (Sownertype.equals("Employee Type")){

            Toast.makeText(this, "Please Select Employee Type", Toast.LENGTH_SHORT).show();

        } else if (hname.length() == 0) {

            Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();

        } else if (Mobileno.length() == 0 || Mobileno.length() < 10) {

            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();

        } else if (Emailid.length() != 0) {
            if (!mValidation.checkEmail(Emailid)) {
                Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
            } else {
                if (Sownertype.equals("Accountant")) {
                    Userrole = "5";// 5 for Accountant
                } else {
                    Userrole = "6";// 6 for warden
                }

                new SendDetails().execute();
            }
        } else {

            if (Sownertype.equals("Accountant")) {
                Userrole = "5";// 5 for Accountant
            } else {
                Userrole = "6";// 6 for warden
            }

            new SendDetails().execute();

        }

    }

    public void GetOwnerTypeDailog() {
        dialogownername = new Dialog(Add_Other_Owner_Activity.this);
        dialogownername.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogownername.setContentView(R.layout.custom_dialog_country);
        ListView month_list = (ListView) dialogownername.findViewById(R.id.list_country);
        Month_Custom_List adapter_data_month = new Month_Custom_List(Add_Other_Owner_Activity.this, Ownertype);
        month_list.setAdapter(adapter_data_month);
        RelativeLayout month_cross_layout = (RelativeLayout) dialogownername.findViewById(R.id.country_cross_layout);
        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogownername.dismiss();
            }
        });
        month_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String owner_value = Ownertype[position].toString();
                textownername.setText(owner_value);
                dialogownername.dismiss();
            }
        });

        dialogownername.show();
    }

    //this class used to send owner type details to server
    private class SendDetails extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Add_Other_Owner_Activity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "addotherowner"));
            listNameValuePairs.add(new BasicNameValuePair("property_id", propertyid));
            listNameValuePairs.add(new BasicNameValuePair("owner_userid", ownerid));
            listNameValuePairs.add(new BasicNameValuePair("name", hname));
            listNameValuePairs.add(new BasicNameValuePair("mobile", Mobileno));
            listNameValuePairs.add(new BasicNameValuePair("email", Emailid));
            listNameValuePairs.add(new BasicNameValuePair("device_type", "Android"));
            listNameValuePairs.add(new BasicNameValuePair("role_id", Userrole));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            String msg = null;
            String status = null;

            if (result == null) {

                Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("status");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        Intent ii = new Intent(Add_Other_Owner_Activity.this, HostelListActivity.class);
                        startActivity(ii);

                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
