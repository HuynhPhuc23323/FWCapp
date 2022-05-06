package com.fwc.cosmetic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateNameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    AppCompatButton btnUpdate;
    AppCompatEditText edtName;
    TextInputLayout tilName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        mAuth = FirebaseAuth.getInstance();

        btnUpdate = findViewById(R.id.btnUpdate);
        tilName = findViewById(R.id.tilName);
        edtName = findViewById(R.id.edtName);

        btnUpdate.setOnClickListener(view -> {
            if (validateInput()) {
                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(edtName.getText().toString()).build();
                mAuth.getCurrentUser().updateProfile(request)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(UpdateNameActivity.this, "The email or password is incorrect",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnUpdate.setEnabled(!edtName.getText().toString().equals(mAuth.getCurrentUser().getDisplayName()));
            }
        });

        edtName.setText(mAuth.getCurrentUser().getDisplayName());
    }


    private Boolean validateInput() {
        if (edtName.getText().length() == 0) {
            tilName.setError("Name cannot be empty");
            return false;
        }
        return true;
    }
}
