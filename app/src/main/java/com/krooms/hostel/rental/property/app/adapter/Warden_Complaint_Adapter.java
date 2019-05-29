package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Complaindetailowner;
import com.krooms.hostel.rental.property.app.modal.Complaindetailownernew;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by ashish on 19/1/17.
 */

public class Warden_Complaint_Adapter extends BaseAdapter {

    Holder holder;
    Context context;

    ArrayList<Complaindetailowner> arraylistdetail;
    ArrayList<Complaindetailownernew> arraylistdetailnew;

    public Warden_Complaint_Adapter(Context lcontext, ArrayList<Complaindetailowner> arraylistdetail, ArrayList<Complaindetailownernew> arraylistdetailnew) {

        context = lcontext;
        this.arraylistdetail = arraylistdetail;
        this.arraylistdetailnew = arraylistdetailnew;
    }


    @Override
    public int getCount() {
        return arraylistdetailnew.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylistdetailnew.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        holder = new Holder();
        if (v == null) {

            LayoutInflater layoutInflater = LayoutInflater.from(context.getApplicationContext());
            v = layoutInflater.inflate(R.layout.listview_item, null);
            holder.textTenantname = (TextView) v.findViewById(R.id.itemtenantname);
            holder.textComplain = (TextView) v.findViewById(R.id.itemcomplain);
            holder.textDate = (TextView) v.findViewById(R.id.itemdate);
            holder.texttime = (TextView) v.findViewById(R.id.itemtime);
            holder.textrole = (TextView) v.findViewById(R.id.itemroletype);
            holder.textroomno = (TextView) v.findViewById(R.id.itemroomno);
            holder.textnameletter = (TextView) v.findViewById(R.id.itemtnamefirstletterowner);
            holder.textcomaplinthead = (TextView) v.findViewById(R.id.itemcomplainthead);
            holder.textComplain.setText(arraylistdetailnew.get(position).getCompdescriptionnew());
            holder.textcomaplinthead.setText(arraylistdetailnew.get(position).getComptitlenew());
            String Checkreply = arraylistdetailnew.get(position).getCheck_replynew();
            String Tenantname = arraylistdetailnew.get(position).getTenant_namenew();
            String Ownername = arraylistdetailnew.get(position).getOwner_namenew();
            if (position % 2 == 0) {

                holder.textnameletter.setBackgroundResource(R.drawable.circle_nameletter);

            } else {

                holder.textnameletter.setBackgroundResource(R.drawable.gray_cirlce_large_img);

            }

            String countvalue = arraylistdetailnew.get(position).getTotal_countnew();
            if (countvalue == null) {
                holder.textroomno.setText(arraylistdetailnew.get(position).getTotal_countnew());
            } else {
                holder.textroomno.setBackgroundResource(R.drawable.circlechat);
                holder.textroomno.setText(arraylistdetailnew.get(position).getTotal_countnew());
            }

            if (Checkreply.equals("1")) {//1 for tenant

                holder.textrole.setText("Receive");
                holder.textTenantname.setText(arraylistdetail.get(position).getTenant_name());
                holder.textnameletter.setText(arraylistdetailnew.get(position).getTenant_roomnonew());
            } else if (Checkreply.equals("2")) {
                holder.textrole.setText("Receive");
                holder.textTenantname.setText("Parent(" + arraylistdetail.get(position).getTenant_name() + ")");
                holder.textnameletter.setText("P");
            }
            //3 for warden
            else if (Checkreply.equals("3")) {
                holder.textrole.setText("Send");
                holder.textTenantname.setText(arraylistdetail.get(position).getTenant_name());
                holder.textnameletter.setText("W");
            } else {
                holder.textrole.setText("Receive");
                // holder.textTenantname.setText(arraylistdetail.get(position).getOwner_name());
                holder.textTenantname.setText("Owner(" + arraylistdetail.get(position).getTenant_name() + ")");
                holder.textnameletter.setText("O");

            }

            String Convertdate = null, newdate = null, newtime = null, Convettime = null;
            String Datetime = arraylistdetailnew.get(position).getDatenew();

            String[] date = Datetime.split(",");
            for (int j = 0; j < date.length; j++) {

                newdate = date[0].toString();
                newtime = date[1].toString();
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd'T'HH:mm:ss//EEE,dd MMM
                Date data1 = sdf.parse(newdate);
                Convertdate = output.format(data1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String currentdatavalue = df.format(Calendar.getInstance().getTime());

            if (Convertdate.equals(currentdatavalue)) {
                holder.textDate.setVisibility(View.GONE);
                holder.texttime.setVisibility(View.VISIBLE);

            } else {
                holder.textDate.setText(Convertdate);
                holder.texttime.setVisibility(View.GONE);

            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("KK:mm:ss");
                SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");//yyyy-MM-dd'T'HH:mm:ss
                Date data = sdf.parse(newtime);
                Convettime = output.format(data);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.texttime.setText(Convettime);
        }

        return v;

    }

    public class Holder {
        TextView textTenantname, textComplain, textDate, texttime, textcomaplinthead, textroomno, textrole, textnameletter;

    }
}


