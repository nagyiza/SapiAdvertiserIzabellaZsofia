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

    }
}
