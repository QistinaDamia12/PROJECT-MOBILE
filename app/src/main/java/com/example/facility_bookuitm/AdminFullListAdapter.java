//package com.example.facility_bookuitm;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//public class AdminFullListAdapter extends RecyclerView.Adapter<AdminFullListAdapter.ViewHolder> {
//    private List<History> requestList; // Reusing your History model for data structure
//
//    public AdminFullListAdapter(List<History> requestList) {
//        this.requestList = requestList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_request, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        History request = requestList.get(position);
//        holder.name.setText(request.getFacilityName());
//        holder.details.setText("Date: " + request.getDate());
//
//        holder.btnApprove.setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), "Approved: " + request.getFacilityName(), Toast.LENGTH_SHORT).show();
//            requestList.remove(position);
//            notifyItemRemoved(position);
//        });
//
//        holder.btnReject.setOnClickListener(v -> {
//            Toast.makeText(v.getContext(), "Rejected: " + request.getFacilityName(), Toast.LENGTH_SHORT).show();
//            requestList.remove(position);
//            notifyItemRemoved(position);
//        });
//    }
//
//    @Override
//    public int getItemCount() { return requestList.size(); }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView name, details;
//        Button btnApprove, btnReject;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.reqFacilityName);
//            details = itemView.findViewById(R.id.reqDetails);
//            btnApprove = itemView.findViewById(R.id.btnApprove);
//            btnReject = itemView.findViewById(R.id.btnReject);
//        }
//    }
//}