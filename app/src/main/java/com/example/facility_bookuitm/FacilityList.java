package com.example.facility_bookuitm;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.FacilityAdapter;

import java.util.ArrayList;
import java.util.List;

public class FacilityList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FacilityAdapter adapter;
    private List<Facility> facilityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reservationfacility_p1); // Load the list screen

        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            facilityList = new ArrayList<>();

            // Add your data
            facilityList.add(new Facility("Auditorium", "Bangunan Pentadbiran", R.drawable.ic_launcher_background, "300"));
            // ... add the rest of your facilities ...

            adapter = new FacilityAdapter(facilityList);
            recyclerView.setAdapter(adapter);
        }
    }
}
