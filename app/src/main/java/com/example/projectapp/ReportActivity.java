package com.example.projectapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class ReportActivity extends AppCompatActivity {

    Button btn;
    ConstraintLayout container;

    String[] required_permissions = new String[]{
            android.Manifest.permission.READ_MEDIA_IMAGES
    };

    boolean is_storage_image_permitted = false;

    String TAG = "Report";
    String eventId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String eventName;
    String organizer;
    String eventDescription;
    String adate;
    Integer diff1 = 0;
    Integer diff2 = 0;
    Integer numUnionItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_layout);
        eventId = (String) getIntent().getSerializableExtra("eventId");
        btn = (Button) findViewById(R.id.btn);
        container = (ConstraintLayout) findViewById(R.id.container);


        if (!is_storage_image_permitted) {
            requestPermissionStorageImages();
        }

        getInfo();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPDFandShare();
                onBackPressed();
            }
        });

    }

    public void createPDF(OutputStream ref_outst) {
        // Standard margins for A4 size paper (1 inch = 72 points)
        int marginLeft = 72;
        int marginTop = 72;
        int marginRight = 72;
        int marginBottom = 72;

        // create a new document
        PdfDocument document = new PdfDocument();

        // create a page description with margins
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawColor(Color.WHITE);

        TextPaint titlePaint = new TextPaint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(30); // Change the text size for the title

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(12);

        String diff1_text = "The number of people who signed up, but didn't check-in: ";
        String diff2_text = "The number of people who checked-in, but didn't sign up: ";

        String eventReportTitle = "Event Report\n";
        String eventInfo = "\nEvent Name: " + eventName +
                "\nOrganizer: " + organizer +
                "\nDate: " + adate +
                "\n" + diff1_text + diff1 +
                "\n" + diff2_text + diff2 +
                "\nEvent Description(Limited at 3000 characters): " + eventDescription;

        // Create StaticLayout for the title
        StaticLayout eventReportTitleLayout = new StaticLayout(eventReportTitle, titlePaint, 595 - marginLeft - marginRight,
                Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        // Calculate space needed for event info and image
        StaticLayout eventInfoLayout = new StaticLayout(eventInfo, textPaint, 595 - marginLeft - marginRight,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        // Draw StaticLayouts on canvas with margins
        canvas.save();
        canvas.translate(marginLeft, marginTop); // Set margin
        float titleY = 0;
        eventReportTitleLayout.draw(canvas);
        titleY += eventReportTitleLayout.getHeight();
        canvas.translate(0, titleY + 20); // Add space between title and info
        eventInfoLayout.draw(canvas);
        canvas.restore();

        // finish the page
        document.finishPage(page);

        try {
            // write the document content
            document.writeTo(ref_outst);

            // close the document
            document.close();

            Toast.makeText(this, "PDF is created, but not saved anywhere yet", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPDFandShare() {
        try {
            // Create a temporary file
            File tempFile = File.createTempFile("temp_event_report", ".pdf", getCacheDir());
            OutputStream outst = new FileOutputStream(tempFile);

            // Code to create PDF
            createPDF(outst);

            // Close the output stream
            outst.close();

            // Get the content URI using FileProvider
            Uri tempFileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", tempFile);

            // Share the file using the content URI
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, tempFileUri);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission to receiving app
            startActivity(Intent.createChooser(share, "Share PDF"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestPermissionStorageImages() {
        if (ContextCompat.checkSelfPermission(this, required_permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, required_permissions[0] + " Granted");
            is_storage_image_permitted = true;
        } else {
            //new android 13 code after onActivityResult is deprecated, now ActivityResultLauncher..
            request_permission_launcher_storage_images.launch(required_permissions[0]);
        }

    }

    private ActivityResultLauncher<String> request_permission_launcher_storage_images =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Log.d(TAG, required_permissions[0] + " Granted");
                            is_storage_image_permitted = true;

                        } else {
                            Log.d(TAG, required_permissions[0] + " Not Granted");
                            is_storage_image_permitted = false;
                        }
                    });

    public void getInfo(){
        // Define the Type for ArrayList<String>
        //Type listType = new TypeToken<ArrayList<String>>() {}.getType();

        db.collection("events")
                .document(eventId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve values from the document
                                eventName = document.getString("name");
                                organizer = document.getString("organizerName");
                                adate = document.getString("date");
                                eventDescription = document.getString("description");
                                if (eventDescription != null){
                                    eventDescription= eventDescription.substring(0, Math.min(3000, eventDescription.length()));
                                };

                                // Extract ArrayList<String> values from the map
                                Map<String, Object> data = document.getData();
                                ArrayList<String> checkins = new ArrayList<>();
                                if (data.containsKey("checkedAttendees")) {
                                    Object checkedAttendeesObject = data.get("checkedAttendees");
                                    if (checkedAttendeesObject instanceof HashMap<?, ?>) {
                                        HashMap<String, Integer> checkedAttendeesMap = (HashMap<String, Integer>) checkedAttendeesObject;
                                        checkins.addAll(checkedAttendeesMap.keySet());
                                    }
                                }

                                ArrayList<String> signups = new ArrayList<>();
                                if (data.containsKey("signedAttendees")) {
                                    Object signedAttendeesObject = data.get("signedAttendees");
                                    if (signedAttendeesObject instanceof ArrayList<?>) {
                                        signups.addAll((ArrayList<String>) signedAttendeesObject);
                                    }
                                }

                                // Find common items
                                Set<String> commonItems = new HashSet<>(checkins);
                                commonItems.retainAll(signups);
                                Integer numCommonItems = commonItems.size();

                                // Find union of the lists
                                Set<String> unionItems = new HashSet<>(checkins);
                                unionItems.addAll(signups);
                                numUnionItems = unionItems.size();

                                if (numCommonItems>=0 && numCommonItems<signups.size()){
                                    diff1 = signups.size() - numCommonItems;
                                }
                                if (numCommonItems>=0 && numCommonItems<checkins.size()){
                                    diff2 = checkins.size() - numCommonItems;
                                }

                                // Now you can use these values as needed
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}