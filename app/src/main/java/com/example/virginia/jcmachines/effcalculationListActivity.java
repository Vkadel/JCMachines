package com.example.virginia.jcmachines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.databinding.ActivityEffcalculationListBinding;
import com.example.virginia.jcmachines.databinding.EffcalculationListBinding;
import com.example.virginia.jcmachines.databinding.EffcalculationListBindingW900dpImpl;
import com.example.virginia.jcmachines.databinding.EffcalculationListItemBinding;
import com.example.virginia.jcmachines.utils.MDateFormating;
import com.example.virginia.jcmachines.viewmodels.efficiencyFormulaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An activity representing a list of effcalculations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link effcalculationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class effcalculationListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private efficiencyFormulaViewModel viewModel = new efficiencyFormulaViewModel();
    private View recyclerView;
    private List<effcalculation> mycalculations = new ArrayList<>();
    private Context mContext;
    private ArrayList<String> mItemsToDelete = new ArrayList<>();
    ActivityEffcalculationListBinding activivityBinding;
    EffcalculationListBindingW900dpImpl newBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_effcalculation_list);
        activivityBinding.setLifecycleOwner(this);
        mContext = this;
        setSupportActionBar(activivityBinding.toolbar);
        activivityBinding.toolbar.setTitle(getResources().getString(R.string.list_of_calculations_eff_brick));
        if (activivityBinding.framedLayoutInclude.effcalculationDetailContainer!= null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        //Get the list of items
        viewModel = ViewModelProviders.of(this).get(efficiencyFormulaViewModel.class);
        viewModel.getMeffCalculationList(FirebaseAuth.getInstance().getUid(), mContext).observe(this, new myObserver());
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mTwoPane, mycalculations, activivityBinding));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final effcalculationListActivity mParentActivity;

        private final List<effcalculation> mycalculations;
        private final boolean mTwoPane;
        private EffcalculationListItemBinding binding;
        ActivityEffcalculationListBinding mactivivityBinding;
        EffcalculationListBinding meffcalculationListBinding;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(machineDetailFragment.EFF_ARG_ITEM_ID, view.getTag().toString());
                    arguments.putBoolean(AddEffCalculationActivityFragment.IS_TWO_PANE, mTwoPane);
                    AddEffCalculationActivityFragment fragment = new AddEffCalculationActivityFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.effcalculation_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, effcalculationDetailActivity.class);
                    intent.putExtra(machineDetailFragment.EFF_ARG_ITEM_ID, view.getTag().toString());
                    context.startActivity(intent);
                }
            }
        };

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
                binding.machineId.setText(mycalculations.get(position).getMid());
                binding.measurementDate.setText(new MDateFormating(mParentActivity).convertMillisTodate(mycalculations.get(position).getDate()));
                binding.eff.setText(String.valueOf(mycalculations.get(position).getEff()));
              /*  binding.eff.setOnClickListener(new ItemDeleteListener(mactivivityBinding));*/
                binding.getRoot().setTag(String.valueOf(position));
                binding.getRoot().setOnClickListener(mOnClickListener);
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

    public class myObserver implements Observer<DataSnapshot> {
        @Override
        public void onChanged(@Nullable DataSnapshot dataSnapshot) {
            List<effcalculation> myList = new ArrayList<>();
            dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                @Override
                public void accept(DataSnapshot dataSnapshot) {
                    myList.add(dataSnapshot.getValue(effcalculation.class));
                }
            });
            mycalculations = myList;
            if(mTwoPane){
                assert activivityBinding.framedLayoutInclude.effcalculationListLarge != null;
                setupRecyclerView((RecyclerView) activivityBinding.framedLayoutInclude.effcalculationListLarge);
            }else{
            assert activivityBinding.framedLayoutInclude.effcalculationList != null;
            setupRecyclerView((RecyclerView) activivityBinding.framedLayoutInclude.effcalculationList);}
        }
    }

    public static class ItemDeleteListener implements View.OnClickListener {
        ActivityEffcalculationListBinding mactivivityBinding;

        public ItemDeleteListener(ActivityEffcalculationListBinding activivityBinding) {
            super();
            mactivivityBinding = activivityBinding;
        }

        @Override
        public void onClick(View v) {
            //make delete button visible
           /* mactivivityBinding = DataBindingUtil.findBinding((View) v.getParent().getParent());
            mactivivityBinding.fab.show();*/
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    activivityBinding.invalidateAll();
    activivityBinding.unbind();
        super.onSaveInstanceState(outState);
    }
}
