package com.helpmewaka.ui.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.adapter.ContracterMsgAdapter;
import com.helpmewaka.ui.contractor.model.MsgContractorModel;
import com.helpmewaka.ui.model.MsgCoustomerModel;
import com.helpmewaka.ui.server.Session;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.helpmewaka.ui.server.API.BASE_URL_IMEGES;

public class CustomerMsgAdapter extends RecyclerView.Adapter<CustomerMsgAdapter.ViewHolder> {
    private ArrayList<MsgCoustomerModel> msgContractorModels;
    private Context context;
    private Session session;
    private String user_id;

    public CustomerMsgAdapter(ArrayList<MsgCoustomerModel> msgContractorModelList, Context context) {
        this.msgContractorModels = msgContractorModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_contractor_list, parent, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (msgContractorModels.size() > 0) {

            MsgCoustomerModel msgContractorModel=msgContractorModels.get(position);
            holder.tv_msg.setText(msgContractorModel.getMsg_content());
            holder.tv_date.setText(msgContractorModel.getCreateDt());


            if (!msgContractorModel.getUsrFile().equalsIgnoreCase(null)){

                Picasso.with(context).load(BASE_URL_IMEGES+msgContractorModel.getUsrFile())
                        .fit().centerCrop()
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(holder.img_profile);
            }


        }
    }

    @Override
    public int getItemCount() {
        return msgContractorModels.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_msg, tv_date;
        public ImageView img_profile;


        public ViewHolder(View parent) {
            super(parent);
            tv_msg = parent.findViewById(R.id.tv_msg);
            tv_date = parent.findViewById(R.id.tv_date);
            img_profile = parent.findViewById(R.id.img_profile);

        }
    }
}
