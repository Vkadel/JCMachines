package com.example.virginia.jcmachines;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.R.id;
import com.example.virginia.jcmachines.R.layout;

import java.util.List;

import timber.log.Timber;
import timber.log.Timber.DebugTree;

/**
 * An activity representing a list of machines. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link machineDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class machineListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private machineViewModel machineViewModel;
    private List<machine> mMachines;
    private StaggeredGridLayoutManager gridLayoutManager;
    private Boolean updatedOnce=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_machine_list);
        this.machineViewModel = ViewModelProviders.of(this).get(machineViewModel.class);
        Timber.plant(new DebugTree());
        Toolbar toolbar = this.findViewById(id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitle(this.getTitle());
        FloatingActionButton fab = this.findViewById(id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final View recyclerView = this.findViewById(id.machine_list);
        assert recyclerView != null;
        if (this.findViewById(id.machine_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            this.mTwoPane = true;
        }
        //check if this is the first time the activity loads. Will subscribe to
        //Viewmodel
        if(savedInstanceState==null){
            this.machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                @Override
                public void onChanged(@Nullable List<machine> machines) {
                    if(machines!=null){
                        mMachines =machines;
                        //Ensure the Recycler only updates once per load when the model sends an update
                        if (!updatedOnce){
                        setupRecyclerViewWithMachines((RecyclerView) recyclerView);}
                        updatedOnce=true;
                    }
                }
            });
        }
        else{
            mMachines = this.machineViewModel.getMachines().getValue();
            setupRecyclerViewWithMachines((RecyclerView) recyclerView);
        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new machineListActivity.SimpleItemRecyclerViewAdapter(this, this.mMachines, this.mTwoPane));
    }
    private void setupRecyclerViewWithMachines(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new machineListActivity.SimpleItemRecyclerViewAdapter(this, this.mMachines, this.mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends Adapter<com.example.virginia.jcmachines.machineListActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final machineListActivity mParentActivity;
        private final List<machine> mValues;
        private final boolean mTwoPane;
        private final OnClickListener mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                machine item = (machine) view.getTag();
                if (machineListActivity.SimpleItemRecyclerViewAdapter.this.mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(machineDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
                    arguments.putBoolean(machineDetailFragment.ARG_IS_TWO_PANE, machineListActivity.SimpleItemRecyclerViewAdapter.this.mTwoPane);
                    machineDetailFragment fragment = new machineDetailFragment();
                    fragment.setArguments(arguments);
                    machineListActivity.SimpleItemRecyclerViewAdapter.this.mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(id.machine_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, machineDetailActivity.class);
                    intent.putExtra(machineDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(machineListActivity parent,
                                      List<machine> items,
                                      boolean twoPane) {
            this.mValues = items;
            this.mParentActivity = parent;
            this.mTwoPane = twoPane;
        }

        @Override
        public machineListActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layout.machine_list_item, parent, false);
            return new machineListActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(machineListActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            if (this.mValues.get(position).getThumbnailImage()!="na"&& this.mValues.get(position).getThumbnailImage()!=null){
                Glide.with(this.mParentActivity)
                        .load(this.mValues.get(position).getThumbnailImage())
                        .into(holder.imageView);
            }

            holder.mContentView.setText(this.mValues.get(position).getMachineFullName());
            holder.itemView.setTag(this.mValues.get(position));
            holder.itemView.setOnClickListener(this.mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (this.mValues ==null){
                return 0;
            }else{
            return this.mValues.size();}
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mContentView;
            final ImageView imageView;

            ViewHolder(View view) {
                super(view);

                this.mContentView = view.findViewById(id.machine_name);
                this.imageView = view.findViewById(id.imageView);
            }
        }
    }
}
