package com.example.projectapp.View;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectapp.Controller.AddEventCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Model.Event;
import com.example.projectapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This activity handles the creation of new events within the application.
 * It allows users to set event information, upload an image, and save the
 * event to Firebase.
 */
public class CreateNewEventActivity extends AppCompatActivity implements AddEventCallback {

    private TextInputEditText eventDateEditText;
    private Calendar calendar;
    private MaterialButton uploadButton;
    ActivityResultLauncher<String> resultLauncher;
    private ImageView poster;
    private String encodedImage;
    private TextInputEditText eventNameEditText, eventDescriptionEditText, attendanceLimitEditText, eventStartTimeEditText, eventEndTimeEditText;
    private final DataHandler dataHandler = DataHandler.getInstance();
    private RadioGroup checkInOptionGroup;
    private Boolean trackLocation = null;

    /**
     * Initializes the activity, sets up UI elements, and prepares date picker functionality.
     *
     * @param savedInstanceState
     *      The saved instance state of the activity.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        eventDateEditText = findViewById(R.id.eventDateEditText);
        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText);
        attendanceLimitEditText = findViewById(R.id.attendanceLimitEditText);
        eventStartTimeEditText = findViewById(R.id.eventStartTimeEditText);
        eventEndTimeEditText = findViewById(R.id.eventEndTimeEditText);
        calendar = Calendar.getInstance();
        checkInOptionGroup = findViewById(R.id.checkInOptionGroup);


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
                                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
                                eventDateEditText.setText(selectedDate);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        eventStartTimeEditText.setOnClickListener(v -> {
            timePicker(eventStartTimeEditText);
        });

        eventEndTimeEditText.setOnClickListener(v -> {
            timePicker(eventEndTimeEditText);
        });

        checkInOptionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button is selected
                if (checkedId == R.id.checkInLocation) {
                    // "Check In at Location" radio button is selected
                    trackLocation = true;
                } else if (checkedId == R.id.noCheckInLocation) {
                    // "Do Not Check In at Location" radio button is selected
                    trackLocation = false;
                }
            }
        });

        MaterialButton saveEventButton = findViewById(R.id.newQrButton);
        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        uploadButton = findViewById(R.id.upload);
        poster = findViewById(R.id.eventPosterImageView);
        registerResult();
        uploadButton.setOnClickListener(v -> pickImage());
    }

    /**
     * Extracts entered event information, creates a new Event object,
     * and saves the event data to Firebase.
     */
    private void saveEvent() {
        // Get the event details from the input fields
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDate = eventDateEditText.getText().toString().trim();
        String eventStartTime = eventStartTimeEditText.getText().toString().trim();
        String eventEndTime = eventEndTimeEditText.getText().toString().trim();
        String eventDescription = eventDescriptionEditText.getText().toString().trim();
        String attendanceLimitStr = attendanceLimitEditText.getText().toString().trim();

        // Check if inputs are valid
        if (eventName.isEmpty() || eventDate.isEmpty() || eventDescription.isEmpty() || eventStartTime.isEmpty() || eventEndTime.isEmpty() || (trackLocation == null)) {
            Toast.makeText(CreateNewEventActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!attendanceLimitStr.isEmpty() && Integer.parseInt(attendanceLimitStr) == 0){
            Toast.makeText(CreateNewEventActivity.this, "Attendance limit can't be 0", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date currentDateTime = new Date();
        Date eventStartParsed;
        Date eventEndParsed;

        try {
            eventStartParsed = sdf.parse(eventDate + " " + eventStartTime);
            eventEndParsed = sdf.parse(eventDate + " " + eventEndTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (currentDateTime.after(eventStartParsed) || currentDateTime.after(eventEndParsed) || eventStartParsed.after(eventEndParsed)){
            Toast.makeText(CreateNewEventActivity.this, "Date or time values are invalid", Toast.LENGTH_SHORT).show();
            return;
        }


        Integer attendanceLimit = attendanceLimitStr.isEmpty() ? 0 : Integer.parseInt(attendanceLimitStr);

        // Set the event details to the newEvent object

        Event newEvent = new Event(eventName, eventDate, eventStartTime, eventEndTime, dataHandler.getLocalOrganizer().getName(), dataHandler.getLocalOrganizer().getOrganizerId(), attendanceLimit, eventDescription, encodedImage);
        newEvent.setTrackLocation(trackLocation);

        dataHandler.addEvent(newEvent, this);
    }

    @Override
    public void onAddEvent(Event event) {
        // Success handling
        if (event != null){
            Toast.makeText(CreateNewEventActivity.this, "Event saved successfully", Toast.LENGTH_SHORT).show();

            // After showing the toast, start the GenerateQrCodeActivity
            Intent intent = new Intent(CreateNewEventActivity.this, GenerateQrCodeActivity.class);
            intent.putExtra("EVENT_ID", event.getEventId()); // Pass the event ID to the next activity
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(CreateNewEventActivity.this, "Failed to save event, image quality might be too high", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launches an image picker intent to allow users to select an image for the event poster.
     */
    private void pickImage(){
        resultLauncher.launch("image/*");
    }

    /**
     * Registers an ActivityResultLauncher to handle the result of the image selection process.
     * Within this, updates the ImageView and encodes the selected image for storage.
     */
    private void registerResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            poster.setImageURI(result);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                                // Set the encodedImage string here
                                encodedImage = bitmapToString(bitmap);
                            } catch (Exception e) {
                                Toast.makeText(CreateNewEventActivity.this, "Failed to load image.", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * Converts a bitmap image into a Base64 encoded string representation.
     *
     * @param bitmap
     *      The bitmap to be encoded.
     * @return
     *      Base64 encoded string of the bitmap.
     */
    public String bitmapToString(Bitmap bitmap) {
        int maxSize = 1300; // Maximum dimension (width or height) for the resized bitmap
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Check if resizing is needed
        if (width > maxSize || height > maxSize) {
            float scale = Math.min(((float) maxSize) / width, ((float) maxSize) / height);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);

            // Rotate the resized bitmap by 90 degrees (adjust as needed)
            matrix.postRotate(90); // You can change the rotation angle here

            Bitmap rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);

            // Convert the rotated bitmap to a Base64 encoded string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int quality = 100; // Initial quality
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            while (baos.size() > 1024 * 1024) { // 1 MiB in bytes
                baos.reset(); // Reset the stream
                quality -= 10; // Reduce quality by 10 each time
                if (quality <= 0) {
                    break; // Exit loop if quality reaches 0
                }
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }

            byte[] byteArray = baos.toByteArray();
            Log.i("ProfileEditActivity", "encodedString2 " + Base64.encodeToString(byteArray, Base64.DEFAULT));
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            // No resizing needed, directly convert the original bitmap to a Base64 encoded string
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }

    private void timePicker(EditText editText){

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        editText.setText(String.format("%02d:%02d", hour, minute));
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public static Bitmap rotateImageIfNeeded(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
                default:
                    // No rotation needed
                    return BitmapFactory.decodeFile(imagePath);
            }

            Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
            return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions (e.g., file not found, invalid image)
            return null;
        }
    }
}

