package com.example.handshake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapterForMySavedDonations extends RecyclerView.Adapter<adapterForMySavedDonations.MyViewHolder2> {

    Context context2;
    ArrayList<SavedDonation> savedDonationsList;

    public adapterForMySavedDonations(Context context2, ArrayList<SavedDonation> savedDonationsList) {
        this.context2 = context2;
        this.savedDonationsList = savedDonationsList;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context2).inflate(R.layout.mysaveddonations, parent,false);
        return new MyViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        SavedDonation donation = savedDonationsList.get(position);
        holder.donationName.setText(donation.getDonationName());
        holder.donationInfo.setText(donation.getDonationInfo());
        holder.donationLocation.setText(donation.getLocation());
        holder.donorName.setText(donation.getDonorName());
        holder.donorPhone.setText(donation.getDonorPhone());

    }

    @Override
    public int getItemCount() {
        return savedDonationsList.size();
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder{

        TextView donationName, donationInfo, donationLocation, donorName, donorPhone;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

            donationName = itemView.findViewById(R.id.savedDonationName);
            donationInfo = itemView.findViewById(R.id.savedDonationInfo);
            donationLocation = itemView.findViewById(R.id.savedDonationLocation);
            donorName = itemView.findViewById(R.id.savedDonorName);
            donorPhone = itemView.findViewById(R.id.savedDonorPhone);
        }
    }
}