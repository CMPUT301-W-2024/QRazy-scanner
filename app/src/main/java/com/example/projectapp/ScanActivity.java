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
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Handle Scanning QR Code
 */
public class ScanActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private final int CAMERA_PERMISSION_CODE = 100;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            checkIfCodeExists(qrData, true);
                        }
                        // organizer scanning to reuse existing code
                        else if (usage.equals("reuseQr")){
                            checkIfCodeExists(qrData, false);
                        }

                        else if (usage.equals("promoQr")){
                            String eventId = qrData.substring("Promo".length()); // Extract event ID
                            Intent intent = new Intent(ScanActivity.this, AttendeePageActivity.class);
                            intent.putExtra("EVENT_ID", eventId);
                            startActivity(intent);
                            finish();
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

    /**
     * Checks event attendance limit and processes check-in.
     *
     * @param event
     *      The event to check.
     */
    private void checkLimit(Event event){
        // if no attendance limit or signed attendees is less than limit then check in
        if (event.getAttendanceLimit() == 0 || event.getSignedAttendees().contains(dataHandler.getAttendee().getAttendeeId()) || event.getAttendance() + event.getSignedAttendees().size() < event.getAttendanceLimit()){
            event.addCheckedAttendee(dataHandler.getAttendee().getAttendeeId());
            dataHandler.getAttendee().addCheckedEvent(event.getEventId());
            dataHandler.subscribeToNotis(event.getEventId());
            Intent intent = new Intent(ScanActivity.this, AttendeePageActivity.class);
            startActivity(intent);
            Intent intent1 = new Intent(ScanActivity.this, GeopointDialog.class);
            intent1.putExtra("eventId", event.getEventId());
            startActivity(intent1);
            finish();
        }
        else {
            Toast.makeText(ScanActivity.this, "Event has reached attendance limit", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Verifies if QR code exists and performs actions based on check-in status.
     *
     * @param qrData    The QR data to check.
     * @param checkInto Flag for check-in process.
     */
    private void checkIfCodeExists(String qrData, boolean checkInto){
        db.collection("events").where(Filter.or(Filter.equalTo("eventId", qrData), Filter.equalTo("qrCode", hashEventCode(qrData)))).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                // code data doesn't exist
                if (querySnapshot.isEmpty()){
                    // attendee is trying to check into event using wrong qrCode
                    if (checkInto){
                        Toast.makeText(ScanActivity.this, "Qr Code is not associated to an event", Toast.LENGTH_SHORT) .show();
                    }
                    // organizer is allowed use qr code for their event
                    else{
                        String eventId = getIntent().getStringExtra("EVENT_ID");
                        setEventQrCode(eventId, qrData);
                        finish();
                    }
                }
                else{
                    List<Event> events = querySnapshot.toObjects(Event.class); // should only be one
                    if (checkInto){
                        checkLimit(events.get(0));
                    }
                    else{
                        Toast.makeText(ScanActivity.this, "Qr Code is already in use", Toast.LENGTH_SHORT) .show();
                    }
                }
            }
            else{
                Toast.makeText(ScanActivity.this, "Couldn't reach firebase", Toast.LENGTH_SHORT) .show();
            }
        });
    }

    /**
     * Sets the hashed QR code for an event in the database.
     *
     * @param eventId The ID of the event.
     * @param qrData  The QR data to hash and set.
     */
    private void setEventQrCode(String eventId, String qrData){
        qrData = hashEventCode(qrData);
        db.collection("events").document(eventId).update("qrCode", qrData);
    }

    /**
     * Hashes a string using SHA-256.
     *
     * @param code
     *      The string to hash.
     * @return
     *      The hashed string.
     */
    private String hashEventCode(String code){
        return Hashing.sha256()
                .hashString(code, StandardCharsets.UTF_8)
                .toString();
    }
}