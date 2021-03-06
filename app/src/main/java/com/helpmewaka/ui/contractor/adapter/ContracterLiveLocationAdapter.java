package com.helpmewaka.ui.contractor.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.ContractorLiveLocation;
import com.helpmewaka.ui.contractor.model.MsgContractorModel;
import com.helpmewaka.ui.server.Session;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import static com.helpmewaka.ui.server.API.BASE_URL_IMEGES;

public class ContracterLiveLocationAdapter extends RecyclerView.Adapter<ContracterLiveLocationAdapter.ViewHolder> {
    private ArrayList<ContractorLiveLocation> msgContractorModels;
    private Context context;
    private Session session;
    private String user_id;

    public ContracterLiveLocationAdapter(ArrayList<ContractorLiveLocation> msgContractorModelList, Context context) {
        this.msgContractorModels = msgContractorModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contractor_live_location_adapter, parent, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (msgContractorModels.size() > 0) {

            final ContractorLiveLocation msgContractorModel=msgContractorModels.get(position);
            holder.tv_longi.setText(msgContractorModel.getLognitude());
            holder.tv_latitu.setText(msgContractorModel.getLatitude());
            holder.tv_date_time.setText(msgContractorModel.getPostDt());

//            Log.e("getLognitude", ""+msgContractorModel.getLognitude());
//            Log.e("getPostDt", ""+msgContractorModel.getPostDt());
//            Log.e("getsize", ""+msgContractorModels.size());


            if (msgContractorModel.getLognitude()!=null && msgContractorModel.getLatitude()!=null){

                holder.tv_google_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uri = "http://maps.google.com/maps?q=loc:" + msgContractorModel.getLatitude() + "," + msgContractorModel.getLognitude();

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }

                    }
                });

            }

        }
    }

    @Override
    public int getItemCount() {
        return msgContractorModels.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_longi, tv_latitu,tv_date_time,tv_google_map;


        public ViewHolder(View parent) {
            super(parent);
            tv_longi = parent.findViewById(R.id.tv_longi);
            tv_latitu = parent.findViewById(R.id.tv_latitu);
            tv_date_time = parent.findViewById(R.id.tv_date_time);
            tv_google_map = parent.findViewById(R.id.tv_google_map);



        }
    }
}
