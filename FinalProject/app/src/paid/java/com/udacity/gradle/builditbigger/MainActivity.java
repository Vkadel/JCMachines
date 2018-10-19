package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jokelib.MyJoker;
import com.example.myjokerlibraryandroid.MainActivityJoker;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements EndPointAsyncTask.OnJokeRetrivalTaskCompletedInterface {

    @BindView(R.id.joke_tv)
    TextView jokeTV;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    static Intent intent;
    EndPointAsyncTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        progressBar.setVisibility(View.VISIBLE);
        jokeTV.setVisibility(View.VISIBLE);
        MyJoker joker = new MyJoker();
        String newJoke=joker.getJokeFromJokerLib();
        task=new EndPointAsyncTask(this);
        task.execute();

    }


    @Override
    public void OnJokeRetrivalTaskCompleted() {
        String newJoke= null;
        try {
            newJoke = task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intent=new Intent(this,MainActivityJoker.class);
        intent.putExtra(MainActivityJoker.MY_JOKE_TAG,newJoke);
        progressBar.setVisibility(View.INVISIBLE);
        jokeTV.setVisibility(View.INVISIBLE);
        startActivity(intent);
    }
}
