package com.example.virginia.jcmachines;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Pdf_viewer_Activity extends AppCompatActivity {
    PDFView pdfView;
    ProgressBar progressBar;
    public static String ARG_LINK="arg_link";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdfView=findViewById(R.id.pdfView);
        progressBar=findViewById(R.id.progressBar);
        //getting the link from the intent
        String link=getIntent().getStringExtra(ARG_LINK);
        new RetrivePDFStream().execute(link);
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
}
