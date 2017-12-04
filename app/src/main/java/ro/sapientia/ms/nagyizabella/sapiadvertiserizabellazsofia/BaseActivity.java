package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    public FirebaseAuth.AuthStateListener mAuthListener;

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



    /*
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
            case "Sign out":
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null){
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(BaseActivity.this, "Sign out", Toast.LENGTH_LONG).show();
                    intent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                }
            //default:break;
        }
        Log.d("MENU", item.toString());
        return true;
    }
*/
    public void isUserCurrentlyLogin(final Context context){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(null != user){
                    Intent profileIntent = new Intent(context, MenuActivity.class);
                    context.startActivity(profileIntent);
                }else{
                    Intent loginIntent = new Intent(context, SignInActivity.class);
                    context.startActivity(loginIntent);
                }
            }
        };
    }
    public FirebaseAuth firebaseAuth;
    public void checkUserLogin(final Context context){
        if(firebaseAuth.getCurrentUser() != null){
            Intent profileIntent = new Intent(context, MenuActivity.class);
            context.startActivity(profileIntent);
        }
    }

}
