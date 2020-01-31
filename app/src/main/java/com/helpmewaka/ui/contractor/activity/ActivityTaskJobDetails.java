package com.helpmewaka.ui.contractor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityTaskJobDetails extends AppCompatActivity {

    private String user_id;
    private ImageView iv_back;
    Button btn_update;
    private TaskDashBoardListData taskData;
    TextView tv_doyou_need,tv_contact_name,tv_contact_nmbr,tv_special_req,tv_job_code,tv_task_code,
    tv_service_type,tv_country,tv_state,tv_city,tv_Post_Date,tv_Job_Status;
    private String Job_Code,Job_Title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_job_details);

        Session session = new Session(this);
        user_id = session.getUser().user_id;

        iv_back = findViewById(R.id.iv_back);
        tv_doyou_need = findViewById(R.id.tv_doyou_need);
        tv_contact_name = findViewById(R.id.tv_contact_name);
        tv_contact_nmbr = findViewById(R.id.tv_contact_nmbr);
        tv_special_req = findViewById(R.id.tv_special_req);
        tv_job_code = findViewById(R.id.tv_job_code);
        tv_task_code = findViewById(R.id.tv_task_code);
        tv_service_type = findViewById(R.id.tv_service_type);
        tv_country = findViewById(R.id.tv_country);
        tv_state = findViewById(R.id.tv_state);
        tv_city = findViewById(R.id.tv_city);
        tv_Post_Date = findViewById(R.id.tv_Post_Date);
        tv_Job_Status = findViewById(R.id.tv_Job_Status);
        btn_update = findViewById(R.id.btn_update);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            taskData = (TaskDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // JOB# 100001 - Grocery drop off request

                String jobdetails="JOB# "+Job_Code+" - "+Job_Title;
               // Toast.makeText(ActivityTaskJobDetails.this, ""+jobdetails, Toast.LENGTH_SHORT).show();
                String url = API.BASE_URL + "contractor_complete_job.php";
                Complete_Job(url,jobdetails);

            }
        });

        if (NetworkUtil.isNetworkConnected(this)) {
            try {
                String url = API.BASE_URL + "contractor_job_details.php";
                GetContractTaskDetails(url);
                //ToastClass.showToast(context, "post....");
            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }

    }

    private void Complete_Job(String url, String jobdetails) {
        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("jobdetails", jobdetails)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res_msg_post", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                Utils.dismissDialog();
                                ToastClass.showToast(ActivityTaskJobDetails.this, message);



                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityTaskJobDetails.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp jobadmn", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error jobadmn ", error.toString());
                        // ToastClass.showToast(ActivityTaskJobDetails.this, error.toString());
                    }
                });

    }

    private void GetContractTaskDetails(String url) {
        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("Task_Code", taskData.TASK_ID)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res_msg_post", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                Utils.dismissDialog();
                                ToastClass.showToast(ActivityTaskJobDetails.this, message);

                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                   String JOB_ID = jsonObject1.getString("JOB_ID");
                                    String CNT_ID = jsonObject1.getString("CNT_ID");
                                    Job_Code = jsonObject1.getString("Job_Code");
                                    Job_Title = jsonObject1.getString("Job_Title");
                                    String TASK_ID = jsonObject1.getString("TASK_ID");
                                    String Service_Requested = jsonObject1.getString("Service_Requested");
                                    String Service_Country = jsonObject1.getString("Service_Country");
                                    String Service_State = jsonObject1.getString("Service_State");
                                    String State_Name = jsonObject1.getString("State_Name");
                                    String Service_City = jsonObject1.getString("Service_City");
                                    String Contact_Number = jsonObject1.getString("Contact_Number");
                                    String Contact_Name = jsonObject1.getString("Contact_Name");
                                    String Special_Request = jsonObject1.getString("Special_Request");
                                    String AssignDt = jsonObject1.getString("AssignDt");
                                    String JobStartDt = jsonObject1.getString("JobStartDt");
                                    String JobEndDt = jsonObject1.getString("JobEndDt");
                                    String Job_StatName = jsonObject1.getString("Job_StatName");


                                    try {
                                        tv_job_code.setText(Job_Code);
                                        tv_doyou_need.setText(Service_Requested);
                                        tv_contact_name.setText(Contact_Name);
                                        tv_contact_nmbr.setText(Contact_Number);
                                        tv_special_req.setText(Special_Request);
                                        tv_task_code.setText(TASK_ID);
                                        tv_service_type.setText(Job_Title);
                                        tv_country.setText(Service_Country);
                                        tv_state.setText(State_Name);
                                        tv_city.setText(Service_City);
                                        tv_Post_Date.setText(AssignDt);
                                        tv_Job_Status.setText(Job_StatName);


                                    }catch (Exception e){

                                    }




                                }




                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityTaskJobDetails.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp jobadmn", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error jobadmn ", error.toString());
                       // ToastClass.showToast(ActivityTaskJobDetails.this, error.toString());
                    }
                });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
