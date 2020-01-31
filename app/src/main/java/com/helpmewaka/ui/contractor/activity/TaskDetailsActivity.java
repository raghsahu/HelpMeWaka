package com.helpmewaka.ui.contractor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.customer.activity.ActivityJobViewMoreCust;
import com.helpmewaka.ui.server.Session;

public class TaskDetailsActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_job_code, tv_task_id, tv_job_title, tv_assign_date, tv_job_deadLine, tv_pay_status, tv_job_status, tv_job_detail;
    private String user_id;
    private String jobCode;
    private TaskDashBoardListData taskListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        Session session = new Session(this);
        user_id = session.getUser().user_id;
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            taskListData = (TaskDashBoardListData) intent.getSerializableExtra("JOBDATA");
            jobCode = taskListData.Job_Code;
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


        if (taskListData != null) {
            setData(taskListData);
        }

        /*if (NetworkUtil.isNetworkConnected(this)) {
            try {

                String url = API.BASE_URL + "customer_job_details.php";
                Log.e("job detail cus URL = ", url);

                CallJobDetails(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }*/

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
                Intent intent = new Intent(TaskDetailsActivity.this, ActivityTaskViewMoreCont.class);
                intent.putExtra("JOBDATA", taskListData);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData(TaskDashBoardListData jobData) {

        tv_job_code.setText(jobData.Job_Code);
        tv_task_id.setText(jobData.TASK_ID);
        tv_job_title.setText(jobData.Job_Title);
        tv_assign_date.setText(jobData.AssignDt);
        tv_job_deadLine.setText(jobData.JobStartDt + " to " + jobData.JobEndDt);
        //tv_job_status.setText(jobData.J);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
