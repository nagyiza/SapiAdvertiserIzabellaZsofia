package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KantinActivity extends BaseActivity {

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kantin);

        /* NavigationView navigationView = (NavigationView)findViewById( R.id.navigation );
        menuItemSelected( navigationView );*/

        database    = FirebaseDatabase.getInstance();
        mDatabase   = database.getReference("kantin" );

        LoadDailyMenu();
    }
    private void LoadDailyMenu()
    {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dailyMenuKey : dataSnapshot.getChildren())
                {
                    if((dailyMenuKey.getKey().toString()).equals("desszert"))
                    {
                        String dessert1         = (String) dailyMenuKey.child("1").getValue();
                        String dessert2         = (String) dailyMenuKey.child("2").getValue();
                        TextView textDessert    = (TextView) findViewById(R.id.textDessert);
                        TextView textDessert2    = (TextView) findViewById(R.id.textDessert2);

                        textDessert.setText( dessert1);
                        textDessert2.setText(dessert2);
                    }

                    if((dailyMenuKey.getKey().toString()).equals("leves"))
                    {
                        String soup1        = (String) dailyMenuKey.child("1").getValue();
                        String soup2        = (String) dailyMenuKey.child("2").getValue();
                        TextView textSoup   = (TextView) findViewById(R.id.textSoup);
                        TextView textSoup2   = (TextView) findViewById(R.id.textSoup2);

                        textSoup.setText( soup1  );
                        textSoup2.setText( soup2 );
                    }

                    if((dailyMenuKey.getKey().toString()).equals("masodik"))
                    {
                        String main1        = (String) dailyMenuKey.child("1").getValue();
                        String main2        = (String) dailyMenuKey.child("2").getValue();
                        TextView textMain   = (TextView) findViewById(R.id.textMain);
                        TextView textMain2   = (TextView) findViewById(R.id.textMain2);

                        textMain.setText( main1 );
                        textMain2.setText( main2 );
                    }

                    if((dailyMenuKey.getKey().toString()).equals("savanyusag"))
                    {
                        String salate1      = (String) dailyMenuKey.child("1").getValue();
                        String salate2      = (String) dailyMenuKey.child("2").getValue();
                        TextView textSalate = (TextView) findViewById(R.id.textSalate);
                        TextView textSalate2 = (TextView) findViewById(R.id.textSalate2);

                        textSalate.setText( salate1 );
                        textSalate2.setText( salate2 );
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
