package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherActivity extends BaseActivity {

    /**
     * Firebase Database object
     */
    private FirebaseDatabase database;
    /**
     * Database reference object
     */
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tanszekek");

        Bundle b = getIntent().getExtras();
        int position = b.getInt("position");
        int position2 = b.getInt("position2");
        Log.d("TeacherActivity", "pos1: " + position + " pos2: " + position2);

        LoadTanar(position, position2);
    }

    private void LoadTanar(final int position, final int position2) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //firebaseElements.add((ArrayList<String>) dataSnapshot.getValue());
                //Toast.makeText(getBaseContext(), "pos: " + position, Toast.LENGTH_SHORT).show();
//                Log.d("TeacherListActivity", "pos: " + position);
                int i = 0;
                int j = 0;
                for(DataSnapshot tanszekKey : dataSnapshot.getChildren()) {
                    ++i;
                    if(i == position) {
                        for (DataSnapshot teacherKey : tanszekKey.child("teachers").getChildren()) {
                            ++j;
                            if(j == position2){
                                String name = (String) teacherKey.child("name").getValue();
                                String email = (String) teacherKey.child("email").getValue();
                                String consultinghours = (String) teacherKey.child("consultinghours").getValue();
                                String room = teacherKey.child("room").getValue().toString();
                                Log.d("TeacherActivity", "name: " + name + ", consultinghours: " + consultinghours + ", room: " + room);

                                TextView tv_email = findViewById(R.id.tv_email);
                                TextView tv_title = findViewById(R.id.tv_title);
                                TextView tv_consultinghour = findViewById(R.id.tv_consultinghour);
                                TextView tv_room = findViewById(R.id.tv_room);
                                ImageView iv_photo = findViewById(R.id.iv_images);

                                Glide.with(TeacherActivity.this)
                                        .load(teacherKey.child("image").getValue())
                                        .error(R.drawable.profile_base)
                                        .fitCenter()
                                        .into(iv_photo);

                                tv_title.setText(name);
                                tv_email.setText("Email: " + email);
                                tv_consultinghour.setText("Fogadó óra: " + consultinghours);
                                tv_room.setText("Terem: " + room);
                                break;
                            }
//                            String name = (String) teacherKey.child("name").getValue();
//                            tanarok.add(name);
//                            mHistory.notifyDataSetChanged();
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
