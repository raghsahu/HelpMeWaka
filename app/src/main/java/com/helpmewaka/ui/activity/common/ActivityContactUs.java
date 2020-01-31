package com.helpmewaka.ui.activity.common;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.customer.activity.ActivityUpdateJob;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

public class ActivityContactUs extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    LinearLayout ll_email, ll_call;
    EditText et_name, et_email, edit_comment;
    Button btn_submit;
    private boolean isPermitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        initView();
        clickListener();


    }


    private void initView() {
        spinner = findViewById(R.id.spinner_support);
        ll_email = findViewById(R.id.ll_email);
        ll_call = findViewById(R.id.ll_call);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        edit_comment = findViewById(R.id.et_comment);
        btn_submit = findViewById(R.id.btn_submit);

        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenEmail("helpme@helpmewaka.com");

            }
        });

        ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:02408028500"));

                if (ActivityCompat.checkSelfPermission(ActivityContactUs.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    checkRunTimePermission();

                    return;
                }
                startActivity(callIntent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Et_name = et_name.getText().toString();
                String Et_email = et_email.getText().toString();
                String Et_comment = edit_comment.getText().toString();
                String SpinSupport = spinner.getSelectedItem().toString();
                if (SpinSupport.equalsIgnoreCase("Select Support")) {
                    Toast.makeText(ActivityContactUs.this, "Please select support", Toast.LENGTH_SHORT).show();
                } else {

                    if (!Et_name.isEmpty() && !Et_email.isEmpty() && !Et_comment.isEmpty()) {

                        if (Patterns.EMAIL_ADDRESS.matcher(Et_email).matches()) {

                            if (NetworkUtil.isNetworkConnected(ActivityContactUs.this)) {

                                SendSupportRequest(Et_name, Et_email, Et_comment, SpinSupport);
                            } else {
                                Toast.makeText(ActivityContactUs.this, "Please check internet", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(ActivityContactUs.this, "Please enter correct email", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(ActivityContactUs.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void SendSupportRequest(String et_name, String et_email, final String et_comment, String spinSupport) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(API.BASE_URL + "contact_us.php")
                .addBodyParameter("fullname", et_name)
                .addBodyParameter("email_add", et_email)
                .addBodyParameter("support_type", spinSupport)
                .addBodyParameter("comment_description", et_comment)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("res_UpdateJob_cus", "" + jsonObject);
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");

                            if (result.equalsIgnoreCase("true")) {

                                ToastClass.showToast(ActivityContactUs.this, message);
                                edit_comment.setText("");


                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(ActivityContactUs.this, message);
                            }
                        } catch (JSONException e) {
                            Log.e("exp_jobupdate", "" + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("error_updatejob ", error.toString());
                        ToastClass.showToast(ActivityContactUs.this, error.toString());
                    }
                });


    }

    private void OpenEmail(String email) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("plain/text");
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//        intent.putExtra(Intent.EXTRA_SUBJECT, "");
//        intent.putExtra(Intent.EXTRA_TEXT, "");
//        startActivity(Intent.createChooser(intent, ""));
//**********************************
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {email};//Add multiple recipients here
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, ""); //Add Mail Subject
        intent.putExtra(Intent.EXTRA_TEXT, "");//Add mail body
        //intent.putExtra(Intent.EXTRA_CC, "");//Add CC emailid's if any
       // intent.putExtra(Intent.EXTRA_BCC, "");//Add BCC email id if any
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");//Added Gmail Package to forcefully open Gmail App
        startActivity(Intent.createChooser(intent, "Send mail"));


    }


    private void checkRunTimePermission() {

        String[] permissionArrays = new String[]{Manifest.permission.CALL_PHONE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
            Toast.makeText(this, "hheee", Toast.LENGTH_SHORT).show();
        } else {
//             if already permition granted
//             PUT YOUR ACTION (Like Open cemara etc..)

            Toast.makeText(this, "hhh", Toast.LENGTH_SHORT).show();
            phonecall("02408028500");

        }
    }

    private void clickListener() {

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityContactUs.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        String item = parent.getItemAtPosition(pos).toString();

        // Showing selected spinner item
        //ToastClass.showToast(this, "Selected: " + item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {

            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            Toast.makeText(ActivityContactUs.this, "Permission required", Toast.LENGTH_SHORT).show();
                            // alertView();
                        }
                    }
                }
            }

            try {
                //selectImage();
            }catch (Exception e){

            }

            if (isPermitted){
                Toast.makeText(this, ""+isPermitted, Toast.LENGTH_SHORT).show();
                phonecall("02408028500");

            }else {
                //Toast.makeText(getActivity(), "Contact list not show", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void phonecall(String number) {
//
        Toast.makeText(this, "ppp", Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));

        startActivity(callIntent);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ActivityContactUs.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
