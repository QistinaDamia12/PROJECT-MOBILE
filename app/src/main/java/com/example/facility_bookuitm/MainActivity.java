package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facility_bookuitm.model.User;
import sharedpref.sharedPrefManager;


public class MainActivity extends AppCompatActivity {

    // --- ADD THE MENU METHODS HERE (Outside of onCreate) ---

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Use MenuInflater to load your XML file [cite: 183, 184]
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle clicks on the menu items
        if (item.getItemId() == R.id.profile) {
            Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- YOUR EXISTING ONCREATE ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvHello = findViewById(R.id.tvHello);

        sharedPrefManager spm = new sharedPrefManager(getApplicationContext());

        //  Check login session
        if (!spm.isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        //  User logged in
        User user = spm.getUser();
        tvHello.setText("Hello " + user.getUsername());

        // 🔁 Redirect based on role
        if (user.getRole().equalsIgnoreCase("Admin")) {
            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            startActivity(new Intent(this, FacilityListActivity.class));
        }
        finish();
    }

    public void logoutClicked(View view) {

        // clear the shared preferences
        sharedPrefManager spm = new sharedPrefManager(getApplicationContext());
        spm.logout();

        // display message
        Toast.makeText(getApplicationContext(),
                "You have successfully logged out.",
                Toast.LENGTH_LONG).show();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


    }
}