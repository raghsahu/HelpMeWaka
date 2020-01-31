package com.helpmewaka.ui.contractor.activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.customer.activity.ActivityJobDetails;
import com.helpmewaka.ui.customer.activity.ActivityJobFileList;
import com.helpmewaka.ui.customer.activity.ActivityJobViewMoreCust;
import com.helpmewaka.ui.customer.activity.ActivityPayment;
import com.helpmewaka.ui.customer.activity.ActivityUpdateJob;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityTaskViewMoreCont extends AppCompatActivity implements View.OnClickListener {

     RelativeLayout rl_update, rl_frame, rl_file, rl_payment,rl_msg,rl_location,rl_ratting_review,
    rl_contact_adm;
    private ImageView iv_back;
    private TaskDashBoardListData taskData;
    private Dialog dialog;
    private Button btn_submit, btn_submit_dispute;
    private EditText et_msg, et_msg_dispute;
    private TextView tv_title;
    private String user_id;
    private TextView tv_task_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_more_cont);

        Session session = new Session(this);
        user_id = session.getUser().user_id;

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            taskData = (TaskDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        initView();
        clickListner();
    }


    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        rl_update = findViewById(R.id.rl_update);
        rl_frame = findViewById(R.id.rl_frame);
        rl_file = findViewById(R.id.rl_file);
        rl_payment = findViewById(R.id.rl_payment);
        tv_task_id = findViewById(R.id.tv_task_id);
        rl_msg = findViewById(R.id.rl_msg);
        rl_location = findViewById(R.id.rl_location);
        rl_ratting_review = findViewById(R.id.rl_ratting_review);

        rl_contact_adm = findViewById(R.id.rl_contact_adm);

      /*  if (taskData.Job_Stat.equalsIgnoreCase("Review Pending")) {
            rl_dispute.setVisibility(View.VISIBLE);
        } else rl_dispute.setVisibility(View.GONE);*/

        if (taskData != null) {
            tv_task_id.setText("Task#" + " " + taskData.TASK_ID);
        }

    }

    private void clickListner() {

        iv_back.setOnClickListener(this);
       rl_update.setOnClickListener(this);
        rl_file.setOnClickListener(this);
        rl_frame.setOnClickListener(this);
        rl_payment.setOnClickListener(this);
        rl_contact_adm.setOnClickListener(this);
        rl_msg.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_ratting_review.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                intent = new Intent(ActivityTaskViewMoreCont.this, TaskDetailsActivity.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_update:
                intent = new Intent(ActivityTaskViewMoreCont.this, ActivityTaskJobDetails.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_frame:
                intent = new Intent(ActivityTaskViewMoreCont.this, ActivityContactorTimeFrame.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();

                break;
            case R.id.rl_file:
               //intent = new Intent(ActivityTaskViewMoreCont.this, ActivityJobFileList.class);
               intent = new Intent(ActivityTaskViewMoreCont.this, ActivityFileListContactor.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_payment:
                intent = new Intent(ActivityTaskViewMoreCont.this, ActivityPaymentMilestone.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_msg:
                intent = new Intent(ActivityTaskViewMoreCont.this, ActivityMessage_Contactor.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_ratting_review:
                intent = new Intent(ActivityTaskViewMoreCont.this, ActivityRatingRevie_Contactor.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
                break;

            case R.id.rl_location:
                intent = new Intent(ActivityTaskViewMoreCont.this, ActivityLocation_Contactor.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
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


        tv_title.setText(taskData.Job_Code + " - " + taskData.Job_Title);
        btn_submit.setOnClickListener(this);

        dialog.show();
    }


    private void CallAdminJob(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                //.addBodyParameter("CLT_ID", )
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("Job_Code", taskData.Job_Code)
                .addBodyParameter("message_content", et_msg.getText().toString())
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
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                dialog.dismiss();
                                ToastClass.showToast(ActivityTaskViewMoreCont.this, message);

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityTaskViewMoreCont.this, message);
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
                        ToastClass.showToast(ActivityTaskViewMoreCont.this, error.toString());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityTaskViewMoreCont.this, TaskDetailsActivity.class);
        intent.putExtra("JOBDATA", taskData);
        startActivity(intent);
        finish();
    }
}
