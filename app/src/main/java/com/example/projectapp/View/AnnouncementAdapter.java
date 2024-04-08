package com.example.projectapp.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.Model.Announcement;
import com.example.projectapp.R;

import java.util.List;

/**
 * The AnnouncementAdapter class manages the display of announcements within a RecyclerView.
 * It is responsible for binding announcement data to the views of each announcement item.
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private List<Announcement> announcements;

    /**
     * Initializes the adapter with a list of announcements.
     * @param announcements The list of Announcement objects to be displayed in the RecyclerView
     */
    public AnnouncementAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    /**
     *  The ViewHolder class represents an individual announcement item within the RecyclerView.
     *  It holds references to the UI elements used to display announcement information.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView event, organizer, announcementContent, dateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            event = (TextView) itemView.findViewById(R.id.announcementEvent);
            organizer = (TextView) itemView.findViewById(R.id.announcementOrganizer);
            announcementContent = (TextView) itemView.findViewById(R.id.announcementContent);
            dateTime = (TextView) itemView.findViewById(R.id.announcementDateTime);
        }
    }

    /**
     * Called by the RecyclerView to create a new ViewHolder.
     *
     * @param parent    The parent ViewGroup of the new ViewHolder.
     * @param viewType  The type of the new View).
     * @return          A new ViewHolder for displaying an announcement item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_widget, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by the RecyclerView to bind announcement data to a ViewHolder for display.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the announcement within the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);

        holder.event.setText(announcement.getEvent());
        holder.organizer.setText(announcement.getOrganizer());
        holder.announcementContent.setText(announcement.getAnnouncement());
        holder.dateTime.setText(announcement.getDateTime());
    }

    /**
     * Returns the total number of announcements to be displayed.
     * @return The size of the announcement list.
     */
    @Override
    public int getItemCount() {
        return announcements.size();
    }
}
