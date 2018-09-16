package com.sample.carpool.carpool.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.sample.carpool.carpool.Utils.SessionManager;
import com.sample.carpool.carpool.fragments.AboutFragment;
import com.sample.carpool.carpool.fragments.NotificationFragment;
import com.sample.carpool.carpool.fragments.ProfileFragment;
import com.sample.carpool.carpool.fragments.SettingsFragment;
import com.sample.carpool.carpool.fragments.MyRidesFragment;
import com.sample.carpool.carpool.R;
import com.sample.carpool.carpool.fragments.HomeFragment;

public class SidebarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GoogleMap mMap;
    private static final int REQUEST_LOCATION = 177 ;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    SimpleDraweeView ImageV;
    TextView nameTV,emailTV;
    Fragment f;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth auth;
    SessionManager sessionManager;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);
        auth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);
        ViewGroup mNavigationView;
        mNavigationView = findViewById(R.id.nav_view);
        LayoutInflater.from(getApplicationContext()).inflate(R.layout.nav_header, mNavigationView);
        ImageV =findViewById(R.id.dp_image);
        nameTV =findViewById(R.id.dp_name);
        emailTV = findViewById(R.id.dp_email);

       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }
            return;
        }else{
            mMap.setMyLocationEnabled(true);
        }
*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

       /* nameTV.setText(sessionManager.fname()+""+sessionManager.lname());
        emailTV.setText(sessionManager.mail());
        ImageV.setImageURI(sessionManager.image());*/


        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedFragment(R.id.home);

        nameTV.setText(sessionManager.fname()+""+sessionManager.lname());
        emailTV.setText(sessionManager.mail());
        ImageV.setImageURI(sessionManager.image());


    }

    @Override
    public void onBackPressed() {

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {

            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        displaySelectedFragment(id);
        return true;
    }

    private void displaySelectedFragment(int id) {
        Fragment fragment = null;

        switch (id) {
            case R.id.home:
                fragment = new HomeFragment();
                setTitle("Home");
                break;
            case R.id.profile:
                fragment = new ProfileFragment();
                setTitle("Profile");
                break;
            case R.id.my_rides:
                fragment = new MyRidesFragment();
                setTitle("My Ride");
                break;
            case R.id.notifications:
                fragment = new NotificationFragment();
                setTitle("Notifications");
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                setTitle("Settings");
                break;
            case R.id.about:
                fragment = new AboutFragment();
                setTitle("About");
                break;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

        }

        if(fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.commit();
            }

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 177:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                    if (fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).setMap();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }

        // other 'case' lines to check for other
        // permissions this app might request.
    }
}

