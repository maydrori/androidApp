package com.example.may.myapplication.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.may.myapplication.R;
import com.example.may.myapplication.model.Model;
import com.example.may.myapplication.model.User;
import com.example.may.myapplication.utils.UsersManager;

/**
 * Created by May on 4/7/2018.
 */

public class ProfileFragment extends Fragment {

    public static ProfileFragment instance(User user) {
        ProfileFragment profileFragment = new ProfileFragment();

        Bundle args = new Bundle();
        args.putSerializable("user", user);
        profileFragment.setArguments(args);

        return profileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final User user = (User)getArguments().getSerializable("user");

        final EditText nameInput = view.findViewById(R.id.text_name);
        final EditText phoneInput = view.findViewById(R.id.text_phone);
        final EditText instegramLinkInput = view.findViewById(R.id.text_instegram);
        final EditText facebookLinkInput = view.findViewById(R.id.text_facebook);

        nameInput.setText(user.getName());
        phoneInput.setText(user.getPhone());
        instegramLinkInput.setText(user.getInstegramLink());
        facebookLinkInput.setText(user.getFacebookLink());

        Button btnSaveProfile = (Button) view.findViewById(R.id.btn_save_profile);
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(nameInput.getText().toString())
                    .setPhone(phoneInput.getText().toString())
                    .setInstegramLink(instegramLinkInput.getText().toString())
                    .setFacebookLink(facebookLinkInput.getText().toString());

                Model.instance().updateUser(user);
                UsersManager.instance().setCurrentUser(user);
                Toast.makeText(getActivity().getApplicationContext(), "העדכון בוצע בהצלחה", Toast.LENGTH_SHORT).show();
                getActivity().getFragmentManager().popBackStack();
            }
        });
    }
}
