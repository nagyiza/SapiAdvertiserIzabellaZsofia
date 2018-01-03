package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.User;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    /**
     * Tag for the logging
     */
    private static final String LOG_TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    /**
     * The database reference object
     */
    private DatabaseReference mDatabase;
    /**
     * The FirebaseAuth and AuthStateListener objects
     */
    private FirebaseAuth mAuth;
    /**
     * The edit text to user's Email
     */
    private EditText mEmail;
    /**
     * The edit text to user's Password
     */
    private EditText mPassword;
    /**
     * The button to the select SIGN IN
     */
    private Button mSignInButton;
    /**
     * The button to the select Google Sign In
     */
    private Button mGoogleButton;
    private GoogleApiClient mGoogleApiClient;

    /**
     * The button to the select Facebook Sign In
     */
    private Button mFacebookButton;
    /**
     * The text view to the Forget Password
     */
    private TextView mForgetPassword;

    /**
     * In the method find view elements by id and add listener on the buttons
     * Get database reference and firebase auth
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //firebase references
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views-> Unsigned Id-s
        mEmail = (EditText) findViewById(R.id.et_email);
        mPassword = (EditText) findViewById(R.id.et_password);
        mSignInButton = (Button) findViewById(R.id.bt_signin);
        mGoogleButton = (Button) findViewById(R.id.google_btn);
        mFacebookButton = (Button) findViewById(R.id.facebook_btn);
        mForgetPassword = (TextView) findViewById(R.id.forget_pssw);

        // Click listeners
        mSignInButton.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mFacebookButton.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            //onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    /**
     * This is a method for Sign IN
     * If Email and Password exist in Database->Sign In
     */
    private void signIn() {
        Log.d(LOG_TAG, "signIn");

        showProgressDialog();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(mSignInButton.getText().toString().equals("Send Email")){
            SendEmail(email);
            hideProgressDialog();
        }else {

            if (!validateForm()) {
                hideProgressDialog();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(LOG_TAG, "signIn:onComplete:" + task.isSuccessful());
                    hideProgressDialog();

                    if (task.isSuccessful()) {
                        //onAuthSuccess(task.getResult().getUser());
                        Toast.makeText(SignInActivity.this, "Sign In", Toast.LENGTH_SHORT).show();
                        Intent profileIntent = new Intent(SignInActivity.this, AdvertisementListActivity.class);
                        profileIntent.putExtra("Type", "allAdvertisement");
                        startActivity(profileIntent);
                        finish();
                    } else {
                        signUp();
                    }
                }
            });
        }
    }

    /**
     * For send Email to User
     * @param email User Email
     */
    private void SendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mForgetPassword.setVisibility(View.VISIBLE);
                            mPassword.setVisibility(View.VISIBLE);

                            mSignInButton.setText("SIGN IN / UP");

                        } else {
                            Toast.makeText(SignInActivity.this, "The email send failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    /**
     * This is a method for Sign UP
     * If Email and Password does not exist in Database->Sign Up
     */
    private void signUp() {
        Log.d(LOG_TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(SignInActivity.this, "Sign Up",
                                    Toast.LENGTH_SHORT).show();
                            Intent profileIntent = new Intent(SignInActivity.this, ProfileActivity.class);
                            startActivity(profileIntent);
                            finish();

                        } else {
                            Toast.makeText(SignInActivity.this, "Sign In and Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * This is a
     * @param user User id&email
     */
    private void onAuthSuccess(FirebaseUser user) {
        //String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewUser(user.getUid(), user.getEmail());

        // Go to Next Activity
        //startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
        //finish();
    }

    //private String usernameFromEmail(String email) {
    //    if (email.contains("@")) {
    //        return email.split("@")[0];
    //    } else {
    //       return email;
    //   }
    //}

    /**
     * In the method validate email and Password
     * @return result
     */
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError("Required");
            result = false;
        } else {
            mEmail.setError(null);
        }

        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError("Required");
            result = false;
        } else {
            mPassword.setError(null);
        }

        return result;
    }

    /**
     * This method write User Data
     * Here is the child sructure
     * @param userId User Id
     * @param email User Email
     */
    private void writeNewUser(String userId, String email) {
        User user = new User(email, "", "", "", "");

        mDatabase.child("users").child(userId).setValue(user);
    }

    /**
     * This is a listener for the buttons
     * @param v this is view element, which clicked
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.bt_signin:
                signIn();
                break;
            case R.id.google_btn:
                googleSignIn();
                break;
            case R.id.facebook_btn:
                facebookSignIn();
                break;
            case R.id.forget_pssw:
                ForgetPassword();
        }
    }

    /**
     * This is a function for forget password Wiew
     *
     */
    private void ForgetPassword() {
        mForgetPassword.setVisibility(View.INVISIBLE);
        mPassword.setVisibility(View.INVISIBLE);

        mSignInButton.setText("Send Email");
    }

    /**
     * This is a function for Google Sign In
     */
    private void googleSignIn() {
        Intent signInIntent = new Intent(SignInActivity.this,GoogleSignInActivity.class);
        signInIntent.putExtra("type", "google");
        startActivity(signInIntent);
    }
    /**
     * This is a function for Facebook Sign In
     */
    private void facebookSignIn() {
        //Intent signInIntent = new Intent(SignInActivity.this,GoogleSignInActivity.class);
        //signInIntent.putExtra("type", "facebook");
        //startActivity(signInIntent);
    }
}
