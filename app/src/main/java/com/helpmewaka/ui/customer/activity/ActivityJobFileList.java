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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.customer.adapter.UploadFileListAdapter;
import com.helpmewaka.ui.model.FileListData;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.PathUtils;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ActivityJobFileList extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LinearLayout ll_img;
    private File imgFile;
    private ImageView iv_back;
    private JobDashBoardListData jobListData;
    private String user_id;
    private ArrayList<FileListData> fileList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycler_view;
    private LinearLayout ll_no_record;
    private UploadFileListAdapter mAdapter;
    private String file_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_file_list);
        Session session = new Session(this);
        user_id = session.getUser().user_id;

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            jobListData = (JobDashBoardListData) intent.getSerializableExtra("JOBDATA");
        }

        initView();
        clickListner();
    }


    private void initView() {
        fileList = new ArrayList<>();


        iv_back = findViewById(R.id.iv_back);
        ll_img = findViewById(R.id.ll_img);
        recycler_view = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_no_record = findViewById(R.id.ll_no_record);

        if (jobListData.Job_Stat.equalsIgnoreCase("Review Pending")) {
            ll_img.setVisibility(View.VISIBLE);
        } else ll_img.setVisibility(View.GONE);


        if (NetworkUtil.isNetworkConnected(ActivityJobFileList.this)) {
            try {
                file_url = API.BASE_URL + "get_customer_files.php";
                CallGetFile(file_url);
                //ToastClass.showToast(context, "post....");
            } catch (NullPointerException e) {
                ToastClass.showToast(ActivityJobFileList.this, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(ActivityJobFileList.this, getString(R.string.no_internet_access));
        }
    }

    private void clickListner() {
        iv_back.setOnClickListener(this);
        ll_img.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent(ActivityJobFileList.this, ActivityJobViewMoreCust.class);
                intent.putExtra("JOBDATA", jobListData);
                startActivity(intent);
                finish();
                break;

            case R.id.ll_img:
                imagePick();
                break;
        }
    }

    private void imagePick() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    //If you want the Uri.
                    //Mandatory to refresh image from Uri.
                    //getImageView().setImageURI(null);
                    //Setting the real returned image.
                    //getImageView().setImageURI(r.getUri());
                    //If you want the Bitmap.
                    //img_profile.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    imgFile = PathUtils.bitmapToFile(ActivityJobFileList.this, r.getBitmap());
                    Log.e("imgFile", "" + imgFile);
                    String filename = imgFile.getName();
                    Log.e("filweName = ", filename);


                    if (NetworkUtil.isNetworkConnected(ActivityJobFileList.this)) {
                        try {
                            String url = API.BASE_URL + "customer_upload_files.php";
                            CallUploadFile(url);
                            //ToastClass.showToast(context, "post....");
                        } catch (NullPointerException e) {
                            ToastClass.showToast(ActivityJobFileList.this, getString(R.string.too_slow));
                        }
                    } else {
                        ToastClass.showToast(ActivityJobFileList.this, getString(R.string.no_internet_access));
                    }

                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(ActivityJobFileList.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show(ActivityJobFileList.this);
    }


    private void CallUploadFile(String url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.upload(url)
                .addMultipartParameter("CLT_ID", user_id)
                .addMultipartParameter("JOB_ID", jobListData.JOB_ID)
                .addMultipartFile("jobfilephotoimg", imgFile)
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
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {

                                ToastClass.showToast(ActivityJobFileList.this, message);

                                if (NetworkUtil.isNetworkConnected(ActivityJobFileList.this)) {
                                    try {
                                        file_url = API.BASE_URL + "get_customer_files.php";
                                        CallGetFile(file_url);
                                        //ToastClass.showToast(context, "post....");
                                    } catch (NullPointerException e) {
                                        ToastClass.showToast(ActivityJobFileList.this, getString(R.string.too_slow));
                                    }
                                } else {
                                    ToastClass.showToast(ActivityJobFileList.this, getString(R.string.no_internet_access));
                                }

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityJobFileList.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp jobupdate", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error updatejob ", error.toString());
                        ToastClass.showToast(ActivityJobFileList.this, error.toString());
                    }
                });
    }

    private void CallGetFile(String file_url) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(file_url)
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("JOB_ID", jobListData.JOB_ID)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res getJob cus", "" + jsonObject);
                        try {
                            fileList.clear();
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject job = jsonArray.getJSONObject(i);
                                    FileListData jobData = new FileListData();
                                    jobData.CLT_ID = job.getString("CLT_ID");
                                    jobData.JOB_ID = job.getString("JOB_ID");
                                    jobData.Image = job.getString("Attachment");
                                    jobData.UploadDt = job.getString("UploadDt");
                                    jobData.Type = job.getString("Type");

                                    fileList.add(jobData);
                                }
                            }

                            //check arraylist size
                            if (fileList.size() == 0) {
                                swipeRefreshLayout.setVisibility(View.GONE);
                                ll_no_record.setVisibility(View.VISIBLE);
                            } else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                ll_no_record.setVisibility(View.GONE);
                            }


                            mAdapter = new UploadFileListAdapter(fileList, ActivityJobFileList.this);
                            RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(ActivityJobFileList.this);
                            recycler_view.setLayoutManager(mLayoutManger);
                            recycler_view.setLayoutManager(new LinearLayoutManager(ActivityJobFileList.this, LinearLayoutManager.VERTICAL, false));
                            recycler_view.setItemAnimator(new DefaultItemAnimator());
                            recycler_view.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            Log.e("exp jobget", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error jobjob ", error.toString());
                        ToastClass.showToast(ActivityJobFileList.this, error.toString());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ActivityJobFileList.this, ActivityJobViewMoreCust.class);
        intent.putExtra("JOBDATA", jobListData);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {
        fileList.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
        CallGetFile(file_url);
    }
}
