package com.example.projectapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class GenerateQrCodeActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private String eventId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        eventId = getIntent().getStringExtra("EVENT_ID"); // Retrieve the event ID

        MaterialButton generateQrCodeButton = findViewById(R.id.generateQrCodeButton); // Find the button by its ID
        generateQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventId != null && !eventId.isEmpty()) {
                    // A valid event ID is present, we can generate the QR code
                    try {
                        Bitmap qrCodeBitmap = generateQRCode(eventId);
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
    }

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
    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void storeQRCodeInFirestore(Bitmap qrCodeBitmap) {
        String qrCodeString = bitmapToString(qrCodeBitmap);
        db.collection("events").document(eventId)
                .update("qrCode", qrCodeString)
                .addOnSuccessListener(aVoid -> Toast.makeText(GenerateQrCodeActivity.this, "QR Code stored successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(GenerateQrCodeActivity.this, "Failed to store QR Code", Toast.LENGTH_SHORT).show());
    }
}

