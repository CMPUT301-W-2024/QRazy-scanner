package com.example.projectapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private List<Announcement> announcements;

    public AnnouncementAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_widget, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);

        holder.event.setText(announcement.getEvent());
        holder.organizer.setText(announcement.getOrganizer());
        holder.announcementContent.setText(announcement.getAnnouncement());
        holder.dateTime.setText(announcement.getDateTime());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }
}
