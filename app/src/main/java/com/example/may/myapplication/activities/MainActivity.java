package com.example.may.myapplication.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.may.myapplication.R;
import com.example.may.myapplication.fragments.LoginFragment;
import com.example.may.myapplication.fragments.UserProfileFragment;
import com.example.may.myapplication.fragments.WorkshopsCalendarFragment;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.utils.ImageHelper;
import com.example.may.myapplication.viewModels.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  {

    private UserViewModel userViewModel;
    DrawerLayout mDrawerLayout;

    private void initViews() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        setUpNavigationView();
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

    private void setUpNavigationView() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        final TextView navUserName = navigationView.getHeaderView(0).findViewById(R.id.text_username);
        final ImageView navUserImage = navigationView.getHeaderView(0).findViewById(R.id.image_user);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
            // set item as selected to persist highlight
            menuItem.setChecked(true);
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
                navUserName.setText(user.getName());

                userViewModel.getImage(user.getImageUrl(), user.getId()).observe(owner, new Observer<Bitmap>() {
                    @Override
                    public void onChanged(@Nullable Bitmap bitmap) {
                        navUserImage.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        switchTab(R.id.nav_home);
    }

    private void switchTab (int menuItemId) {
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(menuItemId) {
            default:
            case R.id.nav_home:
                fragment = new WorkshopsCalendarFragment();
                break;
            case R.id.nav_profile:
                fragment = UserProfileFragment.instance(FirebaseAuth.getInstance().getCurrentUser().getUid());
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                fragment = new LoginFragment();
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}