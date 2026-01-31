package com.example.facility_bookuitm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.ReservationHistoryAdapter;
import com.example.facility_bookuitm.model.Reservation;
import com.example.facility_bookuitm.remote.ReservationService;
import com.example.facility_bookuitm.remote.RetrofitClient;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationHistoryActivity extends AppCompatActivity {

    RecyclerView rvReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_history);

        rvReservation = findViewById(R.id.rvHistoryList);
        rvReservation.setLayoutManager(new LinearLayoutManager(this));

        //loadReservations();
    }

//    private void loadReservations() {
//
//        String token = SharedPrefManager.getInstance(this).getToken();
//        int userId = SharedPrefManager.getInstance(this).getId();
//
//        ReservationService reservationService = RetrofitClient.getReservationService();
//
//        reservationService.getReservationsByUser(token, userId)
//                .enqueue(new Callback<List<Reservation>>() {
//                    @Override
//                    public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//
//                            List<Reservation> reservationList = response.body();
//                            ReservationHistoryAdapter adapter =
//                                    new ReservationHistoryAdapter(ReservationHistoryActivity.this, reservationList);
//                            rvReservation.setAdapter(adapter);
//
//                        } else {
//                            Toast.makeText(ReservationHistoryActivity.this,
//                                    "No reservations found",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Reservation>> call, Throwable t) {
//                        Toast.makeText(ReservationHistoryActivity.this,
//                                "Failed: " + t.getMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
}
