package com.helpmewaka.ui.contractor.fragment;

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.model.BillingData;
import com.helpmewaka.ui.model.UserInfoData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer.rl_header;
import static com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer.tv_title;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBilling extends Fragment {
    private Context context;
    private View view;
    private EditText et_address, et_city, et_state, et_zip, et_country;
    private Button btn_update;
    private Session session;
    private String user_id;
    private String login_type;

    public FragmentBilling() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_billing, container, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        login_type = session.getUser().Type;
        try {
            rl_header.setVisibility(View.VISIBLE);
            tv_title.setText("Billing");
        }catch (Exception e){

        }

        initView();
        clickListner();
        return view;
    }


    private void initView() {
        et_address = view.findViewById(R.id.et_address);
        et_city = view.findViewById(R.id.et_city);
        et_state = view.findViewById(R.id.et_state);
        et_zip = view.findViewById(R.id.et_zip);
        et_country = view.findViewById(R.id.et_country);
        btn_update = view.findViewById(R.id.btn_update);

        if (login_type.equalsIgnoreCase("contractor")) {

            if (NetworkUtil.isNetworkConnected(context)) {
                try {
                    String url = API.BASE_URL + "get_contractor_billing.php";
                    GetBillContractApi(url);

                } catch (NullPointerException e) {
                    ToastClass.showToast(context, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(context, getString(R.string.no_internet_access));
            }

        } else if (login_type.equalsIgnoreCase("customer")) {
            if (NetworkUtil.isNetworkConnected(context)) {
                try {
                    String url = API.BASE_URL + "get_cutomer_billing.php";
                    GetBillCustomerApi(url);

                } catch (NullPointerException e) {
                    ToastClass.showToast(context, getString(R.string.too_slow));
                }
            } else {
                ToastClass.showToast(context, getString(R.string.no_internet_access));
            }
        }


    }

    private void clickListner() {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideSoftKeyboardfragment(context, view);
                checkValidation();
            }
        });
    }


    private void checkValidation() {

        String address = et_address.getText().toString().trim();
        String city = et_city.getText().toString().trim();
        String state = et_state.getText().toString().trim();
        String zip = et_zip.getText().toString().trim();
        String country = et_country.getText().toString().trim();


        Validation validation = new Validation(context);

        if (!validation.isEmpty(address)) {
            ToastClass.showToast(context, getString(R.string.address_v));
            et_address.requestFocus();

        } else if (!validation.isEmpty(city)) {
            ToastClass.showToast(context, getString(R.string.city_v));
            et_city.requestFocus();

        } else if (!validation.isEmpty(state)) {
            ToastClass.showToast(context, getString(R.string.state_v));
            et_state.requestFocus();

        } else if (!validation.isEmpty(zip)) {
            ToastClass.showToast(context, getString(R.string.zip_v));
            et_zip.requestFocus();

        } else if (!validation.isEmpty(country)) {
            ToastClass.showToast(context, getString(R.string.country_v));
            et_country.requestFocus();

        } else {

            if (login_type.equalsIgnoreCase("contractor")) {

                if (NetworkUtil.isNetworkConnected(context)) {
                    try {
                        String url = API.BASE_URL + "update_contractor_billing.php";
                        BillUpdateContractApi(url);

                    } catch (NullPointerException e) {
                        ToastClass.showToast(context, getString(R.string.too_slow));
                    }
                } else {
                    ToastClass.showToast(context, getString(R.string.no_internet_access));
                }

            } else if (login_type.equalsIgnoreCase("customer")) {
                if (NetworkUtil.isNetworkConnected(context)) {
                    try {
                        String url = API.BASE_URL + "update_customer_billing.php";
                        BillUpdateCustomerApi(url);

                    } catch (NullPointerException e) {
                        ToastClass.showToast(context, getString(R.string.too_slow));
                    }
                } else {
                    ToastClass.showToast(context, getString(R.string.no_internet_access));
                }
            }


        }
    }

    private void BillUpdateCustomerApi(String url) {

        Utils.showDialog(context, "Loading Please Wait...");


        AndroidNetworking.post(url)
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("customeraddress", et_address.getText().toString())
                .addBodyParameter("customercity", et_city.getText().toString())
                .addBodyParameter("customerstate", et_state.getText().toString())
                .addBodyParameter("customerpostalcode", et_zip.getText().toString())
                .addBodyParameter("customercountry", et_country.getText().toString())
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("res Billupdate cus=", "" + jsonObject);
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

                                ToastClass.showToast(context, message);
                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus Billupdate", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error cus Billupdate", "" + error);
                    }
                });
    }

    private void GetBillCustomerApi(String url) {

        Utils.showDialog(context, "Loading Please Wait...");


        AndroidNetworking.post(url)
                .addBodyParameter("CLT_ID", user_id)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("res getBill cust=", "" + jsonObject);
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String results = jsonObject.getString("results");

                            if (results.equalsIgnoreCase("true")) {
                                JSONObject userdetail = jsonObject.getJSONObject("data");
                                BillingData user = new BillingData();
                                user.CLT_ID = userdetail.getString("CLT_ID");
                                user.Address = userdetail.getString("Address");
                                user.Country = userdetail.getString("Country");
                                user.City = userdetail.getString("City");
                                user.State = userdetail.getString("State");
                                user.PinCode = userdetail.getString("PinCode");
                                user.Type = userdetail.getString("Type");
                                setData(user);


                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus getbill", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error getcus bill", "" + error);
                    }
                });
    }

    private void GetBillContractApi(String url) {

        Utils.showDialog(context, "Loading Please Wait...");


        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("res getBill contr=", "" + jsonObject);
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String results = jsonObject.getString("results");

                            if (results.equalsIgnoreCase("true")) {
                                JSONObject userdetail = jsonObject.getJSONObject("data");
                                BillingData user = new BillingData();
                                user.CNT_ID = userdetail.getString("CNT_ID");
                                user.Address = userdetail.getString("Address");
                                user.Country = userdetail.getString("Country");
                                user.City = userdetail.getString("City");
                                user.State = userdetail.getString("State");
                                user.PinCode = userdetail.getString("PinCode");
                                user.Type = userdetail.getString("Type");
                                setData(user);


                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus getBill", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error cus getBill", "" + error);
                    }
                });
    }

    private void setData(BillingData user) {
        try {
            et_address.setText(user.Address);
            et_city.setText(user.City);
            et_state.setText(user.State);
            et_zip.setText(user.PinCode);
            et_country.setText(user.Country);
        } catch (Exception e) {
            Log.e("Excp setData Bill", "" + e);
        }
    }

    private void BillUpdateContractApi(String url) {

        Utils.showDialog(context, "Loading Please Wait...");


        AndroidNetworking.post(url)
                .addBodyParameter("CNT_ID", user_id)
                .addBodyParameter("contractoraddress", et_address.getText().toString())
                .addBodyParameter("contractorcity", et_city.getText().toString())
                .addBodyParameter("contractorstate", et_state.getText().toString())
                .addBodyParameter("contractorpostalcode", et_zip.getText().toString())
                .addBodyParameter("contractorcountry", et_country.getText().toString())
                .setTag("update")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("resp update contract=", "" + jsonObject);
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
                                ToastClass.showToast(context, message);
                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error contr updtebill", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error contr updtebill", "" + error);
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
