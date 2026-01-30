package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.UserService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_profile extends AppCompatActivity {

    private TextView tvUsername;
    private TextView tvEmail;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private Button btnChangePassword;
    private Button btnLogout;

    private UserService userService;
    private SharedPrefManager spm;
    private User currentUser;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);

        userService = ApiUtils.getUserService();
        spm = new SharedPrefManager(getApplicationContext());

        // Load user from SharedPreferences
        currentUser = spm.getUser();
        token = currentUser.getToken();

        if (token == null) {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display username and email immediately
        tvUsername.setText("Welcome, " + currentUser.getUsername() + "!");
        tvEmail.setText(currentUser.getEmail());

        // Optional: refresh info from server
        loadUserProfile();

        btnChangePassword.setOnClickListener(v -> changePassword());
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserProfile() {
        userService.getLoggedInUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    tvUsername.setText(currentUser.getUsername());
                    tvEmail.setText(currentUser.getEmail());

                    // Update saved user info
                    spm.storeUser(currentUser);
                } else {
                    Log.e("USER_PROFILE", "Failed to load user: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("USER_PROFILE", "Error loading user: " + t.getMessage());
            }
        });
    }

    public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void changePassword() {
        String newPasswordInput = etNewPassword.getText().toString().trim();

        // Update password
        currentUser.setPassword(md5(newPasswordInput));

        // 🔥 FORCE user to stay active
        currentUser.setIs_active(1);

        userService.updateUser(token, currentUser.getId(), currentUser)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(user_profile.this,
                                    "Password updated successfully",
                                    Toast.LENGTH_SHORT).show();

                            spm.storeUser(currentUser);
                        } else {
                            Toast.makeText(user_profile.this,
                                    "Failed to update password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(user_profile.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void logoutUser() {
        spm.logout();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(user_profile.this, LoginPage.class));
        finish();
    }
}




