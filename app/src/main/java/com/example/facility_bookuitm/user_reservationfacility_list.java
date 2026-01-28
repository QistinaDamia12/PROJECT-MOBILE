package com.example.facility_bookuitm;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.FacilityAdapter;
import com.example.facility_bookuitm.remote.FacilityService;

public class user_reservationfacility_list extends AppCompatActivity {

    //initialize id text
    private FacilityService facilityService;
    private RecyclerView rvReserveFacility;
    private FacilityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_reservationfacitlity_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reservationFacilityList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvReserveFacility = findViewById(R.id.rvReserveFacility);
        registerForContextMenu(rvReserveFacility);
    }
}