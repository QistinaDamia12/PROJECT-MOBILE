package com.example.facility_bookuitm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FacilityAdapter extends RecyclerView.Adapter<com.example.facility_bookuitm.FacilityAdapter.ViewHolder>
{

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {
        public TextView tv;
        public TextView tvAuthor;
        public TextView tvDescription;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }

        @Override
        public boolean onLongClick(View view)
        {
            return false;
        }
    }
    @NonNull
    @Override
    public com.example.facility_bookuitm.FacilityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.facility_bookuitm.FacilityAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
