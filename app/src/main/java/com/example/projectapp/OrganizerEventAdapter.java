package com.example.projectapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrganizerEventAdapter extends RecyclerView.Adapter<OrganizerEventAdapter.ViewHolder> {
    private List<Event> events;
    private OnItemClickListener listener;

    public OrganizerEventAdapter(List<Event> events, OnItemClickListener listener) {
        this.events = events;
        this.listener = listener;
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
        holder.attendeeCountTextView.setText(String.valueOf(event.getAttendanceLimit())); // or any other field representing the count
        holder.itemView.setOnClickListener(v -> listener.onItemClick(event));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;
        TextView attendeeCountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            attendeeCountTextView = itemView.findViewById(R.id.attendeeCountTextView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }
}
