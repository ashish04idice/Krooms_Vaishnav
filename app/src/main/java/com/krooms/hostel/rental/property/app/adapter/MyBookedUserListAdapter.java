package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.activity.TenantDetailActivity;
import com.krooms.hostel.rental.property.app.common.Common;
import com.krooms.hostel.rental.property.app.common.RoundedTransformation;
import com.krooms.hostel.rental.property.app.modal.PropertyUserModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2/1/2016.
 */
public class MyBookedUserListAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private ArrayList<PropertyUserModal> _listArray;
    String comefrom;
    String value;
    String useridmainvalue, parentvalueid;
    String holduserstatus;
    String usertype="";
    boolean isLoading;

    public MyBookedUserListAdapter(Activity context, ArrayList<PropertyUserModal> objects, String key, String useridmainvalue, String parentvalueid, String holduserstatus, String usertype) {
        this.context = context;
        this.activity = context;
        this._listArray = objects;
        comefrom = key;
        this.useridmainvalue = useridmainvalue;
        this.parentvalueid = parentvalueid;
        this.holduserstatus = holduserstatus;
        this.usertype=usertype;
    }


    @Override
    public int getCount() {
        return _listArray.size();
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

    public class ViewHolder {

        ImageView userImage;
        TextView userName, bookRoom, bookBad, bookDate, userAmount, userAddress;
        LinearLayout click, laysms;
        CheckBox smscheckBox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        isLoading=true;
        final ViewHolder _viewHolder;
        View v = convertView;
        if (null == convertView) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.myproeprty_user_list_item, null);
            _viewHolder = new ViewHolder();
            _viewHolder.userName = (TextView) v.findViewById(R.id.property_user_name);
            _viewHolder.bookRoom = (TextView) v.findViewById(R.id.property_user_book_room);
            _viewHolder.bookBad = (TextView) v.findViewById(R.id.property_user_book_room_bad);
            _viewHolder.bookDate = (TextView) v.findViewById(R.id.property_user_book_date);
            _viewHolder.userAddress = (TextView) v.findViewById(R.id.property_user_address);
            _viewHolder.userAmount = (TextView) v.findViewById(R.id.property_user_book_amount);
            _viewHolder.userImage = (ImageView) v.findViewById(R.id.imageview_itm_user);
            _viewHolder.click = (LinearLayout) v.findViewById(R.id.layout_top);
            _viewHolder.laysms = (LinearLayout) v.findViewById(R.id.laysms);
            _viewHolder.smscheckBox = (CheckBox) v.findViewById(R.id.smscheckBox);
            v.setTag(_viewHolder);

        } else {
            _viewHolder = (ViewHolder) convertView.getTag();
        }

        _viewHolder.userName.setText(_listArray.get(position).getTenant_fname() + " " + _listArray.get(position).getTenant_lname());
        _viewHolder.bookRoom.setText("Room no: " + _listArray.get(position).getBookedRoom());
        _viewHolder.bookBad.setText("Rent Amount: " + _listArray.get(position).getMainroomamount() + "/m");
        _viewHolder.userAmount.setText("Status: " + _listArray.get(position).getPaymentStatus());
        _viewHolder.bookDate.setText("Join Date: " + _listArray.get(position).getProperty_hire_date());
        _viewHolder.userAddress.setText("Address: " + _listArray.get(position).getTenant_permanent_address());



if(usertype.equals("2") || usertype.equals("4")){

    _viewHolder.smscheckBox.setVisibility(View.GONE);
}else {
    _viewHolder.smscheckBox.setVisibility(View.GONE);
}

        if(usertype.equals("2") || usertype.equals("4")) {
        }else{
        }

        Picasso.with(context)
                .load(WebUrls.IMG_URL + _listArray.get(position).getTenant_photo())
                .placeholder(R.drawable.user_xl)
                .error(R.drawable.user_xl)
                .transform(new RoundedTransformation(activity))
                .into(_viewHolder.userImage);

        _viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.Config("rashan    " + _listArray.get(position).getRashan_card_no());
                Common.Config("passport    " + _listArray.get(position).getPassport_no());

                Intent mIntent = new Intent(activity, TenantDetailActivity.class);
                mIntent.putExtra("modal", (Parcelable) _listArray.get(position));
                mIntent.putExtra("useridv", _listArray.get(position).getT_user_id());
                String newt_id = _listArray.get(position).getT_user_id();
                //for back button handel
                mIntent.putExtra("KEY", comefrom);
                if (comefrom.equalsIgnoreCase("Parent")) {
                    mIntent.putExtra("useridd", useridmainvalue);
                    mIntent.putExtra("parentidd", parentvalueid);
                }

                mIntent.putExtra("NEWTID", newt_id);
                String tenantid = _listArray.get(position).getTenant_id();
                mIntent.putExtra("tid", tenantid);
                mIntent.putExtra("roomid", _listArray.get(position).getBookedRoomId());
                mIntent.putExtra("tfname", _listArray.get(position).getTenant_fname());
                mIntent.putExtra("tlname", _listArray.get(position).getTenant_lname());
                mIntent.putExtra("tfathername", _listArray.get(position).getTenant_father_name());
                mIntent.putExtra("tfather_no", _listArray.get(position).getTenant_father_contact_no());
                mIntent.putExtra("temail", _listArray.get(position).getEmail_id());
                mIntent.putExtra("tcontact", _listArray.get(position).getContact_number());
                mIntent.putExtra("taddress", _listArray.get(position).getTenant_permanent_address());
                mIntent.putExtra("tlandmark", _listArray.get(position).getLandmark());
                String propertyidforseparatevalue = _listArray.get(position).getPropertyId();
                mIntent.putExtra("propertyidforseparate", propertyidforseparatevalue);
                mIntent.putExtra("thiredate", _listArray.get(position).getProperty_hire_date());
                mIntent.putExtra("tleavedate", _listArray.get(position).getProperty_leave_date());
                mIntent.putExtra("tofc_institute", _listArray.get(position).getTenant_office_institute());
                mIntent.putExtra("tofc_contact", _listArray.get(position).getTenant_contact_number());
                mIntent.putExtra("twork_detail", _listArray.get(position).getTenant_work_detail());
                mIntent.putExtra("tguarantorname", _listArray.get(position).getGuarantor_name());
                mIntent.putExtra("tguarantoraddress", _listArray.get(position).getGuarantor_address());
                mIntent.putExtra("tguarantorcontact", _listArray.get(position).getGuarantor_contact_number());
                mIntent.putExtra("tpmode", _listArray.get(position).getPaymentMode());
                mIntent.putExtra("tporderid", _listArray.get(position).getPaymentOrderId());
                mIntent.putExtra("tptransactionid", _listArray.get(position).getPaymentTransactionId());
                mIntent.putExtra("troomamount", _listArray.get(position).getRoomAmount());//advance amount
                mIntent.putExtra("tvehicalid", _listArray.get(position).getVehicle_registration_no());
                mIntent.putExtra("tvoterid", _listArray.get(position).getVoter_id_card_no());
                mIntent.putExtra("tdrivingid", _listArray.get(position).getDriving_license_no());
                mIntent.putExtra("tadharid", _listArray.get(position).getAadhar_card_no());
                mIntent.putExtra("tarmid", _listArray.get(position).getArm_licence_no());
                mIntent.putExtra("tdriving_issue_ofc", _listArray.get(position).getDriving_licence_issu_ofc_name());
                mIntent.putExtra("tarm_issue_ofc", _listArray.get(position).getArm_licence_issu_ofc_name());
                mIntent.putExtra("trashanid", _listArray.get(position).getRashan_card_no());
                mIntent.putExtra("tpassportid", _listArray.get(position).getPassport_no());
                mIntent.putExtra("totherid", _listArray.get(position).getOtherid_no());
                mIntent.putExtra("tdetailverif", _listArray.get(position).getDetail_verification());
                mIntent.putExtra("tpstatus", _listArray.get(position).getPaymentStatus());
                mIntent.putExtra("tvehicalphoto", _listArray.get(position).getVehicle_registration_photo());
                mIntent.putExtra("tvoteridphoto", _listArray.get(position).getVoter_id_card_photo());
                mIntent.putExtra("tdrivinglicphoto", _listArray.get(position).getDriving_license_photo());
                mIntent.putExtra("taadharphoto", _listArray.get(position).getAadhar_card_photo());
                mIntent.putExtra("tarmphoto", _listArray.get(position).getArm_licence_photo());
                mIntent.putExtra("tpassportphoto", _listArray.get(position).getPassport_photo());
                mIntent.putExtra("trashanphoto", _listArray.get(position).getRashan_card_photo());
                mIntent.putExtra("totherphoto", _listArray.get(position).getOtherid_photo());
                mIntent.putExtra("tenantphoto", _listArray.get(position).getTenant_photo());
                // mIntent.putExtra("tflatno",_listArray.get(position).getFlat_number());
                mIntent.putExtra("tpincode", _listArray.get(position).getPincode());
                mIntent.putExtra("tstate", _listArray.get(position).getState());
                mIntent.putExtra("tcity", _listArray.get(position).getCity());
                mIntent.putExtra("tbookedroomno", _listArray.get(position).getBookedRoom());
                mIntent.putExtra("tflatno", _listArray.get(position).getFlat_number());
                mIntent.putExtra("tflatno", _listArray.get(position).getFlat_number());
                mIntent.putExtra("tarea", _listArray.get(position).getLocation());
                mIntent.putExtra("tRcu_id", _listArray.get(position).getTenant_id());
                mIntent.putExtra("tTransaction_id", _listArray.get(position).getTransaction_id());
                mIntent.putExtra("tParent_id", _listArray.get(position).getParent_id());
                String parentidvaliue = _listArray.get(position).getParent_id();
                mIntent.putExtra("tOwner_id", _listArray.get(position).getOwnerId());
                String tOwner_idvalue = _listArray.get(position).getOwnerId();
                mIntent.putExtra("ownerid", tOwner_idvalue);
                mIntent.putExtra("tPhoto", _listArray.get(position).getTenant_photo());
                mIntent.putExtra("tofficeAddress", _listArray.get(position).getTenant_office_institute());
                mIntent.putExtra("totherVerifaction", _listArray.get(position).getDetail_verification());
                mIntent.putExtra("tDrivinglicenseNo", _listArray.get(position).getDriving_license_no());
                mIntent.putExtra("tDrivinglicensePhoto", _listArray.get(position).getDriving_license_photo());
                mIntent.putExtra("tDrivinglicenseIssuename", _listArray.get(position).getDriving_licence_issu_ofc_name());
                mIntent.putExtra("tAadharcardNo", _listArray.get(position).getAadhar_card_no());
                mIntent.putExtra("tAadharcardPhoto", _listArray.get(position).getAadhar_card_photo());
                mIntent.putExtra("tArmlicenceNo", _listArray.get(position).getArm_licence_no());
                mIntent.putExtra("tArmlicencePhoto", _listArray.get(position).getArm_licence_photo());
                mIntent.putExtra("tArmlicenceIssuename", _listArray.get(position).getArm_licence_issu_ofc_name());
                mIntent.putExtra("tOtherIdno", _listArray.get(position).getOtherid_no());
                mIntent.putExtra("tOtherIdPhoto", _listArray.get(position).getOtherid_photo());
                mIntent.putExtra("tVoteridcardno", _listArray.get(position).getVoter_id_card_no());
                mIntent.putExtra("tVoteridcardPhoto", _listArray.get(position).getVoter_id_card_photo());
                mIntent.putExtra("tVehicleRegistrationNo", _listArray.get(position).getVehicle_registration_no());
                mIntent.putExtra("tVehicleRegistrationPhoto", _listArray.get(position).getVehicle_registration_photo());
                mIntent.putExtra("tPassportNo", _listArray.get(position).getPassport_no());
                mIntent.putExtra("tPassportPhoto", _listArray.get(position).getPassport_photo());
                mIntent.putExtra("tRashanCardNo", _listArray.get(position).getRashan_card_no());
                mIntent.putExtra("tRashanCardPhoto", _listArray.get(position).getRashan_card_photo());
                mIntent.putExtra("tsformTelephoneNo", _listArray.get(position).getTelephone_number());
                mIntent.putExtra("tsformMobileNo", _listArray.get(position).getMobile_number());
                mIntent.putExtra("tmonthlyRoomRant", _listArray.get(position).getMainroomamount());
                Log.d("un 18", "in adapter" + _listArray.get(position).getBookedRoomId());
                mIntent.putExtra("holduserstatus", holduserstatus);
                activity.startActivity(mIntent);
            }
        });

        isLoading=false;
        return v;

    }

    private void Sendtoserver(String tenantid, String propertyidforseparatevalue, String value) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "updateAttendancesms");
            params.put("property_id", propertyidforseparatevalue);
            params.put("tenant_id", tenantid);
            params.put("value",value);

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

    private void getAttendancestatus(String tenantid, String propertyidforseparatevalue, final int position, final CheckBox smscheckBox) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            JSONObject params = new JSONObject();
            params.put("action", "getAttendancestatus");
            params.put("property_id", propertyidforseparatevalue);
            params.put("tenant_id", tenantid);
            String url = WebUrls.MAIN_URL;
            JsonObjectRequest request_json = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialog.dismiss();
                    String result = response.toString();
                    getResponseDatauncheck(result,position,smscheckBox);
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


    public void getResponseDatauncheck(String result, int position, CheckBox smscheckBox) {
        try {
            String status, message,statussms;
            JSONObject jsmain = new JSONObject(result);
            status = jsmain.getString("flag");
            message = jsmain.getString("message");
            if (status.equalsIgnoreCase("Y")) {
                statussms = jsmain.getString("attendance_sms");

            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}