package com.helpmewaka.ui.activity.Service_FragModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.activity.Custom_Spin_Data;
import com.helpmewaka.ui.activity.Custom_spin_Recycler;
import com.helpmewaka.ui.activity.SubCateAdapter;
import com.helpmewaka.ui.server.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public class Custom_spin_FragAdapter extends RecyclerView.Adapter<Custom_spin_FragAdapter.ViewHolder> {
    private List<Custom_Spin_Data> custom_spin_data=new ArrayList<>();

    private Context context;
    private Session session;
    private String user_id;
    SubCateFragAdapter subCateAdapter;

    public Custom_spin_FragAdapter(List<Custom_Spin_Data> paymentList1, Context mcontext) {
        custom_spin_data = paymentList1;
        context = mcontext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems_layout, parent, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // if (custom_spin_data.size() > 0) {
        Log.e("custom_spin_size1", ""+custom_spin_data.size());
        final Custom_Spin_Data  payData = custom_spin_data.get(position);
        holder.text1.setText(payData.getServiceTitle());


        Log.e("subcat_size11", ""+payData.getServieSubCate().size());
        // Log.e("subcat_size11", ""+custom_spin_data.get(1).getServieSubCate().size());
        subCateAdapter = new SubCateFragAdapter(payData.getServieSubCate(),payData.getSERVID(),payData.getServiceTitle(), context);
        holder.recycler_sub_cat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        holder.recycler_sub_cat.setAdapter(subCateAdapter);
        // mAdapter.notifyDataSetChanged();

        // }
    }

    @Override
    public int getItemCount() {
        Log.e("custom_spin_size", ""+custom_spin_data.size());
        return custom_spin_data.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;
        RecyclerView recycler_sub_cat;

        public ViewHolder(View parent) {
            super(parent);
            text1 = parent.findViewById(R.id.title);
            recycler_sub_cat=parent.findViewById(R.id.recycler_sub_cat);

        }
    }
}
