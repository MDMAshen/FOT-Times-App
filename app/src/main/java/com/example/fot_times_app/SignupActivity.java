package com.example.fot_times_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput, confirmPasswordInput, emailInput;
    Button signupButton;
    TextView signInLink;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        emailInput = findViewById(R.id.emailInput);
        signupButton = findViewById(R.id.signupButton);
        signInLink = findViewById(R.id.signInLink);

        signupButton.setOnClickListener(v -> registerUser());

        signInLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //  Prevent invalid Firebase key characters in username
        if (username.contains(".") || username.contains("#") || username.contains("$")
                || username.contains("[") || username.contains("]")) {
            Toast.makeText(this, "Username cannot contain '.', '#', '$', '[', or ']'", Toast.LENGTH_LONG).show();
            return;
        }

        //  Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        //  Save to Firebase Realtime Database
        User user = new User(email, password);
        dbRef.child(username).setValue(user).addOnSuccessListener(unused -> {
            Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();

            // Navigate to login after successful registration
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(SignupActivity.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Firebase data model
    public static class User {
        public String email;
        public String password;

        public User() {
        }

        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
