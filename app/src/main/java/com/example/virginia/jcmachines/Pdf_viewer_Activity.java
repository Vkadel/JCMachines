package com.example.virginia.jcmachines;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.virginia.jcmachines.utils.SendALongToast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Pdf_viewer_Activity extends AppCompatActivity {
    ConnectivityManager.NetworkCallback networkCallback;
    private Activity mActivity;
    private PDFView pdfView;
    private ProgressBar progressBar;
    String mFileName;
    FloatingActionButton downloadFAB;
    private String TAG="Pdf_viewer_Activity: ";
    public static String ARG_LINK="arg_link";
    public static String ARG_MACHINE_ID="machine_id";
    public static String ARG_DOCUMENT_ID="document_type";
    //Document Types Arguments
    public static String ARG_DOCUMENT_TYPE_LUBRICATION_CHART="lubrication_chart";
    public static String ARG_DOCUMENT_TYPE_TECHNICAL_SHEET="technical_sheet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        setContentView(R.layout.activity_pdf_viewer);
        pdfView=findViewById(R.id.pdfView);
        progressBar=findViewById(R.id.progressBar);
        downloadFAB= findViewById(R.id.floatingActionButtonDownload);
        TextView messageTV= findViewById(R.id.message_tv);
        AsyncTask mAs = null;
        downloadFAB.hide();

        //getting the link from the intent
        String link=getIntent().getStringExtra(ARG_LINK);

        //CreateFileName: @ARG_DOCUMENT_ID+@ARG_MACHINE_ID
        mFileName=getIntent().getStringExtra(ARG_DOCUMENT_ID)
                +getIntent().getIntExtra(ARG_MACHINE_ID,0);
        String filePath=this.getFilesDir().getPath()+"/"+mFileName;

        File file=new File(filePath);

        if(isNetworkAvailable()){
            //if network is available always load from network
            networkOnDownloadFile(messageTV, link,downloadFAB);
        }
        else{
            //if network is not available check if file exists
            //if file exists show the file in memory
            //otherwise tell user they need internet
            if(file.exists()){
                noNetworkFileExistsLoadFileFromCache(downloadFAB, messageTV, file); }
            if(!file.exists()){
                noNetworkNoFileInformUser(downloadFAB, messageTV); }
            }


        //Set up downloadFAB
        downloadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                new SendALongToast(getApplicationContext(),getResources().getString(R.string.will_download)).show();
                // Executes the task in parallel to other tasks
                new RetrivePDFStreamToSave().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,link);
                messageTV.setText("");
                progressBar.setVisibility(View.INVISIBLE);}
                else{
                    new SendALongToast(getApplicationContext(),getResources().getString(R.string.no_network)).show();
                  downloadFAB.hide();
                }
        }
        });
    }


    private void noNetworkFileExistsLoadFileFromCache(FloatingActionButton downloadFAB, TextView messageTV, File file) {
        //File exist will load from disk
        Log.d(TAG, "onCreate: "+file.getPath());
        pdfView.fromFile(file).load();
        downloadFAB.hide();
        progressBar.setVisibility(View.INVISIBLE);
        new SendALongToast(getApplicationContext(),getResources().getString(R.string.file_exist)).show();
        messageTV.setText("");
    }

    @Override
    protected void onPause() {
        Log.e("pdfactivity","activityOnPause");
        super.onPause();
    }

    private void networkOnDownloadFile(TextView messageTV, String link, FloatingActionButton downloadFAB) {
        //File does not exist attempt to download online and show downloadfab
        downloadFAB.show();
        // Executes the task in parallel to other tasks.To download the file online with link
        new RetrivePDFStream().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,link);
        messageTV.setText("");
    }

    private void noNetworkNoFileInformUser(FloatingActionButton downloadFAB, TextView messageTV) {
        downloadFAB.hide();
        progressBar.setVisibility(View.INVISIBLE);
        messageTV.setText(getResources().getString(R.string.no_network_to_see_file));
    }

    class RetrivePDFStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL uri = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                //if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                //}
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {

            //Hide progress bar as soon as file is loaded
            OnLoadCompleteListener onLoadCompleteListener=new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                   progressBar.setVisibility(View.GONE);
                }
            };

            pdfView.fromStream(inputStream)
                    .onLoad(onLoadCompleteListener)
                    .load();

        }
    }

    private class RetrivePDFStreamToSave extends AsyncTask<String, Void, File> {
        String fileNameString;
        @Override
        protected File doInBackground(String... strings) {
            InputStream inputStream = null;
            fileNameString=mFileName;
            try {
                URL uri = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                //if (urlConnection.getResponseCode() == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                //}

            } catch (IOException e) {
                return null;
            }
            return getPdfOnDisk(fileNameString,getApplication(),inputStream);
        }
        @Override
        protected void onPostExecute(File file) {
            progressBar.setVisibility(View.INVISIBLE);
            String filePath=getApplication().getFilesDir().getPath()+"/"+mFileName;
            File mfile=new File(filePath);
            Boolean canExec=file.canRead();
            //Check if file is saved let user know
            if(mfile.canRead()){
                new SendALongToast(getApplicationContext() ,getResources().getText(R.string.file_saved).toString()).show();
            }
        }
    }


    private File getPdfOnDisk(String filenamehere, Context context,InputStream inputStream) {
        File file = new File(context.getFilesDir(), filenamehere);
        String filePathTwo=getFilesDir().getPath();
        String filePath=file.getPath();
        copyInputStreamToFile(inputStream,file);
        return file;
    }

    // Copy an InputStream to a File.
    //
    private void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }
                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setUpConnectivityCheck(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkRequest.Builder builder=new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        NetworkRequest networkRequest=builder.build();
        //Setting up networkcallback for system changes. In here the actions for when user is
        //connected and disconnedted online are listed
        ConnectivityManager.NetworkCallback mCallback=new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(Network network) {
                new SendALongToast(getApplicationContext(),getResources().getString(R.string.have_internet)).show();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadFAB.show();
                    }
                });
                super.onAvailable(network);
            }

            @Override
            public void onLost(Network network) {
                new SendALongToast(getApplicationContext(),getResources().getString(R.string.lost_connection)).show();
                Log.e(TAG, "onAvailable: "+"sent connectionlost toast");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadFAB.hide();
                    }
                });
                super.onLost(network);
            }
        };
        //Asking to be notified of connect/disconnects from internet
        connectivityManager.registerNetworkCallback(networkRequest,mCallback);
    }



    @Override
    protected void onDestroy() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //Unregistering the network callback, otherwise it will remain active as long as the
        //application is live
        if(networkCallback!=null){
        connectivityManager.unregisterNetworkCallback(networkCallback);}
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        setUpConnectivityCheck();
        super.onStart();
    }
}
