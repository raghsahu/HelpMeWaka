package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.customer.adapter.PaymentAdapter;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.model.PaymentData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.helpmewaka.ui.customer.adapter.PaymentAdapter.Multi_milestome;

public class ActivityPayment extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private LinearLayout ll_no_record,ll_pay_total;
    private List<PaymentData> paymentList;
    private ImageView iv_back;
    private String url;
    private PaymentAdapter mAdapter;
    private JobDashBoardListData jobListData;
    TextView tv_pay_total;
    private String unpaid_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        intView();
        clickListner();
    }


    @SuppressLint("WrongConstant")
    private void intView() {
        paymentList = new ArrayList<>();

        recycler_view = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_no_record = findViewById(R.id.ll_no_record);
        ll_pay_total = findViewById(R.id.ll_pay_total);
        tv_pay_total = findViewById(R.id.tv_pay_total);

        iv_back = findViewById(R.id.iv_back);


        if (NetworkUtil.isNetworkConnected(this)) {
            try {
                url = API.BASE_URL + "customer_job_payment_history.php";
                PaymentHistoryApi(url);

            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }

        ll_pay_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityPayment.this, ActivityPaymentPayNow.class);
                intent.putExtra("Payment_amt",unpaid_total);
                intent.putExtra("JRT_ID",Multi_milestome);
               startActivity(intent);
            }
        });


    }

    private void clickListner() {

        iv_back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void PaymentHistoryApi(String url) {

        Log.e("JOB_ID_payment", "" + jobListData.JOB_ID);
        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                .addBodyParameter("JOB_ID", jobListData.JOB_ID)
                .setTag("Skills")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("payment rep = ", "" + jsonObject);
                        try {
                            paymentList.clear();

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");


                            if (result.equalsIgnoreCase("true")) {
                                String Job_Title = jsonObject.getString("Job_Title");
                                unpaid_total = jsonObject.getString("upaid_total");

                                tv_pay_total.setText("Unpaid total "+unpaid_total);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject job = jsonArray.getJSONObject(i);
                                    PaymentData payData = new PaymentData();
                                    payData.JOB_ID = job.getString("JOB_ID");
                                    payData.MILESTONE = job.getString("MILESTONE");
                                    payData.TransactionID = job.getString("TransactionID");
                                    payData.InvoiceID = job.getString("InvoiceID");
                                    payData.paymentMode = job.getString("paymentMode");
                                    payData.paymentDt = job.getString("paymentDt");
                                    payData.Amount = job.getString("Amount");
                                    payData.payment_status = job.getString("payment_status");
                                    payData.JRT_ID = job.getString("JRT_ID");

                                    paymentList.add(payData);

                                }
                            }

                            //check arraylist size
                            if (paymentList.size() == 0) {
                                swipeRefreshLayout.setVisibility(View.GONE);
                                ll_pay_total.setVisibility(View.GONE);
                                ll_no_record.setVisibility(View.VISIBLE);
                            } else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                ll_pay_total.setVisibility(View.VISIBLE);
                                ll_no_record.setVisibility(View.GONE);
                            }

                            mAdapter = new PaymentAdapter(paymentList, ActivityPayment.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityPayment.this);
                            recycler_view.setLayoutManager(mLayoutManger);
                            recycler_view.setLayoutManager(new LinearLayoutManager(ActivityPayment.this, LinearLayoutManager.VERTICAL, false));
                            recycler_view.setItemAnimator(new DefaultItemAnimator());
                            recycler_view.setAdapter(mAdapter);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(ActivityPayment.this, ActivityJobViewMoreCust.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        paymentList.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
        PaymentHistoryApi(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityPayment.this, ActivityJobViewMoreCust.class);
        intent.putExtra("JOBDATA", jobListData);
        startActivity(intent);
        finish();
    }
}
