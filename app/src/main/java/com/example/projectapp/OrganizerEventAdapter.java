package com.example.projectapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.transition.TransitionManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        TextView eventNameTextView, attendeeCountTextView, eventDetailTextView, evenDateTextView;
        LinearLayout expandEventLayout;
        ImageView eventQrView;
        ImageButton expandEventButton;

        public ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            attendeeCountTextView = itemView.findViewById(R.id.attendeeCountTextView);
            eventDetailTextView = itemView.findViewById(R.id.eventDetailTextView);
            evenDateTextView = itemView.findViewById(R.id.eventDateTextView);
            expandEventLayout = itemView.findViewById(R.id.expandEventLayout);
            eventQrView = itemView.findViewById(R.id.eventQrView);
            expandEventButton = itemView.findViewById(R.id.expandButton);
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
        holder.attendeeCountTextView.setText("Live Attendee Count: " + String.valueOf(event.getAttendance())); // or any other field representing the count
        holder.eventDetailTextView.setText(event.getDescription());
        holder.evenDateTextView.setText(event.getDate());

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
}
