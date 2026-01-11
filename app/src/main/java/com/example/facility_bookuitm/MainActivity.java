package com.example.facility_bookuitm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;         // ADD THIS
import android.view.MenuInflater;
import android.view.MenuItem;     // ADD THIS
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.login_page);

        Button btnLogin = findViewById(R.id.btnLogin);

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            });
        }
    }
}