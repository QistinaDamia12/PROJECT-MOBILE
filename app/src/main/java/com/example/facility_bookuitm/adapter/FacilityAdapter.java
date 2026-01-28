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

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvStatus;
        public TextView tvName;
        public TextView tvLoca;
        public TextView tvType;
        public TextView tvCapacity;
        public ImageView imagePreview;

        public ViewHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvName = itemView.findViewById(R.id.tvName);
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

    private List<Facility> facilityListData;
    private Context mContext;
    private int currentPos = -1;

    public FacilityAdapter(Context context, List<Facility> listData) {
        this.facilityListData = listData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Facility f = facilityListData.get(position);

        holder.tvName.setText(f.getFacilityName());
        holder.tvStatus.setText(f.getFacilityStatus());
        holder.tvLoca.setText(f.getFacilityLocation());
        holder.tvType.setText(f.getFacilityType());
        holder.tvCapacity.setText(f.getFacilityCapacity() + " People");

        // gambo
        String imgName = f.getFacilityPicture();
        if (imgName != null && !imgName.isEmpty()) {
            // Remove extension if present (e.g., "f1.jpg" -> "f1")
            if (imgName.contains(".")) {
                imgName = imgName.substring(0, imgName.lastIndexOf("."));
            }
            int resID = mContext.getResources().getIdentifier(imgName, "drawable", mContext.getPackageName());
            if (resID != 0) {
                holder.imagePreview.setImageResource(resID);
            } else {
                holder.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery); // Fallback
            }
        }

        //status color
        Typeface jerseyFont = ResourcesCompat.getFont(mContext, R.font.jersey_regular);
        String status = f.getFacilityStatus();

        if (status != null) {

            holder.tvStatus.setTypeface(jerseyFont);

            if (status.equalsIgnoreCase("Available")) {
                holder.tvStatus.setTextColor(
                        mContext.getResources().getColor(android.R.color.holo_green_dark)
                );
            }
            else if (status.equalsIgnoreCase("Maintenance")) {
                holder.tvStatus.setTextColor(
                        mContext.getResources().getColor(android.R.color.holo_red_dark)
                );
            }
        }


    }

    @Override
    public int getItemCount() {
        return facilityListData != null ? facilityListData.size() : 0;
    }

    public Facility getSelectedItem() {
        if (currentPos >= 0 && currentPos < facilityListData.size()) {
            return facilityListData.get(currentPos);
        }
        return null;
    }
}
