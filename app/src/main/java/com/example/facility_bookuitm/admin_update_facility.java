package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class admin_update_facility extends AppCompatActivity {

    private TextInputEditText txtName, txtLoca, txtStatus, txtType, txtCapacity;
    private ImageView imagePreview;
    private Button btnUploadImage, btnSubmit;

    private int facilityID;
    private String selectedImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_update_facility);

        // Bind views
        txtName = findViewById(R.id.txtName);
        txtLoca = findViewById(R.id.txtLoca);
        txtStatus = findViewById(R.id.txtStatus);
        txtType = findViewById(R.id.txtType);
        txtCapacity = findViewById(R.id.txtCapacity);
        imagePreview = findViewById(R.id.imagePreview);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Get intent data
        facilityID = getIntent().getIntExtra("facilityID", 0);
        txtName.setText(getIntent().getStringExtra("facilityName"));
        txtLoca.setText(getIntent().getStringExtra("facilityLocation"));
        txtStatus.setText(getIntent().getStringExtra("facilityStatus"));
        txtType.setText(getIntent().getStringExtra("facilityType"));
        txtCapacity.setText(String.valueOf(getIntent().getIntExtra("facilityCapacity", 0)));
        selectedImageName = getIntent().getStringExtra("facilityPicture");

        // Load existing image
        if (selectedImageName != null) {
            String img = selectedImageName.replace(".jpg", "");
            int resID = getResources().getIdentifier(img, "drawable", getPackageName());
            if (resID != 0) imagePreview.setImageResource(resID);
        }

        // Click to open image picker dialog
        imagePreview.setOnClickListener(v -> showImagePicker());
        btnUploadImage.setOnClickListener(v -> showImagePicker());

        // Submit button
        btnSubmit.setOnClickListener(v -> updateFacility());
    }

    // ===== Predefined Image Picker =====
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

    // ===== Update Facility =====
    private void updateFacility() {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();

        if (user == null || user.getToken() == null || user.getToken().isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        FacilityService service = ApiUtils.getFacilityService();

        Facility updatedFacility = new Facility();
        updatedFacility.setFacilityID(facilityID);
        updatedFacility.setFacilityName(txtName.getText().toString().trim());
        updatedFacility.setFacilityLocation(txtLoca.getText().toString().trim());
        updatedFacility.setFacilityPicture(selectedImageName); // updated image
        updatedFacility.setFacilityStatus(txtStatus.getText().toString().trim());
        updatedFacility.setFacilityType(txtType.getText().toString().trim());
        updatedFacility.setFacilityCapacity(Integer.parseInt(txtCapacity.getText().toString().trim()));

        service.updateFacility(user.getToken(), facilityID, updatedFacility)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(admin_update_facility.this, "Facility updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (response.code() == 401) {
                            Toast.makeText(admin_update_facility.this, "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                            spm.logout();
                            finish();
                        } else {
                            Toast.makeText(admin_update_facility.this, "Update failed: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(admin_update_facility.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}


