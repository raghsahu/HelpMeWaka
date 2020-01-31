package com.helpmewaka.ui.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.customer.activity.ActivityJobViewMoreCust;
import com.helpmewaka.ui.customer.activity.ActivityPayment;
import com.helpmewaka.ui.customer.activity.ActivityPaymentPayNow;
import com.helpmewaka.ui.model.PaymentData;
import com.helpmewaka.ui.server.Session;

import java.util.List;

/**
 * Created by Ravindra Birla on 20/09/2019.
 */
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private List<PaymentData> paymentList;
    private PaymentData payData;
    private Context context;
    private Session session;
    private String user_id;
    public static String Multi_milestome;

    public PaymentAdapter(List<PaymentData> paymentList, Context context) {
        this.paymentList = paymentList;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_payment, parent, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        return new PaymentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentAdapter.ViewHolder holder, final int position) {
        if (paymentList.size() > 0) {
            payData = paymentList.get(position);
            holder.tv_milestone.setText(payData.MILESTONE);
            holder.tv_trasaction.setText(payData.TransactionID);
            holder.tv_invoice_id.setText(payData.InvoiceID);
            holder.tv_payment_mode.setText(payData.paymentMode);
            holder.tv_payment_date.setText(payData.paymentDt);
            holder.tv_amount.setText(payData.Amount);


            /*if (!PaymentData.image.equalsIgnoreCase(null)){

                Picasso.with(context).load(PaymentData.image).fit().centerCrop()
                        .placeholder(R.drawable.doctor)
                        .error(R.drawable.doctor)
                        .into(holder.img_profile);
            }*/


            try {

                StringBuilder sbString = new StringBuilder();
                //iterate through ArrayList
                for(PaymentData language : paymentList){

                    if (language.payment_status.equalsIgnoreCase("Unpaid")){
                        //append ArrayList element followed by comma
                        sbString.append(language.JRT_ID).append(",");
                    }

                }

                //convert StringBuffer to String
                String strList = sbString.toString();

                //remove last comma from String if you want
                if( strList.length() > 0 )
                    strList = strList.substring(0, strList.length() - 1);

                System.out.println(strList);
                Multi_milestome=strList;

            }catch (Exception e){

            }




            //********************************************************

            if (payData.payment_status.equalsIgnoreCase("Unpaid")){

                holder.btn_update.setText("Pay Now");
            }else {
                holder.btn_update.setText("Paid");
            }

            holder.btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.btn_update.getText().toString().equalsIgnoreCase("Pay Now")){

                        Intent intent = new Intent(context, ActivityPaymentPayNow.class);
                        intent.putExtra("Payment_amt",paymentList.get(position).Amount);
                        intent.putExtra("JRT_ID",paymentList.get(position).JRT_ID);
                        intent.putExtra("JOB_ID",paymentList.get(position).JOB_ID);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Already paid", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_milestone, tv_trasaction, tv_invoice_id, tv_payment_mode, tv_payment_date, tv_amount;
        Button btn_update;

        public ViewHolder(View parent) {
            super(parent);
            tv_milestone = parent.findViewById(R.id.tv_milestone);
            tv_trasaction = parent.findViewById(R.id.tv_trasaction);
            tv_invoice_id = parent.findViewById(R.id.tv_invoice_id);
            tv_payment_mode = parent.findViewById(R.id.tv_payment_mode);
            tv_payment_date = parent.findViewById(R.id.tv_payment_date);
            tv_amount = parent.findViewById(R.id.tv_amount);
            btn_update = parent.findViewById(R.id.btn_update);
        }
    }
}
