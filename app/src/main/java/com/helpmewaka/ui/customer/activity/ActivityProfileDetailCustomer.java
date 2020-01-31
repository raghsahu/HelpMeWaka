package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.activity.Service_FragModel.SubCateFragAdapter;
import com.helpmewaka.ui.customer.fragment.FragmentJobDashBoard;
import com.helpmewaka.ui.customer.fragment.FragmentPostAnErrand;
import com.helpmewaka.ui.customer.fragment.FragmentProfileCustomer;

import static com.helpmewaka.ui.customer.fragment.FragmentPostAnErrand.tv_spin_job;

public class ActivityProfileDetailCustomer extends AppCompatActivity implements SubCateFragAdapter.AdapterCallback{

    private TabLayout messageTabLayout;
    public static TextView tv_title;

    private String service_id = "";
     String service_name = "";
     String Category_id = "";
     String Category_title = "";
    private String drawerclick = "";
    private String dashboard = "";
    public static RelativeLayout rl_header;
    public static String SERVICE_id,Category_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail_customer);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            service_id = intent.getStringExtra("SERVICE_ID");
            service_name = intent.getStringExtra("SERVICE_NAME");
            Category_id = intent.getStringExtra("Category_id");
            Category_title = intent.getStringExtra("Category_title");
            dashboard = intent.getStringExtra("DASHBOARD");
            drawerclick = intent.getStringExtra("POST");

        }
        initView();
    }

    private void initView() {
        messageTabLayout = findViewById(R.id.message_tabLayout);
        tv_title = findViewById(R.id.tv_title);
        rl_header = findViewById(R.id.rl_header);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityProfileDetailCustomer.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        //add button text
        messageTabLayout.addTab(messageTabLayout.newTab().setText("Profile"));
        messageTabLayout.addTab(messageTabLayout.newTab().setText("DashBoard"));
        //messageTabLayout.addTab(messageTabLayout.newTab().setText("Billing"));
        messageTabLayout.addTab(messageTabLayout.newTab().setText("Post Job"));

        messageTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        try {
            if (!service_id.equalsIgnoreCase("")
                    && !service_id.equalsIgnoreCase("null")) {
                tv_title.setText("Post Job");
                rl_header.setVisibility(View.VISIBLE);

                TabLayout.Tab tab = messageTabLayout.getTabAt(2);
                if (tab != null) {
                    tab.setText("Post Job");
                    tab.select();
                }

                FragmentPostAnErrand fragmentPostAnErrand = new FragmentPostAnErrand();
                //default selected
                addFragment(fragmentPostAnErrand, false, R.id.frame_layout);

                Bundle bundle = new Bundle();
                bundle.putString("SERVICE_ID", service_id);
                bundle.putString("SERVICE_NAME", service_name);
                bundle.putString("Category_id", Category_id);
                bundle.putString("Category_title", Category_title);
                fragmentPostAnErrand.setArguments(bundle);

            } else {
                //default selected
                addFragment(new FragmentProfileCustomer(), false, R.id.frame_layout);
                TabLayout.Tab tab = messageTabLayout.getTabAt(0);
                if (tab != null) {
                    tab.setText("Profile");
                    tab.select();
                }
            }
        } catch (Exception e) {
            Log.e("Exception = ", "" + e);

            try {
                if (!drawerclick.equalsIgnoreCase("")
                        && !drawerclick.equalsIgnoreCase("null")) {
                /*tv_title.setText("Post Job");
                rl_header.setVisibility(View.VISIBLE);
                FragmentPostAnErrand fragmentPostAnErrand = new FragmentPostAnErrand();
                //default selected
                addFragment(fragmentPostAnErrand, false, R.id.frame_layout);

                Bundle bundle = new Bundle();
                bundle.putString("SERVICE_ID", service_id);
                fragmentPostAnErrand.setArguments(bundle);*/

                    TabLayout.Tab tab = messageTabLayout.getTabAt(2);
                    if (tab != null) {
                        tab.setText("Post Job");
                        tab.select();
                    }

                    rl_header.setVisibility(View.VISIBLE);
                    tv_title.setText("Post An Errand");
                    FragmentPostAnErrand post = new FragmentPostAnErrand();
                    replaceFragment(post, false, R.id.frame_layout);

                }

            } catch (Exception e1) {

                if (!dashboard.equalsIgnoreCase("") && !dashboard.equalsIgnoreCase("null")) {
                    // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    //default selected
                    tv_title.setText("DashBoard");
                    rl_header.setVisibility(View.VISIBLE);
                    addFragment(new FragmentJobDashBoard(), false, R.id.frame_layout);
                    TabLayout.Tab tab = messageTabLayout.getTabAt(1);
                    if (tab != null) {
                        tab.setText("DashBoard");
                        tab.select();
                    }
                } else {
                    //default selected
                    tv_title.setText("Profile");
                    rl_header.setVisibility(View.GONE);
                    addFragment(new FragmentProfileCustomer(), false, R.id.frame_layout);
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
                        rl_header.setVisibility(View.GONE);
                        tv_title.setText("Profile");
                        FragmentProfileCustomer profile = new FragmentProfileCustomer();
                        replaceFragment(profile, false, R.id.frame_layout);
                        break;
                    case 1:
                        rl_header.setVisibility(View.VISIBLE);
                        tv_title.setText("DashBoard");
                        FragmentJobDashBoard deshboard = new FragmentJobDashBoard();
                        replaceFragment(deshboard, false, R.id.frame_layout);
                        break;
//                    case 2:
//                        rl_header.setVisibility(View.VISIBLE);
//                        tv_title.setText("Billing");
//                        FragmentBilling bill = new FragmentBilling();
//                        replaceFragment(bill, false, R.id.frame_layout);
//                        break;
                    case 2:
                        rl_header.setVisibility(View.VISIBLE);
                        tv_title.setText("Post An Errand");
                        FragmentPostAnErrand post = new FragmentPostAnErrand();
                        replaceFragment(post, false, R.id.frame_layout);
                        break;
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
    public void onMethodCallback(String SERVICE_ID, String category_Id, String SERVICE_name, String category_title) {

        tv_spin_job.setText(category_title+"-> "+SERVICE_name);
         SERVICE_id=SERVICE_ID;
        Category_Id=category_Id;

    }
}
