package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AddAdvertisementActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "AddAdvertisementActivity";

    private DatabaseReference mDatabase;
    //the FirebaseAuth and AuthStateListener objects.
    private FirebaseAuth mAuth;

    //Declare the fields
    private EditText EditTitle;
    private EditText EditDetail;
    private ListView ImageList;

    private Button ButtonLocation;
    private Button ButtonAddImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);

        // Views-> Unsigned Id-s
        EditTitle = (EditText)findViewById(R.id.adver_title);
        EditDetail = (EditText)findViewById(R.id.adver_text);
        ImageList =(ListView)findViewById(R.id.list_image);
        ButtonLocation = (Button)findViewById(R.id.adver_location);
        ButtonAddImage = (Button)findViewById(R.id.adver_image);

        ButtonLocation.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.adver_location:
                AddLocation();
                break;
            case R.id.adver_image:
                AddImage();
                break;
        }
    }

    private void AddImage() {
        
    }

    private void AddLocation() {

    }
}
