package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.activity.GetAttendenceActivity;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Attendence_Modal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by anuradhaidice on 22/12/16.
 */

public class Attendence_ListAdapter extends BaseAdapter {
    String roomnoview_value,tenantnameview_value;
    ArrayList<Attendence_Modal> room_model_arraylist;
    RadioButton radioButton;
    RadioGroup radio_group;
    private Activity context;
    String radiovalue;
    View rowView = null;
    ImageView imageright;
    String date="",time="";
    String status = "false";
    ImageView imageright_green;
    RelativeLayout submit_layout;
    ArrayList<Attendence_Modal> selectedarray;

    public Attendence_ListAdapter(Activity context, ArrayList<Attendence_Modal> room_model_arraylist, ArrayList<Attendence_Modal> selectedarray) {
        this.context = context;
        this.room_model_arraylist=room_model_arraylist;
        this.selectedarray=selectedarray;
    }
    static class ViewHolder {
        private TextView roomnoTextView;
        private TextView tnameTextView;
        private TextView statusTextView;
        private RadioButton presentRb;
        private RadioButton absentRb;
        private ImageView imagerightView;
        private RelativeLayout RelativeLayoutLy;
    }

    @Override
    public int getCount() {
        return room_model_arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public View getView(final int position, View vi, ViewGroup parent) {
        View v=vi;
        final ViewHolder holder;
        if (vi==null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(R.layout.listviewitem_attendencedata, null);
            holder = new ViewHolder();
            holder.roomnoTextView= (TextView)v.findViewById(R.id.roomnoview);
            holder.tnameTextView= (TextView)v.findViewById(R.id.tenantnameview);
            holder.presentRb=(RadioButton)v.findViewById(R.id.radio_present);
            holder.absentRb=(RadioButton)v.findViewById(R.id.radio_absent);
            holder.RelativeLayoutLy=(RelativeLayout)v.findViewById(R.id.submit_layout);
            holder.imagerightView=(ImageView)v.findViewById(R.id.imageright);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder)vi.getTag();
        }
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern, new Locale("da", "DK"));
        date= simpleDateFormat.format(new Date());
        Log.d("1.....current time",""+date);
        String pattern1 = "HH:mm:ss";
        SimpleDateFormat simpleDateFormat1 =new SimpleDateFormat(pattern1, new Locale("da", "DK"));
        time = simpleDateFormat1.format(new Date());
        Log.d("1.....current time",""+time);


        if(Build.VERSION.SDK_INT>=21)
        {
            holder.absentRb.setButtonTintMode(PorterDuff.Mode.DST_IN);
            holder.absentRb.setButtonTintMode(PorterDuff.Mode.DST_IN);
        }
        roomnoview_value =room_model_arraylist.get(position).getAttendenceroom_no();
        tenantnameview_value= room_model_arraylist.get(position).getAttendencetenantname();
        Log.d("tenant name",tenantnameview_value);
        holder.roomnoTextView.setText(room_model_arraylist.get(position).getAttendenceroom_no());
        holder.tnameTextView.setText(tenantnameview_value);
        String tenantidvaalue=room_model_arraylist.get(position).getAttendencetenantid();

        if(holder.absentRb.isChecked())
        {
            holder.presentRb.setChecked(false);
        }

        if(holder.presentRb.isChecked())
        {
            holder.absentRb.setChecked(false);
        }


        for(int i=0;i<selectedarray.size();i++)
        {
            String tenaantidvaluenw=selectedarray.get(i).getAttendencetenantid();
            String tenantPresent_value=selectedarray.get(i).getAbsent_present();
            if(tenaantidvaluenw.equals(tenantidvaalue))
            {
                holder.imagerightView.setBackgroundResource(R.drawable.green_right);
                if(tenantPresent_value.equals("A"))
                {
                    holder.absentRb.setEnabled(true);
                }else
                {
                    holder.presentRb.setChecked(true);
                }
                holder.imagerightView.setEnabled(false);
            }

        }


        holder.imagerightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                holder.imagerightView.setBackgroundResource(R.drawable.green_right);
                holder.imagerightView.setEnabled(false);
                String roomnovalue=holder.roomnoTextView.getText().toString();
                String tenantnameviewvalue=holder.tnameTextView.getText().toString();
                String tenantid=room_model_arraylist.get(position).getAttendencetenantid();
                Calendar calander = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                time = simpleDateFormat.format(calander.getTime());
                String currentdate_value_tenant=time;
                System.out.println(currentdate_value_tenant);

                if(holder.absentRb.isChecked())
                {
                    radiovalue="A";

                }

                if(holder.presentRb.isChecked())
                {
                    radiovalue="P";
                }
                SQLiteDatabase sq=context.openOrCreateDatabase("AttendenceTable.sqlite",MODE_PRIVATE, null);
                sq.execSQL("update Attendence set attendence='"+radiovalue+"',time='"+time+"'where tenant_id='"+tenantid+"'");
            }
        });
        return v;
    }
}
