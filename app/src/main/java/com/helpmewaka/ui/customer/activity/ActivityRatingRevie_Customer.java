package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.activity.ActivityRatingRevie_Contactor;
import com.helpmewaka.ui.contractor.activity.TaskDetailsActivity;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.helpmewaka.ui.server.API.BASE_URL;

public class ActivityRatingRevie_Customer extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;
    private JobDashBoardListData jobListData;
    RatingBar rating,rating_give;
    TextView tv_msg_rating,tv_time_rating;
    private Context context;
    String user_id,url,Rating_Point;
    CardView card_post_rating,card_show_rating;
    EditText et_comments;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_revie__customer);


        context= ActivityRatingRevie_Customer.this;
        Session session = new Session(context);
        user_id = session.getUser().user_id;

        iv_back = findViewById(R.id.iv_back);
        rating = findViewById(R.id.rating);
        tv_msg_rating = findViewById(R.id.tv_msg_rating);
        tv_time_rating = findViewById(R.id.tv_time_rating);
        card_post_rating = findViewById(R.id.card_post_rating);
        card_show_rating = findViewById(R.id.card_show_rating);
        rating_give = findViewById(R.id.rating_give);
        et_comments = findViewById(R.id.et_comments);
        btn_submit = findViewById(R.id.btn_submit);

        iv_back.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        // http://logicalsofttech.com/helpmewaka/api/get_contractor_rating_reviews.php

        if (NetworkUtil.isNetworkConnected(context)) {
            try {
              //  http://logicalsofttech.com/helpmewaka/api/get_customer_rating_review.php
                url = BASE_URL + "get_customer_rating_review.php";
                GetLCustomerRating(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));



        rating_give.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                Float ratingVal = (Float) rating;
                Float ratingvalue = (Float) rating_give.getRating();
               // Toast.makeText(getApplicationContext(), " Ratings : " + String.valueOf(ratingVal) + "", Toast.LENGTH_SHORT).show();
              //  Toast.makeText(getApplicationContext(), " Ratings1 : " + ratingvalue + "", Toast.LENGTH_SHORT).show();
                Rating_Point=ratingvalue.toString();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Et_coment=et_comments.getText().toString();
                String rating = String.valueOf(rating_give.getRating());

                if (!Et_coment.isEmpty() && !rating.isEmpty()){

                  //  http://logicalsofttech.com/helpmewaka/api/customer_post_job_rating.php
                    String url=BASE_URL + "customer_post_job_rating.php";
                  PostRating(url,Et_coment,rating);

                }else {
                    Toast.makeText(context, "Please enter comment & rating", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void PostRating(String url, String et_coment, String rating_point) {
        AndroidNetworking.post(url)
                .addBodyParameter("ratings", rating_point)
                .addBodyParameter("RatingTaskID", jobListData.Task_Code)
                .addBodyParameter("RatingJOBID", jobListData.JOB_ID)
                .addBodyParameter("rating_comment", et_coment)
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
//
                                JSONArray jsonArray=jsonObject.getJSONArray("data");
//
//                                for (int i=0; i<jsonArray.length();i++){
//                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
//                                    String CLT_ID = jsonObject1.getString("CLT_ID");
//                                    String JOB_ID = jsonObject1.getString("JOB_ID");
//                                    String Job_Code = jsonObject1.getString("Job_Code");
//                                    String TASK_ID = jsonObject1.getString("TASK_ID");
//                                    String Rating_Comment = jsonObject1.getString("Rating_Comment");
//                                    String Rating = jsonObject1.getString("Rating");
//                                    String PostDt = jsonObject1.getString("PostDt");
//                                    String Stat = jsonObject1.getString("Stat");
//
//
//                                    try {
//                                        tv_msg_rating.setText(Rating_Comment);
//                                        tv_time_rating.setText(PostDt);
//                                        rating.setRating(Float.parseFloat(Rating));
//                                    }catch (Exception e){
//
//                                    }
//
//
//                                }
                                try {
                                    if (jsonArray.length()>0){
                                        card_post_rating.setVisibility(View.GONE);
                                        card_show_rating.setVisibility(View.VISIBLE);
                                    }else
                                    {
                                        card_post_rating.setVisibility(View.VISIBLE);
                                        card_show_rating.setVisibility(View.GONE);
                                    }
                                }catch (Exception e){

                                }



                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);
                            else {
                                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();
                        Log.e("error_rating", error.getErrorDetail());
                    }
                });

    }

    private void GetLCustomerRating(String url) {
        AndroidNetworking.post(url)
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("Job_Code", jobListData.Job_Code)
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
                                    String CLT_ID = jsonObject1.getString("CLT_ID");
                                    String JOB_ID = jsonObject1.getString("JOB_ID");
                                    String Job_Code = jsonObject1.getString("Job_Code");
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

                                try {
                                    if (jsonArray.length()>0){
                                        card_post_rating.setVisibility(View.GONE);
                                        card_show_rating.setVisibility(View.VISIBLE);
                                    }else
                                    {
                                        card_post_rating.setVisibility(View.VISIBLE);
                                        card_show_rating.setVisibility(View.GONE);
                                    }
                                }catch (Exception e){

                                }



                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);
                            else {
                                Toast.makeText(context, "No details found", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();
                        Log.e("error_get_rating", error.getErrorDetail());
                    }
                });


    }

    @Override
    public void onClick(View v) {

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(context, TaskDetailsActivity.class);
//        intent.putExtra("JOBDATA", jobListData);
//        startActivity(intent);
//        finish();

    }
}
