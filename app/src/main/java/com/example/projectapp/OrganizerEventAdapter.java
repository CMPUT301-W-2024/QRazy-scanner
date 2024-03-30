package com.example.projectapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.google.auth.oauth2.GoogleCredentials;

/**
 * Adapter for displaying an organizer's Events in a RecyclerView. Handles
 * event details, showing attendance counts, and providing expandable sections to
 * display QR codes.
 */
public class OrganizerEventAdapter extends RecyclerView.Adapter<OrganizerEventAdapter.ViewHolder> {
    private List<Event> events;
    private Context context;

    public OrganizerEventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, attendeeCountTextView, eventDetailTextView, evenDateTimeTextView;
        LinearLayout expandEventLayout;
        ImageView eventQrView;
        ImageButton expandEventButton, announcementButton;
        Button viewmapButton;

        public ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameOrgText);
            attendeeCountTextView = itemView.findViewById(R.id.attendeeCountTextView);
            eventDetailTextView = itemView.findViewById(R.id.eventDetailOrgText);
            evenDateTimeTextView = itemView.findViewById(R.id.eventDateTimeOrgText);
            expandEventLayout = itemView.findViewById(R.id.expandEventLayout);
            eventQrView = itemView.findViewById(R.id.eventQrView);
            expandEventButton = itemView.findViewById(R.id.expandButton);
            announcementButton = itemView.findViewById(R.id.announcementButton);
            viewmapButton = itemView.findViewById(R.id.viewMapButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventNameTextView.setText(event.getName());
        holder.attendeeCountTextView.setText("Live Attendee Count: " + String.valueOf(event.getAttendance()));
        holder.eventDetailTextView.setText(event.getDescription());
        holder.evenDateTimeTextView.setText(event.getDate() + " at " + event.getTime());

        if (event.getQrCode() != null){
            Bitmap bitmap = stringToBitmap(event.getQrCode());
            holder.eventQrView.setImageBitmap(bitmap);
        }

        holder.attendeeCountTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventAttendeesActvity.class);
            intent.putExtra("EVENT", event);
            context.startActivity(intent);
        });

        holder.expandEventButton.setOnClickListener(v -> {
            View view = holder.expandEventLayout;
            int visibility = (view.getVisibility() == View.GONE) ? View.VISIBLE: View.GONE;
            view.setVisibility(visibility);
        });

        holder.announcementButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Announcement");

            EditText input = new EditText(context);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        requestAPI(event, input.getText().toString());
                        event.addAnnouncements(input.getText().toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        });

        holder.viewmapButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra("eventId", event.getEventId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    /**
     * Function to convert a Base64 encoded string to a Bitmap.
     * @param encodedString The Base64 encoded string representation of an image.
     * @return The Bitmap image or null if an error occurs.
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

    private void requestAPI(Event event, String announcement) throws Exception{
        String projectId = "qrazy-scanner";
        String topic = event.getEventId();

        String url = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
        String payload = "{\"message\": {\"topic\": \"" + topic + "\", \"data\": {\"event\": \""+ event.getName() +"\", \"announcement\": \"" + announcement + "\"}}}";
        byte[] output = payload.getBytes(StandardCharsets.UTF_8);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setFixedLengthStreamingMode(output.length);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; UTF-8");
        con.setRequestProperty("Authorization", "Bearer " + getAccessToken());

        try (OutputStream os = con.getOutputStream()) {
            os.write(output);
        }

        con.getResponseCode();
    }

    private static String getAccessToken() throws IOException {

        String[] SCOPES = {"https://www.googleapis.com/auth/firebase.messaging"};
        String serviceAccount = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"qrazy-scanner\",\n" +
                "  \"private_key_id\": \"204fcee81c3441fefd193251b9e1d1e88149b826\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDkqx3U6J3btsY\\nX3pnzqz4h39TduDs1cHvFh6l0Ut+2hMHrsDl2mS/wh1FRrG9gfwG7zCVBIbsMBjy\\nVZj7SPQZpeq/X8CKz3EcUW2EqqhRyhMNxwZaPOJd0WH4XXsV4TPLKHvvmqzpIcRs\\naGkm1NKMzpthFnrbI0EAgn6WpJCI50/xKnOjgdgxA210BKf0vT10cNd7Pf3jwfiE\\nntwgvFTUTrbGPH/OuEKNfUHY7GbHia1eaqy2VMlpKi1GDZ4w1IqMCvOjJS1zYaMH\\nRBJGAZjpmET8mJ0X+q2CpI7snq16SHnkObuVALHLfdd75OlLD+Eg9JGCIuilWsdl\\nHSyq3Wm7AgMBAAECggEAAvf/g+IySv4qTOuUx+2fD44S/zkvPzkcHrJ8bZzzOJjm\\nhWKAOB0BaYMbVE63P6lbojP6+KV0cjJlYTcDwaYWWoCmbcLuK81BVMzhojbATlmI\\nbNhIFDzsshWNwf+Sm5RAZ9rp+Ar7iUsYww0vXMZfRIGVtzq9oQT0etBVoUNx6Y9p\\nNGjXNdIlNbinyny5Myd92bQ7HtqN5K7qd4OYsZcgMgp0Xklm9eMZg9hyFwWv7r5o\\nhXSpyirwiCZ26WqNPOhzSuBY5L1uicX12E0RYbzF9drO18tgBvHZR4BQe1k4VycB\\nItvGO/5ouG7bdMl0zwqRLahx2mL4dBW/rUmlbmG45QKBgQDnQglVS/mXBMRG7VYj\\nUb9Apxw0LBGjHwfe902/4jFnF+ViweXlauxQM5AWDCD3WcpUUiDE7esfX0Aqv5CO\\nfwvf8oMIzMzL5roQGI21GgmZUaUKHTCEhbn+bEYfdYH4qfq57p6QOPzV7IaQdZzq\\ncuXlFOvT/XKaYhJjhOFkiT4JbwKBgQDYf0EgDwudB+l/3ldzIHoP4UCyeE0OwJKw\\nlGCKHmxGmbgYKysX+lyiSwLum1DqR0ZVseAqY5D7+Z4U6YbizQMoQ5b8yPdW0Uaz\\nrLrR9rRbCpBzSYq1t6PpoCUOJx3Qz0V741vXqIig8LOg1pys17ljpmZyw7yn4o+E\\nazGwiR2GdQKBgQC/8CVC8E31s/UcUTwfEGhGRuy3uKPi2Yx02JllW11ZjZHLh9dB\\ntJ7yafl68xIhehreJVQhXr65SRs+38QhIP1AIE31bdXEnnlrhpWG7FdvMz5hyJxO\\nQZd/vWnuDl+TfbElxRFB7qqa+zcsixFz3W1F1zlst3z4+dD9XHqeMPKWbQKBgH+v\\nLb22oebPT8t2WqUvtk2/T+TyRqA4u0shd35+SuWoq4a1jwjpQ9ED5IrNV3+U4cqQ\\nyeC2MEAsDCvRPxhsSTxqAJa+AAJYExbM/LHwipZXOLKF4SUjVazoInKiZ1dLp3NV\\nuEkMwOgKjiaB7I2T/WbkMO/muVFascIrZnbzp1IxAoGADKodaq7Wi9gWZde6MY6S\\nT/R8pCDYoyQ0p7YUFz05ZzZrjfJ6GWi0N0e/znjJElYPswJYlNLcsSUR7INGJEHB\\npPhdh6pJrL13NMboCUPdHa1KRMq9GcH0TTrYiuHwssq2V3OYifhNsR5G2/70/XSx\\nFnUiD9n4lg5/2Roalmk5+oo=\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"firebase-adminsdk-cfln6@qrazy-scanner.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"115043820653373096359\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-cfln6%40qrazy-scanner.iam.gserviceaccount.com\",\n" +
                "  \"universe_domain\": \"googleapis.com\"\n" +
                "}";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(serviceAccount.getBytes()))
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }
}
