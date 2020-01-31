package com.helpmewaka.ui.contractor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.Milestome_model;
import com.helpmewaka.ui.server.Session;

import java.util.ArrayList;

public class ContractorPaymentMilestoneAdapter extends RecyclerView.Adapter<ContractorPaymentMilestoneAdapter.ViewHolder> {
    private ArrayList<Milestome_model> msgContractorModels;
    private Context context;
    private Session session;
    private String user_id;

    public ContractorPaymentMilestoneAdapter(ArrayList<Milestome_model> msgContractorModelList, Context context) {
        this.msgContractorModels = msgContractorModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.milestone_adapter, parent, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (msgContractorModels.size() > 0) {

            Milestome_model msgContractorModel=msgContractorModels.get(position);
            holder.tv_milestone.setText(msgContractorModel.getRate_caption());
            holder.tv_mile_amount.setText(msgContractorModel.getAmount());


//            if (!msgContractorModel.getUsrFile().equalsIgnoreCase(null)){
//
//                Picasso.with(context).load(BASE_URL_IMEGES+msgContractorModel.getUsrFile())
//                        .fit().centerCrop()
//                        .placeholder(R.drawable.ic_profile)
//                        .error(R.drawable.ic_profile)
//                        .into(holder.img_profile);
//            }


        }
    }

    @Override
    public int getItemCount() {
        return msgContractorModels.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_milestone, tv_mile_amount;
        public ImageView img_profile;


        public ViewHolder(View parent) {
            super(parent);
            tv_milestone = parent.findViewById(R.id.tv_milestone);
            tv_mile_amount = parent.findViewById(R.id.tv_mile_amount);


        }
    }

}
