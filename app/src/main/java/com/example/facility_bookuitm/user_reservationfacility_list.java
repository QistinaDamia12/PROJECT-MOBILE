package com.example.facility_bookuitm;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.AdminFacilityAdapter;
import com.example.facility_bookuitm.adapter.UserFacilityAdapter;
import com.example.facility_bookuitm.model.DeleteResponse;
import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_reservationfacility_list extends AppCompatActivity {

    //initialize id text
    private FacilityService facilityService;
    private RecyclerView rvReserveFacility;
    private UserFacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_reservationfacitlity_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reservationFacilityList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvReserveFacility = findViewById(R.id.rvReserveFacility);
        registerForContextMenu(rvReserveFacility);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();

        if (user == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            clearSessionAndRedirect();
            return;
        }

        String token = user.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Session expired. Please login again", Toast.LENGTH_SHORT).show();
            clearSessionAndRedirect();
            return;
        }

        facilityService = ApiUtils.getFacilityService();

        facilityService.getAllFacility(token).enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.code() == 200) {
                    List<Facility> facilities = response.body();

                    adapter = new UserFacilityAdapter(getApplicationContext(), facilities);
                    rvReserveFacility.setAdapter(adapter);
                    rvReserveFacility.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvReserveFacility.addItemDecoration(new DividerItemDecoration(
                            rvReserveFacility.getContext(),
                            DividerItemDecoration.VERTICAL
                    ));

                    Toast.makeText(getApplicationContext(),
                            facilities.size() + " facilities loaded",
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid session. Please login again",
                            Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error connecting to the server",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void displayAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finish();
        startActivity(new Intent(this, loginPage.class));
    }


    public void floatingAddBookClicked(View view) {
        startActivity(new Intent(getApplicationContext(), admin_add_facility.class));
    }
}

