package com.example.virginia.jcmachines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.virginia.jcmachines.Data.machine;

import java.util.List;

import timber.log.Timber;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_list);
        machineViewModel = ViewModelProviders.of(this).get(machineViewModel.class);
        Timber.plant(new Timber.DebugTree());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.machine_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        final View recyclerView = findViewById(R.id.machine_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        //check if this is the first time the activity loads. Will subscribe to
        //Viewmodel
        if(savedInstanceState==null){
            machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                @Override
                public void onChanged(@Nullable List<machine> machines) {
                    if(machines!=null){
                        mMachines=machines;
                        setupRecyclerViewWithMachines((RecyclerView) recyclerView);
                    }
                }
            });
        }
        else{
            mMachines=machineViewModel.getMachines().getValue();
            setupRecyclerViewWithMachines((RecyclerView) recyclerView);
        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mMachines, mTwoPane));
    }
    private void setupRecyclerViewWithMachines(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mMachines, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final machineListActivity mParentActivity;
        private final List<machine> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                machine item = (machine) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(machineDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
                    machineDetailFragment fragment = new machineDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.machine_detail_container, fragment)
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
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.machine_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (mValues.get(position).getThumbnailImage()!="na"&& mValues.get(position).getThumbnailImage()!=null){
                Glide.with(mParentActivity)
                        .load(mValues.get(position).getThumbnailImage())
                        .into(holder.imageView);
            }
            holder.mIdView.setText(mValues.get(position).getId()+"");
            holder.mContentView.setText(mValues.get(position).getMachineFullName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (mValues==null){
                return 0;
            }else{
            return mValues.size();}
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView imageView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                imageView = (ImageView)view.findViewById(R.id.imageView);
            }
        }
    }
}
