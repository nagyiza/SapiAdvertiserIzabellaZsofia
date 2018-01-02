package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

public class AdvertisementData extends BaseActivity implements View.OnClickListener{

    /**
     * The Image view to advertisement's images
     */
    private ImageView image;
    private TextView next;
    private TextView prev;

    /**
     * The text View to advertisement title
     */
    private TextView title;
    /**
     * The text View to advertisement detail
     */
    private TextView detail;

    //maps

    private ImageView profileImage;
    private TextView user;
    private TextView phone;
    private Button call;

    private Advertisement adv;
    private List<String> images;
    private String phoneNumber; //for calling

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_data);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);

        //database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        image = (ImageView) findViewById(R.id.idImageViewSemafor);
        next =(TextView) findViewById(R.id.next_image);
        prev =(TextView) findViewById(R.id.prev_image);

        title = (TextView) findViewById(R.id.title_data);
        detail = (TextView) findViewById(R.id.adver_text);
        profileImage = (ImageView)findViewById(R.id.cv_profileImage);
        user = (TextView) findViewById(R.id.creator);
        phone = (TextView) findViewById(R.id.phone_number);
        call = (Button) findViewById(R.id.bt_call);

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
    }

    private int imagePosition = 0;
    private void AdvertisementImages(String direction) {
        if(direction.equals("next")){
            imagePosition = imagePosition + 1;
            if(imagePosition == images.size()){
                imagePosition = 0;
            }
        }else{
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

    private void AdvertisementDetail() {
        title.setText(adv.getTitle());
        detail.setText(adv.getDetail());

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

    private void callCreator() {
        if(phoneNumber.length() != 0){
            Intent callIntent =new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.fromParts("tel", phoneNumber, null));
            startActivity(callIntent);
        }else{
            Toast.makeText(AdvertisementData.this, "Not is phone number", Toast.LENGTH_LONG).show();
        }

    }
}
