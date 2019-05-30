package com.example.virginia.jcmachines;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virginia.jcmachines.R.id;
import com.example.virginia.jcmachines.R.layout;
import com.example.virginia.jcmachines.databinding.ActivityMainBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,layout.activity_main);
        ((ViewDataBinding) binding).setLifecycleOwner(this);
        Timber.plant(new DebugTree());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Setting up database persistence
        if (!firebaseIsSetUp) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            firebaseIsSetUp = true;
        }
        binding.signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        } else {
         launchSignIn();
        }
    }

    private void launchSignIn(){
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
                Intent intent=new Intent(this,machineListActivity.class);
                startActivity(intent);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...


            }
        }
    }

    private void logout(){

    }

    private void updateUI(FirebaseUser account) {
        if (account == null) {
            //Do something when user is null
            ;
        } else {
            //Do someting when user is not null
        }
    }


}
