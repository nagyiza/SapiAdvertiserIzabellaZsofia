package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignInButton = findViewById(R.id.signin);
        // Click listeners
        mSignInButton.setOnClickListener(this);


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

    private  void signIn(){
        Intent intent = new Intent(MainActivity.this, SignIn.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        switch (i) {
            case R.id.signin:
                signIn();
                break;
        }
    }
}