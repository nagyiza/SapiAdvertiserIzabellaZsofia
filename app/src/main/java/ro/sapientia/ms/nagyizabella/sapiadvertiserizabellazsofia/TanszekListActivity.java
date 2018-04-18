package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TanszekListActivity extends BaseActivity {

    /**
     * Firebase Database object
     */
    private FirebaseDatabase database;
    /**
     * Database reference object
     */
    private DatabaseReference myRef;

    private List<String> tanszekek = new ArrayList<>();

    private ArrayAdapter<String> mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanszek_list);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tanszekek");

//        tanszekek = new ArrayList<>();
//        tanszekek.add("Kerteszmernoki");
//        tanszekek.add(myRef.child("tanszekek").child("1").child("name").getValue());

        ListView listView=(ListView)findViewById(R.id.lv_tanszekek);
        mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tanszekek);
        listView.setAdapter(mHistory);

        LoadTanszekek();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                String value = (String)adapter.getItemAtPosition(position);
//                Toast.makeText(getBaseContext(), "You clicked " + (position+1) , Toast.LENGTH_SHORT).show();

                Intent teacherIntent = new Intent(TanszekListActivity.this, TeacherListActivity.class);
                teacherIntent.putExtra("position", position+1);
                startActivity(teacherIntent);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
    }

    private void LoadTanszekek() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());
                tanszekek.clear();
                for (DataSnapshot tanszekKey : dataSnapshot.getChildren()) {
                    String name = (String) tanszekKey.child("name").getValue();
                    tanszekek.add(name + " tanszék");
                    mHistory.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                //String td = "";
            }
        });


    }
}
