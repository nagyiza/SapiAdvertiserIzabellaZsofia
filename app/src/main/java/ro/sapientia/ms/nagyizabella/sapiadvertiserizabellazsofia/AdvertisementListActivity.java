package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters.RecyclerViewAdapter;
import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

public class AdvertisementListActivity extends AppCompatActivity implements View.OnClickListener{

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

        mAdapter = new RecyclerViewAdapter(advertisements, photo);
        recyclerView.setAdapter(mAdapter);

        LoadAdvertisement();
    }

    private void LoadAdvertisement() {
        myRef.child("advertisements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());
                Object o = dataSnapshot.getValue();
                HashMap<String, HashMap<String,String>> hm = (HashMap<String, HashMap<String,String>>) o;

                Object advertisemenetItem;
                for (Object obj : hm.keySet()) {
                    advertisemenetItem = hm.get(obj.toString());
                    HashMap<String, HashMap<String,String>> hm2 = (HashMap<String, HashMap<String,String>>) advertisemenetItem;

                    Object title = hm2.get("title");
                    Object detail = hm2.get("detail");
                    Object user = hm2.get("userId");
                    Object photos = hm2.get("photos");

                    //TODO photo
                    Advertisement adv = new Advertisement(title.toString(),detail.toString(), "", user.toString(), (List<Uri>)photos);
                    advertisements.add(adv);

                    //photo
                    //mStorageRef = FirebaseStorage.getInstance().getReference();

                    //Uri uris =  mStorageRef.child("AdvertisementPhotos").child(obj.toString()).getDownloadUrl();
                    //photo.add(uris);
                    //String ss= "";
                    /*
                    try {
                        final File localFile = File.createTempFile("images", "jpg");
                        mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                String s = "";
                            }
                        });
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    */

                }
                mAdapter.notifyDataSetChanged();
                //Advertisement advertisement = new Advertisement(element.get(0),"","","","");
                //notifyDataSetChanged();
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
        Intent signInIntent = new Intent(AdvertisementListActivity.this, AddAdvertisementActivity.class);
        startActivity(signInIntent);
    }
}
