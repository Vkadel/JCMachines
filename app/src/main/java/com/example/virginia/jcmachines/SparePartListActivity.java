package com.example.virginia.jcmachines;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.Data.spareParts;
import com.example.virginia.jcmachines.viewmodels.machineViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import timber.log.Timber;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
    private com.example.virginia.jcmachines.viewmodels.machineViewModel machineViewModel;
    private List<machine> mMachines;
    private List<spareParts> sparePartsList;
    private machine mMachine;
    private spareParts spareParts;
    private String thisMachineId;
    private Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sparepart_list);
            this.machineViewModel = ViewModelProviders.of(this).get(machineViewModel.class);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
            Timber.plant(new Timber.DebugTree());
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
            thisMachineId=getIntent().getStringExtra(SparePartDetailFragment.ARG_ITEM_ID);
            final View recyclerView = findViewById(R.id.sparepart_list);
            assert recyclerView != null;
            if(savedInstanceState==null){
                thisMachineId = getIntent().getStringExtra(ARG_ITEM_ID);
                //this.isTwopane = this.getArguments().getBoolean(machineDetailFragment.ARG_IS_TWO_PANE);

                        machineViewModel.getMachines().observe(this, new Observer<PagedList<machine>>() {
                            @Override
                            public void onChanged(@Nullable PagedList<machine> machines) {
                                if (machines != null) {
                                    mMachines = machines;
                                    sparePartsList = mMachines.get(Integer.valueOf(thisMachineId)).getSpareParts();
                                    setupRecyclerViewWithSpateParts((RecyclerView) recyclerView);
                                }
                            }
                        });
            }
            else{
                mMachines = machineViewModel.getMachines().getValue();
                sparePartsList=mMachines.get(Integer.valueOf(thisMachineId)).getSpareParts();
                setupRecyclerViewWithSpateParts((RecyclerView) recyclerView);
            }
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.sparepart_list_pull_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                machineViewModel.loadArticlesOnline(); // your code
                pullToRefresh.setRefreshing(false);
                Toast.makeText(activity,getResources().getString(R.string.updating_data_online),Toast.LENGTH_LONG).show();
                recyclerView.invalidate();
            }
        });
      activity=this;
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
            navigateUpTo(new Intent(this, machineDetailActivity.class).putExtra(SparePartDetailFragment.ARG_ITEM_ID,thisMachineId));
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

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final SparePartListActivity mParentActivity;
        private final List<spareParts> mValues;
        private final boolean mTwoPane;
        private final String mMachineID;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spareParts item = (spareParts) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(SparePartDetailFragment.ARG_ITEM_ID,mMachineID);
                    arguments.putString(SparePartDetailFragment.ARG_SPARE_ITEM_ID, String.valueOf(item.getId()));
                    arguments.putBoolean(SparePartDetailFragment.ARG_IS_TWO_PANE,mTwoPane);
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
                    intent.putExtra(SparePartDetailFragment.ARG_IS_TWO_PANE,mTwoPane);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(SparePartListActivity parent,
                                      List<spareParts> items,
                                      boolean twoPane, String thisMachineId) {
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
            if(!mTwoPane){
             resizeImage(holder.SparePartImage);
            }
            Timber.d("adding: "+mValues.get(position).getName());
            if(!mValues.get(position).getImageLink().equals("na")){
                holder.SparePartImage.setVisibility(View.VISIBLE);
                Glide.with(mParentActivity).load(mValues.get(position)
                        .getImageLink()).transition(withCrossFade())
                        .apply(new RequestOptions().override(holder.SparePartImage.getLayoutParams().width,holder.SparePartImage.getLayoutParams().height))
                        .apply(new RequestOptions().transforms(new RoundedCorners(16)))
                        .into(holder.SparePartImage);
            }else{
                holder.SparePartImage.setVisibility(View.GONE);
            }
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (this.mValues==null){
                return 0;
            }else{
                return this.mValues.size();}
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView SparePartImage;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.machine_id);
                mContentView = (TextView) view.findViewById(R.id.eff);
                SparePartImage=(ImageView)view.findViewById(R.id.spare_part_item_iv);
            }
        }
    }
    public ImageView resizeImage(ImageView imageView){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int orgWidth = imageView.getWidth();
        int orgHeight = imageView.getHeight();


        imageView.getLayoutParams().height=width-(int)getResources().getInteger(R.integer.margin_take_out_for_list);
        imageView.getLayoutParams().width=width-(int)getResources().getInteger(R.integer.margin_take_out_for_list);
        return imageView;
    }
}
