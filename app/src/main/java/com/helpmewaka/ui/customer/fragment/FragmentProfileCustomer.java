package com.helpmewaka.ui.customer.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.contractor.activity.ActivityDegreeSkills;
import com.helpmewaka.ui.contractor.fragment.FragmentBilling;
import com.helpmewaka.ui.model.UserInfoData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.PathUtils;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.NetworkUtil;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer.rl_header;
import static com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer.tv_title;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfileCustomer extends Fragment implements View.OnClickListener {
    private Context context;
    private View view;
    private TextView tv_change_pass;
    private Dialog dialog;
    private EditText et_old_password, et_new_password, et_Cpassword;
    private Button btn_update_pass,btn_billing;
    private ImageView iv_profile, iv_edit_profile;
    private File imgFile;
    private Button btn_update;
    private EditText et_fName, et_lname, et_alr_phone;
    private String user_id;
    private Session session;
    private TextView tv_email, tv_phone;

    public FragmentProfileCustomer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_customer, container, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        try {
            rl_header.setVisibility(View.GONE);
            //tv_title.setText("profile");
        }catch (Exception e){}

        initView();
        clickListner();


        return view;
    }


    private void initView() {
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        et_fName = view.findViewById(R.id.et_fName);
        et_lname = view.findViewById(R.id.et_lname);
        et_alr_phone = view.findViewById(R.id.et_alr_phone);
        iv_profile = view.findViewById(R.id.iv_profile);
        iv_edit_profile = view.findViewById(R.id.iv_edit_profile);
        tv_change_pass = view.findViewById(R.id.tv_change_pass);
        btn_update = view.findViewById(R.id.btn_update);
        btn_billing = view.findViewById(R.id.btn_billing);


        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                String get_url = API.BASE_URL + "customer_profile.php";
                GetProfileApi(get_url);

            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else {
            ToastClass.showToast(context, getString(R.string.no_internet_access));
        }
    }

    private void clickListner() {
        iv_edit_profile.setOnClickListener(this);
        tv_change_pass.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_billing.setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_change_pass:
                openChangePassDialog();
                break;

            case R.id.btn_update_pass:
                dialog.dismiss();
                break;
            case R.id.iv_edit_profile:
                imagePick();
                break;

            case R.id.btn_update:
                checkValidation();
                break;

            case R.id.btn_billing:
                FragmentBilling bill = new FragmentBilling();
                replaceFragment(bill, true, R.id.frame_layout);

                break;
        }
    }

    //*********fragment replace*****
    @SuppressLint("WrongConstant")
    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        String backStackName = fragment.getClass().getName();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int i = fm.getBackStackEntryCount();
        while (i > 0) {
            fm.popBackStackImmediate();
            i--;
        }
        boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStackName).setTransition(FragmentTransaction.TRANSIT_UNSET);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }




    private void GetProfileApi(String get_url) {

        Utils.showDialog(context, "Loading Please Wait...");

        AndroidNetworking.post(get_url)

                .addBodyParameter("CLT_ID", user_id)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("res getProfile cus=", "" + jsonObject);
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
                                user.Dob = userdetail.getString("Dob");
                                user.Pic = userdetail.getString("Pic");
                                user.Address = userdetail.getString("Address");
                                user.Country = userdetail.getString("Country");
                                user.City = userdetail.getString("City");
                                user.State = userdetail.getString("State");
                                user.PinCode = userdetail.getString("PinCode");
                                //user.lat = userdetail.getString("lat");
                                //user.lon = userdetail.getString("lon");
                                //user.register_id = userdetail.getString("register_id");
                                setData(user);

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
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

    private void setData(UserInfoData user) {
        et_fName.setText(user.FName);
        et_lname.setText(user.LName);
        tv_email.setText(user.Email);
        tv_phone.setText(user.Contact_Num);
        et_alr_phone.setText(user.Alternate_Num);


        if (!user.Pic.equalsIgnoreCase(null) && !user.Pic.equalsIgnoreCase("")) {

            Picasso.with(context).load(API.BASE_URL_IMG_CUST + user.Pic).fit().centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(iv_profile);
        }
    }

    private void checkValidation() {

        String fname = et_fName.getText().toString().trim();
        String lname = et_lname.getText().toString().trim();


        Validation validation = new Validation(context);

        if (!validation.isEmpty(fname)) {
            ToastClass.showToast(context, getString(R.string.fname_null));
            et_fName.requestFocus();

        } else if (!validation.isEmpty(lname)) {
            ToastClass.showToast(context, getString(R.string.lname_null));
            et_lname.requestFocus();

        } else {
            if (NetworkUtil.isNetworkConnected(context)) {
                try {
                    String url = API.BASE_URL + "update_customer_profile.php";
                    CallUpdateApi(url);

                } catch (NullPointerException e) {
                    ToastClass.showToast(context, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(context, getString(R.string.no_internet_access));
            }
        }
    }

    private void CallUpdateApi(String url) {

        Utils.showDialog(context, "Loading Please Wait...");

        AndroidNetworking.post(url)

                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("fname", et_fName.getText().toString())
                .addBodyParameter("lname", et_lname.getText().toString())
                .addBodyParameter("custmerAlterNo", et_alr_phone.getText().toString())
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("response update cus=", "" + jsonObject);
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
                                user.Dob = userdetail.getString("Dob");
                                user.Pic = userdetail.getString("Pic");
                                user.Address = userdetail.getString("Address");
                                user.Country = userdetail.getString("Country");
                                user.City = userdetail.getString("City");
                                user.State = userdetail.getString("State");
                                user.PinCode = userdetail.getString("PinCode");
                                //user.lat = userdetail.getString("lat");
                                //user.lon = userdetail.getString("lon");
                                //user.register_id = userdetail.getString("register_id");


                                session.createSession(user);

                                ToastClass.showToast(context, message);

                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                ((Activity) context).finish();

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus update", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error cus update", "" + error);
                    }
                });
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


                    imgFile = PathUtils.bitmapToFile(context, r.getBitmap());
                    Log.e("imgFileProfile", "" + imgFile);

                    if (imgFile != null) {
                        if (NetworkUtil.isNetworkConnected(context)) {
                            try {
                                String url = API.BASE_URL + "customer_uploadprofile.php";
                                uploadProfileImage(url);

                            } catch (NullPointerException e) {
                                ToastClass.showToast(context, getString(R.string.too_slow));
                            }
                        } else {
                            ToastClass.showToast(context, getString(R.string.no_internet_access));
                        }
                    }


                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(getActivity(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show((FragmentActivity) context);
    }

    private void uploadProfileImage(String url) {

        Utils.showDialog(context, "Loading Please Wait...");

        AndroidNetworking.upload(url)
                .addMultipartFile("photoimg", imgFile)
                .addMultipartParameter("CLT_ID", user_id)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("res Imgupdate cus=", "" + jsonObject);
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
                                user.Dob = userdetail.getString("Dob");
                                user.Pic = userdetail.getString("Pic");
                                user.Address = userdetail.getString("Address");
                                user.Country = userdetail.getString("Country");
                                user.City = userdetail.getString("City");
                                user.State = userdetail.getString("State");
                                user.PinCode = userdetail.getString("PinCode");
                                //user.lat = userdetail.getString("lat");
                                //user.lon = userdetail.getString("lon");
                                //user.register_id = userdetail.getString("register_id");

                                session.createSession(user);

                                ToastClass.showToast(context, message);


                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus uploadImg", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error cus uploadImg", "" + error);
                    }
                });
    }

    private void openChangePassDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_change_pass);
        et_old_password = dialog.findViewById(R.id.et_old_password);
        et_new_password = dialog.findViewById(R.id.et_new_password);
        et_Cpassword = dialog.findViewById(R.id.et_Cpassword);
        btn_update_pass = dialog.findViewById(R.id.btn_update_pass);

        btn_update_pass.setOnClickListener(this);

        dialog.show();
    }
}
