package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class AddAdvertisementActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    /**
     * Tag for the logging
     */
    private static final String LOG_TAG = "AddAdvertisementActivity";

    /**
     * It is the identification
     */
    private int RESULT_LOAD_IMAGE = 1;

    /**
     * The new advertisement's title
     */
    public String title;
    /**
     * The new advertisement's detail
     * In detail is written the advertisement's locale, date and short description
     */
    public String detail;
    /**
     * The user id, which take from firebase
     */
    public String id;
    /**
     * The database reference object
     */
    private DatabaseReference mDatabase;
    /**
     * The FirebaseAuth and AuthStateListener objects
     */
    private FirebaseAuth mAuth;

    //Declare the fields
    /**
     * The edit text to advertisement's title
     * The edit text is in the activity_add_advertisement.xml
     */
    private EditText EditTitle;
    /**
     * The edit text to advertisement's detail
     * The edit text is in the activity_add_advertisement.xml
     */
    private EditText EditDetail;
    /**
     * The grid view to advertisement's images
     * Grid view because it is possible to download more pictures
     * The grid view is in the activity_add_advertisement.xml
     */
    private GridView ImageList;

    /**
     * The button to the add images
     */
    private Button ButtonAddImage;
    /**
     * The button to the add the advertisement in the database
     */
    private Button ButtonAddAdvertisement;

    //image list
    /**
     * The cursor list when more images are picked
     */
    private List<String> imagesEncodedList;
    /**
     * The list, in which are images's uri's
     * The uris need for the displaying pictures
     */
    private ArrayList<Uri>  imageURIs;
    /**
     * The cursor when an images is picked
     */
    private String imageEncoded;
    /**
     * List for images, which are in format bitmap
     */
    private ArrayList<Bitmap> list;
    /**
     * When upload a picture in the storage, get bach the download uri
     * In this list are the pictures's download uris, which save in the database
     */
    private List<String> downloadUri = new ArrayList<String>();
    /**
     * It is a counter, which count the pictures.
     */
    private int counter;


    /**
     * The storage reference object
     * In the firebase's sorage upload the pictures
     */
    private StorageReference mStorageRef;


    //for maps
    private GoogleMap mMap;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mylocation;
    private LatLng locationPos;

    /**
     * In the method find view elements by id and add listener on the buttons
     * Set database reference and firebase auth
     * Display menu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);

        //firebase references
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views-> Unsigned Id-s
        EditTitle = (EditText)findViewById(R.id.adver_title);
        EditDetail = (EditText)findViewById(R.id.adver_text);
        ImageList =(GridView)findViewById(R.id.list_image);
        ButtonAddImage = (Button)findViewById(R.id.adver_image);
        ButtonAddAdvertisement = (Button)findViewById(R.id.add_adver);

        //listeners
        ButtonAddImage.setOnClickListener(this);
        ButtonAddAdvertisement.setOnClickListener(this);

        //for maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

    }

    /**
     * This is a listener for the buttons
     * @param view this is view element, which clicked
     */
    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i) {
            case R.id.adver_image:
                AddImage();
                break;
            case R.id.add_adver:
                AddAdvertisement();
                break;
        }
    }

    /**
     * This method get the advertisement title and detail
     * Call the AdvertisementSave method, witch save the advertisement, if someone is entering
     */
    private void AddAdvertisement() {
        showProgressDialog();

        title = EditTitle.getText().toString();
        detail = EditDetail.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(AddAdvertisementActivity.this, "Not exist user", Toast.LENGTH_SHORT).show();
        } else {
            if(title.length() != 0 && detail.length() != 0) {
                id = user.getUid();

                mDatabase = FirebaseDatabase.getInstance().getReference("advertisements");

                //save image's download uri in database
                AdvertisementSave(imageURIs);

            }else{
                Toast.makeText(AddAdvertisementActivity.this, "The input is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method open the gallery, from where need to choose pictures
     */
    private void AddImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
    }


    /**
     * This method runs after choose the pictures
     * Get data in the param data, this is the image uri
     * Save bitmap in the list, and save cursors.
     * Display the images in the grid view with an adapter
     * @param requestCode It is an identification, one code
     * @param resultCode It is tell the result is oke
     * @param data  It is the data, so the pictures
     */
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

    /**
     * This method save the advertisement in the firebase and save images in the firebase storage
     * @param mImageUri The picture's uris
     */
    private void AdvertisementSave(final ArrayList<Uri> mImageUri) {

        mStorageRef = FirebaseStorage.getInstance().getReference();
        counter = 1;
        Double lat = locationPos.latitude;
        Double lon = locationPos.longitude;
        final String loc = lat.toString() + ";" + lon.toString();

        final String key =  mDatabase.push().getKey();
        if(mImageUri == null){
            Advertisement adv = new Advertisement(title, detail, loc, id, downloadUri);
            adv.setId(key);
            adv.setLocation(loc);
            mDatabase.child(key).setValue(adv);
            hideProgressDialog();

            Intent intent = new Intent(AddAdvertisementActivity.this, AdvertisementListActivity.class);
            intent.putExtra("Type", "allAdvertisement");
            startActivity(intent);
            finish();
        }else {
            for (Uri photoUri : mImageUri) {
                // [START get_child_ref]
                // Get a reference to store file at photos/<FILENAME>.jpg

                final StorageReference photoRef = mStorageRef.child("AdvertisementPhotos").child(key).child(photoUri.getLastPathSegment());
                // [END get_child_ref]

                photoRef.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddAdvertisementActivity.this, "Upload succes ", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();

                        downloadUri.add(taskSnapshot.getDownloadUrl().toString());

                        if (counter == mImageUri.size()) {
                            Advertisement adv = new Advertisement(title, detail, loc, id, downloadUri);
                            adv.setId(key);
                            mDatabase.child(key).setValue(adv);

                            Intent intent = new Intent(AddAdvertisementActivity.this, AdvertisementListActivity.class);
                            intent.putExtra("Type", "allAdvertisement");
                            startActivity(intent);
                            finish();
                        }

                        counter++;
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


    //for map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitude", latitude);
                returnIntent.putExtra("longitude", longitude);
                setResult(Activity.RESULT_OK, returnIntent);
                locationPos = new LatLng(latitude, longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(locationPos).title("Location"));
                //finish();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permissions.permissionRequest(this, permissions.permissions, permissions.PERMISSION_KEY);
            Toast.makeText(AddAdvertisementActivity.this, "The permissions are oke",Toast.LENGTH_SHORT).show();
        }
        mMap.setMyLocationEnabled(true);
        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permissions.permissionRequest(this, permissions.permissions, permissions.PERMISSION_KEY);
            Toast.makeText(AddAdvertisementActivity.this, "The permissions are oke",Toast.LENGTH_SHORT).show();

        }
        final Double mylatitude ,mylongitude;
        final LatLng targuMures = new LatLng(46.55, 24.5667);
        locationPos = new LatLng(46.55, 24.5667);
        if(mylocation != null) {
            mylatitude = mylocation.getLatitude();
            mylongitude = mylocation.getLongitude();
            final Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        mylocation = (Location) task.getResult();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(mylatitude, mylongitude)));
                    }
                }
            });
        }else{
            mMap.addMarker(new MarkerOptions().position(targuMures).title("Marker in Targu Mures"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targuMures, 12));
        }
    }
}
