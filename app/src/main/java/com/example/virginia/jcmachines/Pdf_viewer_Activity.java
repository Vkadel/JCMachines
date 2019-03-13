package com.example.virginia.jcmachines;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Pdf_viewer_Activity extends AppCompatActivity {

    private PDFView pdfView;
    private ProgressBar progressBar;
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
        setContentView(R.layout.activity_pdf_viewer);
        pdfView=findViewById(R.id.pdfView);
        progressBar=findViewById(R.id.progressBar);


        //TODO: add logic to know if connected to the internet
        //TODO: if no internet attempt to load from Disk



        //getting the link from the intent
        String link=getIntent().getStringExtra(ARG_LINK);

        //CreateFileName: @ARG_DOCUMENT_ID+@ARG_MACHINE_ID

        String mFileName=getIntent().getStringExtra(ARG_DOCUMENT_ID)
                +getIntent().getIntExtra(ARG_MACHINE_ID,0);

        AsyncTask mAs = null;

        if(hasActiveInternetConnection(this)){
        // Executes the task in parallel to other tasks
        mAs=new RetrivePDFStream().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,link);}
        else{
            //TODO:CheckIfFileExist
            //TODO:If file exists LOAD Existing file, else let user know that needs to have internet connection
            //or download file

        }


        FloatingActionButton downloadFAB=(FloatingActionButton)findViewById(R.id.floatingActionButtonDownload);
        //TODO: hide @downloadFAB If item its already downloaded
        downloadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                toast = Toast.makeText(getApplication(),"Will Download",Toast.LENGTH_LONG);
                toast.show();
                AsyncTask mAs = null;
                // Executes the task in parallel to other tasks
                mAs=new RetrivePDFStreamToSave().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,link);
        }
        });
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
            fileNameString=strings[0].toString();
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
        protected void onPostExecute(File inputStream) {
            progressBar.setVisibility(View.INVISIBLE);
            //Hide progress bar as soon as file is loaded
            OnLoadCompleteListener onLoadCompleteListener=new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    progressBar.setVisibility(View.GONE);
                }
            };

            //TODO: mayNeedInput stream from disc
            pdfView.fromFile(inputStream).load();

        }
    }

    private File getPdfOnDisk(String filenamehere, Context context,InputStream inputStream) {
        File file = new File(context.getFilesDir(), "myNewFile.pdf");
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

    public boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
