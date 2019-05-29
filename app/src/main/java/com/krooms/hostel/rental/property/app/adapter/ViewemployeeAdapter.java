package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.activity.HostelListActivity;
import com.krooms.hostel.rental.property.app.modal.OtherownerdetailsModel;
import com.krooms.hostel.rental.property.app.util.WebUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 3/29/2017.
 */
public class ViewemployeeAdapter extends BaseAdapter {

    ArrayList<OtherownerdetailsModel> ownerlist = new ArrayList<>();
    private Activity context;
    String Contectno, Oid, Propertyid;

    public ViewemployeeAdapter(Activity context, ArrayList<OtherownerdetailsModel> ownerlist) {
        this.context = context;
        this.ownerlist = ownerlist;
    }

    @Override
    public int getCount() {
        return ownerlist.size();
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
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater;
        inflater = context.getLayoutInflater();
        View rowView = null;
        {
            rowView = inflater.inflate(R.layout.viewemployeelististem, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.property_user_name);
            Button remove = (Button) rowView.findViewById(R.id.remove);
            TextView property_user_book_room = (TextView) rowView.findViewById(R.id.property_user_book_room);
            TextView property_user_book_date = (TextView) rowView.findViewById(R.id.property_user_book_date);
            TextView property_user_role = (TextView) rowView.findViewById(R.id.property_usertype);

            txtTitle.setText(ownerlist.get(position).getFullname().toString());
            property_user_book_room.setText(ownerlist.get(position).getEmail().toString());
            property_user_book_date.setText(ownerlist.get(position).getContactno().toString());

            String role = ownerlist.get(position).getRoletype();

            if (role.equals("6")) {
                property_user_role.setText("Warden");
            } else {
                property_user_role.setText("Accountant");
            }

            Contectno = ownerlist.get(position).getContactno().toString();
            Oid = ownerlist.get(position).getOid().toString();
            Propertyid = ownerlist.get(position).getPropertyid().toString();

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new RemoveUser().execute();

                }
            });
        }
        return rowView;
    }

    //this class used to send Fine to Server
    private class RemoveUser extends AsyncTask<String, String, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            String result = null;
            String url = null;
            ArrayList<NameValuePair> listNameValuePairs;
            listNameValuePairs = new ArrayList<NameValuePair>();
            listNameValuePairs.add(new BasicNameValuePair("action", "otherownerremove"));
            listNameValuePairs.add(new BasicNameValuePair("o_id", Oid));
            listNameValuePairs.add(new BasicNameValuePair("property_id", Propertyid));
            listNameValuePairs.add(new BasicNameValuePair("mobile", Contectno));
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

                Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject obj = new JSONObject(result);
                    status = obj.getString("flag");
                    msg = obj.getString("message");

                    if (status.equalsIgnoreCase("Y")) {

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, HostelListActivity.class);
                        context.startActivity(intent);

                    } else {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
