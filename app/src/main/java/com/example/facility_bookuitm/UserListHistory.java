package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.UserReserveAdapter;
import com.example.facility_bookuitm.model.Reservation;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.ReservationService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListHistory extends AppCompatActivity {

    private ReservationService reservationService;
    private RecyclerView rvHistoryList;
    private UserReserveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_history);

        rvHistoryList = findViewById(R.id.rvHistoryList);
        rvHistoryList.setLayoutManager(new LinearLayoutManager(this));
        rvHistoryList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new UserReserveAdapter(this, new ArrayList<>());
        rvHistoryList.setAdapter(adapter);

        loadUserHistory();
    }

    private void loadUserHistory() {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();

        if (user == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginPage.class));
            finish();
            return;
        }

        reservationService = ApiUtils.getReservationService();
        Call<List<Reservation>> call = reservationService.getReservationsByUser(user.getToken(), user.getId());

        Log.d("DEBUG", "Fetching reservations for user ID: " + user.getId());

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                } else if (response.code() == 401) {
                    Toast.makeText(UserListHistory.this, "Session expired. Login again.", Toast.LENGTH_LONG).show();
                    spm.logout();
                    startActivity(new Intent(UserListHistory.this, LoginPage.class));
                    finish();
                } else {
                    Toast.makeText(UserListHistory.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("DEBUG", "Error response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Toast.makeText(UserListHistory.this, "Error connecting to server", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "onFailure: ", t);
            }
        });
    }

    public void btnDashboard(View view) {
        startActivity(new Intent(this, user_dashboard.class));
        finish();
    }
}
