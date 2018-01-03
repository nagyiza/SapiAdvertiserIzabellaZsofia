package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

public class AdvertisementData extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    /**
     * The Image view to advertisement's images
     */
    private ImageView image;
    /**
     * The text View to next advertiserment's image
     */
    private TextView next;
    /**
     * The text View to previous advertiserment's image
     */
    private TextView prev;
    /**
     * The text View to advertisement title
     */
    private TextView title;
    /**
     * The text View to advertisement detail
     */
    private TextView detail;
    /**
     * The Image View to Profile Image
     */
    private ImageView profileImage;
    /**
     * The text View to User Name
     */
    private TextView user;
    /**
     * The text View to User Phone Numer
     */
    private TextView phone;
    /**
     * The button to the select User Call
     */
    private Button call;
    /**
     * The advertisement
     */
    private Advertisement adv;
    /**
     * The cursor list when more images are picked
     */
    private List<String> images;
    /**
     * The User Phone Numer for calling
     */
    private String phoneNumber;
    /**
     * The FirebaseAuth and AuthStateListener objects
     */
    private FirebaseDatabase database;
    /**
     * The database reference object
     */
    private DatabaseReference myRef;

    //for maps
    private GoogleMap mMap;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mylocation;
    private LatLng locationPos;

    /**
     *In the method find view elements by id and add listener on the buttons
     * Get database reference and firebase auth
     * Display menu
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_data);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);

        //firebase references
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        // Views-> Unsigned Id-s
        image = (ImageView) findViewById(R.id.idImageViewSemafor);
        next =(TextView) findViewById(R.id.next_image);
        prev =(TextView) findViewById(R.id.prev_image);

        title = (TextView) findViewById(R.id.title_data);
        detail = (TextView) findViewById(R.id.adver_text);
        profileImage = (ImageView)findViewById(R.id.cv_profileImage);
        user = (TextView) findViewById(R.id.creator);
        phone = (TextView) findViewById(R.id.phone_number);
        call = (Button) findViewById(R.id.bt_call);
        //listeners
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        call.setOnClickListener(this);

        Intent intent = getIntent();
        adv = (Advertisement) intent.getExtras().getSerializable("Advertisement");

        AdvertisementDetail();

        images = adv.getPhoto();
        if(images == null){
            images = new ArrayList<String>();
            images.add("");
        }
        AdvertisementImages("");

        //for maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

    }


    private int imagePosition = 0;

    /**
     * This is a method  for advertiserments Image Views
     * @param direction
     */
    private void AdvertisementImages(String direction) {
        /**
         * Next advertiserment's image
         */
        if(direction.equals("next")){
            imagePosition = imagePosition + 1;
            if(imagePosition == images.size()){
                imagePosition = 0;
            }
        }
        /**
         * Previous advertiserment's image
         */
        else{
            if(direction.equals("prev")){
                imagePosition = imagePosition - 1;
                if(imagePosition < 0){
                    imagePosition = images.size()-1;
                }
            }
        }

        if (images.get(imagePosition) != null && images.get(imagePosition).length() != 0) {
            Glide.with(AdvertisementData.this).load(images.get(imagePosition))
                    .into(image);
        }
    }

    /**
     * This is a method for the Actualizate Advertisement Datail
     */
    private void AdvertisementDetail() {
        title.setText(adv.getTitle());
        detail.setText(adv.getDetail());

        String loc = adv.getLocation();
        String[] position = loc.split(";");
        locationPos = new LatLng( Double.valueOf(position[0]).doubleValue(), Double.valueOf(position[1]).doubleValue());

        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userKey : dataSnapshot.getChildren()) {
                    if(userKey.getKey().equals(adv.getUserId())) {
                        String email = (String) userKey.child("email").getValue();
                        String firstName = (String) userKey.child("firstName").getValue();
                        String lastName = (String) userKey.child("lastName").getValue();
                        phoneNumber = (String) userKey.child("phoneNumbers").getValue();
                        String profilImage = (String) userKey.child("profilImage").getValue();

                        user.setText(firstName + " " + lastName);
                        phone.setText(phoneNumber);

                        if (profilImage != null && profilImage.length() != 0) {
                            Glide.with(AdvertisementData.this).load(profilImage)
                                    .override(50, 50)
                                    .into(profileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        //child(adv.getUserId()).child("firstName").getKey();
    }

    /**
     * This is a listener for the buttons
     * @param v this is view element, which clicked
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.next_image:
                AdvertisementImages("next");
                break;
            case R.id.prev_image:
                AdvertisementImages("prev");
                break;
            case R.id.bt_call:
                callCreator();
                break;
        }
    }

    /**
     * This is a method for the Creator calling
     */
    private void callCreator() {
        if(phoneNumber.length() != 0){
            Intent callIntent =new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.fromParts("tel", phoneNumber, null));
            startActivity(callIntent);
        }else{
            Toast.makeText(AdvertisementData.this, "Not is phone number", Toast.LENGTH_LONG).show();
        }

    }

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
                //finish();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permissions.permissionRequest(this, permissions.permissions, permissions.PERMISSION_KEY);
            Toast.makeText(AdvertisementData.this, "The permissions are oke",Toast.LENGTH_SHORT).show();
        }
        mMap.setMyLocationEnabled(true);
        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permissions.permissionRequest(this, permissions.permissions, permissions.PERMISSION_KEY);
            Toast.makeText(AdvertisementData.this, "The permissions are oke",Toast.LENGTH_SHORT).show();

        }
        final Double mylatitude ,mylongitude;
        final LatLng advlocation = new LatLng(locationPos.latitude, locationPos.longitude);
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
            mMap.addMarker(new MarkerOptions().position(advlocation).title("Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(advlocation, 12));
        }
    }
}
