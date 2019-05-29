package com.krooms.hostel.rental.property.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.adapter.Month_Custom_List;
import com.krooms.hostel.rental.property.app.adapter.SelectPropertymultiple_Adapter;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.Propertynamemodel;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 3/25/2017.
 */
public class SelectPropertymultipleActivity extends AppCompatActivity
{
    Animation rotation;
    ImageView loader;
    String propertyid_valuemain="",keyvalue="",property_name="";
    ListView student_list_dialog;
    Intent in;
    String propertydata,owneridvalue;
    LinearLayout spinnerlayout;
    TextView selectproperty,selectpropetyidvalue,propertyname;
    Button tanentpaid;
    ArrayList<Propertynamemodel> propertyarraylist;
    Propertynamemodel Propertyadapter;
    String[] noofproperty;
    SharedStorage mSharedStorage;
    RelativeLayout fback_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectpropertyactivity);
        mSharedStorage = SharedStorage.getInstance(SelectPropertymultipleActivity.this);
        in=getIntent();
        owneridvalue=in.getStringExtra("Ownerid");
        spinnerlayout=(LinearLayout)findViewById(R.id.spinnerlayout);
        selectproperty=(TextView)findViewById(R.id.selectproperty);
        selectpropetyidvalue=(TextView)findViewById(R.id.selectpropetyidvalue);
        propertyname=(TextView)findViewById(R.id.propertyname);
        fback_button=(RelativeLayout)findViewById(R.id.fback_button);
        rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);

        fback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SelectPropertymultipleActivity.this,HostelListActivity.class);
                startActivity(i);
            }
        });

        tanentpaid=(Button)findViewById(R.id.tanentpaid);
        tanentpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String propertyid_key_value = selectproperty.getText().toString();
                String property_idvalue=selectpropetyidvalue.getText().toString();
                String property_name=propertyname.getText().toString();
                if (!propertyid_key_value.equals("")) {
                    mSharedStorage.setUserPropertyId(property_idvalue);
                    mSharedStorage.setPropertyName(property_name);
                   //Toast.makeText(SelectPropertymultipleActivity.this, "P-"+property_name, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SelectPropertymultipleActivity.this, HostelListActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(SelectPropertymultipleActivity.this, "Please Fill Property Name", Toast.LENGTH_SHORT).show();
                }

            }
        });
        spinnerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertydialogMethod();
            }
        });

    }
    //these is for student name list
    public void  propertydialogMethod()
    {
        final Dialog property_dialog=new Dialog(SelectPropertymultipleActivity.this);
        property_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        property_dialog.setContentView(R.layout.custom_dialog_country);
        TextView statepopup_heading=(TextView)property_dialog.findViewById(R.id.statepopup_heading);
        statepopup_heading.setText("Property List");
        loader=(ImageView)property_dialog.findViewById(R.id.student_loader);
        student_list_dialog=(ListView)property_dialog.findViewById(R.id.list_country);
        RelativeLayout month_cross_layout=(RelativeLayout)property_dialog.findViewById(R.id.country_cross_layout);


        if(NetworkConnection.isConnected(SelectPropertymultipleActivity.this))
        {
            new MultiplePropertyByOwnerid().execute();
        }else
        {
            Toast.makeText(getApplicationContext(),"Please check your network connection", Toast.LENGTH_SHORT).show();
        }

        month_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                property_dialog.dismiss();
            }
        });
        student_list_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                propertyid_valuemain=propertyarraylist.get(position).getPropertuid().toString();
                keyvalue=propertyarraylist.get(position).getPropertkeyid().toString();
                property_name= propertyarraylist.get(position).getPropertyname().toString();
                selectproperty.setText(property_name);
                selectpropetyidvalue.setText(propertyid_valuemain);
                propertyname.setText(property_name);
                property_dialog.dismiss();
            }
        });
        property_dialog.show();
    }


    private class MultiplePropertyByOwnerid extends AsyncTask<String, String, String> {
        private ProgressDialog dialog = new ProgressDialog(SelectPropertymultipleActivity.this);

        int count;
        String name,result;
        private boolean IsError =false;
        String message;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loader.startAnimation(rotation);
            loader.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... args)
        {

            propertyarraylist=new ArrayList<Propertynamemodel>();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            //String result = null;
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action","multiplepropertyowner"));
                nameValuePairs.add(new BasicNameValuePair("owner_id",owneridvalue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());

                //{"flag":"Y","data":[{"property_id":"1","key_property_information":"KR\/H\/1","owner_id":"1","rating":"0","user_id":"1","property_type":"1","property_name":"Priya hostel","property_nature":"nature","paid_service":"","owner_address":"AB road","landmark":"Vijay nagar","state":"1","city":"1","colony":"32","pincode":"452010","lat":"23.989595","long":"12.9794995","total_area":"0","video_url":"","google_url":"","super_built_up_area":"","property_face":"Property Face","kitchen":"","kitchen_type":"","furnish_type":"","property_feature":"","property_list":"property-list","water_supply":"Select Supply","power_backup":"Select Backup","other_aminities":"","no_of_room":"7","for_whom":"Girls","railway_station":"","bus_stand":"","airport":"","nearest":"","square":"","tenant_type":"0","curfew_time":"","vehicle":"No Of Vehicle","smoking":"Smoking","drinking":"Drinking","no_of_people":"No Of People","rent_amount":"10000","advance":"5000","maintenance":"","water_bill":"","other_expenses":"","electricity":"","policy_fix_sqr":"","created_date":"2017-03-27 09:45:03","weightage":"","property_image":"","description":"Jsjsjdj","status":"1"}],"message":"Get Data Successfull"}

                JSONObject jsmain =new JSONObject(object);
                result = jsmain.getString("flag");
                message=jsmain.getString("message");
                if (result.equals("Y")) {
                   JSONArray jsonarray = jsmain.getJSONArray("data");
                    Log.d("uu 0", "" + jsonarray);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        Propertynamemodel propertyid=new Propertynamemodel();
                        String propertyid_value=jsonobj.getString("key_property_information");
                        propertyid.setPropertuid(jsonobj.getString("property_id"));
                        propertyid.setPropertyname(jsonobj.getString("property_name"));
                        propertyid.setPropertkeyid(propertyid_value);
                        propertyarraylist.add(propertyid);
                    }

                }
            } catch (Exception e)
            {
                IsError = true;
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            loader.clearAnimation();
            loader.setVisibility(View.GONE);
            if(result.equalsIgnoreCase("Y"))
            {
                SelectPropertymultiple_Adapter adapter_data_month = new SelectPropertymultiple_Adapter(SelectPropertymultipleActivity.this,propertyarraylist);
                student_list_dialog.setAdapter(adapter_data_month);

            } else{
            }
        }

    }


}
