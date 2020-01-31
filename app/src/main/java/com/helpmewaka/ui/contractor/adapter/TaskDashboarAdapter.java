package com.helpmewaka.ui.contractor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.activity.TaskDetailsActivity;
import com.helpmewaka.ui.contractor.model.TaskDashBoardListData;
import com.helpmewaka.ui.customer.activity.ActivityJobDetails;

import java.util.List;

/**
 * Created by Ravindra Birla on 17/09/2019.
 */
public class TaskDashboarAdapter extends RecyclerView.Adapter<TaskDashboarAdapter.ViewHolder> {
    private List<TaskDashBoardListData> taskList;
    private TaskDashBoardListData taskData;
    private Context context;

    public TaskDashboarAdapter(List<TaskDashBoardListData> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskDashboarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_taskdashboard, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDashboarAdapter.ViewHolder holder, int position) {
        if (taskList.size() > 0) {
            taskData = taskList.get(position);
            holder.tv_job_title.setText(taskData.Job_Title);
            holder.tv_job_code.setText(taskData.Job_Code);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_job_code, tv_job_title;
        LinearLayout ll_item;


        public ViewHolder(View parent) {
            super(parent);

            tv_job_code = parent.findViewById(R.id.tv_job_code);
            tv_job_title = parent.findViewById(R.id.tv_job_title);
            ll_item = parent.findViewById(R.id.ll_item);

            ll_item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_item:
                    taskData = taskList.get(getAdapterPosition());
                    Intent intent = new Intent(context, TaskDetailsActivity.class);
                    intent.putExtra("JOBDATA", taskData);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
