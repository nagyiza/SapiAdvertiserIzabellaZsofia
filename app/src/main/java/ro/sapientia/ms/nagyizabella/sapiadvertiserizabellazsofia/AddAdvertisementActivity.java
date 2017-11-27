package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters.AddImageAdapter;
import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

public class AddAdvertisementActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "AddAdvertisementActivity";

    private int RESULT_LOAD_IMAGE = 1;

    private DatabaseReference mDatabase;
    //the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;

    //Declare the fields
    private EditText EditTitle;
    private EditText EditDetail;
    private GridView ImageList;

    private Button ButtonLocation;
    private Button ButtonAddImage;
    private Button ButtonAddAdvertisement;

    //image list
    private List<String> imagesEncodedList;
    private ArrayList<Uri>  imageURIs;
    private String imageEncoded;
    private ArrayList<Bitmap> list;

    // [START declare_ref]
    private StorageReference mStorageRef;
    // [END declare_ref]
    private String adventisementKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views-> Unsigned Id-s
        EditTitle = (EditText)findViewById(R.id.adver_title);
        EditDetail = (EditText)findViewById(R.id.adver_text);
        ImageList =(GridView)findViewById(R.id.list_image);
        ButtonLocation = (Button)findViewById(R.id.adver_location);
        ButtonAddImage = (Button)findViewById(R.id.adver_image);
        ButtonAddAdvertisement = (Button)findViewById(R.id.add_adver);

        ButtonLocation.setOnClickListener(this);
        ButtonAddImage.setOnClickListener(this);
        ButtonAddAdvertisement.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.adver_location:
                AddLocation();
                break;
            case R.id.adver_image:
                AddImage();
                break;
            case R.id.add_adver:
                AddAdvertisement();
                break;
        }
    }

    private void AddAdvertisement() {
        String title = EditTitle.getText().toString();
        String detail = EditDetail.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(AddAdvertisementActivity.this, "Not exist user", Toast.LENGTH_SHORT).show();
        } else {
            if(title.length() != 0 && detail.length() != 0) {
                String id = user.getUid();

                mDatabase = FirebaseDatabase.getInstance().getReference("advertisements");
                String key =  mDatabase.push().getKey();

                //save image's download uri in database
                //TODO
                ImageSave(imageURIs, key);
                Advertisement adv = new Advertisement(title, detail, "",id, downloadUri);
                mDatabase.child(key).setValue(adv);
                mDatabase.child(key).child("photos");

                Intent intent = new Intent(AddAdvertisementActivity.this, AdvertisementListActivity.class);
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(AddAdvertisementActivity.this, "The input is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void AddImage() {
        //Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, RESULT_LOAD_IMAGE);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
*/
    }

    private void AddLocation() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageList =(GridView)findViewById(R.id.list_image);
        list = new ArrayList<Bitmap>();
        imageURIs = new ArrayList<Uri>();
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    imageURIs.add(mImageUri);
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);

                    list.add(Bitmap.createScaledBitmap(bitmap, 120, 120, false));

                    ImageList = findViewById(R.id.list_image);
                    AddImageAdapter imageAdapter = new AddImageAdapter(this, R.layout.image_item, list);
                    ImageList.setAdapter(imageAdapter);


                } else {
                    // When more Images are picked
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        if (mClipData.getItemCount() > 4) {
                            Toast.makeText(this, "Maximum 4 images can be selected!", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                imageURIs.add(uri);
                                // Get the cursor
                                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();
                                Bitmap bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri), 120, 120, false);
                                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                list.add(bitmap);
                            }
                            ImageList = findViewById(R.id.list_image);
                            AddImageAdapter imageAdapter = new AddImageAdapter(this, R.layout.image_item, list);
                            ImageList.setAdapter(imageAdapter);



                            Log.d("LOG_TAG", "Selected Images" + imageURIs.size());
                        }
                    }

                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong" + e, Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void ImageSave(ArrayList<Uri> mImageUri, String advertisementKey) {
        uploadFromUri(mImageUri, advertisementKey);
    }
    List<Uri> downloadUri = new ArrayList<Uri>();
    private void uploadFromUri(ArrayList<Uri> fileUri, final String advertisementKey) {

        mStorageRef = FirebaseStorage.getInstance().getReference();
        this.adventisementKey = advertisementKey;
        for (Uri photoUri: fileUri)
        {
            // [START get_child_ref]
            // Get a reference to store file at photos/<FILENAME>.jpg

            final StorageReference photoRef = mStorageRef.child("AdvertisementPhotos").child(advertisementKey).child(photoUri.getLastPathSegment());
            // [END get_child_ref]

            photoRef.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddAdvertisementActivity.this, "Upload succes ", Toast.LENGTH_SHORT).show();
                    //TODO
                    downloadUri.add(taskSnapshot.getMetadata().getDownloadUrl());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddAdvertisementActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
