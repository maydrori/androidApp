package com.example.may.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.TextView;

import com.example.may.myapplication.fragments.UserProfileFragment;
import com.example.may.myapplication.fragments.WorkshopsCalendarFragment;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.repositories.UserRepository;
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
        final TextView navUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_username);
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

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                navUsername.setText(user.getName());
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
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}
