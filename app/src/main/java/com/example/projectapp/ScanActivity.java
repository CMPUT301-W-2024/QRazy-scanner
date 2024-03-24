package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.Result;

/**
 * Handle Scanning QR Code
 */
public class ScanActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private final int CAMERA_PERMISSION_CODE = 100;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DataHandler dataHandler = DataHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        checkPermission();
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        db.collection("events").document(result.getText()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Event event = documentSnapshot.toObject(Event.class);
                                if (event != null){
                                    // if no attendance limit or signed attendees is less than limit then sign up
                                    if (event.getAttendanceLimit() == 0 || event.getSignedAttendees().contains(dataHandler.getAttendee().getAttendeeId()) || event.getAttendance() + event.getSignedAttendees().size() < event.getAttendanceLimit()){
                                        event.addCheckedAttendee(dataHandler.getAttendee().getAttendeeId());
                                        dataHandler.getAttendee().addCheckedEvent(event.getEventId());
                                        FirebaseMessaging.getInstance().subscribeToTopic(event.getEventId());
                                        Intent intent = new Intent(ScanActivity.this, AttendeePageActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(ScanActivity.this, "Event has reached attendance limit", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(ScanActivity.this, "Could not get event", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    // Gets permission for camera if needed

    /**
     * Check for camera's permission
     */
    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(ScanActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //permission is not granted yet
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    // What to do if permission granted or not granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ScanActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(ScanActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
    }
}