package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class admin_dashboard  extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminDashboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Put this inside admin_dashboard.java
    public void facilityList(View view) {
        Intent intent = new Intent(this, admin_list_facility.class);
        startActivity(intent);
    }

    public void requestList(View view) {

    }
    // In admin_dashboard.java
    public void logout(View view) {
        // Clear session
        com.example.facility_bookuitm.sharedpref.SharedPrefManager spm =
                new com.example.facility_bookuitm.sharedpref.SharedPrefManager(getApplicationContext());
        spm.logout();

        // Close app and go to login
        finish();
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
}