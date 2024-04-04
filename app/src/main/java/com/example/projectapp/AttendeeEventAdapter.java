package com.example.projectapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for displaying a list of Events in a RecyclerView, specifically for an attendee's view.
 * Fetches organizer data from Firebase and handles clicks on Event items.
 */
public class AttendeeEventAdapter extends RecyclerView.Adapter<AttendeeEventAdapter.ViewHolder> {
    private List<Event> events;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    /**
     * View Holder class to represent individual Event items within the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, organizerText, infoText, dateText;

        public ViewHolder(View view) {
            super(view);
            nameText = (TextView) view.findViewById(R.id.eventNameText);
            organizerText = (TextView) view.findViewById(R.id.eventOrganizerNameText);
            infoText = (TextView) view.findViewById(R.id.eventInfoText);
            dateText = (TextView) view.findViewById(R.id.eventDateText);
        }

        public void bind(final Event event,final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(event);
                }
            });
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param events list containing the data to populate views to be used
     */
    public AttendeeEventAdapter(List<Event> events, OnItemClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_widget, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Event event = events.get(position);

        viewHolder.bind(event, listener);

        viewHolder.nameText.setText(event.getName());
        viewHolder.organizerText.setText(event.getOrganizerName());
        viewHolder.infoText.setText(event.getDescription());
        viewHolder.dateText.setText(event.getDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }
}
