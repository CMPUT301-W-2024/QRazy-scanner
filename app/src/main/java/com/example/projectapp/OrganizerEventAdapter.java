package com.example.projectapp;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrganizerEventAdapter extends RecyclerView.Adapter<OrganizerEventAdapter.ViewHolder> {
    private List<Event> events;

    public OrganizerEventAdapter(List<Event> events) {
        this.events = events;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, attendeeCountTextView, eventDetailTextView, evenDateTextView;
        LinearLayout expandEventLayout;
        ImageButton expandEventButton;

        public ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            attendeeCountTextView = itemView.findViewById(R.id.attendeeCountTextView);
            eventDetailTextView = itemView.findViewById(R.id.eventDetailTextView);
            evenDateTextView = itemView.findViewById(R.id.eventDateTextView);
            expandEventLayout = itemView.findViewById(R.id.expandEventLayout);
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
        holder.attendeeCountTextView.setText("Attendee Count: " + String.valueOf(event.getAttendance())); // or any other field representing the count
        holder.eventDetailTextView.setText(event.getDescription());
        holder.evenDateTextView.setText(event.getDate());
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


    public interface OnItemClickListener {
        void onItemClick(Event event);
    }
}
