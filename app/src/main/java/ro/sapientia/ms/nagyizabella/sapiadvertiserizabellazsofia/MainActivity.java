package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    /**
     * The button to the sign in or sign up
     * When click open the SignInActivity
     */
    private Button mSignInButton;
    /**
     * The button to the application, without login
     * When click open the AdvertisementListActivity, but the guest don't know add, remove advertisement, just see
     */
    private Button mGuestButton;

    /**
     * In the method find view elements by id and add listener on the buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignInButton = findViewById(R.id.signin);
        mGuestButton = findViewById(R.id.guest);
        // Click listeners
        mSignInButton.setOnClickListener(this);
        mGuestButton.setOnClickListener(this);

    }

    /**
     * Open the SignInActivity
     */
    private void signIn() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Check someone is login, if yes, logout
     * Open the AdvertisementListActivity
     */
    private void guest() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            FirebaseAuth.getInstance().signOut();
        }
        Intent intent = new Intent(MainActivity.this, AdvertisementListActivity.class);
        intent.putExtra("Type", "allAdvertisement");
        startActivity(intent);
        finish();
    }

    /**
     * This is a listener for the buttons
     * @param v this is view element, which clicked
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.signin:
                signIn();
                break;
            case R.id.guest:
                guest();
                break;
        }
    }
}

