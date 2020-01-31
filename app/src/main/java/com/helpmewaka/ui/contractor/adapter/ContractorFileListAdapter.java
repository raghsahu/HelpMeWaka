package com.helpmewaka.ui.contractor.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.helpmewaka.R;
import com.helpmewaka.ui.contractor.model.FileListContractData;
import com.helpmewaka.ui.customer.adapter.UploadFileListAdapter;
import com.helpmewaka.ui.model.FileListData;
import com.helpmewaka.ui.server.API;
import com.helpmewaka.ui.util.ToastClass;

import java.io.File;
import java.util.List;

public class ContractorFileListAdapter extends RecyclerView.Adapter<ContractorFileListAdapter.ViewHolder> {
    private List<FileListContractData> fList;
    private FileListContractData fileData;
    private Context context;
    String url = "";
    File file;
    String dirPath, fileName;

    public ContractorFileListAdapter(List<FileListContractData> fList, Context context) {
        this.fList = fList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_file, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (fList.size() > 0) {
            fileData = fList.get(position);
            holder.tv_title.setText(fileData.Attachment);
            holder.tv_date_time.setText("Uploaded" + " | " + fileData.UploadDt);


            /*if (!FileListData.image.equalsIgnoreCase(null)){

                Picasso.with(context).load(FileListData.image).fit().centerCrop()
                        .placeholder(R.drawable.doctor)
                        .error(R.drawable.doctor)
                        .into(holder.img_profile);
            }*/
        }
    }

    @Override
    public int getItemCount() {
        return fList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_date_time, tv_download;


        public ViewHolder(View parent) {
            super(parent);
            tv_title = parent.findViewById(R.id.tv_title);
            tv_date_time = parent.findViewById(R.id.tv_date_time);
            tv_download = parent.findViewById(R.id.tv_download);

            //Folder Creating Into Phone Storage
            dirPath = Environment.getExternalStorageDirectory() + "/ImageR";

            tv_download.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_download) {
                int pos = getAdapterPosition();
                fileData = fList.get(pos);
                fileName = fileData.Attachment;
                url = API.BASE_URL_DOWNLOAD_IMG_CONTRACTOR + fileData.Attachment;
                Log.e("Download img", "link " + url + " filename " + "" + fileName);

                //file Creating With Folder & Fle Name
                file = new File(dirPath, fileName);

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                    ToastClass.showToast(context, "Need Permission to access storage for Downloading Image");
                } else {
                    //ToastClass.showToast(context, "Downloading Image...");
                    DownloadImageFromServer();
                }


            }
        }

        private void DownloadImageFromServer() {
            AndroidNetworking.download(url, dirPath, fileName)
                    .build()
                    .startDownload(new DownloadListener() {

                        @Override
                        public void onDownloadComplete() {

                            Toast.makeText(context, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(ANError e) {
                            Log.e("Error ", "" + e);

                        }
                    });

        }
    }

}
