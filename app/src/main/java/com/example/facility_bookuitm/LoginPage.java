package com.example.facility_bookuitm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private ProgressDialog progressDialog;

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

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        // If user is already logged in, redirect based on role
        SharedPrefManager spm = new SharedPrefManager(this);
        if (spm.isLoggedIn()) {
            User user = spm.getUser();
            if (user.getToken() != null && !user.getToken().isEmpty()) {
                redirectUser(user); // redirect automatically
            } else {
                spm.logout(); // invalid token, clear session
            }
        }
    }

    /**
     * Login button action handler
     */
    public void loginClicked(View view) {

        // display loading progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // get username and password entered by user
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // validate form
        if (validateLogin(username, password)) {
            doLogin(username, password);
        } else {
            progressDialog.dismiss();
        }
    }

    /**
     * Call REST API to login
     */
    private void doLogin(String username, String password) {
        UserService userService = ApiUtils.getUserService();
        Call<User> call = userService.login(username, password);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                // end progress dialog
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();

                    if (user.getToken() == null || user.getToken().isEmpty()) {
                        displayToast("Token missing. Cannot login.");
                        Log.e("LoginPage", "Token missing from response!");
                        return;
                    }

                    // Save user session
                    SharedPrefManager spm = new SharedPrefManager(LoginPage.this);
                    spm.storeUser(user);

                    displayToast("Login successful");

                    // Redirect based on role
                    redirectUser(user);

                } else {
                    try {
                        String errorResp = response.errorBody().string();
                        FailLogin fail = new Gson().fromJson(errorResp, FailLogin.class);
                        Snackbar.make(findViewById(R.id.loginPage), fail.getError().getMessage(), Snackbar.LENGTH_LONG).show();
                        Log.e("LoginPage", "Login failed: " + fail.getError().getMessage());
                    } catch (Exception e) {
                        displayToast("Login failed");
                        Log.e("LoginPage", "Error parsing login response: " + e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                displayToast("Error connecting to server.");
                Log.e("LoginPage", t.toString());
            }
        });
    }

    /**
     * Validate username and password
     */
    private boolean validateLogin(String username, String password) {
        if (username == null || username.isEmpty()) {
            displayToast("Username is required");
            return false;
        }
        if (password == null || password.isEmpty()) {
            displayToast("Password is required");
            return false;
        }
        return true;
    }

    /**
     * Redirect user based on role in database
     */
    private void redirectUser(User user) {
        if ("admin".equalsIgnoreCase(user.getRole())) {
            startActivity(new Intent(LoginPage.this, admin_dashboard.class));
            finish();
        } else if ("user".equalsIgnoreCase(user.getRole())) {
            startActivity(new Intent(LoginPage.this, user_dashboard.class));
            finish();
        } else {
            displayToast("Unknown role: " + user.getRole());
            Log.e("LoginPage", "Unknown role: " + user.getRole());
        }
    }

    /**
     * Display Toast message
     */
    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
