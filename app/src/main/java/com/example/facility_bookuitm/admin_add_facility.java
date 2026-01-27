package com.example.facility_bookuitm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class admin_add_facility extends AppCompatActivity {

    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_facility);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        imagePreview = findViewById(R.id.imagePreview);

        //add image to display
        btnUploadImage.setOnClickListener(v -> showImagePicker());

        //go to previous page
        ImageView btnBack2 = findViewById(R.id.btnBack);
        btnBack2.setOnClickListener(v -> {
            // Go back to previous Activity
            finish();
        });

    }

    private void showImagePicker() {
        String[] images = {"Auditorium", "Meeting Room", "Surau", "Court", "Gym", "Lab", "Lecture Room"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Facility Image");
        builder.setItems(images, (dialog, which) -> {
            switch (which) {
                case 0:
                    imagePreview.setImageResource(R.drawable.f1);
                    break;
                case 1:
                    imagePreview.setImageResource(R.drawable.f2);
                    break;
                case 2:
                    imagePreview.setImageResource(R.drawable.f3);
                    break;
                case 3:
                    imagePreview.setImageResource(R.drawable.f4);
                    break;
                case 4:
                    imagePreview.setImageResource(R.drawable.f5);
                    break;
                case 5:
                    imagePreview.setImageResource(R.drawable.f6);
                    break;
                case 6:
                    imagePreview.setImageResource(R.drawable.f7);
                    break;
            }
        });
        builder.show();
    }


}
