package com.helpmewaka.ui.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.helpmewaka.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomPagerAdapter(Context context) {
        this.context = context;
    }

    public int[] sliderImage = {
            R.drawable.home_slider1,
            R.drawable.home_slider2,
            R.drawable.home_slider3
    };

    @Override
    public int getCount() {
        return sliderImage.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.list_item_welcome, container, false);

        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageResource(sliderImage[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
