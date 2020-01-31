package com.helpmewaka.ui.contractor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.adapter.ContracterMsgAdapter;
import com.helpmewaka.ui.contractor.adapter.TaskDashboarAdapter;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ActivityMessage_Contactor extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private Context context;
    private String user_id;
    ContracterMsgAdapter mAdapter;
    private String url;
    ArrayList<MsgContractorModel>msgContractorModels=new ArrayList<>();

    Button btn_submit;
    EditText et_msg;
    private TaskDashBoardListData taskData;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message__contactor);
        context=ActivityMessage_Contactor.this;
        Session session = new Session(context);
        user_id = session.getUser().user_id;

        recycler_view = findViewById(R.id.recycler_view);
        swipeRefreshLayout =findViewById(R.id.swipeRefreshLayout);
        btn_submit =findViewById(R.id.btn_submit);
        et_msg =findViewById(R.id.et_msg);
        iv_back =findViewById(R.id.iv_back);

        swipeRefreshLayout.setOnRefreshListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            taskData = (TaskDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }


        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                url = API.BASE_URL + "contractor_job_msg_list.php";
                GetMsgList(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));


//********************button post
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_msg.getText().toString().isEmpty()){
                    if (NetworkUtil.isNetworkConnected(context)) {
                        try {
                           // contractor_job_message_post
                            String url = API.BASE_URL + "contractor_job_message_post.php";
                           // String url = API.BASE_URL + "job_quickMessage_post_contractor.php";
                            PostMsgContract(url,et_msg.getText().toString());
                        } catch (NullPointerException e) {
                            ToastClass.showToast(context, getString(R.string.too_slow));
                        }
                    } else ToastClass.showToast(context, getString(R.string.no_internet_access));

                }else {
                    Toast.makeText(ActivityMessage_Contactor.this, "Please Enter Message", Toast.LENGTH_SHORT).show();
                }



            }
        });



    }

    private void PostMsgContract(String url, String msg) {

        Utils.showDialog(context, "Please Wait...");

        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("MsgJOBID", taskData.JOB_ID)
                .addBodyParameter("MsgTaskID", taskData.TASK_ID)
                .addBodyParameter("job_messages", msg)
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response_msg_post ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                et_msg.setText("");
                                Toast.makeText(ActivityMessage_Contactor.this, "Send successfully", Toast.LENGTH_SHORT).show();
                                String url = API.BASE_URL + "contractor_job_msg_list.php";
                                GetMsgList(url);
                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);


                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();
                        Log.e("error_msg", error.getErrorDetail());
                    }
                });
    }

    private void GetMsgList(String url) {
        AndroidNetworking.post(url)
                .addBodyParameter("msgjobid", taskData.JOB_ID)
                .addBodyParameter("msgtaskid", taskData.TASK_ID)
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
                                msgContractorModels.clear();

                                JSONArray jsonArray=jsonObject.getJSONArray("data");
                                for (int i=0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String CNT_ID = jsonObject1.getString("CNT_ID");
                                    String JOB_ID = jsonObject1.getString("JOB_ID");
                                    String TASK_ID = jsonObject1.getString("TASK_ID");
                                    String MSG_Content = jsonObject1.getString("MSG_Content");
                                    String Attachment = jsonObject1.getString("Attachment");
                                    String CreateDt = jsonObject1.getString("CreateDt");
                                    String Sender = jsonObject1.getString("Sender");
                                    String usrFile = jsonObject1.getString("usrFile");
                                    String Type = jsonObject1.getString("Type");

                                    msgContractorModels.add(i, new MsgContractorModel(CNT_ID,JOB_ID,TASK_ID,MSG_Content,Attachment
                                    ,CreateDt,Sender,usrFile,Type));

                                }


                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);
                            else {
                                Toast.makeText(ActivityMessage_Contactor.this, "No message found", Toast.LENGTH_SHORT).show();
                            }

                            mAdapter = new ContracterMsgAdapter(msgContractorModels, context);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(context);
                            recycler_view.setLayoutManager(mLayoutManger);
                            recycler_view.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                            recycler_view.setItemAnimator(new DefaultItemAnimator());
                            recycler_view.setAdapter(mAdapter);
                            recycler_view.setFocusable(false);
                            mAdapter.notifyDataSetChanged();
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
    public void onRefresh() {
        msgContractorModels.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
        GetMsgList(url);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
