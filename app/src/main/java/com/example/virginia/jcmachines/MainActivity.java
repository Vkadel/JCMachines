package com.example.virginia.jcmachines;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;

import com.example.virginia.jcmachines.Data.machine;
import com.example.virginia.jcmachines.R.layout;
import com.example.virginia.jcmachines.databinding.ActivityMainBinding;
import com.example.virginia.jcmachines.utils.DoWhenNetWorkIsActive;
import com.example.virginia.jcmachines.viewmodels.machineViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;
import timber.log.Timber.DebugTree;


public class MainActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    ImageView userPic;
    static final int RC_SIGN_IN = 1;
    TextView userEmail;
    static Boolean firebaseIsSetUp = false;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    ActivityMainBinding binding;
    private Boolean launchedAuth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, layout.activity_main);
        binding.setLifecycleOwner(this);
        Timber.plant(new DebugTree());


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Setting up database persistence
        if (!firebaseIsSetUp && mAuth.getUid() == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            //Check if user has internet Access
            CheckConnectivity();
            firebaseIsSetUp = true;
        } else {
            //Only go straight to list of machines if there are machines available in the internal database
            machineViewModel machineViewModel= ViewModelProviders.of(this).get(machineViewModel.class);
            machineViewModel.getMachines().observe(this, new Observer<PagedList<machine>>() {
                @Override
                public void onChanged(PagedList<machine> machines) {
                    if(machines.size()!=0){
                        goToListofMachines();
                    }
                    else{
                        binding.userMessageTv.setVisibility(View.VISIBLE);
                    }
                }
            });
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
                if (!launchedAuth) {
                    binding.userMessageTv.setVisibility(View.GONE);
                    launchedAuth = true;
                    launchSignIn();
                }
            }
        };
        doIfnetWorkIsNOTAvailable = new Runnable() {
            @Override
            public void run() {
                binding.userMessageTv.setVisibility(View.VISIBLE);
            }
        };
        new DoWhenNetWorkIsActive(doIfnetworkIsAvailable, doIfnetWorkIsNOTAvailable, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void launchSignIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.steele_logo_100)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                goToListofMachines();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...


            }
        }

    }

    private void logout() {

    }

    private void goToListofMachines() {
        Intent intent = new Intent(this, machineListActivity.class);
        startActivity(intent);
    }
}
