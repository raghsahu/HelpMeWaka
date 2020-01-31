package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityJobDetails extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_job_code, tv_task_id, tv_job_title, tv_assign_date, tv_job_deadLine, tv_pay_status, tv_job_status, tv_job_detail;
    private String jobCode;
    private JobDashBoardListData jobListData;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job__details_);
        Session session = new Session(this);
        user_id = session.getUser().user_id;
        Intent intent = getIntent();

        try {
            if (intent.getExtras() != null) {
                jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
                jobCode = jobListData.Job_Code;
            }

        }catch (Exception e){

        }


        initView();
        clickListner();
    }


    private void initView() {
        iv_back = findViewById(R.id.iv_back);

        tv_job_code = findViewById(R.id.tv_job_code);
        tv_task_id = findViewById(R.id.tv_task_id);
        tv_job_title = findViewById(R.id.tv_job_title);
        tv_assign_date = findViewById(R.id.tv_assign_date);
        tv_job_deadLine = findViewById(R.id.tv_job_deadLine);
        tv_pay_status = findViewById(R.id.tv_pay_status);
        tv_job_status = findViewById(R.id.tv_job_status);
        tv_job_detail = findViewById(R.id.tv_job_detail);


        if (NetworkUtil.isNetworkConnected(this)) {
            try {

                String url = API.BASE_URL + "customer_job_details.php";
                Log.e("job detail cus URL = ", url);

                CallJobDetails(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }

    }


    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_job_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityJobDetails.this, ActivityJobViewMoreCust.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
            }
        });

        tv_pay_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_pay_status.getText().toString().equalsIgnoreCase("Pay now")){
                    Intent intent = new Intent(ActivityJobDetails.this, ActivityPayment.class);
                    intent.putExtra("JOBDATA", jobListData);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }


    private void CallJobDetails(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                //.addBodyParameter("CLT_ID", )
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("jobcode", jobCode)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res_job_detail_cus", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                JSONObject userdetail = jsonObject.getJSONObject("data");
                                jobListData = new JobDashBoardListData();
                                jobListData.CLT_ID = userdetail.getString("CLT_ID");
                                jobListData.JOB_ID = userdetail.getString("JOB_ID");
                                jobListData.Job_Code = userdetail.getString("Job_Code");
                                jobListData.Job_Title = userdetail.getString("Job_Title");
                                jobListData.SERV_ID = userdetail.getString("SERV_ID");
                                jobListData.Service_Requested = userdetail.getString("Service_Requested");
                                jobListData.Service_Country = userdetail.getString("Service_Country");
                                jobListData.Service_State = userdetail.getString("Service_State");
                                jobListData.Service_City = userdetail.getString("Service_City");
                                jobListData.Job_Stat = userdetail.getString("Job_Stat");
                                jobListData.JobStrtDtApprox = userdetail.getString("JobStrtDtApprox");
                                jobListData.JobEndDtApprox = userdetail.getString("JobEndDtApprox");
                                jobListData.paymentstatus = userdetail.getString("paymentstatus");
                                jobListData.Task_Code = userdetail.getString("Task_Code");

                                setData(jobListData);

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityJobDetails.this, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error forgot ", error.toString());
                        ToastClass.showToast(ActivityJobDetails.this, error.toString());
                    }
                });
    }

    private void setData(JobDashBoardListData jobData) {

        tv_job_code.setText(jobData.Job_Code);

        if (jobData.Task_Code!=null && !jobData.Task_Code.isEmpty()){
            tv_task_id.setText(jobData.Task_Code);
        }else {
            tv_task_id.setText("---");
        }

        tv_job_title.setText(jobData.Job_Title);
        tv_assign_date.setText(jobData.JobStrtDtApprox);
        tv_job_deadLine.setText(jobData.JobEndDtApprox);

        if (jobData.paymentstatus.equalsIgnoreCase("UNPAID")){
            tv_pay_status.setText("Pay now");
        }

        tv_job_status.setText(jobData.Job_Stat);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
