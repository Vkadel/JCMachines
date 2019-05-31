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

import com.example.virginia.jcmachines.Data.effcalculation;
import com.example.virginia.jcmachines.dummy.DummyContent;
import com.example.virginia.jcmachines.utils.MDateFormating;
import com.example.virginia.jcmachines.viewmodels.efficiencyFormulaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private efficiencyFormulaViewModel viewModel=new efficiencyFormulaViewModel();
    private View recyclerView;
    private List<effcalculation> mycalculations=new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effcalculation_list);
        mContext=this;
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

        if (findViewById(R.id.effcalculation_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        //Get the list of items
        if(savedInstanceState==null){
        viewModel= ViewModelProviders.of(this).get(efficiencyFormulaViewModel.class);
        viewModel.getMeffCalculationList(FirebaseAuth.getInstance().getUid(),mContext).observe(this,new myObserver());}

        recyclerView = findViewById(R.id.effcalculation_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane,mycalculations));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final effcalculationListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final List<effcalculation> mycalculations;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(machineDetailFragment.EFF_ARG_ITEM_ID, item.id);
                    AddEffCalculationActivityFragment fragment = new AddEffCalculationActivityFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.effcalculation_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, effcalculationDetailActivity.class);
                    intent.putExtra(machineDetailFragment.EFF_ARG_ITEM_ID, item.id);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(effcalculationListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane,List<effcalculation> calc) {

            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
            mycalculations = calc;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.effcalculation_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if(mycalculations.size()>0||mycalculations!=null){
            holder.mIdView.setText(mycalculations.get(position).getMid());
            holder.mDate.setText(new MDateFormating(mParentActivity).convertMillisTodate(mycalculations.get(position).getDate()));
            holder.mEff.setText(String.valueOf(mycalculations.get(position).getEff()));
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);}
        }

        @Override
        public int getItemCount() {
            return mycalculations.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mEff;
            final TextView mDate;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.machine_id);
                mEff = (TextView) view.findViewById(R.id.eff);
                mDate=(TextView) view.findViewById(R.id.measurement_date);
            }
        }
    }
    public class myObserver implements Observer<DataSnapshot>{
        @Override
        public void onChanged(@Nullable DataSnapshot dataSnapshot) {
            List<effcalculation> myList=new ArrayList<>();
            dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                @Override
                public void accept(DataSnapshot dataSnapshot) {
                    myList.add(dataSnapshot.getValue(effcalculation.class));
                }
            });
            mycalculations=myList;
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }

}
