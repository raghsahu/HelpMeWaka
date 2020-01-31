package com.helpmewaka.ui.contractor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.adapter.ContractorPaymentMilestoneAdapter;
import com.helpmewaka.ui.contractor.model.FileListContractData;
import com.helpmewaka.ui.contractor.model.Milestome_model;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityPaymentMilestone extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_view;
    LinearLayout ll_no_record;
    ContractorPaymentMilestoneAdapter mAdapter;
    String user_id;
    ImageView iv_back;
    private TaskDashBoardListData taskData;
    ArrayList<Milestome_model>milestome_models=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_milestone);

        Session session = new Session(this);
        user_id = session.getUser().user_id;
        recycler_view = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_no_record = findViewById(R.id.ll_no_record);
        iv_back = findViewById(R.id.iv_back);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            taskData = (TaskDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }


        clickListner();

        if (NetworkUtil.isNetworkConnected(ActivityPaymentMilestone.this)) {
            try {
                String url="https://helpmewaka.com/api/contractor_payment_milestone.php";
                CallPaymentMilestone(url);

            } catch (NullPointerException e) {
                ToastClass.showToast(ActivityPaymentMilestone.this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(ActivityPaymentMilestone.this, getString(R.string.no_internet_access));
        }
    }

    private void CallPaymentMilestone(String url) {
        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("Task_Code", taskData.TASK_ID)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res_cont_getfile", "" + jsonObject);
                        try {
                           // fileList.clear();
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject job = jsonArray.getJSONObject(i);

                                  String  JOB_ID = job.getString("JOB_ID");
                                    String   TASK_ID = job.getString("TASK_ID");
                                    String  CNT_ID = job.getString("CNT_ID");
                                    String Rate_Caption = job.getString("Rate_Caption");
                                    String  Amount = job.getString("Amount");

                                   milestome_models.add(new Milestome_model(JOB_ID,TASK_ID,CNT_ID,Rate_Caption,Amount));
                                }
                            }

                            //check arraylist size
//                            if (fileList.size() == 0) {
//                                swipeRefreshLayout.setVisibility(View.GONE);
//                                ll_no_record.setVisibility(View.VISIBLE);
//                            } else {
//                                swipeRefreshLayout.setVisibility(View.VISIBLE);
//                                ll_no_record.setVisibility(View.GONE);
//                            }

                            mAdapter = new ContractorPaymentMilestoneAdapter(milestome_models, ActivityPaymentMilestone.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityPaymentMilestone.this);
                            recycler_view.setLayoutManager(mLayoutManger);
                            recycler_view.setLayoutManager(new LinearLayoutManager(ActivityPaymentMilestone.this, LinearLayoutManager.VERTICAL, false));
                            recycler_view.setItemAnimator(new DefaultItemAnimator());
                            recycler_view.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            Log.e("exp jobget", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error jobjob ", error.toString());
                        ToastClass.showToast(ActivityPaymentMilestone.this, error.toString());
                    }
                });
    }

    private void clickListner() {
        iv_back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                Intent intent = new Intent(ActivityJobFileList.this, ActivityJobViewMoreCust.class);
//                intent.putExtra("JOBDATA", jobListData);
//                startActivity(intent);
//                finish();
                onBackPressed();
                break;


    }
    }

    @Override
    public void onRefresh() {
       // fileList.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
       // CallGetContractFile(file_url);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
