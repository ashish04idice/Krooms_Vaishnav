package com.krooms.hostel.rental.property.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.modal.TenantModal;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anuradhaidice on 22/4/17.
 */

public class Tenant_Details_Adapter extends BaseAdapter {

    Context context;
    String[] roomno;
    int[] imageId;
    String[] tenantname;
    int[] imagestatus;
    int attendencecount;

    ArrayList<TenantModal> userlist;
    ArrayList<TenantModal> tenantarray;

    public Tenant_Details_Adapter(Context context, ArrayList<TenantModal> userlist, ArrayList<TenantModal> tenantarray) {
        this.context=context;
        this.userlist=userlist;
        this.tenantarray=tenantarray;
    }

    @Override
    public int getCount() {
        return userlist.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi=inflater.inflate(R.layout.grid_itemview_layout,null,true);
        TextView txtroomno= (TextView) vi.findViewById(R.id.txtroomno);
        TextView txtname = (TextView) vi.findViewById(R.id.txtname);
        CircleImageView profile=(CircleImageView)vi.findViewById(R.id.tenat_pic);
        ImageView profile1= (ImageView)vi.findViewById(R.id.img_active);
        txtroomno.setText(userlist.get(position).getTenant_roomno());
        txtname.setText(userlist.get(position).getTenant_name());
      //  UniversalImageLoaderNew.initUniversalImageLoaderOptions();
     if(!userlist.get(position).getTenant_img().equals(""))
     {
         String valueimg=userlist.get(position).getTenant_img();
         String fullpathimg= WebUrls.IMG_URL+valueimg;
         Picasso.with(context)
                 .load(fullpathimg)
                 .placeholder(R.drawable.user_xl)
                 .error(R.drawable.user_xl)
                 .into(profile);
     }
            attendencecount=0;
            String useridvlue=userlist.get(position).getTenant_id();

            if(tenantarray.size() == 0 )
            {
                profile.setBorderColor(context.getResources().getColor(R.color.red_color));
            }else
            {
                for(int j=0;j<tenantarray.size();j++)
                {
                    String tenantidvalue = tenantarray.get(j).getTenant_id();
                    if (useridvlue.equals(tenantidvalue)) {

                        attendencecount=1;
                        String attendcestatusvalue=tenantarray.get(j).getAtt_status();
                        if(attendcestatusvalue.equalsIgnoreCase("sign in") || attendcestatusvalue.equalsIgnoreCase("Night in"))
                        {
                            profile.setBorderColor(context.getResources().getColor(R.color.green_color));

                        }else  if(attendcestatusvalue.equalsIgnoreCase("sign out"))
                        {
                            profile.setBorderColor(context.getResources().getColor(R.color.red_color));

                        }else if(attendcestatusvalue.equalsIgnoreCase("market"))
                        {
                            profile.setBorderColor(context.getResources().getColor(R.color.blue_color));

                        }else if(attendcestatusvalue.equalsIgnoreCase("class"))
                        {
                            profile.setBorderColor(context.getResources().getColor(R.color.class_orage));

                        }else if(attendcestatusvalue.equalsIgnoreCase("vacation"))
                        {
                            profile.setBorderColor(context.getResources().getColor(R.color.black));
                        }
                    }

                }

                if(attendencecount ==0)
                {
                    profile.setBorderColor(context.getResources().getColor(R.color.red_color));
                }
            }


        return vi;
    }
}
