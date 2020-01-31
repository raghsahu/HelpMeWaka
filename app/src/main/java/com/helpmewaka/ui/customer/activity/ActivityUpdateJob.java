package com.helpmewaka.ui.customer.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.model.JobData;
import com.helpmewaka.ui.model.StateData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ActivityUpdateJob extends AppCompatActivity {
    private String state_id;
    private ArrayList<StateData> stateList;
    private Spinner spinner_state;
    private JobDashBoardListData jobListData;
   // private TaskDashBoardListData jobListData;
    private Session session;
    private String user_id;
    private EditText et_additional_info, et_contact_person, et_contact_no, et_spl_reqest, et_city;
    private Button btn_update;
    private JobData jobData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job);
        session = new Session(this);
        user_id = session.getUser().user_id;

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        initView();
        clickListner();
    }


    private void initView() {
        stateList = new ArrayList<>();

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityUpdateJob.this, ActivityJobViewMoreCust.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
            }
        });

        spinner_state = findViewById(R.id.spinner_state);
        et_additional_info = findViewById(R.id.et_additional_info);
        et_contact_person = findViewById(R.id.et_contact_person);
        et_contact_no = findViewById(R.id.et_contact_no);
        et_spl_reqest = findViewById(R.id.et_spl_reqest);
        et_city = findViewById(R.id.et_city);
        btn_update = findViewById(R.id.btn_update);


        if (NetworkUtil.isNetworkConnected(this)) {
            try {
                String url_get = API.BASE_URL + "get_customer_job_detail.php";
                CallGetJob(url_get);
            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(this, getString(R.string.no_internet_access));


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_id = stateList.get(position).STATE_ID;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void clickListner() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cheValidation();
            }
        });
    }

    private void cheValidation() {

        String city = et_city.getText().toString();

        Validation validation = new Validation(this);

        if (spinner_state != null) {
            if (!state_id.isEmpty()){


            if (spinner_state.getSelectedItem().toString().trim().equals("SELECT STATE")) {
                ToastClass.showToast(this, getString(R.string.spinner_state_v));
            } else if (!validation.isEmpty(city)) {
                ToastClass.showToast(this, getString(R.string.city_v));
                et_city.requestFocus();

            } else {

                if (NetworkUtil.isNetworkConnected(this)) {
                    try {
                        String url = API.BASE_URL + "customer_job_update.php";
                        CallUpdateJob(url);
                        //ToastClass.showToast(context, "post....");
                    } catch (NullPointerException e) {
                        ToastClass.showToast(this, getString(R.string.too_slow));
                    }
                } else {
                    ToastClass.showToast(this, getString(R.string.no_internet_access));
                }
            }
         }
        } else ToastClass.showToast(this, getString(R.string.spinner_state_v));
    }


    private void CallGetJob(String url_get) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url_get)
                //.addBodyParameter("CLT_ID", )
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("jobcode", jobListData.Job_Code)
                .setTag("get job")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res getJob cus", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                JSONObject userdetail = jsonObject.getJSONObject("data");
                                jobData = new JobData();
                                jobData.CLT_ID = userdetail.getString("CLT_ID");
                                jobData.JOB_ID = userdetail.getString("JOB_ID");
                                jobData.Job_Code = userdetail.getString("Job_Code");
                                jobData.Job_Title = userdetail.getString("Job_Title");
                                jobData.Service_Country = userdetail.getString("Service_Country");
                                jobData.Service_State = userdetail.getString("Service_State");
                                jobData.Service_City = userdetail.getString("Service_City");
                                jobData.Special_Request = userdetail.getString("Special_Request");
                                jobData.additional_info = userdetail.getString("additional_info");
                                jobData.Contact_Person = userdetail.getString("Contact_Name");
                                jobData.Contact_Number = userdetail.getString("Contact_Number");


                                //ToastClass.showToast(ActivityUpdateJob.this, message);


                                if (NetworkUtil.isNetworkConnected(ActivityUpdateJob.this)) {
                                    try {
                                        String url = API.BASE_URL + "state_list.php";
                                        getStateApi(url);
                                    } catch (NullPointerException e) {
                                        ToastClass.showToast(ActivityUpdateJob.this, getString(R.string.too_slow));
                                    }
                                } else
                                    ToastClass.showToast(ActivityUpdateJob.this, getString(R.string.no_internet_access));


                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityUpdateJob.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp jodupdate", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error updatejob ", error.toString());
                        ToastClass.showToast(ActivityUpdateJob.this, error.toString());
                    }
                });
    }

    private void getStateApi(String url) {

        Utils.showDialog(this, "Loading Please Wait...");

        AndroidNetworking.get(url)
                .setTag("List")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response stateList ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");
                            stateList.clear();
                            stateList.add(new StateData("SELECT STATE"));
                            if (result.equalsIgnoreCase("true")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<StateData>>() {
                                }.getType();
                                //json array
                                List<StateData> data = gson.fromJson(jsonObject.getString("data"), listType);
                                //List<ProductData> data = (List<ProductData>) gson.fromJson(jsonObject.getString("Product_list").toString(), Products.class);
                                stateList.addAll(data);

                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);


                            ArrayAdapter<StateData> aa = new ArrayAdapter<StateData>(ActivityUpdateJob.this, R.layout.list_item_spinner1, stateList) {
                                @NonNull
                                @Override
                                //set hint text by default in center
                                public View getView(int position, @Nullable View cView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, cView, parent);
                                    view.setPadding(0, view.getTop(), view.getRight(), view.getBottom());
                                    return view;
                                }
                            };
                            aa.setDropDownViewResource(R.layout.list_item_spinner1);
                            //Setting the ArrayAdapter data on the Spinner
                            spinner_state.setAdapter(aa);

                            setData(jobData);

                            //set select category
                            /*for (int i = 0; i < stateList.size(); i++) {
                                if (stateList.get(i).STATE_ID.equalsIgnoreCase(state_id)) {
                                    spinner_state.setSelection(i);
                                }
                            }*/
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

    private void setData(JobData jobData) {

        if (jobData != null) {

            //set select category
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).STATE_ID.equalsIgnoreCase(jobData.Service_State)) {
                    spinner_state.setSelection(i);
                }
            }

            et_additional_info.setText(jobData.additional_info);
            et_contact_person.setText(jobData.Contact_Person);
            et_contact_no.setText(jobData.Contact_Number);
            et_spl_reqest.setText(jobData.Special_Request);
            et_city.setText(jobData.Service_City);
        }
    }

    private void CallUpdateJob(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)
                //.addBodyParameter("CLT_ID", )
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("JOBID", jobListData.JOB_ID)
                .addBodyParameter("additional_info", et_additional_info.getText().toString())
                .addBodyParameter("contact_name", et_contact_person.getText().toString())
                .addBodyParameter("contact_number", et_contact_no.getText().toString())
                .addBodyParameter("special_request", et_spl_reqest.getText().toString())
                .addBodyParameter("service_state", state_id)
                .addBodyParameter("service_city", et_city.getText().toString())
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res UpdateJob cus", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {

                                ToastClass.showToast(ActivityUpdateJob.this, message);

                                onBackPressed();

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityUpdateJob.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp jobupdate", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error updatejob ", error.toString());
                        ToastClass.showToast(ActivityUpdateJob.this, error.toString());
                    }
                });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ActivityUpdateJob.this, ActivityJobViewMoreCust.class);
        intent.putExtra("JOBDATA", jobListData);
        startActivity(intent);
        finish();
    }
}
