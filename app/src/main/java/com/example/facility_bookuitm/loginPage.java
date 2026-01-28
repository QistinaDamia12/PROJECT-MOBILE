package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.facility_bookuitm.model.FailLogin;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.UserService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginPage extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get references to form elements
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

    }

    /**
     * Login button action handler
     */
    public void loginClicked(View view) {

        // get username/email and password entered by user
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // validate form
        if (validateLogin(username, password)) {
            doLogin(username, password);
        }
    }

    /**
     * Call REST API to login
     *
     * @param username username or email
     * @param password password
     */
    private void doLogin(String username, String password) {
        UserService userService = ApiUtils.getUserService();
        Call<User> call = userService.login(username, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    if (user != null && user.getToken() != null) {
                        Log.d("MyApp", "=== LOGIN SUCCESSFUL ===");
                        Log.d("MyApp", "Token: " + user.getToken());
                        Log.d("MyApp", "User Role: " + user.getRole());
                        Log.d("MyApp", "Username: " + user.getUsername());
                        Log.d("MyApp", "Email: " + user.getEmail());

                        Toast.makeText(loginPage.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Save user session
                        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
                        spm.storeUser(user);

                        // Redirect based on role from database
                        String role = user.getRole();

                        if (role == null || role.isEmpty()) {
                            Toast.makeText(loginPage.this, "User role not found", Toast.LENGTH_SHORT).show();
                            Log.e("MyApp", "Role is null or empty!");
                            return;
                        }

                        // Navigate based on role
                        if (role.equalsIgnoreCase("admin")) {
                            Log.d("MyApp", "Redirecting to Admin Dashboard");
                            startActivity(new Intent(loginPage.this, admin_dashboard.class));
                            finish();
                        } else if (role.equalsIgnoreCase("user")) {
                            Log.d("MyApp", "Redirecting to User Dashboard");
                            startActivity(new Intent(loginPage.this, user_dashboard.class));
                            finish();
                        } else {
                            Toast.makeText(loginPage.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                            Log.e("MyApp", "Unknown role: " + role);
                        }

                    } else {
                        Log.e("MyApp", "User or Token is null!");
                        Toast.makeText(loginPage.this, "Login error: Invalid response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorResp = response.errorBody().string();
                        Log.e("MyApp", "Login error: " + errorResp);
                        FailLogin fail = new Gson().fromJson(errorResp, FailLogin.class);
                        Toast.makeText(loginPage.this, fail.getError().getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("LoginActivity", "Error parsing error response: " + e.toString());
                        Toast.makeText(loginPage.this, "Login failed", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(loginPage.this, "Error connecting to server", Toast.LENGTH_LONG).show();
                Log.e("LoginActivity", "Connection error: " + t.toString());
            }
        });
    }
    /**
     * Validate email/username and password
     */
    private boolean validateLogin(String username, String password) {
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password == null || password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
