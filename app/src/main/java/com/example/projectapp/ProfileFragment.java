package com.example.projectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;

/**
 * Store save value of a profile
 */
public class ProfileFragment extends Fragment implements AddAttendeeCallback{

    private EditText userNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private ImageView avatar;
    private String encodedImage;
    ActivityResultLauncher<Intent> resultLauncher;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DataHandler dataHandler = DataHandler.getInstance();
    Integer fulfill = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        // Initialize EditText fields
        userNameEditText = rootView.findViewById(R.id.userNameEditText);
        emailEditText = rootView.findViewById(R.id.emailEditText);
        phoneEditText = rootView.findViewById(R.id.phoneEditText);
        avatar= rootView.findViewById(R.id.avatarImage);

        // Load saved values from SharedPreferences
        if (dataHandler.getAttendee() != null){
            loadSavedValues();
        }

        // Save button click listener
        Button saveButton = rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fulfill = saveValues();
            }
        });


        Button avatarButton = rootView.findViewById(R.id.avatarImageButton);
        registerResult();
        avatarButton.setOnClickListener(v -> pickImage());
        return rootView;
    }

    /**
     * load the saved values on firestore
     */
    private void loadSavedValues() {
        CollectionReference a = db.collection("attendees");
        Query query = a.whereEqualTo("attendeeId", dataHandler.getAttendee().getAttendeeId());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Query execution successful
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Iterate through the query results (assuming attendeeID is unique)
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);

                    // Retrieve the profilePic field directly as a string
                    String profilePicUrl = document.getString("profilePic");
                    avatar.setImageBitmap(stringToBitmap(profilePicUrl));
                    userNameEditText.setText(document.getString("name"));
                    emailEditText.setText(document.getString("contactInfo"));
                    phoneEditText.setText(document.getString("homepage"));
                }
            }
        });
    }

    /**
     * Pick image
     */
    private void pickImage(){
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
            intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        }
        resultLauncher.launch(intent);
    }

    /**
     * get picture and set it
     */
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try{
                            Uri imageUri = o.getData().getData();
                            avatar.setImageURI(imageUri);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            encodedImage = bitmapToString(bitmap);
                        }catch(Exception e){
                            //
                        }
                    }
                }
        );
    }

    /**
     * convert bitmap to string
     * @param bitmap a bitmap
     * @return a string
     */
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    /**
     * convert string to bitmap
     * @param encodedString a string
     * @return a bitmap
     */
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


    /**
     * save values, make sure the attendee have enough information
     * @return 0 if not enough fields, 1 if it is.
     */
    private Integer saveValues() {
        String name = userNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return 0; // Stop execution if any of the fields is empty
        }

        if (encodedImage == null || encodedImage.isEmpty()) {
            Bitmap generatedProfilePic = IdenticonGenerator.generate(name, 128);
            if (generatedProfilePic != null) {
                encodedImage = bitmapToString(generatedProfilePic);
            }
        }

        Attendee attendee = new Attendee(encodedImage, name, email, phone);
        dataHandler.addAttendee(attendee, this);
        return 1;
    }

    @Override
    public void onAddAttendee(Attendee attendee) {
        if (attendee != null){
            Toast.makeText(getActivity(), "Created profile", Toast.LENGTH_SHORT).show();
            dataHandler.setAttendee(attendee);
            saveAttendeeId();
            if (fulfill == 1){
                startActivity(new Intent(getContext(), AttendeePageActivity.class));
            }
        }
        else {
            Toast.makeText(getActivity(), "Couldn't create profile, image quality might be too high", Toast.LENGTH_SHORT).show();
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



    /**
     * save attendee ID
     */
    public void saveAttendeeId(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("AttendeePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("attendeeId", dataHandler.getAttendee().getAttendeeId());
        editor.apply();
    }
}
