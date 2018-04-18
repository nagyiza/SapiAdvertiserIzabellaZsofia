package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OpenActivity extends BaseActivity {
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        NavigationView navigationView = (NavigationView)findViewById( R.id.navigation );
        menuItemSelected( navigationView );

        database    = FirebaseDatabase.getInstance();
        mDatabase   = database.getReference("nyitvatartasok" );

        LoadOpeningHours();
    }
    private void LoadOpeningHours()
    {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot OpenKey : dataSnapshot.getChildren())
                {
                    if((OpenKey.getKey().toString()).equals("bufe"))
                    {
                        String bufe         = (String) OpenKey.child("1").getValue();
                        TextView textBufe   = (TextView) findViewById(R.id.textBufe);

                        textBufe.setText( bufe );
                    }

                    if((OpenKey.getKey().toString()).equals("kantin"))
                    {
                        String kantin       = (String) OpenKey.child("1").getValue();
                        TextView textKantin = (TextView) findViewById(R.id.textKantin);

                        textKantin.setText( kantin );
                    }

                    if((OpenKey.getKey().toString()).equals("konyvtar"))
                    {
                        String kolcsonzo        = (String) OpenKey.child("kolcsonzo").getValue();
                        String olvasoterem      = (String) OpenKey.child("olvasoterem").getValue();
                        TextView textKolcsonzo  = (TextView) findViewById(R.id.textKolcsonzo);
                        TextView textOlvaso     = (TextView) findViewById(R.id.textOlvaso);

                        textKolcsonzo.setText( kolcsonzo );
                        textOlvaso.setText( olvasoterem );
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
