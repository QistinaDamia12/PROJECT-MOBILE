package com.example.facility_bookuitm;

import android.content.DialogInterface;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class admin_add_facility extends AppCompatActivity {

    private ImageView imagePreview;
    private EditText txtName, txtLoca, txtStatus, txtType, txtCapacity;
    private String selectedImageName = "default.jpg"; // default image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_facility);

        // Bind views
        imagePreview = findViewById(R.id.imagePreview);
        txtName = findViewById(R.id.txtName);
        txtLoca = findViewById(R.id.txtLoca);
        txtStatus = findViewById(R.id.txtStatus);
        txtType = findViewById(R.id.txtType);
        txtCapacity = findViewById(R.id.txtCapacity);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(v -> showImagePicker());

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    // ===== IMAGE PICKER =====
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

    // ===== ADD NEW FACILITY =====
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

        if (user == null || user.getToken() == null || user.getToken().isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            clearSessionAndRedirect();
            return;
        }

        // ✅ SEND THE SELECTED IMAGE
        FacilityService facilityService = ApiUtils.getFacilityService();
        Call<Facility> call = facilityService.addFacility(
                user.getToken(),
                name,
                location,
                selectedImageName, // <---- send selected image here
                status,
                type,
                capacityInt
        );

        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 201) {
                    Facility addedFacility = response.body();
                    Toast.makeText(getApplicationContext(),
                            addedFacility.getFacilityType() + " added successfully.",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error [" + t.getMessage() + "]", Toast.LENGTH_LONG).show();
                Log.d("MyApp:", "Error: " + (t.getCause() != null ? t.getCause().getMessage() : t.getMessage()));
            }
        });
    }

    // ===== LOGOUT AND REDIRECT =====
    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finish();
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
}

