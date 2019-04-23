package com.example.virginia.jcmachines;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import com.bumptech.glide.request.RequestOptions;
import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.R.id;
import com.example.virginia.jcmachines.R.layout;

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
    private PagedList<machine> mMachines;
    private Boolean updatedOnce=false;
    private int thisItemID;
    private Boolean cameFromWidget=false;
    private RecyclerView saveInstanceOfRecyclerView;
    Activity activity;
    Boolean isLarge=false;
    Boolean isSmall=true;
    Boolean isLandScape=false;
    Boolean isPortrait=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_machine_list);
        this.machineViewModel = ViewModelProviders.of(this).get(machineViewModel.class);

        PagedList.Config.Builder config= new PagedList.Config.Builder().setPageSize(2).setPrefetchDistance(4);
        Timber.d("atMachineList: Started List activity");
        Timber.plant(new DebugTree());
        activity=this;
        Toolbar toolbar = this.findViewById(id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitle(this.getTitle());


        //Check if the application is connected to the web, and give the user a toast to signal that the information may not be up to date
        if(!isNetworkAvailable()){
            Toast.makeText(activity,getResources().getString(R.string.noNetwork),Toast.LENGTH_LONG).show();
        }
        if(getIntent().getStringExtra(machineDetailFragment.ARG_ITEM_ID)!=null&getIntent().getStringExtra(machineDetailFragment.ARG_CAME_FROM_WIDGET)!=null){
            cameFromWidget=true;
            thisItemID=Integer.parseInt(getIntent().getStringExtra(machineDetailFragment.ARG_ITEM_ID));
        }

        View recyclerView = this.findViewById(id.machine_list);
        assert recyclerView != null;
        if (this.findViewById(id.machine_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            this.mTwoPane = true;
        }
        final machineAdapter machineAdapter=new machineAdapter(this,mTwoPane);;
        //Checked if application loaded data at least once before. This
        //will be used to prevent the blips when data loads more than once in
        //the recycler. Otherwise when preventing the blip the application
        //will not load data upon install unless is restarted


        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        String  defaultValue = getResources().getString(R.string.loaded_once_preference_value);
        final String pref = sharedPref.getString(getString(R.string.loaded_once_preference_key), defaultValue);
         //check if this is the first time the activity loads. Will subscribe to
        //Viewmodel
        if(savedInstanceState==null){
            machineViewModel.machines.observe(this, new Observer<PagedList<machine>>() {
                @Override
                public void onChanged(@Nullable PagedList<machine> machines) {
                    if(machines!=null && machines.size()!=0){
                        Timber.d("Going to update recycler after data update/change");
                        mMachines =machines;
                        //Ensure the Recycler only updates once per load when the model sends an update
                        if ((pref.equals("false")||mMachines.size()==0)||!updatedOnce){
                            //Check if the machine list is zero
                            if(machines.size()==0){
                                Toast.makeText(activity,getResources().getString(R.string.list_is_zero),Toast.LENGTH_LONG).show();
                            }
                            ((RecyclerView)recyclerView).setAdapter(machineAdapter);
                            machineAdapter.submitList(machines);
                            updatedOnce=true;
                            editor.putString(getString(R.string.loaded_once_preference_key), "true");
                            editor.commit();
                            //Check if the item Came from the widget.
                            if (cameFromWidget) {
                                Timber.d("At MachineListActivity: Came from Widget ");
                                Timber.d("At MachineListActivity: Widget to display "+thisItemID);

                                if (mTwoPane) {
                                    Bundle arguments = new Bundle();
                                    arguments.putString(machineDetailFragment.ARG_ITEM_ID, String.valueOf(thisItemID));
                                    arguments.putBoolean(machineDetailFragment.ARG_IS_TWO_PANE, mTwoPane);
                                    machineDetailFragment fragment = new machineDetailFragment();
                                    fragment.setArguments(arguments);
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(id.machine_detail_container, fragment)
                                            .commit();
                                    cameFromWidget=false;

                                } else {
                                    Intent intent = new Intent(getApplicationContext(), machineDetailActivity.class);
                                    intent.putExtra(machineDetailFragment.ARG_ITEM_ID, String.valueOf(thisItemID));
                                    startActivity(intent);
                                    cameFromWidget=false;

                                }
                                ((RecyclerView) recyclerView).scrollToPosition(thisItemID);
                            }
                        }

                    }
                    machineAdapter.submitList(machines);

                }
            });}
        else{
            mMachines = this.machineViewModel.getMachines().getValue();
            machineAdapter.submitList(mMachines);
            ((RecyclerView)recyclerView).setAdapter(machineAdapter);
            ((RecyclerView) recyclerView).scrollToPosition(thisItemID);
        }

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pull_refresh_machine_list);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                machineViewModel.loadArticlesOnline();
                pullToRefresh.setRefreshing(false);
                Toast.makeText(activity,getResources().getString(R.string.updating_data_online),Toast.LENGTH_LONG).show();

            }
        });

        ((RecyclerView)recyclerView).setAdapter(machineAdapter);

        if(cameFromWidget&&thisItemID!=0){
        ((RecyclerView) recyclerView).scrollToPosition(thisItemID);}

    }



    //Change size of ImageViews Based on Screen size
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
    //Check for Internet Connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(machineDetailFragment.ARG_ITEM_ID,thisItemID);
        super.onSaveInstanceState(outState);
    }

    public class machineAdapter
            extends PagedListAdapter<machine, machineAdapter.ViewHolder> {

        private machineListActivity mParentActivity;
        //private final List<machine> mValues;
        private final boolean mTwoPane;
        private final OnClickListener mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                machine item = (machine) view.getTag();
                if (machineAdapter.this.mTwoPane && !isPortraitAndLarge(getApplicationContext())) {
                    Bundle arguments = new Bundle();
                    thisItemID=item.getId();
                    arguments.putString(machineDetailFragment.ARG_ITEM_ID, String.valueOf(thisItemID));
                    arguments.putBoolean(machineDetailFragment.ARG_IS_TWO_PANE, machineAdapter.this.mTwoPane);
                    machineDetailFragment fragment = new machineDetailFragment();
                    fragment.setArguments(arguments);
                    machineAdapter.this.mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(id.machine_detail_container, fragment)
                            .commit();
                } else if(isPortraitAndLarge(getApplicationContext())||isSmall) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, machineDetailActivity.class);
                    intent.putExtra(machineDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
                    context.startActivity(intent);
                }

            }

        };

        machineAdapter(machineListActivity parent,
                       boolean twoPane) {
            super(DIFF_CALLBACK);
            this.mParentActivity = parent;
            this.mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(layout.machine_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(machineAdapter.ViewHolder holder, int position) {
            //Checks
            machine mMachine=null;
            int number=getItemCount();
            mMachine=getItem(position);

            if(getItem(position)!=null){
            if(!mTwoPane){
            resizeImage(holder.imageView);}
            if (mMachine.getThumbnailImage()!="na"&& mMachine.getThumbnailImage()!=null){
                holder.imageView.setVisibility(View.VISIBLE);
                Glide.with(this.mParentActivity)
                        .load(mMachine.getThumbnailImage())
                        .transition(withCrossFade()).apply(new RequestOptions().override(holder.imageView.getWidth(),holder.imageView.getHeight()))
                        .apply(new RequestOptions().transforms(new RoundedCorners(16)))
                        .into(holder.imageView);
            }

            holder.mContentView.setText(mMachine.getMachineFullName());
            holder.itemView.setTag(mMachine);
            holder.itemView.setOnClickListener(this.mOnClickListener);
            //check if the widget triggered the intent
            if(cameFromWidget&&thisItemID==position){
                mOnClickListener.onClick(holder.itemView);
            }
            }else{
                // Null defines a placeholder item - PagedListAdapter will automatically invalidate
                // this row when the actual object is loaded from the database
                holder.itemView.invalidate();
            }

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
    private static DiffUtil.ItemCallback<machine> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<machine>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull machine oldUser, @NonNull machine newUser) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldUser.getId() == newUser.getId();
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull machine oldUser, @NonNull machine newUser) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldUser.getId()== newUser.getId();
                }
            };
    private Boolean isPortraitAndLarge(Context context){
        getScreenSize(context);
        getScreenOrientation(context);
        return isLarge&&isPortrait;
    }
    private void triggerWidgetUdate() {
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, jcSteeleMachineWidget.class));
        //Calling a widget Update manually
        jcSteeleMachineWidget myWidget;
        myWidget = new jcSteeleMachineWidget();
        myWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
    }
    void getScreenSize(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        String toastMsg;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                toastMsg = "Extra Large screen";
                isLarge =true;
                isSmall=false;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                isLarge =true;
                isSmall=false;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                isSmall=true;
                isLarge =false;
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                isSmall=true;
                isLarge =false;
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        Timber.d("Screen Size is: "+ toastMsg) ;
    }
    void getScreenOrientation(Context context) {
        int screenOrientation = context.getResources().getConfiguration().orientation;
        String toastMsg;
        switch (screenOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                toastMsg = "Landscape";
                isLandScape=true;
                isPortrait=false;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                toastMsg = "Portrait";
                isPortrait=true;
                isLandScape=false;
                break;
            case Configuration.ORIENTATION_UNDEFINED:
                isSmall=true;
                isLandScape=false;
                toastMsg = "Screen Undefined";
                break;
            default:
                toastMsg = "Screen size is neither landscape or portrait";
        }
        Timber.d("Screen Orientation is: "+ toastMsg) ;
    }

}
