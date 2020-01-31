package com.helpmewaka.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helpmewaka.R;
import com.helpmewaka.ui.customer.activity.ActivityPayment;
import com.helpmewaka.ui.customer.adapter.PaymentAdapter;
import com.helpmewaka.ui.model.ServiceListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<Custom_Spin_Data> {

    LayoutInflater flater;
    Context context;
   // ArrayList<ServiceListData>serviceListData=new ArrayList<>();

    public CustomSpinnerAdapter(Activity context, int listitems_layout, List<Custom_Spin_Data> list){

        super(context,listitems_layout, list);
        this.context=context;
//        flater = context.getLayoutInflater();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){

        Custom_Spin_Data rowItem = getItem(position);

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.listitems_layout, null, false);

            holder.txtTitle = (TextView) rowview.findViewById(R.id.title);
            holder.recycler_sub_cat =  rowview.findViewById(R.id.recycler_sub_cat);
           // holder.imageView = (ImageView) rowview.findViewById(R.id.icon);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }
        //holder.imageView.setImageResource(rowItem.getImageId());
        holder.txtTitle.setText(rowItem.getServiceTitle());


//        Log.e("sub_cat_size", ""+rowItem.getServieSubCate().size());
//        SubCateAdapter mAdapter = new SubCateAdapter(rowItem.getServieSubCate(), context);
//        holder.recycler_sub_cat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//        holder.recycler_sub_cat.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

        return rowview;
    }

    private class viewHolder{
        TextView txtTitle;
        RecyclerView recycler_sub_cat;
        //ImageView imageView;
    }
}
