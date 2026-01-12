package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sharedpref.SharedPrefManager;

public class LoginPage  extends AppCompatActivity
{
    private EditText etID;
    private EditText etPassword;
    private Spinner spRole;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginPage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get references to form elements
        etID = findViewById(R.id.etID);
        etPassword = findViewById(R.id.etPassword);
        spRole = findViewById(R.id.spRole);

    }

    public void loginClicked(View view) {

        // get username/email and password entered by user
        String userid = etID.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        Spinner role = findViewById(R.id.spRole);


        // validate form
        if (validateLogin(userid, password, role)) {
            doLogin(userid, password);
        }
    }

    private void doLogin(String userid, String password) {

        UserService userService = ApiUtils.getUserService();

        // email login
        Call<User> call = userService.login(userid, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User user = response.body();

                    if (user != null && user.getToken() != null) {

                        Toast.makeText(LoginPage.this, "Login successful", Toast.LENGTH_LONG).show();

                        // save session
                        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
                        spm.storeUser(user);

                        //go to user_dashboard
                        //go to admin dashboard
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    } else {
                        Toast.makeText(LoginPage.this, "Login error", Toast.LENGTH_LONG).show();
                    }

                } else {
                    try {
                        String errorResp = response.errorBody().string();
                        FailLogin fail = new Gson().fromJson(errorResp, FailLogin.class);
                        Toast.makeText(LoginPage.this, fail.getError().getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("LoginActivity", e.toString());
                        Toast.makeText(LoginPage.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginPage.this, "Error connecting to server", Toast.LENGTH_LONG).show();
                Log.e("LoginActivity", t.toString());
            }
        });
    }

    private boolean validateLogin(String userid, String password, Spinner role) {
        if (userid == null || userid.isEmpty()) {
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
