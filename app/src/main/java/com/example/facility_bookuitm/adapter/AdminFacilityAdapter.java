package com.example.facility_bookuitm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.facility_bookuitm.R;
import com.example.facility_bookuitm.model.Facility;

import java.util.List;

public class AdminFacilityAdapter extends RecyclerView.Adapter<AdminFacilityAdapter.ViewHolder> {

    private List<Facility> facilityListData;
    private Context mContext;
    private int currentPos = -1; // selected item position

    public AdminFacilityAdapter(Context context, List<Facility> listData) {
        this.mContext = context;
        this.facilityListData = listData;
    }

    // Get currently selected facility (optional)
    public Facility getSelectedItem() {
        if (currentPos >= 0 && facilityListData != null && currentPos < facilityListData.size()) {
            return facilityListData.get(currentPos);
        }
        return null;
    }

    public void updateFacility(Facility updatedFacility) {
        if (facilityListData == null || updatedFacility == null) return;

        for (int i = 0; i < facilityListData.size(); i++) {
            Facility f = facilityListData.get(i);
            if (f.getFacilityID() == updatedFacility.getFacilityID()) { // match by ID
                facilityListData.set(i, updatedFacility);
                notifyItemChanged(i); // refresh only this item
                break;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        ImageView imagePreview;
        TextView tvName, tvStatus, tvLoca, tvType, tvCapacity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLoca = itemView.findViewById(R.id.tvLoca);
            tvType = itemView.findViewById(R.id.tvType);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            imagePreview = itemView.findViewById(R.id.imagePreview);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition();
            return false;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Facility f = facilityListData.get(position);

        holder.tvName.setText(f.getFacilityName());
        holder.tvLoca.setText(f.getFacilityLocation());
        holder.tvType.setText(f.getFacilityType());
        holder.tvCapacity.setText(f.getFacilityCapacity() + " People");

        // ===== STATUS TEXT + COLOR =====
        String status = f.getFacilityStatus();
        holder.tvStatus.setText(status);

        if (status != null) {
            if (status.equalsIgnoreCase("MAINTENANCE")) {
                holder.tvStatus.setTextColor(
                        mContext.getResources().getColor(android.R.color.holo_red_dark)
                );
            } else if (status.equalsIgnoreCase("AVAILABLE")) {
                holder.tvStatus.setTextColor(
                        mContext.getResources().getColor(android.R.color.holo_green_dark)
                );
            } else {
                holder.tvStatus.setTextColor(
                        mContext.getResources().getColor(android.R.color.darker_gray)
                );
            }
        }

        // ===== IMAGE LOADING WITH GLIDE =====
        String imgName = f.getFacilityPicture();
        int resID = 0;

        if (imgName != null && !imgName.isEmpty()) {
            if (imgName.contains(".")) {
                imgName = imgName.substring(0, imgName.lastIndexOf("."));
            }
            resID = mContext.getResources()
                    .getIdentifier(imgName, "drawable", mContext.getPackageName());
        }

        // Load with Glide to ensure update
        if (resID != 0) {
            Glide.with(mContext)
                    .load(resID)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.imagePreview);
        } else {
            holder.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return facilityListData != null ? facilityListData.size() : 0;
    }
}

