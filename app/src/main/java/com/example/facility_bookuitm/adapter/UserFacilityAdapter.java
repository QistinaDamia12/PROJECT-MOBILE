package com.example.facility_bookuitm.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.R;
import com.example.facility_bookuitm.model.Facility;

import java.util.List;

public class UserFacilityAdapter extends RecyclerView.Adapter<UserFacilityAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvName, tvLoca, tvType, tvCapacity;
        ImageView imagePreview;

        ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvName = itemView.findViewById(R.id.tvName);
            tvLoca = itemView.findViewById(R.id.tvLoca);
            tvType = itemView.findViewById(R.id.tvType);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            imagePreview = itemView.findViewById(R.id.imagePreview);
        }
    }

    private final List<Facility> facilityList;
    private final Context context;

    public UserFacilityAdapter(Context context, List<Facility> facilityList) {
        this.context = context;
        this.facilityList = facilityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.admin_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Facility f = facilityList.get(position);

        holder.tvName.setText(f.getFacilityName());
        holder.tvLoca.setText(f.getFacilityLocation());
        holder.tvType.setText(f.getFacilityType());
        holder.tvCapacity.setText(f.getFacilityCapacity() + " People");
        holder.tvStatus.setText(f.getFacilityStatus());

        // Image
        String imgName = f.getFacilityPicture();
        if (imgName != null && !imgName.isEmpty()) {
            if (imgName.contains(".")) {
                imgName = imgName.substring(0, imgName.lastIndexOf("."));
            }
            int resID = context.getResources()
                    .getIdentifier(imgName, "drawable", context.getPackageName());

            holder.imagePreview.setImageResource(
                    resID != 0 ? resID : android.R.drawable.ic_menu_gallery
            );
        }

        // Status color
        Typeface font = ResourcesCompat.getFont(context, R.font.jersey_regular);
        holder.tvStatus.setTypeface(font);

        if ("Available".equalsIgnoreCase(f.getFacilityStatus())) {
            holder.tvStatus.setTextColor(
                    context.getResources().getColor(android.R.color.holo_green_dark)
            );
        } else if ("Maintenance".equalsIgnoreCase(f.getFacilityStatus())) {
            holder.tvStatus.setTextColor(
                    context.getResources().getColor(android.R.color.holo_red_dark)
            );
        }
    }

    @Override
    public int getItemCount() {
        return facilityList == null ? 0 : facilityList.size();
    }
}

