package com.example.facility_bookuitm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.facility_bookuitm.R;
import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.user_add_reservation;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class UserFacilityAdapter extends RecyclerView.Adapter<UserFacilityAdapter.ViewHolder> {

    private List<Facility> facilityListData;
    private Context mContext;

    public UserFacilityAdapter(Context context, List<Facility> listData) {
        this.mContext = context;
        this.facilityListData = listData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePreview;
        TextView tvName, tvStatus, tvLoca, tvType, tvCapacity;
        MaterialButton btnBook;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLoca = itemView.findViewById(R.id.tvLoca);
            tvType = itemView.findViewById(R.id.tvType);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            imagePreview = itemView.findViewById(R.id.imagePreview);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_reservationfacility_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Facility f = facilityListData.get(position);

        // Set text fields
        holder.tvName.setText(f.getFacilityName());
        holder.tvLoca.setText(f.getFacilityLocation());
        holder.tvType.setText(f.getFacilityType());
        holder.tvCapacity.setText(f.getFacilityCapacity() + " People");

        // Set status text
        String status = f.getFacilityStatus();
        holder.tvStatus.setText(status != null ? status : "Available");

        // Load image with Glide
        String imgName = f.getFacilityPicture();
        int resID = 0;
        if (imgName != null && !imgName.isEmpty()) {
            if (imgName.contains(".")) imgName = imgName.substring(0, imgName.lastIndexOf("."));
            resID = mContext.getResources().getIdentifier(imgName, "drawable", mContext.getPackageName());
        }
        if (resID != 0) {
            Glide.with(mContext)
                    .load(resID)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.imagePreview);
        } else {
            holder.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Button logic
        if (status != null && status.equalsIgnoreCase("MAINTENANCE")) {
            holder.btnBook.setEnabled(false);
            holder.btnBook.setAlpha(0.5f);
        } else {
            holder.btnBook.setEnabled(true);
            holder.btnBook.setAlpha(1f);
            holder.btnBook.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, user_add_reservation.class);
                intent.putExtra("facility_id", f.getFacilityID());
                intent.putExtra("facility_name", f.getFacilityName());
                intent.putExtra("facility_type", f.getFacilityType());
                intent.putExtra("facility_capacity", f.getFacilityCapacity());
                intent.putExtra("facility_location", f.getFacilityLocation());
                intent.putExtra("facility_image", f.getFacilityPicture());
                mContext.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return facilityListData != null ? facilityListData.size() : 0;
    }

    // Optional: update a facility in the list
    public void updateFacility(Facility updatedFacility) {
        if (facilityListData == null || updatedFacility == null) return;
        for (int i = 0; i < facilityListData.size(); i++) {
            Facility f = facilityListData.get(i);
            if (f.getFacilityID() == updatedFacility.getFacilityID()) {
                facilityListData.set(i, updatedFacility);
                notifyItemChanged(i);
                break;
            }
        }
    }
}
