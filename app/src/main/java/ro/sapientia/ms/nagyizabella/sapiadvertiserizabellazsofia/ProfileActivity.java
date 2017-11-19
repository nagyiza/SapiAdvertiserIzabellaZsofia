package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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

    private Button MyadvertisermentButton;
    private Button saveEditButton;

    private ImageView profilePhoto;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        EditEmail.setText(mDatabase.child(id).child("email").toString());
        EditFistName.setText(mDatabase.child(id).child("firstName").toString());
        EditLastName.setText(mDatabase.child(id).child("lastName").toString());
        EditPhoneNumbers.setText(mDatabase.child(id).child("phoneNumbers").toString());


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

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Toast.makeText(ProfileActivity.this, "Not exist user", Toast.LENGTH_SHORT).show();
                } else {
                    String id = user.getUid();
                    if(profileEmail != null){
                        mDatabase.child(id).child("email").setValue(profileEmail);
                    }

                    if(profileFirstName != null){
                        mDatabase.child(id).child("email").setValue(profileFirstName);
                    }

                    if(profileLastName != null){
                        mDatabase.child(id).child("email").setValue(profileLastName);
                    }

                    if(profilePhoneNumber != null){
                        mDatabase.child(id).child("email").setValue(profilePhoneNumber);
                    }

                    if(profilePassword != null && profilePassword == profileConfirmPassword){
                        //TODO password megvaltoztatasa
                    }

                }
            }

        });
    }


    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
     // writeNewUser(user.getUid(),username,user.getEmail(),user.getFirstName(),user.getLastName(),user.getPhoneNumer(),user.getPassword(),user.getConfirmPassword());

        // Go to Next Activity
        //startActivity(new Intent(SignIn.this, MainActivity.class));
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
