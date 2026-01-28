package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.FacilityAdapter;
import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_list_facility extends AppCompatActivity {

    private FacilityService facilityService;
    private RecyclerView rvFacilityList;
    private FacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 1. CHANGED: Now using your existing layout file
        setContentView(R.layout.user_reservationfacility_p1);

        // 2. CHANGED: Updated IDs to match your XML file
        // Your XML uses "recyclerView", not "rvFacilityList"
        rvFacilityList = findViewById(R.id.recyclerView);

        // Setup Back Button (Your XML calls it "btnProfile")
        ImageView btnBack = findViewById(R.id.btnProfile);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }


        // Adjust padding for the top bar so it doesn't hide behind the camera hole
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.topBar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup Layout Manager
        rvFacilityList.setLayoutManager(new LinearLayoutManager(this));

        // Add divider line between items
        rvFacilityList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public void backToDashboard(View view) {
        finish(); // Closes this screen and goes back to Dashboard
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFacilities();
    }

    private void loadFacilities() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        if (user == null || user.getToken() == null) {
            Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        facilityService = ApiUtils.getFacilityService();

        // Using "Bearer " + token as discussed
        facilityService.getAllFacility("Bearer " + user.getToken()).enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Facility> facilityList = response.body();

                    // Initialize adapter
                    adapter = new FacilityAdapter(user_list_facility.this, facilityList);
                    rvFacilityList.setAdapter(adapter);
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Session Invalid. Login again.", Toast.LENGTH_LONG).show();
                    spm.logout();
                    Intent intent = new Intent(user_list_facility.this, loginPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to load data: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                Log.e("UserList", "Error: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}