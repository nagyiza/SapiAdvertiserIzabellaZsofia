package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //wait(1000);
        //this.setOnTouchListener(new () {
        //    @Override
        //   public void onSwipingLeft(final MotionEvent event) {
        Intent intent = new Intent(MainActivity.this, SignIn.class);
        startActivity(intent);
        //   }
        //});


        // Write a message to the database
        //try {
        //    database = FirebaseDatabase.getInstance();
        //    myRef = database.getReference();

        //    myRef.child("izabella");
        //    myRef.setValue("valami").addOnFailureListener(new OnFailureListener() {
        //       @Override
        //        public void onFailure(@NonNull Exception e) {
        //           Log.d(LOG_TAG, e.getLocalizedMessage());
        //       }
        //   });
        //   Toast.makeText(this,"success", Toast.LENGTH_LONG).show();

        //}catch(Exception e){
        //    Toast.makeText(this,"failed", Toast.LENGTH_LONG).show();

        //}

    }
}