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
import com.google.zxing.Result;

import java.util.HashSet;
import java.util.Set;

/**
 * Handle Scanning QR Code
 */
public class ScanActivity extends AppCompatActivity implements GetEventCallback, GetQrCodeEventCallback, UpdateEventCallback, UpdateAttendeeCallback {

    private CodeScanner mCodeScanner;
    private final int CAMERA_PERMISSION_CODE = 100;
    private final DataHandler dataHandler = DataHandler.getInstance();

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
                        String qrData = result.getText();
                        String usage = getIntent().getStringExtra("usage");
                        // attendee scanning to check in
                        if (usage == null){
                            dataHandler.getQrCodeEvent(qrData, true, ScanActivity.this);
                        }
                        // organizer scanning to reuse existing code
                        else if (usage.equals("reuseQr")){
                            dataHandler.getQrCodeEvent(qrData, false, ScanActivity.this);
                        }
                        // attendee scanning promo qr code
                        else if (usage.equals("promoQr")){
                            String eventId = qrData.substring("Promo".length()); // Extract event ID
                            dataHandler.getEvent(eventId, ScanActivity.this);
                        }

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


    private void checkIn(Event event){
        Attendee attendee = dataHandler.getLocalAttendee();

        // gets set of all checked and signed up attendees
        Set<String> unionAttendees = new HashSet<>(event.getCheckedAttendees().keySet());
        unionAttendees.addAll(event.getSignedAttendees());

        // if no attendance limit or signed attendees is less than limit then check in
        if (event.getAttendanceLimit() == 0 || event.getSignedAttendees().contains(attendee.getAttendeeId()) || unionAttendees.size() < event.getAttendanceLimit()){

            event.addCheckedAttendee(attendee.getAttendeeId());
            dataHandler.updateEvent(event.getEventId(), "checkedAttendees", event.getCheckedAttendees(), this);

            attendee.addCheckedEvent(event.getEventId());
            dataHandler.updateAttendee(attendee.getAttendeeId(), "checkedInEvents", attendee.getCheckedInEvents(), this);

            dataHandler.subscribeToNotis(event.getEventId());

            Intent intent1 = new Intent(ScanActivity.this, GeopointDialog.class);
            intent1.putExtra("eventId", event.getEventId());
            startActivity(intent1);
            finish();
        }
        else {
            Toast.makeText(ScanActivity.this, "Event has reached attendance limit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetQrCodeEvent(Event event, boolean checkInto, String qrData) {
        if (event == null){
            if (checkInto){
                Toast.makeText(ScanActivity.this, "Qr Code is not associated to an event", Toast.LENGTH_SHORT) .show();
            }
            else {
                String eventId = getIntent().getStringExtra("EVENT_ID");
                dataHandler.updateEvent(eventId, "qrCode", qrData, this);
            }
        }
        else {
            if (checkInto){
                checkIn(event);
            }
            else {
                Toast.makeText(ScanActivity.this, "Qr Code is already in use", Toast.LENGTH_SHORT) .show();
            }
        }
    }

    @Override
    public void onGetEvent(Event event) {
        if (event != null){
            Intent intent = new Intent(ScanActivity.this, AttendeePageActivity.class);
            intent.putExtra("EVENT", event);
            setResult(1, intent);
            finish();
        }
        else {
            Toast.makeText(this, "Error fetching event details", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateEvent(String eventId) {
        if (eventId == null){
            Toast.makeText(this, "Error reaching firebase", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateAttendee(String attendeeId) {
        if (attendeeId == null){
            Toast.makeText(this, "Error reaching firebase", Toast.LENGTH_SHORT).show();
        }
    }

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