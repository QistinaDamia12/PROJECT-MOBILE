package com.example.facility_bookuitm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.Reservation;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.remote.ReservationService;
import com.example.facility_bookuitm.remote.UserService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_add_reservation extends AppCompatActivity {

    private TextView txtSelectedDate, txtSelectedTime, tvName, tvType, tvLoca, tvCapacity;
    private EditText etPurpose;
    private ImageView btnBack, imagePreview;
    private LinearLayout btnOpenCalendar, btnOpenTimePicker;
    private Button btnNextToConfirm;

    private int facilityId;
    private Facility facilityData;
    private ReservationService reservationService;
    private FacilityService facilityService;
    private UserService userService;

    private Calendar selectedCalendar = Calendar.getInstance();
    private String selectedTime;
    private User loggedInUser; // fetched from SharedPrefManager/API
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_add_reservation);

        // Bind views
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtSelectedTime = findViewById(R.id.txtSelectedTime);
        etPurpose = findViewById(R.id.etPurpose);
        btnBack = findViewById(R.id.btnBack);
        btnOpenCalendar = findViewById(R.id.btnOpenCalendar);
        btnOpenTimePicker = findViewById(R.id.btnOpenTimePicker);
        btnNextToConfirm = findViewById(R.id.btnNextToConfirm);

        tvName = findViewById(R.id.tvName);
        tvType = findViewById(R.id.tvType);
        tvLoca = findViewById(R.id.tvLoca);
        tvCapacity = findViewById(R.id.tvCapacity);
        imagePreview = findViewById(R.id.imagePreview);

        // Get facility ID from intent
        facilityId = getIntent().getIntExtra("facility_id", -1);
        if (facilityId == -1) {
            Toast.makeText(this, "Invalid facility", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initialize API services
        reservationService = ApiUtils.getReservationService();
        facilityService = ApiUtils.getFacilityService();
        userService = ApiUtils.getUserService();

        // Load logged-in user from SharedPrefManager
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        loggedInUser = spm.getUser();
        if (loggedInUser != null) {
            userToken = loggedInUser.getToken();
        }

        // Load facility info
        loadFacilityInfo();

        // Button actions
        btnBack.setOnClickListener(v -> finish());
        btnOpenCalendar.setOnClickListener(v -> openDatePicker());
        btnOpenTimePicker.setOnClickListener(v -> openTimePicker());
        btnNextToConfirm.setOnClickListener(v -> submitReservation());
    }

    private void loadFacilityInfo() {
        String token = "YOUR_API_KEY"; // replace with actual user token

        facilityService.getFacilityById(token, facilityId).enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                if (response.isSuccessful() && response.body() != null) {
                    facilityData = response.body();
                    tvName.setText(facilityData.getFacilityName());
                    tvType.setText(facilityData.getFacilityType());
                    tvLoca.setText(facilityData.getFacilityLocation());
                    tvCapacity.setText(facilityData.getFacilityCapacity() + " People");

                    // Placeholder image
                    int resId = getResources().getIdentifier(
                            "ic_facility_placeholder", "drawable", getPackageName());
                    imagePreview.setImageResource(resId);
                } else {
                    Toast.makeText(user_add_reservation.this, "Facility not found", Toast.LENGTH_SHORT).show();
                    Log.e("LOAD_FACILITY", "Response code: " + response.code());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                Toast.makeText(user_add_reservation.this, "Failed to load facility", Toast.LENGTH_SHORT).show();
                Log.e("LOAD_FACILITY", "Error: " + t.getMessage());
                finish();
            }
        });
    }

    private void openDatePicker() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, y, m, d) -> {
            selectedCalendar.set(y, m, d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            txtSelectedDate.setText(sdf.format(selectedCalendar.getTime()));
        }, year, month, day).show();
    }

    private void openTimePicker() {
        int hour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = selectedCalendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, h, m) -> {
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
            txtSelectedTime.setText(selectedTime);
        }, hour, minute, true).show();
    }

    private void submitReservation() {
        if (loggedInUser == null || userToken == null) {
            Toast.makeText(this, "User not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        String purpose = etPurpose.getText().toString().trim();
        if (txtSelectedDate.getText().toString().equals("Select Date")) {
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedTime == null) {
            Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show();
            return;
        }
        if (purpose.isEmpty()) {
            etPurpose.setError("Purpose required");
            etPurpose.requestFocus();
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setFacilityID(facilityId);
        reservation.setReserveDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCalendar.getTime()));
        reservation.setReserveTime(selectedTime);
        reservation.setReservePurpose(purpose);
        reservation.setUserID(String.valueOf(loggedInUser.getId()));


        reservationService.addReservation(
                userToken,
                reservation.getReserveDate(),
                reservation.getReserveTime(),
                reservation.getReservePurpose(),
                facilityId,               // pass as int
                loggedInUser.getId()      // pass as int
        ).enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(user_add_reservation.this, "Reservation Successful Request", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(user_add_reservation.this, user_reservationfacility_list.class));
                    finish();
                } else {
                    Toast.makeText(user_add_reservation.this, "Failed to submit reservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Toast.makeText(user_add_reservation.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



