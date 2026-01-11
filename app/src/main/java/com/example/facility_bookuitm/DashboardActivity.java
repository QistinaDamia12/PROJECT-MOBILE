package com.example.facility_bookuitm;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    ImageView btnProfile;
    MaterialCardView cardBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);

        // Initialize views
        btnProfile = findViewById(R.id.btnProfile);
        cardBook = findViewById(R.id.cardBook);

        // Profile button click
        btnProfile.setOnClickListener(v -> {
            Toast.makeText(DashboardActivity.this,
                    "Opening Profile...", Toast.LENGTH_SHORT).show();

            // Uncomment when ProfileActivity exists
            // Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            // startActivity(intent);
        });

        // Book Facility card click
        cardBook.setOnClickListener(v -> {
            Toast.makeText(DashboardActivity.this,
                    "Opening Booking Page...", Toast.LENGTH_SHORT).show();

            // Uncomment when BookingActivity exists
            // Intent intent = new Intent(DashboardActivity.this, BookingActivity.class);
            // startActivity(intent);
        });
    }
}

