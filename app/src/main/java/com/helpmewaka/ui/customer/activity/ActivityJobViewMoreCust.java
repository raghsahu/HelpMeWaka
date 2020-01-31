package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.activity.ActivityContactorTimeFrame;
import com.helpmewaka.ui.contractor.activity.ActivityLocation_Contactor;
import com.helpmewaka.ui.contractor.activity.ActivityMessage_Contactor;
import com.helpmewaka.ui.contractor.activity.ActivityRatingRevie_Contactor;
import com.helpmewaka.ui.contractor.activity.ActivityTaskJobDetails;
import com.helpmewaka.ui.contractor.activity.ActivityTaskViewMoreCont;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityJobViewMoreCust extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rl_update, rl_file, rl_payment, rl_dispute, rl_contact_adm,
            rl_frame,rl_msg,rl_location,rl_ratting_review,rl_details;
    private ImageView iv_back;
    private JobDashBoardListData jobListData;
    private Dialog dialog;
    private Button btn_submit, btn_submit_dispute;
    private EditText et_msg, et_msg_dispute;
    private TextView tv_title;
    private String user_id;
    TextView tv_job_id;
    CardView card_update_job,card_job_details,card_time_frame,card_files,card_payment
            ,card_message,card_livelocation,card_review_rating,card_dispute,card_contact_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_view_more);
        Session session = new Session(this);
        user_id = session.getUser().user_id;

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        initView();
        clickListner();
       // http://logicalsofttech.com/helpmewaka/api/customer_job_details.php
    }


    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        rl_update = findViewById(R.id.rl_update);
        rl_file = findViewById(R.id.rl_file);
        rl_payment = findViewById(R.id.rl_payment);
        rl_dispute = findViewById(R.id.rl_dispute);
        rl_contact_adm = findViewById(R.id.rl_contact_adm);
        rl_frame = findViewById(R.id.rl_frame);
        rl_msg = findViewById(R.id.rl_msg);
        rl_location = findViewById(R.id.rl_location);
        rl_ratting_review = findViewById(R.id.rl_ratting_review);
        rl_details = findViewById(R.id.rl_details);
        tv_job_id = findViewById(R.id.tv_job_id);

        card_dispute = findViewById(R.id.card_dispute);
        card_contact_admin = findViewById(R.id.card_contact_admin);
        card_update_job=findViewById(R.id.card_update_job);
        card_job_details=findViewById(R.id.card_job_details);
        card_time_frame=findViewById(R.id.card_time_frame);
        card_files=findViewById(R.id.card_files);
        card_payment=findViewById(R.id.card_payment);
        card_message=findViewById(R.id.card_message);
        card_livelocation=findViewById(R.id.card_livelocation);
        card_review_rating=findViewById(R.id.card_review_rating);

        if (jobListData.Job_Stat.equalsIgnoreCase("Review Pending")) {
            card_dispute.setVisibility(View.VISIBLE);
        } else card_dispute.setVisibility(View.GONE);

        if (jobListData.Job_Stat.equalsIgnoreCase("In Progress")){

            //if ()
            card_update_job.setVisibility(View.GONE);
            card_dispute.setVisibility(View.VISIBLE);
        }else {
            card_update_job.setVisibility(View.VISIBLE);
            card_dispute.setVisibility(View.GONE);
        }

        if (jobListData.Job_Stat.equalsIgnoreCase("Dispute")){
            card_update_job.setVisibility(View.GONE);
            card_time_frame.setVisibility(View.GONE);
            card_message.setVisibility(View.GONE);
            card_livelocation.setVisibility(View.GONE);
            card_review_rating.setVisibility(View.GONE);
            card_dispute.setVisibility(View.GONE);

        }else {

        }

        if (jobListData.Job_Stat.equalsIgnoreCase("Open")){
            card_update_job.setVisibility(View.GONE);
            card_time_frame.setVisibility(View.GONE);
            card_message.setVisibility(View.GONE);
            card_livelocation.setVisibility(View.GONE);
            card_review_rating.setVisibility(View.GONE);
            card_dispute.setVisibility(View.VISIBLE);
        }


        if (jobListData != null) {
            tv_job_id.setText("Job#" + " " + jobListData.Job_Code);
        }
    }

    private void clickListner() {

        iv_back.setOnClickListener(this);
        rl_update.setOnClickListener(this);
        rl_file.setOnClickListener(this);
        rl_payment.setOnClickListener(this);
        rl_dispute.setOnClickListener(this);
        rl_contact_adm.setOnClickListener(this);
        rl_frame.setOnClickListener(this);
        rl_msg.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_ratting_review.setOnClickListener(this);
        rl_details.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityJobDetails.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_update:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityUpdateJob.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_details:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityCustJobDetails.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_frame:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityCustomerTimeFrame.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();

                break;

            case R.id.rl_file:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityJobFileList.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_payment:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityPayment.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_msg:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityMessage_Customer.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;


            case R.id.rl_location:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityLocation_Customer.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_ratting_review:
                intent = new Intent(ActivityJobViewMoreCust.this, ActivityRatingRevie_Customer.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_dispute:
                openDisputeDialog();
                break;
            case R.id.rl_contact_adm:
                openAdminDialog();
                break;

            case R.id.btn_submit:

                if (et_msg.getText().toString().equalsIgnoreCase("")) {
                    ToastClass.showToast(this, "Please input some message content");
                } else {


                    if (NetworkUtil.isNetworkConnected(this)) {
                        try {
                            String url = API.BASE_URL + "contact_admin_message_post.php";
                            CallAdminJob(url);
                            //ToastClass.showToast(context, "post....");
                        } catch (NullPointerException e) {
                            ToastClass.showToast(this, getString(R.string.too_slow));
                        }
                    } else {
                        ToastClass.showToast(this, getString(R.string.no_internet_access));
                    }

                }
                break;

            case R.id.btn_submit_dispute:

                if (et_msg_dispute.getText().toString().equalsIgnoreCase("")) {
                    ToastClass.showToast(this, "Please input your reason of dispute");
                } else {

                    if (NetworkUtil.isNetworkConnected(this)) {
                        try {
                            String url = API.BASE_URL + "dispute_customer.php";
                            CallDisputeJob(url);
                            //ToastClass.showToast(context, "post....");
                        } catch (NullPointerException e) {
                            ToastClass.showToast(this, getString(R.string.too_slow));
                        }
                    } else {
                        ToastClass.showToast(this, getString(R.string.no_internet_access));
                    }

                }
                break;
        }


    }

    private void openAdminDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.admin_dialog_cus);
        et_msg = dialog.findViewById(R.id.et_msg);
        tv_title = dialog.findViewById(R.id.tv_title);
        btn_submit = dialog.findViewById(R.id.btn_submit);


        tv_title.setText(jobListData.Job_Code + " - " + jobListData.Job_Title);
        btn_submit.setOnClickListener(this);

        dialog.show();
    }


    private void openDisputeDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dispute_dialog_cus);
        et_msg_dispute = dialog.findViewById(R.id.et_msg_dispute);
        btn_submit_dispute = dialog.findViewById(R.id.btn_submit_dispute);


        btn_submit_dispute.setOnClickListener(this);

        dialog.show();
    }


    private void CallAdminJob(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                //.addBodyParameter("CLT_ID", )
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("Job_Code", jobListData.Job_Code)
                .addBodyParameter("message_content", et_msg.getText().toString())
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res admin cus", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                dialog.dismiss();
                                ToastClass.showToast(ActivityJobViewMoreCust.this, message);

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityJobViewMoreCust.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp jobadmn", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Log.e("error jobadmn ", error.toString());
                        ToastClass.showToast(ActivityJobViewMoreCust.this, error.toString());
                    }
                });
    }

    private void CallDisputeJob(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                //.addBodyParameter("CLT_ID", )
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("JobCode", jobListData.Job_Code)
                .addBodyParameter("Dispute_Comments", et_msg_dispute.getText().toString())
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res dispute cus", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                dialog.dismiss();
                                rl_dispute.setVisibility(View.GONE);
                                ToastClass.showToast(ActivityJobViewMoreCust.this, message);

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityJobViewMoreCust.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp jobdispute", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error jobdispute ", error.toString());
                        ToastClass.showToast(ActivityJobViewMoreCust.this, error.toString());
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityJobViewMoreCust.this, ActivityJobDetails.class);
        intent.putExtra("JOBDATA", jobListData);
        startActivity(intent);
        finish();
    }
}
