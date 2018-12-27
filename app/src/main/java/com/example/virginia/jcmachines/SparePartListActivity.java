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
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.Data.spareParts;

import java.util.List;

/**
 * An activity representing a list of SpareParts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SparePartDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SparePartListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private boolean mTwoPane;
    private machineViewModel machineViewModel;
    private List<machine> mMachines;
    private List<spareParts> sparePartsList;
    private machine mMachine;
    private spareParts spareParts;
    private int thisMachineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart_list);
        this.machineViewModel =ViewModelProviders.of(this).get(machineViewModel.class);
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
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.sparepart_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        final View recyclerView = findViewById(R.id.sparepart_list);
        assert recyclerView != null;
        if(savedInstanceState==null){
            thisMachineId = getIntent().getIntExtra(ARG_ITEM_ID,0);
            //this.isTwopane = this.getArguments().getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);
            machineViewModel.getMachines().observe(this, new Observer<List<machine>>() {
                @Override
                public void onChanged(@Nullable List<machine> machines) {
                    if(machines!=null){
                        mMachines =machines;
                        sparePartsList=mMachines.get(thisMachineId).getSpareParts();
                        setupRecyclerViewWithSpateParts((RecyclerView) recyclerView);
                    }
                }
            });
        }
        else{
            mMachines = machineViewModel.getMachines().getValue();
            sparePartsList=mMachines.get(thisMachineId).getSpareParts();
            setupRecyclerViewWithSpateParts((RecyclerView) recyclerView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, sparePartsList, mTwoPane,thisMachineId));
    }
    private void setupRecyclerViewWithSpateParts(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, sparePartsList, mTwoPane,thisMachineId));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final SparePartListActivity mParentActivity;
        private final List<spareParts> mValues;
        private final boolean mTwoPane;
        private final int mMachineID;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spareParts item = (spareParts) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(SparePartDetailFragment.ARG_ITEM_ID,mMachineID);
                    arguments.putString(SparePartDetailFragment.ARG_SPARE_ITEM_ID, String.valueOf(item.getId()));
                    SparePartDetailFragment fragment = new SparePartDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.sparepart_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SparePartDetailActivity.class);
                    intent.putExtra(SparePartDetailFragment.ARG_SPARE_ITEM_ID, String.valueOf(item.getId()));
                    intent.putExtra(SparePartDetailFragment.ARG_ITEM_ID,mMachineID);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(SparePartListActivity parent,
                                      List<spareParts> items,
                                      boolean twoPane, int thisMachineId) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mMachineID=thisMachineId;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sparepart_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getName());
            holder.mContentView.setText(mValues.get(position).getDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (this.mValues ==null){
                return 0;
            }else{
                return this.mValues.size();}
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}