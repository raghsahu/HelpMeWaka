package com.helpmewaka.ui.contractor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.adapter.ContracterMsgAdapter;
import com.helpmewaka.ui.contractor.model.MsgContractorModel;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityContactorTimeFrame extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_assign_date,tv_date_from,tv_date_to;
    Context context;
    private String user_id;
    private TaskDashBoardListData taskData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactor_time_frame);
        context=ActivityContactorTimeFrame.this;
        Session session = new Session(context);
        user_id = session.getUser().user_id;

        iv_back=findViewById(R.id.iv_back);
        tv_assign_date=findViewById(R.id.tv_assign_date);
        tv_date_from=findViewById(R.id.tv_date_from);
        tv_date_to=findViewById(R.id.tv_date_to);

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

        if (NetworkUtil.isNetworkConnected(context)) {
            try {
               String url = API.BASE_URL + "contractor_AssignJob_time_frame.php";
                GetTimeFrame(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));




    }

    private void GetTimeFrame(String url) {
        Utils.showDialog(context, "Please Wait...");
        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("taskcode", taskData.TASK_ID)
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response_msg_get ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {


                                JSONObject jsonObject1=jsonObject.getJSONObject("data");

                                    String CNT_ID = jsonObject1.getString("CNT_ID");
                                    String Task_Code = jsonObject1.getString("Task_Code");
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
