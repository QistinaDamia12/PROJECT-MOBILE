package com.example.facility_bookuitm;

import android.os.Bundle;
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
public class admin_update_facility extends AppCompatActivity {

    private EditText txtName, txtLoca, txtStatus, txtType, txtCapacity;
    private ImageView imagePreview;
    private Button btnSubmit;

    private int facilityID;
    private String selectedImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_update_facility);

        // bind views
        imagePreview = findViewById(R.id.imagePreview);
        txtName = findViewById(R.id.txtName);
        txtLoca = findViewById(R.id.txtLoca);
        txtStatus = findViewById(R.id.txtStatus);
        txtType = findViewById(R.id.txtType);
        txtCapacity = findViewById(R.id.txtCapacity);
        btnSubmit = findViewById(R.id.btnSubmit);

        // get intent data
        facilityID = getIntent().getIntExtra("facilityID", 0);
        txtName.setText(getIntent().getStringExtra("facilityName"));
        txtLoca.setText(getIntent().getStringExtra("facilityLocation"));
        txtStatus.setText(getIntent().getStringExtra("facilityStatus"));
        txtType.setText(getIntent().getStringExtra("facilityType"));
        txtCapacity.setText(String.valueOf(getIntent().getIntExtra("facilityCapacity", 0)));
        selectedImageName = getIntent().getStringExtra("facilityPicture");

        // load image if exists
        if (selectedImageName != null) {
            String img = selectedImageName.replace(".jpg", "");
            int resID = getResources().getIdentifier(img, "drawable", getPackageName());
            if (resID != 0) imagePreview.setImageResource(resID);
        }

        // 🔹 Attach submit button
        btnSubmit.setOnClickListener(v -> updateFacility());

        // back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    // ✅ Place your updateFacility() function **here**, after onCreate() but inside the class
    private void updateFacility() {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();

        if (user == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        FacilityService service = ApiUtils.getFacilityService();

        Call<Facility> call = service.updateFacility(
                user.getToken(),
                facilityID,
                txtName.getText().toString().trim(),
                txtLoca.getText().toString().trim(),
                selectedImageName,
                txtStatus.getText().toString().trim(),
                txtType.getText().toString().trim(),
                Integer.parseInt(txtCapacity.getText().toString().trim())
        );

        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(admin_update_facility.this,
                            "Facility updated successfully",
                            Toast.LENGTH_SHORT).show();
                    finish(); // go back to previous activity
                } else if (response.code() == 401) {
                    Toast.makeText(admin_update_facility.this,
                            "Invalid session. Please login again",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(admin_update_facility.this,
                            "Update failed: " + response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                Toast.makeText(admin_update_facility.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
