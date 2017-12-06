package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters.RecyclerViewAdapter;
import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

public class AdvertisementListActivity extends BaseActivity implements View.OnClickListener{

    private String type; // all or my advertisement

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView newTextView;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    // [START declare_ref]
    private StorageReference mStorageRef;
    // [END declare_ref]

    private List<List<String>> firebaseElements = new ArrayList<List<String>>();
    private List<Advertisement> advertisements = new ArrayList<Advertisement>();
    private List<Uri> photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_list);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);

        Intent intent = getIntent();
        type = (String) intent.getExtras().getSerializable("Type");
        if(type == null){
            type = "allAdvertisement";
        }

        newTextView = (TextView) findViewById(R.id.tv_new);
        newTextView.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        recyclerView = (RecyclerView) findViewById(R.id.rv_advertisement);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<Advertisement> advertisementList = new ArrayList<Advertisement>();

        mAdapter = new RecyclerViewAdapter(advertisements);
        recyclerView.setAdapter(mAdapter);

        LoadAdvertisement();


    }

    private void LoadAdvertisement() {
        myRef.child("advertisements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());

                for (DataSnapshot adventisementKey : dataSnapshot.getChildren()) {
                    String title = (String) adventisementKey.child("title").getValue();
                    String detail = (String) adventisementKey.child("detail").getValue();
                    String user = (String) adventisementKey.child("userId").getValue();
                    ArrayList<String> photos = (ArrayList<String>) adventisementKey.child("photo").getValue();
                    //for (DataSnapshot ds : adventisementKey.child("photos").getChildren()) {
                    //    photos.add((String) ds.getValue());
                    //}
                    if(type.equals("myAdvertisement")){
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if(currentUser == null){
                            Toast.makeText(AdvertisementListActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AdvertisementListActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            if (currentUser.getUid().toString().equals(user)) {
                                Advertisement adv = new Advertisement(title, detail, "", user, photos);
                                advertisements.add(adv);
                            }
                        }
                    }else{ // all advertisement
                        Advertisement adv = new Advertisement(title, detail, "", user, photos);
                        advertisements.add(adv);
                    }

                }


                mAdapter = new RecyclerViewAdapter(advertisements);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(AdvertisementListActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                String td = "";
            }
        });


    }
    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.tv_new:
                newAdvertisement();
                break;
        }
    }

    private void newAdvertisement() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            Intent signInIntent = new Intent(AdvertisementListActivity.this, AddAdvertisementActivity.class);
            startActivity(signInIntent);
            finish();
        }else{
            Intent signInIntent = new Intent(AdvertisementListActivity.this, SignInActivity.class);
            startActivity(signInIntent);
            finish();
        }

    }
}
