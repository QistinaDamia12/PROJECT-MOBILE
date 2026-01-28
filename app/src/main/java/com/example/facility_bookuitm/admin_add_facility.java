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

import java.text.SimpleDateFormat;
import java.util.Locale;
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
                    selectedImageName = "f1.jpg";
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
        String name = txtName.getText().toString().trim();
        String location = txtLoca.getText().toString().trim();
        String status = txtStatus.getText().toString().trim();
        String type = txtType.getText().toString().trim();
        String capacityStr = txtCapacity.getText().toString().trim();

        if (name.isEmpty() || location.isEmpty() || status.isEmpty() || type.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int capacityInt = 0;
        try {
            capacityInt = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number for capacity", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // send request to add new book to the REST API
        FacilityService facilityService = ApiUtils.getFacilityService();
        Call<Facility> call = facilityService.addFacility(user.getToken(), name, location, "default.jpg", status, type, capacityInt);

        // execute
        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {

                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 201) {
                    // book added successfully
                    Facility addedFacility = response.body();
                    // display message
                    Toast.makeText(getApplicationContext(),
                            addedFacility.getFacilityType() + " added successfully.",
                            Toast.LENGTH_LONG).show();

                    // end this activity and go back to previous activity, BookListActivity
                    finish();
                }
                else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error [" + t.getMessage() + "]",
                        Toast.LENGTH_LONG).show();
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
            }
        });
    }


    public void clearSessionAndRedirect() {
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