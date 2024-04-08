package com.example.projectapp.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.Model.Event;
import com.example.projectapp.R;

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

    /**
     * Called by the RecyclerView when a new ViewHolder needs to be created to display an event item.
     *
     * @param viewGroup  The parent ViewGroup that the new ViewHolder will be attached to.
     * @param viewType   An integer code used for potential view type differentiation (not used here).
     * @return           A new ViewHolder instance to hold the view hierarchy for an event item.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_widget, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Called by the RecyclerView to populate the data of a specific event item into the ViewHolder.
     *
     * @param viewHolder The ViewHolder whose views should be updated.
     * @param position   The index of the event within the list to be displayed.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Event event = events.get(position);

        viewHolder.bind(event, listener);

        viewHolder.nameText.setText(event.getName());
        viewHolder.organizerText.setText(event.getOrganizerName());
        viewHolder.infoText.setText(event.getDescription());
        viewHolder.dateText.setText(event.getDate());
    }

    /**
     * Returns the total number of events in the dataset to be displayed by the RecyclerView.
     *
     * @return The size of the events list.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }
}
