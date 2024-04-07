package com.example.projectapp.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
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

import com.example.projectapp.Controller.AddAttendeeCallback;
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Controller.LocalAttendeeListenerCallback;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProfileEditActivity extends AppCompatActivity implements AddAttendeeCallback, LocalAttendeeListenerCallback {
    private ImageView avatar;
    private String encodedImage;
    private ActivityResultLauncher<String> resultLauncher;
    private EditText userNameEditText, emailEditText, phoneEditText;
    private Button saveButton, deleteButton;
    private Attendee currentAttendee;
    private final DataHandler dataHandler = DataHandler.getInstance();
    private boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        avatar = findViewById(R.id.avatarImage);
        Button avatarButton = findViewById(R.id.avatarImageButton);

        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        registerResult();

        currentAttendee = dataHandler.getLocalAttendee();

        if (currentAttendee != null){
            loadAttendeeInfo();
            updateDeleteButtonVisibility();
            dataHandler.addLocalAttendeeListener(this);
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    private void deleteProfilePicAndGenerateNewOne() {
        Bitmap generatedProfilePic = IdenticonGenerator.generate(currentAttendee.getName(), 128);
        if (generatedProfilePic != null) {
            avatar.setImageBitmap(generatedProfilePic);
            encodedImage = bitmapToString(generatedProfilePic);
        }
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
        userNameEditText.setText(currentAttendee.getName());
        emailEditText.setText(currentAttendee.getHomepage());
        phoneEditText.setText(currentAttendee.getContactInfo());

        encodedImage = currentAttendee.getProfilePic();
        if (encodedImage != null && !encodedImage.isEmpty()) {
            Bitmap bitmap = stringToBitmap(encodedImage);
            if (bitmap != null) {
                //bitmap = ExifUtil.rotateBitmap(bitmap, exifOrientation);;
                Log.i("ProfileEditActivity", "encodedString1 "+ bitmapToString(bitmap)+ "vs " + encodedImage);
                avatar.setImageBitmap(bitmap);
            }
        }
        else {
            Bitmap generatedProfilePic = IdenticonGenerator.generate(currentAttendee.getName(), 128);
            avatar.setImageBitmap(generatedProfilePic);
            encodedImage = bitmapToString(generatedProfilePic);
        }

    }

    private void saveProfileChanges() {
        String name = userNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // Stop execution if any of the fields is empty
        }

        if (encodedImage == null || encodedImage.isEmpty()) {
            Bitmap generatedProfilePic = IdenticonGenerator.generate(name, 128);
            if (generatedProfilePic != null) {
                encodedImage = bitmapToString(generatedProfilePic);
            }
        }

        // create new attendee, first time using app
        if (currentAttendee == null){
            currentAttendee = new Attendee(encodedImage, name, email, phone);
            dataHandler.setLocalAttendee(currentAttendee);
            saveLocalAttendeeId();
            dataHandler.addAttendee(currentAttendee, true, this);
        }
        else {
            currentAttendee.setName(name);
            currentAttendee.setHomepage(email);
            currentAttendee.setContactInfo(phone);
            currentAttendee.setProfilePic(encodedImage);
            dataHandler.addAttendee(currentAttendee, false, this);
        }
    }

    @Override
    public void onAddAttendee(Attendee attendee, boolean newAttendee) {
        if (attendee != null){
            Toast.makeText(this, "Updated profile", Toast.LENGTH_SHORT).show();
            if (newAttendee){
                Intent intent = new Intent(this, AttendeePageActivity.class);
                startActivity(intent);
            }
            finish();
        }
        else {
            Toast.makeText(this, "Couldn't update profile, image quality might be too high", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImage() {
        resultLauncher.launch("image/*");
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        Uri imageUri = result;
                        avatar.setImageURI(imageUri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            encodedImage = bitmapToString(bitmap);
                        } catch (IOException e) {
                            Log.e("ProfileEditActivity", "Error converting image", e);
                        }
                    }
                });
    }

    public String bitmapToString(Bitmap bitmap) {
        int maxSize = 3072; // Maximum dimension (width or height) for the resized bitmap
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


    public Bitmap stringToBitmap(String encodedString) {
        try {
            Log.i("ProfileEditActivity", "encodedString3 "+ encodedString);
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

    @Override
    public void onLocalAttendeeDeleted() {
        if (active){
            dataHandler.setLocalAttendee(null);
            restart();
        }
    }

    private void restart(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }

    /**
     * save attendee ID to device
     */
    public void saveLocalAttendeeId(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("AttendeePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("attendeeId", dataHandler.getLocalAttendee().getAttendeeId());
        editor.apply();
    }

    private Bitmap rotateBitmapIfRequired(Bitmap bitmap, String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            File file = File.createTempFile("temp", null, getCacheDir());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decodedBytes);
            fos.close();

            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
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
                    return bitmap;
            }

            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            file.delete(); // Clean up temporary file
            return rotatedBitmap;

        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }

}