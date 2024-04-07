package com.example.projectapp.View;

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
import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Controller.GetEventCallback;
import com.example.projectapp.Controller.GetQrCodeEventCallback;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.R;
import com.example.projectapp.Controller.UpdateAttendeeCallback;
import com.example.projectapp.Controller.UpdateEventCallback;
import com.google.firebase.firestore.FieldValue;
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

    /**
     * Sets up the activity with a QR code scanner and handles QR code processing.
     * Depending on the 'usage' intent extra, it checks in attendees, reuses QR codes, or processes promo QRs.
     *
     * @param savedInstanceState
     *      Contains data supplied in onSaveInstanceState(Bundle)
     *      if activity is re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        checkPermission();
        CodeScannerView scannerView = findViewById(R.id.scannerView);
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

    /**
     * Resumes the camera preview when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    /**
     * Releases camera resources and pauses the camera preview when the activity is paused.
     */
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    /**
     * Checks in the attendee for an event and sends for handling geolocation capture.
     *
     * @param event
     *      The event to check in for.
     */
    private void checkIn(Event event){
        Attendee attendee = dataHandler.getLocalAttendee();

        // gets set of all checked and signed up attendees
        Set<String> unionAttendees = new HashSet<>(event.getCheckedAttendees().keySet());
        unionAttendees.addAll(event.getSignedAttendees());

        // if no attendance limit or signed attendees is less than limit then check in
        if (event.getAttendanceLimit() == 0 || unionAttendees.contains(attendee.getAttendeeId()) || unionAttendees.size() < event.getAttendanceLimit()){

            dataHandler.updateEvent(event.getEventId(), "checkedAttendees." + attendee.getAttendeeId(), FieldValue.increment(1), this);

            dataHandler.updateAttendee(attendee.getAttendeeId(), "checkedInEvents." + event.getEventId(), FieldValue.increment(1), this);

            //run if organizer wants location for this event
            if (event.getTrackLocation()) {
                Intent intent1 = new Intent(ScanActivity.this, GeopointDialog.class);
                intent1.putExtra("eventId", event.getEventId());
                startActivity(intent1);
            }
            finish();
        }
        else {
            Toast.makeText(ScanActivity.this, "Event has reached attendance limit", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Processes the QR code for event check-in or updates the event with the QR code data.
     *
     * @param event     The event associated with the QR code.
     * @param checkInto Flag indicating whether to check into the event.
     * @param qrData    The QR code data.
     */
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

    /**
     * Handles the retrieval of an event's details and navigates to the AttendeePageActivity.
     *
     * @param event
     *      The event to retrieve details for.
     */
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

    /**
     * Notifies the user of an error if updating the event in Firebase fails.
     *
     * @param eventId
     *      The ID of the event being updated.
     */
    @Override
    public void onUpdateEvent(String eventId) {
        if (eventId == null){
            Toast.makeText(this, "Error reaching firebase", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Notifies the user of an error if updating the attendee in Firebase fails.
     *
     * @param attendeeId
     *      The ID of the attendee being updated.
     */
    @Override
    public void onUpdateAttendee(String attendeeId) {
        if (attendeeId == null){
            Toast.makeText(this, "Error reaching firebase", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks for camera permission and requests it if not already granted.
     */
    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(ScanActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //permission is not granted yet
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    /**
     * Handles the result of the camera permission request.
     * Displays a toast message indicating whether the permission was granted or denied.
     *
     * @param requestCode  The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
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