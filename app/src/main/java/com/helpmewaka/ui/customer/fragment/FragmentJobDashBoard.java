package com.helpmewaka.ui.customer.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helpmewaka.R;
import com.helpmewaka.ui.model.JobDashBoardListData;
import com.helpmewaka.ui.customer.adapter.JobDashboarAdapter;
import com.helpmewaka.ui.model.ServiceListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentJobDashBoard extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recycler_view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout ll_no_record;
    private JobDashboarAdapter mAdapter;
    private Context context;
    private List<JobDashBoardListData> jobList;
    private String user_id;
    private String url;


    public FragmentJobDashBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_job_dashboard, container, false);
        Session session = new Session(context);
        user_id = session.getUser().user_id;
        initView();
        Utils.hideSoftKeyboardfragment(context, view);
        return view;
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        jobList = new ArrayList<>();
        recycler_view = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        ll_no_record = view.findViewById(R.id.ll_no_record);


        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new JobDashboarAdapter(jobList, context);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(mLayoutManger);
        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);


        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                url = API.BASE_URL + "customer_job_list.php";
                GetJobList(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));
    }

    private void GetJobList(String url) {

        Utils.showDialog(getContext(), "Loading Please Wait...");

        AndroidNetworking.post(url)
                .addBodyParameter("CLT_ID", user_id)
                .setTag("List")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("resp joblistContract ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");
                            jobList.clear();

                            if (result.equalsIgnoreCase("true")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<JobDashBoardListData>>() {
                                }.getType();
                                //json array
                                List<JobDashBoardListData> data = gson.fromJson(jsonObject.getString("data"), listType);
                                //List<ProductData> data = (List<ProductData>) gson.fromJson(jsonObject.getString("Product_list").toString(), Products.class);
                                jobList.addAll(data);

                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);


                            if (jobList.size() == 0) {
                                swipeRefreshLayout.setVisibility(View.GONE);
                                ll_no_record.setVisibility(View.VISIBLE);
                            } else {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                ll_no_record.setVisibility(View.GONE);
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();
                        Log.e("error joblistContract", error.getErrorDetail());
                    }
                });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onRefresh() {

        jobList.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
        GetJobList(url);

    }
}
