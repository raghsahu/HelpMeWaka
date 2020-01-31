package com.helpmewaka.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer;
import com.helpmewaka.ui.server.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public class SubCateAdapter extends RecyclerView.Adapter<SubCateAdapter.ViewHolder> {
    private List<ServieSubCateData> paymentList;
    private ServieSubCateData payData;
    private Context context;
    private Session session;
    private String user_id;
    private String loginType = "";
    String Category_id,Category_title;

    public SubCateAdapter(List<ServieSubCateData> paymentList1, String servid, String serviceTitle, Context context) {
        this.paymentList = paymentList1;
        this.context = context;
        this.Category_id = servid;
        this.Category_title = serviceTitle;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spin_recycler_item, parent, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       // if (paymentList.size() > 0) {
            Log.e("subcat_size_ad", ""+paymentList.size());
            payData = paymentList.get(position);
            holder.text1.setText(payData.getServiceSubcategory());


            holder.text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("pos_id",paymentList.get(position).getSubCatSERVID());
                    loginType = session.getUser().Type;
                if (loginType.equalsIgnoreCase("customer")) {
                        Intent intent = new Intent(context, ActivityProfileDetailCustomer.class);
                        intent.putExtra("SERVICE_ID", paymentList.get(position).getSubCatSERVID());
                        intent.putExtra("Category_id", Category_id);
                        intent.putExtra("Category_title", Category_title);
                        intent.putExtra("SERVICE_NAME", paymentList.get(position).getServiceSubcategory());
                        context.startActivity(intent);
                    }

                }
            });

       // }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;

        public ViewHolder(View parent) {
            super(parent);
            text1 = parent.findViewById(R.id.tv_sub_cat);

        }
    }
}
