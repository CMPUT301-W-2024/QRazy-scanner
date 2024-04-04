package com.example.projectapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Activity for generating and storing a QR code associated with an event.
 */
public class GenerateQrCodeActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private Event event;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     *  Handles the activity creation. Extracts the event ID from the intent
     *  and sets up the UI.
     *
     *  @param savedInstanceState
     *      The saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        event = (Event) getIntent().getSerializableExtra("EventId"); // Retrieve the eventId

        MaterialButton generateQrCodeButton = findViewById(R.id.generateQrCodeButton); // Find the button by its ID
        generateQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event.getEventId() != null && !event.getEventId().isEmpty()) {
                    // A valid event ID is present, we can generate the QR code
                    try {
                        Bitmap qrCodeBitmap = generateQRCode(event.getEventId());
                        qrCodeImageView.setImageBitmap(qrCodeBitmap);
                        storeQRCodeInFirestore(qrCodeBitmap); // Store the QR code in Firebase
                    } catch (WriterException e) {
                        e.printStackTrace();
                        Toast.makeText(GenerateQrCodeActivity.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No valid event ID, show an error or provide a default image
                    Toast.makeText(GenerateQrCodeActivity.this, "No Event ID provided", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MaterialButton generatePromotionalQrButton = findViewById(R.id.generatePromotionQrCodeButton);
        generatePromotionalQrButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (event.getPromoQrId() != null && !event.getPromoQrId().isEmpty()) {
                    // A valid event ID is present, we can generate the QR code
                    try {
                        Bitmap qrPromoBitmap = generateQRCode(event.getPromoQrId());
                        qrCodeImageView.setImageBitmap(qrPromoBitmap);
                        storePromoQRCodeInFirestore(qrPromoBitmap); // Store the QR code in Firebase
                    } catch (WriterException e) {
                        e.printStackTrace();
                        Toast.makeText(GenerateQrCodeActivity.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No valid event ID, show an error or provide a default image
                    Toast.makeText(GenerateQrCodeActivity.this, "No Event ID provided", Toast.LENGTH_SHORT).show();
                }
            }
        });


        MaterialButton finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackToCreateEvent();
            }
        });

        MaterialButton shareQrCodeButton = findViewById(R.id.shareQrCodeButton);
        shareQrCodeButton.setOnClickListener(v -> {
            if (event != null && event.getEventId() != null && !event.getEventId().isEmpty()) {
                fetchAndShareQRCode(event.getEventId(), "qrCode");
            } else {
                Toast.makeText(GenerateQrCodeActivity.this, "No event selected.", Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton sharePromoButton = findViewById(R.id.sharePromoQrCodeButton);
        sharePromoButton.setOnClickListener(v -> {
            if (event != null && event.getEventId() != null && !event.getEventId().isEmpty()) {
                fetchAndShareQRCode(event.getEventId(), "promoQrCode");
            } else {
                Toast.makeText(GenerateQrCodeActivity.this, "No event selected.", Toast.LENGTH_SHORT).show();
            }
        });

        MaterialButton useOwnQrButton = findViewById(R.id.useOwnQrButton);
        useOwnQrButton.setOnClickListener(v -> {
            Intent i = new Intent(this, ScanActivity.class);
            i.putExtra("usage", "reuseQr");
            i.putExtra("Event", event);
            startActivity(i);
        });

    }

    /**
     * Generates a bitmap representation of a QR code for the given input text.
     *
     * @param text
     *      The text to be encoded into the QR code.
     * @return
     *      Bitmap object containing the QR code image.
     * @throws WriterException
     *      If there's an error during QR code creation.
     */
    private Bitmap generateQRCode(String text) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        final int width = 500; // Width in pixels
        final int height = 500; // Height in pixels
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        try {
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Black and White
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * Converts a bitmap image into a Base64 encoded string representation.
     *
     * @param bitmap
     *      The bitmap to be encoded.
     * @return
     *      Base64 encoded string of the bitmap.
     */
    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Stores a QR code bitmap string in Firebase, associated with the event.
     *
     * @param qrCodeBitmap
     *      The bitmap string of the QR code to store.
     */
    private void storeQRCodeInFirestore(Bitmap qrCodeBitmap) {
        String qrCodeString = bitmapToString(qrCodeBitmap);
        db.collection("events").document(event.getEventId())
                .update("qrCode", qrCodeString)
                .addOnSuccessListener(aVoid -> Toast.makeText(GenerateQrCodeActivity.this, "QR Code stored successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(GenerateQrCodeActivity.this, "Failed to store QR Code", Toast.LENGTH_SHORT).show());
    }

    private void storePromoQRCodeInFirestore(Bitmap qrCodeBitmap) {
        String qrCodeString = bitmapToString(qrCodeBitmap);
        db.collection("events").document(event.getEventId())
                .update("promoQrCode", qrCodeString)
                .addOnSuccessListener(aVoid -> Toast.makeText(GenerateQrCodeActivity.this, "QR Code stored successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(GenerateQrCodeActivity.this, "Failed to store QR Code", Toast.LENGTH_SHORT).show());
    }

    /**
     * Navigates back to the CreateEventActivity.
     */
    private void navigateBackToCreateEvent() {
        Intent intent = new Intent(GenerateQrCodeActivity.this, OrganizerPageActivity.class);
        startActivity(intent);
    }

    private Uri bitmapToUri(Bitmap bitmap) {
        // Creating a file
        File cachePath = new File(getExternalCacheDir(), "shared_qr_code.png");
        try {
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the URI using FileProvider
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cachePath);
    }

    private void shareQRCode(Bitmap bitmap) {
        Uri qrCodeUri = bitmapToUri(bitmap);

        // Creating a share intent
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, qrCodeUri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
    }


    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void fetchAndShareQRCode(String eventId, String qrCode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String qrCodeString = documentSnapshot.getString(qrCode);
                        if (qrCodeString != null && !qrCodeString.isEmpty()) {
                            Bitmap qrCodeBitmap = stringToBitmap(qrCodeString);
                            shareQRCode(qrCodeBitmap);
                        } else {
                            Toast.makeText(GenerateQrCodeActivity.this, "QR Code not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GenerateQrCodeActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(GenerateQrCodeActivity.this, "Failed to fetch QR Code.", Toast.LENGTH_SHORT).show());
    }

}

