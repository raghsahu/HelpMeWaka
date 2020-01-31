package com.helpmewaka.ui.activity.Service_FragModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.activity.ServieSubCateData;
import com.helpmewaka.ui.server.Session;

import java.util.ArrayList;
import java.util.List;

import static com.helpmewaka.ui.customer.fragment.FragmentPostAnErrand.NextQuizLevel_dialog;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public class SubCateFragAdapter extends RecyclerView.Adapter<SubCateFragAdapter.ViewHolder> {
    private List<ServieSubCateData> paymentList=new ArrayList<>();
    private ServieSubCateData payData;
    private Context context;
    private Session session;
    private String user_id;
    private String loginType = "";
    String Category_Id;
    String Category_title;
    AdapterCallback mAdapterCallback;

    public SubCateFragAdapter(List<ServieSubCateData> paymentList1, String servid, String serviceTitle, Context mcontext) {
        this.paymentList = paymentList1;
        context = mcontext;
        Category_Id=servid;
        Category_title=serviceTitle;
    }

//    public SubCateFragAdapter(FragmentActivity activity) {
//    }
    public SubCateFragAdapter(Context fragment) {
        try {
            this.mAdapterCallback = ((AdapterCallback) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
    }

//    public SubCateFragAdapter() {
//
//    }


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
           // Log.e("subcat_size_ad", ""+paymentList.size());
            payData = paymentList.get(position);
            holder.text1.setText(payData.getServiceSubcategory());

            try {
                mAdapterCallback = ((AdapterCallback) context);
            } catch (ClassCastException e) {
               // throw new ClassCastException("Activity must implement AdapterCallback.");
            }

            holder.text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // loginType = session.getUser().Type;
//                    if (loginType.equalsIgnoreCase("customer")) {
//                        Intent intent = new Intent(context, ActivityProfileDetailCustomer.class);
//                        intent.putExtra("SERVICE_ID", payData.getSubCatSERVID());
//                        context.startActivity(intent);
//                    }

                    Log.e("ser_id_parti", paymentList.get(position).getSubCatSERVID());
                    Log.e("ser_name_parti", paymentList.get(position).getServiceSubcategory());

                    String SERVICE_ID=paymentList.get(position).getSubCatSERVID();
                    String SERVICE_name=paymentList.get(position).getServiceSubcategory();

                    mAdapterCallback.onMethodCallback(SERVICE_ID,Category_Id,SERVICE_name,Category_title);

                    NextQuizLevel_dialog.dismiss();

                }
            });

       // }
    }


    public interface AdapterCallback {
        void onMethodCallback(String SERVICE_ID, String category_Id, String SERVICE_name, String category_title);
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
