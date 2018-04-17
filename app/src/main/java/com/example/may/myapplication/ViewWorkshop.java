package com.example.may.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.dal.firebase.ModelFirebase;
import com.example.may.myapplication.fragments.AddWorkshopFragment;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.models.WorkshopMembers;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.utils.DateFormatter;
import com.example.may.myapplication.viewModels.UserViewModel;
import com.example.may.myapplication.viewModels.WorkshopMembersViewModel;
import com.example.may.myapplication.viewModels.WorkshopViewModel;

/**
 * Created by May on 4/3/2018.
 */

public class ViewWorkshop extends AppCompatActivity {

    WorkshopViewModel workshopViewModel;
    WorkshopMembersViewModel workshopMembersViewModel;

    String workshopId;
    Workshop workshop = null;
    WorkshopMembers workshopMembers = null;

    boolean isMyWorkshop;
    ImageView teacherPhoto;
    TextView genre;
    TextView place;
    TextView level;
    TextView date;
    TextView time;

    ProgressBar progressBarFinishLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_workshop);

        workshopId = getIntent().getExtras().getString("workshopId");

        initViewFields();
        initViewModels(workshopId);
        registerEvents();
    }

    private void initViewModels(String workshopId) {
        workshopViewModel = ViewModelProviders.of(this).get(WorkshopViewModel.class);
        workshopViewModel.init(workshopId);

        workshopMembersViewModel = ViewModelProviders.of(this).get(WorkshopMembersViewModel.class);
        workshopMembersViewModel.init(workshopId);

        workshopViewModel.getWorkshop().observe(this, new Observer<Workshop>() {
            @Override
            public void onChanged(@Nullable Workshop w) {
                workshop = w;
                if (workshopMembers != null) updateUI();
            }
        });

        workshopMembersViewModel.getWorkshopMembers().observe(this, new Observer<WorkshopMembers>() {
            @Override
            public void onChanged(@Nullable WorkshopMembers wm) {
                workshopMembers = wm;
                if (workshop != null) updateUI();
            }
        });
    }

    private void updateUI() {

        setFieldsByWorkshop();

        // Check if the workshop is of the current user
        isMyWorkshop = (workshop.getTeacherId().equals(UserRepository.getCurrentUserId()));

        String membersOverview;
        int btnActionToShow;
        boolean toShowSoldOutButton = false;

        // Workshop is full
        if (workshopMembers.getRegistered().size() == workshop.getMaxParticipants()) {
            membersOverview = workshopMembers.getWaitingList().size() + " ממתינים ";
            btnActionToShow = (workshopMembers.getWaitingList().contains(UserRepository.getCurrentUserId())) ? R.id.btn_leave_waitinglist : R.id.btn_waitinglist;

            toShowSoldOutButton = true;
        }
        // Workshop still got place
        else {
            membersOverview = workshopMembers.getRegistered().size() + "/" + workshop.getMaxParticipants();
            btnActionToShow = (workshopMembers.getRegistered().contains(UserRepository.getCurrentUserId())) ? R.id.btn_unregister : R.id.btn_register;
        }

        if (isMyWorkshop) {
            ((TextView)findViewById(R.id.text_members)).setText(membersOverview);
            btnActionToShow = R.id.btn_delete;
        }

        showRelevantActionButton(btnActionToShow);
        ((TextView)findViewById(R.id.text_sold_out)).setVisibility((toShowSoldOutButton) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_my_workshop, menu);
        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:

                FragmentManager fm = getSupportFragmentManager();
                AddWorkshopFragment addWorkshopFragment = new AddWorkshopFragment();

                Bundle args = new Bundle();
                // TODO
//                args.putSerializable("workshop", workshop);
                addWorkshopFragment.setArguments(args);
                addWorkshopFragment.show(fm, "Add workshop Fragment");

                return true;

            case R.id.action_view_participants:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void initViewFields() {
        progressBarFinishLoading = findViewById(R.id.progressBarFinishLoading);
        progressBarFinishLoading.setVisibility(View.VISIBLE);

        teacherPhoto = findViewById(R.id.image_teacher);
        genre = findViewById(R.id.text_genre);
        place = findViewById(R.id.text_place);
        level = findViewById(R.id.text_level);
        date = findViewById(R.id.text_date);
        time = findViewById(R.id.text_time);
    }

    private void setFieldsByWorkshop() {

        genre.setText(workshop.getGenre());
        place.setText(workshop.getPlace());
        level.setText(workshop.getLevel());
        date.setText(DateFormatter.toDateFormat(workshop.getDate()));
        time.setText(DateFormatter.toTimeFormat(workshop.getDate()));

        setTeacherInfo();
    }

    private void setTeacherInfo() {
        Model.instance().getUserById(workshop.getTeacherId(), new ModelFirebase.GetDataListener<User>() {
            @Override
            public void onComplete(final User user) {

                // Set teacher name
                TextView teacherName = findViewById(R.id.text_teacher);
                teacherName.setText(user.getName());

                // Set teacher image
                setTeacherPhoto(user);
            }
        });
    }

    private void setTeacherPhoto (final User teacher) {
        teacherPhoto.setTag(teacher.getImageUrl());

        if (teacher.getImageUrl() != null) {
            Model.instance().getImage("image-" + teacher.getId(), new Model.GetImageListener() {
                @Override
                public void onSuccess(Bitmap bitmap) {

                    if (progressBarFinishLoading.getVisibility() == View.VISIBLE) progressBarFinishLoading.setVisibility(View.INVISIBLE);

                    if (teacherPhoto.getTag().equals(teacher.getImageUrl())) {
                        teacherPhoto.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFail() {

                }
            });
        }
    }

    private void showRelevantActionButton(int btnId) {
        int[] buttons = new int[]{R.id.btn_register,
                R.id.btn_unregister,
                R.id.btn_waitinglist,
                R.id.btn_leave_waitinglist,
                R.id.btn_delete};

        // Go through the buttons- hide all of them except the one we want to show
        for (int currId : buttons) {
            int visibility = (currId == btnId) ? View.VISIBLE : View.INVISIBLE;
            ((Button)findViewById(currId)).setVisibility(visibility);
        }
    }

    private void registerEvents() {

        final String userId = UserRepository.getCurrentUserId();

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().registerMemberToWorkshop(workshopId, userId);
            }
        });

        findViewById(R.id.btn_unregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().unregisterMemberFromWorkshop(workshopId, userId);
            }
        });

        findViewById(R.id.btn_waitinglist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().enterWaitingList(workshopId, userId);
            }
        });

        findViewById(R.id.btn_leave_waitinglist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().leaveWaitingList(workshopId, userId);
            }
        });

        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Model.instance().deleteWorkshop(workshopId);
            finish();
            }
        });
    }
}
