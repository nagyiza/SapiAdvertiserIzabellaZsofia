package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
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

public class TeacherListActivity extends BaseActivity {

    /**
     * Firebase Database object
     */
    private FirebaseDatabase database;
    /**
     * Database reference object
     */
    private DatabaseReference myRef;

    private List<String> tanarok = new ArrayList<>();

    private ArrayAdapter<String> mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        //menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        menuItemSelected(navigationView);

//        String position = savedInstanceState.getString("position");
        Bundle b = getIntent().getExtras();
        final int position = b.getInt("position");
        Log.d("TeacherListActivity", "pos: " + position);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tanszekek");

        tanarok.add("lajos");

        ListView listView=(ListView)findViewById(R.id.lv_teachers);
        mHistory = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tanarok);
        listView.setAdapter(mHistory);

        LoadTanarok(position);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long arg3) {
                //String value = (String)adapter.getItemAtPosition(position-1);
//                Toast.makeText(getBaseContext(), "You clicked " + (pos+1) , Toast.LENGTH_SHORT).show();

                Intent teacherIntent = new Intent(TeacherListActivity.this, TeacherActivity.class);
                teacherIntent.putExtra("position", position);
                teacherIntent.putExtra("position2", pos+1);
                startActivity(teacherIntent);
            }
        });
    }

    private void LoadTanarok(final int position) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());
                //Toast.makeText(getBaseContext(), "pos: " + position, Toast.LENGTH_SHORT).show();
//                Log.d("TeacherListActivity", "pos: " + position);
                tanarok.clear();
                int i = 0;
                for(DataSnapshot tanszekKey : dataSnapshot.getChildren()) {
                    ++i;
                    if(i == position) {
                        for (DataSnapshot teacherKey : tanszekKey.child("teachers").getChildren()) {
                            String name = (String) teacherKey.child("name").getValue();
                            tanarok.add(name);
                            mHistory.notifyDataSetChanged();
                        }
                        break;
                    }
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
