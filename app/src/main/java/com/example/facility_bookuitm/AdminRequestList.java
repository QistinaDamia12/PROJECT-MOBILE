package com.example.facility_bookuitm;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AdminRequestList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_request_list);

        // REMOVED: The EdgeToEdge/ViewCompat code block that was looking for R.id.main
        // This prevents the "NullPointerException" crash.
    }

    // This method handles the back button click defined in your XML
    public void backToDashboard(View view) {
        // Ensure 'admin_dashboard' matches exactly the name of your Dashboard Java class
        Intent intent = new Intent(this, admin_dashboard.class);

        // Flags to clear history so the user goes fresh to the dashboard
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }
}