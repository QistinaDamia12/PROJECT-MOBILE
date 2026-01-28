package com.example.facility_bookuitm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.FacilityAdapter;
import com.example.facility_bookuitm.model.DeleteResponse;
import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class admin_list_facility extends AppCompatActivity {

    private FacilityService facilityService;
    private RecyclerView rvFacilityList;
    private FacilityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_list_facility);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.facilityList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvFacilityList = findViewById(R.id.rvFacilityList);
        registerForContextMenu(rvFacilityList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();

        if (user == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            clearSessionAndRedirect();
            return;
        }

        String token = user.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Session expired. Please login again", Toast.LENGTH_SHORT).show();
            clearSessionAndRedirect();
            return;
        }

        facilityService = ApiUtils.getFacilityService();

        facilityService.getAllFacility(token).enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.code() == 200) {
                    List<Facility> facilities = response.body();

                    adapter = new FacilityAdapter(getApplicationContext(), facilities);
                    rvFacilityList.setAdapter(adapter);
                    rvFacilityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvFacilityList.addItemDecoration(new DividerItemDecoration(
                            rvFacilityList.getContext(),
                            DividerItemDecoration.VERTICAL
                    ));

                    Toast.makeText(getApplicationContext(),
                            facilities.size() + " facilities loaded",
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid session. Please login again",
                            Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error connecting to the server",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void displayAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finish();
        startActivity(new Intent(this, loginPage.class));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.facility_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Facility selectedFacility = adapter.getSelectedItem();

        if (selectedFacility == null) return super.onContextItemSelected(item);

        if (item.getItemId() == R.id.menu_update) {
            doUpdate(selectedFacility);
        }

        if (item.getItemId() == R.id.menu_delete) {
            doDeleteBook(selectedFacility);
        }

        return super.onContextItemSelected(item);
    }

    private void doUpdate(Facility selectedFacility) {

        if (selectedFacility == null) {
            Toast.makeText(this, "No facility selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, admin_update_facility.class);

        // Pass data to update page
        intent.putExtra("facilityID", selectedFacility.getFacilityID());
        intent.putExtra("facilityName", selectedFacility.getFacilityName());
        intent.putExtra("facilityLocation", selectedFacility.getFacilityLocation());
        intent.putExtra("facilityStatus", selectedFacility.getFacilityStatus());
        intent.putExtra("facilityType", selectedFacility.getFacilityType());
        intent.putExtra("facilityCapacity", selectedFacility.getFacilityCapacity());
        intent.putExtra("facilityPicture", selectedFacility.getFacilityPicture());

        startActivity(intent);
    }


    private void doDeleteBook(Facility selectedFacility) {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        facilityService = ApiUtils.getFacilityService();
        facilityService.deleteFacility(token, selectedFacility.getFacilityID())
                .enqueue(new Callback<DeleteResponse>() {
                    @Override
                    public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                        if (response.code() == 200) {
                            displayAlert("Facility successfully deleted");
                            updateRecyclerView();
                        } else if (response.code() == 401) {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid session. Please login again",
                                    Toast.LENGTH_LONG).show();
                            clearSessionAndRedirect();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + response.message(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteResponse> call, Throwable t) {
                        displayAlert("Error: " + t.getMessage());
                    }
                });
    }


    public void floatingAddBookClicked(View view) {
        startActivity(new Intent(getApplicationContext(), admin_add_facility.class));
    }
}

