package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private Button MyadvertisermentButton;
    private Button saveEditButton;

    private ImageView profilePhoto;

    public ProfileActivity() {
    }

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

        saveEditButton = (Button)findViewById(R.id.bt_save);
        MyadvertisermentButton = (Button)findViewById(R.id.my_advertiserment);

//getCurrentUser to FireBase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

//Modifying data on Database
        mDatabase.child("users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EditEmail.setText(dataSnapshot.child("email").getValue().toString());
                EditFistName.setText(dataSnapshot.child("firstName").getValue().toString());
                EditLastName.setText(dataSnapshot.child("lastName").getValue().toString());
                EditPhoneNumbers.setText(dataSnapshot.child("phoneNumbers").getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


/*
        profilePhoto = (ImageView)findViewById(R.id.circleView);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2000);
            }
        });
        */
        //*--------------------------------------------

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileEmail = EditEmail.getText().toString();
                String profileFirstName = EditFistName.getText().toString();
                String profileLastName = EditLastName.getText().toString();
                String profilePhoneNumber = EditPhoneNumbers.getText().toString();
                String profilePassword = EditPassword.getText().toString();
                String profileConfirmPassword = EditConfirmPassword.getText().toString();

                if(validate(profileEmail, profileFirstName, profileLastName, profilePhoneNumber)) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Toast.makeText(ProfileActivity.this, "Not exist user", Toast.LENGTH_SHORT).show();
                    } else {
                        String id = user.getUid();
                        if (profileEmail != "") {
                            //TODO authentifikalasnal is valtozzon meg
                            mDatabase.child("users").child(id).child("email").setValue(profileEmail);
                        }

                        if (profileFirstName != "") {
                            mDatabase.child("users").child(id).child("firstName").setValue(profileFirstName);
                        }

                        if (profileLastName != "") {
                            mDatabase.child("users").child(id).child("lastName").setValue(profileLastName);
                        }

                        if (profilePhoneNumber != "") {
                            mDatabase.child("users").child(id).child("phoneNumbers").setValue(profilePhoneNumber);
                        }

                        if (profilePassword != "" && profilePassword == profileConfirmPassword) {
                            //TODO password megvaltoztatasa

                        }

                    }
                    Intent addAdvertisementIntent  = new Intent(ProfileActivity.this, AdvertisementListActivity.class);
                    startActivity(addAdvertisementIntent);
                    finish();
                }
            }

        });
    }
//Validate  profileEmail,length profileFirstName,length profileLastName,profilePhoneNumber
    private boolean validate(String profileEmail, String profileFirstName, String profileLastName, String profilePhoneNumber) {

        String MobilePattern = "[0-9]{10}";
        //String email1 = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (profileFirstName.length() > 15 || profileLastName.length() > 15) {
            if (profileFirstName.length() > 15 ) {
                Toast.makeText(getApplicationContext(), "pls enter less the 15 character in First Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (profileLastName.length() > 15 ) {
                Toast.makeText(getApplicationContext(), "pls enter less the 15 character in Last Name", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (profileFirstName.length() == 0 || profileLastName.length() == 0 || profilePhoneNumber.length() == 0 || profileEmail.length() ==
                0 ) {

            Toast.makeText(getApplicationContext(), "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;

        } else if (profileEmail.toString().matches(emailPattern)) {

            //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
            return true;

        } else if(!profileEmail.toString().matches(emailPattern)) {

            Toast.makeText(getApplicationContext(),"Please Enter Valid Email Address",Toast.LENGTH_SHORT).show();
            return false;

        } else if(profilePhoneNumber.toString().matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "phone number is valid", Toast.LENGTH_SHORT).show();
            return true;

        } else if(!profilePhoneNumber.toString().matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
     // writeNewUser(user.getUid(),username,user.getEmail(),user.getFirstName(),user.getLastName(),user.getPhoneNumer(),user.getPassword(),user.getConfirmPassword());

        // Go to Next Activity
        //startActivity(new Intent(SignInActivity.this, MainActivity.class));
        //finish();
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    // [START basic_write]
    private void writeNewUser(String userId,String profileEmail,String profileFirstName,String profileLastName,String profilePhoneNumber,String profilePassword,String profileConfirmPassword) {
      //  User user = new User(userId, profileEmail,profileFirstName, profileLastName, profilePhoneNumber, profilePassword, profileConfirmPassword);

      //  mDatabase.child("uj").child(userId).setValue(user);
    }
    // [END basic_write]

}
