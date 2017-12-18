package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

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

    public void menuItemSelected(NavigationView navigationView) {

        disableNavigationViewScrollbars(navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(currentUser != null) {
                        Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                    }
                } else if (id == R.id.nav_home) {
                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_myadvert) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(currentUser != null) {
                        Intent intent = new Intent(BaseActivity.this, AdvertisementListActivity.class);
                        intent.putExtra("Type", "myAdvertisement");
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                    }
                } else if (id == R.id.nav_alladvert) {
                    Intent intent = new Intent(BaseActivity.this, AdvertisementListActivity.class);
                    intent.putExtra("Type", "allAdvertisement");
                    startActivity(intent);
                    finish();
                }else if(id == R.id.nav_signout){
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(currentUser != null){
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(BaseActivity.this, "Sign out", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                    }
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

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
