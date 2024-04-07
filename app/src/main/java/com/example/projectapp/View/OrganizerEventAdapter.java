package com.example.projectapp.View;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.Controller.GetAttendeeCallback;
import com.example.projectapp.Model.Announcement;
import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.R;
import com.example.projectapp.Controller.UpdateEventCallback;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adapter for displaying an organizer's Events in a RecyclerView. Handles
 * event details, showing attendance counts, and providing expandable sections to
 * display QR codes.
 */
public class OrganizerEventAdapter extends RecyclerView.Adapter<OrganizerEventAdapter.ViewHolder> implements UpdateEventCallback{
    private List<Event> events;
    private Context context;

    public OrganizerEventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, attendeeCountTextView, eventDetailTextView, eventDateTextView, eventTimeTextView;
        LinearLayout expandEventLayout;
        ImageView eventQrView, promoQrView;
        TextView eventQrText, promoQrText;
        ImageButton expandEventButton, announcementButton;
        Button viewmapButton;
        Button pdfButton;

        public ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameOrgText);
            attendeeCountTextView = itemView.findViewById(R.id.attendeeCountTextView);
            eventDetailTextView = itemView.findViewById(R.id.eventDetailOrgText);
            eventDateTextView = itemView.findViewById(R.id.eventDateOrgText);
            eventTimeTextView = itemView.findViewById(R.id.eventTimeOrgText);
            expandEventLayout = itemView.findViewById(R.id.expandEventLayout);
            eventQrView = itemView.findViewById(R.id.eventQrView);
            promoQrView = itemView.findViewById(R.id.promoQrView);
            eventQrText = itemView.findViewById(R.id.eventQrText);
            promoQrText = itemView.findViewById(R.id.promoQrText);
            expandEventButton = itemView.findViewById(R.id.expandButton);
            announcementButton = itemView.findViewById(R.id.announcementButton);
            pdfButton = itemView.findViewById(R.id.pdfButton);
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
        holder.eventDateTextView.setText(event.getDate());
        holder.eventTimeTextView.setText(event.getStartTime() + " - " +event.getEndTime());

        if (event.getQrCode() != null){
            Bitmap bitmap = stringToBitmap(event.getQrCode());
            if (bitmap != null){
                holder.eventQrView.setImageBitmap(bitmap);
                holder.eventQrView.setVisibility(View.VISIBLE);
                holder.eventQrText.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.eventQrView.setVisibility(View.GONE);
            holder.eventQrText.setVisibility(View.GONE);
        }

        if (event.getPromoQrCode() != null){
            Bitmap bitmap = stringToBitmap(event.getPromoQrCode());
            holder.promoQrView.setImageBitmap(bitmap);
            holder.promoQrView.setVisibility(View.VISIBLE);
            holder.promoQrText.setVisibility(View.VISIBLE);
        }
        else {
            holder.promoQrView.setVisibility(View.GONE);
            holder.promoQrText.setVisibility(View.GONE);
        }

        holder.attendeeCountTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventAttendeesActivity.class);
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
                    DataHandler dataHandler = DataHandler.getInstance();
                    // gets set of all checked and signed up attendees
                    Set<String> unionAttendees = new HashSet<>(event.getCheckedAttendees().keySet());
                    unionAttendees.addAll(event.getSignedAttendees());

                    for (String attendeeId : unionAttendees){
                        dataHandler.getAttendee(attendeeId, new GetAttendeeCallback() {
                            @Override
                            public void onGetAttendee(Attendee attendee, boolean deleted) {
                                try {
                                    dataHandler.sendNotification(attendee.getFcmToken() ,event.getName(), input.getText().toString());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    Date currentDateTime = new Date();
                    Announcement announcement = new Announcement(input.getText().toString(), sdf.format(currentDateTime), event.getName(), event.getOrganizerName());
                    dataHandler.updateEvent(event.getEventId(), "announcements", FieldValue.arrayUnion(announcement), OrganizerEventAdapter.this);
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
            intent.putExtra("EVENT_ID", event.getEventId());
            context.startActivity(intent);
        });
        holder.pdfButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportActivity.class);
            intent.putExtra("EVENT", event);
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

    @Override
    public void onUpdateEvent(String eventId) {
        if (eventId != null){
            Toast.makeText(context, "Announcement made", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Error accessing firebase", Toast.LENGTH_SHORT).show();
        }
    }
}
