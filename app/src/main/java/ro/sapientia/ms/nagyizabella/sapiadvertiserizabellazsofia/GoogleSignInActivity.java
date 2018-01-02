package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.User;

public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener
{
    /**
     * Database reference object
     */
    private DatabaseReference mDatabase;

    /**
     * Tag for the logging
     */
    private static final String TAG = "GoogleActivity";
    /**
     * It is the identification of google sign in
     */
    private static final int RC_SIGN_IN = 9001;
    /**
     * It is the identification of facebook sign in
     */
    private static final int RC_SIGN_IN_FACEBOOK = 9002;

    /**
     * The FirebaseAuth object
     */
    private FirebaseAuth mAuth;
    /**
     * Google api object
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Configure Google Sign In
     * Get database firebase database and firebase auth
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_auth]

        signIn();
    }

    /**
     * Check if user is signed in (non-null) and update UI accordingly.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }


    /**
     * This method runs after choose the google account(google or facebook)
     * @param requestCode It is an identification, one code
     * @param resultCode It is tell the result is oke
     * @param data It is the data
     */
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

                Intent advIntent = new Intent(GoogleSignInActivity.this, AdvertisementListActivity.class);
                advIntent.putExtra("Type", "allAdvertisement");
                startActivity(advIntent);
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

    /**
     * Authentification with google
     * @param acct
     */
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

                            try {
                                User userModel = new User(user.getEmail(), "", "", "", "");
                                mDatabase.child("users").child(user.getUid()).setValue(userModel);

                            }catch (NullPointerException e){
                                Log.d("GOOGLE",e.getMessage());
                            }



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

    /**
     * Open the google sign in panel
     * Select the google account
     */
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

    /**
     * If the connection faild
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Go to the back window
     */
    private void back(){
        Intent intent = new Intent(GoogleSignInActivity.this, SignInActivity.class);
        startActivity(intent);
    }

}

