package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    //MENU
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        FirebaseUser currentUser;
        switch (item.toString()){
            case "Home":
                intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case "Profile":
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null) {
                    intent = new Intent(BaseActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                }
                break;
            case "Advertisement":
                intent = new Intent(BaseActivity.this, AdvertisementListActivity.class);
                intent.putExtra("Type", "allAdvertisement");
                startActivity(intent);
                finish();
                break;
            case "My Advertisement":
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null) {
                    intent = new Intent(BaseActivity.this, AdvertisementListActivity.class);
                    intent.putExtra("Type", "myAdvertisement");
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                }
                break;
            //default:break;
        }
        Log.d("MENU", item.toString());
        return true;
    }

}
