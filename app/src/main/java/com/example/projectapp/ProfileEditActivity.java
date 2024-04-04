package com.example.projectapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProfileEditActivity extends AppCompatActivity implements AddAttendeeCallback{
    private ImageView avatar;
    private String encodedImage;
    private ActivityResultLauncher<Intent> resultLauncher;
    private EditText userNameEditText, emailEditText, phoneEditText;
    private Button saveButton, deleteButton;
    private Attendee currentAttendee;
    private DataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        avatar = findViewById(R.id.avatarImage);
        Button avatarButton = findViewById(R.id.avatarImageButton);

        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.delete_button);
        registerResult();
        dataHandler = DataHandler.getInstance();
        loadAttendeeInfo();
        updateDeleteButtonVisibility();

        avatarButton.setOnClickListener(v -> pickImage());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
                updateDeleteButtonVisibility();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProfilePicAndGenerateNewOne();
                updateDeleteButtonVisibility();
            }
        });
    }

    private void deleteProfilePicAndGenerateNewOne() {
        if (currentAttendee == null) {
            Toast.makeText(ProfileEditActivity.this, "No attendee data available.", Toast.LENGTH_SHORT).show();
            return;
        }
        String attendeeId = currentAttendee.getAttendeeId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("attendees").document(attendeeId)
                .update("profilePic", null)
                .addOnSuccessListener(aVoid -> {

                    Bitmap generatedImage = IdenticonGenerator.generate(currentAttendee.getName(), 128);
                    String encodedImage = bitmapToString(generatedImage);
                    currentAttendee.setProfilePic(encodedImage);
                    avatar.setImageBitmap(generatedImage);
                    db.collection("attendees").document(attendeeId)
                            .update("profilePic", encodedImage)
                            .addOnSuccessListener(aVoid1 -> Toast.makeText(ProfileEditActivity.this, "Profile picture updated successfully.", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ProfileEditActivity.this, "Error updating profile picture.", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(ProfileEditActivity.this, "Error setting profile picture to null.", Toast.LENGTH_SHORT).show());
    }



    private void updateDeleteButtonVisibility() {
        if (currentAttendee != null && isUploadedProfilePic(currentAttendee.getProfilePic())) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
        }
    }

    private boolean isUploadedProfilePic(String profilePic) {
        return profilePic != null && profilePic.length() > 1000;
    }


    private void loadAttendeeInfo() {
        currentAttendee = dataHandler.getAttendee();
        if (currentAttendee != null) {
            userNameEditText.setText(currentAttendee.getName());
            emailEditText.setText(currentAttendee.getHomepage());
            phoneEditText.setText(currentAttendee.getContactInfo());

            String encodedImage = currentAttendee.getProfilePic();
            if (encodedImage != null && !encodedImage.isEmpty()) {
                Bitmap bitmap = stringToBitmap(encodedImage);
                if (bitmap != null) {
                    avatar.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void saveProfileChanges() {
        String newName = userNameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();

        if (currentAttendee == null) {
            Toast.makeText(this, "Attendee data is not loaded!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentAttendee.setName(newName);
        currentAttendee.setHomepage(newEmail);
        currentAttendee.setContactInfo(newPhone);

        if (encodedImage != null && !encodedImage.isEmpty()) {
            currentAttendee.setProfilePic(encodedImage);
        }

        dataHandler.addAttendee(currentAttendee, this);

        Intent intent = new Intent(this, AttendeePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAddAttendee(Attendee attendee) {
        if (attendee != null){
            Toast.makeText(this, "Updated profile", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Couldn't update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        avatar.setImageURI(imageUri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            encodedImage = bitmapToString(bitmap);
                        } catch (IOException e) {
                            Log.e("ProfileEditActivity", "Error converting image", e);
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

    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static class IdenticonGenerator {

        public static Bitmap generate(String username, int size) {
            try {
                byte[] hash = MessageDigest.getInstance("MD5").digest(username.getBytes());
                Bitmap identicon = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

                int gridSize = 10;
                int cellSize = size / gridSize;

                for (int x = 0; x < gridSize; x++) {
                    for (int y = 0; y < gridSize; y++) {
                        int i = x < gridSize / 2 ? x : gridSize - 1 - x;
                        if ((hash[i] >> (y % 8) & 0x01) == 0x01) {
                            int color = Color.rgb(hash[i] & 0xFF, hash[(i + 1) % hash.length] & 0xFF, hash[(i + 2) % hash.length] & 0xFF);
                            fillCell(identicon, x, y, cellSize, color);
                        }
                    }
                }

                return identicon;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }

        private static void fillCell(Bitmap bitmap, int x, int y, int cellSize, int color) {
            for (int i = 0; i < cellSize; i++) {
                for (int j = 0; j < cellSize; j++) {
                    bitmap.setPixel(x * cellSize + i, y * cellSize + j, color);
                }
            }
        }
    }

}