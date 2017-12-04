package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener
{

    private Button mSignOutButton;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_SIGN_IN_FACEBOOK = 9002;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        mSignOutButton = findViewById(R.id.bt_signout);
        mSignOutButton.setOnClickListener(this);


        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        signIn();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    // [END on_start_check_user]


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                Intent profileIntent = new Intent(GoogleSignInActivity.this, AdvertisementListActivity.class);
                profileIntent.putExtra("Type", "allAdvertisement");
                startActivity(profileIntent);
                finish();
            } else {
                // Google Sign In failed
                Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();

                Intent back = new Intent(GoogleSignInActivity.this, SignInActivity.class);
                startActivity(back);

            }
        }else{
            if(requestCode == RC_SIGN_IN_FACEBOOK){

            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(GoogleSignInActivity.this, "Sign in with google",
                                    Toast.LENGTH_SHORT).show();


                            //next activity
                            findViewById(R.id.bt_signout).setVisibility(View.VISIBLE);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Intent back = new Intent(GoogleSignInActivity.this, SignInActivity.class);
                            startActivity(back);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent intent = getIntent();
        if(intent != null) {
            String type = (String) intent.getExtras().getSerializable("type");
            if (type.equals("google")) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }else{
                if (type.equals("facebook")) {
                    //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    //startActivityForResult(signInIntent, RC_SIGN_IN_FACEBOOK);
                }
            }

        }else{
            Toast.makeText(GoogleSignInActivity.this, "Intent is null",
                    Toast.LENGTH_SHORT).show();
        }


    }
    // [END signin]




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void back(){
        Intent intent = new Intent(GoogleSignInActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.bt_signout:
                signOut();
                break;
        }
    }

    private void signOut() {

        FirebaseAuth.getInstance().signOut();
        // Google sign out


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                   public void onResult(@NonNull Status status) {
                        back();
                    }
                });

        //AuthUI.getInstance().signOut(this).addOnClompleteListener(new OnCompleteListener<>(){
        //    @Override
        //    public void onComplete(@NonNull Task task) {
         //       finish();
         //   }
        //});

        back();
        Toast.makeText(GoogleSignInActivity.this,"Signed out", Toast.LENGTH_SHORT).show();

    }

}

