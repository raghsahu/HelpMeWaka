package com.helpmewaka.ui.contractor.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.contractor.adapter.TaskDashboarAdapter;
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
public class FragmentTaskDashBoard extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recycler_view;
    private TaskDashboarAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout ll_no_record;
    private Context context;
    private List<TaskDashBoardListData> taskList;
    private String servie_id;
    private String url;
    private String user_id;


    public FragmentTaskDashBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_dashboard, container, false);
        Session session = new Session(context);
        user_id = session.getUser().user_id;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            servie_id = bundle.getString("SERVICE_ID");
            Log.e("bundle servic_id ", servie_id);
        }
        initView();
        return view;
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        taskList = new ArrayList<>();
        recycler_view = view.findViewById(R.id.recycler_view);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        ll_no_record = view.findViewById(R.id.ll_no_record);


        swipeRefreshLayout.setOnRefreshListener(this);


        mAdapter = new TaskDashboarAdapter(taskList, context);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(context);
        recycler_view.setLayoutManager(mLayoutManger);
        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);


        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                url = API.BASE_URL + "contractor_dashboard.php";
                GetTaskList(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));

    }

    private void GetTaskList(String url) {

        Utils.showDialog(getContext(), "Loading Please Wait...");

        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .setTag("List")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response taskList ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");
                            taskList.clear();

                            if (result.equalsIgnoreCase("true")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TaskDashBoardListData>>() {
                                }.getType();
                                //json array
                                List<TaskDashBoardListData> data = gson.fromJson(jsonObject.getString("data"), listType);
                                //List<ProductData> data = (List<ProductData>) gson.fromJson(jsonObject.getString("Product_list").toString(), Products.class);
                                taskList.addAll(data);

                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);


                            if (taskList.size() == 0) {
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
                        Log.e("error", error.getErrorDetail());
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
        taskList.clear();
        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);
        GetTaskList(url);
    }
}
