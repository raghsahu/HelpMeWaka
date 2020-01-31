package com.helpmewaka.ui.customer.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.common.ActivityLogin;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.model.UserInfoData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerSignupFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private TextView tv_Login;
    private Button btn_signup;
    private EditText et_fName, et_lname, et_email, et_password, et_Cpassword, et_phone;
    private Session session;

    public CustomerSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_signup, container, false);

        session = new Session(context);
        initView();
        clickListner();


        return view;
    }


    private void initView() {


        et_fName = view.findViewById(R.id.et_fName);
        et_lname = view.findViewById(R.id.et_lname);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        et_Cpassword = view.findViewById(R.id.et_Cpassword);
        et_phone = view.findViewById(R.id.et_phone);
        tv_Login = view.findViewById(R.id.tv_Login);
        btn_signup = view.findViewById(R.id.btn_signup);
    }

    private void clickListner() {
        tv_Login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_Login:
                intent = new Intent(context, ActivityLogin.class);
                startActivity(intent);
                ((Activity) context).finish();
                break;

            case R.id.btn_signup:
                checkValidation();
                /*intent = new Intent(context, MainActivity.class);
                intent.putExtra("Click", "2");
                startActivity(intent);
                ((Activity) context).finish();*/
                break;
        }
    }

    private void checkValidation() {

        String fname = et_fName.getText().toString().trim();
        String lname = et_lname.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String pass = et_password.getText().toString().trim();
        String Cpass = et_Cpassword.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();

        Validation validation = new Validation(context);
        /*if (!validation.hasImage(img_profile)) {
            ToastClass.showToast(context, getString(R.string.select_img));
            img_profile.requestFocus();
            return;

        } else */
        if (!validation.isEmpty(fname)) {
            ToastClass.showToast(context, getString(R.string.fname_null));
            et_fName.requestFocus();

        } else if (!validation.isEmpty(lname)) {
            ToastClass.showToast(context, getString(R.string.lname_null));
            et_lname.requestFocus();

        } else if (!validation.isValidEmail(email)) {
            ToastClass.showToast(context, getString(R.string.email_v));
            et_email.requestFocus();

        } else if (!validation.isValidPassword(pass)) {
            ToastClass.showToast(context, getString(R.string.password_null));
            et_password.requestFocus();
        } else if (!validation.isMaxCharPassword(pass)) {
            ToastClass.showToast(context, getString(R.string.password_v));
            et_password.requestFocus();
        } else if (!validation.isMaxCharPassword(Cpass)) {
            ToastClass.showToast(context, getString(R.string.password_v));
            et_Cpassword.requestFocus();
        } else if (validation.isConfirmPassword(pass, Cpass)) {
            ToastClass.showToast(context, getString(R.string.cpassword_null));
            et_Cpassword.requestFocus();
        } else if (!validation.isValidNo(phone)) {
            ToastClass.showToast(context, getString(R.string.phone_null));
            et_phone.requestFocus();

        } else {
            if (NetworkUtil.isNetworkConnected(context)) {
                try {
                    String url = API.BASE_URL + "customer_signup.php";
                    CallSignUpApi(url);

                } catch (NullPointerException e) {
                    ToastClass.showToast(context, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(context, getString(R.string.no_internet_access));
            }
        }
    }

    private void CallSignUpApi(String url) {
        Utils.showDialog(context, "Loading Please Wait...");

        AndroidNetworking.post(url)

                .addBodyParameter("fname", et_fName.getText().toString())
                .addBodyParameter("lname", et_lname.getText().toString())
                .addBodyParameter("email", et_email.getText().toString())
                .addBodyParameter("password", et_password.getText().toString())
                .addBodyParameter("confirmpass", et_Cpassword.getText().toString())
                .addBodyParameter("telephone", et_phone.getText().toString())
                /* .addBodyParameter("lat", "")
                 .addBodyParameter("lon", "")
                 .addBodyParameter("type", type)
                 .addBodyParameter("register_id", "")
                 .addBodyParameter("image", "")*/
                //.addBodyParameter("image", img)
                .setTag("user")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("response sigup cus=", "" + jsonObject);
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String results = jsonObject.getString("results");

                            if (results.equalsIgnoreCase("true")) {
                                JSONObject userdetail = jsonObject.getJSONObject("data");
                                UserInfoData user = new UserInfoData();
                                user.user_id = userdetail.getString("CLT_ID");
                                user.FName = userdetail.getString("FName");
                                user.LName = userdetail.getString("LName");
                                user.Email = userdetail.getString("Email");
                                user.Contact_Num = userdetail.getString("Contact_Num");
                                user.Alternate_Num = userdetail.getString("Alternate_Num");
                                user.Type = userdetail.getString("Type");
                                //user.Dob = userdetail.getString("Dob");
                                user.Pic = userdetail.getString("Pic");
                                //user.lat = userdetail.getString("lat");
                                //user.lon = userdetail.getString("lon");
                                //user.register_id = userdetail.getString("register_id");


                                session.createSession(user);

                                //ToastClass.showToast(context, "Registration Sucesss...");

                                Utils.openAlertDialog(context, message);

                                Intent intent = new Intent(context, ActivityLogin.class);
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                ((Activity) context).finish();

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus sigup", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error cus sigup", "" + error);
                    }
                });
    }
}
