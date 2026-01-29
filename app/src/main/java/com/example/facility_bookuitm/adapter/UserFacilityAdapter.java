package com.example.facility_bookuitm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.R;
import com.example.facility_bookuitm.model.Facility;

import java.util.List;

public class UserFacilityAdapter extends RecyclerView.Adapter<UserFacilityAdapter.ViewHolder> {

    private final List<Facility> facilityListData;
    private final Context mContext;
    private final FacilityClickListener clickListener;

    public interface FacilityClickListener {
        void onFacilityClick(Facility facility);
    }

    public UserFacilityAdapter(Context context, List<Facility> listData, FacilityClickListener listener) {
        this.facilityListData = listData;
        this.mContext = context;
        this.clickListener = listener;
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

        // Set texts
        holder.tvName.setText(f.getFacilityName());
        holder.tvLoca.setText(f.getFacilityLocation());
        holder.tvType.setText(f.getFacilityType());
        holder.tvCapacity.setText(f.getFacilityCapacity() + " People");

        // Set status text & color
        String status = f.getFacilityStatus();
        holder.tvStatus.setText(status);
        boolean isUnderMaintenance = false;

        if (status != null) {
            switch (status.toUpperCase()) {
                case "MAINTENANCE":
                    holder.tvStatus.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
                    isUnderMaintenance = true;
                    break;
                case "AVAILABLE":
                    holder.tvStatus.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
                    break;
                default:
                    holder.tvStatus.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
            }
        }

        // Load image
        String imgName = f.getFacilityPicture();
        if (imgName != null && !imgName.isEmpty()) {
            if (imgName.contains(".")) imgName = imgName.substring(0, imgName.lastIndexOf("."));
            int resID = mContext.getResources().getIdentifier(imgName, "drawable", mContext.getPackageName());
            if (resID != 0) holder.imagePreview.setImageResource(resID);
            else holder.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // ===== Handle reserve button click =====
        if (isUnderMaintenance) {
            // Disable button and blur item design
            holder.btnBook.setEnabled(false);
            holder.btnBook.setAlpha(0.5f);
            holder.itemView.setAlpha(0.5f); // Dim the whole item card
            holder.itemView.setOnClickListener(v ->
                    Toast.makeText(mContext, "This facility is under maintenance and cannot be reserved.", Toast.LENGTH_SHORT).show()
            );
        } else {
            holder.btnBook.setEnabled(true);
            holder.btnBook.setAlpha(1f);
            holder.itemView.setAlpha(1f);
            holder.btnBook.setOnClickListener(v -> {
                if (clickListener != null) clickListener.onFacilityClick(f);
            });
            // Optional: also make entire card clickable
            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) clickListener.onFacilityClick(f);
            });
        }
    }

    @Override
    public int getItemCount() {
        return facilityListData != null ? facilityListData.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvStatus, tvName, tvLoca, tvType, tvCapacity;
        final ImageView imagePreview;
        final Button btnBook;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvName = itemView.findViewById(R.id.tvName);
            tvLoca = itemView.findViewById(R.id.tvLoca);
            tvType = itemView.findViewById(R.id.tvType);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            imagePreview = itemView.findViewById(R.id.imagePreview);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}



