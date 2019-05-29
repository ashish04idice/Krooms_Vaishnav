package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.database.DataBaseAdapter;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.HostelDetailActivity;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.modal.PropertyModal;
import com.krooms.hostel.rental.property.app.modal.StateModal;
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
 * Created by user on 2/19/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {
    TextView propertyAddress;
    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PropertyModal> mList = new ArrayList<PropertyModal>();
    ViewHolder holder;
    View convertView;
    String area = "";

    public CustomPagerAdapter(Activity context, ArrayList<PropertyModal> list) {
        mContext = context;
        mActivity = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        holder = null;
        convertView = mLayoutInflater.inflate(R.layout.hostel_list_item, container, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.property_icon_id);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        TextView propertyName = (TextView) convertView.findViewById(R.id.property_name_id);
        propertyAddress = (TextView) convertView.findViewById(R.id.property_address_id);
        TextView propertyType = (TextView) convertView.findViewById(R.id.property_type_id);
        TextView rentProperty = (TextView) convertView.findViewById(R.id.property_price_id);
        ImageButton propertyLocation = (ImageButton) convertView.findViewById(R.id.propertyLocation);
        propertyName.setText(mList.get(position).getProperty_name());
        DataBaseAdapter mDBAdapter = new DataBaseAdapter(mActivity);
        mDBAdapter.createDatabase();
        mDBAdapter.open();


        if (mList.get(position).getProperty_area().equals("")) {
            area = "Area";
        } else {

            String propertyareaid = mList.get(position).getProperty_area();
            String areanamevalue = mList.get(position).getColonyname();

            if (areanamevalue.equalsIgnoreCase("")) {
                area = "Area";
                propertyAddress.setText(area);
            } else {
                area = propertyareaid;
                propertyAddress.setText(areanamevalue);
            }
        }
        mDBAdapter.close();
        rentProperty.setText("Rent: " + mList.get(position).getProperty_rent_price());
        if (mList.get(position).getProperty_type_id().equals("1")) {
            propertyType.setText("Hostel");
        } else if (mList.get(position).getProperty_type_id().equals("2")) {
            propertyType.setText("PG");
        } else if (mList.get(position).getProperty_type_id().equals("3")) {
            propertyType.setText("Service Apartment");
        } else if (mList.get(position).getProperty_type_id().equals("4")) {
            propertyType.setText("Room");
        } else if (mList.get(position).getProperty_type_id().equals("5")) {
            propertyType.setText("Studio Apartment");
        }

        propertyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        if (mList.get(position).getPropertyImage().size() != 0) {

            if (!mList.get(position).getPropertyImage().get(0).getPhoto_Url().equals("Not Available")) {
                Common.loadImage(mActivity, mList.get(position).getPropertyImage().get(0).getPhoto_Url(), imageView);
            } else {

                imageView.setImageResource(R.drawable.ic_default_background);

            }

        }


        container.addView(convertView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < Common.CommonPropertList.size(); i++) {
                    if (mList.get(position).getProperty_id().equals(Common.CommonPropertList.get(i).getProperty_id())) {
                        Intent it = new Intent(mActivity, HostelDetailActivity.class);
                        it.putExtra("selected_index", i);
                        it.putExtra("property_id", mList.get(position).getProperty_id());
                        mActivity.startActivity(it);
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    class ViewHolder {

    }

    public class GetAreaList extends AsyncTask<String, String, String> {
        String areanamevalue;
        String name, result, message, areaId;
        ProgressDialog pd;
        private boolean IsError = false;

        public GetAreaList(String areaId) {
            this.areaId = areaId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(WebUrls.MAIN_URL);
            Log.d("uu 1", "" + post);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("action", "getAreaListByAreaId"));
                nameValuePairs.add(new BasicNameValuePair("area_id", areaId));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                String objectmain = object.replace("\t", "").replace("\n", "");
                System.out.print(objectmain);
                JSONObject jsmain = new JSONObject(objectmain);
                result = jsmain.getString("flag");
                message = jsmain.getString("message");
                JSONArray jsonarray = jsmain.getJSONArray("records");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobj = jsonarray.getJSONObject(i);
                    areanamevalue = jsonobj.getString("area_name");
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
            if (result.equalsIgnoreCase("Y")) {
                if (areanamevalue.equalsIgnoreCase("")) {
                    area = "Area";
                    propertyAddress.setText(area);
                } else {
                    area = areanamevalue;
                    propertyAddress.setText(area);
                }

            } else if (result.equalsIgnoreCase("N")) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
