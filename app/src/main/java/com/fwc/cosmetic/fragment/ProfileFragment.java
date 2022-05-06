package com.fwc.cosmetic.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fwc.cosmetic.LoginActivity;
import com.fwc.cosmetic.R;
import com.fwc.cosmetic.UpdateNameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private AppCompatTextView tvName, tvEmail;
    private ConstraintLayout clUpdateName, clChangeAvatar, clLogout;
    private CircleImageView ivAvatar;
    private FirebaseAuth mAuth;
    private FirebaseUser userInfo;
    int PICK_IMAGE_REQUEST_CODE = 2;
    int UPDATE_NAME_REQUEST_CODE = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        clUpdateName = view.findViewById(R.id.clUpdateName);
        clChangeAvatar = view.findViewById(R.id.clChangeAvatar);
        clLogout = view.findViewById(R.id.clLogout);
        ivAvatar = view.findViewById(R.id.ivAvatar);

        userInfo = mAuth.getCurrentUser();

        setData();

        clLogout.setOnClickListener(view1 -> {
            mAuth.signOut();
            startActivity(new Intent(getContext(), LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            getActivity().finish();
        });

        clUpdateName.setOnClickListener(view1 -> {
            startActivityForResult(new Intent(getContext(), UpdateNameActivity.class), UPDATE_NAME_REQUEST_CODE);
        });

        clChangeAvatar.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_CODE);
        });
        return view;
    }

    private void setData() {
        tvName.setText(userInfo.getDisplayName());
        tvEmail.setText(userInfo.getEmail());
        Glide.with(getContext())
                .load(userInfo.getPhotoUrl())
                .placeholder(R.drawable.ic_profile)
                .into(ivAvatar);
    }

    private void updateUserProfilePicture(final Uri uri) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        userInfo.updateProfile(profileChangeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setData();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE) {
            final Uri imageUri = data.getData();
            updateUserProfilePicture(imageUri);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == UPDATE_NAME_REQUEST_CODE) {
            setData();
        }
    }
}
