package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class UserListHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_history);

        // I removed the "EdgeToEdge" and "ViewCompat" code blocks here.
        // They were causing the crash because your XML doesn't have an ID called 'main'.
    }

    public void backToDashboard(View view) {
        // Ensure 'user_dashboard' matches exactly the name of your Dashboard Java class
        Intent intent = new Intent(this, user_dashboard.class);

        // These flags ensure you return to the main dashboard and don't stack screens
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }
}