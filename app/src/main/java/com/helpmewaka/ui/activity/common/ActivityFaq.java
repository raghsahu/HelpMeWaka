package com.helpmewaka.ui.activity.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;

public class ActivityFaq extends AppCompatActivity implements View.OnClickListener {
    private TextView tv1, tv2, tv3, tv4, tv5, tv6;
    private TextView tv_11, tv_22, tv_33, tv_44, tv_55, tv_66;

    private ImageView iv_back;
    private boolean isclick1 = false;
    private boolean isclick2 = false;
    private boolean isclick3 = false;
    private boolean isclick4 = false;
    private boolean isclick5 = false;
    private boolean isclick6 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);


        initView();
        clickListner();
    }


    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        tv_11 = findViewById(R.id.tv_11);
        tv_22 = findViewById(R.id.tv_22);
        tv_33 = findViewById(R.id.tv_33);
        tv_44 = findViewById(R.id.tv_44);
        tv_55 = findViewById(R.id.tv_55);
        tv_66 = findViewById(R.id.tv_66);
    }

    private void clickListner() {
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ActivityFaq.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent i = new Intent(ActivityFaq.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;

            case R.id.tv1:
                if (isclick1) {
                    tv_11.setVisibility(View.GONE);
                    isclick1 = false;
                } else {
                    tv_11.setVisibility(View.VISIBLE);
                    isclick1 = true;
                }
                break;

            case R.id.tv2:
                if (isclick2) {
                    tv_22.setVisibility(View.GONE);
                    isclick2 = false;
                } else {
                    tv_22.setVisibility(View.VISIBLE);
                    isclick2 = true;
                }
                break;

            case R.id.tv3:
                if (isclick3) {
                    tv_33.setVisibility(View.GONE);
                    isclick3 = false;
                } else {
                    tv_33.setVisibility(View.VISIBLE);
                    isclick3 = true;
                }
                break;

            case R.id.tv4:
                if (isclick4) {
                    tv_44.setVisibility(View.GONE);
                    isclick4 = false;
                } else {
                    tv_44.setVisibility(View.VISIBLE);
                    isclick4 = true;
                }
                break;

            case R.id.tv5:
                if (isclick5) {
                    tv_55.setVisibility(View.GONE);
                    isclick5 = false;
                } else {
                    tv_55.setVisibility(View.VISIBLE);
                    isclick5 = true;
                }
                break;

            case R.id.tv6:
                if (isclick6) {
                    tv_66.setVisibility(View.GONE);
                    isclick6 = false;
                } else {
                    tv_66.setVisibility(View.VISIBLE);
                    isclick6 = true;
                }
                break;
        }
    }
}
