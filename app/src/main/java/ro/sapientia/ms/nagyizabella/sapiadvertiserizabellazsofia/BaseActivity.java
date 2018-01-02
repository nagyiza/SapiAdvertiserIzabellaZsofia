package ro.sapientia.ms.nagyizabella.sapiadvertiserizabellazsofia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class BaseActivity extends AppCompatActivity {
    /**
     * Declare progress dialog
     */
    private ProgressDialog mProgressDialog;
    /**
     * The FirebaseAuth and AuthStateListener objects
     */
    public FirebaseAuth.AuthStateListener mAuthListener;
    /**
     *  Ready to start the progress dialog
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }
    /**
     * As soon as the page moves from this to another fragment
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * This method get User Id from FireBase
     * @return
     */
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * This method is used to control the menu
     * This menu is a Navigator Drawer Menu
     * Link to the main page(home),Profile, My Advertiserment, All Advertiserment and Sign Out
     * @param navigationView
     */

    public void menuItemSelected(NavigationView navigationView) {

        disableNavigationViewScrollbars(navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
        /**
        * If you selected the profile
        * Link to the Profile
         */
                if (id == R.id.nav_profile) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(currentUser != null) {
                        Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                    }
                }
                /**
                 * If you selected the Home
                 * Link to the main page(home)
                 */
                else if (id == R.id.nav_home) {
                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                /**
                 * If you selected the  My Advertiserment
                 * Link to the My Advertiserment
                 */
                else if (id == R.id.nav_myadvert) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(currentUser != null) {
                        Intent intent = new Intent(BaseActivity.this, AdvertisementListActivity.class);
                        intent.putExtra("Type", "myAdvertisement");
                        startActivity(intent);
                        finish();
                    }
                    /**
                     * If you are not logged in, Navigate to the Main Page
                     */
                    else{
                        Toast.makeText(BaseActivity.this, "You are not sign in", Toast.LENGTH_LONG).show();
                    }
                }
                /**
                 * If you selected the All Advertiserment
                 * Link to the All Advertiserment
                 */
                else if (id == R.id.nav_alladvert) {
                    Intent intent = new Intent(BaseActivity.this, AdvertisementListActivity.class);
                    intent.putExtra("Type", "allAdvertisement");
                    startActivity(intent);
                    finish();
                }
                /**
                 * If you selected the Sign Out
                 * Link to the Home Page
                 */
                else if(id == R.id.nav_signout){
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
    /**
     * The database reference object
     */
    private DatabaseReference mDatabase;

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }
}
