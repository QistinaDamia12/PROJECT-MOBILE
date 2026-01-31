package com.example.facility_bookuitm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.facility_bookuitm.model.Reservation;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.ReservationService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_add_reservation extends AppCompatActivity {

    private TextView tvFacilityName, tvFacilityType, tvFacilityCapacity,
            tvFacilityLocation, txtDate, txtTime;
    private EditText etPurpose;
    private Button btnReserve;
    private ImageView imgFacility;

    private Calendar calendar = Calendar.getInstance();
    private String selectedTime;
    private int facilityId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_add_reservation);

        // ===== FIND VIEWS =====
        tvFacilityName = findViewById(R.id.tvFacilityName);
        tvFacilityType = findViewById(R.id.tvFacilityType);
        tvFacilityLocation = findViewById(R.id.tvFacilityLocation);
        tvFacilityCapacity = findViewById(R.id.tvFacilityCapacity);
        txtDate = findViewById(R.id.txtSelectedDate);
        txtTime = findViewById(R.id.txtSelectedTime);
        etPurpose = findViewById(R.id.etPurpose);
        btnReserve = findViewById(R.id.btnNextToConfirm);
        imgFacility = findViewById(R.id.imgFacility);

        // ===== GET INTENT DATA =====
        facilityId = getIntent().getIntExtra("facility_id", -1);
        String facilityName = getIntent().getStringExtra("facility_name");
        String facilityImage = getIntent().getStringExtra("facility_image");
        String facilityCapacity = getIntent().getStringExtra("facility_capacity");
        String facilityType = getIntent().getStringExtra("facility_type");
        String facilityLocation = getIntent().getStringExtra("facility_location");

        if (facilityId == -1) {
            Toast.makeText(this, "Invalid facility", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ===== DISPLAY DATA =====
        tvFacilityName.setText(facilityName != null ? facilityName : "-");
        tvFacilityType.setText(facilityType != null ? facilityType : "-");
        tvFacilityLocation.setText(facilityLocation != null ? facilityLocation : "-");
        tvFacilityCapacity.setText(
                facilityCapacity != null ? facilityCapacity + " People" : "-"
        );

        // ===== LOAD IMAGE =====
        if (facilityImage != null && !facilityImage.isEmpty()) {
            if (facilityImage.contains(".")) {
                facilityImage = facilityImage.substring(0, facilityImage.lastIndexOf("."));
            }
            int resID = getResources().getIdentifier(
                    facilityImage, "drawable", getPackageName()
            );

            if (resID != 0) {
                Glide.with(this)
                        .load(resID)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .into(imgFacility);
            } else {
                imgFacility.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            imgFacility.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // ===== GET USER =====
        user = new SharedPrefManager(this).getUser();

        // ===== LISTENERS =====
        txtDate.setOnClickListener(v -> pickDate());
        txtTime.setOnClickListener(v -> pickTime());
        btnReserve.setOnClickListener(v -> reserveFacility());
    }

    // ================= DATE PICKER =================
    private void pickDate() {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            txtDate.setText(
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(calendar.getTime())
            );
        }, y, m, d).show();
    }

    // ================= TIME PICKER =================
    private void pickTime() {
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hour, minute) -> {
            selectedTime = String.format(
                    Locale.getDefault(), "%02d:%02d", hour, minute
            );
            txtTime.setText(selectedTime);
        }, h, m, true).show();
    }

    // ================= RESERVE =================
    private void reserveFacility() {

        String date = txtDate.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();
        String status = "Pending"; // constant value

        if (date.isEmpty() || selectedTime == null || purpose.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user == null || user.getToken() == null || user.getToken().isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        // prevent double click
        btnReserve.setEnabled(false);

        ReservationService service = ApiUtils.getReservationService();

        // 🔥 include the constant status here
        Call<Reservation> call = service.addReservation(
                user.getToken(),
                date,
                selectedTime,
                purpose,
                status,      // <-- pass "PENDING"
                facilityId,
                user.getId()
        );


        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {

                if (response.isSuccessful()) {

                    // 🔥 Always go to success page
                    Intent intent = new Intent(
                            user_add_reservation.this,
                            UserReservationSuccessful.class
                    );

                    // Safe fallback data
                    intent.putExtra("facilityName", tvFacilityName.getText().toString());
                    intent.putExtra("reserveDate", date);
                    intent.putExtra("reserveTime", selectedTime);
                    intent.putExtra("reservePurpose", purpose);

                    if (response.body() != null) {
                        Reservation r = response.body();
                        intent.putExtra("reserveID", r.getReserveID());
                    }

                    startActivity(intent);
                    finish();

                } else {
                    btnReserve.setEnabled(true);
                    Toast.makeText(
                            user_add_reservation.this,
                            "Reservation failed (" + response.code() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                btnReserve.setEnabled(true);
                Toast.makeText(
                        user_add_reservation.this,
                        "Server error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

}





