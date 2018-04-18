package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class BufeListActivity extends BaseActivity{
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

    private List<String> bufeList = new ArrayList<>();

    private ArrayAdapter<String> mHistory;

    /**
     * List of advertisement
     */
    private List<Advertisement> bufeItems = new ArrayList<Advertisement>();

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
        setContentView(R.layout.activity_bufe_list);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);


        //newTextView = (TextView) findViewById(R.id.tv_new);
        //newTextView.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();



        ListView listView=(ListView)findViewById(R.id.lv_bufe);
        mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bufeList);
        listView.setAdapter(mHistory);

        LoadBufeItems();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                String value = (String)adapter.getItemAtPosition(position);
//                Toast.makeText(getBaseContext(), "You clicked " + (position+1) , Toast.LENGTH_SHORT).show();

                Intent teacherIntent = new Intent(BufeListActivity.this, BufeActivity.class);
                teacherIntent.putExtra("position", value);
                startActivity(teacherIntent);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
    }


    /**
     * Display the list of advertisement
     * Get the advertisements from database
     * Set recycler view adapter
     */
    private void LoadBufeItems() {
        myRef.child("bufe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());
                //String id = (String) dataSnapshot.getKey();
                for (DataSnapshot bufeKey : dataSnapshot.getChildren()) {
                    String name = (String) bufeKey.getKey();

                    bufeList.add(name);
                    mHistory.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(AdvertisementListActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                String td = "";
            }
        });


    }
}

