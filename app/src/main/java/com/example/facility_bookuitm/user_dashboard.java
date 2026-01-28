package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class user_dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_dashboard);

        // Memastikan UI tidak bertindih dengan system bars
        View dashboardView = findViewById(R.id.userDashboard);
        if (dashboardView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(dashboardView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    /**
     * Berfungsi apabila kad/butang 'Book Facility' ditekan
     */
    public void reserveFacility(View view) {
        // 1. Create the Intent to go to the correct page (user_list_facility)
        Intent intent = new Intent(this, user_list_facility.class);

        // 2. Start the activity
        startActivity(intent);
    }

    /**
     * Berfungsi apabila kad/butang 'Profile/History' ditekan
     */
    public void profilebtn(View view) {
        Toast.makeText(this, "Opening Booking Profile...", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, user_.class);
        //startActivity(intent);
    }

    public void bookFacility(View view) {
        finish();
        // forward to Login Page
        Intent intent = new Intent(this, user_list_facility.class);
        startActivity(intent);
    }
}
