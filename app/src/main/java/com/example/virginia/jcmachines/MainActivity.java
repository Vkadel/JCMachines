package com.example.virginia.jcmachines;

import android.app.Person;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.virginia.jcmachines.R.drawable;
import com.example.virginia.jcmachines.R.id;
import com.example.virginia.jcmachines.R.layout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import timber.log.Timber;
import timber.log.Timber.DebugTree;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    GoogleSignInClient mGoogleSignInClient;
    ImageView userPic;
    static final int RC_SIGN_IN=1;
    TextView userEmail;
    static Boolean firebaseIsSetUp=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_main);
        Timber.plant(new DebugTree());
        this.userEmail = this.findViewById(id.user_email);
        this.userPic = this.findViewById(id.user_pic);
        this.updateUI(null);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        this.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //On click for user, to start signing flow
        this.findViewById(id.sign_in_button).setOnClickListener(this);
        this.findViewById(id.sign_out_button).setOnClickListener(this);

        //Setting up database persistence
        if(!firebaseIsSetUp){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
            firebaseIsSetUp=true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        this.updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account==null){
            this.userEmail.setText("Please login");
            this.userPic.setImageResource(drawable.place_holder_image);}
        else
        {
            this.userEmail.setText(account.getDisplayName());
            Uri PhotoURL=account.getPhotoUrl();
            String id=account.getId();
            String Name=account.getDisplayName();
            Timber.d(id+"photo");
            if (PhotoURL!=null){
                String convertURI=PhotoURL.toString();
                GlideApp.with(this).load(PhotoURL.toString())
                        .apply(RequestOptions.centerInsideTransform())
                        .into(this.userPic);
            }
        }
       if(account!=null){

        Intent intent = new Intent(this, machineListActivity.class);
           this.startActivity(intent);}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case id.sign_in_button:
                this.signIn();
                break;
            case id.sign_out_button:
                this.signOut();
                break;
        }
    }

    private void signOut() {
        this.mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        this.updateUI(null);

    }

    private void signIn() {
        Intent signInIntent = this.mGoogleSignInClient.getSignInIntent();
        this.startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == MainActivity.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            this.handleSignInResult(task);
    }
}

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            // only try to get data if sign in isSuccessful, show authenticated UI.
            if(task.isSuccessful()){
                this.updateUI(account);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.w("signInResult:failed code=" + e.getStatusCode());
            this.updateUI(null);
        }
    }
    }
