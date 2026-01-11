package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facility_bookuitm.model.FailLogin;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.UserService;
import sharedpref.sharedPrefManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
    public class LoginActivity extends AppCompatActivity {

        private EditText edtEmail;
        private EditText edtPassword;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            edtEmail = findViewById(R.id.etID);
            edtPassword = findViewById(R.id.etPassword);
        }

        public void loginClicked(View view) {

            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (validateLogin(email, password)) {
                doLogin(email, password);
            }
        }

        private void doLogin(String email, String password) {

            UserService userService = ApiUtils.getUserService();

            Call<User> call = userService.login(email, password);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        User user = response.body();

                        if (user.getToken() != null) {

                            sharedPrefManager spm =
                                    new sharedPrefManager(getApplicationContext());
                            spm.storeUser(user);

                            Toast.makeText(LoginActivity.this,
                                    "Login successful",
                                    Toast.LENGTH_LONG).show();

                            finish();
                            startActivity(new Intent(LoginActivity.this,
                                    MainActivity.class));
                        } else {
                            displayToast("Login failed");
                        }

                    } else {
                        try {
                            String errorResp = response.errorBody().string();
                            FailLogin e =
                                    new Gson().fromJson(errorResp, FailLogin.class);
                            displayToast(e.getError().getMessage());
                        } catch (Exception e) {
                            displayToast("Login error");
                            Log.e("Login", e.toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    displayToast("Cannot connect to server");
                    Log.e("Login", t.toString());
                }
            });
        }

        private boolean validateLogin(String email, String password) {
            if (email == null || email.trim().isEmpty()) {
                displayToast("Email is required");
                return false;
            }
            if (password == null || password.trim().isEmpty()) {
                displayToast("Password is required");
                return false;
            }
            return true;
        }

        public void displayToast(String message) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }


