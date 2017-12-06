package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Button mSignInButton;
    private Button mGuestButton;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignInButton = findViewById(R.id.signin);
        mGuestButton = findViewById(R.id.guest);
        // Click listeners
        mSignInButton.setOnClickListener(this);
        mGuestButton.setOnClickListener(this);


        // Write a message to the database
        //try {
        //    database = FirebaseDatabase.getInstance();
        //    myRef = database.getReference();

        //    myRef.child("izabella");
        //    myRef.setValue("valami").addOnFailureListener(new OnFailureListener() {
        //       @Override
        //        public void onFailure(@NonNull Exception e) {
        //           Log.d(LOG_TAG, e.getLocalizedMessage());
        //       }
        //   });
        //   Toast.makeText(this,"success", Toast.LENGTH_LONG).show();

        //}catch(Exception e){
        //    Toast.makeText(this,"failed", Toast.LENGTH_LONG).show();
        //}

    }



    private void signIn() {
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

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

