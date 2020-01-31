package com.helpmewaka.ui.contractor.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.common.ActivityLogin;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.model.UserInfoData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.PathUtils;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.NetworkUtil;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContractorSignupFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private TextView tv_Login;
    private Button btn_signup;
    private EditText et_fName, et_lname, et_email, et_phone;
    private Session session;
    private ImageView iv_profile, iv_profile_edit;
    private ImageView iv_unique_profile, iv_unique_profile_edit;
    private EditText et_ref_name1, et_ref_address1, et_ref_mob1, et_ref_email1;
    private EditText et_ref_name2, et_ref_address2, et_ref_mob2, et_ref_email2;
    private EditText et_ref_name3, et_ref_address3, et_ref_mob3, et_ref_email3;
    private File imgFileProfile;
    private File imgFileUnique;

    public ContractorSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contractor_signup, container, false);
        session = new Session(context);
        initView();
        clickListner();


        return view;

    }

    private void initView() {

        iv_unique_profile = view.findViewById(R.id.iv_unique_profile);
        iv_unique_profile_edit = view.findViewById(R.id.iv_unique_profile_edit);
        iv_profile = view.findViewById(R.id.iv_profile);
        iv_profile_edit = view.findViewById(R.id.iv_profile_edit);

        et_fName = view.findViewById(R.id.et_fName);
        et_lname = view.findViewById(R.id.et_lname);
        et_email = view.findViewById(R.id.et_email);
        et_phone = view.findViewById(R.id.et_phone);
        et_phone = view.findViewById(R.id.et_phone);

        et_ref_name1 = view.findViewById(R.id.et_ref_name1);
        et_ref_address1 = view.findViewById(R.id.et_ref_address1);
        et_ref_mob1 = view.findViewById(R.id.et_ref_mob1);
        et_ref_email1 = view.findViewById(R.id.et_ref_email1);

        et_ref_name2 = view.findViewById(R.id.et_ref_name2);
        et_ref_address2 = view.findViewById(R.id.et_ref_address2);
        et_ref_mob2 = view.findViewById(R.id.et_ref_mob2);
        et_ref_email2 = view.findViewById(R.id.et_ref_email2);

        et_ref_name3 = view.findViewById(R.id.et_ref_name3);
        et_ref_address3 = view.findViewById(R.id.et_ref_address3);
        et_ref_mob3 = view.findViewById(R.id.et_ref_mob3);
        et_ref_email3 = view.findViewById(R.id.et_ref_email3);

        tv_Login = view.findViewById(R.id.tv_Login);
        btn_signup = view.findViewById(R.id.btn_signup);
    }


    private void clickListner() {
        tv_Login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        iv_unique_profile_edit.setOnClickListener(this);
        iv_profile_edit.setOnClickListener(this);
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
                break;
            case R.id.iv_profile_edit:
                imagePick();
                break;

            case R.id.iv_unique_profile_edit:
                imagePick1();
                break;
        }
    }


    private void checkValidation() {

        String fname = et_fName.getText().toString().trim();
        String lname = et_lname.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();


        String ref_name1 = et_ref_name1.getText().toString().trim();
        String ref_add1 = et_ref_address1.getText().toString().trim();
        String ref_mob1 = et_ref_mob1.getText().toString().trim();
        String ref_email1 = et_ref_email1.getText().toString().trim();

        String ref_name2 = et_ref_name2.getText().toString().trim();
        String ref_add2 = et_ref_address2.getText().toString().trim();
        String ref_mob2 = et_ref_mob2.getText().toString().trim();
        String ref_email2 = et_ref_email2.getText().toString().trim();

        String ref_name3 = et_ref_name3.getText().toString().trim();
        String ref_add3 = et_ref_address3.getText().toString().trim();
        String ref_mob3 = et_ref_mob3.getText().toString().trim();
        String ref_email3 = et_ref_email3.getText().toString().trim();


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

        } else if (!validation.isValidNo(phone)) {
            ToastClass.showToast(context, getString(R.string.phone_null));
            et_phone.requestFocus();

        } else if (!validation.isEmpty(ref_name1)) {
            ToastClass.showToast(context, getString(R.string.ref_name_v));
            et_ref_name1.requestFocus();

        } else if (!validation.isEmpty(ref_add1)) {
            ToastClass.showToast(context, getString(R.string.ref_add_v));
            et_ref_address1.requestFocus();

        } else if (!validation.isEmpty(ref_mob1)) {
            ToastClass.showToast(context, getString(R.string.ref_phone_v));
            et_ref_mob1.requestFocus();

        } else if (!validation.isValidEmail(ref_email1)) {
            ToastClass.showToast(context, getString(R.string.ref_email_v));
            et_ref_email1.requestFocus();

        } else if (!validation.isEmpty(ref_name2)) {
            ToastClass.showToast(context, getString(R.string.ref_name_v));
            et_ref_name2.requestFocus();

        } else if (!validation.isEmpty(ref_add2)) {
            ToastClass.showToast(context, getString(R.string.ref_add_v));
            et_ref_address2.requestFocus();

        } else if (!validation.isEmpty(ref_mob2)) {
            ToastClass.showToast(context, getString(R.string.ref_phone_v));
            et_ref_mob2.requestFocus();

        } else if (!validation.isValidEmail(ref_email2)) {
            ToastClass.showToast(context, getString(R.string.ref_email_v));
            et_ref_email2.requestFocus();

        } else if (!validation.isEmpty(ref_name3)) {
            ToastClass.showToast(context, getString(R.string.ref_name_v));
            et_ref_name3.requestFocus();

        } else if (!validation.isEmpty(ref_add3)) {
            ToastClass.showToast(context, getString(R.string.ref_add_v));
            et_ref_address3.requestFocus();

        } else if (!validation.isEmpty(ref_mob3)) {
            ToastClass.showToast(context, getString(R.string.ref_phone_v));
            et_ref_mob3.requestFocus();

        } else if (!validation.isValidEmail(ref_email3)) {
            ToastClass.showToast(context, getString(R.string.ref_email_v));
            et_ref_email3.requestFocus();

        } else {
            if (NetworkUtil.isNetworkConnected(context)) {
                try {
                    String url = API.BASE_URL + "contractor_registration.php";
                    CallSignUpApi(url);

                } catch (NullPointerException e) {
                    ToastClass.showToast(context, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(context, getString(R.string.no_internet_access));
            }
        }
    }


    private void imagePick() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    //If you want the Uri.
                    //Mandatory to refresh image from Uri.
                    //getImageView().setImageURI(null);
                    //Setting the real returned image.
                    //getImageView().setImageURI(r.getUri());
                    //If you want the Bitmap.
                    iv_profile.setImageBitmap(r.getBitmap());
                    Log.e("bitmap = ", "" + r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    //img_uri = String.valueOf(r.getUri());
                    Log.e("img_uri_profile", String.valueOf(r.getUri()));


                    imgFileProfile = PathUtils.bitmapToFile(context, r.getBitmap());
                    Log.e("imgFileProfile", "" + imgFileProfile);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(getActivity(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show((FragmentActivity) context);
    }

    private void imagePick1() {
        final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
        dialog.setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        }).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult r) {

                if (r.getError() == null) {
                    //If you want the Uri.
                    //Mandatory to refresh image from Uri.
                    //getImageView().setImageURI(null);
                    //Setting the real returned image.
                    //getImageView().setImageURI(r.getUri());
                    //If you want the Bitmap.
                    iv_unique_profile.setImageBitmap(r.getBitmap());
                    Log.e("bitmap = ", "" + r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    //img_uri = String.valueOf(r.getUri());
                    Log.e("img_uri_unique", String.valueOf(r.getUri()));


                    imgFileUnique = PathUtils.bitmapToFile(context, r.getBitmap());
                    Log.e("imgFileUnique", "" + imgFileUnique);


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(getActivity(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show((FragmentActivity) context);
    }

    private void CallSignUpApi(String url) {
        Utils.showDialog(context, "Loading Please Wait...");

        AndroidNetworking.upload(url)
                .addMultipartFile("profilepic", imgFileProfile)
                .addMultipartFile("photocopypic", imgFileUnique)
                .addMultipartParameter("fname", et_fName.getText().toString().trim())
                .addMultipartParameter("lname", et_lname.getText().toString().trim())
                .addMultipartParameter("email", et_email.getText().toString().trim())
                .addMultipartParameter("telephone", et_phone.getText().toString().trim())

                .addMultipartParameter("ref_name1", et_ref_name1.getText().toString().trim())
                .addMultipartParameter("ref_address1", et_ref_address1.getText().toString().trim())
                .addMultipartParameter("ref_phone1", et_ref_mob1.getText().toString().trim())
                .addMultipartParameter("ref_email1", et_ref_email1.getText().toString().trim())

                .addMultipartParameter("ref_name2", et_ref_name2.getText().toString().trim())
                .addMultipartParameter("ref_address2", et_ref_address2.getText().toString().trim())
                .addMultipartParameter("ref_phone2", et_ref_mob2.getText().toString().trim())
                .addMultipartParameter("ref_email2", et_ref_email2.getText().toString().trim())

                .addMultipartParameter("ref_name3", et_ref_name3.getText().toString().trim())
                .addMultipartParameter("ref_address3", et_ref_address3.getText().toString().trim())
                .addMultipartParameter("ref_phone3", et_ref_mob3.getText().toString().trim())
                .addMultipartParameter("ref_email3", et_ref_email3.getText().toString().trim())

                // .addMultipartParameter("lat", String.valueOf(latitude))
                //.addMultipartParameter("lng", String.valueOf(longitude))


                .setTag("SignUp")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("sigup res contractor = ", "" + jsonObject);
                        Utils.dismissDialog();
                        try {
                            String result = jsonObject.getString("results");
                            //String status = jsonObject.getString("is_status");
                            String message = jsonObject.getString("msg");
                            // Log.e("status = ", status);

                            if (result.equalsIgnoreCase("true")) {
                                JSONObject job = jsonObject.getJSONObject("data");


                                UserInfoData user = new UserInfoData();
                                //  user.job_id = job.getString("user_id");
                                user.user_id = job.getString("CNT_ID");
                                user.FName = job.getString("FName");
                                user.LName = job.getString("LName");
                                user.Dob = job.getString("BirthDt");
                                user.Email = job.getString("Email");
                                user.Salt = job.getString("Salt");
                                user.Contact_Num = job.getString("Contact_Num");
                                user.Alternate_Num = job.getString("Alternate_Num");
                                user.Pic = job.getString("Pic");
                                user.ScanId_Proof = job.getString("ScanId_Proof");
                                user.Address = job.getString("Address");
                                user.Country = job.getString("Country");
                                user.City = job.getString("City");
                                user.State = job.getString("State");
                                user.PinCode = job.getString("PinCode");
                                user.Reference_Name1 = job.getString("Reference_Name1");
                                user.Reference_Address1 = job.getString("Reference_Address1");
                                user.Reference_Email1 = job.getString("Reference_Email1");
                                user.Reference_Phone1 = job.getString("Reference_Phone1");

                                user.Reference_Name2 = job.getString("Reference_Name2");
                                user.Reference_Address2 = job.getString("Reference_Address2");
                                user.Reference_Email2 = job.getString("Reference_Email2");
                                user.Reference_Phone2 = job.getString("Reference_Phone2");

                                user.Reference_Name3 = job.getString("Reference_Name3");
                                user.Reference_Address3 = job.getString("Reference_Address3");
                                user.Reference_Email3 = job.getString("Reference_Email3");
                                user.Reference_Phone3 = job.getString("Reference_Phone3");
                                user.Type = job.getString("Type");

                                session.createSession(user);

                                Utils.openAlertDialog(context, message);

                                Intent intent = new Intent(context, ActivityLogin.class);
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                ((Activity) context).finish();

                            } else Utils.openAlertDialog(context, message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("exp = ", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        ToastClass.showToast(context, error.toString());
                        Log.e("error = ", "" + error);
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
