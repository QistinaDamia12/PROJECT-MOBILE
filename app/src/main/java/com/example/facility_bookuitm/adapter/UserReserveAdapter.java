package com.example.facility_bookuitm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.facility_bookuitm.R;
import com.example.facility_bookuitm.model.Facility;
import com.example.facility_bookuitm.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class UserReserveAdapter extends RecyclerView.Adapter<UserReserveAdapter.ViewHolder> {

    private final Context mContext;
    private List<Reservation> reservationList;

    public UserReserveAdapter(Context context, List<Reservation> reservationList) {
        this.mContext = context;
        this.reservationList = reservationList != null ? reservationList : new ArrayList<>();
    }

    public void updateData(List<Reservation> newList) {
        reservationList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePreview;
        TextView tvName, tvType, tvDate, tvTime, tvPurpose, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePreview = itemView.findViewById(R.id.historyFacilityImage);
            tvName = itemView.findViewById(R.id.historyFacilityName);
            tvType = itemView.findViewById(R.id.historyFacilityType);
            tvDate = itemView.findViewById(R.id.historyDate);
            tvTime = itemView.findViewById(R.id.historyTime);
            tvPurpose = itemView.findViewById(R.id.historyPurpose);
            tvStatus = itemView.findViewById(R.id.historyStatus);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.user_list_history_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        Facility facility = reservation.getFacility();

        if (facility != null) {
            holder.tvName.setText(facility.getFacilityName() != null ? facility.getFacilityName() : "Unknown");
            holder.tvType.setText(facility.getFacilityType() != null ? facility.getFacilityType() : "-");

            String imgName = facility.getFacilityPicture();
            int resId = 0;
            if (imgName != null && !imgName.isEmpty()) {
                if (imgName.contains(".")) imgName = imgName.substring(0, imgName.lastIndexOf("."));
                resId = mContext.getResources().getIdentifier(imgName, "drawable", mContext.getPackageName());
            }

            Glide.with(mContext)
                    .load(resId != 0 ? resId : android.R.drawable.ic_menu_gallery)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.imagePreview);
        } else {
            holder.tvName.setText("Unknown Facility");
            holder.tvType.setText("-");
            holder.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.tvDate.setText("📅 Date: " + (reservation.getReserveDate() != null ? reservation.getReserveDate() : "-"));
        holder.tvTime.setText("⏰ Time: " + (reservation.getReserveTime() != null ? reservation.getReserveTime() : "-"));
        holder.tvPurpose.setText("🎯 Purpose: " + (reservation.getReservePurpose() != null ? reservation.getReservePurpose() : "-"));

        String status = reservation.getReserveStatus() != null ? reservation.getReserveStatus() : "PENDING";
        holder.tvStatus.setText(status);

        switch (status.toUpperCase()) {
            case "APPROVED":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#2E7D32"));
                holder.tvStatus.setTextColor(Color.WHITE);
                break;
            case "REJECTED":
                holder.tvStatus.setBackgroundColor(Color.parseColor("#D32F2F"));
                holder.tvStatus.setTextColor(Color.WHITE);
                break;
            default:
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FFA000"));
                holder.tvStatus.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }
}
