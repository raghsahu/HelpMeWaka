package com.helpmewaka.ui.contractor.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.common.ActivitySignUp;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.contractor.activity.ActivityProfileDetailContractor;
import com.helpmewaka.ui.model.UserInfoData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContractorLoginFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private View view;
    private Button btn_login;
    private TextView tv_forgot;
    private TextView tv_signUp;
    private Dialog dialog;
    private EditText et_email_forgot;
    private Button btn_forgot_submit;
    private EditText et_email, et_password;
    private Session session;


    public ContractorLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contractor_login, container, false);
       context=getActivity();
        session = new Session(context);
        initView();
        clickListner();

        return view;
    }


    private void initView() {
        btn_login = view.findViewById(R.id.btn_login);
        tv_forgot = view.findViewById(R.id.tv_forgot);
        tv_signUp = view.findViewById(R.id.tv_signUp);

        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);

    }

    private void clickListner() {
        tv_forgot.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_signUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_login:
                checkValidation();
                break;

            case R.id.tv_forgot:
                openForgotDialog();
                break;

            case R.id.tv_signUp:
                intent = new Intent(context, ActivitySignUp.class);
                startActivity(intent);
                ((Activity) context).finish();
                break;

            case R.id.btn_forgot_submit:
                Utils.hideSoftKeyboard(view);

                String email = et_email_forgot.getText().toString();
                Validation validation = new Validation(context);

                if (!validation.isValidEmail(email)) {
                    ToastClass.showToast(context, getString(R.string.email_v));
                    et_email.requestFocus();

                } else {

                    if (NetworkUtil.isNetworkConnected(context)) {
                        try {

                            String url = API.BASE_URL + "contractor_forgot_password.php";
                            Log.e("Forgot URL = ", url);

                            sendEmailToServer(url);
                            //Forgot_Call(email);
                        } catch (NullPointerException e) {
                            ToastClass.showToast(context, getString(R.string.too_slow));
                        }
                    } else {
                        ToastClass.showToast(context, getString(R.string.no_internet_access));
                    }
                }
                break;

        }
    }

    private void openForgotDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.forgot_dialog);
        et_email_forgot = dialog.findViewById(R.id.et_email_forgot);
        btn_forgot_submit = dialog.findViewById(R.id.btn_forgot_submit);

        btn_forgot_submit.setOnClickListener(this);

        dialog.show();
    }

    private void sendEmailToServer(String url) {

        Utils.showDialog(context, "Loading Please Wait...");
        AndroidNetworking.post(url)
                //.addBodyParameter("CLT_ID", )
                .addBodyParameter("emailid", et_email_forgot.getText().toString().trim())
                .setTag("forgot password")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        try {

                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");

                            if (result.equalsIgnoreCase("true")) {
                                //Utils.openAlertDialog(ActivitySignin.this, result);
                                Utils.openAlertDialogForgot(context, message, true);
                                dialog.dismiss();

                            } else {
                                //ToastClass.showToast(ActivityLogin.this, result);
                                //Utils.openAlertDialog(ActivitySignin.this, result);
                                Utils.openAlertDialogForgot(context, message, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error forgot ", error.toString());
                        ToastClass.showToast(context, error.toString());
                    }
                });
    }

    private void checkValidation() {

        String email = et_email.getText().toString();
        String pass = et_password.getText().toString();

        Validation validation = new Validation(context);

        if (!validation.isValidEmail(email)) {
            ToastClass.showToast(context, getString(R.string.email_v));
            et_email.requestFocus();

        } else if (!validation.isValidPassword(pass)) {
            ToastClass.showToast(context, getString(R.string.password_null));
            et_password.requestFocus();
        } else {

            if (NetworkUtil.isNetworkConnected(context)) {
                try {
                    String url = API.BASE_URL + "contractor_login.php";
                    CallLoginApi(url);
                } catch (NullPointerException e) {
                    ToastClass.showToast(context, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(context, getString(R.string.no_internet_access));
            }
        }

    }

    private void CallLoginApi(String url) {

        Utils.showDialog(context, "Loading Please Wait...");

        AndroidNetworking.post(url)

                .addBodyParameter("emailid", et_email.getText().toString())
                .addBodyParameter("pass", et_password.getText().toString())
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

                        Log.e("response login cus=", "" + jsonObject);
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String results = jsonObject.getString("results");

                            if (results.equalsIgnoreCase("true")) {
                                JSONObject userdetail = jsonObject.getJSONObject("data");
                                UserInfoData user = new UserInfoData();
                                user.user_id = userdetail.getString("CNT_ID");
                                user.FName = userdetail.getString("FName");
                                user.LName = userdetail.getString("LName");
                                user.Dob = userdetail.getString("BirthDt");
                                user.Email = userdetail.getString("Email");
                                user.Salt = userdetail.getString("Salt");
                                user.Contact_Num = userdetail.getString("Contact_Num");
                                user.Alternate_Num = userdetail.getString("Alternate_Num");

                                user.Pic = userdetail.getString("Pic");
                                user.ScanId_Proof = userdetail.getString("ScanId_Proof");
                                user.Address = userdetail.getString("Address");
                                user.Country = userdetail.getString("Country");
                                user.City = userdetail.getString("City");
                                user.State = userdetail.getString("State");
                                user.PinCode = userdetail.getString("PinCode");

                                user.Reference_Name1 = userdetail.getString("Reference_Name1");
                                user.Reference_Address1 = userdetail.getString("Reference_Address1");
                                user.Reference_Email1 = userdetail.getString("Reference_Email1");
                                user.Reference_Phone1 = userdetail.getString("Reference_Phone1");

                                user.Reference_Name2 = userdetail.getString("Reference_Name2");
                                user.Reference_Address2 = userdetail.getString("Reference_Address2");
                                user.Reference_Email2 = userdetail.getString("Reference_Email2");
                                user.Reference_Phone2 = userdetail.getString("Reference_Phone2");

                                user.Reference_Name3 = userdetail.getString("Reference_Name3");
                                user.Reference_Address3 = userdetail.getString("Reference_Address3");
                                user.Reference_Email3 = userdetail.getString("Reference_Email3");
                                user.Reference_Phone3 = userdetail.getString("Reference_Phone3");
                                user.Type = userdetail.getString("Type");

                                session.createSession(user);

                                ToastClass.showToast(context, message);

                                //Intent intent = new Intent(context, MainActivity.class);
                                Intent intent = new Intent(context, ActivityProfileDetailContractor.class);
                                intent.putExtra("SPLASH","SPLASH");
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                ((Activity) context).finish();

                            } else {
                                //Utils.openAlertDialog(context, message);
                               // ToastClass.showToast(context, message);
                                Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus login", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error cus login", "" + error);
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
