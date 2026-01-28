package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class admin_add_facility extends AppCompatActivity {

    private static ImageView imagePreview;
    private EditText txtName;
    private EditText txtLoca;
    private EditText txtStatus;
    private EditText txtType;
    private EditText txtCapacity;
    private String selectedImageName = "default.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_facility);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        imagePreview = findViewById(R.id.imagePreview);
        // get view objects references
        txtName = findViewById(R.id.txtName);
        txtLoca = findViewById(R.id.txtLoca);
        txtStatus = findViewById(R.id.txtStatus);
        txtType = findViewById(R.id.txtType);
        txtCapacity = findViewById(R.id.txtCapacity);

        //add image to display
        btnUploadImage.setOnClickListener(v -> showImagePicker());

        // Go back to previous page
        ImageView btnBack2 = findViewById(R.id.btnBack);
        btnBack2.setOnClickListener(v -> {
            finish(); // closes current activity and returns to previous
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

    }

    private void showImagePicker() {
        String[] images = {"Auditorium", "Meeting Room", "Surau", "Court", "Gym", "Lab", "Lecture Room"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Facility Image");
        builder.setItems(images, (dialog, which) -> {
            switch (which) {
                case 0:
                    imagePreview.setImageResource(R.drawable.f1);
                    selectedImageName = "f1.jpg";  // Remove AtomicReference, use class variable directly
                    break;
                case 1:
                    imagePreview.setImageResource(R.drawable.f2);
                    selectedImageName = "f2.jpg";
                    break;
                case 2:
                    imagePreview.setImageResource(R.drawable.f3);
                    selectedImageName = "f3.jpg";
                    break;
                case 3:
                    imagePreview.setImageResource(R.drawable.f4);
                    selectedImageName = "f4.jpg";
                    break;
                case 4:
                    imagePreview.setImageResource(R.drawable.f5);
                    selectedImageName = "f5.jpg";
                    break;
                case 5:
                    imagePreview.setImageResource(R.drawable.f6);
                    selectedImageName = "f6.jpg";
                    break;
                case 6:
                    imagePreview.setImageResource(R.drawable.f7);
                    selectedImageName = "f7.jpg";
                    break;
            }
        });
        builder.show();
    }


    public void addNewFacility(View v) {
        Log.d("MyApp", "=== ADD FACILITY BUTTON CLICKED ===");

        // get values in form
        String facilityName = txtName.getText().toString().trim();
        String facilityLocation = txtLoca.getText().toString().trim();
        String facilityStatus = txtStatus.getText().toString().trim();
        String facilityType = txtType.getText().toString().trim();
        String capacityStr = txtCapacity.getText().toString().trim();

        // Log all input values
        Log.d("MyApp", "Facility Name: '" + facilityName + "'");
        Log.d("MyApp", "Location: '" + facilityLocation + "'");
        Log.d("MyApp", "Status: '" + facilityStatus + "'");
        Log.d("MyApp", "Type: '" + facilityType + "'");
        Log.d("MyApp", "Capacity String: '" + capacityStr + "'");
        Log.d("MyApp", "Selected Image: '" + selectedImageName + "'");

        // Validate inputs
        if (facilityName.isEmpty()) {
            Log.e("MyApp", "ERROR: Name is empty!");
            txtName.setError("Name is required");
            txtName.requestFocus();
            return;
        }

        if (facilityLocation.isEmpty()) {
            Log.e("MyApp", "ERROR: Location is empty!");
            txtLoca.setError("Location is required");
            txtLoca.requestFocus();
            return;
        }

        if (facilityStatus.isEmpty()) {
            Log.e("MyApp", "ERROR: Status is empty!");
            txtStatus.setError("Status is required");
            txtStatus.requestFocus();
            return;
        }

        if (facilityType.isEmpty()) {
            Log.e("MyApp", "ERROR: Type is empty!");
            txtType.setError("Type is required");
            txtType.requestFocus();
            return;
        }

        //convert text to int
        int facilityCapacity = 0;
        try {
            facilityCapacity = Integer.parseInt(capacityStr);
            Log.d("MyApp", "Capacity parsed successfully: " + facilityCapacity);

            if (facilityCapacity <= 0) {
                Log.e("MyApp", "ERROR: Capacity must be greater than 0");
                txtCapacity.setError("Capacity must be greater than 0");
                txtCapacity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Log.e("MyApp", "ERROR: Invalid capacity number - " + e.getMessage());
            txtCapacity.setError("Enter a valid number");
            txtCapacity.requestFocus();
            return;
        }

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        if (user == null) {
            Log.e("MyApp", "ERROR: User is null!");
            Toast.makeText(getApplicationContext(), "Session expired. Please login again", Toast.LENGTH_LONG).show();
            clearSessionAndRedirect();
            return;
        }

        String token = user.getToken();
        Log.d("MyApp", "User Token: " + (token != null ? token.substring(0, Math.min(10, token.length())) + "..." : "NULL"));
        Log.d("MyApp", "Base URL: " + ApiUtils.BASE_URL);
        Log.d("MyApp", "Full API URL: " + ApiUtils.BASE_URL + "facilities");

        // send request to add new facility to the REST API
        FacilityService facilityService = ApiUtils.getFacilityService();

        Call<Facility> call = facilityService.addFacility(
                token,
                facilityName,
                facilityLocation,
                selectedImageName,
                facilityStatus,
                facilityType,
                facilityCapacity
        );

        Log.d("MyApp", "Sending API request...");

        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                Log.d("MyApp", "=== API RESPONSE RECEIVED ===");
                Log.d("MyApp", "Response Code: " + response.code());
                Log.d("MyApp", "Response Message: " + response.message());
                Log.d("MyApp", "Request URL: " + call.request().url());
                Log.d("MyApp", "Response: " + response.raw().toString());

                // Try to get error body
                String errorBody = "";
                try {
                    if (response.errorBody() != null) {
                        errorBody = response.errorBody().string();
                        Log.e("MyApp", "Error Body: " + errorBody);
                    }
                } catch (Exception e) {
                    Log.e("MyApp", "Error reading error body: " + e.getMessage());
                }

                if (response.code() == 201) {
                    // facility added successfully
                    Facility addedFacility = response.body();
                    if (addedFacility != null) {
                        Log.d("MyApp", "SUCCESS! Facility added: " + addedFacility.toString());
                        Toast.makeText(getApplicationContext(),
                                addedFacility.getFacilityName() + " added successfully.",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Log.e("MyApp", "ERROR: Response body is null!");
                        Toast.makeText(getApplicationContext(), "Error: Empty response", Toast.LENGTH_LONG).show();
                    }
                }
                else if (response.code() == 401) {
                    Log.e("MyApp", "ERROR: Unauthorized (401)");
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else if (response.code() == 404) {
                    Log.e("MyApp", "ERROR: Endpoint not found (404)");
                    Toast.makeText(getApplicationContext(), "Error: API endpoint not found. Error: " + errorBody, Toast.LENGTH_LONG).show();
                }
                else if (response.code() == 500) {
                    Log.e("MyApp", "ERROR: Server error (500)");
                    Toast.makeText(getApplicationContext(), "Server error: " + errorBody, Toast.LENGTH_LONG).show();
                }
                else {
                    Log.e("MyApp", "ERROR: Unexpected response code: " + response.code());
                    Toast.makeText(getApplicationContext(), "Error " + response.code() + ": " +
                            (errorBody.isEmpty() ? response.message() : errorBody), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                Log.e("MyApp", "=== API CALL FAILED ===");
                Log.e("MyApp", "Error Type: " + t.getClass().getName());
                Log.e("MyApp", "Error Message: " + t.getMessage());
                Log.e("MyApp", "Stack trace:");
                t.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "Connection failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearSessionAndRedirect()
    {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
}
