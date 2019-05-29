package com.krooms.hostel.rental.property.app.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.krooms.hostel.rental.property.app.asynctask.DownloadRCUFromServiceAsyncTask;
import com.krooms.hostel.rental.property.app.util.WebUrls;
import com.krooms.hostel.rental.property.app.callback.ServiceResponce;
import com.krooms.hostel.rental.property.app.R;
import com.krooms.hostel.rental.property.app.common.FileDownloader;
import com.krooms.hostel.rental.property.app.common.PrintDialogActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class DownloadRcuFrom extends AppCompatActivity implements ServiceResponce {

    String property_id;
    String user_id;
    String table_id;
    Boolean isPrintActivieted = false;
    private Button printBtn, viewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_rcu_from);

        printBtn = (Button) findViewById(R.id.printBtn);
        viewBtn = (Button) findViewById(R.id.viewBtn);
        property_id = getIntent().getExtras().getString("property_id");
        user_id = getIntent().getExtras().getString("user_id");
        table_id = getIntent().getExtras().getString("table_id");

        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrintActivieted = true;
                downLoadRCU();
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrintActivieted = false;
                downLoadRCU();
            }
        });


    }

    public void downLoadRCU() {

        DownloadRCUFromServiceAsyncTask serviceAsyncTask = new DownloadRCUFromServiceAsyncTask(this);
        serviceAsyncTask.setCallBack(this);
        serviceAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, table_id, property_id, user_id, "Police Station");

    }

    @Override
    public void requestResponce(String result) {


        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString(WebUrls.STATUS_JSON);
            if (status.equals("true")) {
                 String pdf = WebUrls.IMG_URL + "" + jsonObject.getString(WebUrls.DATA_JSON);
                new DownloadFile().execute(pdf, "rcu_form" + System.currentTimeMillis() + ".pdf");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

    }

    public void view(String pdfName) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() +
                "/Krooms/" + pdfName);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(DownloadRcuFrom.this,
                    "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public void print(String pdfName) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() +
                "/Krooms/" + pdfName);  // -> filename = maven.pdf
        Intent printIntent = new Intent(this, PrintDialogActivity.class);
        printIntent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
        printIntent.putExtra("title", pdfName);
        startActivity(printIntent);
    }

    private class DownloadFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Krooms");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return fileName;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if (isPrintActivieted) {
                print(aVoid);
            } else {
                view(aVoid);
            }
        }
    }


}