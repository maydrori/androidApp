package com.example.may.myapplication.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.may.myapplication.R;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.fragments.UserProfileFragment;
import com.example.may.myapplication.fragments.WorkshopsCalendarFragment;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.viewModels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  {

    private UserViewModel userViewModel;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    TextView navUserName;
    ImageView navUserImage;

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_main_frame);
        navigationView.setCheckedItem(R.id.nav_home);

        // Don't go back if the fragment is the calender one
        // (because we don't want to go back to login screen)
        if (!(f instanceof WorkshopsCalendarFragment)) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpApplicationBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void setUpNavigationView() {

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navUserName = navigationView.getHeaderView(0).findViewById(R.id.text_username);
        navUserImage = navigationView.getHeaderView(0).findViewById(R.id.image_user);
        navUserImage.setImageResource(R.drawable.ic_person);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();

                switchTab(menuItem.getItemId());

                return true;
             }
        });

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init(UserRepository.getCurrentUserId());

        final LifecycleOwner owner = this;
        userViewModel.getUser().observe(owner, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    updateNavigationViewUi(user);
                }
            }
        });
    }

    private void updateNavigationViewUi(User user) {
        navUserName.setText(user.getName());

        Model.instance().getImage(user.getImageUrl(), new Model.GetImageListener() {
            @Override
            public void onDone(Bitmap bitmap) {
                if (bitmap != null) navUserImage.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUpApplicationBar();
        setUpNavigationView();

        // Default tab- home
        switchTab(R.id.nav_home);
    }

    private void switchTab (int menuItemId) {

        // Set item as checked
        navigationView.setCheckedItem(menuItemId);

        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch(menuItemId) {
            default:
            case R.id.nav_home:
                // Show calendar fragment
                fragment = new WorkshopsCalendarFragment();
                f.replace(R.id.content_main_frame, fragment).commit();
                break;
            case R.id.nav_profile:
                // Show user profile fragment
                fragment = UserProfileFragment.instance(FirebaseAuth.getInstance().getCurrentUser().getUid());
                f.replace(R.id.content_main_frame, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_logout:
                // Sign out and go back to login screen
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                break;
        }
    }
}
