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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.adapter.ContracterLiveLocationAdapter;
import com.helpmewaka.ui.contractor.model.ContractorLiveLocation;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityRatingRevie_Contactor extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;
    private TaskDashBoardListData taskData;
    RatingBar rating;
    TextView tv_msg_rating,tv_time_rating;
    private Context context;
    String user_id,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_revie__contactor);

        context=ActivityRatingRevie_Contactor.this;
        Session session = new Session(context);
        user_id = session.getUser().user_id;

        iv_back = findViewById(R.id.iv_back);
        rating = findViewById(R.id.rating);
        tv_msg_rating = findViewById(R.id.tv_msg_rating);
        tv_time_rating = findViewById(R.id.tv_time_rating);


        iv_back.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            taskData = (TaskDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

       // http://logicalsofttech.com/helpmewaka/api/get_contractor_rating_reviews.php

        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                url = API.BASE_URL + "get_contractor_rating_reviews.php";
                GetLContractRating(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));


    }

    private void GetLContractRating(String url) {

        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("Task_Code", taskData.TASK_ID)
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response_rating", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                // contractorLiveLocations.clear();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");

                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String CNT_ID = jsonObject1.getString("CNT_ID");
                                    String JOB_ID = jsonObject1.getString("JOB_ID");
                                    String Task_Code = jsonObject1.getString("Task_Code");
                                    String TASK_ID = jsonObject1.getString("TASK_ID");
                                    String Rating_Comment = jsonObject1.getString("Rating_Comment");
                                    String Rating = jsonObject1.getString("Rating");
                                    String PostDt = jsonObject1.getString("PostDt");
                                    String Stat = jsonObject1.getString("Stat");


                                    try {
                                        tv_msg_rating.setText(Rating_Comment);
                                        tv_time_rating.setText(PostDt);
                                        rating.setRating(Float.parseFloat(Rating));
                                    }catch (Exception e){

                                    }


                                }


                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);
                            else {
                                Toast.makeText(ActivityRatingRevie_Contactor.this, "No details found", Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                intent = new Intent(ActivityRatingRevie_Contactor.this, TaskDetailsActivity.class);
                intent.putExtra("JOBDATA", taskData);
                startActivity(intent);
                finish();
                break;




    }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
       Intent intent = new Intent(ActivityRatingRevie_Contactor.this, TaskDetailsActivity.class);
        intent.putExtra("JOBDATA", taskData);
        startActivity(intent);
        finish();

    }
}
