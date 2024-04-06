package com.example.projectapp.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.Model.Attendee;
import com.example.projectapp.Model.Event;
import com.example.projectapp.R;

import java.util.List;

/**
 * Adapter responsible for displaying a list of Attendees associated with a specific Event.
 * Handles populating Attendee data into the RecyclerView items and displays check-in
 * information related to the Event.
 */
public class EventAttendeeAdapter extends RecyclerView.Adapter<EventAttendeeAdapter.ViewHolder>{

    private List<Attendee> attendees;
    private Event event;
    private EventAttendeeAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Attendee attendee);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, emailText, phoneText, checkInText;

        public ViewHolder(View view) {
            super(view);
            nameText = (TextView) view.findViewById(R.id.attendeeNameText);
            emailText = (TextView) view.findViewById(R.id.attendeeEmailText);
            phoneText = (TextView) view.findViewById(R.id.attendeePhoneText);
            checkInText = (TextView) view.findViewById(R.id.attendeeCheckInText);
        }

        public void bind(final Attendee attendee,final EventAttendeeAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (listener != null){
                        listener.onItemClick(attendee);
                    }
                }
            });
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param attendees list containing the data to populate views to be used
     */
    public EventAttendeeAdapter(List<Attendee> attendees, Event event, OnItemClickListener listener) {
        this.attendees = attendees;
        this.event = event;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendee_widget, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventAttendeeAdapter.ViewHolder viewHolder, final int position) {
        Attendee attendee = attendees.get(position);
        viewHolder.bind(attendee, listener);

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nameText.setText(attendee.getName());
        viewHolder.emailText.setText("Homepage: " + attendee.getHomepage());
        viewHolder.phoneText.setText("Contact Info: " + attendee.getContactInfo());
        viewHolder.checkInText.setVisibility(View.GONE);

        if (event != null){
            viewHolder.checkInText.setVisibility(View.VISIBLE);
            Integer checkIns = attendee.getCheckedInEvents().get(event.getEventId());
            if (checkIns != null){
                viewHolder.checkInText.setText("Check Ins: " + checkIns.toString());
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return attendees.size();
    }
}
