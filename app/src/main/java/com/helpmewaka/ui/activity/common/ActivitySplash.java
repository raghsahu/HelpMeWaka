package com.helpmewaka.ui.activity.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.github.florent37.viewanimator.ViewAnimator;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.MainActivity;
import com.helpmewaka.ui.contractor.activity.ActivityProfileDetailContractor;
import com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ActivitySplash extends AppCompatActivity {

    private ImageView ActivitySplash_logo_image;
    private Session session;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new Session(this);
        ActivitySplash_logo_image = findViewById(R.id.splash_logo_image);

//        Glide.with(this)
//                .load(R.drawable.splash_logo)
//                .centerCrop()
//                .placeholder(R.drawable.splash_logo)
//                .into(ActivitySplash_logo_image);
//        printHashKey();

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[4])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[5])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[6])
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);

                // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
            } else {

//                ViewAnimator
//                        .animate(ActivitySplash_logo_image)
//                        .translationY(-1000, 0)
//                        .alpha(0, 1)
//                        .thenAnimate(ActivitySplash_logo_image)
//                        .scale(1f, 0.5f, 1f)
//                        .accelerate()
//                        .duration(1000)
//                        .start();
                
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = null;
                        //intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                        //intent = new Intent(ActivityActivitySplash.this, ActivityNavigation.class);
                        Animatoo.animateFade(ActivitySplash.this);
                        if (session.isLoggedIn()) {

                            if (session.getUser().user_id != null && !session.getUser().user_id.equalsIgnoreCase("")) {


                                if (session.getUser().Type.equalsIgnoreCase("contractor")) {
                                    intent = new Intent(ActivitySplash.this, ActivityProfileDetailContractor.class);
                                    intent.putExtra("SPLASH","SPLASH");
                                } else if (session.getUser().Type.equalsIgnoreCase("customer")) {
                                    intent = new Intent(ActivitySplash.this, MainActivity.class);
                                }
                                //intent = new Intent(ActivityActivitySplash.this, ActivityLogin.class);

                                Animatoo.animateFade(ActivitySplash.this);
                            } else ToastClass.showToast(ActivitySplash.this, "user not exist !");

                        } else {
                            intent = new Intent(ActivitySplash.this, ActivityWelCome.class);
                            //intent = new Intent(ActivityActivitySplash.this, ActivityNavigation.class);
                            Animatoo.animateFade(ActivitySplash.this);
                        }

                        startActivity(intent);
                        finish();

                    }
                }, 6000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception", "" + e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code", "" + requestCode);
        System.out.println(grantResults[0] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[1] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[2] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[3] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[4] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[5] == PackageManager.PERMISSION_GRANTED);
        System.out.println(grantResults[6] == PackageManager.PERMISSION_GRANTED);


        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 7 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == PackageManager.PERMISSION_GRANTED
            ) {

//                ViewAnimator
//                        .animate(ActivitySplash_logo_image)
//                        .translationY(-1000, 0)
//                        .alpha(0, 1)
//                        .thenAnimate(ActivitySplash_logo_image)
//                        .scale(1f, 0.5f, 1f)
//                        .accelerate()
//                        .duration(1000)
//                        .start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = null;
                        //intent = new Intent(ActivitySplash.this, ActivityLogin.class);
                        //intent = new Intent(ActivityActivitySplash.this, ActivityNavigation.class);
                        Animatoo.animateFade(ActivitySplash.this);

                        if (session.isLoggedIn()) {
                            if (session.getUser().user_id != null && !session.getUser().user_id.equalsIgnoreCase("")) {

                                intent = new Intent(ActivitySplash.this, MainActivity.class);
                                //intent = new Intent(ActivityActivitySplash.this, ActivityLogin.class);
                                Animatoo.animateFade(ActivitySplash.this);
                            } else ToastClass.showToast(ActivitySplash.this, "user not exist !");
                        } else {
                            intent = new Intent(ActivitySplash.this, ActivityWelCome.class);
                            //intent = new Intent(ActivityActivitySplash.this, ActivityNavigation.class);
                            Animatoo.animateFade(ActivitySplash.this);
                        }

                        startActivity(intent);
                        finish();

                    }
                }, 4000);

            } else {
                Toast.makeText(ActivitySplash.this, "Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.helpmewaka",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
