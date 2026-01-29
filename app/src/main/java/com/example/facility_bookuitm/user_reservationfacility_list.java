package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class user_reservationfacility_list extends AppCompatActivity {

    private FacilityService facilityService;
    private RecyclerView rvReserveFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_reservationfacility_list);

        rvReserveFacility = findViewById(R.id.rvReserveFacility);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reservationFacilityList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                if (response.code() == 200) {
                    List<Facility> facilities = response.body();

                    UserFacilityAdapter adapter = new UserFacilityAdapter(user_reservationfacility_list.this, facilities, facility -> {
                        Intent intent = new Intent(user_reservationfacility_list.this, user_add_reservation.class);
                        intent.putExtra("facility_id", facility.getFacilityID());
                        startActivity(intent);
                    });

                    rvReserveFacility.setAdapter(adapter);
                    rvReserveFacility.setLayoutManager(new LinearLayoutManager(user_reservationfacility_list.this));

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvReserveFacility.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvReserveFacility.addItemDecoration(dividerItemDecoration);

                } else if (response.code() == 401) {
                    spm.logout();
                    finish();
                } else {
                    Toast.makeText(user_reservationfacility_list.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                Toast.makeText(user_reservationfacility_list.this, "Error connecting to server", Toast.LENGTH_LONG).show();
            }
        });
    }
}
