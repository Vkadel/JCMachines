package com.example.virginia.jcmachines;

import android.R.id;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View.OnClickListener;

import com.example.virginia.jcmachines.R.layout;

import timber.log.Timber;

/**
 * An activity representing a single machine detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link machineListActivity}.
 */
public class machineDetailActivity extends AppCompatActivity {
String thisItemID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_machine_detail);
        Toolbar toolbar = this.findViewById(R.id.detail_toolbar);
        this.setSupportActionBar(toolbar);
        Timber.plant(new Timber.DebugTree());
        FloatingActionButton fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (this.findViewById(R.id.machine_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            //this.mTwoPane = true;
            Timber.d("Going TwoPane");
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            thisItemID=this.getIntent().getStringExtra(machineDetailFragment.ARG_ITEM_ID);
            arguments.putString(machineDetailFragment.ARG_ITEM_ID, thisItemID);
            machineDetailFragment fragment = new machineDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.machine_detail_container, fragment)
                    .commit();
        }
        else{

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //

            this.navigateUpTo(new Intent(this, machineListActivity.class));

            Intent intent = new Intent(this, machineListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int screenSize = getResources().getConfiguration().screenLayout;
        Timber.d("Going out of state detail activity "+screenSize+" "+thisItemID);
        outState.putString(machineDetailFragment.ARG_ITEM_ID,thisItemID);
        super.onSaveInstanceState(outState);
    }
}
