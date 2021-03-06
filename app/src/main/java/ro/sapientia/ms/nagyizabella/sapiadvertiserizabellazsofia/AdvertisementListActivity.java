package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.adapters.RecyclerViewAdapter;
import ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia.models.Advertisement;

public class AdvertisementListActivity extends BaseActivity implements View.OnClickListener, RecyclerViewAdapter.ItemClickListener, RecyclerViewAdapter.ItemLongClickListener{

    /**
     * Type of advertisement (all or my advertisements)
     */
    private String type;
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
    private List<Advertisement> advertisements = new ArrayList<Advertisement>();

    /**
     * In the method find view elements by id and add listener on the buttons
     * Set database reference and firebase database
     * Set recycler view adapter for display advertisements
     * Display menu
     * @param savedInstanceState
     */
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

        mAdapter = new RecyclerViewAdapter(AdvertisementListActivity.this, advertisements);
        recyclerView.setAdapter(mAdapter);

        LoadAdvertisement();
    }

    /**
     * Create search menu item in action bar
     * Add listener in the search menu item, and if change the search field, call the getFilter method
     * The getFilter method get the search result
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);

                return false;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //mAdapter.updateData(advertisements);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Display the list of advertisement
     * Get the advertisements from database
     * Set recycler view adapter
     */
    private void LoadAdvertisement() {
        myRef.child("advertisements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());
                String id = (String) dataSnapshot.getKey();
                for (DataSnapshot adventisementKey : dataSnapshot.getChildren()) {
                    String title = (String) adventisementKey.child("title").getValue();
                    String detail = (String) adventisementKey.child("detail").getValue();
                    String user = (String) adventisementKey.child("userId").getValue();
                    String advId = (String) adventisementKey.child("id").getValue();
                    ArrayList<String> photos = (ArrayList<String>) adventisementKey.child("photo").getValue();
                    //for (DataSnapshot ds : adventisementKey.child("photos").getChildren()) {
                    //    photos.add((String) ds.getValue());
                    //}
                    String locationPos = (String) adventisementKey.child("location").getValue();

                    String hide = (String) adventisementKey.child("hide").getValue();
                    if(hide.equals("false")) { //if the advertisement not is hide
                        if (type.equals("myAdvertisement")) {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser == null) {
                                Toast.makeText(AdvertisementListActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AdvertisementListActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                if (currentUser.getUid().toString().equals(user)) {
                                    Advertisement adv = new Advertisement(title, detail, locationPos, user, photos);
                                    adv.setId(advId);
                                    advertisements.add(adv);
                                }
                            }
                        } else { // all advertisement
                            Advertisement adv = new Advertisement(title, detail, locationPos, user, photos);
                            adv.setId(advId);
                            advertisements.add(adv);
                        }
                    }

                }


                mAdapter = new RecyclerViewAdapter(AdvertisementListActivity.this, advertisements);
                mAdapter.setClickListener(AdvertisementListActivity.this);
                mAdapter.setLongClickListener(AdvertisementListActivity.this);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(AdvertisementListActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                String td = "";
            }
        });


    }

    /**
     * This is a listener for the text
     * @param v this is view element, which clicked
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.tv_new:
                newAdvertisement();
                break;
        }
    }

    /**
     * This method open the AddAdvertisementActivity, where need to add a new advertisement
     */
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

    /**
     * Implement the item click listener
     * Open the AdvertisementData activity, where can to see detail of advertisement
     * @param v This is view element, which clicked
     * @param position This is advertisement item's position, which clicked
     */
    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(AdvertisementListActivity.this, AdvertisementData.class);
        intent.putExtra("Advertisement", advertisements.get(position));
        startActivity(intent);
    }

    //remove (hide)

    /**
     * Implement the item long click listener
     * Get the user from the database and if the advertisement is by the current user, delete(hide) the advertisement in the database
     * @param v
     * @param position
     */
    @Override
    public void onItemLongClick(View v, final int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getUid().equals(mAdapter.getUserId(position))){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdvertisementListActivity.this);
            // set title
            alertDialogBuilder.setTitle("Delete the contact");
            // set dialog message
            alertDialogBuilder
                    .setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //implement yes
                            database.getReference("advertisements").child(mAdapter.getItem(position)).child("hide").setValue("true");

                            advertisements.remove(position);
                            mAdapter.updateData(advertisements);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // implement no
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

        }
    }
}
