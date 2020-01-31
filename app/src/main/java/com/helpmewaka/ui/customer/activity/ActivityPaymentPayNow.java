package com.helpmewaka.ui.customer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.Config.PayPalConfig;
import com.helpmewaka.ui.activity.common.ActivityTermCondition;
import com.helpmewaka.ui.activity.common.ConfirmationActivity;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.Utils;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class ActivityPaymentPayNow extends AppCompatActivity {

    Button btn_paynow;
    TextView tv_amount,tv_t_c;
    String Payment_amt;
    CheckBox check_t_c;
    ImageView iv_back;
     String JRT_ID,JOB_ID;
    private Session session;
    private String user_id;
    public static final int PAYPAL_REQUEST_CODE = 123;

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            //.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pay_now);

        session = new Session(ActivityPaymentPayNow.this);
        user_id = session.getUser().user_id;

        btn_paynow=findViewById(R.id.btn_paynow);
        tv_amount=findViewById(R.id.tv_amount);
        check_t_c=findViewById(R.id.check_t_c);
        tv_t_c=findViewById(R.id.tv_t_c);
        iv_back=findViewById(R.id.iv_back);



        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);


        try {
            if (getIntent()!=null){
                Payment_amt=getIntent().getStringExtra("Payment_amt");
                JRT_ID=getIntent().getStringExtra("JRT_ID");
                JOB_ID=getIntent().getStringExtra("JOB_ID");
                tv_amount.setText(Payment_amt);
            }
        }catch (Exception e){

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check_t_c.isChecked()){
                    //*********call paypal gateway

                    CallPaypal();

                }else {
                    Toast.makeText(ActivityPaymentPayNow.this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_t_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Intent intent=new Intent(ActivityPaymentPayNow.this, ActivityTermCondition.class);
              startActivity(intent);
            // finish();

            }
        });


    }

    private void CallPaypal() {

       // paymentAmount = editTextAmount.getText().toString();

        //Creating a paypalpayment                                          Payment_amt
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(Payment_amt)),
                "USD", "Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);


        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);
                        Toast.makeText(this, "Payment Success", Toast.LENGTH_LONG).show();
                        //Starting a new activity for the payment details and also putting the payment details with intent

                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);

                            String trns_id=jsonDetails.getJSONObject("response").getString("id");
                            String status=jsonDetails.getJSONObject("response").getString("state");

                         callPayment_HistoryApi(trns_id,status,paymentDetails,Payment_amt);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void callPayment_HistoryApi(String trns_id, String status, final String paymentDetails, final String payment_amt) {

        Utils.showDialog(this, "Loading Please Wait...");
        AndroidNetworking.post(API.BASE_URL + "customer_payment_multi_milstone.php")
                .addBodyParameter("JOB_ID", JOB_ID)
                .addBodyParameter("CLT_ID", user_id)
                .addBodyParameter("JRT_ID", JRT_ID)
                .addBodyParameter("txn_id", trns_id)
                .addBodyParameter("payment_status", status)
                .addBodyParameter("payment_amount", payment_amt)

                .setTag("membershipApp job")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Utils.dismissDialog();
                        Log.e("memberAppUser_resp = ", "" + jsonObject);
                        try {
                            String message = jsonObject.getString("msg");
                            String result = jsonObject.getString("result");
                            // String is_status = jsonObject.getString("is_status");

                            if (result.equalsIgnoreCase("true")) {
                                Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ActivityPaymentPayNow.this, ConfirmationActivity.class)
                                        .putExtra("PaymentDetails", paymentDetails)
                                        .putExtra("PaymentAmount", payment_amt));
                            } else {
                                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("exception = ", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.e("error = ", "" + error);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
