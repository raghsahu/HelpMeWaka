package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.helpmewaka.ui.contractor.activity.ActivityContactorTimeFrame;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCustomerTimeFrame extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_assign_date,tv_date_from,tv_date_to;
    Context context;
    private String user_id;
    private JobDashBoardListData jobListData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_time_frame);

        context= ActivityCustomerTimeFrame.this;
        Session session = new Session(context);
        user_id = session.getUser().user_id;

        iv_back=findViewById(R.id.iv_back);
        tv_assign_date=findViewById(R.id.tv_assign_date);
        tv_date_from=findViewById(R.id.tv_date_from);
        tv_date_to=findViewById(R.id.tv_date_to);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                //http://logicalsofttech.com/helpmewaka/api/customer_job_time_frame.php
                String url = API.BASE_URL + "customer_job_time_frame.php";
                GetTimeFrame(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));



    }

    private void GetTimeFrame(String url) {

        Utils.showDialog(context, "Please Wait...");
        AndroidNetworking.post(url)
                //.addBodyParameter("CNT_ID", user_id)
                //.addBodyParameter("taskcode", taskData.TASK_ID)
                .addBodyParameter("JOB_ID", jobListData.JOB_ID)
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response_timeframe_get ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {


                                JSONObject jsonObject1=jsonObject.getJSONObject("data");

                                String JOB_ID = jsonObject1.getString("JOB_ID");
                                String Assign_date = jsonObject1.getString("Assign_Date");
                                String Date_From = jsonObject1.getString("Date_From");
                                String Date_To = jsonObject1.getString("Date_To");

                                tv_assign_date.setText(Assign_date);
                                tv_date_from.setText(Date_From);
                                tv_date_to.setText(Date_To);


                            }


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
