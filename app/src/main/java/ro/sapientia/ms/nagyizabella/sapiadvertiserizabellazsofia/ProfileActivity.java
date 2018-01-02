package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends BaseActivity implements  View.OnClickListener{
    /**
     * Tag for the profile
     */
    private static final String LOG_TAG = "ProfileActivity";
    /**
     * It is the identification
     */
    private int RESULT_LOAD_IMAGE = 1;
    /**
     * The database reference object
     */
    private DatabaseReference mDatabase;
    /**
     * The FirebaseAuth object
     */
    private FirebaseAuth mAuth;
    //Declare the fields
    /**
     * The edit text to profile email
     * The edit text is in the activity_profile.xml
     */
    private EditText EditEmail;
    /**
     * The edit text to profile First Name
     * The edit text is in the activity_profile.xml
     */
    private EditText EditFistName;
    /**
     * The edit text to profile Last Name
     * The edit text is in the activity_profile.xml
     */
    private EditText EditLastName;
    /**
     * The edit text to profile Phone Number
     * The edit text is in the activity_profile.xml
     */
    private EditText EditPhoneNumbers;
    /**
     * The edit text to profile Password
     * The edit text is in the activity_profile.xml
     */
    private EditText EditPassword;
    /**
     * The edit text to profile Confirm Password
     * The edit text is in the activity_profile.xml
     */
    private EditText EditConfirmPassword;
    /**
     * The text view to profile Profile Name
     * The text view is in the activity_profile.xml
     */
    private TextView ProfileName;
    /**
     * The text view to profile Phone Number
     * The text view is in the activity_profile.xml
     */
    private TextView PhoneNumber;
    /**
     * The button to the select My Advertiserment
     */
    private Button MyadvertisermentButton;
    /**
     * The button to the select Save Profile Data
     */
    private Button saveEditButton;
    /**
     * The image to the profile Photo View
     */
    private ImageView profilePhoto;

     //For Image
    /**
     * The cursor when an images is picked
     */
    private String imagesEncoded;
    /**
     * Image in format bitmap
     */
    private Bitmap bitmap = null; // list
    private Uri imageURI = null;
    private LayoutInflater inflater;
    /**
     * For Image Save
     */
    /**
     * When upload a picture in the storage, get bach the download uri
     * In this list are the pictures's download uris, which save in the database
     */
    private String downloadUri;
    /**
     * It is a counter, which count the pictures.
     */
    private int counter;
    /**
     * The storage reference object
     * In the firebase's sorage upload the pictures
     */
    private StorageReference mStorageRef;

    // Click listeners
    /**
     * In the method find view elements by id and add listener on the buttons
     * Get database reference and firebase auth
     * Display menu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);

        //Firebase references
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views-> Unsigned Id-s
        EditEmail = (EditText)findViewById(R.id.et_email);
        EditFistName = (EditText)findViewById(R.id.first_name);
        EditLastName = (EditText)findViewById(R.id.last_name);
        EditPhoneNumbers = (EditText)findViewById(R.id.phone_number);
        EditPassword = (EditText)findViewById(R.id.et_password);
        EditConfirmPassword = (EditText)findViewById(R.id.confirm_password);

        ProfileName = (TextView)  findViewById(R.id.profile_name);
        PhoneNumber = (TextView) findViewById(R.id.tel);

        saveEditButton = (Button)findViewById(R.id.bt_save);
        MyadvertisermentButton = (Button)findViewById(R.id.my_advertiserment);

        profilePhoto = (ImageView)findViewById(R.id.cv_profileImage);

        //get Current User to FireBase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        //Get user's data in database and write in view
        writeUserData(id);

        //listeners
        profilePhoto.setOnClickListener(this);
        saveEditButton.setOnClickListener(this);
        MyadvertisermentButton.setOnClickListener(this);

    }

/*
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }*/

    /**
     * This method runs after choose the pictures
     * Get data in the param data, this is the image uri
     * Save bitmap in the list, and save cursors.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        imageURI = null;
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    imageURI = mImageUri;
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagesEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);

                    bitmap = Bitmap.createScaledBitmap(bp, 50, 50, false);


                   profilePhoto.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, false));


                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong" + e, Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    /**
     * In the method the image is saved in the firebase Storage
     * @param mImageUri
     */
    private void ImageSave(Uri mImageUri) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        counter = 1;
        final String key =  mDatabase.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        final StorageReference photoRef = mStorageRef.child("UserProfilePhotos").child(id).child(mImageUri.getLastPathSegment());

        photoRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfileActivity.this, "Upload succes ", Toast.LENGTH_SHORT).show();

                downloadUri = taskSnapshot.getDownloadUrl().toString();

                if(counter <= 2) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String id = user.getUid();

                    mDatabase.child("users").child(id).child("profilImage").setValue(downloadUri);

                    Intent intent = new Intent(ProfileActivity.this, AdvertisementListActivity.class);
                    intent.putExtra("Type", "allAdvertisement");
                    startActivity(intent);
                    finish();
                }

                counter++;


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * In the method validate profileEmail,profileFirstName,profileLastName and profilePhoneNumber.
     *
     * @param profileEmail
     * @param profileFirstName
     * @param profileLastName
     * @param profilePhoneNumber
     * @return
     */
    private boolean validate(String profileEmail, String profileFirstName, String profileLastName, String profilePhoneNumber) {

        String MobilePattern = "[0-9]{10}";
        //String email1 = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (profileFirstName.length() > 15 || profileLastName.length() > 15) {
            if (profileFirstName.length() > 15 ) {
                Toast.makeText(getApplicationContext(), "Enter less the 15 character in First Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (profileLastName.length() > 15 ) {
                Toast.makeText(getApplicationContext(), "Enter less the 15 character in Last Name", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (profileFirstName.length() == 0 || profileLastName.length() == 0 || profilePhoneNumber.length() == 0 || profileEmail.length() ==
                0 ) {

            Toast.makeText(getApplicationContext(), "Fill the empty fields", Toast.LENGTH_SHORT).show();
            return false;

        }

        else if(!profileEmail.toString().matches(emailPattern)) {

            Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
            return false;

        } else if(!profilePhoneNumber.toString().matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "Enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    /**
     * This is a listener for the buttons
     * @param v this is view element, whitch clicked
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.bt_save:
                saveAdvertisement();
                break;
            case R.id.my_advertiserment:
                myAdvertisement();
                break;
            case R.id.cv_profileImage:
                changeProfileImage();
        }
    }

    /**
     * This method is modify(change) the Profile Image
     */
    private void changeProfileImage() {
        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
    }

    /**
     * In the method is a intent call to AdvertisementListActivity
     */
    private void myAdvertisement() {
        Intent intent  = new Intent(ProfileActivity.this, AdvertisementListActivity.class);
        intent.putExtra("Type", "myAdvertisement");
        startActivity(intent);
        finish();
    }

    /**
     * In the method saved
     */
    private void saveAdvertisement() {
        //   showProgressDialog();
        String profileEmail = EditEmail.getText().toString();
        String profileFirstName = EditFistName.getText().toString();
        String profileLastName = EditLastName.getText().toString();
        String profilePhoneNumber = EditPhoneNumbers.getText().toString();
        final String profilePassword = EditPassword.getText().toString();
        String profileConfirmPassword = EditConfirmPassword.getText().toString();

        if(validate(profileEmail, profileFirstName, profileLastName, profilePhoneNumber)) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(ProfileActivity.this, "Not exist user", Toast.LENGTH_SHORT).show();
            } else {

                String id = user.getUid();
                if (profileEmail != "") {
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

                    AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
                    final String email = user.getEmail();

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(profilePassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Password Successfully Modified", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                if (bitmap != null) {
                    ImageSave(imageURI);
                } else {
                    Intent addAdvertisementIntent = new Intent(ProfileActivity.this, AdvertisementListActivity.class);
                    addAdvertisementIntent.putExtra("Type", "allAdvertisement");
                    startActivity(addAdvertisementIntent);
                    //finish();
                }


            }
        }
        }

    /**
     * This method write User Data
     * Here is the child sructure
     * @param id
     */
    public void writeUserData(String id) {
        mDatabase.child("users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                EditEmail.setText(dataSnapshot.child("email").getValue().toString());
                EditFistName.setText(dataSnapshot.child("firstName").getValue().toString());
                EditLastName.setText(dataSnapshot.child("lastName").getValue().toString());
                String profileName = dataSnapshot.child("firstName").getValue().toString() + " " + dataSnapshot.child("lastName").getValue().toString();
                if (!profileName.equals("")) {
                    ProfileName.setText(profileName);
                }else{
                    ProfileName.setText("Name");
                }
                EditPhoneNumbers.setText(dataSnapshot.child("phoneNumbers").getValue().toString());
                PhoneNumber.setText(EditPhoneNumbers.getText());

                String image = dataSnapshot.child("profilImage").getValue().toString();
                if (image != null && image.length() != 0) {
                    Glide.with(ProfileActivity.this).load(image)
                            .override(50, 50)
                            .into(profilePhoto);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }
}
