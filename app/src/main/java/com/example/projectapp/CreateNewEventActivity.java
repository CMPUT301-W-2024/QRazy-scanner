package com.example.projectapp;

import static java.security.AccessController.getContext;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Locale;

public class CreateNewEventActivity extends AppCompatActivity {

    private TextInputEditText eventDateEditText;
    private Calendar calendar;

    private MaterialButton uploadButton;
    ActivityResultLauncher<Intent> resultLauncher;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView poster;
    private Event newEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_fragment);

        eventDateEditText = findViewById(R.id.eventDateEditText);
        calendar = Calendar.getInstance();
        newEvent = new Event();
        db.collection("events").document(newEvent.getEventId()).set(newEvent);



        eventDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the current date as the default date in the picker
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and show it
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateNewEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Set the chosen date to the EditText
                                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                                eventDateEditText.setText(selectedDate);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                db.collection("events").document(newEvent.getEventId()).update("poster", eventDateEditText.getText().toString());
            }
        });

        uploadButton = findViewById(R.id.upload);
        poster = findViewById(R.id.eventPosterImageView);
        registerResult();
        uploadButton.setOnClickListener(v -> pickImage());


    }
    private void pickImage(){
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
            intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        }
        resultLauncher.launch(intent);
    }
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try{
                            Uri imageUri = o.getData().getData();
                            poster.setImageURI(imageUri);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(CreateNewEventActivity.this.getContentResolver(), imageUri);
                            db.collection("events").document(newEvent.getEventId()).update("poster", bitmapToString(bitmap));
                        }catch(Exception e){
                            //
                        }
                    }
                }
        );
    }
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}

