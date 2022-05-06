package com.fwc.cosmetic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    AppCompatTextView tvLogin;
    AppCompatButton btnSignUp;
    AppCompatEditText edtName, edtEmail, edtPassword, edtConfirmPass;
    TextInputLayout tilName, tilEmail, tilPassword, tilConfirmPass;
    ContentLoadingProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPass = findViewById(R.id.edtConfirmPassword);
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPass = findViewById(R.id.tilConfirmPassword);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        btnSignUp.setOnClickListener(view -> {
            if (validateInput()){
                loadingProgressBar.show();
                // tao tai khoan bang email + pass
                mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            loadingProgressBar.hide();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();

                                // sau khi tao thanh cong, update display name
                                UserProfileChangeRequest profileUpdates =
                                        new UserProfileChangeRequest.Builder().setDisplayName(edtName.getText().toString()).build();
                                user.updateProfile(profileUpdates);
                                Toast.makeText(SignupActivity.this, "Sign up success",
                                        Toast.LENGTH_SHORT).show();

                                // sau khi dang ky thanh cong, goi RESULT_OK de ben Login co the nhan duoc data: user.getEmail()
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("email",user.getEmail());
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        tvLogin.setOnClickListener(view -> {
            finish();
        });

    }

    private Boolean validateInput() {
        if (edtName.getText().toString().length() == 0) {
            tilName.setError("Name cannot be empty");
            return false;
        }
        if (edtEmail.getText().toString().length() == 0) {
            tilEmail.setError("Email cannot be empty");
            return false;
        }
        if (edtPassword.getText().toString().length() == 0) {
            tilPassword.setError("Password cannot be empty");
            return false;
        }
        if (edtConfirmPass.getText().toString().length() == 0) {
            tilConfirmPass.setError("Confirm password cannot be empty");
            return false;
        }
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(edtEmail.getText().toString());
        if (!matcher.find()){
            tilEmail.setError("Invalid email address");
            return false;
        }
        if (!edtConfirmPass.getText().toString().equals(edtPassword.getText().toString())) {
            tilConfirmPass.setError("Confirm password does not match");
            return false;
        }
        return true;
    }
}