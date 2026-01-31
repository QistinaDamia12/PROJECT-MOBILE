package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.UserService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class admin_dashboard  extends AppCompatActivity {

    private TextView tvUsername;
    private UserService userService;
    private SharedPrefManager spm;
    private User currentUser;
    private String token;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_dashboard);

        tvUsername = findViewById(R.id.tvUsername);

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

        // Optional: refresh info from server
        loadUserProfile();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void loadUserProfile() {
        userService.getLoggedInUser(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    tvUsername.setText(currentUser.getUsername());

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

    // Put this inside admin_dashboard.java
    public void facilityList(View view) {
        Intent intent = new Intent(this, admin_list_facility.class);
        startActivity(intent);
    }

    public void requestList(View view) {
        Intent intent = new Intent(this, AdminApproveList.class);
        startActivity(intent);

    }
    // In admin_dashboard.java
    public void logout(View view) {
        // Clear session
        com.example.facility_bookuitm.sharedpref.SharedPrefManager spm =
                new com.example.facility_bookuitm.sharedpref.SharedPrefManager(getApplicationContext());
        spm.logout();

        // Close app and go to login
        finish();
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}