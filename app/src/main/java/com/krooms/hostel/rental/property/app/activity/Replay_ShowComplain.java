package com.krooms.hostel.rental.property.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.modal.Complaindetailowner;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ashish on 19/1/17.
 */

public class Replay_ShowComplain extends AppCompatActivity implements View.OnClickListener{


    TextView txtshowcomp,txtreplycomp;
    Button btnreplay,btnfeedback;
    LinearLayout layout1,layout2;
    RelativeLayout backarrow;
    Context context;
    String feedback_id="",owner_id="",property_id="",title="",description="",tenant_name="",user_id="",reply_msg="",date,tenant_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replyshowcomplain_activity);

        context=this;

        DateFormat df = new SimpleDateFormat("d/MM/yyyy,HH:mm:ss");
        date = df.format(Calendar.getInstance().getTime());
        txtshowcomp=(TextView)findViewById(R.id.txtreplyshowcomp);
        txtreplycomp=(TextView)findViewById(R.id.replycomplaintmsg);
        btnreplay=(Button)findViewById(R.id.btnreply_complaint);
        btnfeedback=(Button)findViewById(R.id.btnsubmit_complaint);
        layout1=(LinearLayout)findViewById(R.id.replytextlayout);
        layout2=(LinearLayout)findViewById(R.id.replybtnlayout);
        backarrow=(RelativeLayout)findViewById(R.id.back_arrow);

        Intent ii=getIntent();

        feedback_id=ii.getStringExtra("FeedbackId");
        owner_id=ii.getStringExtra("OwnerId");
        property_id=ii.getStringExtra("PropertyId");
        title=ii.getStringExtra("Title");
        description=ii.getStringExtra("Description");
        tenant_name=ii.getStringExtra("TenantName");
        user_id=ii.getStringExtra("UserId");
        tenant_id=ii.getStringExtra("TenantId");
        txtshowcomp.setText(description);

        btnreplay.setOnClickListener(this);
        btnfeedback.setOnClickListener(this);
        backarrow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnreply_complaint:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                break;

            case R.id.back_arrow:

                finish();

                break;

            case R.id.btnsubmit_complaint:

             reply_msg=txtreplycomp.getText().toString().trim();
                if (reply_msg.length()==0){

                    Toast.makeText(this, "Please Enter Feedback", Toast.LENGTH_SHORT).show();

                }else {

                    new SendFeedback().execute();
                }

                break;

        }

    }

    public class SendFeedback extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(Replay_ShowComplain.this);

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
            listNameValuePairs.add(new BasicNameValuePair("action","owner_complain"));
            listNameValuePairs.add(new BasicNameValuePair("property_id",property_id));
            listNameValuePairs.add(new BasicNameValuePair("owner_id",owner_id));
            listNameValuePairs.add(new BasicNameValuePair("user_id",user_id));
            listNameValuePairs.add(new BasicNameValuePair("feedback_id",feedback_id));
            listNameValuePairs.add(new BasicNameValuePair("description",reply_msg));
            listNameValuePairs.add(new BasicNameValuePair("date",date));
            listNameValuePairs.add(new BasicNameValuePair("tenant_id",tenant_id));
            listNameValuePairs.add(new BasicNameValuePair("tenant_name",tenant_name));
            url = WebUrls.MAIN_URL;
            result = Utility.postParamsAndfindJSON(url, listNameValuePairs);
            listNameValuePairs.clear();
           // Log.e("result is ----", result);
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
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equals("Y")) {

                        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                        Intent ii=new Intent(Replay_ShowComplain.this,HostelListActivity.class);
                        startActivity(ii);


                    } else{
                        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }
    }
}
