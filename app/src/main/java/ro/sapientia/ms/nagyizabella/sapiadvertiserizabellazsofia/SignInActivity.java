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


    private static final String LOG_TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private DatabaseReference mDatabase;
    //the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignInButton;
    private Button mGoogleButton;
    private Button mFacebookButton;
    private TextView mForgetPassword;

    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
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

    private void writeNewUser(String userId, String email) {
        User user = new User(email, "", "", "", "");

        mDatabase.child("users").child(userId).setValue(user);
    }

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

    private void ForgetPassword() {
        mForgetPassword.setVisibility(View.INVISIBLE);
        mPassword.setVisibility(View.INVISIBLE);

        mSignInButton.setText("Send Email");
    }

    private void googleSignIn() {
        Intent signInIntent = new Intent(SignInActivity.this,GoogleSignInActivity.class);
        signInIntent.putExtra("type", "google");
        startActivity(signInIntent);
    }

    private void facebookSignIn() {
        //Intent signInIntent = new Intent(SignInActivity.this,GoogleSignInActivity.class);
        //signInIntent.putExtra("type", "facebook");
        //startActivity(signInIntent);
    }
}
