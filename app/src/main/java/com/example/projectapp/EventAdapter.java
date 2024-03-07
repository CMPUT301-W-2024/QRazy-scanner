package com.example.projectapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, organizerText, infoText;

        public ViewHolder(View view) {
            super(view);
            nameText = (TextView) view.findViewById(R.id.scroll_event_name_text);
            organizerText = (TextView) view.findViewById(R.id.scroll_organizer_name_text);
            infoText = (TextView) view.findViewById(R.id.scroll_info_text);
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param events list containing the data to populate views to be used
     */
    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_scroll_view, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nameText.setText(events.get(position).getName());
        viewHolder.organizerText.setText(events.get(position).getOrganizer());
        viewHolder.infoText.setText(events.get(position).getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return events.size();
    }
}
