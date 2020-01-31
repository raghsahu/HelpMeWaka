package com.helpmewaka.ui.customer.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helpmewaka.R;
import com.helpmewaka.ui.activity.Custom_Spinner_Model;
import com.helpmewaka.ui.activity.Service_FragModel.Custom_spin_FragAdapter;
import com.helpmewaka.ui.activity.Service_FragModel.SubCateFragAdapter;
import com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer;
import com.helpmewaka.ui.model.ServiceListData;
import com.helpmewaka.ui.model.StateData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.APIClient;
import com.helpmewaka.ui.server.Api_Call;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.PathUtils;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.Validation;
import com.helpmewaka.ui.util.helper.DateFarmateChange;
import com.helpmewaka.ui.util.helper.NetworkUtil;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer.Category_Id;
import static com.helpmewaka.ui.customer.activity.ActivityProfileDetailCustomer.SERVICE_id;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPostAnErrand extends Fragment implements View.OnClickListener , SubCateFragAdapter.AdapterCallback {

    SubCateFragAdapter subCateFragAdapter;
    private View view;
    private Context context;
    private ArrayList<ServiceListData> catList;
    private ArrayList<StateData> stateList;
    private Spinner spinner;
    private Spinner spinner_state;
    private String servie_id,servie_name,category_id,Category_title;
    private TextView tv_start_date, tv_end_date;
    public static TextView tv_spin_job;
    private Boolean isDateApply = false;
    private String serverDate;
    private Button btn_update;
    private LinearLayout ll_select_file, ll_file_name;
    private EditText et_city, et_reqest, et_phone, et_contact_person, et_additional_info;
    private TextView tv_country;
    private String state_id;
    private String user_id;
    private Session session;
    private File imgFile;
    private Dialog dialog;
    private LinearLayout ll_image, ll_doc;
    private TextView tv_file_name, tv_download;

    String url_file = "";
    File file;
    String dirPath, fileName;
    RecyclerView recycler_spin;
    public static Dialog NextQuizLevel_dialog;

    public FragmentPostAnErrand() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        view = inflater.inflate(R.layout.fragment_post_an_errand, container, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        subCateFragAdapter = new SubCateFragAdapter(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            servie_id = bundle.getString("SERVICE_ID");
            servie_name = bundle.getString("SERVICE_NAME");
            category_id = bundle.getString("Category_id");
            Category_title = bundle.getString("Category_title");
            Log.e("bundle servic_id ", servie_id);
            Log.e("bundle_servic_name ", servie_name);
        }



        initView();
        clickListner();
        return view;
    }


    private void initView() {
        catList = new ArrayList<>();
        stateList = new ArrayList<>();

        //Folder Creating Into Phone Storage
        dirPath = Environment.getExternalStorageDirectory() + "/PostImage";

        spinner = view.findViewById(R.id.spinner);
        tv_spin_job = view.findViewById(R.id.tv_spin_job);

        if(Category_title!=null && servie_name!=null){
            tv_spin_job.setText(Category_title+ "-> "+servie_name);

        }else {
            tv_spin_job.setText("-Select service-");
        }


        spinner_state = view.findViewById(R.id.spinner_state);
        tv_start_date = view.findViewById(R.id.tv_start_date);
        tv_end_date = view.findViewById(R.id.tv_end_date);
        et_city = view.findViewById(R.id.et_city);
        et_reqest = view.findViewById(R.id.et_reqest);
        et_phone = view.findViewById(R.id.et_phone);
        et_contact_person = view.findViewById(R.id.et_contact_person);
        et_additional_info = view.findViewById(R.id.et_additional_info);

        tv_country = view.findViewById(R.id.tv_country);

        ll_file_name = view.findViewById(R.id.ll_file_name);
        tv_file_name = view.findViewById(R.id.tv_file_name);
        tv_download = view.findViewById(R.id.tv_download);

        ll_select_file = view.findViewById(R.id.ll_select_file);
        btn_update = view.findViewById(R.id.btn_update);


        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                String url = API.BASE_URL + "home_services_list.php";
                //Spin_Get_Service(url);


            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));

        if (NetworkUtil.isNetworkConnected(context)) {
            try {
                String url = API.BASE_URL + "state_list.php";
                getStateApi(url);
            } catch (NullPointerException e) {
                ToastClass.showToast(context, getString(R.string.too_slow));
            }
        } else ToastClass.showToast(context, getString(R.string.no_internet_access));

   //******************************
        //************************tv_spin_onclick
        tv_spin_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NextQuizLevel_dialog = new Dialog(getActivity());
                NextQuizLevel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                NextQuizLevel_dialog.setCancelable(true);
                NextQuizLevel_dialog.setContentView(R.layout.custom_spin_dialog);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(NextQuizLevel_dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                NextQuizLevel_dialog.getWindow().setAttributes(lp);

                recycler_spin=NextQuizLevel_dialog.findViewById(R.id.recycler_spin);

                Spin_Get_ServiceRetro();


                try {
                    if (!getActivity().isFinishing()){
                        NextQuizLevel_dialog.show();
                    }
                }
                catch (WindowManager.BadTokenException e) {
                    //use a log message
                }
            }
        });


    }

    private void Spin_Get_ServiceRetro() {

        Utils.showDialog(getActivity(), "Loading Please Wait...");

        Api_Call apiInterface = APIClient.getClient().create(Api_Call.class);

        Call<Custom_Spinner_Model> call = apiInterface.GetHomeServiceDetails();

        call.enqueue(new Callback<Custom_Spinner_Model>() {
            @Override
            public void onResponse(Call<Custom_Spinner_Model> call, Response<Custom_Spinner_Model> response) {

                try{
                    Utils.dismissDialog();
                    if (response!=null){
                        Log.e("spin_result",""+response.body().getResults());

                        if (response.body().getResults().equalsIgnoreCase("true")){


                            Log.e("data_spin", ""+response.body().getData().size());
                            for (int i=0; i<response.body().getData().size(); i++){

                                Log.e("spin_sub_title", ""+response.body().getData().get(i).getServiceTitle());
                                // Log.e("spin_sub_data_size", ""+response.body().getData().get(i).getServieSubCate().size());

//                                for (int k=0; k<response.body().getData().get(i).getServieSubCate().size(); i++){
//
//                                    Log.e("spin_sub_data1", ""+response.body().getData().get(i)
//                                            .getServieSubCate().get(k).getServiceSubcategory());
//
//
//                                }

                            }

                            Custom_spin_FragAdapter custom_spin_FragAdapter = new Custom_spin_FragAdapter(response.body().getData(), getActivity());
                            recycler_spin.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            recycler_spin.setAdapter(custom_spin_FragAdapter);
                            //mAdapter.notifyDataSetChanged();


                            //*************************************
                        }else {
                            Toast.makeText(getActivity(), ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Log.e("error_Dr_login", e.getMessage());
                }
                // progressDialog.dismiss();
                Utils.dismissDialog();
            }

            @Override
            public void onFailure(Call<Custom_Spinner_Model> call, Throwable t) {
                // progressDialog.dismiss();
                Utils.dismissDialog();
                Log.e("error_spin",t.getMessage());
                //Toast.makeText(AllCountries.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void clickListner() {
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        ll_select_file.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        tv_download.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servie_id = catList.get(position).SERV_ID;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_id = stateList.get(position).STATE_ID;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Spin_Get_Service(String url) {

        Utils.showDialog(getContext(), "Loading Please Wait...");

        AndroidNetworking.get(url)
                .setTag("List")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response servicelist ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");
                            catList.clear();
                            catList.add(new ServiceListData("SELECT SERVICE"));
                            if (result.equalsIgnoreCase("true")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<ServiceListData>>() {
                                }.getType();
                                //json array
                                List<ServiceListData> data = gson.fromJson(jsonObject.getString("data"), listType);
                                //List<ProductData> data = (List<ProductData>) gson.fromJson(jsonObject.getString("Product_list").toString(), Products.class);
                                catList.addAll(data);

                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);


                            ArrayAdapter<ServiceListData> aa = new ArrayAdapter<ServiceListData>(context, R.layout.list_item_spinner1, catList) {
                                @NonNull
                                @Override
                                //set hint text by default in center
                                public View getView(int position, @Nullable View cView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, cView, parent);
                                    view.setPadding(0, view.getTop(), view.getRight(), view.getBottom());
                                    return view;
                                }
                            };
                            aa.setDropDownViewResource(R.layout.list_item_spinner1);
                            //Setting the ArrayAdapter data on the Spinner
                            spinner.setAdapter(aa);
                            Log.e("size service = ", "" + catList.size());

                            //set select category
                            for (int i = 0; i < catList.size(); i++) {
                                if (catList.get(i).SERV_ID.equalsIgnoreCase(servie_id)) {
                                    spinner.setSelection(i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();
                        Log.e("error", error.getErrorDetail());
                    }
                });
    }

    private void getStateApi(String url) {

        Utils.showDialog(getContext(), "Loading Please Wait...");

        AndroidNetworking.get(url)
                .setTag("List")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("response stateList ", "" + jsonObject);
                        try {
                            Utils.dismissDialog();

                            String msg = jsonObject.getString("msg");
                            String result = jsonObject.getString("results");
                            stateList.clear();
                            stateList.add(new StateData("SELECT STATE"));
                            if (result.equalsIgnoreCase("true")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<StateData>>() {
                                }.getType();
                                //json array
                                List<StateData> data = gson.fromJson(jsonObject.getString("data"), listType);
                                //List<ProductData> data = (List<ProductData>) gson.fromJson(jsonObject.getString("Product_list").toString(), Products.class);
                                stateList.addAll(data);

                            } //else Utils.openAlertDialog(ActivityNibblersDetail.context, result);


                            ArrayAdapter<StateData> aa = new ArrayAdapter<StateData>(context, R.layout.list_item_spinner1, stateList) {
                                @NonNull
                                @Override
                                //set hint text by default in center
                                public View getView(int position, @Nullable View cView, @NonNull ViewGroup parent) {
                                    View view = super.getView(position, cView, parent);
                                    view.setPadding(0, view.getTop(), view.getRight(), view.getBottom());
                                    return view;
                                }
                            };
                            aa.setDropDownViewResource(R.layout.list_item_spinner1);
                            //Setting the ArrayAdapter data on the Spinner
                            spinner_state.setAdapter(aa);
                            //Log.e("size service = ", "" + catList.size());

                            //set select category
                           /* for (int i = 0; i < catList.size(); i++) {
                                if (catList.get(i).SERV_ID.equalsIgnoreCase(servie_id)) {
                                    spinner_state.setSelection(i);
                                }
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();
                        Log.e("error", error.getErrorDetail());
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_date:
                Utils.hideSoftKeyboardfragment(context, view);
                isDateApply = true;
                getDate();
                break;

            case R.id.tv_end_date:
                Utils.hideSoftKeyboardfragment(context, view);
                isDateApply = false;
                getDate();
                break;

            case R.id.btn_update:
                Utils.hideSoftKeyboardfragment(context, view);
                checkValidation();
                break;

            case R.id.ll_select_file:
                Utils.hideSoftKeyboardfragment(context, view);
                openPickerDialog();

                break;

            case R.id.tv_download:
                Utils.hideSoftKeyboardfragment(context, view);
                url_file = API.BASE_URL_DOWNLOAD_IMG_CUST + imgFile.getName();
                Log.e("Download img", "link " + url_file + " filename " + "" + imgFile.getName());

                //file Creating With Folder & Fle Name
                file = new File(dirPath, imgFile.getName());

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                    ToastClass.showToast(context, "Need Permission to access storage for Downloading Image");
                } else {
                    //ToastClass.showToast(context, "Downloading Image...");
                    DownloadImageFromServer();
                }

                break;

            case R.id.ll_image:
                Utils.hideSoftKeyboardfragment(context, view);
                imagePick();

                break;

            case R.id.ll_doc:
                Utils.hideSoftKeyboardfragment(context, view);
                PickdocPdf();

                break;
        }
    }

    private void DownloadImageFromServer() {
        AndroidNetworking.download(url_file, dirPath, imgFile.getName())
                .build()
                .startDownload(new DownloadListener() {

                    @Override
                    public void onDownloadComplete() {

                        Toast.makeText(context, "DownLoad Complete...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError e) {
                        Log.e("Error ", "" + e);
                        Toast.makeText(context, "DownLoad Complete Page not found...", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void openPickerDialog() {
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_chooser);
        ll_image = dialog.findViewById(R.id.ll_image);
        ll_doc = dialog.findViewById(R.id.ll_doc);

        ll_image.setOnClickListener(this);
        ll_doc.setOnClickListener(this);

        dialog.show();

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

                    //img_profile.setImageBitmap(r.getBitmap());

                    Log.e("Imagepath", r.getPath());

                    imgFile = PathUtils.bitmapToFile(context, r.getBitmap());
                    Log.e("imgFile", "" + imgFile);

                    Log.e("fileName = ", imgFile.getName());
                    ll_file_name.setVisibility(View.VISIBLE);
                    tv_file_name.setText(imgFile.getName());
                    dialog.dismiss();
                    //r.getPath();
                } else {
                    //Handle possible errors
                    //TODO: do what you have to do with r.getError();
                    Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }).show((FragmentActivity) context);
    }

    private void PickdocPdf() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] extraMimeTypes = {"application/pdf", "application/doc"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 13);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // String path = "";
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 13) {


                if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                    // For JellyBean and above
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            // Do something with the URI
                            Log.e("loopUri img = ", "" + uri);

                            String path = PathUtils.getPath(context, uri);
                            imgFile = new File(path);
                            dialog.dismiss();
                            Log.e("fileName doc = ", imgFile.getName());
                            ll_file_name.setVisibility(View.VISIBLE);
                            tv_file_name.setText(imgFile.getName());


                        }
                    }
                    // For Ice Cream Sandwich

                } else {
                    Uri uri = data.getData();
                    // Do something with the URI

                    String path = PathUtils.getPath(context, uri);
                    imgFile = new File(path);
                    dialog.dismiss();
                    Log.e("fileName doc = ", imgFile.getName());
                    ll_file_name.setVisibility(View.VISIBLE);
                    tv_file_name.setText(imgFile.getName());
                    Log.e("else imgFile doc = ", "" + imgFile);
                    Log.e("else uri doc = ", "" + uri);
                    Log.e("else path doc = ", "" + path);
                }
            }
        }
    }


    private void checkValidation() {

        String addional_info = et_additional_info.getText().toString();
        String contact_person = et_contact_person.getText().toString();
        String phone = et_phone.getText().toString();
        String request = et_reqest.getText().toString();
        String city = et_city.getText().toString();
        String s_date = tv_start_date.getText().toString();
        String e_date = tv_end_date.getText().toString();

        try {
            if (SERVICE_id!=null && Category_Id!=null){
                servie_id=SERVICE_id;
                category_id=Category_Id;
                Log.e("service_id11", servie_id);
                Log.e("category_id11",category_id );
            }else {

            }
        }catch (Exception e){

        }

        Validation validation = new Validation(context);

//        if (spinner != null) {
//            if (spinner.getSelectedItem().toString().trim().equals("SELECT SERVICE")) {
//                ToastClass.showToast(context, getString(R.string.spinner_service_v));
//            } else if (spinner_state.getSelectedItem().toString().trim().equals("SELECT STATE")) {
//                ToastClass.showToast(context, getString(R.string.spinner_state_v));
//            } else
            if (!validation.isEmpty(city)) {
                ToastClass.showToast(context, getString(R.string.city_v));
                et_city.requestFocus();

            } else {

                if (NetworkUtil.isNetworkConnected(context)) {
                    try {
                        String url = API.BASE_URL + "customer_job_post.php";
                        CallPostJobApi(url,servie_id,category_id);
                        //ToastClass.showToast(context, "post....");
                    } catch (NullPointerException e) {
                        ToastClass.showToast(context, getString(R.string.too_slow));
                    }
                } else {
                    ToastClass.showToast(context, getString(R.string.no_internet_access));
                }
            }
       // }

    }

    private void getDate() {

        int day, month, year;
        Calendar c = Calendar.getInstance();

        if (isDateApply) {
            if (tv_start_date.getText().toString().trim().equals("")) {
                // Get Current Date

                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


            } else {
                //when edit then open calender select date
                String[] nameArray = tv_start_date.getText().toString().split("-");
                year = Integer.parseInt(nameArray[2]);
                month = Integer.parseInt(nameArray[1]) - 1;
                day = Integer.parseInt(nameArray[0]);
            }
        } else {
            if (tv_end_date.getText().toString().trim().equals("")) {
                // Get Current Date
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

            } else {
                //when edit then open calender select date
                String[] nameArray = tv_end_date.getText().toString().split("-");
                year = Integer.parseInt(nameArray[2]);
                month = Integer.parseInt(nameArray[1]) - 1;
                day = Integer.parseInt(nameArray[0]);
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.calender_dialog_theme, datePicker, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            selectedMonth += 1;
            String date = selectedDay + "-" + selectedMonth + "-" + selectedYear;

            //change date farmate 1/1/2018 to 01/01/2018
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d_date = null;
            try {
                d_date = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String strDate = dateFormat.format(d_date);


            Log.e("strDate=", strDate);
            //change date farmate dd-MM-yyyy to yyyy-MM-dd
            try {
                serverDate = DateFarmateChange.convertddMMyyyyToyyyyMMdd(strDate);
                Log.e("serverDate=", serverDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (isDateApply) {
                tv_start_date.setText(strDate);
            } else {
                tv_end_date.setText(strDate);
            }

        }

    };

    private void CallPostJobApi(String url, String servie_id, String category_id) {

        String addional_info = et_additional_info.getText().toString();
        String contact_person = et_contact_person.getText().toString();
        String phone = et_phone.getText().toString();
        String request = et_reqest.getText().toString();
        String city = et_city.getText().toString();
        String s_date = tv_start_date.getText().toString();
        String e_date = tv_end_date.getText().toString();

        Utils.showDialog(context, "Loading Please Wait...");

        AndroidNetworking.upload(url)
                .addMultipartFile("Image", imgFile)
                .addMultipartParameter("CLT_ID", user_id)
                .addMultipartParameter("servicelist", category_id)
                .addMultipartParameter("service_sub_cat_id", servie_id)
                .addMultipartParameter("contact_name", contact_person)
                .addMultipartParameter("contact_number", phone)
                .addMultipartParameter("special_request", request)
                .addMultipartParameter("service_state", state_id)
                .addMultipartParameter("service_city", city)
                .addMultipartParameter("jobstrt_date", s_date)
                .addMultipartParameter("jobend_date", e_date)
                .addMultipartParameter("paymentstatus", "UNPAID")
                .addMultipartParameter("service_requested", "")
                .setTag("user")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Utils.hideProgress(mdialog);
                        Utils.dismissDialog();

                        Log.e("response postjob cus=", "" + jsonObject);
                        try {

                            //JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String results = jsonObject.getString("results");

                            if (results.equalsIgnoreCase("true")) {
                                JSONObject userdetail = jsonObject.getJSONObject("data");
                                ToastClass.showToast(context, message);
                                Intent intent = new Intent(context, ActivityProfileDetailCustomer.class);
                                intent.putExtra("DASHBOARD","DASHBOARD");
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                ((Activity) context).finish();

                            } else {
                                //Utils.openAlertDialog(context, message);
                                ToastClass.showToast(context, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error cus postjob", "" + e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Utils.dismissDialog();
                        Log.e("Error cus postjob", "" + error);
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onMethodCallback(String SERVICE_ID, String category_Id, String SERVICE_name, String category_title) {

        servie_id=SERVICE_ID;
        category_id=category_Id;
        servie_name=SERVICE_name;
        tv_spin_job.setText(servie_name);

        Log.e("ser1_id", servie_id);
        Log.e("category_id", category_id);


    }
}
