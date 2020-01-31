package com.helpmewaka.ui.contractor.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.contractor.adapter.NotificationAdapter;
import com.helpmewaka.ui.contractor.model.NotificationData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityNotitfication extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private List<NotificationData> notificationList;
    private NotificationAdapter mAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private LinearLayout ll_no_record;

    private ImageView iv_back;
    private String url;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notitfication);
        Session session = new Session(this);
        user_id = session.getUser().user_id;
        initView();
        clickListner();
    }


    @SuppressLint("WrongConstant")
    private void initView() {
        notificationList = new ArrayList<>();


        iv_back = findViewById(R.id.iv_back);
        recycler_view = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_no_record = findViewById(R.id.ll_no_record);


        if (NetworkUtil.isNetworkConnected(this)) {
            try {
                url = API.BASE_URL + "customer_notification_history.php";
                callNotificationApi(url);

            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }

        mAdapter = new NotificationAdapter(notificationList, this);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManger);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);
    }

    private void clickListner() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityNotitfication.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

    }

    private void callNotificationApi(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                .addBodyParameter("CLT_ID", user_id)
                .setTag("notification")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("notification rep = ", "" + jsonObject);
                        try {
                            notificationList.clear();

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject job = jsonArray.getJSONObject(i);
                                    NotificationData payData = new NotificationData();
                                    payData.UNT_ID = job.getString("UNT_ID");
                                    payData.NotifyDesc = job.getString("NotifyDesc");
                                    payData.PostDt = job.getString("PostDt");
                                    payData.User = job.getString("User");
                                    payData.UID = job.getString("UID");

                                    notificationList.add(payData);
                                }
                            }

                            //check arraylist size
                            if (notificationList.size() == 0) {
                                swipeRefreshLayout.setVisibility(View.GONE);
                                ll_no_record.setVisibility(View.VISIBLE);
                            } else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                ll_no_record.setVisibility(View.GONE);
                            }

                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Utils.dismissDialog();
                            e.printStackTrace();
                            Log.e("payment error = ", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("payment error = ", "" + error);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ActivityNotitfication.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        notificationList.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
        callNotificationApi(url);
    }
}
