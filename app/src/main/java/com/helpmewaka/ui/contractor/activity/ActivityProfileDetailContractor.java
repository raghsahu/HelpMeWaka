package com.helpmewaka.ui.contractor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.contractor.fragment.FragmentBilling;
import com.helpmewaka.ui.contractor.fragment.FragmentTaskDashBoard;
import com.helpmewaka.ui.contractor.fragment.FragmentProfileContractor;
import com.helpmewaka.ui.customer.fragment.FragmentJobDashBoard;
import com.helpmewaka.ui.customer.fragment.FragmentPostAnErrand;
import com.helpmewaka.ui.customer.fragment.FragmentProfileCustomer;

public class ActivityProfileDetailContractor extends AppCompatActivity {
    private TabLayout messageTabLayout;
    private ImageView iv_back;
    private String service_id;
    private String splash;
    private String dashboard;
    private TextView tv_title;
    private RelativeLayout rl_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail_contractor);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            service_id = intent.getStringExtra("SERVICE_ID");
            splash = intent.getStringExtra("SPLASH");
            dashboard = intent.getStringExtra("DASHBOARD");

            Log.e("DASHBOARD_nav", ""+dashboard);
        }

        initView();
    }

    private void initView() {
        messageTabLayout = findViewById(R.id.message_tabLayout);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        rl_header = findViewById(R.id.rl_header);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityProfileDetailContractor.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        //add button text
        messageTabLayout.addTab(messageTabLayout.newTab().setText("Profile"));
        messageTabLayout.addTab(messageTabLayout.newTab().setText("Task DashBoard"));
        //messageTabLayout.addTab(messageTabLayout.newTab().setText("Billing"));
        //messageTabLayout.addTab(messageTabLayout.newTab().setText("Post an Errand"));

        messageTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //default selected
        addFragment(new FragmentProfileContractor(), false, R.id.frame_layout);

        try {
            if (!service_id.equalsIgnoreCase("") && !service_id.equalsIgnoreCase("null")) {
                tv_title.setText("Task DashBoard");
                rl_header.setVisibility(View.VISIBLE);
                FragmentTaskDashBoard fragmentdashboard = new FragmentTaskDashBoard();
                //default selected
                addFragment(fragmentdashboard, false, R.id.frame_layout);

                Bundle bundle = new Bundle();
                bundle.putString("SERVICE_ID", service_id);
                fragmentdashboard.setArguments(bundle);

                TabLayout.Tab tab = messageTabLayout.getTabAt(1);
                if (tab != null) {
                    tab.setText("Task Dashboard");
                    tab.select();
                }
            } else {
                //default selected
                tv_title.setText("Profile");
                rl_header.setVisibility(View.GONE);
                addFragment(new FragmentProfileContractor(), false, R.id.frame_layout);
                TabLayout.Tab tab = messageTabLayout.getTabAt(0);
                if (tab != null) {
                    tab.setText("Profile");
                    tab.select();
                }
            }
        } catch (Exception e) {
            Log.e("Exception = ", "" + e);

            try {
                if (!splash.equalsIgnoreCase("") && !splash.equalsIgnoreCase("null")) {

                    TabLayout.Tab tab = messageTabLayout.getTabAt(1);
                    if (tab != null) {
                        tab.setText("Task Dashboard");
                        tab.select();
                    }

                    rl_header.setVisibility(View.VISIBLE);
                    tv_title.setText("Task DashBoard");
                    FragmentTaskDashBoard deshboard = new FragmentTaskDashBoard();
                    replaceFragment(deshboard, false, R.id.frame_layout);


                }
            } catch (Exception e1) {

                try {

                if (!dashboard.equalsIgnoreCase("") && dashboard!=null) {
               // if (dashboard.equalsIgnoreCase("DASHBOARD")) {
                    TabLayout.Tab tab = messageTabLayout.getTabAt(1);
                    if (tab != null) {
                        tab.setText("Task Dashboard");
                        tab.select();
                    }

                    rl_header.setVisibility(View.VISIBLE);
                    tv_title.setText("Task DashBoard");
                    FragmentTaskDashBoard deshboard = new FragmentTaskDashBoard();
                    replaceFragment(deshboard, false, R.id.frame_layout);

                } else {
                    //default selected
                    tv_title.setText("Profile");
                    rl_header.setVisibility(View.GONE);
                    addFragment(new FragmentProfileContractor(), false, R.id.frame_layout);
                    TabLayout.Tab tab = messageTabLayout.getTabAt(0);
                    if (tab != null) {
                        tab.setText("Profile");
                        tab.select();
                    }
                }

                }catch (Exception ex){

                    tv_title.setText("Profile");
                    rl_header.setVisibility(View.GONE);
                    addFragment(new FragmentProfileContractor(), false, R.id.frame_layout);
                    TabLayout.Tab tab = messageTabLayout.getTabAt(0);
                    if (tab != null) {
                        tab.setText("Profile");
                        tab.select();
                    }

                }
            }


        }


        //**************************************

        messageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            private void setCurrentTabFragment(int tabPosition) {
                switch (tabPosition) {
                    case 0:
                        tv_title.setText("Profile");
                        rl_header.setVisibility(View.GONE);
                        tv_title.setText("Profile");
                        FragmentProfileContractor profile = new FragmentProfileContractor();
                        replaceFragment(profile, false, R.id.frame_layout);
                        break;
                    case 1:
                        rl_header.setVisibility(View.VISIBLE);
                        tv_title.setText("Task DashBoard");
                        FragmentTaskDashBoard deshboard = new FragmentTaskDashBoard();
                        replaceFragment(deshboard, false, R.id.frame_layout);
                        break;
//                    case 2:
//                        tv_title.setText("Billing");
//                        rl_header.setVisibility(View.VISIBLE);
//                        FragmentBilling bill = new FragmentBilling();
//                        replaceFragment(bill, false, R.id.frame_layout);
//                        break;
                    /*case 3:
                        FragmentPostAnErrand post = new FragmentPostAnErrand();
                        replaceFragment(post, false, R.id.frame_layout);
                        break;*/
                    default:
                        break;
                }
            }

            /*public void replaceFragment(Fragment fragment) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.message_viewPager, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }*/
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    /*....................replaceFragment()....................................*/
    @SuppressLint("WrongConstant")
    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        String backStackName = fragment.getClass().getName();
        FragmentManager fm = getSupportFragmentManager();
        int i = fm.getBackStackEntryCount();
        while (i > 0) {
            fm.popBackStackImmediate();
            i--;
        }
        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_UNSET);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        String backStackName = fragment.getClass().getName();
        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ActivityProfileDetailContractor.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
