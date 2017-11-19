package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.User;

public class SignIn extends BaseActivity implements View.OnClickListener{

    
    private static final String LOG_TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private DatabaseReference mDatabase;
    //the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignInButton;
    private Button mGoogleButton;
    private Button mFacebookButton;

    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mSignInButton = findViewById(R.id.bt_signin);
        mGoogleButton = findViewById(R.id.google_btn);
        mFacebookButton = findViewById(R.id.facebook_btn);

        // Click listeners
        mSignInButton.setOnClickListener(this);
        mGoogleButton.setOnClickListener(this);
        mFacebookButton.setOnClickListener(this);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void signIn() {
        Log.d(LOG_TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            //onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(SignIn.this, "Sign In",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            signUp();
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
                            Toast.makeText(SignIn.this, "Sign Up",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(SignIn.this, "Sign In and Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());


        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to Next Activity
        startActivity(new Intent(SignIn.this, ProfileActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

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

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(email);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]

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
        }
    }

    private void googleSignIn() {
        Intent signInIntent = new Intent(SignIn.this,GoogleSignInActivity.class);
        signInIntent.putExtra("type", "google");
        startActivity(signInIntent);
    }

    private void facebookSignIn() {
        Intent signInIntent = new Intent(SignIn.this,GoogleSignInActivity.class);
        signInIntent.putExtra("type", "facebook");
        startActivity(signInIntent);
    }

}