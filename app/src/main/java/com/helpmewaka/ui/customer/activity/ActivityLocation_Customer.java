package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.activity.ActivityLocation_Contactor;
import com.helpmewaka.ui.contractor.activity.TaskDetailsActivity;
import com.helpmewaka.ui.contractor.adapter.ContracterLiveLocationAdapter;
import com.helpmewaka.ui.contractor.model.ContractorLiveLocation;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.customer.adapter.CustomerLiveLocationAdapter;
import com.helpmewaka.ui.model.CustomerLiveLocation;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityLocation_Customer extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private ImageView iv_back;
    private JobDashBoardListData jobListData;
    RecyclerView recycler_livelocation;
    private Context context;
    String user_id,url;
    CustomerLiveLocationAdapter mAdapter;
    ArrayList<CustomerLiveLocation> customerLiveLocations=new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__customer);



        iv_back = findViewById(R.id.iv_back);
        recycler_livelocation = findViewById(R.id.recycler_livelocation);
        context= ActivityLocation_Customer.this;
        Session session = new Session(context);
        user_id = session.getUser().user_id;
        Log.e("user_id", ""+user_id);
        swipeRefreshLayout =findViewById(R.id.swipeRefreshLayout);



        iv_back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }



        if (NetworkUtil.isNetworkConnected(context)) {
            try {
             //   http://logicalsofttech.com/helpmewaka/api/customer_job_location.php
                url = API.BASE_URL + "customer_job_location.php";
                GetLiveLocationList(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));



    }

    private void GetLiveLocationList(String url) {
        AndroidNetworking.post(url)
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("Job_Code", jobListData.Job_Code)
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response_livelocation", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                // contractorLiveLocations.clear();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String CLT_ID = jsonObject1.getString("CLT_ID");
                                    String Job_Code = jsonObject1.getString("Job_Code");
                                    String JOB_ID = jsonObject1.getString("JOB_ID");
                                    String Lognitude = jsonObject1.getString("Lognitude");
                                    String Latitude = jsonObject1.getString("Latitude");
                                    String PostDt = jsonObject1.getString("PostDt");

                                    customerLiveLocations.add(i, new CustomerLiveLocation(CLT_ID,Job_Code,JOB_ID,Lognitude
                                            ,Latitude,PostDt));

                                }


                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);
                            else {
                                Toast.makeText(ActivityLocation_Customer.this, "No details found", Toast.LENGTH_SHORT).show();
                            }

                            mAdapter = new CustomerLiveLocationAdapter(customerLiveLocations, context);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(context);
                            recycler_livelocation.setLayoutManager(mLayoutManger);
                            recycler_livelocation.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            recycler_livelocation.setItemAnimator(new DefaultItemAnimator());
                            recycler_livelocation.setAdapter(mAdapter);
                            recycler_livelocation.setFocusable(false);
                            mAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;




        }
    }

    @Override
    public void onRefresh() {
        customerLiveLocations.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
        GetLiveLocationList(url);
    }
}
