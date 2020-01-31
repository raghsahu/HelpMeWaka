package com.helpmewaka.ui.activity.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.helpmewaka.R;
import com.helpmewaka.ui.adapter.SliderAdapter;

public class ActivityWelCome extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;
    private int dotsCount;
    private ImageView[] dotes;
    private TextView tv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_come);
        initMethod();
    }

    private void initMethod() {

        viewPager = findViewById(R.id.slider_pager);
        linearLayout = findViewById(R.id.linear_layout);
        tv_skip = findViewById(R.id.tv_skip);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityWelCome.this, ActivityLogin.class);
                startActivity(intent);
            }
        });

        //dotesIndicater(0);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(pageChangeListener);
        dotesIndicater();
    }


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            //dotesIndicater(position);

            //for (int i = 0; i < dotes.length; i++) {
            /*for (ImageView dote : dotsCount) {
                dotes[position].setImageResource(R.drawable.circle_inactive);
            }*/

            for (int i = 0; i < dotsCount; i++) {
                dotes[i].setImageDrawable(getResources().getDrawable(R.drawable.circle_inactive));
            }

            dotes[position].setImageResource(R.drawable.circle_active);


            if (position + 1 == dotsCount) {
                tv_skip.setText("Get Start");

            } else {
                tv_skip.setText("Skip");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void dotesIndicater() {
        dotsCount = sliderAdapter.getCount();
        dotes = new ImageView[dotsCount];
        linearLayout.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dotes[i] = new ImageView(this);
            dotes[i].setImageResource(R.drawable.circle_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20,
                    20
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dotes[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    viewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            linearLayout.addView(dotes[i], params);

            //linearLayout.addView(dotes[i]);
            //linearLayout.bringToFront();
        }

        /*if (dotes.length > 0) {
            dotes[i].setImageResource(R.drawable.circle_active);
        }*/
    }
}
