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
import com.example.projectapp.ImageHandler;
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
        Button viewMapButton, pdfButton, generateQrButton;

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
            viewMapButton = itemView.findViewById(R.id.viewMapButton);
            generateQrButton = itemView.findViewById(R.id.eventAdapterQrButton);
        }
    }

    /**
     * Called by the RecyclerView to create a new ViewHolder.
     *
     * @param parent   The parent ViewGroup of the new ViewHolder.
     * @param viewType The type of the new View.
     * @return         A new ViewHolder for displaying an Event item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by the RecyclerView to populate event data into a ViewHolder.
     * Sets up click listeners and handles visibility of QR codes and expandable sections.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the Event within the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventNameTextView.setText(event.getName());
        holder.attendeeCountTextView.setText("Live Attendee Count: " + String.valueOf(event.getAttendance()));
        holder.eventDetailTextView.setText(event.getDescription());
        holder.eventDateTextView.setText(event.getDate());
        holder.eventTimeTextView.setText(event.getStartTime() + " - " +event.getEndTime());

        if (event.getQrCode() != null){
            Bitmap bitmap = ImageHandler.getInstance().stringToBitmap(event.getQrCode());
            if (bitmap != null){
                holder.eventQrView.setImageBitmap(bitmap);
                holder.eventQrView.setVisibility(View.VISIBLE);
                holder.eventQrText.setVisibility(View.VISIBLE);
            }
            else {
                holder.eventQrView.setVisibility(View.GONE);
                holder.eventQrText.setVisibility(View.GONE);
            }
        }
        else {
            holder.eventQrView.setVisibility(View.GONE);
            holder.eventQrText.setVisibility(View.GONE);
        }

        if (event.getPromoQrCode() != null){
            Bitmap bitmap = ImageHandler.getInstance().stringToBitmap(event.getPromoQrCode());
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
                    sendAnnouncement(input.getText().toString(), event);
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


        holder.generateQrButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, GenerateQrCodeActivity.class);
            intent.putExtra("EVENT_ID", event.getEventId());
            context.startActivity(intent);
        });

        if (event.getTrackLocation() != null){
            holder.viewMapButton.setVisibility(event.getTrackLocation() ? View.VISIBLE : View.GONE);
        }

        holder.viewMapButton.setOnClickListener(v -> {
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

    /**
     * Returns the total number of Event items to be displayed.
     *
     * @return The size of the events list.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     *  Handles sending announcements to attendees of a given event.
     *
     * @param message The text content of the announcement.
     * @param event   The Event for which the announcement is being sent.
     */
    private void sendAnnouncement(String message, Event event){
        DataHandler dataHandler = DataHandler.getInstance();
        // gets set of all checked and signed up attendees
        Set<String> unionAttendees = new HashSet<>(event.getCheckedAttendees().keySet());
        unionAttendees.addAll(event.getSignedAttendees());

        for (String attendeeId : unionAttendees){
            dataHandler.getAttendee(attendeeId, new GetAttendeeCallback() {
                @Override
                public void onGetAttendee(Attendee attendee, boolean deleted) {
                    try {
                        dataHandler.sendNotification(attendee.getFcmToken() ,event.getName(), message);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date currentDateTime = new Date();
        Announcement announcement = new Announcement(message, sdf.format(currentDateTime), event.getName(), event.getOrganizerName());
        dataHandler.updateEvent(event.getEventId(), "announcements", FieldValue.arrayUnion(announcement), OrganizerEventAdapter.this);
    }


    /**
     * Callback triggered after an Event update attempt. Displays feedback to the user.
     *
     * @param eventId The ID of the Event that was updated (or null if failure).
     */
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
