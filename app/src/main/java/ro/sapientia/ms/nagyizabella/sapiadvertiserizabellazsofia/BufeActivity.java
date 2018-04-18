package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters.RecyclerViewAdapter;
import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

public class BufeActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAdapter.ItemClickListener, RecyclerViewAdapter.ItemLongClickListener{

    /**
     * The recycler view to list of advertisement
     */
    private RecyclerView recyclerView;
    /**
     * Recycler view adapter object
     */
    private RecyclerViewAdapter mAdapter;
    /**
     * LayoutManager object for the recycler view
     */
    private RecyclerView.LayoutManager layoutManager;
    /**
     * Text view for the add a new advertisement
     */
    private TextView newTextView;

    /**
     * Firebase Database object
     */
    private FirebaseDatabase database;
    /**
     * Database reference object
     */
    private DatabaseReference myRef;

    /**
     * List of advertisement
     */
    private List<Advertisement> bufeItems = new ArrayList<Advertisement>();

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bufe);

        Intent intent = getIntent();
        type = (String) intent.getExtras().getSerializable("position");
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(type);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);


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

        mAdapter = new RecyclerViewAdapter(BufeActivity.this, bufeItems);
        recyclerView.setAdapter(mAdapter);

        LoadAdvertisement();
    }

    private void LoadAdvertisement() {
        myRef.child("bufe").child(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());
                String id = (String) dataSnapshot.getKey();
                for (DataSnapshot adventisementKey : dataSnapshot.getChildren()) {
                    String title = (String) adventisementKey.child("name").getValue();
                    String detail = (String) adventisementKey.child("price").getValue();

                    String photo = (String) adventisementKey.child("image").getValue();
                    List<String> photos = new ArrayList<String>();
                    photos.add(photo);

                    Advertisement adv = new Advertisement(title, detail, "", "", photos);
                    bufeItems.add(adv);
                }


                mAdapter = new RecyclerViewAdapter(BufeActivity.this, bufeItems);
                mAdapter.setClickListener(BufeActivity.this);
                mAdapter.setLongClickListener(BufeActivity.this);
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
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(View v, int position) {

    }

    @Override
    public void onItemLongClick(View v, int position) {

    }
}
