package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.ShowAssign_Inventory_Activity;
import com.krooms.hostel.rental.property.app.modal.Articales_Model;
import com.krooms.hostel.rental.property.app.modal.OwnerStudentNameModel;
import com.krooms.hostel.rental.property.app.modal.ShowInventory_item_model;
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
import java.util.List;

/**
 * Created by admin on 4/20/2017.
 */
public class ShowAssignInventory_Adapter extends BaseAdapter {

    ArrayList<ShowInventory_item_model> showinventory_arraylist = new ArrayList<>();
    private Activity context;
    LinearLayout iteam_remove;
    String item_code_value = "";
    String tenantidvalue, propertyid;

    public ShowAssignInventory_Adapter(Activity context, ArrayList<ShowInventory_item_model> showinventory_arraylist, String tenantidvalue, String propertyid) {
        this.context = context;
        this.showinventory_arraylist = showinventory_arraylist;
        this.tenantidvalue = tenantidvalue;
        this.propertyid = propertyid;
    }

    @Override
    public int getCount() {
        return showinventory_arraylist.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater;

        inflater = context.getLayoutInflater();
        View rowView = null;
        {
            inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.show_add_layout_item, null, true);
            final TextView iteam_code = (TextView) rowView.findViewById(R.id.iteam_code_view);
            final TextView item_name = (TextView) rowView.findViewById(R.id.iteam_name);
            final TextView iteam_assign_date = (TextView) rowView.findViewById(R.id.iteam_assign_date);
            iteam_remove = (LinearLayout) rowView.findViewById(R.id.iteam_remove);
            final TextView artical_id = (TextView) rowView.findViewById(R.id.artical_id);
            String item_name_value = showinventory_arraylist.get(position).getItem_name();
            String item_code_value = showinventory_arraylist.get(position).getItem_code();
            String artical_id_value = showinventory_arraylist.get(position).getItem_id();
            String assign_datevalue = showinventory_arraylist.get(position).getAssign_date();
            iteam_assign_date.setText(assign_datevalue);
            iteam_code.setText(item_code_value);
            item_name.setText(item_name_value);
            artical_id.setText(artical_id_value);

            iteam_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String artical_code_value = iteam_code.getText().toString();
                    String item_name_value = item_name.getText().toString();
                    String artical_id_value_main = artical_id.getText().toString();
                    new RemoveAssignInventoryItme(artical_code_value, item_name_value, artical_id_value_main).execute();

                }
            });


        }
        return rowView;
    }

    private class RemoveAssignInventoryItme extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

        String name, result, message;
        private boolean IsError = false;
        String artical_code_value, item_name_value, artical_id_value_main;

        public RemoveAssignInventoryItme(String artical_code_value, String item_name_value, String artical_id_value_main) {
            this.artical_code_value = artical_code_value;
            this.item_name_value = item_name_value;
            this.artical_id_value_main = artical_id_value_main;
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "removeassignitem"));
                nameValuePairs.add(new BasicNameValuePair("property_id", propertyid));
                nameValuePairs.add(new BasicNameValuePair("tenant_id", tenantidvalue));
                nameValuePairs.add(new BasicNameValuePair("artical_code", artical_code_value));
                nameValuePairs.add(new BasicNameValuePair("item_name", item_name_value));
                nameValuePairs.add(new BasicNameValuePair("item_id", artical_id_value_main));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");

                //  {"flag":"Y","records":[{"item_id":"1","item_name":"fan","item_quantity":"12","price":"12000","property_id":"1","owner_id":"1"},{"item_id":"2","item_name":"basket","item_quantity":"30","price":"300","property_id":"1","owner_id":"1"}],"message":"Item is Available !"}

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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result.equalsIgnoreCase("Y")) {
                Toast.makeText(context, "Remove Assign Item", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, ShowAssign_Inventory_Activity.class);
                i.putExtra("propertyidseprate", propertyid);
                i.putExtra("tenantid", tenantidvalue);
                context.startActivity(i);

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(context, "No item is avaliable", Toast.LENGTH_LONG).show();
            }

        }
    }


}
