package com.example.facility_bookuitm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.facility_bookuitm.adapter.UserFacilityAdapter;
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
    private RecyclerView rvReserveFacility;
    private UserFacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_reservationfacility_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reservationFacilityList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvReserveFacility = findViewById(R.id.rvReserveFacility);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();
        String token = user.getToken();

        facilityService = ApiUtils.getFacilityService();
        facilityService.getAllFacility(token).enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    List<Facility> facilities = response.body();

                    // Initialize adapter with click listener
                    adapter = new UserFacilityAdapter(getApplicationContext(), facilities, facility -> {
                        // This lambda is called when user clicks an AVAILABLE facility
                        if ("AVAILABLE".equalsIgnoreCase(facility.getFacilityStatus())) {
                            Intent intent = new Intent(user_list_facility.this, user_add_reservation.class);
                            intent.putExtra("facility_id", facility.getFacilityID());
                            startActivity(intent);
                        } else {
                            Toast.makeText(user_list_facility.this,
                                    "This facility is under maintenance and cannot be reserved.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    rvReserveFacility.setAdapter(adapter);
                    rvReserveFacility.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvReserveFacility.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvReserveFacility.addItemDecoration(dividerItemDecoration);

                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finish();
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
}
