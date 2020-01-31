package com.helpmewaka.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helpmewaka.BuildConfig;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.common.ActivityAbout;
import com.helpmewaka.ui.activity.common.ActivityContactUs;
import com.helpmewaka.ui.activity.common.ActivityCost;
import com.helpmewaka.ui.activity.common.ActivityFaq;
import com.helpmewaka.ui.activity.common.ActivityHowItWork;
import com.helpmewaka.ui.activity.common.ActivityServices;
import com.helpmewaka.ui.activity.common.ActivityTermCondition;
import com.helpmewaka.ui.adapter.CustomPagerAdapter;
import com.helpmewaka.ui.adapter.DeshboardMenuAdapter;
import com.helpmewaka.ui.contractor.activity.ActivityNotitfication;
import com.helpmewaka.ui.contractor.activity.ActivityProfileDetailContractor;
import com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer;
import com.helpmewaka.ui.model.ServiceListData;
import com.helpmewaka.ui.model.WorkListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.APIClient;
import com.helpmewaka.ui.server.Api_Call;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private List<WorkListData> menuList;
    private RecyclerView recyclerview;
    private DeshboardMenuAdapter mAdapter;
    private Session session;
    CardView card_spinner,card_dashboard;
    TextView tv_spin_job;
    private DrawerLayout drawer;
    private View headerview;
    private LinearLayout nav_header;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RelativeLayout rl_notification;
    private String loginType = "";
    private ArrayList<ServiceListData> catList;
    private Spinner spinner;
    private String servie_id;
    private ViewPager VP_banner_slidder;
    private CircleIndicator CI_indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public int[] sliderImage = {
            R.drawable.home_slider1,
            R.drawable.home_slider2,
            R.drawable.home_slider3
    };
     RecyclerView  recycler_spin;
    Custom_spin_Recycler custom_spin_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new Session(this);
        loginType = session.getUser().Type;
        Log.e("user_id ", session.getUser().user_id);
        initView();
        ClickListner();


    }

    private void initView() {

        //initialize memory
        catList = new ArrayList<>();
        menuList = new ArrayList<>();

        spinner = findViewById(R.id.spinner);
        rl_notification = findViewById(R.id.rl_notification);
        recyclerview = findViewById(R.id.recycler_view);
        card_spinner = findViewById(R.id.card_spinner);
        card_dashboard = findViewById(R.id.card_dashboard);
        tv_spin_job = findViewById(R.id.tv_spin_job);
        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        VP_banner_slidder = findViewById(R.id.user_home_viewpager);
        CI_indicator = findViewById(R.id.user_home_ci_indicator);
        //navigationView1 = findViewById(R.job_id.nav_view);
        headerview = navigationView.getHeaderView(0);

        nav_header = headerview.findViewById(R.id.nav_header_lay);
        CircleImageView nav_imageView = headerview.findViewById(R.id.nav_imageView);
        TextView nav_tv_name = headerview.findViewById(R.id.nav_tv_name);

        Banner_Call();
        Menu nav_Menu = navigationView.getMenu();

        if (loginType.equalsIgnoreCase("contractor")) {
            toolbar.setTitle("Contractor Home");
            nav_Menu.findItem(R.id.nav_postJob).setVisible(false);
            card_dashboard.setVisibility(View.VISIBLE);
            card_spinner.setVisibility(View.GONE);
        } else if (loginType.equalsIgnoreCase("customer")) {
            toolbar.setTitle("Customer Home");
            nav_Menu.findItem(R.id.nav_postJob).setVisible(true);
        }


        if (session != null) {

            if (!session.getUser().FName.equalsIgnoreCase("") && session.getUser().FName != null) {
                nav_tv_name.setText(session.getUser().FName + " " + session.getUser().LName);
            }


            Log.e("image_sesion ", session.getUser().Pic);

            if (session.getUser().Pic != null && !session.getUser().Pic.isEmpty()) {

                if (loginType.equalsIgnoreCase("contractor")) {
                    Picasso.with(this).load(API.BASE_URL_IMG_CON + session.getUser().Pic)
                            .placeholder(R.drawable.ic_profile)
                            .into(nav_imageView);
                } else if (loginType.equalsIgnoreCase("customer")) {
                    Picasso.with(this).load(API.BASE_URL_IMG_CUST + session.getUser().Pic)
                            .placeholder(R.drawable.ic_profile)
                            .into(nav_imageView);
                }


            }

        }
        setSupportActionBar(toolbar);

        /*swipeRefreshLayout = findViewById(R.job_id.swipe_refresh_layout);
        l_no_record = findViewById(R.job_id.no_record);*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (NetworkUtil.isNetworkConnected(this)) {
            try {
                String url = API.BASE_URL + "home_services_list.php";
               // Spin_Get_Service(url);
                //Spin_Get_ServiceRetro();

            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(this, getString(R.string.no_internet_access));

       // WorkListData moving_service = new WorkListData("Move Item in Nigeria?", "Moving Service", R.drawable.ic_moving1);
        WorkListData moving_service = new WorkListData("Hamper Services in Nigeria?", "Moving Service", R.drawable.ic_travel1);
        WorkListData account = new WorkListData("Do you need an Accountant?", "Finacial Service", R.drawable.ic_finacial1);
        WorkListData legal = new WorkListData("Do you need a Lawyer?", "Legal Service", R.drawable.ic_lawyer1);
        WorkListData general = new WorkListData("Nursing an elderly person", "General Service", R.drawable.ic_nurse1);
        WorkListData construction = new WorkListData("Facilitate Building and construction", "Construction", R.drawable.ic_construction);
        WorkListData travel = new WorkListData("Need a vacation in Nigeria?", "Travel", R.drawable.ic_travel1);

        menuList.add(moving_service);
        menuList.add(account);
        menuList.add(legal);
        menuList.add(general);
        menuList.add(construction);
        menuList.add(travel);

        mAdapter = new DeshboardMenuAdapter(menuList, this);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);


        //************************tv_spin_onclick
        tv_spin_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog NextQuizLevel_dialog;

                NextQuizLevel_dialog = new Dialog(MainActivity.this);
                NextQuizLevel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                NextQuizLevel_dialog.setCancelable(true);
                NextQuizLevel_dialog.setContentView(R.layout.custom_spin_dialog);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(NextQuizLevel_dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                NextQuizLevel_dialog.getWindow().setAttributes(lp);

                recycler_spin=NextQuizLevel_dialog.findViewById(R.id.recycler_spin);

                Spin_Get_ServiceRetro();


                try {
                    if (!MainActivity.this.isFinishing()){
                        NextQuizLevel_dialog.show();
                    }
                }
                catch (WindowManager.BadTokenException e) {
                    //use a log message
                }
            }
        });

    }

    private void Spin_Get_ServiceRetro() {

        Utils.showDialog(this, "Loading Please Wait...");

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<Custom_Spinner_Model> call = apiInterface.GetHomeServiceDetails();

        call.enqueue(new Callback<Custom_Spinner_Model>() {
            @Override
            public void onResponse(Call<Custom_Spinner_Model> call, Response<Custom_Spinner_Model> response) {

                try{
                    Utils.dismissDialog();
                    if (response!=null){
                        Log.e("spin_result",""+response.body().getResults());

                        if (response.body().getResults().equalsIgnoreCase("true")){
                            // Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();

                            //*******************custom spinner recycler
//                            Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
//                            CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(MainActivity.this,
//                                    R.layout.listitems_layout, response.body().getData());
//                            spinner1.setAdapter(adapter1);

                            Log.e("data_spin", ""+response.body().getData().size());
                            for (int i=0; i<response.body().getData().size(); i++){

                                Log.e("spin_sub_title", ""+response.body().getData().get(i).getServiceTitle());
                               // Log.e("spin_sub_data_size", ""+response.body().getData().get(i).getServieSubCate().size());

//                                for (int k=0; k<response.body().getData().get(i).getServieSubCate().size(); i++){
//
//                                    Log.e("spin_sub_data1", ""+response.body().getData().get(i)
//                                            .getServieSubCate().get(k).getServiceSubcategory());
//
//
//                                }

                            }

                            custom_spin_recycler = new Custom_spin_Recycler(response.body().getData(), MainActivity.this);
                            recycler_spin.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
                            recycler_spin.setAdapter(custom_spin_recycler);
                            //mAdapter.notifyDataSetChanged();


                            //*************************************
                        }else {
                            Toast.makeText(MainActivity.this, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_Dr_login", e.getMessage());
                }
               // progressDialog.dismiss();
                Utils.dismissDialog();
            }

            @Override
            public void onFailure(Call<Custom_Spinner_Model> call, Throwable t) {
               // progressDialog.dismiss();
                Utils.dismissDialog();
                Log.e("error_spin",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void Banner_Call() {

        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(MainActivity.this);
        VP_banner_slidder.setAdapter(customPagerAdapter);
        CI_indicator.setViewPager(VP_banner_slidder);
        NUM_PAGES = sliderImage.length;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                VP_banner_slidder.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        CI_indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    private void ClickListner() {

        rl_notification.setOnClickListener(this);
        nav_header.setOnClickListener(this);
        card_dashboard.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servie_id = catList.get(position).SERV_ID;

                if (!servie_id.equalsIgnoreCase("-1")) {
                    if (loginType.equalsIgnoreCase("contractor")) {
                        Intent intent = new Intent(MainActivity.this, ActivityProfileDetailContractor.class);
                        intent.putExtra("SERVICE_ID", servie_id);
                        startActivity(intent);
                    } else if (loginType.equalsIgnoreCase("customer")) {
                        Intent intent = new Intent(MainActivity.this, ActivityProfileDetailCustomer.class);
                        intent.putExtra("SERVICE_ID", servie_id);
                        startActivity(intent);
                    }

                }

                Log.e("Cat name ", "" + catList.get(position).Service_Title + "id = " + catList.get(position).SERV_ID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_about_us) {
            Intent i = new Intent(MainActivity.this, ActivityAbout.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_service) {
            Intent i = new Intent(this, ActivityServices.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_work) {
            Intent i = new Intent(this, ActivityHowItWork.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_cost) {
            Intent i = new Intent(this, ActivityCost.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_dashboard) {
            Intent intent;
            if (loginType.equalsIgnoreCase("contractor")) {
                intent = new Intent(getApplicationContext(), ActivityProfileDetailContractor.class);
                intent.putExtra("DASHBOARD", "DASHBOARD");
                startActivity(intent);
            } else if (loginType.equalsIgnoreCase("customer")) {
                intent = new Intent(getApplicationContext(), ActivityProfileDetailCustomer.class);
                intent.putExtra("DASHBOARD", "DASHBOARD");
                startActivity(intent);
            }
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_postJob) {
            Intent intent;
            if (loginType.equalsIgnoreCase("contractor")) {
                intent = new Intent(getApplicationContext(), ActivityProfileDetailContractor.class);
                intent.putExtra("POST", "POST");
                startActivity(intent);
            } else if (loginType.equalsIgnoreCase("customer")) {
                intent = new Intent(getApplicationContext(), ActivityProfileDetailCustomer.class);
                intent.putExtra("POST", "POST");
                startActivity(intent);
            }
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_contact) {
            Intent i = new Intent(this, ActivityContactUs.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_faq) {
            Intent i = new Intent(this, ActivityFaq.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_term_cond) {
            Intent i = new Intent(this, ActivityTermCondition.class);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_share) {
            shareTextUrl();

        } else if (id == R.id.nav_logout) {
            LogOut();

        }

        /*DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.nav_header_lay:
                try {
                    if (loginType.equalsIgnoreCase("contractor")) {
                        intent = new Intent(getApplicationContext(), ActivityProfileDetailContractor.class);
                        //intent.putExtra("DASHBOARD", "DASHBOARD");
                        startActivity(intent);
                    } else if (loginType.equalsIgnoreCase("customer")) {
                        intent = new Intent(getApplicationContext(), ActivityProfileDetailCustomer.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("Excep click pro", "" + e);
                }

                break;

            case R.id.rl_notification:
                intent = new Intent(getApplicationContext(), ActivityNotitfication.class);
                startActivity(intent);
                break;

            case R.id.card_dashboard:
                if (loginType.equalsIgnoreCase("contractor")) {
                    intent = new Intent(getApplicationContext(), ActivityProfileDetailContractor.class);
                    intent.putExtra("DASHBOARD", "DASHBOARD");
                    startActivity(intent);
                }
                break;
        }
    }

    private void LogOut() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("HelpMeWaka")
                .setMessage("Do you want to log out?");

        dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                // mGoogleSignInClient.signOut();
                // LoginManager.getInstance().logOut();
                session.logout();

            }


        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void shareTextUrl() {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HelpMeWaka");
            String shareMessage= "\nWelcome to HelpMeWaka! You can download app from Play Store:-\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }

    }

    private void Spin_Get_Service(String url) {

        Utils.showDialog(this, "Loading Please Wait...");

        AndroidNetworking.get(url)
                .setTag("List")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response servicelist ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");
                            catList.clear();
                            catList.add(new ServiceListData("SELECT YOUR ERRAND REQUEST"));
                            if (result.equalsIgnoreCase("true")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<ServiceListData>>() {
                                }.getType();
                                //json array
                                List<ServiceListData> data = gson.fromJson(jsonObject.getString("data"), listType);
                                //List<ProductData> data = (List<ProductData>) gson.fromJson(jsonObject.getString("Product_list").toString(), Products.class);
                                catList.addAll(data);

                            } //else Utils.openAlertDialog(ActivityNibblersDetail.this, result);


                            ArrayAdapter<ServiceListData> aa = new ArrayAdapter<ServiceListData>(MainActivity.this, R.layout.list_item_spinner, catList) {
                                @NonNull
                                @Override
                                //set hint text by default in center
                                public View getView(int position, @Nullable View cView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, cView, parent);
                                    view.setPadding(view.getLeft(), view.getTop(), 0, view.getBottom());
                                    return view;
                                }
                            };
                            aa.setDropDownViewResource(R.layout.list_item_spinner);
                            //Setting the ArrayAdapter data on the Spinner
                            spinner.setAdapter(aa);
                            Log.e("size service = ", "" + catList.size());

                            //set select category
                           /* for (int i = 0; i < catList.size(); i++) {
                                if (catList.get(i).id.equalsIgnoreCase(cat_id)) {
                                    spinner.setSelection(i);
                                }
                            }*/




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();
                        Log.e("error", error.getErrorDetail());
                    }
                });
    }
}
