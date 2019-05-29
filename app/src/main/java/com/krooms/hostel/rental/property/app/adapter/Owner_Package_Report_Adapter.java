package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.OwnerPackageReportModel;
import com.krooms.hostel.rental.property.app.modal.PaymentPaidUnpaidModel;

import java.util.ArrayList;


/**
 * Created by ashishidice on 22/4/17.
 */

public class Owner_Package_Report_Adapter extends BaseAdapter {

    Context context;
    ArrayList<OwnerPackageReportModel> list;

    public Owner_Package_Report_Adapter(Context context, ArrayList<OwnerPackageReportModel> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflater.inflate(R.layout.owner_package_report_adapter_item, null, true);

        int srno=1;


        TextView txtsrno = (TextView) vi.findViewById(R.id.txtsrno);
        TextView txtroomno = (TextView) vi.findViewById(R.id.txtroomno);
        TextView txtname = (TextView) vi.findViewById(R.id.txtname);
        TextView txtadv_a_dept_amount = (TextView) vi.findViewById(R.id.txtadv_a_dept_amount);
        TextView txtadv_a_dept_date = (TextView) vi.findViewById(R.id.txtadv_a_dept_date);
        TextView txtreturn_amount_date = (TextView) vi.findViewById(R.id.txtreturn_amount_date);
        TextView txtreturn_amount = (TextView) vi.findViewById(R.id.txtreturn_amount);
        TextView txtpackage1 = (TextView) vi.findViewById(R.id.txtpackage1);
        TextView txtpackage2 = (TextView) vi.findViewById(R.id.txtpackage2);
        TextView txt_room_amount_revied_date = (TextView) vi.findViewById(R.id.txt_room_amount_revied_date);
        TextView txt_room_revied_amount = (TextView) vi.findViewById(R.id.txt_room_revied_amount);
        TextView txtConsuption_dept_amount = (TextView) vi.findViewById(R.id.txtConsuption_dept_amount);
        TextView txtConsuption_dept_amount_date = (TextView) vi.findViewById(R.id.txtConsuption_dept_amount_date);


        try {

            if(list.get(position).getSrno().equals("null")){
                txtsrno.setText("");
            }
            else{

                txtsrno.setText(list.get(position).getSrno());
            }

            if(list.get(position).getRoom_no().equals("null")){
                txtroomno.setText("");
            }
            else{

                txtroomno.setText(list.get(position).getRoom_no());
            }


            if(list.get(position).getTenant_name().equals("null")){
                txtname.setText("");
            }
            else{

                txtname.setText(list.get(position).getTenant_name());
            }
            if(list.get(position).getAdvance_Amount().equals("null")){
                txtadv_a_dept_amount.setText("");
            }
            else{

                txtadv_a_dept_amount.setText(list.get(position).getAdvance_Amount());
            }
            if(list.get(position).getAdvance_date().equals("null")){
                txtadv_a_dept_date.setText("");
            }
            else{

                txtadv_a_dept_date.setText(list.get(position).getAdvance_date());
            }

            if(list.get(position).getRoom_amout_revied().equals("null")){
                txt_room_revied_amount.setText("");
            }
            else{

                txt_room_revied_amount.setText(list.get(position).getRoom_amout_revied());
            }

            if(list.get(position).getRoom_amout_revied_date().equals("null")){
                txt_room_amount_revied_date.setText("");
            }
            else{

                txt_room_amount_revied_date.setText(list.get(position).getRoom_amout_revied_date());
            }

            if(list.get(position).getPackage1().equals("null")){
                txtpackage1.setText("");
            }
            else{

                txtpackage1.setText(list.get(position).getPackage1());
            }

            if(list.get(position).getPackage2().equals("null")){
                txtpackage2.setText("");
            }
            else{

                txtpackage2.setText(list.get(position).getPackage2());
            }

            if(list.get(position).getTenant_return_amount().equals("null")){
                txtreturn_amount.setText("");
            }
            else{

                txtreturn_amount.setText(list.get(position).getTenant_return_amount());
            }
            if(list.get(position).getTenant_return_amount_Date().equals("null")){
                txtreturn_amount_date.setText("");
            }
            else{

                txtreturn_amount_date.setText(list.get(position).getTenant_return_amount_Date());
            }

            if(list.get(position).getConsuption_Dept_Amount().equals("null")){
                txtConsuption_dept_amount.setText("");
            }
            else{

                txtConsuption_dept_amount.setText(list.get(position).getConsuption_Dept_Amount());
            }
            if(list.get(position).getConsuption_Dept_Amount_date().equals("null")){
                txtConsuption_dept_amount_date.setText("");
            }
            else{

                txtConsuption_dept_amount_date.setText(list.get(position).getConsuption_Dept_Amount_date());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }
}
