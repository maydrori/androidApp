package com.example.may.myapplication.fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.may.myapplication.R;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.models.Workshop;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.utils.DateFormatter;
import com.example.may.myapplication.viewModels.WorkshopViewModel;

/**
 * Created by May on 4/22/2018.
 */

public class ViewWorkshopFragment extends Fragment {

    WorkshopViewModel workshopViewModel;

    Workshop workshop;
    String workshopId;

    boolean isMyWorkshop;
    ImageView teacherPhoto;
    TextView teacherName;
    TextView genre;
    TextView place;
    TextView level;
    TextView date;
    TextView time;

    ProgressBar progressBarFinishLoading;

    public static ViewWorkshopFragment instance(String workshopId) {
        ViewWorkshopFragment fragment = new ViewWorkshopFragment();

        Bundle args = new Bundle();
        args.putString("workshopId", workshopId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_my_workshop, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:

                AddWorkshopFragment addWorkshopFragment = AddWorkshopFragment.instance(workshop);
                addWorkshopFragment.show(getActivity().getSupportFragmentManager(), "Add workshop Fragment");

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_workshop, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workshopId = getArguments().getString("workshopId");

        initViewFields();
        initViewModel(workshopId);
        registerEvents();
    }

    private void initViewModel(String workshopId) {
        workshopViewModel = ViewModelProviders.of(this).get(WorkshopViewModel.class);
        workshopViewModel.init(workshopId);

        final LifecycleOwner owner = this;
        workshopViewModel.getWorkshop().observe(owner, new Observer<Workshop>() {
            @Override
            public void onChanged(@Nullable Workshop w) {
                if (w != null) {
                    workshop = w;
                    isMyWorkshop = workshop.getTeacherId().equals(UserRepository.getCurrentUserId());
                    setHasOptionsMenu(isMyWorkshop);
                    updateUI();
                }
            }
        });
    }

    private void updateUI() {

        updateWorkshopInfo();
        updateRelevantActions();
    }

    private void initViewFields() {
        progressBarFinishLoading = getView().findViewById(R.id.progressBarFinishLoading);
        progressBarFinishLoading.setVisibility(View.VISIBLE);

        teacherPhoto = getView().findViewById(R.id.image_teacher);
        teacherPhoto.setImageResource(R.drawable.ic_person);

        teacherName = getView().findViewById(R.id.text_teacher);
        genre = getView().findViewById(R.id.text_genre);
        place = getView().findViewById(R.id.text_place);
        level = getView().findViewById(R.id.text_level);
        date = getView().findViewById(R.id.text_date);
        time = getView().findViewById(R.id.text_time);
    }

    private void updateRelevantActions() {
        String membersOverview;
        int btnActionToShow;
        boolean toShowSoldOutButton = false;

        // Workshop is full
        if (workshop.getRegisteredMembers().size() == workshop.getMaxParticipants()) {
            membersOverview = workshop.getWaitingListMembers().size() + " ממתינים ";

            // User is in waiting list
            if (workshop.getWaitingListMembers().contains(UserRepository.getCurrentUserId())) btnActionToShow = R.id.btn_leave_waitinglist;
                // User registered to workshop
            else if (workshop.getRegisteredMembers().contains(UserRepository.getCurrentUserId())) btnActionToShow = R.id.btn_unregister;
                // User not registered to workshop and not in the waiting list
            else btnActionToShow = R.id.btn_waitinglist;

            toShowSoldOutButton = true;
        }
        // Workshop still got place
        else {
            membersOverview = workshop.getRegisteredMembers().size() + "/" + workshop.getMaxParticipants();

            // Check if user already registered to the workshop
            btnActionToShow = (workshop.getRegisteredMembers().contains(UserRepository.getCurrentUserId())) ? R.id.btn_unregister : R.id.btn_register;
        }

        // If the current workshop is mine, show the number of registered members and the delete button
        if (isMyWorkshop) {
            ((TextView)getView().findViewById(R.id.text_members)).setText(membersOverview);
            btnActionToShow = R.id.btn_delete;
        }

        showRelevantActionButton(btnActionToShow);
        ((TextView)getView().findViewById(R.id.text_sold_out)).setVisibility((toShowSoldOutButton) ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateWorkshopInfo() {

        genre.setText(workshop.getGenre());
        place.setText(workshop.getPlace());
        level.setText(workshop.getLevel());
        date.setText(DateFormatter.toDateFormat(workshop.getStartTime()));
        time.setText(DateFormatter.toTimeFormat(workshop.getStartTime()) + " - " + DateFormatter.toTimeFormat(workshop.getEndTime()));
        teacherName.setText(workshop.getTeacherName());

        Model.instance().getImage(workshop.getTeacherImageUrl(), new Model.GetImageListener() {
            @Override
            public void onDone(Bitmap bitmap) {
                if (progressBarFinishLoading.getVisibility() == View.VISIBLE)
                    progressBarFinishLoading.setVisibility(View.INVISIBLE);

                if (bitmap != null) teacherPhoto.setImageBitmap(bitmap);
            }
        });
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
            ((Button)getView().findViewById(currId)).setVisibility(visibility);
        }
    }

    private void registerEvents() {

        final String userId = UserRepository.getCurrentUserId();

        getView().findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance().registerMemberToWorkshop(workshopId, userId);
            }
        });

        getView().findViewById(R.id.btn_unregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance().unregisterMemberFromWorkshop(workshopId, userId);
            }
        });

        getView().findViewById(R.id.btn_waitinglist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance().enterWaitingList(workshopId, userId);
            }
        });

        getView().findViewById(R.id.btn_leave_waitinglist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance().leaveWaitingList(workshopId, userId);
            }
        });

        getView().findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance().deleteWorkshop(workshopId);
                getActivity().finish();
            }
        });

        // Open the teacher profile fragment when user click on the teacher's image
        teacherPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (workshop != null) {
                    Fragment fragment = UserProfileFragment.instance(workshop.getTeacherId());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }
}
