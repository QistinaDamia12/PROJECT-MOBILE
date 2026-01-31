package com.example.facility_bookuitm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facility_bookuitm.R;
import com.example.facility_bookuitm.model.Reservation;

import java.util.List;

public class ReservationHistoryAdapter extends RecyclerView.Adapter<ReservationHistoryAdapter.ViewHolder> {

    private Context context;
    private List<Reservation> reservationList;

    public ReservationHistoryAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_list_history_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation r = reservationList.get(position);

        holder.tvDate.setText("Date: " + r.getReserveDate());
        holder.tvTime.setText("Time: " + r.getReserveTime());
        holder.tvPurpose.setText("Purpose: " + r.getReservePurpose());
        holder.tvStatus.setText("Status: " + r.getReserveStatus());

//        if (r.getFacility() != null) {
//            holder.tvFacilityName.setText("Facility: " + r.getFacility().getFacilityName());
//        } else {
//            holder.tvFacilityName.setText("Facility ID: " + r.getFacility_id());
//        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFacilityName, tvDate, tvTime, tvPurpose, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFacilityName = itemView.findViewById(R.id.tvFacilityName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPurpose = itemView.findViewById(R.id.historyPurpose);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}

