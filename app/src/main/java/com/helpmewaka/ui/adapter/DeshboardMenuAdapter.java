package com.helpmewaka.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.activity.common.ActivityServices;
import com.helpmewaka.ui.model.WorkListData;
import com.helpmewaka.ui.util.ToastClass;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ravindra Birla on 14/09/2019.
 */
public class DeshboardMenuAdapter extends RecyclerView.Adapter<DeshboardMenuAdapter.ViewHolder> {
    private List<WorkListData> menuList;
    private WorkListData menuListData;
    private Context context;

    public DeshboardMenuAdapter(List<WorkListData> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeshboardMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_service, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeshboardMenuAdapter.ViewHolder holder, int position) {
        if (menuList.size() > 0) {
            menuListData = menuList.get(position);
            holder.tv_name.setText(menuListData.name);
            holder.tv_title.setText(menuListData.title);


            Picasso.with(context).load(menuListData.image).fit().centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(holder.iv_profile);

            /*if (!menuListData.image.equalsIgnoreCase(null) && !menuListData.image.equalsIgnoreCase("")) {

                Picasso.with(context).load(menuListData.image).fit().centerCrop()
                        .placeholder(R.drawable.doctor)
                        .error(R.drawable.doctor)
                        .into(holder.img_profile);
            }*/

        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_name, tv_title;
        public ImageView iv_profile;
        RelativeLayout ll_item;
        public RatingBar ratingBar;


        public ViewHolder(View parent) {
            super(parent);
            tv_name = parent.findViewById(R.id.tv_name);
            tv_title = parent.findViewById(R.id.tv_title);
            iv_profile = parent.findViewById(R.id.iv_profile);
            ll_item = parent.findViewById(R.id.ll_item);


            ll_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.ll_item:
                    menuListData = menuList.get(getAdapterPosition());
                    //ToastClass.showToast(context,menuListData.name);
                    Intent i = new Intent(context, ActivityServices.class);
                    context.startActivity(i);
                    break;
            }
        }
    }
}
