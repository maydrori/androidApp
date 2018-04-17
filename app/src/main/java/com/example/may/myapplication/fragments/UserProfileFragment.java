package com.example.may.myapplication.fragments;

import android.app.Activity;
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
import com.example.may.myapplication.viewModels.UserViewModel;

import java.io.IOException;

/**
 * Created by May on 4/7/2018.
 */

public class UserProfileFragment extends Fragment {

    private UserViewModel userViewModel;
    public static final int PICK_IMAGE = 1;

    String userId;

    Bitmap userBitmap;
    EditText nameInput;
    EditText phoneInput;
    EditText instegramLinkInput;
    EditText facebookLinkInput;
    ImageView userPhoto;
    ProgressBar progressBarSaveProfile;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userId = getArguments().getString("userId");
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init(userId);

        userViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                updateUI(user);
            }
        });
    }

    private void updateUI (User user) {
        nameInput.setText(user.getName());
        phoneInput.setText(user.getPhone());
        instegramLinkInput.setText(user.getInstegramLink());
        facebookLinkInput.setText(user.getFacebookLink());
    }

    public static UserProfileFragment instance(String userId) {
        UserProfileFragment profileFragment = new UserProfileFragment();

        Bundle args = new Bundle();
        args.putString("userId", userId);
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

    private void saveUser(User user) {

        // Save the user
        Model.instance().saveUser(user);

        // Alert success
        Toast.makeText(getActivity().getApplicationContext(), "העדכון בוצע בהצלחה", Toast.LENGTH_SHORT).show();
        progressBarSaveProfile.setVisibility(View.INVISIBLE);

        // Go back
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        progressBarSaveProfile = getView().findViewById(R.id.progressBarSaveProfile);
        progressBarSaveProfile.setVisibility(View.INVISIBLE);
    }

    private void handleSaveClicked() {

        Button btnSaveProfile = getView().findViewById(R.id.btn_save_profile);
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            progressBarSaveProfile.setVisibility(View.VISIBLE);

            final User updatedUser = new User(userId,
                    nameInput.getText().toString(),
                    phoneInput.getText().toString(),
                    instegramLinkInput.getText().toString(),
                    facebookLinkInput.getText().toString());

            if (userBitmap != null) {
                Model.instance().saveImage(userBitmap, "image-" + userId, new Model.SaveImageListener() {
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
        Button btnEditImage = getView().findViewById(R.id.btn_edit_image);
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
