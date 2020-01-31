package com.helpmewaka.ui.activity.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.server.Session;

public class ActivityTermCondition extends AppCompatActivity {
    private WebView about_us_web;
    private String loginType = "";
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);
        session = new Session(this);
        loginType = session.getUser().Type;
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityTermCondition.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        about_us_web = findViewById(R.id.about_us_web);

        about_us_web.getSettings().setJavaScriptEnabled(true); // enable javascript
        about_us_web.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(ActivityTermCondition.this, description, Toast.LENGTH_SHORT).show();
            }
        });
       // about_us_web.loadUrl("http://logicalsofttech.com/helpmewaka/rules/Terms_&_Condition.html");

       // https://www.helpmewaka.com/rules/Terms_&_Condition.html

        if (loginType.equalsIgnoreCase("contractor")) {
            about_us_web.loadUrl("https://www.helpmewaka.com/term-condition-contractor.html");
        } else if (loginType.equalsIgnoreCase("customer")) {
            about_us_web.loadUrl("https://www.helpmewaka.com/term-condition-user.html");

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ActivityTermCondition.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
