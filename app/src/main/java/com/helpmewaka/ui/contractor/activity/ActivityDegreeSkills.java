package com.helpmewaka.ui.contractor.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.contractor.adapter.DegreeListAdapter;
import com.helpmewaka.ui.contractor.adapter.SkillsListAdapter;
import com.helpmewaka.ui.contractor.model.DegreeListData;
import com.helpmewaka.ui.contractor.model.SkillsListData;
import com.helpmewaka.ui.model.UserInfoData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityDegreeSkills extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private TextView tv_degree, tv_skill;
    private ImageView iv_degree, iv_skill;
    private Dialog dialog;
    private EditText et_degreeName, et_degreeYr;
    private EditText et_skills;
    private Button btn_submit_degree, btn_submit_skill;
    private Session session;
    private String user_id;
    private ImageView iv_back;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private LinearLayout ll_no_record;
    private List<DegreeListData> degreeList;
    private List<SkillsListData> skillsList;
    private DegreeListAdapter mAdapter;
    private SkillsListAdapter sAdapter;
    private String degree_url;
    private String skill_url;
    private String referece = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degree_skills);

        session = new Session(this);
        user_id = session.getUser().user_id;

        intView();
        clickListner();
    }


    @SuppressLint("WrongConstant")
    private void intView() {
        degreeList = new ArrayList<>();
        skillsList = new ArrayList<>();

        recycler_view = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_no_record = findViewById(R.id.ll_no_record);

        iv_degree = findViewById(R.id.iv_degree);
        iv_skill = findViewById(R.id.iv_skill);
        tv_degree = findViewById(R.id.tv_degree);
        tv_skill = findViewById(R.id.tv_skill);
        iv_back = findViewById(R.id.iv_back);


        if (NetworkUtil.isNetworkConnected(this)) {
            try {
                degree_url = API.BASE_URL + "contractor_degree_list.php";
                DegreeListApi(degree_url);

            } catch (NullPointerException e) {
                ToastClass.showToast(this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(this, getString(R.string.no_internet_access));
        }


    }

    private void clickListner() {
        iv_degree.setOnClickListener(this);
        iv_skill.setOnClickListener(this);
        tv_degree.setOnClickListener(this);
        tv_skill.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_degree:
                referece = "1";

                iv_degree.setVisibility(View.VISIBLE);
                iv_skill.setVisibility(View.GONE);
                tv_degree.setTextColor(Color.parseColor("#FFFFFF"));
                tv_skill.setTextColor(Color.parseColor("#ed1256"));
                tv_degree.setBackgroundResource(R.color.colorAccent);
                //tv_degree.setBackgroundResource(R.drawable.squere_corner_border_bg);
                tv_skill.setBackgroundResource(R.drawable.squere_border_red);


                if (NetworkUtil.isNetworkConnected(this)) {
                    try {
                        //degree_url = API.BASE_URL + "contractor_degree_list.php";
                        DegreeListApi(degree_url);

                    } catch (NullPointerException e) {
                        ToastClass.showToast(this, getString(R.string.too_slow));
                    }
                } else {
                    ToastClass.showToast(this, getString(R.string.no_internet_access));
                }


                break;

            case R.id.tv_skill:
                referece = "2";

                iv_degree.setVisibility(View.GONE);
                iv_skill.setVisibility(View.VISIBLE);
                tv_skill.setTextColor(Color.parseColor("#FFFFFF"));
                tv_degree.setTextColor(Color.parseColor("#ed1256"));
                tv_skill.setBackgroundResource(R.color.colorAccent);
                // tv_skill.setBackgroundResource(R.drawable.squere_corner_border_bg);
                tv_degree.setBackgroundResource(R.drawable.squere_border_red);


                if (NetworkUtil.isNetworkConnected(this)) {
                    try {
                        skill_url = API.BASE_URL + "contractor_skill_list.php";
                        SkillsListApi(skill_url);

                    } catch (NullPointerException e) {
                        ToastClass.showToast(this, getString(R.string.too_slow));
                    }
                } else {
                    ToastClass.showToast(this, getString(R.string.no_internet_access));
                }
                break;

            case R.id.iv_degree:
                openDegreeDialog();
                break;
            case R.id.iv_skill:
                Utils.hideSoftKeyboard(view);
                openSkillsDialog();
                break;

            case R.id.btn_submit_degree:
                Utils.hideSoftKeyboard(view);
                checkValidation();
                break;

            case R.id.btn_submit_skill:
                callSkillsApi();
                break;
        }
    }

    private void openDegreeDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_degree);
        et_degreeName = dialog.findViewById(R.id.et_degreeName);
        et_degreeYr = dialog.findViewById(R.id.et_degreeYr);
        btn_submit_degree = dialog.findViewById(R.id.btn_submit_degree);

        btn_submit_degree.setOnClickListener(this);

        dialog.show();
    }

    private void checkValidation() {

        String name = et_degreeName.getText().toString();
        String yr = et_degreeYr.getText().toString();

        Validation validation = new Validation(this);

        if (!validation.isEmpty(name)) {
            ToastClass.showToast(this, getString(R.string.degree_null));
            et_degreeName.requestFocus();

        } else if (!validation.isValidPassword(yr)) {
            ToastClass.showToast(this, getString(R.string.year_null));
            et_degreeYr.requestFocus();
        } else {

            if (NetworkUtil.isNetworkConnected(this)) {
                try {
                    String url = API.BASE_URL + "add_contractor_degree.php";
                    AddDegreeApi(url);
                } catch (NullPointerException e) {
                    ToastClass.showToast(this, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(this, getString(R.string.no_internet_access));
            }
        }

    }


    private void AddDegreeApi(String url) {

        Utils.showDialog(this, "Loading Please Wait...");

        AndroidNetworking.post(url)

                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("degree_name", et_degreeName.getText().toString())
                .addBodyParameter("year_pass", et_degreeYr.getText().toString())
                .setTag("user")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("res adddegree contrct=", "" + jsonObject);
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String results = jsonObject.getString("result");

                            if (results.equalsIgnoreCase("true")) {
                                dialog.dismiss();

                                DegreeListApi(degree_url);
                                ToastClass.showToast(ActivityDegreeSkills.this, message);
                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityDegreeSkills.this, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error Degree", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error Degree", "" + error);
                    }
                });
    }

    private void openSkillsDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_skills);
        et_skills = dialog.findViewById(R.id.et_skills);
        btn_submit_skill = dialog.findViewById(R.id.btn_submit_skill);

        btn_submit_skill.setOnClickListener(this);

        dialog.show();
    }

    private void callSkillsApi() {

        String skills = et_skills.getText().toString();

        Validation validation = new Validation(this);

        if (!validation.isEmpty(skills)) {
            ToastClass.showToast(this, getString(R.string.skills_null));
            et_skills.requestFocus();

        } else {

            if (NetworkUtil.isNetworkConnected(this)) {
                try {
                    String url = API.BASE_URL + "add_contractor_skills.php";
                    AddSkillsApi(url);
                } catch (NullPointerException e) {
                    ToastClass.showToast(this, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(this, getString(R.string.no_internet_access));
            }
        }
    }

    private void AddSkillsApi(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(url)

                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("skill", et_skills.getText().toString())
                .setTag("user")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("res skills contract=", "" + jsonObject);
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String results = jsonObject.getString("result");

                            if (results.equalsIgnoreCase("true")) {
                                dialog.dismiss();
                                SkillsListApi(skill_url);
                                ToastClass.showToast(ActivityDegreeSkills.this, message);
                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityDegreeSkills.this, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error Degree", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error Degree", "" + error);
                    }
                });
    }

    private void DegreeListApi(String degree_url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(degree_url)
                .addBodyParameter("CNT_ID", user_id)
                .setTag("degreelist")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("Degree rep = ", "" + jsonObject);
                        try {
                            if (skillsList.size() > 0) {
                                skillsList.clear();
                            }
                            degreeList.clear();
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject job = jsonArray.getJSONObject(i);
                                    DegreeListData jobData = new DegreeListData();
                                    jobData.DEG_ID = job.getString("DEG_ID");
                                    jobData.Degree_Name = job.getString("Degree_Name");
                                    jobData.Year_Of_Pass = job.getString("Year_Of_Pass");

                                    degreeList.add(jobData);
                                }
                            }

                            //check arraylist size
                            if (degreeList.size() == 0) {
                                swipeRefreshLayout.setVisibility(View.GONE);
                                ll_no_record.setVisibility(View.VISIBLE);
                            } else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                ll_no_record.setVisibility(View.GONE);
                            }


                            mAdapter = new DegreeListAdapter(degreeList, ActivityDegreeSkills.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityDegreeSkills.this);
                            recycler_view.setLayoutManager(mLayoutManger);
                            recycler_view.setLayoutManager(new LinearLayoutManager(ActivityDegreeSkills.this, LinearLayoutManager.VERTICAL, false));
                            recycler_view.setItemAnimator(new DefaultItemAnimator());
                            recycler_view.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.e("error = ", "" + error);
                    }
                });

    }

    private void SkillsListApi(String skill_url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(skill_url)
                .addBodyParameter("CNT_ID", user_id)
                .setTag("Skills")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("Skills rep = ", "" + jsonObject);
                        try {
                            if (degreeList.size() > 0) {
                                degreeList.clear();
                            }
                            skillsList.clear();

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject job = jsonArray.getJSONObject(i);
                                    SkillsListData skillData = new SkillsListData();
                                    skillData.SKL_ID = job.getString("SKL_ID");
                                    skillData.Skills = job.getString("Skills");

                                    skillsList.add(skillData);
                                }
                            }

                            //check arraylist size
                            if (skillsList.size() == 0) {
                                swipeRefreshLayout.setVisibility(View.GONE);
                                ll_no_record.setVisibility(View.VISIBLE);
                            } else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                ll_no_record.setVisibility(View.GONE);
                            }


                            sAdapter = new SkillsListAdapter(skillsList, ActivityDegreeSkills.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityDegreeSkills.this);
                            recycler_view.setLayoutManager(mLayoutManger);
                            recycler_view.setLayoutManager(new LinearLayoutManager(ActivityDegreeSkills.this, LinearLayoutManager.VERTICAL, false));
                            recycler_view.setItemAnimator(new DefaultItemAnimator());
                            recycler_view.setAdapter(sAdapter);
                            sAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.e("error = ", "" + error);
                    }
                });

    }

    @Override
    public void onRefresh() {

        if (referece.equalsIgnoreCase("1")) {
            degreeList.clear();
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
            DegreeListApi(degree_url);

        } else if (referece.equalsIgnoreCase("2")) {
            swipeRefreshLayout.setRefreshing(false);
            SkillsListApi(skill_url);
        }

    }

}
