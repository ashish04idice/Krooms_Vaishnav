package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.Complaintdetailtenant;
import com.krooms.hostel.rental.property.app.modal.Complaintdetailtenantnew;
import com.krooms.hostel.rental.property.app.modal.Tenant_Complaintcountmodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by ashish on 19/1/17.
 */

public class listviewshowcomplaintadapter extends BaseAdapter {

    Holder holder;
    Context context;
    ArrayList<String> listmaincomplint = new ArrayList<>();
    ArrayList<Complaintdetailtenant> arraylistdetail;
    ArrayList<Tenant_Complaintcountmodel> countarray;
    ArrayList<Complaintdetailtenantnew> arraylistdetailnew;

    public listviewshowcomplaintadapter(Context lcontext, ArrayList<Complaintdetailtenantnew> arraylistdetailnew, ArrayList<Tenant_Complaintcountmodel> countarray) {

        context = lcontext;
        this.countarray = countarray;
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
            v = layoutInflater.inflate(R.layout.listview_item_viewcomplainttenant, null);
            holder.textownername = (TextView) v.findViewById(R.id.itemownername);
            holder.textComplaint = (TextView) v.findViewById(R.id.itemcomplaintowner);
            holder.textComplainthead = (TextView) v.findViewById(R.id.itemcomplaintheadowner);
            holder.textroomno = (TextView) v.findViewById(R.id.oitemroomno);
            holder.texttime = (TextView) v.findViewById(R.id.itemtimeowner);
            holder.textDate = (TextView) v.findViewById(R.id.itemdateowner);
            holder.textrole = (TextView) v.findViewById(R.id.itemroleowner);
            holder.textnamelatter = (TextView) v.findViewById(R.id.itemtnamefirstletter);
            holder.textComplaint.setText(arraylistdetailnew.get(position).getOwner_complaintdescriptionnew());
            holder.textComplainthead.setText(arraylistdetailnew.get(position).getOwner_complainttypenew());
            String Tenantname = arraylistdetailnew.get(position).getTenant_namenew();
            String Ownername = arraylistdetailnew.get(position).getOwner_namenew();

            if (position % 2 == 0) {

                holder.textnamelatter.setBackgroundResource(R.drawable.circle_nameletter);

            } else {

                holder.textnamelatter.setBackgroundResource(R.drawable.gray_cirlce_large_img);

            }

            String CheckReply = arraylistdetailnew.get(position).getCheck_Replynew();
            String countvalue = arraylistdetailnew.get(position).getOwnercounttotalnew();
            if (countvalue == null) {
                holder.textroomno.setText(arraylistdetailnew.get(position).getOwnercounttotalnew());
            } else {
                holder.textroomno.setBackgroundResource(R.drawable.circlechat);
                holder.textroomno.setText(arraylistdetailnew.get(position).getOwnercounttotalnew());
            }


            if (CheckReply.equals("1")) {
                holder.textrole.setText("Send");
                holder.textownername.setText(arraylistdetailnew.get(position).getTenant_namenew());
                holder.textnamelatter.setText(arraylistdetailnew.get(position).getTenant_roomnonew());
            } else if (CheckReply.equals("2")) {

                holder.textrole.setText("Receive");
                holder.textownername.setText("Parent");
                holder.textnamelatter.setText("P");
            } else if (CheckReply.equals("3")) {
                holder.textrole.setText("Receive");
                holder.textownername.setText("Warden(O-" + arraylistdetailnew.get(position).getOwner_namenew() + ")");
                holder.textnamelatter.setText("W");
            } else {
                holder.textrole.setText("Receive");
                holder.textownername.setText(arraylistdetailnew.get(position).getOwner_namenew());
                holder.textnamelatter.setText("O");
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
        TextView textownername, textComplaint, textComplainthead, textroomno, textDate, texttime, texttenantname, textrole, textnamelatter;
    }
}


