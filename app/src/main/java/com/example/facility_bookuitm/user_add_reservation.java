package com.example.facility_bookuitm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class user_add_reservation extends AppCompatActivity {

    // Views (from YOUR XML)
    private TextView txtSelectedDate, txtSelectedTime;
    private EditText etPurpose;
    private ImageView btnBack;
    private LinearLayout btnOpenCalendar, btnOpenTimePicker;
    private Button btnNextToConfirm;

    // Data
    private Date selectedDate;
    private String selectedTime;
    private int facilityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_add_reservation); // 👈 YOUR layout

        // Bind views
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtSelectedTime = findViewById(R.id.txtSelectedTime);
        etPurpose = findViewById(R.id.etPurpose);
        btnBack = findViewById(R.id.btnBack);
        btnOpenCalendar = findViewById(R.id.btnOpenCalendar);
        btnOpenTimePicker = findViewById(R.id.btnOpenTimePicker);
        btnNextToConfirm = findViewById(R.id.btnNextToConfirm);

        // Get facility_id from previous screen
        facilityId = getIntent().getIntExtra("facility_id", -1);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Date picker
        btnOpenCalendar.setOnClickListener(v -> openDatePicker());

        // Time picker
        btnOpenTimePicker.setOnClickListener(v -> openTimePicker());

        // Submit button
        btnNextToConfirm.setOnClickListener(v -> submitForm());
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    selectedDate = selected.getTime();

                    SimpleDateFormat sdf =
                            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    txtSelectedDate.setText(sdf.format(selectedDate));
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void openTimePicker() {
        Calendar c = Calendar.getInstance();

        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedTime = String.format(
                            Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    txtSelectedTime.setText(selectedTime);
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        );

        dialog.show();
    }

    private void submitForm() {
        String purpose = etPurpose.getText().toString().trim();

        // Validation
        if (facilityId == -1) {
            Toast.makeText(this, "Invalid facility", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Please select booking date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTime == null) {
            Toast.makeText(this, "Please select booking time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (purpose.isEmpty()) {
            etPurpose.setError("Purpose is required");
            etPurpose.requestFocus();
            return;
        }

        Toast.makeText(this,
                "Reservation request submitted!",
                Toast.LENGTH_LONG).show();

        }
}

