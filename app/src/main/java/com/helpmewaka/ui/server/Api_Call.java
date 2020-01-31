package com.helpmewaka.ui.server;

import com.helpmewaka.ui.activity.Custom_Spinner_Model;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Raghvendra Sahu on 29/11/2019.
 */
public interface Api_Call {

    @GET("home_services_list.php")
     Call<Custom_Spinner_Model> GetHomeServiceDetails();
}
