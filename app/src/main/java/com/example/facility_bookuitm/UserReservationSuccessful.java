package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserReservationSuccessful  extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_reservation_success);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reserveSuccess), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void btnHistory(View view) {

        finish();
        Intent intent = new Intent(this, UserListHistory.class);
        startActivity(intent);
    }
    public void btnDashboard(View view) {

        finish();
        Intent intent = new Intent(this, user_dashboard.class);
        startActivity(intent);
    }
}
