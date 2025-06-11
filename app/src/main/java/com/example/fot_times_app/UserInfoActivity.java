package com.example.fot_times_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class UserInfoActivity extends AppCompatActivity {

    TextView usernameValue, emailValue;
    Button signOutButton, editButton;
    DatabaseReference userRef;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        usernameValue = findViewById(R.id.usernameValue);
        emailValue = findViewById(R.id.emailValue);
        signOutButton = findViewById(R.id.signOutButton);
        editButton = findViewById(R.id.editButton);
        ImageView backIcon = findViewById(R.id.backIcon);

        currentUsername = getIntent().getStringExtra("username");
        if (currentUsername == null) {
            Toast.makeText(this, "No username passed!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = snapshot.child("email").getValue(String.class);
                usernameValue.setText(currentUsername);
                emailValue.setText(email != null ? email : "No email found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserInfoActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
            }
        });

        backIcon.setOnClickListener(v -> finish());

        signOutButton.setOnClickListener(v -> {
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_sign_out, null);
            Button okButton = dialogView.findViewById(R.id.okButton);
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);

            AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this)
                    .setView(dialogView)
                    .setCancelable(false)
                    .create();

            okButton.setOnClickListener(ok -> {
                dialog.dismiss();
                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                finish();
            });

            cancelButton.setOnClickListener(cancel -> dialog.dismiss());
            dialog.show();
        });

        editButton.setOnClickListener(v -> {
            View editDialogView = getLayoutInflater().inflate(R.layout.dialog_edit_info, null);

            EditText usernameInput = editDialogView.findViewById(R.id.usernameInput);
            EditText emailInput = editDialogView.findViewById(R.id.emailInput);
            Button okEditBtn = editDialogView.findViewById(R.id.okEditButton);
            Button cancelEditBtn = editDialogView.findViewById(R.id.cancelEditButton);

            usernameInput.setText(currentUsername);
            emailInput.setText(emailValue.getText().toString());

            AlertDialog editDialog = new AlertDialog.Builder(UserInfoActivity.this)
                    .setView(editDialogView)
                    .setCancelable(false)
                    .create();

            okEditBtn.setOnClickListener(ok -> {
                String newUsername = usernameInput.getText().toString().trim();
                String newEmail = emailInput.getText().toString().trim();

                if (newUsername.isEmpty() || newEmail.isEmpty()) {
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newUsername.equals(currentUsername)) {
                    // Check if the new username already exists
                    userRef.child(newUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(UserInfoActivity.this, "Username already exists. Choose another.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Proceed with update
                                userRef.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            userRef.child(newUsername).setValue(snapshot.getValue()).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    userRef.child(currentUsername).removeValue();
                                                    userRef.child(newUsername).child("email").setValue(newEmail);
                                                    currentUsername = newUsername;
                                                    usernameValue.setText(currentUsername);
                                                    emailValue.setText(newEmail);
                                                    Toast.makeText(UserInfoActivity.this, "Info updated!", Toast.LENGTH_SHORT).show();
                                                    editDialog.dismiss();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(UserInfoActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(UserInfoActivity.this, "Error checking username", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // Only email update
                    userRef.child(currentUsername).child("email").setValue(newEmail)
                            .addOnSuccessListener(unused -> {
                                emailValue.setText(newEmail);
                                Toast.makeText(this, "Info updated!", Toast.LENGTH_SHORT).show();
                                editDialog.dismiss();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
                }
            });

            cancelEditBtn.setOnClickListener(cancel -> editDialog.dismiss());
            editDialog.show();
        });
    }
}