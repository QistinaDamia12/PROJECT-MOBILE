//package com.example.facility_bookuitm;
//
//import android.os.Bundle;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.facility_bookuitm.adapter.ReservationAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HistoryActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private ReservationAdapter adapter; // You'll create this next
//    private List<History> historyList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_page); // Use your main layout with the two buttons
//
//        // Handle the Button Highlighting
//        Button btnFacilities = findViewById(R.id.btnFacilities);
//        Button btnHistory = findViewById(R.id.btnHistory);
//
//        btnHistory.setSelected(true); // Highlight history by default on this screen
//
//        btnFacilities.setOnClickListener(v -> finish()); // Go back to Facility list
//
//        // Set up the List
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        historyList = new ArrayList<>();
//        historyList.add(new History("Futsal Court A", "12 Oct 2025", "CONFIRMED"));
//        historyList.add(new History("Dewan Agung", "15 Oct 2025", "PENDING"));
//
//        adapter = new ReservationAdapter(historyList);
//        recyclerView.setAdapter(adapter);
//    }
//}