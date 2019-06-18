package com.example.virginia.jcmachines;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.animations.appearAnimText;
import com.example.virginia.jcmachines.animations.fadeAnimBar;
import com.example.virginia.jcmachines.animations.fadeText;
import com.example.virginia.jcmachines.databinding.ActivityEffcalculationListBinding;
import com.example.virginia.jcmachines.databinding.EffcalculationListItemBinding;
import com.example.virginia.jcmachines.utils.DoWhenNetWorkIsActive;
import com.example.virginia.jcmachines.utils.MDateFormating;
import com.example.virginia.jcmachines.utils.SendALongToast;
import com.example.virginia.jcmachines.viewmodels.efficiencyFormulaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import timber.log.Timber;

/**
 * An activity representing a list of effcalculations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link effcalculationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class effcalculationListActivity extends AppCompatActivity {
    private static String DONT_HAVE_CHILDREN_ARG = "dont_have_children";
    private static ArrayList<String> mItemsToDelete = new ArrayList<>();
    private static ArrayList<Integer> viewsClicked = new ArrayList<>();
    private static Boolean imDeleting = false;
    private static boolean dontHaveChildren = true;
    ActivityEffcalculationListBinding activivityBinding;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private efficiencyFormulaViewModel viewModel = new efficiencyFormulaViewModel();
    private View recyclerView;
    private List<effcalculation> mycalculations = new ArrayList<>();
    private List<effcalculation> myNewcalculations = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_effcalculation_list);
        activivityBinding.setLifecycleOwner(this);
        mContext = this;
        mActivity=this;
        CheckConnectivity();
        activivityBinding.toolbar.setTitle(getResources().getString(R.string.list_of_calculations_eff_brick_Activity_title));
        setSupportActionBar(activivityBinding.toolbar);
        if (activivityBinding.framedLayoutInclude.effcalculationDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        //Get the list of items
        viewModel = ViewModelProviders.of(this).get(efficiencyFormulaViewModel.class);
        String userid = FirebaseAuth.getInstance().getUid();
        viewModel.getMeffCalculationListbyChildren(userid, mContext).observe(this, new myObserver());
        activivityBinding.fab.setOnClickListener(new ReadyToDeleteOnClickListener());

        //Get the have Childrenstate
        if(savedInstanceState!=null&&savedInstanceState.getBoolean(DONT_HAVE_CHILDREN_ARG)){
            dontHaveChildrenUpdateButHaveInternet();
        }
        //Check if User has calculations only once when activity loads, if they don't
        //save the fact and ensure you don't call it again
        if (savedInstanceState == null || dontHaveChildren) {
            viewModel.getMeffCalculationList(userid, this).observe(this, new Observer<DataSnapshot>() {
                @Override
                public void onChanged(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        new SendALongToast(mContext, getResources().getString(R.string.please_add_more_eff_calc_brick)).show();
                        dontHaveChildrenUpdateButHaveInternet();
                    }
                }
            });
        }
        if(savedInstanceState!=null){
            activivityBinding.progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void dontHaveChildrenUpdateButHaveInternet() {
        new fadeAnimBar( activivityBinding.progressBar,this).animate();
        activivityBinding.addMoreCalculationsIntructionsTv.setText(R.string.please_go_to_machine_add_calculations);
        new appearAnimText(activivityBinding.addMoreCalculationsIntructionsTv,this).animate();
        dontHaveChildren = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //TODO: save any clicked items to delete that have not been deleted yet
        outState.putBoolean(DONT_HAVE_CHILDREN_ARG, dontHaveChildren);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (!imDeleting) {
            super.onBackPressed();
        } else {
            imDeleting = false;
            mItemsToDelete = new ArrayList<>();
            activivityBinding.fab.hide();
            redrawRecycler();
        }

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mTwoPane, mycalculations, activivityBinding));
    }

    private void redrawRecycler() {
        if (mTwoPane) {
            assert activivityBinding.framedLayoutInclude.effcalculationListLarge != null;
            setupRecyclerView(activivityBinding.framedLayoutInclude.effcalculationListLarge);
        } else {
            assert activivityBinding.framedLayoutInclude.effcalculationList != null;
            setupRecyclerView(activivityBinding.framedLayoutInclude.effcalculationList);
        }
    }

    private void CheckConnectivity() {
        Runnable doIfnetworkIsAvailable;
        Runnable doIfnetWorkIsNOTAvailable;
        doIfnetworkIsAvailable = new Runnable() {
            @Override
            public void run() {
                //Need to prevent the authentication event from launching twice
                //after a network update
                if (mycalculations.size()==0) {
                    activivityBinding.addMoreCalculationsIntructionsTv.setVisibility(View.INVISIBLE);
                    activivityBinding.addMoreCalculationsIntructionsTv.setText(R.string.please_go_to_machine_add_calculations);
                }
            }
        };
        doIfnetWorkIsNOTAvailable = new Runnable() {
            @Override
            public void run() {
                if(mycalculations.size()==0){
                    new SendALongToast(mActivity,getString(R.string.no_network)).show();
                }
                dontHaveInternetUIUpdate();
            }
        };
        DoWhenNetWorkIsActive doWhenNetWorkIsActive=
                new DoWhenNetWorkIsActive(doIfnetworkIsAvailable, doIfnetWorkIsNOTAvailable, this, this);
            if(!doWhenNetWorkIsActive.FirstCheck()){
            dontHaveInternetUIUpdate();
        };
        //Check if you do not have internet already;

    }

    public void dontHaveInternetUIUpdate(){
        new fadeAnimBar(activivityBinding.progressBar,mContext).animate();
        activivityBinding.addMoreCalculationsIntructionsTv.setText(R.string.no_network);
        new appearAnimText(activivityBinding.addMoreCalculationsIntructionsTv,mActivity).animate();
    }
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final effcalculationListActivity mParentActivity;
        private final List<effcalculation> mycalculations;
        private final boolean mTwoPane;
        ActivityEffcalculationListBinding mactivivityBinding;
        private EffcalculationListItemBinding binding;


        SimpleItemRecyclerViewAdapter(effcalculationListActivity parent,
                                      boolean twoPane, List<effcalculation> calc, ActivityEffcalculationListBinding activivityBinding) {
            mParentActivity = parent;
            mTwoPane = twoPane;
            mycalculations = calc;
            mactivivityBinding = activivityBinding;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(mParentActivity), R.layout.effcalculation_list_item, parent, false);
            binding.setLifecycleOwner(mParentActivity);
            View view = binding.getRoot();
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (mycalculations.size() > 0 || mycalculations != null) {
                binding.machineId.setText(mycalculations.get(position).getMids());
                binding.measurementDate.setText(new MDateFormating(mParentActivity).convertMillisTodate(mycalculations.get(position).getDate()));
                binding.companyName.setText(mycalculations.get(position).getCompid());
                binding.eff.setText(String.valueOf((int)mycalculations.get(position).getEff()));
                binding.eff.setOnClickListener(new ItemDeleteListener(mactivivityBinding, mycalculations, mycalculations.get(position).getCalcid(), mParentActivity));
                binding.eff.getBackground().setTint(mParentActivity.getResources().getColor(R.color.colorWhite, mParentActivity.getTheme()));
                binding.getRoot().setTag(String.valueOf(position));
                binding.getRoot().setOnClickListener(new mylistItemClick(mTwoPane, mycalculations.get(position), mParentActivity));
                //Check if this item is in que to delete
                if (mItemsToDelete.contains(String.valueOf(mycalculations.get(position).getCalcid()))) {
                    binding.eff.getBackground().setTint(mParentActivity.getResources().getColor(R.color.gray, mParentActivity.getTheme()));
                    mactivivityBinding.fab.show();
                }
            }
        }

        @Override
        public int getItemCount() {
            return mycalculations.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View view) {
                super(view);
            }
        }
    }

    public static class ItemDeleteListener implements View.OnClickListener {
        ActivityEffcalculationListBinding mactivivityBinding;
        List<effcalculation> mmycalculations;
        String thiscalcculationId;
        Activity myparent;

        public ItemDeleteListener(ActivityEffcalculationListBinding activivityBinding, List<effcalculation> mycalculations, String itemID, Activity parent) {
            super();
            mactivivityBinding = activivityBinding;
            mmycalculations = mycalculations;
            thiscalcculationId = itemID;
            myparent = parent;
        }

        @Override
        public void onClick(View v) {
            //Check if the item has been clicked before
            if (mItemsToDelete.contains(thiscalcculationId)) {
                v.getBackground().setTintList(null);
                mItemsToDelete.remove(thiscalcculationId);
                if (mItemsToDelete.isEmpty() && mactivivityBinding.fab.getVisibility() == View.VISIBLE) {
                    mactivivityBinding.fab.hide();
                    imDeleting = false;
                    if(mmycalculations.size()==0){
                      new appearAnimText(mactivivityBinding.addMoreCalculationsIntructionsTv,myparent).animate();
                    }
                }
            } else {
                imDeleting = true;
                mItemsToDelete.add(thiscalcculationId);
                v.getBackground().setTint(v.getResources().getColor(R.color.gray, myparent.getTheme()));
                if (mactivivityBinding.fab.getVisibility() == View.INVISIBLE || mactivivityBinding.fab.getVisibility() == View.GONE) {
                    mactivivityBinding.fab.show();
                }
            }
        }
    }

    public static class mylistItemClick implements View.OnClickListener {
        Boolean mTwoPane;
        effcalculation item;
        effcalculationListActivity mParentActivity;

        public mylistItemClick(Boolean twoPane, effcalculation myitem, effcalculationListActivity parentActivity) {
            mTwoPane = twoPane;
            item = myitem;
            mParentActivity = parentActivity;
        }

        @Override
        public void onClick(View v) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(machineDetailFragment.EFF_ARG_ITEM_ID, v.getTag().toString());
                arguments.putString(machineDetailFragment.ARG_ITEM_ID, String.valueOf(item.getMid()));
                arguments.putBoolean(AddEffCalculationActivityFragment.IS_TWO_PANE, mTwoPane);
                AddEffCalculationActivityFragment fragment = new AddEffCalculationActivityFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.effcalculation_detail_container, fragment)
                        .commit();
            } else {
                Context context = v.getContext();
                Intent intent = new Intent(context, AddEffCalculationActivity.class);
                intent.putExtra(machineDetailFragment.EFF_ARG_ITEM_ID, v.getTag().toString());
                intent.putExtra(machineDetailFragment.ARG_ITEM_ID, String.valueOf(item.getMid()));
                context.startActivity(intent);
            }
        }
    }

    public class myObserver implements Observer<DataSnapshot> {
        @Override
        public void onChanged(@Nullable DataSnapshot dataSnapshot) {
            dontHaveChildren=false;
            new fadeAnimBar(activivityBinding.progressBar,mContext).animate();
            new fadeText(activivityBinding.addMoreCalculationsIntructionsTv,mContext).animate();
            Timber.e(dataSnapshot.getValue().toString());
            effcalculation thisEff = dataSnapshot.getValue(com.example.virginia.jcmachines.Data.effcalculation.class);
            myNewcalculations.removeIf(effcalculation -> {
                return (Boolean) effcalculation.getCalcid().equals(thisEff.getCalcid());
            });
            boolean isremove = viewModel.isRemove();
            if (thisEff.getActive() == true && !isremove) {
                myNewcalculations.add(thisEff);
            }
            mycalculations = myNewcalculations;
            if (mycalculations == null || mycalculations.size() == 0) {
                new SendALongToast(mContext, getResources().getString(R.string.please_add_more_eff_calc_brick)).show();
                new appearAnimText(activivityBinding.addMoreCalculationsIntructionsTv,getBaseContext()).animate();
            }
            redrawRecycler();
        }
    }

    public class ReadyToDeleteOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mItemsToDelete.forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    String refString = getString(R.string.firebase_ref_calculations_delete_one, FirebaseAuth.getInstance().getUid(), s);
                    DatabaseReference refDeleteOneItem = FirebaseDatabase.getInstance().getReference(refString);
                    refDeleteOneItem.setValue(false);
                }
            });
            v.setVisibility(View.GONE);
            mItemsToDelete = new ArrayList<>();
        }
    }
}
