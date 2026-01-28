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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class admin_list_facility extends AppCompatActivity {


    private FacilityService bookService;
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

        // get reference to the RecyclerView bookList
        rvFacilityList = findViewById(R.id.rvFacilityList);

        //register for context menu
        registerForContextMenu(rvFacilityList);

        // fetch and update book list

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        // get user info from SharedPreferences to get token value
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        // get book service instance
        bookService = ApiUtils.getFacilityService();

        // execute the call. send the user token when sending the query
        bookService.getAllFacility(token).enqueue(new Callback<List<Facility>>() {
            @Override
            public void onResponse(Call<List<Facility>> call, Response<List<Facility>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // Get list of book object from response
                    List<Facility> books = response.body();

                    // initialize adapter
                    adapter = new FacilityAdapter(getApplicationContext(), books);

                    // set adapter to the RecyclerView
                    rvFacilityList.setAdapter(adapter);

                    // set layout to recycler view
                    rvFacilityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // add separator between item in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFacilityList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvFacilityList.addItemDecoration(dividerItemDecoration);
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
            public void onFailure(Call<List<Facility>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
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
        Intent intent = new Intent(this, loginPage.class);
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
        Log.d("MyApp", "selected "+selectedFacility.toString());    // debug purpose

        if (item.getItemId() == R.id.menu_update) {
            doUpdateBook(selectedFacility);

        }
        else if (item.getItemId() == R.id.menu_delete) {
            // user clicked the delete contextual menu
            doDeleteBook(selectedFacility);
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Delete book record. Called by contextual menu "Delete"
     * @param selectedBook - book selected by user
     */
    private void doDeleteBook(Facility selectedBook) {
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();

        // prepare REST API call
        FacilityService facilityService = ApiUtils.getFacilityService();
        Call<DeleteResponse> call = bookService.deleteFacility(user.getToken(), selectedBook.getFacilityID());

        // execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("Book successfully deleted");
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

    private void doUpdateBook(Facility selectedFacility)
    {

    }



    /**
     * Action handler for Add Book floating action button
     * @param view
     */
    public void floatingAddBookClicked(View view) {

        Intent intent = new Intent(getApplicationContext(), admin_add_facility.class);
        startActivity(intent);
    }
}