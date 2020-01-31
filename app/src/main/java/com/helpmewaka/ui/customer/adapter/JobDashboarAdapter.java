package com.helpmewaka.ui.customer.adapter;

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
import com.helpmewaka.ui.customer.activity.ActivityJobDetails;
import com.helpmewaka.ui.model.JobDashBoardListData;

import java.util.List;

/**
 * Created by Ravindra Birla on 17/09/2019.
 */
public class JobDashboarAdapter extends RecyclerView.Adapter<JobDashboarAdapter.ViewHolder> {
    private List<JobDashBoardListData> jobList;
    private JobDashBoardListData jobListData;
    private Context context;

    public JobDashboarAdapter(List<JobDashBoardListData> jobList, Context context) {
        this.jobList = jobList;
        this.context = context;
    }

    @NonNull
    @Override
    public JobDashboarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_jobdashboard, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobDashboarAdapter.ViewHolder holder, int position) {
        if (jobList.size() > 0) {
            jobListData = jobList.get(position);

            holder.tv_job_title.setText(jobListData.Job_Title);
            //holder.tv_job_start.setText(jobListData.JobStrtDtApprox);
            holder.tv_job_code.setText(jobListData.Job_Code);

            /*if (!jobListData.image.equalsIgnoreCase(null)){

                Picasso.with(context).load(jobListData.image).fit().centerCrop()
                        .placeholder(R.drawable.doctor)
                        .error(R.drawable.doctor)
                        .into(holder.img_profile);
            }*/

        }
    }

    @Override
    public int getItemCount() {
        return jobList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_job_code, tv_job_title;
        LinearLayout ll_item;

        public ViewHolder(View parent) {
            super(parent);
            tv_job_code = parent.findViewById(R.id.tv_job_code);
            tv_job_title = parent.findViewById(R.id.tv_job_title);
            //tv_job_start = parent.findViewById(R.id.tv_job_start);
            ll_item = parent.findViewById(R.id.ll_item);

            ll_item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_item:
                    jobListData = jobList.get(getAdapterPosition());
                    Intent intent = new Intent(context, ActivityJobDetails.class);
                    intent.putExtra("JOBDATA", jobListData);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
