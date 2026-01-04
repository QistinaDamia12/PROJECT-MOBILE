package com.example.facility_bookuitm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {
    private List<Facility> list;
    public FacilityAdapter(List<Facility> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facility, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Facility f = list.get(position);
        holder.name.setText(f.getName());
        holder.location.setText(f.getLocation());
        holder.img.setImageResource(f.getImageResId());
        holder.capacity.setText("Capacity: " + f.getCapacity() + " Pax");
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, location;
        ImageView img;
        TextView capacity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtFacilityName);
            location = itemView.findViewById(R.id.txtLocation);
            img = itemView.findViewById(R.id.imgFacility);
            capacity = itemView.findViewById(R.id.txtCapacity);
        }
    }
}
