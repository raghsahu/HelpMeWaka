package com.helpmewaka.ui.contractor.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.NotificationData;

import java.util.List;

/**
 * Created by Ravindra Birla on 17/09/2019.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationData> notificationList;
    private NotificationData notificationData;
    private Context context;

    public NotificationAdapter(List<NotificationData> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        if (notificationList.size() > 0) {
            notificationData = notificationList.get(position);
           // holder.tv_title.setText(notificationData.NotifyDesc);
            holder.tv_date.setText(notificationData.PostDt);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.tv_title.setText(Html.fromHtml(notificationData.NotifyDesc, Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.tv_title.setText(Html.fromHtml(notificationData.NotifyDesc));
            }

            /*if (!notificationData.image.equalsIgnoreCase(null)){

                Picasso.with(context).load(notificationData.image).fit().centerCrop()
                        .placeholder(R.drawable.doctor)
                        .error(R.drawable.doctor)
                        .into(holder.img_profile);
            }*/
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
        //return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, tv_date;
        public ImageView iv_profile;


        public ViewHolder(View parent) {
            super(parent);
            tv_title = parent.findViewById(R.id.tv_title);
            tv_date = parent.findViewById(R.id.tv_date);
            iv_profile = parent.findViewById(R.id.iv_profile);

        }
    }
}