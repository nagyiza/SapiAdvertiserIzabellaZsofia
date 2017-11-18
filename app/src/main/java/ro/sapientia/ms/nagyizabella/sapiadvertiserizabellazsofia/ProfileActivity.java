package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProfileActivity";

    private DatabaseReference mDatabase;
    //the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;

    //Declare the fields
    private EditText EditEmail;
    private EditText EditFistName;
    private EditText EditLastName;
    private EditText EditPhoneNumbers;
    private EditText EditPassword;
    private EditText EditConfirmPassword;
/*
    private Button Myadvertiserment;
    private Button Save;
*/
    // Click listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views-> Unsigned Id-s
        EditEmail = (EditText)findViewById(R.id.et_email);
        EditFistName = (EditText)findViewById(R.id.first_name);
        EditLastName = (EditText)findViewById(R.id.last_name);
        EditPhoneNumbers = (EditText)findViewById(R.id.phone_number);
        EditPassword = (EditText)findViewById(R.id.et_password);
        EditConfirmPassword = (EditText)findViewById(R.id.confirm_password);

        Button saveEditButton = (Button)findViewById(R.id.bt_save);
        Button MyadvertisermentButton = (Button)findViewById(R.id.my_advertiserment);


        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileEmail = EditEmail.getText().toString();
                String profileFirstName = EditFistName.getText().toString();
                String profileLastName = EditLastName.getText().toString();
                String profilePhoneNumber = EditPhoneNumbers.getText().toString();
                String profilePassword = EditPassword.getText().toString();
                String profileConfirmPassword = EditConfirmPassword.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Intent firebaseUserIntent = new Intent(ProfileActivity.this,SignIn.class);
                    startActivity(firebaseUserIntent);
                    finish();
                } else {
                    String id = user.getUid();


                   User userEntity = new User(id, profileEmail,profileFirstName, profileLastName, profilePhoneNumber, profilePassword, profileConfirmPassword);
                  //  FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
                  //  firebaseDatabaseHelper.createUserInFirebaseDatabase(id, userEntity);

                    EditEmail.setText("");
                    EditFistName.setText("");
                    EditLastName.setText("");
                    EditPhoneNumbers.setText("");
                    EditPassword.setText("");
                    EditConfirmPassword.setText("");
                }

            }

        });
    }


}
