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

import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

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
                    break;
                case 1:
                    imagePreview.setImageResource(R.drawable.f2);
                    break;
                case 2:
                    imagePreview.setImageResource(R.drawable.f3);
                    break;
                case 3:
                    imagePreview.setImageResource(R.drawable.f4);
                    break;
                case 4:
                    imagePreview.setImageResource(R.drawable.f5);
                    break;
                case 5:
                    imagePreview.setImageResource(R.drawable.f6);
                    break;
                case 6:
                    imagePreview.setImageResource(R.drawable.f7);
                    break;
            }
        });
        builder.show();
    }


    public void addFacility(View v) {
        // get values in form
        String name = txtName.getText().toString();
        String location = txtLoca.getText().toString();
        String status = txtStatus.getText().toString();
        String type = txtType.getText().toString();

        //convert text to int
        int capacity = 0;
        try {
            capacity = Integer.parseInt(txtCapacity.getText().toString().trim());
        } catch (NumberFormatException e) {
            txtCapacity.setError("Enter a valid number");
            return; // stop further processing
        }

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // send request to add new book to the REST API
        FacilityService facilityService = ApiUtils.getFacilityService();
        Call<Facility> call = facilityService.addFacility(name, location, "default.jpg", status, type, capacity);

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
                            addedFacility.getName() + " added successfully.",
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
            public void onFailure(Call<Facility> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Error [" + t.getMessage() + "]",
                        Toast.LENGTH_LONG).show();
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());

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
}
