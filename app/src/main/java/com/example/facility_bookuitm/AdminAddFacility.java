package com.example.facility_bookuitm;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.User;
import com.example.facility_bookuitm.remote.ApiUtils;
import com.example.facility_bookuitm.remote.FacilityService;
import com.example.facility_bookuitm.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddFacility extends AppCompatActivity {

    private EditText txtName, txtType, txtLoca, txtStatus, txtCapacity;
    private Button btnSubmit, btnUploadImage;
    private ImageView imagePreview;
    private Uri imageUri; // for gallery pick
    private int selectedDrawable = -1; // index for drawable array

    private static final int PICK_IMAGE_REQUEST = 1;

    // ===== Preloaded drawable images =====
    private int[] drawableImages = {
            R.drawable.f1,
            R.drawable.f2,
            R.drawable.f3,
            R.drawable.f4,
            R.drawable.f5,
            R.drawable.f6,
            R.drawable.f7,
            R.drawable.ic_launcher_foreground // fallback
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_facility);

        txtName = findViewById(R.id.txtName);
        txtLoca = findViewById(R.id.txtLoca);
        txtType = findViewById(R.id.txtType);
        txtStatus = findViewById(R.id.txtStatus);
        txtCapacity = findViewById(R.id.txtCapacity);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        imagePreview = findViewById(R.id.imagePreview);

        btnUploadImage.setOnClickListener(v -> showImageChoiceDialog());
        btnSubmit.setOnClickListener(this::addNewFacility);
    }

    // ===== Show dialog to choose image source =====
    private void showImageChoiceDialog() {
        String[] options = {"Choose from drawable", "Pick from gallery", "Use default"};
        new AlertDialog.Builder(this)
                .setTitle("Select Image")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            chooseFromDrawable();
                            break;
                        case 1:
                            openImagePicker();
                            break;
                        case 2:
                            imageUri = null;
                            selectedDrawable = -1;
                            imagePreview.setImageResource(R.drawable.ic_launcher_foreground);
                            break;
                    }
                })
                .show();
    }

    // ===== Choose image from preloaded drawable array =====
    private void chooseFromDrawable() {
        String[] drawableNames = {"Image 1", "Image 2", "Image 3", "Default"};
        new AlertDialog.Builder(this)
                .setTitle("Select Drawable Image")
                .setItems(drawableNames, (dialog, which) -> {
                    selectedDrawable = which;
                    imageUri = null; // clear any gallery selection
                    if (which >= 0 && which < drawableImages.length) {
                        imagePreview.setImageResource(drawableImages[which]);
                    }
                })
                .show();
    }

    // ===== Open gallery to pick image =====
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // ===== Handle result from gallery =====
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            selectedDrawable = -1; // clear drawable selection
            imagePreview.setImageURI(imageUri);
        }
    }

    // ===== Add new facility =====
    public void addNewFacility(View v) {
        String name = txtName.getText().toString().trim();
        String loca = txtLoca.getText().toString().trim();
        String type = txtType.getText().toString().trim();
        String status = txtStatus.getText().toString().trim();
        String capacityStr = txtCapacity.getText().toString().trim();

        if (name.isEmpty() || loca.isEmpty() || type.isEmpty() || status.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Capacity must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageName = "default.jpg"; // default

        if (imageUri != null) {
            imageName = getFileName(imageUri); // use gallery image
        } else if (selectedDrawable >= 0 && selectedDrawable < drawableImages.length) {
            // use drawable as facility image name (for server, you may need to map this)
            imageName = "drawable_" + selectedDrawable + ".jpg";
        }

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        User user = spm.getUser();
        if (user == null || user.getToken() == null || user.getId() == 0) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        Facility facility = new Facility();
        facility.setFacilityName(name);
        facility.setFacilityLocation(loca);
        facility.setFacilityType(type);
        facility.setFacilityStatus(status);
        facility.setFacilityCapacity(capacity);
        facility.setFacilityPicture("default.jpg"); // API will handle image later

        FacilityService service = ApiUtils.getFacilityService();
        Call<Facility> call = service.addFacility(
                user.getToken(),
                name,
                loca,
                imageName,
                status,
                type,
                capacity,
                user.getId()
        );

        call.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AdminAddFacility.this,
                            response.body().getFacilityName() + " added successfully",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AdminAddFacility.this, "Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                Toast.makeText(AdminAddFacility.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ===== Helper to get gallery filename =====
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (idx >= 0) result = cursor.getString(idx);
                }
            }
        }
        if (result == null) result = uri.getLastPathSegment();
        return result;
    }
}




