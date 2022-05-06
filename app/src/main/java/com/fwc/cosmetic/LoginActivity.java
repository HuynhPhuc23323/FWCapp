package com.fwc.cosmetic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    AppCompatButton btnLogin;
    AppCompatTextView tvSignUp;
    AppCompatEditText edtEmail, edtPassword;
    TextInputLayout tilEmail, tilPassword;
    ContentLoadingProgressBar loadingProgressBar;

    int SIGNUP_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // khai bao view
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignup);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        tvSignUp.setOnClickListener(view -> {
            // chay activity Signup khi muon nhan lai du lieu tu activity signup o ham onActivityResult()
            startActivityForResult(new Intent(this, SignupActivity.class), SIGNUP_ACTIVITY_REQUEST_CODE);
        });

        btnLogin.setOnClickListener(view -> {
            if (validateInput()){
                loadingProgressBar.show();
                // goi ham dang nhap cua firebase
                mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnCompleteListener(this, task -> {
                            loadingProgressBar.hide();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "The email or password is incorrect",
                                        Toast.LENGTH_SHORT).show();
                                edtPassword.setText("");
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // lay data khi activity signup tra ve RESULT_OK
        if (requestCode == SIGNUP_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String email = data.getStringExtra("email");
                edtEmail.setText(email);
            }
        }
    }

    private Boolean validateInput() {
        if (edtEmail.getText().toString().length() == 0) {
            tilEmail.setError("Email cannot be empty");
            return false;
        }
        if (edtPassword.getText().toString().length() == 0) {
            tilPassword.setError("Password cannot be empty");
            return false;
        }
        // validate email giong form email
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(edtEmail.getText());
        if (!matcher.find()){
            tilEmail.setError("Invalid email address");
            return false;
        }
        return true;
    }
}
