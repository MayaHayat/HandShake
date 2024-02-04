package com.example.handshake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterForDonationsISaved extends RecyclerView.Adapter<adapterForDonationsISaved.MyViewHolderSaved>{

    Context context;
    ArrayList<TakenDonations> savedDonationList;

    public adapterForDonationsISaved(Context context, ArrayList<TakenDonations> savedDonationList) {
        this.context = context;
        this.savedDonationList = savedDonationList;
    }

    @NonNull
    @Override
    public MyViewHolderSaved onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolderSaved(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSaved holder, int position) {
        TakenDonations donation = savedDonationList.get(position);
        holder.donationName.setText(donation.getDonationName());
        holder.donationInfo.setText(donation.getDonationInfo());
        holder.donorName.setText(donation.getDonorName());
        holder.donorPhone.setText(donation.getDonorPhone());
        holder.donationLocation.setText(donation.getDonationLocation());
    }

    @Override
    public int getItemCount() {
        return savedDonationList.size();
    }

    public static class MyViewHolderSaved extends RecyclerView.ViewHolder{

        TextView donationName, donationInfo, donorName, donorPhone, donationLocation;

        public MyViewHolderSaved(@NonNull View itemView) {
            super(itemView);

            donationName = itemView.findViewById(R.id.savedDonationName);
            donationInfo = itemView.findViewById(R.id.savedDonationInfo);
            donorName = itemView.findViewById(R.id.savedDonorName);
            donorPhone = itemView.findViewById(R.id.savedDonorPhone);
            donationLocation = itemView.findViewById(R.id.savedDonationLocation);

        }
    }

}
