package com.example.projectapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAttendeeAdapter extends RecyclerView.Adapter<EventAttendeeAdapter.ViewHolder>{

    private List<Attendee> attendees;
    private Event event;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, emailText, phoneText, checkInText;

        public ViewHolder(View view) {
            super(view);
            nameText = (TextView) view.findViewById(R.id.attendeeNameText);
            emailText = (TextView) view.findViewById(R.id.attendeeEmailText);
            phoneText = (TextView) view.findViewById(R.id.attendeePhoneText);
            checkInText = (TextView) view.findViewById(R.id.attendeeCheckInText);
        }

    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param attendees list containing the data to populate views to be used
     */
    public EventAttendeeAdapter(List<Attendee> attendees, Event event) {
        this.attendees = attendees;
        this.event = event;
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
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nameText.setText(attendee.getName());
        viewHolder.emailText.setText("Homepage: " + attendee.getHomepage());
        viewHolder.phoneText.setText("Contact Info: " + attendee.getContactInfo());

        Integer checkIns = attendee.getCheckedInEvents().get(event.getEventId());
        if (checkIns != null){
            viewHolder.checkInText.setText("Check Ins: " + checkIns.toString());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return attendees.size();
    }
}
