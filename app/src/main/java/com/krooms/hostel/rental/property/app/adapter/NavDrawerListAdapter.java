package com.krooms.hostel.rental.property.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.Utility.Utility;
import com.krooms.hostel.rental.property.app.activity.HostelListActivity;
import com.krooms.hostel.rental.property.app.activity.Home_Accountantactivity;
import com.krooms.hostel.rental.property.app.common.SharedStorage;
import com.krooms.hostel.rental.property.app.modal.NavDrawerItem;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private SharedStorage mSharedStorage;
    TextView txtCount;
    ArrayList<String> datapaidlist;


    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //change

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    //

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        mSharedStorage = SharedStorage.getInstance(context);

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        RelativeLayout main_layout = (RelativeLayout) convertView.findViewById(R.id.main_layout);
        txtCount = (TextView) convertView.findViewById(R.id.counter);
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        if (mSharedStorage.getUserType().equals("3")) {

            if (navDrawerItems.get(position).getTitle().equals("Payment")) {
                String size = String.valueOf(HostelListActivity.paidarray.length);
                for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                    String paidvalue = HostelListActivity.paidarray[j];
                    if (paidvalue.equalsIgnoreCase("Payment")) {
                        HostelListActivity.statusvaluepaidvideo = "true";
                        Utility.setSharedPreference(context, "Payment", "1");

                        break;
                    } else {
                        Utility.setSharedPreference(context, "Payment", "0");
                        HostelListActivity.statusvaluepaidvideo = "false";
                    }
                }

                String sts = Utility.getSharedPreferences(context, "Payment");

                if (sts.equalsIgnoreCase("1")) {

                    txtTitle.setText("Payment");
                    txtTitle.setTextColor(Color.BLACK);
                    imgIcon.setVisibility(View.VISIBLE);

                } else {

                    txtTitle.setText("Paid Service");
                    txtTitle.setTextColor(Color.GRAY);
                    imgIcon.setVisibility(View.INVISIBLE);
                }

            }


            if (navDrawerItems.get(position).getTitle().equals("Add Employee")) {

                String size = String.valueOf(HostelListActivity.paidarray.length);
                for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                    String paidvalue = HostelListActivity.paidarray[j];
                    if (paidvalue.equalsIgnoreCase("Add Employee")) {
                        HostelListActivity.statusvaluepaidvideo = "true";
                        Utility.setSharedPreference(context, "Employee", "1");

                        break;
                    } else {
                        HostelListActivity.statusvaluepaidvideo = "false";
                        Utility.setSharedPreference(context, "Employee", "0");
                    }

                }

                String sts = Utility.getSharedPreferences(context, "Employee");

                if (sts.equalsIgnoreCase("1")) {

                    txtTitle.setText("Add Employee");
                    txtTitle.setTextColor(Color.BLACK);
                    imgIcon.setVisibility(View.VISIBLE);

                } else {
                    txtTitle.setText("Paid Service");
                    txtTitle.setTextColor(Color.GRAY);
                    imgIcon.setVisibility(View.INVISIBLE);
                }
            }


            if (navDrawerItems.get(position).getTitle().equals("Other Charges")) {

                String size = String.valueOf(HostelListActivity.paidarray.length);
                for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                    String paidvalue = HostelListActivity.paidarray[j];
                    if (paidvalue.equalsIgnoreCase("Other Charges")) {
                        HostelListActivity.statusvaluepaidvideo = "true";
                        Utility.setSharedPreference(context, "Other", "1");

                        break;
                    } else {
                        HostelListActivity.statusvaluepaidvideo = "false";
                        Utility.setSharedPreference(context, "Other", "0");
                    }
                }

                String sts = Utility.getSharedPreferences(context, "Other");


                if (sts.equalsIgnoreCase("1")) {
                    txtTitle.setText("Other Charges");
                    txtTitle.setTextColor(Color.BLACK);
                    imgIcon.setVisibility(View.VISIBLE);
                } else {
                    txtTitle.setText("Paid Service");
                    txtTitle.setTextColor(Color.GRAY);
                    imgIcon.setVisibility(View.INVISIBLE);
                }

            }


            if (navDrawerItems.get(position).getTitle().equals("View Employee")) {
                String size = String.valueOf(HostelListActivity.paidarray.length);
                for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                    String paidvalue = HostelListActivity.paidarray[j];
                    if (paidvalue.equalsIgnoreCase("Add Employee")) {
                        HostelListActivity.statusvaluepaidvideo = "true";

                        Utility.setSharedPreference(context, "Employee", "1");
                        break;
                    } else {
                        HostelListActivity.statusvaluepaidvideo = "false";
                        Utility.setSharedPreference(context, "Employee", "0");
                    }

                }

                String sts = Utility.getSharedPreferences(context, "Employee");

                if (sts.equalsIgnoreCase("1")) {
                    txtTitle.setText("View Employee");
                    txtTitle.setTextColor(Color.BLACK);
                    imgIcon.setVisibility(View.VISIBLE);
                } else {
                    txtTitle.setText("Paid Service");
                    txtTitle.setTextColor(Color.GRAY);
                    imgIcon.setVisibility(View.INVISIBLE);
                }
            }

            if (navDrawerItems.get(position).getTitle().equals("Attendance")) {
                String size = String.valueOf(HostelListActivity.paidarray.length);
                for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                    String paidvalue = HostelListActivity.paidarray[j];
                    if (paidvalue.equalsIgnoreCase("Attendance")) {
                        HostelListActivity.statusvaluepaidvideo = "true";
                        Utility.setSharedPreference(context, "Attendance", "1");

                        break;
                    } else {
                        HostelListActivity.statusvaluepaidvideo = "false";
                        Utility.setSharedPreference(context, "Attendance", "0");

                    }

                }

                String sts = Utility.getSharedPreferences(context, "Attendance");
                if (sts.equalsIgnoreCase("1")) {
                    txtTitle.setText("Attendance");
                    txtTitle.setTextColor(Color.BLACK);
                    imgIcon.setVisibility(View.VISIBLE);
                } else {
                    txtTitle.setText("Paid Service");
                    txtTitle.setTextColor(Color.GRAY);
                    imgIcon.setVisibility(View.INVISIBLE);
                }


            }


            if (navDrawerItems.get(position).getTitle().equals("Inventory")) {
                String size = String.valueOf(HostelListActivity.paidarray.length);
                for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                    String paidvalue = HostelListActivity.paidarray[j];
                    if (paidvalue.equalsIgnoreCase("Inventory")) {
                        HostelListActivity.statusvaluepaidvideo = "true";
                        Utility.setSharedPreference(context, "Inventory", "1");

                        break;
                    } else {
                        HostelListActivity.statusvaluepaidvideo = "false";
                        Utility.setSharedPreference(context, "Inventory", "0");
                    }

                }

                String sts = Utility.getSharedPreferences(context, "Inventory");

                if (sts.equalsIgnoreCase("1")) {
                    txtTitle.setText("Inventory");
                    txtTitle.setTextColor(Color.BLACK);
                    imgIcon.setVisibility(View.VISIBLE);
                } else {
                    txtTitle.setText("Paid Service");
                    txtTitle.setTextColor(Color.GRAY);
                    imgIcon.setVisibility(View.INVISIBLE);
                }

            }


            if (navDrawerItems.get(position).getTitle().equals("Attendance Report")) {

                String size = String.valueOf(HostelListActivity.paidarray.length);
                for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                    String paidvalue = HostelListActivity.paidarray[j];
                    if (paidvalue.equalsIgnoreCase("Attendance")) {
                        HostelListActivity.statusvaluepaidvideo = "true";
                        Utility.setSharedPreference(context, "Attendance", "1");
                        break;
                    } else {
                        HostelListActivity.statusvaluepaidvideo = "false";
                        Utility.setSharedPreference(context, "Attendance", "0");
                    }

                }

                String sts = Utility.getSharedPreferences(context, "Attendance");
                if (sts.equalsIgnoreCase("1")) {
                    txtTitle.setText("Attendance Report");
                    txtTitle.setTextColor(Color.BLACK);
                    imgIcon.setVisibility(View.VISIBLE);

                } else {
                    txtTitle.setText("Paid Service");
                    txtTitle.setTextColor(Color.GRAY);
                    imgIcon.setVisibility(View.INVISIBLE);
                }

            }

            if (navDrawerItems.get(position).getTitle().equals("Complaint")) {
                txtCount.setVisibility(View.VISIBLE);
                String countnew = HostelListActivity.Countss;
                String maincountvaluecomp = countnew;
                if (maincountvaluecomp != null && !maincountvaluecomp.isEmpty() && !maincountvaluecomp.equals("null") && !maincountvaluecomp.equals("0")) {
                    txtCount.setVisibility(View.VISIBLE);
                    txtCount.setTextColor(Color.WHITE);
                    txtCount.setText("" + maincountvaluecomp);
                    txtCount.setBackgroundResource(R.drawable.circlechat);
                } else {
                    txtCount.setText(" ");
                    txtCount.setBackgroundResource(R.drawable.whitecircle);
                }
            }

            if (navDrawerItems.get(position).getTitle().equals("Feedback")) {
                txtCount.setVisibility(View.VISIBLE);
                String fcountnew = HostelListActivity.feedbackownercountss;
                String maincountvalue = fcountnew;
                if (maincountvalue != null && !maincountvalue.isEmpty() && !maincountvalue.equals("null") && !maincountvalue.equals("0")) {
                    txtCount.setVisibility(View.VISIBLE);
                    txtCount.setTextColor(Color.WHITE);
                    txtCount.setText("" + maincountvalue);
                    txtCount.setBackgroundResource(R.drawable.circlechat);
                } else {
                    txtCount.setText(" ");
                    txtCount.setBackgroundResource(R.drawable.whitecircle);
                }
            }
            if (navDrawerItems.get(position).getTitle().equals("Night Attendance Status")) {
                txtCount.setVisibility(View.VISIBLE);
                String fcountnew = HostelListActivity.nightattendancecount;
                String maincountvalue = fcountnew;
                if (maincountvalue != null && !maincountvalue.isEmpty() && !maincountvalue.equals("null") && !maincountvalue.equals("0")) {
                    txtCount.setVisibility(View.VISIBLE);
                    txtCount.setTextColor(Color.WHITE);
                    txtCount.setText("" + maincountvalue);
                    txtCount.setBackgroundResource(R.drawable.circlechat);
                } else {
                    txtCount.setText(" ");
                    txtCount.setBackgroundResource(R.drawable.whitecircle);
                }
            }
            //

        } else

            //for warden
            if (mSharedStorage.getUserType().equals("6")) {


                if (navDrawerItems.get(position).getTitle().equals("Payment")) {
                    String size = String.valueOf(HostelListActivity.paidarray.length);
                    for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                        String paidvalue = HostelListActivity.paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Payment")) {
                            HostelListActivity.statusvaluepaidvideo = "true";
                            break;
                        } else {
                            HostelListActivity.statusvaluepaidvideo = "false";
                        }

                    }

                    if (HostelListActivity.statusvaluepaidvideo.equals("true")) {
                        txtTitle.setText("Payment");
                        txtTitle.setTextColor(Color.BLACK);
                        imgIcon.setVisibility(View.VISIBLE);

                    } else {
                        txtTitle.setText("Paid Service");
                        txtTitle.setTextColor(Color.GRAY);
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }


                if (navDrawerItems.get(position).getTitle().equals("Other Charges")) {
                    String size = String.valueOf(HostelListActivity.paidarray.length);
                    for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                        String paidvalue = HostelListActivity.paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Other Charges")) {
                            HostelListActivity.statusvaluepaidvideo = "true";
                            break;
                        } else {
                            HostelListActivity.statusvaluepaidvideo = "false";
                        }

                    }

                    if (HostelListActivity.statusvaluepaidvideo.equals("true")) {
                        txtTitle.setText("Other Charges");
                        txtTitle.setTextColor(Color.BLACK);
                        imgIcon.setVisibility(View.VISIBLE);
                    } else {
                        txtTitle.setText("Paid Service");
                        txtTitle.setTextColor(Color.GRAY);
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }


                if (navDrawerItems.get(position).getTitle().equals("Attendance")) {
                    String size = String.valueOf(HostelListActivity.paidarray.length);
                    for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                        String paidvalue = HostelListActivity.paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Attendance")) {
                            HostelListActivity.statusvaluepaidvideo = "true";
                            break;
                        } else {
                            HostelListActivity.statusvaluepaidvideo = "false";
                        }

                    }

                    if (HostelListActivity.statusvaluepaidvideo.equals("true")) {
                        txtTitle.setText("Attendance");
                        txtTitle.setTextColor(Color.BLACK);
                        imgIcon.setVisibility(View.VISIBLE);
                    } else {
                        txtTitle.setText("Paid Service");
                        txtTitle.setTextColor(Color.GRAY);
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }


                if (navDrawerItems.get(position).getTitle().equals("Attendance Report")) {
                    String size = String.valueOf(HostelListActivity.paidarray.length);
                    for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                        String paidvalue = HostelListActivity.paidarray[j];
                        if (paidvalue.equalsIgnoreCase("Attendance")) {
                            HostelListActivity.statusvaluepaidvideo = "true";
                            break;
                        } else {
                            HostelListActivity.statusvaluepaidvideo = "false";
                        }

                    }

                    if (HostelListActivity.statusvaluepaidvideo.equals("true")) {
                        txtTitle.setText("Attendance Report");
                        txtTitle.setTextColor(Color.BLACK);
                        imgIcon.setVisibility(View.VISIBLE);

                    } else {
                        txtTitle.setText("Paid Service");
                        txtTitle.setTextColor(Color.GRAY);
                        imgIcon.setVisibility(View.INVISIBLE);
                    }
                }

                if (navDrawerItems.get(position).getTitle().equals("Complaint")) {
                    String count1new = HostelListActivity.complaintwardencountss;
                    String maincountvaluecomp = count1new;
                    if (maincountvaluecomp != null && !maincountvaluecomp.isEmpty() && !maincountvaluecomp.equals("null") && !maincountvaluecomp.equals("0")) {
                        txtCount.setVisibility(View.VISIBLE);
                        txtCount.setTextColor(Color.WHITE);
                        txtCount.setText("" + maincountvaluecomp);
                        txtCount.setBackgroundResource(R.drawable.circlechat);
                    } else {
                        txtCount.setText(" ");
                        txtCount.setBackgroundResource(R.drawable.whitecircle);
                    }
                }

                if (navDrawerItems.get(position).getTitle().equals("Feedback")) {
                    String fcountnew = HostelListActivity.feedbackwardencountss;
                    String maincountvalue = fcountnew;

                    if (maincountvalue != null && !maincountvalue.isEmpty() && !maincountvalue.equals("null") && !maincountvalue.equals("0")) {
                        txtCount.setVisibility(View.VISIBLE);
                        txtCount.setTextColor(Color.WHITE);
                        txtCount.setText("" + maincountvalue);
                        txtCount.setBackgroundResource(R.drawable.circlechat);
                    } else {
                        txtCount.setText(" ");
                        txtCount.setBackgroundResource(R.drawable.whitecircle);
                    }
                }

                if (navDrawerItems.get(position).getTitle().equals("Night Attendance Status")) {
                    txtCount.setVisibility(View.VISIBLE);
                    String fcountnew = HostelListActivity.nightattendancecount;
                    String maincountvalue = fcountnew;
                    if (maincountvalue != null && !maincountvalue.isEmpty() && !maincountvalue.equals("null") && !maincountvalue.equals("0")) {
                        txtCount.setVisibility(View.VISIBLE);
                        txtCount.setTextColor(Color.WHITE);
                        txtCount.setText("" + maincountvalue);
                        txtCount.setBackgroundResource(R.drawable.circlechat);
                    } else {
                        txtCount.setText(" ");
                        txtCount.setBackgroundResource(R.drawable.whitecircle);
                    }
                }

            } else
                //

                if (mSharedStorage.getUserType().equals("4")) {

                    if (navDrawerItems.get(position).getTitle().equals("Make Payment")) {
                        String size = String.valueOf(HostelListActivity.paidarray.length);
                        for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                            String paidvalue = HostelListActivity.paidarray[j];
                            if (paidvalue.equalsIgnoreCase("Monthly Payment")) {
                                HostelListActivity.statusvaluepaidvideo = "true";
                                break;
                            } else {
                                HostelListActivity.statusvaluepaidvideo = "false";
                            }

                        }

                        if (HostelListActivity.statusvaluepaidvideo.equals("true")) {
                            txtTitle.setText("Make Payment");
                            txtTitle.setTextColor(Color.BLACK);
                            imgIcon.setVisibility(View.VISIBLE);

                        } else {
                            txtTitle.setText("Paid Service");
                            txtTitle.setTextColor(Color.GRAY);
                            imgIcon.setVisibility(View.INVISIBLE);
                        }
                    }


                    if (navDrawerItems.get(position).getTitle().equals("Complaint")) {
                        int count1 = HostelListActivity.tenantcount;
                        String count1new = HostelListActivity.Parentcomplaintcount;
                        String maincountvaluecomp = count1new;

                        if (maincountvaluecomp != null && !maincountvaluecomp.isEmpty() && !maincountvaluecomp.equals("null") && !maincountvaluecomp.equals("0")) {
                            txtCount.setVisibility(View.VISIBLE);
                            txtCount.setTextColor(Color.WHITE);
                            txtCount.setText("" + maincountvaluecomp);
                            txtCount.setBackgroundResource(R.drawable.circlechat);
                        } else {
                            txtCount.setText(" ");
                            txtCount.setBackgroundResource(R.drawable.whitecircle);
                        }
                    }

                    if (navDrawerItems.get(position).getTitle().equals("Feedback")) {
                        int fcount1 = HostelListActivity.feedbacktenantcount;
                        String fcountnew = HostelListActivity.Parentfeedbackcount;
                        String maincountvalue = fcountnew;
                        if (maincountvalue != null && !maincountvalue.isEmpty() && !maincountvalue.equals("null") && !maincountvalue.equals("0")) {
                            txtCount.setVisibility(View.VISIBLE);
                            txtCount.setTextColor(Color.WHITE);
                            txtCount.setText("" + maincountvalue);
                            txtCount.setBackgroundResource(R.drawable.circlechat);
                        } else {
                            //Log.e("Count44", "" + fcountnew);
                            txtCount.setText(" ");
                            txtCount.setBackgroundResource(R.drawable.whitecircle);

                        }
                    }

                } else if (mSharedStorage.getUserType().equals("2")) {

                    if (navDrawerItems.get(position).getTitle().equals("Make Payment")) {
                        String size = String.valueOf(HostelListActivity.paidarray.length);
                        for (int j = 0; j < HostelListActivity.paidarray.length; j++) {
                            String paidvalue = HostelListActivity.paidarray[j];
                            if (paidvalue.equalsIgnoreCase("Monthly Payment")) {
                                HostelListActivity.statusvaluepaidvideo = "true";
                                break;
                            } else {
                                HostelListActivity.statusvaluepaidvideo = "false";
                            }

                        }

                        if (HostelListActivity.statusvaluepaidvideo.equals("true")) {
                            txtTitle.setText("Make Payment");
                            txtTitle.setTextColor(Color.BLACK);
                            imgIcon.setVisibility(View.VISIBLE);

                        } else {
                            txtTitle.setText("Paid Service");
                            txtTitle.setTextColor(Color.GRAY);
                            imgIcon.setVisibility(View.INVISIBLE);
                        }
                    }


                    if (navDrawerItems.get(position).getTitle().equals("Complaint")) {
                        //txtCount.setVisibility(View.VISIBLE);
                        int count1 = HostelListActivity.tenantcount;
                        String count1new = HostelListActivity.tenantcountss;
                        String maincountvaluecomp = count1new;

                        if (maincountvaluecomp != null && !maincountvaluecomp.isEmpty() && !maincountvaluecomp.equals("null") && !maincountvaluecomp.equals("0")) {
                            txtCount.setVisibility(View.VISIBLE);
                            txtCount.setTextColor(Color.WHITE);
                            txtCount.setText("" + maincountvaluecomp);
                            txtCount.setBackgroundResource(R.drawable.circlechat);
                        } else {
                            txtCount.setText(" ");
                            txtCount.setBackgroundResource(R.drawable.whitecircle);
                        }
                    }

                    if (navDrawerItems.get(position).getTitle().equals("Feedback")) {
                        //   txtCount.setVisibility(View.VISIBLE);
                        int fcount1 = HostelListActivity.feedbacktenantcount;
                        String fcountnew = HostelListActivity.feedbacktenantcountss;
                        String maincountvalue = fcountnew;

                        if (maincountvalue != null && !maincountvalue.isEmpty() && !maincountvalue.equals("null") && !maincountvalue.equals("0")) {
                            txtCount.setVisibility(View.VISIBLE);
                            txtCount.setTextColor(Color.WHITE);
                            txtCount.setText("" + maincountvalue);
                            txtCount.setBackgroundResource(R.drawable.circlechat);
                        } else {
                            //Log.e("Count44", "" + fcountnew);
                            txtCount.setText(" ");
                            txtCount.setBackgroundResource(R.drawable.whitecircle);
                        }
                    }
                } else {

                    if (navDrawerItems.get(position).getTitle().equals("Payment")) {
                        for (int j = 0; j < Home_Accountantactivity.paidarray.length; j++) {
                            String paidvalue = Home_Accountantactivity.paidarray[j];
                            if (paidvalue.equalsIgnoreCase("Payment")) {
                                Home_Accountantactivity.statusvaluepaidlogic = "true";
                                break;
                            } else {
                                Home_Accountantactivity.statusvaluepaidlogic = "false";
                            }
                        }

                        if (Home_Accountantactivity.statusvaluepaidlogic.equals("true")) {
                            txtTitle.setText("Payment");
                            txtTitle.setTextColor(Color.BLACK);
                            imgIcon.setVisibility(View.VISIBLE);

                        } else {
                            txtTitle.setText("Paid Service");
                            txtTitle.setTextColor(Color.GRAY);
                            imgIcon.setVisibility(View.INVISIBLE);
                        }
                    }

                    if (navDrawerItems.get(position).getTitle().equals("Other Charges")) {
                        for (int j = 0; j < Home_Accountantactivity.paidarray.length; j++) {
                            String paidvalue = Home_Accountantactivity.paidarray[j];
                            if (paidvalue.equalsIgnoreCase("Other Charges")) {
                                Home_Accountantactivity.statusvaluepaidlogic = "true";
                                break;
                            } else {
                                Home_Accountantactivity.statusvaluepaidlogic = "false";
                            }
                        }

                        if (Home_Accountantactivity.statusvaluepaidlogic.equals("true")) {
                            txtTitle.setText("Other Charges");
                            txtTitle.setTextColor(Color.BLACK);
                            imgIcon.setVisibility(View.VISIBLE);

                        } else {
                            txtTitle.setText("Paid Service");
                            txtTitle.setTextColor(Color.GRAY);
                            imgIcon.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        return convertView;
    }
}


