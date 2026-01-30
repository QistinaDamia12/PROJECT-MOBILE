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
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.adapter.AdminFacilityAdapter;
import com.example.facility_bookuitm.model.DeleteResponse;
import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class admin_list_facility extends AppCompatActivity {

    private FacilityService facilityService;
    private RecyclerView rvFacilityList;
    private AdminFacilityAdapter adapter;

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

        // get reference to the RecyclerView bookList
        rvFacilityList = findViewById(R.id.rvFacilityList);

        //register for context menu
        registerForContextMenu(rvFacilityList);

        // fetch and update book list
        updateRecyclerView();

    }

    private void updateRecyclerView() {
        // get user info from SharedPreferences to get token value
        SharedPrefManager spm = new SharedPrefManager(this);
        User user = spm.getUser();
        String token = user != null ? user.getToken() : "";

        // get facility service instance
        facilityService = ApiUtils.getFacilityService();

        // execute the call
        facilityService.getAllFacility(token).enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Facility> facilities = response.body();

                    // initialize adapter
                    adapter = new AdminFacilityAdapter(getApplicationContext(), facilities);

                    // set adapter to RecyclerView
                    rvFacilityList.setAdapter(adapter);
                    rvFacilityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // add separator between items
                    DividerItemDecoration divider = new DividerItemDecoration(rvFacilityList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvFacilityList.addItemDecoration(divider);

                } else {
                    // API returned error (like 500)
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No details";
                        Log.e("API_ERROR", "Error: " + response.code() + " Body: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // show toast to user
                    Toast.makeText(getApplicationContext(),
                            "Failed to load facilities. Please try again later.",
                            Toast.LENGTH_LONG).show();

                    // optional: show empty placeholder list
                    adapter = new AdminFacilityAdapter(getApplicationContext(), new ArrayList<>());
                    rvFacilityList.setAdapter(adapter);
                    rvFacilityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                Log.e("API_ERROR", "Connection error: " + t.getMessage());

                Toast.makeText(getApplicationContext(),
                        "Error connecting to server. Please check your internet.",
                        Toast.LENGTH_LONG).show();

                // optional: show empty placeholder list
                adapter = new AdminFacilityAdapter(getApplicationContext(), new ArrayList<>());
                rvFacilityList.setAdapter(adapter);
                rvFacilityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });
    }

    /**
     * Delete book record. Called by contextual menu "Delete"
     * @param selectedFacility - book selected by user
     */
    private void doDeleteBook(Facility selectedFacility) {
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // prepare REST API call
        FacilityService facilityService = ApiUtils.getFacilityService();
        Call<DeleteResponse> call = facilityService.deleteFacility(user.getToken(), selectedFacility.getFacilityID());

        // execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("Facility successfully deleted");
                    // update data in list view
                    updateRecyclerView();
                }
                else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
            }
        });
    }

    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.facility_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Facility selectedFacility = adapter.getSelectedItem();
        Log.d("MyApp", "selected "+selectedFacility.toString()); // debug

        if (item.getItemId() == R.id.menu_update) {
            // ✅ Pass all facility data to the update activity
            Intent intent = new Intent(getApplicationContext(), AdminUpdateFacility.class);
            intent.putExtra("facilityID", selectedFacility.getFacilityID());
            intent.putExtra("facilityName", selectedFacility.getFacilityName());
            intent.putExtra("facilityLocation", selectedFacility.getFacilityLocation());
            intent.putExtra("facilityStatus", selectedFacility.getFacilityStatus());
            intent.putExtra("facilityType", selectedFacility.getFacilityType());
            intent.putExtra("facilityCapacity", selectedFacility.getFacilityCapacity());
            intent.putExtra("facilityPicture", selectedFacility.getFacilityPicture());
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_delete) {
            doDeleteBook(selectedFacility);
        }

        return super.onContextItemSelected(item);
    }



    /**
     * Action handler for Add Book floating action button
     * @param view
     */
    public void floatingAddFacilityClicked(View view) {
        // forward user to new
        Intent intent = new Intent(getApplicationContext(), AdminAddFacility.class);
        startActivity(intent);
    }
}
