package com.helpmewaka.ui.contractor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.activity.ActivityDegreeSkills;
import com.helpmewaka.ui.contractor.model.SkillsListData;
import com.helpmewaka.ui.contractor.model.SkillsListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.server.Session;
import com.helpmewaka.ui.util.ToastClass;
import com.helpmewaka.ui.util.Utils;
import com.helpmewaka.ui.util.helper.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ravindra Birla on 20/09/2019.
 */
public class SkillsListAdapter extends RecyclerView.Adapter<SkillsListAdapter.ViewHolder> {
    private List<SkillsListData> skillsList;
    private SkillsListData SkillsListData;
    private Context context;
    private Session session;
    private String user_id;

    public SkillsListAdapter(List<SkillsListData> skillsList, Context context) {
        this.skillsList = skillsList;
        this.context = context;
    }

    @NonNull
    @Override
    public SkillsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_skills, parent, false);
        session = new Session(context);
        user_id = session.getUser().user_id;
        return new SkillsListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsListAdapter.ViewHolder holder, final int position) {
        if (skillsList.size() > 0) {
            SkillsListData = skillsList.get(position);
            holder.tv_skill.setText(SkillsListData.Skills);



            /*if (!SkillsListData.image.equalsIgnoreCase(null)){

                Picasso.with(context).load(SkillsListData.image).fit().centerCrop()
                        .placeholder(R.drawable.doctor)
                        .error(R.drawable.doctor)
                        .into(holder.img_profile);
            }*/


            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SkillsListData = skillsList.get(position);

                    if (NetworkUtil.isNetworkConnected(context)) {
                        try {
                            String url = API.BASE_URL + "delete_contractor_skills.php";
                            DeleteSkillsApi(url, SkillsListData);
                        } catch (NullPointerException e) {
                            ToastClass.showToast(context, context.getString(R.string.too_slow));
                        }
                    } else {
                        ToastClass.showToast(context, context.getString(R.string.no_internet_access));
                    }


                }

                private void DeleteSkillsApi(String url, final SkillsListData skillsListData) {

                    Utils.showDialog(context, "Loading Please Wait...");
                    AndroidNetworking.post(url)

                            .addBodyParameter("CNT_ID", user_id)
                            .addBodyParameter("SKL_ID", skillsListData.SKL_ID)
                            .setTag("user")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    // Utils.hideProgress(mdialog);
                                    Utils.dismissDialog();

                                    Log.e("res Delskills contract=", "" + jsonObject);
                                    try {

                                        //JSONObject jsonObject = new JSONObject(response);
                                        String message = jsonObject.getString("msg");
                                        String results = jsonObject.getString("result");

                                        if (results.equalsIgnoreCase("true")) {
                                            skillsList.remove(position);
                                            ToastClass.showToast(context, message);
                                            notifyDataSetChanged();
                                        } else {
                                            //Utils.openAlertDialog(context, message);
                                            ToastClass.showToast(context, message);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("Error Degree", "" + e);
                                    }
                                }

                                @Override
                                public void onError(ANError error) {
                                    Utils.dismissDialog();
                                    Log.e("Error Degree", "" + error);
                                }
                            });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return skillsList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_skill;
        public ImageView iv_delete;


        public ViewHolder(View parent) {
            super(parent);
            tv_skill = parent.findViewById(R.id.tv_skill);
            iv_delete = parent.findViewById(R.id.iv_delete);
        }
    }
}
