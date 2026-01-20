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

    RadioGroup rgRole;
    RadioButton rbUser, rbAdmin;

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

        //radiobtn
        rgRole = findViewById(R.id.rgRole);
        rbUser = findViewById(R.id.rbUser);
        rbAdmin = findViewById(R.id.rbAdmin);
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

        // email login
        Call<User> call = userService.login(username, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User user = response.body();

                    if (user != null && user.getToken() != null) {

                        Toast.makeText(loginPage.this, "Login successful", Toast.LENGTH_LONG).show();

                        // save session
                        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
                        spm.storeUser(user);

                        //radio button page
                        int selectedId = rgRole.getCheckedRadioButtonId();

                        if (selectedId == -1) {
                            Toast.makeText(loginPage.this, "Please select a role", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        RadioButton selectedRadioButton = findViewById(selectedId);
                        String role = selectedRadioButton.getText().toString().trim();

                        if (role.equalsIgnoreCase("Admin")) {
                            startActivity(new Intent(loginPage.this, admin_dashboard.class));
                            finish();
                        } else {
                            // User = Student/Lecturer
                            startActivity(new Intent(loginPage.this, user_dashboard.class));
                            finish();
                        }

                    } else {
                        Toast.makeText(loginPage.this, "Login error", Toast.LENGTH_LONG).show();
                    }

                } else {
                    try {
                        String errorResp = response.errorBody().string();
                        FailLogin fail = new Gson().fromJson(errorResp, FailLogin.class);
                        Toast.makeText(loginPage.this, fail.getError().getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("LoginActivity", e.toString());
                        Toast.makeText(loginPage.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(loginPage.this, "Error connecting to server", Toast.LENGTH_LONG).show();
                Log.e("LoginActivity", t.toString());
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
