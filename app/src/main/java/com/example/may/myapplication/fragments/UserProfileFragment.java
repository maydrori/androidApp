package com.example.may.myapplication.fragments;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.may.myapplication.R;
import com.example.may.myapplication.dal.Model;
import com.example.may.myapplication.models.User;
import com.example.may.myapplication.repositories.UserRepository;
import com.example.may.myapplication.viewModels.UserViewModel;

import java.io.IOException;

/**
 * Created by May on 4/7/2018.
 */

public class UserProfileFragment extends Fragment {

    private UserViewModel userViewModel;
    public static final int PICK_IMAGE = 1;

    String userId;
    boolean readOnly;

    Bitmap userBitmap;
    EditText nameInput;
    EditText phoneInput;
    EditText instegramLinkInput;
    EditText facebookLinkInput;
    ImageView userPhoto;
    ProgressBar progressBarSaveProfile;
    ProgressBar progressBarLoadProfile;
    Button btnEditImage;
    Button btnSaveProfile;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userId = getArguments().getString("userId");

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init(userId);

        final LifecycleOwner owner = this;

        // Observe the user in order to update the ui inputs
        userViewModel.getUser().observe(owner, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                progressBarLoadProfile.setVisibility(View.INVISIBLE);

                if (user != null) {
                    updateUI(user);
                }
            }
        });
    }

    private void updateUI (User user) {
        nameInput.setText(user.getName());
        phoneInput.setText(user.getPhone());
        instegramLinkInput.setText(user.getInstegramLink());
        facebookLinkInput.setText(user.getFacebookLink());

        Model.instance().getImage(user.getImageUrl(), new Model.GetImageListener() {
            @Override
            public void onDone(Bitmap bitmap) {
                if (bitmap != null) userPhoto.setImageBitmap(bitmap);
            }
        });
    }

    public static UserProfileFragment instance(String userId) {
        UserProfileFragment profileFragment = new UserProfileFragment();

        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putBoolean("readOnly", !userId.equals(UserRepository.getCurrentUserId()));
        profileFragment.setArguments(args);

        return profileFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    Uri imageUri = data.getData();
                    userBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    userPhoto.setImageBitmap(userBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_view, container, false);
    }

    private void saveUser(final User user) {

        // Save the user
        Model.instance().saveUser(user);
        progressBarSaveProfile.setVisibility(View.INVISIBLE);

        // Alert success
        String msg =  getContext().getResources().getString(R.string.alert_finishUpdate);
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        // Go back
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        readOnly = getArguments().getBoolean("readOnly");

        // Init input fields
        initInputs();

        // Enable the user to select an image when clicking on the edit image button.
        handleImageClicked();

        // Update the user profile when clicking the save button.
        handleSaveClicked();
    }

    private void initInputs () {
        nameInput = getView().findViewById(R.id.text_name);
        phoneInput = getView().findViewById(R.id.text_phone);
        instegramLinkInput = getView().findViewById(R.id.text_instegram);
        facebookLinkInput = getView().findViewById(R.id.text_facebook);
        userPhoto = getView().findViewById(R.id.image_user);
        userPhoto.setImageResource(R.drawable.ic_person);
        btnEditImage = getView().findViewById(R.id.btn_edit_image);
        btnSaveProfile = getView().findViewById(R.id.btn_save_profile);

        progressBarSaveProfile = getView().findViewById(R.id.progressBarSaveProfile);
        progressBarSaveProfile.setVisibility(View.INVISIBLE);

        progressBarLoadProfile = getView().findViewById(R.id.progressBarLoadProfile);
        progressBarLoadProfile.setVisibility(View.VISIBLE);

        if (readOnly) setViewAsReadOnly(nameInput, phoneInput, instegramLinkInput, facebookLinkInput);
    }

    private void setViewAsReadOnly(EditText... inputs) {

        // Set all input fields as read only-
        // Disable edit
        // Enable select
        for (EditText input : inputs) {
            input.setKeyListener(null);
            input.setTextIsSelectable(true);
        }

        // Hide buttons like save and edit
        btnSaveProfile.setVisibility(View.INVISIBLE);
        btnEditImage.setVisibility(View.INVISIBLE);
    }

    private void handleSaveClicked() {
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            progressBarSaveProfile.setVisibility(View.VISIBLE);

            final User updatedUser = new User(userId,
                    nameInput.getText().toString(),
                    phoneInput.getText().toString(),
                    instegramLinkInput.getText().toString(),
                    facebookLinkInput.getText().toString());

            // If the user has image, save it and save the user with its url,
            // else save the user without url
            if (userBitmap != null) {
                Model.instance().saveImage(userBitmap, new Model.SaveImageListener() {
                    @Override
                    public void complete(String url) {
                        updatedUser.setImageUrl(url);
                        saveUser(updatedUser);
                    }

                    @Override
                    public void fail() {
                        saveUser(updatedUser);
                    }
                });
            }
            else saveUser(updatedUser);
            }
        });
    }

    private void handleImageClicked() {
        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }
}
